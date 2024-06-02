package com.example.icecube;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.inuker.bluetooth.library.connect.options.BleConnectOptions;
import com.inuker.bluetooth.library.connect.response.BleConnectResponse;
import com.inuker.bluetooth.library.connect.response.BleNotifyResponse;
import com.inuker.bluetooth.library.connect.response.BleUnnotifyResponse;
import com.inuker.bluetooth.library.connect.response.BleWriteResponse;
import com.inuker.bluetooth.library.model.BleGattProfile;

import java.util.UUID;

public class PassActivity extends AppCompatActivity implements BleNotifyResponse, BleUnnotifyResponse, BleWriteResponse, View.OnTouchListener, View.OnClickListener, TextWatcher {
    private String MAC;
    private UUID service;
    private UUID character;
    private MybluetoothClient mClient;
    private ProgressBar progressDialog;
    private String passstr;
    private DataRead dataRead;
    private EditText pass1, pass2, pass3;
    private Button btcancel;
    private int texttimes = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pass);

        pass1 = findViewById(R.id.password1);
        pass2 = findViewById(R.id.password2);
        pass3 = findViewById(R.id.password3);
        progressDialog = findViewById(R.id.prgbar);
        //   btcancel = findViewById(R.id.btcancel);

        //设置字体
        TextView txtLogo = findViewById(R.id.txtlogo);
//        Typeface typeface = Typeface.createFromAsset(getAssets(), "font/Montserrat-Black.ttf");
//        txtLogo.setTypeface(typeface);

        pass1.setOnTouchListener(this);
        pass1.addTextChangedListener(this);
        pass2.setOnTouchListener(this);
        pass2.addTextChangedListener(this);
        pass3.setOnTouchListener(this);
        pass3.addTextChangedListener(this);
        //    btcancel.setOnClickListener(this);

        mClient = MybluetoothClient.getInstance(getApplicationContext());
        mClient.notify(MAC, service, character, this);
        dataRead = new DataRead();
        Intent intent = getIntent();
        if (intent != null) {
            MAC = intent.getStringExtra("mac");
            service = (UUID) intent.getSerializableExtra("service");
            character = (UUID) intent.getSerializableExtra("character");
        }
        //返回
        ImageButton btback = findViewById(R.id.btback);
        btback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(PassActivity.this, ScanActivity.class);
                startActivity(intent);
            }
        });
        //显示密码
        showPassWord();
    }

    //显示密钥
    public void showPassWord() {
        if (character != null) {
            byte[] write = new byte[8];
            write[0] = (byte) 0xAA;
            write[1] = 0x09;
            write[2] = 0x01;
            write[3] = 0x00;
            write[4] = 0x00;
            byte[] bytin = {write[1], write[2], write[3], write[4]};
            int x = utilCRC.alex_crc16(bytin, 4);
            write[6] = (byte) (0xFF & x);
            write[5] = (byte) (0xFF & (x >> 8));
            write[7] = 0x55;
            mClient.notify(MAC, service, character, this);
            mClient.write(MAC, service, character, write, this);
        }
    }

    //收到通知
    public void updateStatus(byte[] data) {
        Log.v("update----", "now---");
        if (data.length == 22) {
            dataRead.setData(data);

            int testzero = 0;
            testzero = dataRead.getTempSetting() + dataRead.getTempReal() + dataRead.getFroseReal()
                    +dataRead.getFroseSetinng() + dataRead.getTurbo() + dataRead.getMode()
                    +dataRead.getBattery() +dataRead.getUnit() + dataRead.getStatus()
                    +dataRead.getErr() + dataRead.getVolhigh() + dataRead.getVollow()
                    +dataRead.getType() + dataRead.getHeatSetting() + dataRead.getReserver1()
                    +dataRead.getReserver2() + dataRead.getReserver3();
            if (testzero == 0) {
                Log.v("password", "wrong");
            } else {
                mClient.unnotify(MAC, service, character, new BleUnnotifyResponse() {
                    @Override
                    public void onResponse(int code) {
                        Log.v("unnotify", "------------here-----------");
                    }
                });


                try {
                    Log.v("password", "saved!");
                    //  SharedPreferences sharedPre = getSharedPreferences("myfile", Context.MODE_PRIVATE);
                    //SharedPreferences.Editor editor = sharedPre.edit();
                    // editor.putString(MAC, passstr);
                    // editor.apply();

                    SharedPreferences.Editor editor = PassActivity.this.getSharedPreferences("datafile", MODE_PRIVATE).edit();
                    editor.putString("device", MAC);
                    editor.putString(MAC, passstr);
                    editor.apply();

                } catch (Exception e) {
                    Log.v("password", "saved worong!");
                }
                Intent intent = new Intent(PassActivity.this, BoardActivity.class);
                // intent.putExtra("devicename",arrayList.get(i));
                mClient = null;
                intent.putExtra("mac", MAC);
                intent.putExtra("service", service);
                intent.putExtra("character", character);


                Log.v("-------奇怪", "---------------------哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈--------");
                startActivity(intent);
            }
        }
    }

    @Override
    public void onNotify(UUID service, UUID character, byte[] value) {
        updateStatus(value);
    }

    @Override
    public void onResponse(int code) {
        Log.v("失败", "断开连接");
        BleConnectOptions options = new BleConnectOptions.Builder()
                .setConnectRetry(3)   // 连接如果失败重试3次
                .setConnectTimeout(30000)   // 连接超时30s
                .setServiceDiscoverRetry(3)  // 发现服务如果失败重试3次
                .setServiceDiscoverTimeout(20000)  //
                .setServiceDiscoverTimeout(2000)  // 发现服务超时20s
                .build();
        mClient.connect(MAC, options, new BleConnectResponse() {
            @Override
            public void onResponse(int code, BleGattProfile data) {
                if (code != -1) {
                    Log.v("重连", "重新连接成功！");
                }
            }
        });
    }


    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
//        switch (view.getId()) {
//            case R.id.password1:
//                pass1.setText(null);
//                break;
//            case R.id.password2:
//                pass2.setText(null);
//                break;
//            case R.id.password3:
//                pass3.setText(null);
//                break;
//        }
        return false; //取消软键盘
    }

    @Override
    public void onClick(View view) {

    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void afterTextChanged(Editable editable) {
        if (pass2.hasFocus()) {
            pass3.requestFocus();
            pass3.requestFocusFromTouch();
        }
        if (pass1.hasFocus()) {
            pass2.requestFocus();
            pass2.requestFocusFromTouch();
        }

        if (texttimes == 0) {
            if (pass1.getText().length() + pass2.getText().length() + pass3.getText().length() == 3) {
                String a = pass1.getText().toString();
                String b = pass2.getText().toString();
                String c = pass3.getText().toString();
                handlepass(a, b, c);
                texttimes++;
            }
        }
    }

    //使用获得的密码查询信息
    private void handlepass(String a, String b, String c) {
        passstr = a + b + c;
        try {
            byte[] write = new byte[8];
            write[0] = (byte) 0xAA;
            write[1] = 0x01;
            write[2] = 0x00;
            write[3] = (byte) Integer.parseInt(a, 16);
            write[4] = (byte) (Integer.parseInt(b, 16) * 16 + Integer.parseInt(c, 16));
            byte[] bytin = {write[1], write[2], write[3], write[4]};
            int x = utilCRC.alex_crc16(bytin, 4);
            write[6] = (byte) (0xFF & x);
            write[5] = (byte) (0xFF & (x >> 8));
            write[7] = 0x55;
            mClient.write(MAC, service, character, write, this);
        } catch (Exception e) {
            Log.v("password---", "out of range");
        }
    }
}