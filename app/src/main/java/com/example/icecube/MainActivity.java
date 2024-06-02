package com.example.icecube;

import androidx.appcompat.app.AppCompatActivity;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ImageButton btadd = findViewById(R.id.btadd);
        btadd.setOnClickListener(view -> {
            Intent  intent = new Intent();
            intent.setClass(MainActivity.this, DeviceActivity.class);
//            intent.setClass(MainActivity.this, PassActivity.class);

            startActivity(intent);
        });
    }
}