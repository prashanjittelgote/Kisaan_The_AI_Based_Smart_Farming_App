package com.prashanjit.kisaan.ProfilrAct;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.google.android.material.textfield.TextInputEditText;
import com.prashanjit.kisaan.R;
import com.prashanjit.kisaan.common.Urls;
import com.prashanjit.kisaan.common.VolleyMultipartRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class UpdateProfileActivity extends AppCompatActivity {
    CircleImageView profileImage;
    Button btnAddImage, btnUpdate;

    TextInputEditText etName, etEmail, etMobile;
    Bitmap bitmap;
    Uri filepath;
    private int pick_image_request = 789;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profile);
        getWindow().setNavigationBarColor(ContextCompat.getColor(UpdateProfileActivity.this,R.color.white));
        SharedPreferences sp = getSharedPreferences("UserData", MODE_PRIVATE);
        String name = sp.getString("name", "User");
        String job = sp.getString("role", "Farmer");
        String mobbile = sp.getString("mobileno", "Farmer");
        getUserProfile(mobbile);

        profileImage = findViewById(R.id.profileImage);
        btnAddImage = findViewById(R.id.btnAddImage);
        btnUpdate = findViewById(R.id.btnUpdate);

        etName = findViewById(R.id.etName);
        etEmail = findViewById(R.id.etEmail);
        etMobile = findViewById(R.id.etMobile);

        // Mobile number cannot be edited
        etMobile.setEnabled(false);
        btnUpdate.setOnClickListener(v -> {

            String name1 = etName.getText().toString().trim();
            String email = etEmail.getText().toString().trim();
            String mobile = etMobile.getText().toString().trim();
            updateProfile(name1,email,mobile);

            // Here you will call update API
        });
        btnAddImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SelectUserProfileImage();
            }
        });



    }
    private void SelectUserProfileImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Image For Profile"), pick_image_request);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == pick_image_request && resultCode == RESULT_OK && data != null) {
            filepath = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filepath);
                profileImage.setImageBitmap(bitmap);


            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    private void UserImageSaveToDatabase(Bitmap bitmap, String strTitle) {
        VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST, Urls.updateImage,
                response -> Toast.makeText(UpdateProfileActivity.this, "Image saved as profile for " + strTitle, Toast.LENGTH_SHORT).show(),
                error -> {
                    String errorMsg = error.getMessage();
                    if (error.networkResponse != null && error.networkResponse.data != null) {
                        errorMsg = new String(error.networkResponse.data);
                    }
                    Log.e("UploadError", errorMsg);
                    Toast.makeText(this, "Upload Error: " + errorMsg, Toast.LENGTH_LONG).show();
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parms = new HashMap<>();
                parms.put("tags", strTitle);
                return parms;
            }

            @Override
            protected Map<String, DataPart> getByteData() throws AuthFailureError {
                Map<String, DataPart> parms = new HashMap<>();
                long imagename = System.currentTimeMillis();
                parms.put("pic", new DataPart(imagename + ".jpeg", getFileDataFromBitmap(bitmap)));
                return parms;
            }
        };

        Volley.newRequestQueue(this).add(volleyMultipartRequest);
    }
    private byte[] getFileDataFromBitmap(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 80, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }

    private void updateProfile(String name, String email, String mobileno) {

        StringRequest request = new StringRequest(Request.Method.POST, Urls.updateUsser,

                response -> {
                    Toast.makeText(this, response, Toast.LENGTH_LONG).show();

                    // Upload image after profile update
                    UserImageSaveToDatabase(bitmap, mobileno);
                },

                error -> {
                    Toast.makeText(this, error.getMessage(), Toast.LENGTH_LONG).show();
                }) {

            @Override
            protected Map<String, String> getParams() {

                Map<String, String> params = new HashMap<>();
                params.put("mobileno", mobileno);
                params.put("name", name);
                params.put("email", email);

                return params;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(request);
    }
    private void getUserProfile(String mobile) {





        StringRequest request = new StringRequest(Request.Method.POST, Urls.getUsser,
                response -> {

                    try {

                        JSONObject jsonObject = new JSONObject(response);

                        int success = jsonObject.getInt("success");

                        if (success == 1) {

                            JSONObject user = jsonObject.getJSONObject("user");

                            String name = user.getString("name");
                            String email = user.getString("email");
                            String mobile1 = user.getString("mobileno");
                            String image = user.getString("image");

                            etName.setText(name);
                            etEmail.setText(email);
                            etMobile.setText(mobile);

                            String imageUrl = Urls.imageAddress + image;

                            Glide.with(UpdateProfileActivity.this)
                                    .load(imageUrl)
                                    .placeholder(R.drawable.kisan_logorb)
                                    .into(profileImage);

                        } else {

                            Toast.makeText(UpdateProfileActivity.this,
                                    "User not found", Toast.LENGTH_SHORT).show();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                },
                error -> Toast.makeText(UpdateProfileActivity.this,
                        error.getMessage(), Toast.LENGTH_LONG).show()) {

            @Override
            protected Map<String, String> getParams() {

                Map<String, String> params = new HashMap<>();
                params.put("mobileno", mobile);

                return params;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(request);
    }
}