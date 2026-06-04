package com.prashanjit.kisaan.HomeAct;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;


import com.prashanjit.kisaan.R;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.FileAsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.prashanjit.kisaan.common.Urls;

import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Locale;

import cz.msebera.android.httpclient.Header;

public class HowItWorksActivity extends AppCompatActivity {

    private ImageView imgPreview;
    private AppCompatButton btnCamera, btnGallery, btnPredict;
    private TextView txtDisease, txtConfidence;
    private CardView cardResult;

    private Bitmap selectedBitmap;
    private Uri cameraImageUri;
    private String detectedDisease = "";

    private final ActivityResultLauncher<String> cameraPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(),
                    isGranted -> {
                        if (isGranted) openCameraInternal();
                        else Toast.makeText(this,"Camera Permission Denied",Toast.LENGTH_SHORT).show();
                    });

    private final ActivityResultLauncher<String> galleryPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(),
                    isGranted -> {
                        if (isGranted) openGalleryInternal();
                        else Toast.makeText(this,"Storage Permission Denied",Toast.LENGTH_SHORT).show();
                    });

    private final ActivityResultLauncher<Intent> cameraLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                    result -> {
                        if (result.getResultCode() == Activity.RESULT_OK && cameraImageUri != null) {
                            try {
                                selectedBitmap = MediaStore.Images.Media.getBitmap(
                                        getContentResolver(),
                                        cameraImageUri
                                );
                                imgPreview.setImageBitmap(selectedBitmap);
                                resetPredictionUI();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });

    private final ActivityResultLauncher<Intent> galleryLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                    result -> {
                        if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                            try {
                                Uri imageUri = result.getData().getData();
                                selectedBitmap = MediaStore.Images.Media.getBitmap(
                                        getContentResolver(),
                                        imageUri
                                );
                                imgPreview.setImageBitmap(selectedBitmap);
                                resetPredictionUI();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_how_it_works);

        imgPreview = findViewById(R.id.imgPreview);
        btnCamera = findViewById(R.id.btnCamera);
        btnGallery = findViewById(R.id.btnGallery);
        btnPredict = findViewById(R.id.btnPredict);


        txtDisease = findViewById(R.id.txtDisease);
        txtConfidence = findViewById(R.id.txtConfidence);
        cardResult = findViewById(R.id.cardResult);



        btnCamera.setOnClickListener(v -> openCamera());
        btnGallery.setOnClickListener(v -> openGallery());
        btnPredict.setOnClickListener(v -> predictImage());
        requestPermissionsIfNeeded();

    }
    private void requestPermissionsIfNeeded() {
        // Camera
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            cameraPermissionLauncher.launch(Manifest.permission.CAMERA);
        }

        // Gallery / Storage
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_MEDIA_IMAGES)
                    != PackageManager.PERMISSION_GRANTED) {
                galleryPermissionLauncher.launch(Manifest.permission.READ_MEDIA_IMAGES);
            }
        } else {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                galleryPermissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE);
            }
        }
    }

    private void openCamera() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED) {
            openCameraInternal();
        } else {
            cameraPermissionLauncher.launch(Manifest.permission.CAMERA);
        }
    }

    private void openCameraInternal() {
        try {
            // Create a temporary file in cache
            File imageFile = File.createTempFile("camera_", ".jpg", getCacheDir());

            // Get URI using FileProvider
            cameraImageUri = androidx.core.content.FileProvider.getUriForFile(
                    this,
                    getPackageName() + ".provider",
                    imageFile
            );

            // Camera intent
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, cameraImageUri);
            intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);

            // Launch camera
            cameraLauncher.launch(intent);

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this,"Camera Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void openGallery() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_MEDIA_IMAGES)
                    == PackageManager.PERMISSION_GRANTED) openGalleryInternal();
            else galleryPermissionLauncher.launch(Manifest.permission.READ_MEDIA_IMAGES);
        } else {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) openGalleryInternal();
            else galleryPermissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE);
        }
    }

    private void openGalleryInternal() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        galleryLauncher.launch(intent);
    }

    private void predictImage() {

        if (selectedBitmap == null) {
            Toast.makeText(this,"Please select an image first",Toast.LENGTH_SHORT).show();
            return;
        }

        if (!isImageValid(selectedBitmap) || !isLikelyLeaf(selectedBitmap)) {
            txtDisease.setText("Not a leaf object");
            txtConfidence.setText("0 %");
            cardResult.setVisibility(android.view.View.VISIBLE);
            return;
        }

        cardResult.setVisibility(android.view.View.GONE);

        btnPredict.setEnabled(false);

        try {
            File file = File.createTempFile("scan_", ".jpg", getCacheDir());
            FileOutputStream fos = new FileOutputStream(file);
            selectedBitmap.compress(Bitmap.CompressFormat.JPEG, 90, fos);
            fos.close();

            RequestParams params = new RequestParams();
            params.put("file", file);

            AsyncHttpClient client = new AsyncHttpClient();
            client.setTimeout(120000);

            client.post("https://avantika2530-plant-disease-api.hf.space/predict",
                    params,
                    new com.loopj.android.http.JsonHttpResponseHandler() {

                        @Override
                        public void onStart() {
                            super.onStart();
                            Toast.makeText(HowItWorksActivity.this,"Processing...",Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONObject obj) {
                            btnPredict.setEnabled(true);
                            try {
                                String disease = obj.getString("prediction");
                                double confidence = obj.getDouble("confidence");

                                if (confidence < 70) {
                                    txtDisease.setText("Image unclear / Not a leaf");
                                } else {
                                    detectedDisease = disease;
                                    txtDisease.setText(disease);
//                                    uploadScanHistory(disease, confidence);

                                }

                                txtConfidence.setText(String.format(Locale.US,"%.2f %%", confidence));
                                cardResult.setVisibility(android.view.View.VISIBLE);

                            } catch (Exception e) {
                                e.printStackTrace();
                                Toast.makeText(HowItWorksActivity.this,"Response error",Toast.LENGTH_LONG).show();
                            }
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                            btnPredict.setEnabled(true);
                            Toast.makeText(HowItWorksActivity.this,"Server error: "+throwable.getMessage(),Toast.LENGTH_LONG).show();
                        }
                    });

        } catch (Exception e) {
            btnPredict.setEnabled(true);
            e.printStackTrace();
        }
    }

    private void uploadScanHistory(String disease,double confidence) {

        RequestParams params = new RequestParams();
        params.put("disease_name",disease);
        params.put("confidence",confidence);

        AsyncHttpClient client = new AsyncHttpClient();
        client.post(Urls.updateImage,params,
                new FileAsyncHttpResponseHandler(this) {

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, File response) {
                        Toast.makeText(HowItWorksActivity.this,"History Saved",Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, File file) {
                        Toast.makeText(HowItWorksActivity.this,"Fail to save History",Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private boolean isImageValid(Bitmap bitmap) {
        return bitmap.getWidth() >= 150 && bitmap.getHeight() >= 150;
    }

    private boolean isLikelyLeaf(Bitmap bitmap) {
        int greenPixels = 0;
        int totalPixels = 0;

        for (int x = 0; x < bitmap.getWidth(); x += 10) {
            for (int y = 0; y < bitmap.getHeight(); y += 10) {
                int pixel = bitmap.getPixel(x, y);
                int r = (pixel >> 16) & 0xff;
                int g = (pixel >> 8) & 0xff;
                int b = pixel & 0xff;

                if (g > r && g > b && g > 60) greenPixels++;
                totalPixels++;
            }
        }

        return ((double) greenPixels / totalPixels) > 0.25;
    }

    private void resetPredictionUI() {
        detectedDisease = "";
        txtDisease.setText("");
        txtConfidence.setText("");
        cardResult.setVisibility(android.view.View.GONE);

    }
}