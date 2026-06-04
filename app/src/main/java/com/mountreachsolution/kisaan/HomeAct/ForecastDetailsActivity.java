package com.mountreachsolution.kisaan.HomeAct;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.mountreachsolution.kisaan.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ForecastDetailsActivity extends AppCompatActivity {
    TextView txtDate, txtTime, txtTemp, txtCondition, txtHumidity, txtWind;
    ImageView imgWeatherIcon;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forecast_details);
        txtDate = findViewById(R.id.txtDate);
        txtTime = findViewById(R.id.txtTime);
        txtTemp = findViewById(R.id.txtTemp);
        txtCondition = findViewById(R.id.txtCondition);
        txtHumidity = findViewById(R.id.txtHumidity);
        txtWind = findViewById(R.id.txtWind);

        // Get Intent Data safely
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String day = extras.getString("day", "N/A");
            double temp = extras.getDouble("temp", 0);
            int humidity = extras.getInt("humidity", 0);
            String condition = extras.getString("condition", "N/A");
            String pod = extras.getString("pod", "d"); // day/night

            // Set Date
            txtDate.setText(day);

            // Set Current Time
            SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a", Locale.getDefault());
            txtTime.setText(sdf.format(new Date()));

            // Temperature & Condition
            txtTemp.setText(Math.round(temp) + "°C");
            txtCondition.setText(condition);

            // Humidity & Wind
            txtHumidity.setText("Humidity: " + humidity + "%");

            // Wind - optional, pass via intent if available
            String wind = extras.containsKey("wind") ? extras.getString("wind") : "0 km/h";
            txtWind.setText("Wind: " + wind);

            // Weather Icon
            int iconRes;
            String weather = condition.toLowerCase();
            boolean isDay = "d".equalsIgnoreCase(pod);

            if (weather.contains("cloud")) iconRes = isDay ? R.drawable.cloud : R.drawable.cloud_night;
            else if (weather.contains("rain")) iconRes = isDay ? R.drawable.rain : R.drawable.rain_night;
            else if (weather.contains("snow")) iconRes = isDay ? R.drawable.snow : R.drawable.snow_night;
            else iconRes = isDay ? R.drawable.sun : R.drawable.moon;

            // Set icon if drawable exists
            txtCondition.post(() -> {
                if (iconRes != 0) {
                    if (findViewById(R.id.imgWeatherIcon) == null) return;
                    imgWeatherIcon = findViewById(R.id.imgWeatherIcon);
                    imgWeatherIcon.setImageResource(iconRes);
                }
            });
        }
    }
}