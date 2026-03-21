package com.example.cs712androidapp;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private static final String MY_ACTION = "com.example.cs712androidapp.MY_ACTION";
    private static final int REQ_NOTIF = 100;

    private MyBroadcastReceiver receiver;
    private boolean receiverRegistered = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Register receiver
        receiver = new MyBroadcastReceiver();
        IntentFilter filter = new IntentFilter(MY_ACTION);

        if (Build.VERSION.SDK_INT >= 33) {
            registerReceiver(receiver, filter, Context.RECEIVER_NOT_EXPORTED);
        } else {
            registerReceiver(receiver, filter);
        }

        receiverRegistered = true;

        // Notification permission (Android 13+)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (checkSelfPermission(Manifest.permission.POST_NOTIFICATIONS)
                    != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.POST_NOTIFICATIONS}, REQ_NOTIF);
            }
        }

        // Buttons
        Button btnExplicit = findViewById(R.id.btnExplicit);
        Button btnImplicit = findViewById(R.id.btnImplicit);
        Button btnStartService = findViewById(R.id.btnStartService);
        Button btnSendBroadcast = findViewById(R.id.btnSendBroadcast);

        // ✅ NEW BUTTON
        Button btnViewImageActivity = findViewById(R.id.btnViewImageActivity);

        // Existing functionality
        btnExplicit.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, SecondActivity.class));
        });

        btnImplicit.setOnClickListener(v -> {
            startActivity(new Intent("com.example.cs712androidapp.OPEN_SECOND"));
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
            Intent i = new Intent(MY_ACTION);
            i.setPackage(getPackageName());
            sendBroadcast(i);
        });

        // ✅ OPEN THIRD ACTIVITY
        btnViewImageActivity.setOnClickListener(v -> {
            Toast.makeText(this, "Opening Image Activity", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(MainActivity.this, ThirdActivity.class);
            startActivity(intent);
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (receiverRegistered) {
            unregisterReceiver(receiver);
            receiverRegistered = false;
        }
    }
}