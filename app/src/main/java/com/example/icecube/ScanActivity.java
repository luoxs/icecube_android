package com.example.icecube;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import me.jessyan.autosize.internal.CustomAdapt;

public class ScanActivity extends AppCompatActivity implements CustomAdapt {

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
    }
}