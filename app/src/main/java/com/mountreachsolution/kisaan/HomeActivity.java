package com.mountreachsolution.kisaan;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.Window;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.mountreachsolution.kisaan.Fragment.ChatFragment;
import com.mountreachsolution.kisaan.Fragment.HomeFragment;
import com.mountreachsolution.kisaan.Fragment.KisanDootFragment;
import com.mountreachsolution.kisaan.Fragment.MarketFragment;
import com.mountreachsolution.kisaan.Fragment.WeatherFragment;

public class HomeActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {
    BottomNavigationView bottomNavigationView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Window window = getWindow();

        getWindow().setNavigationBarColor(ContextCompat.getColor(HomeActivity.this,R.color.white));
        bottomNavigationView = findViewById(R.id.bottomnevigatiomuserhome);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);

        bottomNavigationView.setSelectedItemId(R.id.nav_home);
    }
    HomeFragment homeFragment = new HomeFragment();

    KisanDootFragment profil = new KisanDootFragment();
    WeatherFragment weatherFragment = new WeatherFragment();
    ChatFragment agent = new ChatFragment();
    MarketFragment marketFragment = new MarketFragment();
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.nav_weather){
            getSupportFragmentManager().beginTransaction().replace(R.id.FrameLayoutuserhome,weatherFragment).commit();
        }else if(item.getItemId()==R.id.nav_chat){
            getSupportFragmentManager().beginTransaction().replace(R.id.FrameLayoutuserhome,agent).commit();
        }else if(item.getItemId()==R.id.nav_home){
            getSupportFragmentManager().beginTransaction().replace(R.id.FrameLayoutuserhome,homeFragment).commit();
        }else if(item.getItemId()==R.id.nav_market){
            getSupportFragmentManager().beginTransaction().replace(R.id.FrameLayoutuserhome,marketFragment).commit();
        }else if(item.getItemId()==R.id.nav_Kisandoot){
            getSupportFragmentManager().beginTransaction().replace(R.id.FrameLayoutuserhome,profil).commit();
        }
        return true;
    }

}