package com.example.icecube;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.Objects;

import me.jessyan.autosize.internal.CustomAdapt;

public class BoardActivity extends AppCompatActivity implements CustomAdapt {
    private mode_dialog mdialog;
    private turbo_dialog tdialog;
    private com.google.android.material.button.MaterialButton bthigh;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board);

        ImageButton btmode = findViewById(R.id.btmode);
        ImageButton btstatus = findViewById(R.id.btstatus);

        btmode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                
            }
        });

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
                               // com.google.android.material.button.MaterialButton bthigh = findViewById(R.id.bthigh);
//                                LayoutInflater inflater = getLayoutInflater();
//                                View dialogView = inflater.inflate(R.layout.dialog_mode, null);
//                                bthigh = dialogView.findViewById(R.id.bthigh);
                               // bthigh.setBackgroundColor(Color.RED);
                                Log.v("high button ","high button clicked!------------");
                            }
                        }, new mode_dialog.OnmiddleListener() {
                            @Override
                            public void onMiddle() {
                                Log.v("middle button ","middle button clicked!------------");
                            }
                        }, new mode_dialog.OnLowListener() {
                            @Override
                            public void onLow() {
                                Log.v("low button ","low button clicked!------------");
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

                            }
                        }, new turbo_dialog.OnEcoListener() {
                            @Override
                            public void onEco() {

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
    }

    @Override
    public boolean isBaseOnWidth() {
        return false;
    }

    @Override
    public float getSizeInDp() {
        return 0;
    }

}