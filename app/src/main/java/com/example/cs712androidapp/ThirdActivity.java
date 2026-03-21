package com.example.cs712androidapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import android.content.pm.PackageManager;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class ThirdActivity extends AppCompatActivity {

    private static final int REQUEST_IMAGE = 1;

    ImageView imageView;
    Button btnCaptureImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_third);

        imageView = findViewById(R.id.imageView);
        btnCaptureImage = findViewById(R.id.btnCaptureImage);

        btnCaptureImage.setOnClickListener(v -> {
            Toast.makeText(this, "Button clicked", Toast.LENGTH_SHORT).show();
            openCamera();
        });
        if (checkSelfPermission(android.Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{android.Manifest.permission.CAMERA}, 100);
        }
    }

    private void openCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(intent, REQUEST_IMAGE);
        } else {
            Toast.makeText(this, "Camera not available", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_IMAGE && resultCode == RESULT_OK) {

            if (data == null) {
                Toast.makeText(this, "No image data received", Toast.LENGTH_SHORT).show();
                return;
            }

            if (data.getExtras() == null) {
                Toast.makeText(this, "No extras found", Toast.LENGTH_SHORT).show();
                return;
            }

            Bitmap image = (Bitmap) data.getExtras().get("data");

            if (image != null) {
                imageView.setImageBitmap(image);
            } else {
                Toast.makeText(this, "Failed to capture image", Toast.LENGTH_SHORT).show();
            }
        }
    }
}