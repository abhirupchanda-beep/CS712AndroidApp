package com.example.cs712androidapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

public class MyBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("BR_TEST", "Broadcast received");
        Toast.makeText(context, "Broadcast received!", Toast.LENGTH_SHORT).show();
    }
}
