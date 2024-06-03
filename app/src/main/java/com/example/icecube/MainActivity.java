package com.example.icecube;

import androidx.appcompat.app.AppCompatActivity;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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