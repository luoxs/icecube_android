package com.example.icecube;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;


import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ImageButton btadd = findViewById(R.id.btadd);

        initPermission();


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

    private void initPermission() {
        List<String> mPermissionList = new ArrayList<>();
        // Android 版本大于等于 12 时，申请新的蓝牙权限
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            mPermissionList.add(android.Manifest.permission.BLUETOOTH_SCAN);
            mPermissionList.add(android.Manifest.permission.BLUETOOTH_ADVERTISE);
            mPermissionList.add(android.Manifest.permission.BLUETOOTH_CONNECT);
            //根据实际需要申请定位权限
            //mPermissionList.add(Manifest.permission.ACCESS_COARSE_LOCATION);
            //mPermissionList.add(Manifest.permission.ACCESS_FINE_LOCATION);
        } else {
            mPermissionList.add(android.Manifest.permission.ACCESS_COARSE_LOCATION);
            mPermissionList.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }
        int REQUEST_PERMISSION_CODE = 1001;
        ActivityCompat.requestPermissions(this, mPermissionList.toArray(new String[0]), REQUEST_PERMISSION_CODE);
    }
}