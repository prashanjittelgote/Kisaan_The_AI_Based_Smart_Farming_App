package com.mountreachsolution.kisaan;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.mountreachsolution.kisaan.common.Urls;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class KsRegistrationActivity extends AppCompatActivity {
     TextInputLayout layoutName, layoutPhone, layoutEmail,
            layoutPassword, layoutConfirmPassword;

    // EditTexts
     TextInputEditText etFullName, etPhone, etEmail,
            etPassword, etConfirmPassword;

     Button btnRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ks_registration);

        initViews();
        setListeners();

    }
    private void initViews() {

        layoutName = findViewById(R.id.input_layout_name);
        layoutPhone = findViewById(R.id.input_layout_phone);
        layoutEmail = findViewById(R.id.input_layout_email);
        layoutPassword = findViewById(R.id.input_layout_password);
        layoutConfirmPassword = findViewById(R.id.input_layout_password1);

        etFullName = findViewById(R.id.etFullName);
        etPhone = findViewById(R.id.etPhone);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        etConfirmPassword = findViewById(R.id.etConfirmPassword);

        btnRegister = findViewById(R.id.btnRegister);
    }
    private void setListeners() {
        btnRegister.setOnClickListener(v -> validateAndRegister());
    }


    private void validateAndRegister() {

        // Get Data
        String name = etFullName.getText().toString().trim();
        String phone = etPhone.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        String confirmPassword = etConfirmPassword.getText().toString().trim();

        // Clear Previous Errors
        layoutName.setError(null);
        layoutPhone.setError(null);
        layoutEmail.setError(null);
        layoutPassword.setError(null);
        layoutConfirmPassword.setError(null);

        boolean isValid = true;

        // ===== Name Validation =====
        if (TextUtils.isEmpty(name)) {
            layoutName.setError("Name is required");
            isValid = false;

        } else if (name.length() < 3) {
            layoutName.setError("Name must be at least 3 characters");
            isValid = false;
        }

        // ===== Phone Validation =====
        if (TextUtils.isEmpty(phone)) {
            layoutPhone.setError("Mobile number is required");
            isValid = false;

        } else if (phone.length() != 10) {
            layoutPhone.setError("Enter valid 10 digit mobile number");
            isValid = false;

        } else if (!phone.matches("[6-9][0-9]{9}")) {
            layoutPhone.setError("Invalid Indian mobile number");
            isValid = false;
        }

        // ===== Email Validation =====
        if (TextUtils.isEmpty(email)) {
            layoutEmail.setError("Email is required");
            isValid = false;

        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            layoutEmail.setError("Invalid email format");
            isValid = false;
        }

        // ===== Password Validation =====
        if (TextUtils.isEmpty(password)) {
            layoutPassword.setError("Password is required");
            isValid = false;

        } else if (password.length() < 6) {
            layoutPassword.setError("Password must be at least 6 characters");
            isValid = false;
        }

        // ===== Confirm Password Validation =====
        if (TextUtils.isEmpty(confirmPassword)) {
            layoutConfirmPassword.setError("Confirm your password");
            isValid = false;

        } else if (!password.equals(confirmPassword)) {
            layoutConfirmPassword.setError("Passwords do not match");
            isValid = false;
        }

        // If All Valid → Call Register Method
        if (isValid) {
            registerMethod(name, phone, email, password);
        }
    }

    // ===== Registration Method =====
    private void registerMethod(String name, String phone,
                                String email, String password) {

        RequestQueue queue = Volley.newRequestQueue(this);

        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                Urls.userregiter,
                response -> {

                    try {
                        JSONObject obj = new JSONObject(response);

                        int success = obj.getInt("success");
                        String message = obj.getString("message");

                        Toast.makeText(KsRegistrationActivity.this,
                                message,
                                Toast.LENGTH_LONG).show();

                        if (success == 1) {
                            // Registration success
                            finish(); // go back to login
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(KsRegistrationActivity.this,
                                "JSON Error",
                                Toast.LENGTH_SHORT).show();
                    }

                },
                error -> Toast.makeText(KsRegistrationActivity.this,
                        "Volley Error: " + error.getMessage(),
                        Toast.LENGTH_LONG).show()
        ) {

            @Override
            protected Map<String, String> getParams() {

                Map<String, String> params = new HashMap<>();
                params.put("name", name);
                params.put("email", email);
                params.put("mobileno", phone);
                params.put("password", password);

                return params;
            }
        };

        queue.add(stringRequest);
    }
}
