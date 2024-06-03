package com.example.icecube;

import static com.inuker.bluetooth.library.Code.REQUEST_SUCCESS;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.inuker.bluetooth.library.BluetoothClient;
import com.inuker.bluetooth.library.connect.response.BleNotifyResponse;
import com.inuker.bluetooth.library.connect.response.BleWriteResponse;

import java.util.ArrayList;
import java.util.Objects;
import java.util.UUID;

import me.jessyan.autosize.internal.CustomAdapt;

public class BoardActivity extends AppCompatActivity implements android.view.View.OnClickListener, BleWriteResponse, BleNotifyResponse, CustomAdapt {

    private final UUID service4UUID = UUID.fromString("0000fee0-0000-1000-8000-00805f9b34fb");
    private final UUID charAUUID = UUID.fromString("0000fee1-0000-1000-8000-00805f9b34fb");
    ImageButton btmode;
    ImageButton btstatus;
    private BluetoothClient mClient;
    private String brand;
    private String MAC;
    private UUID service;
    private UUID character;
    private DataRead dataRead;
    private mode_dialog mdialog;
    private turbo_dialog tdialog;
    private ImageButton bt_return;
    private ImageButton bt_power;
    private ImageButton bt_unit;
    private ImageView iv_back;
    private TextView text_setting;
    private ImageButton bt_minus;
    private ImageButton bt_add;
    private ImageButton bt_seafood;
    private ImageButton bt_drink;
    private ImageButton bt_ice;
    private ImageButton bt_vagetable;
    private ProgressBar progressBar;
    private String a, b, c;   //密码
    private int style;  //四种保鲜模式

    //用于定时
    private Handler handler = new Handler();
    private Runnable runnable;
    private int count = 0;
    private boolean isRunning = true;

    @Override
    public boolean isBaseOnWidth() {
        return false;
    }

    @Override
    public float getSizeInDp() {
        return 0;
    }

    private void starttimer(){
        if (!isRunning) {
            isRunning = true;
            runnable = new Runnable() {
                @Override
                public void run() {
                    count++;
                    getStatus();
                    handler.postDelayed(this, 5000); // 5000 milliseconds
                }
            };
        }
        handler.postDelayed(runnable, 1000); // Initial delay
    }

    private  void stoptimer(){
        if (isRunning) {
            isRunning = false;
            handler.removeCallbacks(runnable);
        }
    }

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board);

        btmode = findViewById(R.id.btmode);
        btstatus = findViewById(R.id.btstatus);

        handler = new Handler();

        initBluetooth();  //初始化蓝牙
        initController();  //初始化控件
        getPassword();//获取密码
        dataRead = new DataRead();
        getStatus(); //获取冰箱状态

        //点击电池模式按钮
        btmode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mdialog =
                        new mode_dialog(BoardActivity.this, R.layout.dialog_mode, new mode_dialog.OnConfirmListener() {
                            @Override
                            public void onConfirm() {
                                if (mdialog != null) {
                                    mdialog.dismiss();
                                }
                            }
                        }, new mode_dialog.OnHighListener() {
                            @SuppressLint("ResourceAsColor")
                            @Override
                            public void onHigh() {
                                Log.v("high button ", "high button clicked!------------");
                                if ((service != null) && (character != null)) {
                                    byte[] write = new byte[8];
                                    write[0] = (byte) 0xAA;
                                    write[1] = 0x07;
                                    write[2] = 0x02;
                                    write[3] = (byte) Integer.parseInt(a, 16);
                                    write[4] = (byte) (Integer.parseInt(b, 16) * 16 + Integer.parseInt(c, 16));
                                    byte[] bytein = {write[1], write[2], write[3], write[4]};
                                    int x = utilCRC.alex_crc16(bytein, 4);
                                    write[6] = (byte) (0xFF & x);
                                    write[5] = (byte) (0xFF & (x >> 8));
                                    write[7] = 0x55;
                                    mClient.write(MAC, service, character, write, BoardActivity.this);
                                }
                            }
                        }, new mode_dialog.OnmiddleListener() {
                            @Override
                            public void onMiddle() {
                                Log.v("middle button ", "middle button clicked!------------");
                                if ((service != null) && (character != null)) {
                                    byte[] write = new byte[8];
                                    write[0] = (byte) 0xAA;
                                    write[1] = 0x07;
                                    write[2] = 0x01;
                                    write[3] = (byte) Integer.parseInt(a, 16);
                                    write[4] = (byte) (Integer.parseInt(b, 16) * 16 + Integer.parseInt(c, 16));
                                    byte[] bytein = {write[1], write[2], write[3], write[4]};
                                    int x = utilCRC.alex_crc16(bytein, 4);
                                    write[6] = (byte) (0xFF & x);
                                    write[5] = (byte) (0xFF & (x >> 8));
                                    write[7] = 0x55;
                                    mClient.write(MAC, service, character, write, BoardActivity.this);
                                }
                            }
                        }, new mode_dialog.OnLowListener() {
                            @Override
                            public void onLow() {
                                Log.v("low button ", "low button clicked!------------");
                                if ((service != null) && (character != null)) {
                                    byte[] write = new byte[8];
                                    write[0] = (byte) 0xAA;
                                    write[1] = 0x07;
                                    write[2] = 0x00;
                                    write[3] = (byte) Integer.parseInt(a, 16);
                                    write[4] = (byte) (Integer.parseInt(b, 16) * 16 + Integer.parseInt(c, 16));
                                    byte[] bytein = {write[1], write[2], write[3], write[4]};
                                    int x = utilCRC.alex_crc16(bytein, 4);
                                    write[6] = (byte) (0xFF & x);
                                    write[5] = (byte) (0xFF & (x >> 8));
                                    write[7] = 0x55;
                                    mClient.write(MAC, service, character, write, BoardActivity.this);
                                }
                            }
                        });
                Window window = mdialog.getWindow();
                if (window != null) {
                    WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
                    layoutParams.copyFrom(window.getAttributes());
                    // 设置背景半透明，这里的0.5f表示50%的透明度
                    layoutParams.dimAmount = 0.7f;
                    layoutParams.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
                    window.setAttributes(layoutParams);
                    // 可选：设置对话框窗口的背景为透明，这样对话框本身的背景也会是透明的
                    window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                }
                mdialog.show();
            }
        });
        //点击电池保护模式
        btstatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tdialog =
                        new turbo_dialog(BoardActivity.this, R.layout.dialog_turbo, new turbo_dialog.OnConfirmListener() {
                            @Override
                            public void onConfirm() {
                                if (tdialog != null) {
                                    tdialog.dismiss();
                                }
                            }
                        }, new turbo_dialog.OnTurboListener() {
                            @Override
                            public void onTurbo() {
                                if ((service != null) && (character != null)) {
                                    byte[] write = new byte[8];
                                    write[0] = (byte) 0xAA;
                                    write[1] = 0x05;
                                    write[2] = 0x01;
                                    write[3] = (byte) Integer.parseInt(a, 16);
                                    write[4] = (byte) (Integer.parseInt(b, 16) * 16 + Integer.parseInt(c, 16));
                                    byte[] bytein = {write[1], write[2], write[3], write[4]};
                                    int x = utilCRC.alex_crc16(bytein, 4);
                                    write[6] = (byte) (0xFF & x);
                                    write[5] = (byte) (0xFF & (x >> 8));
                                    write[7] = 0x55;
                                    mClient.write(MAC, service, character, write, BoardActivity.this);
                                }
                            }
                        }, new turbo_dialog.OnEcoListener() {
                            @Override
                            public void onEco() {
                                if ((service != null) && (character != null)) {
                                    byte[] write = new byte[8];
                                    write[0] = (byte) 0xAA;
                                    write[1] = 0x05;
                                    write[2] = 0x00;
                                    write[3] = (byte) Integer.parseInt(a, 16);
                                    write[4] = (byte) (Integer.parseInt(b, 16) * 16 + Integer.parseInt(c, 16));
                                    byte[] bytein = {write[1], write[2], write[3], write[4]};
                                    int x = utilCRC.alex_crc16(bytein, 4);
                                    write[6] = (byte) (0xFF & x);
                                    write[5] = (byte) (0xFF & (x >> 8));
                                    write[7] = 0x55;
                                    mClient.write(MAC, service, character, write, BoardActivity.this);
                                }
                            }
                        });

                Window window = tdialog.getWindow();
                if (window != null) {
                    WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
                    layoutParams.copyFrom(window.getAttributes());
                    // 设置背景半透明，这里的0.5f表示50%的透明度
                    layoutParams.dimAmount = 0.7f;
                    layoutParams.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
                    window.setAttributes(layoutParams);

                    // 可选：设置对话框窗口的背景为透明，这样对话框本身的背景也会是透明的
                    window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                }
                tdialog.show();
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

    //初始化蓝牙
    public void initBluetooth() {
        mClient = MybluetoothClient.getInstance(getApplicationContext());
        MAC = getIntent().getStringExtra("mac");
        service = service4UUID;
        character = charAUUID;
        // getStatus();
        mClient.notify(MAC, service, character, this);
    }

    //初始化控件
    public void initController() {
        bt_return = findViewById(R.id.btback);
        text_setting = findViewById(R.id.txttemp);
        bt_unit = findViewById(R.id.btunit);
        iv_back = findViewById(R.id.imageback);
        bt_minus = findViewById(R.id.btminus);
        bt_add = findViewById(R.id.btadd);
        bt_seafood = findViewById(R.id.btfresh);
        bt_drink = findViewById(R.id.btdrink);
        bt_ice = findViewById(R.id.bticecream);
        bt_vagetable = findViewById(R.id.btfruit);
        bt_power = findViewById(R.id.btpower);
        progressBar = findViewById(R.id.progresstemp);

        bt_return.setOnClickListener(this);
        bt_unit.setOnClickListener(this);
        bt_minus.setOnClickListener(this);
        bt_add.setOnClickListener(this);
        bt_seafood.setOnClickListener(this);
        bt_drink.setOnClickListener(this);
        bt_ice.setOnClickListener(this);
        bt_vagetable.setOnClickListener(this);
        bt_power.setOnClickListener(this);
    }

    //获取密码
    public void getPassword() {
        SharedPreferences sharedPref = BoardActivity.this.getSharedPreferences("myfile", Context.MODE_PRIVATE);
        String passstr = sharedPref.getString(MAC, "");
        try {
            a = passstr.substring(0, 1);
            b = passstr.substring(1, 2);
            c = passstr.substring(2, 3);
        } catch (Exception e) {
            Log.v("password failed!", e.toString());
        }
    }

    //获取冰箱状态
    public void getStatus() {
        if ((service != null) && (character != null)) {
            byte[] write = new byte[8];
            write[0] = (byte) 0xAA;
            write[1] = 0x01;
            write[2] = 0x00;
            write[3] = (byte) Integer.parseInt(a, 16);
            write[4] = (byte) (Integer.parseInt(b, 16) * 16 + Integer.parseInt(c, 16));
            byte[] bytein = {write[1], write[2], write[3], write[4]};
            int x = utilCRC.alex_crc16(bytein, 4);
            write[6] = (byte) (0xFF & x);
            write[5] = (byte) (0xFF & (x >> 8));
            write[7] = 0x55;
            mClient.write(MAC, service, character, write, this);
        }
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btback) {
            setReturn();
        }
        if (view.getId() == R.id.btunit) {
            setUnit();
        }
        if (view.getId() == R.id.btminus) {
            setMinus();
        }
        if (view.getId() == R.id.btadd) {
            setAdd();
        }
        if (view.getId() == R.id.btfresh) {
            setSeafood();
        }
        if (view.getId() == R.id.btdrink) {
            setDrink();
        }
        if (view.getId() == R.id.bticecream) {
            setIce();
        }
        if (view.getId() == R.id.btfruit) {
            setVagetable();
        }
        if (view.getId() == R.id.btpower) {
            setPower();
        }
    }


    //返回
    public void setReturn() {
        SharedPreferences sharedPref = BoardActivity.this.getSharedPreferences(getString(R.string.filekey), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(getString(R.string.MACkey), "");
        editor.putString(getString(R.string.serviceKey), service.toString());
        editor.putString(getString(R.string.characterKey), character.toString());
        editor.apply();
        Intent intent = new Intent(BoardActivity.this, MainActivity.class);
        startActivity(intent);
    }

    //是否写入成功
    @Override
    public void onResponse(int code) {
        if (code == REQUEST_SUCCESS) {
            Log.v("write result", "Write datas successfully!");
        } else {
            Log.v("write result", "Write datas faily!");
        }
    }

    //收到设备返回通知
    @Override
    public void onNotify(UUID service, UUID character, byte[] value) {
        Log.v("notify", "recieve notify!");
        updateStatus(value);
        starttimer();
    }

    //开机关机
    public void setPower() {
        Log.v("power", "power clicked");
        byte powerstatus = dataRead.getPower();
        if (powerstatus == 0x00) {
            powerstatus = 0x01;
        } else {
            powerstatus = 0x00;
        }
        if ((service != null) && (character != null)) {
            byte[] write = new byte[8];
            write[0] = (byte) 0xAA;
            write[1] = 0x02;
            write[2] = powerstatus;
            write[3] = (byte) Integer.parseInt(a, 16);
            write[4] = (byte) (Integer.parseInt(b, 16) * 16 + Integer.parseInt(c, 16));
            byte[] bytein = {write[1], write[2], write[3], write[4]};
            int x = utilCRC.alex_crc16(bytein, 4);
            write[6] = (byte) (0xFF & x);
            write[5] = (byte) (0xFF & (x >> 8));
            write[7] = 0x55;
            mClient.write(MAC, service, character, write, this);
        }
    }

    //设置单位
    public void setUnit() {
        int scale = dataRead.getUnit();
        if ((service != null) && (character != null)) {
            byte[] write = new byte[8];
            write[0] = (byte) 0xAA;
            write[1] = 0x08;
            write[2] = (byte) scale;
            write[3] = (byte) Integer.parseInt(a, 16);
            write[4] = (byte) (Integer.parseInt(b, 16) * 16 + Integer.parseInt(c, 16));
            byte[] bytein = {write[1], write[2], write[3], write[4]};
            int x = utilCRC.alex_crc16(bytein, 4);
            write[6] = (byte) (0xFF & x);
            write[5] = (byte) (0xFF & (x >> 8));
            write[7] = 0x55;
            mClient.write(MAC, service, character, write, this);
        }
    }

    //温度减
    public void setMinus() {
        int setting = dataRead.getTempSetting();
        setting--;

        if ((service != null) && (character != null)) {
            byte[] write = new byte[8];
            write[0] = (byte) 0xAA;

            write[1] = 0x03;
            write[2] = (byte) setting;

            write[3] = (byte) Integer.parseInt(a, 16);
            write[4] = (byte) (Integer.parseInt(b, 16) * 16 + Integer.parseInt(c, 16));
            byte[] bytein = {write[1], write[2], write[3], write[4]};
            int x = utilCRC.alex_crc16(bytein, 4);
            write[6] = (byte) (0xFF & x);
            write[5] = (byte) (0xFF & (x >> 8));
            write[7] = 0x55;
            mClient.write(MAC, service, character, write, this);
        }
    }

    //温度加
    public void setAdd() {
        stoptimer();
        int setting = dataRead.getTempSetting();
        setting++;

        if ((service != null) && (character != null)) {
            byte[] write = new byte[8];
            write[0] = (byte) 0xAA;

            write[1] = 0x03;
            write[2] = (byte) setting;

            write[3] = (byte) Integer.parseInt(a, 16);
            write[4] = (byte) (Integer.parseInt(b, 16) * 16 + Integer.parseInt(c, 16));
            byte[] bytein = {write[1], write[2], write[3], write[4]};
            int x = utilCRC.alex_crc16(bytein, 4);
            write[6] = (byte) (0xFF & x);
            write[5] = (byte) (0xFF & (x >> 8));
            write[7] = 0x55;
            mClient.write(MAC, service, character, write, this);
        }
    }

    //海鲜模式
    @SuppressLint("UseCompatLoadingForDrawables")
    public void setSeafood() {
        if (style != 1) {
            style = 1;
        } else {
            style = 0;
        }

        if ((service != null) && (character != null)) {
            byte[] write = new byte[8];
            write[0] = (byte) 0xAA;

            write[1] = 0x03;

            write[2] = -20;
            write[3] = (byte) Integer.parseInt(a, 16);
            write[4] = (byte) (Integer.parseInt(b, 16) * 16 + Integer.parseInt(c, 16));
            byte[] bytein = {write[1], write[2], write[3], write[4]};
            int x = utilCRC.alex_crc16(bytein, 4);
            write[6] = (byte) (0xFF & x);
            write[5] = (byte) (0xFF & (x >> 8));
            write[7] = 0x55;
            mClient.write(MAC, service, character, write, this);
        }
    }

    //饮料模式
    @SuppressLint("UseCompatLoadingForDrawables")
    public void setDrink() {
        if (style != 2) {
            style = 2;
        } else {
            style = 0;
        }

        if ((service != null) && (character != null)) {
            byte[] write = new byte[8];
            write[0] = (byte) 0xAA;
            write[0] = (byte) 0xAA;

            write[1] = 0x03;

            write[2] = 4;
            write[3] = (byte) Integer.parseInt(a, 16);
            write[4] = (byte) (Integer.parseInt(b, 16) * 16 + Integer.parseInt(c, 16));
            byte[] bytein = {write[1], write[2], write[3], write[4]};
            int x = utilCRC.alex_crc16(bytein, 4);
            write[6] = (byte) (0xFF & x);
            write[5] = (byte) (0xFF & (x >> 8));
            write[7] = 0x55;
            mClient.write(MAC, service, character, write, this);
        }
    }

    //冰淇淋模式
    @SuppressLint("UseCompatLoadingForDrawables")
    public void setIce() {
        if (style != 3) {
            style = 3;
        } else {
            style = 0;
        }

        if ((service != null) && (character != null)) {
            byte[] write = new byte[8];
            write[0] = (byte) 0xAA;
            write[0] = (byte) 0xAA;

            write[1] = 0x03;

            write[2] = -15;
            write[3] = (byte) Integer.parseInt(a, 16);
            write[4] = (byte) (Integer.parseInt(b, 16) * 16 + Integer.parseInt(c, 16));
            byte[] bytein = {write[1], write[2], write[3], write[4]};
            int x = utilCRC.alex_crc16(bytein, 4);
            write[6] = (byte) (0xFF & x);
            write[5] = (byte) (0xFF & (x >> 8));
            write[7] = 0x55;
            mClient.write(MAC, service, character, write, this);
        }
    }

    //蔬菜模式
    @SuppressLint("UseCompatLoadingForDrawables")
    public void setVagetable() {
        if (style != 4) {
            style = 4;
        } else {
            style = 0;
        }

        if ((service != null) && (character != null)) {
            byte[] write = new byte[8];
            write[0] = (byte) 0xAA;
            write[0] = (byte) 0xAA;

            write[1] = 0x03;

            write[2] = 8;
            write[3] = (byte) Integer.parseInt(a, 16);
            write[4] = (byte) (Integer.parseInt(b, 16) * 16 + Integer.parseInt(c, 16));
            byte[] bytein = {write[1], write[2], write[3], write[4]};
            int x = utilCRC.alex_crc16(bytein, 4);
            write[6] = (byte) (0xFF & x);
            write[5] = (byte) (0xFF & (x >> 8));
            write[7] = 0x55;
            mClient.write(MAC, service, character, write, this);
        }
    }


    //更新状态
    @SuppressLint({"UseCompatLoadingForDrawables", "SetTextI18n"})
    public void updateStatus(byte[] data) {
        Log.v("---update", "successfully!");
        if (data.length == 22) {
            dataRead.setData(data);
        } else {
            return;
        }
        int setting = dataRead.getTempSetting(); //设定温度
        int real = dataRead.getTempReal();  //实时温度
        int scale = dataRead.getUnit();  //单位
        if(scale == 0){
            bt_unit.setImageResource(R.drawable.fahre);
        }else{
            bt_unit.setImageResource(R.drawable.ceils);
        }

        if (setting > 127) setting -= 256;
        if (real > 127) real -= 256;

        //关机
        if (dataRead.getPower() == 0) {
            bt_unit.setImageResource(R.drawable.ceils);
            iv_back.setImageResource(R.drawable.center);
            bt_seafood.setImageResource(R.drawable.fresh);
            bt_drink.setImageResource(R.drawable.drink);
            bt_ice.setImageResource(R.drawable.icecream);
            bt_vagetable.setImageResource(R.drawable.fruit);
            text_setting.setText("0°C");
            text_setting.setTextColor(Color.WHITE);
        } else {
            //开机
            iv_back.setImageResource(R.drawable.center);
            bt_seafood.setImageResource(R.drawable.fresh);
            bt_drink.setImageResource(R.drawable.drink);
            bt_ice.setImageResource(R.drawable.icecream);
            bt_vagetable.setImageResource(R.drawable.fruit);

            if (scale == 0) {
                bt_unit.setBackground(getDrawable(R.drawable.fahre));
            } else {
                bt_unit.setBackground(getDrawable(R.drawable.ceils));
            }
            switch (style) {
                case 1:
                    iv_back.setImageResource(R.drawable.backfresh);
                    bt_seafood.setImageResource(R.drawable.fresh1);
                    break;
                case 2:
                    iv_back.setImageResource(R.drawable.backdrink);
                    bt_drink.setImageResource(R.drawable.drink1);
                    break;
                case 3:
                    iv_back.setImageResource(R.drawable.backicecream);
                    bt_ice.setImageResource(R.drawable.icecream1);
                    break;
                case 4:
                    iv_back.setImageResource(R.drawable.backfruit);
                    bt_vagetable.setImageResource(R.drawable.fruit1);
                    break;
                default:
                    break;
            }

            //实际温度显示
            if (scale == 0) {//华氏
                text_setting.setText("Current: " + Math.round((real * 1.8 + 32)) + "℉");
            } else {  //摄氏
                text_setting.setText("Current: " + real + "°C");
            }
            progressBar.setProgress(setting);

            if (scale == 0) {//华氏
                text_setting.setText(String.valueOf((int) (setting * 1.8 + 32)) + "℉");
            } else {  //摄氏
                text_setting.setText(String.valueOf(setting) + "°C");
            }
            // slidetem = String.valueOf(setting);
        }
    }
}