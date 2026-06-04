package com.prashanjit.kisaan.ProfilrAct;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.prashanjit.kisaan.R;

public class ReportIssueActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_issue);

        EditText etSubject = findViewById(R.id.etSubject);
        EditText etDescription = findViewById(R.id.etDescription);
        Button btnSubmit = findViewById(R.id.btnSubmitIssue);

        btnSubmit.setOnClickListener(v -> {

            String subject = etSubject.getText().toString().trim();
            String description = etDescription.getText().toString().trim();

            if(subject.isEmpty() || description.isEmpty()){
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            Intent emailIntent = new Intent(Intent.ACTION_SEND);
            emailIntent.setType("message/rfc822");
            emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{"support@yourapp.com"});
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, "App Issue: " + subject);
            emailIntent.putExtra(Intent.EXTRA_TEXT, description);

            startActivity(Intent.createChooser(emailIntent, "Send Email"));
        });

    }
}