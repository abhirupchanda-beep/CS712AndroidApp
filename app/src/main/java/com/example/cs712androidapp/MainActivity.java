package com.example.cs712androidapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnExplicit = findViewById(R.id.btnExplicit);
        Button btnImplicit = findViewById(R.id.btnImplicit);

        // Explicit
        btnExplicit.setOnClickListener(v -> {
            Intent i = new Intent(MainActivity.this, SecondActivity.class);
            startActivity(i);
        });

        // Implicit (matches intent-filter in manifest)
        btnImplicit.setOnClickListener(v -> {
            Intent i = new Intent("com.example.cs712androidapp.OPEN_SECOND");
            startActivity(i);
        });
    }
}
