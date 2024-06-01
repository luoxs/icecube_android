package com.example.icecube;

import android.app.Dialog;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
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
                            @Override
                            public void onHigh() {
                                v.setBackgroundResource(R.color.blue);
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