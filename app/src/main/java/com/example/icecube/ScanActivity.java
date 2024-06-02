package com.example.icecube;

import static com.inuker.bluetooth.library.Code.REQUEST_FAILED;
import static com.inuker.bluetooth.library.Code.REQUEST_SUCCESS;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.inuker.bluetooth.library.BluetoothClient;
import com.inuker.bluetooth.library.connect.options.BleConnectOptions;
import com.inuker.bluetooth.library.connect.response.BleConnectResponse;
import com.inuker.bluetooth.library.model.BleGattProfile;
import com.inuker.bluetooth.library.search.SearchRequest;
import com.inuker.bluetooth.library.search.SearchResult;
import com.inuker.bluetooth.library.search.response.SearchResponse;

import java.util.ArrayList;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import me.jessyan.autosize.internal.CustomAdapt;

public class ScanActivity extends AppCompatActivity implements CustomAdapt {

    private final UUID service4UUID = UUID.fromString("0000fee0-0000-1000-8000-00805f9b34fb");
    private final UUID charAUUID = UUID.fromString("0000fee1-0000-1000-8000-00805f9b34fb");
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

        //返回上一页
        ImageButton btback = findViewById(R.id.btback);
        btback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mClient != null && MAC!=null){
                    mClient.disconnect(MAC);
                }
                finish();
            }
        });

        SharedPreferences sharedPreferences = getSharedPreferences("type", 0);
        brand = sharedPreferences.getString("protype","");
        TextView textprotype = findViewById(R.id.protype);
        textprotype.setText(brand);

        arrayList = new ArrayList<String>();
        arrayMAC = new ArrayList<>();

        boolean locationEnable = isLocationEnabled();
        if (!locationEnable) {
            Toast.makeText(ScanActivity.this, "Please turn on location", Toast.LENGTH_SHORT).show();
        }

        // 检测PHONE_STATE 如果已授权
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            //做你想做的
            Log.v("hhhhhhh", "okokokokokookok");
        } else {
            String[] permission = {"android.permission.ACCESS_FINE_LOCATION", "android.permission.ACCESS_COARSE_LOCATION"};
            ActivityCompat.requestPermissions(this, permission, 1);
        }
//
//        //返回按钮
//        ImageButton btback = findViewById(R.id.btback);
//        btback.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent();
//                intent.setClass(ScanActivity.this, LogoActivity.class);
//                startActivity(intent);
//            }
//        });

        mClient = MybluetoothClient.getInstance(getApplicationContext());
        SearchRequest request = new SearchRequest.Builder()
                .searchBluetoothLeDevice(3000, 3)   // 先扫BLE设备3次，每次3s
                //  .searchBluetoothClassicDevice(5000) // 再扫经典蓝牙5s
                .searchBluetoothLeDevice(2000)      // 再扫BLE设备2s
                .build();

        prgbar = findViewById(R.id.prgbar);
        prgbar.setVisibility(View.VISIBLE);
        //扫描蓝牙设备
        mClient.search(request, new SearchResponse() {
            @Override
            public void onSearchStarted() {
            }

            @Override
            public void onDeviceFounded(SearchResult device) {
                // Beacon beacon = new Beacon(device.scanRecord);
                // BluetoothLog.v(String.format("----beacon for %s\n%s", device.getAddress(), beacon.toString()));
                Log.v("------found", device.getName());
                if (device.getName().startsWith(brand)) {
                    prgbar.setVisibility(View.INVISIBLE);
                    if (!arrayList.contains(device.getName())) {
                        Toast.makeText(ScanActivity.this, "Device Found. you can connet it now!", Toast.LENGTH_SHORT).show();
                        arrayList.add(device.getName());
                        arrayMAC.add(device.getAddress());
                        if(arrayList.size()>=2) mClient.stopSearch();
                    }
                }
            }

            @Override
            public void onSearchStopped() {
                prgbar.setVisibility(View.INVISIBLE);
                Toast.makeText(ScanActivity.this, "Device not Found!", Toast.LENGTH_SHORT).show();
                Log.v("over", "-------scanstopped");
                for (String name : arrayList) {
                    Log.v("----name---", name);
                }
                for (String mac : arrayMAC) {
                    Log.v("----mac---", mac);
                }
            }

            @Override
            public void onSearchCanceled() {
            }
        });


        //扫描二维码
        ImageButton btscanqr = findViewById(R.id.btscan);
        btscanqr.setOnClickListener(view -> initScan());

        //选择蓝牙
        ImageButton btbluetooth = (ImageButton) findViewById(R.id.btbluetooth);
        btbluetooth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog();
            }
        });
    }

    public void alertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        // String[] array = new String[]{"A", "B", "C", "D", "E"};
        String[] array = arrayList.toArray(new String[0]);
        builder.setTitle("Select a Device to Connect")
                .setCancelable(true)
                .setItems(array, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        connect(which);
                    }
                })

                .setNeutralButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                }).create().show();
    }

    public void initScan() {
        IntentIntegrator integrator = new IntentIntegrator(this);
        // 设置要扫描的条码类型，ONE_D_CODE_TYPES：一维码，QR_CODE_TYPES-二维码
        // integrator.setDesiredBarcodeFormats();
        integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE);
        integrator.setCaptureActivity(CaptureActivity1.class); //设置打开摄像头的Activity
        integrator.setPrompt(""); //底部的提示文字，设为""可以置空
        integrator.setCameraId(0); //前置或者后置摄像头
        integrator.setBeepEnabled(true); //扫描成功的「哔哔」声，默认开启
        integrator.setBarcodeImageEnabled(true);
        integrator.initiateScan();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (requestCode == IntentIntegrator.REQUEST_CODE) {
            IntentResult scanResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
            if (scanResult != null && scanResult.getContents() != null) {
                String result = scanResult.getContents();
                Log.d("扫码返回: ", result);
                if (result != null) {
                    String devicename = getFieldValue(result, "sn");
//                    if (devicename.startsWith("EVA24VTR") || devicename.startsWith("EVA12VRV") || devicename.startsWith("EVA2700RV")) {
                    if (devicename.startsWith("EVA")) {
                        for (int i = 0; i < arrayList.size(); i++) {
                            if (arrayList.get(i).equals(devicename)) {
                                connect(i);
                            }
                        }
                    }
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setTitle("Wrong").setMessage("This is a wrong QR code!");
                    // 获取AlertDialog
                    AlertDialog dialog = builder.create();
                    // 显示
                    dialog.show();
                }
                // tvMsg.setText(result);
            }
        }
    }

    //从链接字段获取设备名
    private String getFieldValue(String urlStr, String field) {
        String result = "";
        Pattern pXM = Pattern.compile(field + "=([^&]*)");
        Matcher mXM = pXM.matcher(urlStr);
        while (mXM.find()) {
            result += mXM.group(1);
        }
        return result;
    }

    //连接设备
    private void connect(int i) {
        mClient.stopSearch();
        progressDialog = ProgressDialog.show(ScanActivity.this, "Connect", "Connect device...");
        BleConnectOptions options = new BleConnectOptions.Builder()
                .setConnectRetry(3)   // 连接如果失败重试3次
                .setConnectTimeout(30000)   // 连接超时30s
                .setServiceDiscoverRetry(3)  // 发现服务如果失败重试3次
                .setServiceDiscoverTimeout(20000)  //
                .setServiceDiscoverTimeout(2000)  // 发现服务超时20s
                .build();

        mClient.connect(arrayMAC.get(i), options, new BleConnectResponse() {
            @Override
            public void onResponse(int code, BleGattProfile profile) {
                if (code == REQUEST_SUCCESS) {
                    Log.d("connect", "---Connected successfully!---");
                    service = service4UUID;
                    character = charAUUID;
                    progressDialog.dismiss();

                    checkpass(arrayMAC.get(i));
//
//                    Intent intent = new Intent();
//                    SharedPreferences sp = getSharedPreferences("intentdata",MODE_PRIVATE);//获取
//                    SharedPreferences.Editor editor = sp.edit(); // 获取编辑器对象
//                    editor.putString("MAC",arrayMAC.get(i)); // 存入String类型数据
//                    editor.putString("brand",brand); // 存入int类型数据
//                    editor.putString("sn",arrayList.get(i));
//                    editor.commit(); // 提交数据
//
//                    intent.setClass(ScanActivity.this, BoardActivity.class);
//
//                    startActivity(intent);
                } else if (code == REQUEST_FAILED) {
                    progressDialog.dismiss();
                }
            }
        });
    }

    //判断用户是否开启定位
    public boolean isLocationEnabled() {
        int locationMode = 0;
        String locationProviders;
        try {
            locationMode = Settings.Secure.getInt(getContentResolver(), Settings.Secure.LOCATION_MODE);
        } catch (Settings.SettingNotFoundException e) {
            e.printStackTrace();
            return false;
        }
        return locationMode != Settings.Secure.LOCATION_MODE_OFF;
    }

    //看连接的设备是否有保存过的密码
    private void checkpass(String mac) {
        try {
            SharedPreferences sharepre = getSharedPreferences("datafile", Context.MODE_PRIVATE);
            String MacStr = sharepre.getString(mac, "");
            if (MacStr != "") {
                Intent intent = new Intent(ScanActivity.this, BoardActivity.class);
                // intent.putExtra("devicename",arrayList.get(i));
                intent.putExtra("mac", mac);
                intent.putExtra("service", service);
                intent.putExtra("character", character);
                // mClient = null;
                startActivity(intent);
            } else {
                Intent intent = new Intent(ScanActivity.this, PassActivity.class);
                // intent.putExtra("devicename",arrayList.get(i));
                intent.putExtra("mac", mac);
                intent.putExtra("service", service);
                intent.putExtra("character", character);
                //    mClient = null;
                startActivity(intent);
            }
        } catch (Exception e) {
            Log.v("data store errr", e.toString());
        }
    }
}