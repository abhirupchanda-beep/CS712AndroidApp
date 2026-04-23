package com.example.cs712androidapp;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class MainActivity extends AppCompatActivity {

    private static final String MY_ACTION = "com.example.cs712androidapp.MY_ACTION";

    private static final String CUSTOM_PERMISSION = "com.example.cs712androidapp.MSE712";
    private static final int REQ_MSE712 = 101;
    private static final int REQ_NOTIF = 100;

    private MyBroadcastReceiver receiver;
    private boolean receiverRegistered = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        requestCustomPermission();
        requestNotificationPermission();

        receiver = new MyBroadcastReceiver();
        IntentFilter filter = new IntentFilter(MY_ACTION);

        if (Build.VERSION.SDK_INT >= 33) {
            registerReceiver(receiver, filter, Context.RECEIVER_NOT_EXPORTED);
        } else {
            registerReceiver(receiver, filter);
        }

        receiverRegistered = true;

        Button btnExplicit = findViewById(R.id.btnExplicit);
        Button btnImplicit = findViewById(R.id.btnImplicit);
        Button btnStartService = findViewById(R.id.btnStartService);
        Button btnSendBroadcast = findViewById(R.id.btnSendBroadcast);
        Button btnViewImageActivity = findViewById(R.id.btnViewImageActivity);

        btnExplicit.setOnClickListener(v -> {
            if (ContextCompat.checkSelfPermission(this, CUSTOM_PERMISSION)
                    == PackageManager.PERMISSION_GRANTED) {

                Intent intent = new Intent(MainActivity.this, SecondActivity.class);
                startActivity(intent);

            } else {
                Toast.makeText(this, "MSE712 permission not granted", Toast.LENGTH_SHORT).show();
            }
        });

        btnImplicit.setOnClickListener(v -> {
            if (ContextCompat.checkSelfPermission(this, CUSTOM_PERMISSION)
                    == PackageManager.PERMISSION_GRANTED) {

                Intent intent = new Intent("com.example.cs712androidapp.OPEN_SECOND");
                intent.addCategory(Intent.CATEGORY_DEFAULT);
                startActivity(intent);

            } else {
                Toast.makeText(this, "MSE712 permission not granted", Toast.LENGTH_SHORT).show();
            }
        });

        btnStartService.setOnClickListener(v -> {
            Intent serviceIntent = new Intent(MainActivity.this, MyForegroundService.class);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                startForegroundService(serviceIntent);
            } else {
                startService(serviceIntent);
            }
        });

        btnSendBroadcast.setOnClickListener(v -> {
            Intent intent = new Intent(MY_ACTION);
            intent.setPackage(getPackageName());
            sendBroadcast(intent);
        });

        btnViewImageActivity.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, ThirdActivity.class);
            startActivity(intent);
        });
    }

    private void requestCustomPermission() {
        if (ContextCompat.checkSelfPermission(this, CUSTOM_PERMISSION)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(
                    this,
                    new String[]{CUSTOM_PERMISSION},
                    REQ_MSE712
            );
        }
    }

    private void requestNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS)
                    != PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(
                        this,
                        new String[]{Manifest.permission.POST_NOTIFICATIONS},
                        REQ_NOTIF
                );
            }
        }
    }

    @Override
    protected void onStop() {
        super.onStop();

        if (receiverRegistered) {
            unregisterReceiver(receiver);
            receiverRegistered = false;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQ_MSE712) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "MSE712 permission granted", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "MSE712 permission denied", Toast.LENGTH_SHORT).show();
            }
        }

        if (requestCode == REQ_NOTIF) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Notification permission granted", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Notification permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }
}