package com.mountreachsolution.kisaan;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.mountreachsolution.kisaan.common.Urls;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class KsLoginActivity extends AppCompatActivity {
    // Declare Views
    private TextInputLayout inputLayoutUsername, inputLayoutPassword;
    private TextInputEditText editTextUsername, editTextPassword;
    private Button buttonLogin;
    private MaterialButton buttonGoogleSignIn;
    private TextView textRegisterLink;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ks_login);

        // Initialize Views
        initViews();

        // Set Click Listeners
        setListeners();

    }
    private void initViews() {
        inputLayoutUsername = findViewById(R.id.input_layout_username);
        inputLayoutPassword = findViewById(R.id.input_layout_password);

        editTextUsername = findViewById(R.id.edit_text_username);
        editTextPassword = findViewById(R.id.edit_text_password);

        buttonLogin = findViewById(R.id.button_login);

        textRegisterLink = findViewById(R.id.text_register_link);
    }

    private void setListeners() {

        buttonLogin.setOnClickListener(view -> validateAndLogin());


        textRegisterLink.setOnClickListener(view -> {
            Toast.makeText(KsLoginActivity.this,
                    "Redirect to Register Screen",
                    Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, KsRegistrationActivity.class));
        });
    }
    private void validateAndLogin() {

        // Get Data from EditText
        String mobile = editTextUsername.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();

        // Reset Errors
        inputLayoutUsername.setError(null);
        inputLayoutPassword.setError(null);

        // Validation Flag
        boolean isValid = true;

        // Validate Mobile Number
        if (TextUtils.isEmpty(mobile)) {
            inputLayoutUsername.setError("Mobile number is required");
            isValid = false;

        } else if (mobile.length() != 10) {
            inputLayoutUsername.setError("Enter valid 10 digit mobile number");
            isValid = false;

        } else if (!mobile.matches("[6-9][0-9]{9}")) {
            inputLayoutUsername.setError("Invalid mobile format");
            isValid = false;
        }

        // Validate Password
        if (TextUtils.isEmpty(password)) {
            inputLayoutPassword.setError("Password is required");
            isValid = false;

        } else if (password.length() < 6) {
            inputLayoutPassword.setError("Password must be at least 6 characters");
            isValid = false;
        }

        // If Valid → Call Login Method
        if (isValid) {
            loginMethod(mobile, password);
        }
    }
    // Your Login Method
    private void loginMethod(String mobile, String password) {



        StringRequest request = new StringRequest(
                Request.Method.POST,
                Urls.getLogin,
                response -> {

                    try {
                        JSONObject obj = new JSONObject(response);
                        int success = obj.getInt("success");
                        String message = obj.getString("message");

                        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();

                        if (success == 1) {

                            JSONObject user = obj.getJSONObject("user");

                            String id = user.getString("id");
                            String name = user.getString("name");
                            String email = user.getString("email");
                            String mobileno = user.getString("mobileno");

                            // Example: Save in SharedPreferences
                            SharedPreferences sp = getSharedPreferences("UserData", MODE_PRIVATE);
                            SharedPreferences.Editor editor = sp.edit();
                            editor.putString("id", id);
                            editor.putString("name", name);
                            editor.putString("email", email);
                            editor.putString("mobileno", mobileno);
                            editor.putBoolean("isLoggedIn", true);
                            editor.apply();

                            // Move to HomeActivity
                            startActivity(new Intent(KsLoginActivity.this, MainActivity.class));
                            // finish();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                },
                error -> Toast.makeText(this,
                        "Volley Error: " + error.getMessage(),
                        Toast.LENGTH_LONG).show()
        ) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("mobileno", mobile);
                params.put("password", password);
                return params;
            }
        };

        Volley.newRequestQueue(this).add(request);



    }


}
