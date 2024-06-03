package com.example.icecube;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

public class DeviceActivity extends AppCompatActivity  {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device);


        ImageButton btselect = findViewById(R.id.btselect);

        btselect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        SharedPreferences sharedPreferences = getSharedPreferences("type", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        ImageButton btic23 = findViewById(R.id.btic23);
        btic23.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    editor.putString("protype","IC23");
                    editor.apply();
                    Intent  intent = new Intent();
                    intent.setClass(DeviceActivity.this,ScanActivity.class);
                    startActivity(intent);
            }
        });

        ImageButton btic43 = findViewById(R.id.btic43);
        btic43.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editor.putString("protype","IC43");
                editor.apply();
                Intent  intent = new Intent();
                intent.setClass(DeviceActivity.this,ScanActivity.class);
                startActivity(intent);
            }
        });

        ImageButton btic63 = findViewById(R.id.btic63);
        btic63.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editor.putString("protype","IC63");
                editor.apply();
                Intent  intent = new Intent();
                intent.setClass(DeviceActivity.this,ScanActivity.class);
                startActivity(intent);
            }
        });

        Button btsite = findViewById(R.id.btsite);
        btsite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = "https://icecube.ru";
                Intent intent = new Intent();
                intent.setAction("android.intent.action.VIEW");
                Uri content_url = Uri.parse(url);
                intent.setData(content_url);
                startActivity(intent);
            }
        });
    }
}