package com.prashanjit.kisaan.Fragment;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.prashanjit.kisaan.Adapter.ForecastAdapter;
import com.prashanjit.kisaan.R;
import com.prashanjit.kisaan.network.ForecastResponse;
import com.prashanjit.kisaan.network.WeatherApi;
import com.prashanjit.kisaan.network.WeatherResponse;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class WeatherFragment extends Fragment {
    TextView txtCity, txtTemperature, txtCondition,
            txtHumidity, txtWind, txtTime;
    ImageView imgWeatherIcon;
    RecyclerView recyclerSevenDays;

    FusedLocationProviderClient fusedLocationClient;

    String API_KEY = "7e8b42ed96f75f79e3b78d0162eb2a06";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_weather, container, false);
        txtCity = view.findViewById(R.id.txtCity);
        txtTemperature = view.findViewById(R.id.txtTemperature);
        txtCondition = view.findViewById(R.id.txtCondition);
        txtHumidity = view.findViewById(R.id.txtHumidity);
        txtWind = view.findViewById(R.id.txtWind);
        txtTime = view.findViewById(R.id.txtTime);
        imgWeatherIcon = view.findViewById(R.id.imgWeatherIcon);
        recyclerSevenDays = view.findViewById(R.id.recyclerSevenDays);

        recyclerSevenDays.setLayoutManager(new LinearLayoutManager(getContext()));

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity());

        if (ActivityCompat.checkSelfPermission(requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            requestPermissions(
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    1);
            return view;
        }

        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(location -> {
                    if (location != null) {
                        loadWeather(location.getLatitude(), location.getLongitude());
                    }
                });

        return view;
    }
    private void loadWeather(double lat, double lon) {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.openweathermap.org/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        WeatherApi api = retrofit.create(WeatherApi.class);

        // Current Weather
        api.getWeatherByCoordinates(lat, lon, API_KEY, "metric")
                .enqueue(new Callback<WeatherResponse>() {
                    @Override
                    public void onResponse(Call<WeatherResponse> call, Response<WeatherResponse> response) {

                        if (response.isSuccessful() && response.body() != null) {

                            WeatherResponse data = response.body();

                            txtCity.setText(data.getName());
                            txtTemperature.setText(Math.round(data.getMain().getTemp()) + "°C");
                            txtCondition.setText(data.getWeather().get(0).getMain());
                            txtHumidity.setText(data.getMain().getHumidity() + "%");
                            txtWind.setText(data.getWind().getSpeed() + " km/h");

                            SimpleDateFormat format = new SimpleDateFormat("hh:mm a", Locale.getDefault());
                            txtTime.setText(format.format(new Date()));

                            // ✅ Day/Night icon logic
                            long currentTime = System.currentTimeMillis() / 1000L;
                            long sunrise = data.getSys().getSunrise();
                            long sunset = data.getSys().getSunset();
                            boolean isDay = currentTime >= sunrise && currentTime <= sunset;

                            String weather = data.getWeather().get(0).getMain().toLowerCase();
                            int iconRes;

                            if (weather.contains("cloud")) iconRes = isDay ? R.drawable.cloud : R.drawable.cloud_night;
                            else if (weather.contains("rain")) iconRes = isDay ? R.drawable.rain : R.drawable.rain_night;
                            else if (weather.contains("snow")) iconRes = isDay ? R.drawable.snow : R.drawable.snow_night;
                            else if (weather.contains("clear")) iconRes = isDay ? R.drawable.sun : R.drawable.moon;
                            else iconRes = isDay ? R.drawable.sun : R.drawable.moon;

                            imgWeatherIcon.setImageResource(iconRes);
                        }
                    }

                    @Override
                    public void onFailure(Call<WeatherResponse> call, Throwable t) {
                        t.printStackTrace();
                    }
                });

        // 7-Day Forecast
        api.getForecastByCoordinates(lat, lon, API_KEY, "metric")
                .enqueue(new Callback<ForecastResponse>() {
                    @Override
                    public void onResponse(Call<ForecastResponse> call, Response<ForecastResponse> response) {

                        if (response.isSuccessful() && response.body() != null) {

                            Map<String, ForecastResponse.ForecastItem> dailyForecast = new LinkedHashMap<>();
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

                            for (ForecastResponse.ForecastItem item : response.body().getList()) {
                                String date = sdf.format(new Date(item.getDt() * 1000L));
                                if (!dailyForecast.containsKey(date)) {
                                    dailyForecast.put(date, item);
                                }
                            }

                            List<ForecastResponse.ForecastItem> oneRowPerDay = new ArrayList<>(dailyForecast.values());
                            ForecastAdapter adapter = new ForecastAdapter(oneRowPerDay);
                            recyclerSevenDays.setAdapter(adapter);
                        }
                    }

                    @Override
                    public void onFailure(Call<ForecastResponse> call, Throwable t) {
                        t.printStackTrace();
                    }
                });
    }
}