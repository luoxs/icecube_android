package com.example.icecube;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.inuker.bluetooth.library.BluetoothClient;

import java.util.ArrayList;
import java.util.UUID;

import me.jessyan.autosize.internal.CustomAdapt;

public class ScanActivity extends AppCompatActivity implements CustomAdapt {

    private final UUID service4UUID = UUID.fromString("0000ffe0-0000-1000-8000-00805f9b34fb");
    private final UUID charAUUID = UUID.fromString("0000ffe1-0000-1000-8000-00805f9b34fb");
    private ArrayList<String> arrayList;   //设备名列表
    private ArrayList<String> arrayMAC;    //设备地址列表
    private BluetoothClient mClient;

    private String brand;
    private String MAC;
    private UUID service;
    private UUID character;
    private ProgressDialog progressDialog;
    private ProgressBar prgbar;
    private TextView v2;

    @Override
    public boolean isBaseOnWidth() {
        return false;
    }
    @Override
    public float getSizeInDp() {
        return 0;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);

        ImageButton btback = findViewById(R.id.btback);
        btback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        SharedPreferences sharedPreferences = getSharedPreferences("type", 0);
        brand = sharedPreferences.getString("protype","");
        TextView textprotype = findViewById(R.id.protype);
        textprotype.setText(brand);
    }
}