package com.prashanjit.kisaan.Adapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.prashanjit.kisaan.R;
import com.prashanjit.kisaan.HomeAct.ForecastDetailsActivity;
import com.prashanjit.kisaan.network.ForecastResponse;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ForecastAdapter extends RecyclerView.Adapter<ForecastAdapter.ViewHolder> {

    private List<ForecastResponse.ForecastItem> forecastList;

    public ForecastAdapter(List<ForecastResponse.ForecastItem> forecastList) {
        this.forecastList = forecastList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_forecast, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        ForecastResponse.ForecastItem item = forecastList.get(position);

        long dt = item.getDt() * 1000L;
        String day = new SimpleDateFormat("EEE", Locale.getDefault()).format(new Date(dt));
        String date = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date(dt));

        holder.txtDay.setText(day + " " + date); // Full date with year
        holder.txtDayTemp.setText(Math.round(item.getMain().getTemp()) + "°C");

        String weather = item.getWeather().get(0).getMain().toLowerCase();
        boolean isDay = "d".equalsIgnoreCase(item.getSys().getPod());

        int iconRes;
        if(weather.contains("cloud")) iconRes = isDay ? R.drawable.cloud : R.drawable.cloud_night;
        else if(weather.contains("rain")) iconRes = isDay ? R.drawable.rain : R.drawable.rain_night;
        else if(weather.contains("snow")) iconRes = isDay ? R.drawable.snow : R.drawable.snow_night;
        else iconRes = isDay ? R.drawable.sun : R.drawable.moon;

        holder.imgDayIcon.setImageResource(iconRes);

        // Click → ForecastDetailsActivity
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), ForecastDetailsActivity.class);
            intent.putExtra("day", day + " " + date);
            intent.putExtra("temp", item.getMain().getTemp());
            intent.putExtra("humidity", item.getMain().getHumidity());
            intent.putExtra("condition", item.getWeather().get(0).getMain());
            intent.putExtra("pod", item.getSys().getPod());
            v.getContext().startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return forecastList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtDay, txtDayTemp;
        ImageView imgDayIcon;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtDay = itemView.findViewById(R.id.txtDay);
            txtDayTemp = itemView.findViewById(R.id.txtDayTemp);
            imgDayIcon = itemView.findViewById(R.id.imgDayIcon);
        }
    }
}