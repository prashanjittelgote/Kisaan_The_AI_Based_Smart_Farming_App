package com.mountreachsolution.kisaan;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuInflater;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        ImageView gif = findViewById(R.id.splashGif);

        Glide.with(this)
                .asGif()
                .load(R.drawable.kisan_gif)
                .into(gif);

        new Handler().postDelayed(() -> {

            // Get SharedPreferences
            SharedPreferences sp = getSharedPreferences("UserData", MODE_PRIVATE);
            boolean isLoggedIn = sp.getBoolean("isLoggedIn", false);

            if (isLoggedIn) {
                // If already logged in, go to HomeActivity (or main dashboard)
                startActivity(new Intent(MainActivity.this, HomeActivity.class));
            } else {
                // If not logged in, go to LoginActivity
                startActivity(new Intent(MainActivity.this, KsLoginActivity.class));
            }

            finish(); // Close splash screen

        }, 4000); // 3-second delay

    }
}