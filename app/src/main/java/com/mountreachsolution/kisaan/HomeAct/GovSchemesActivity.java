package com.mountreachsolution.kisaan.HomeAct;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import com.mountreachsolution.kisaan.R;
import androidx.appcompat.app.AppCompatActivity;

public class GovSchemesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gov_schemes);

        findViewById(R.id.btn_pmkisan).setOnClickListener(v -> openWeb("https://pmkisan.gov.in/"));
        findViewById(R.id.btn_pmfby).setOnClickListener(v -> openWeb("https://pmfby.gov.in/"));
        findViewById(R.id.btn_soil).setOnClickListener(v -> openWeb("https://soilhealth.dac.gov.in/"));
        findViewById(R.id.btn_enam).setOnClickListener(v -> openWeb("https://www.enam.gov.in/"));
        findViewById(R.id.btn_kcc).setOnClickListener(v -> openWeb("https://www.myscheme.gov.in/schemes/kcc"));
        findViewById(R.id.btn_nabard).setOnClickListener(v -> openWeb("https://www.nabard.org/"));
        findViewById(R.id.btn_agriinfra).setOnClickListener(v -> openWeb("https://agriinfra.dac.gov.in/"));
        findViewById(R.id.btn_pmkmy).setOnClickListener(v -> openWeb("https://maandhan.in/"));
    }

    private void openWeb(String url) {
        Intent intent = new Intent(this, WebViewActivity.class);
        intent.putExtra("url", url);
        startActivity(intent);
    }
}