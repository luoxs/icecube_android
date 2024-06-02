package com.example.icecube;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import java.util.Objects;


public class mode_dialog extends Dialog {

    private Button btHigh;
    private Button btmiddle;
    private Button btlow;
    private Button mConfirm;

    private int mLayoutId;
    private OnConfirmListener mConfirmListener;
    private OnHighListener mHighListener;
    private OnmiddleListener mMiddleListener;
    private OnLowListener mLowListener;

    /**
     *
     * @param context
     * @param layoutId
     * @param confirmListener
     * @param highListener
     * @param middleListener
     * @param lowListener
     */

    public mode_dialog(Context context, int layoutId, OnConfirmListener confirmListener, OnHighListener highListener,OnmiddleListener middleListener,OnLowListener lowListener) {
        super(context,R.style.Theme_IceCube);
        mConfirmListener=confirmListener;
        mHighListener = highListener;
        mMiddleListener = middleListener;
        mLowListener = lowListener;
        mLayoutId=layoutId;
    }

    //点击对应的监听器
    public interface OnHighListener {
        void onHigh();
    }

    //点击对应的监听器
    public interface OnmiddleListener {
        void onMiddle();
    }

    //点击对应的监听器
    public interface OnLowListener {
        void onLow();
    }

    //点击确定取消对应的监听器
    public interface OnConfirmListener {
        void onConfirm();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(mLayoutId);

        btHigh = (Button) findViewById(R.id.bthigh);
        btmiddle = (Button) findViewById(R.id.btmiddle);
        btlow = (Button) findViewById(R.id.btlow);
        mConfirm= (Button) findViewById(R.id.btcomfirm);

        mConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mConfirmListener.onConfirm();
            }
        });

        btHigh.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View v) {
                mHighListener.onHigh();
                btHigh.setBackgroundColor(Color.parseColor("#0C3B95"));
                btHigh.setTextColor(Color.parseColor("#FFFFFF"));
                btmiddle.setBackgroundColor(Color.parseColor("#FFFFFF"));
                btmiddle.setTextColor(Color.parseColor("#E4E4E4"));
                btlow.setBackgroundColor(Color.parseColor("#FFFFFF"));
                btlow.setTextColor(Color.parseColor("#E4E4E4"));
                mConfirm.setBackgroundColor(Color.parseColor("#0C3B95"));
                mConfirm.setTextColor(Color.parseColor("#FFFFFF"));
            }
        });

        btmiddle.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View v) {
                mMiddleListener.onMiddle();
                btmiddle.setBackgroundColor(Color.parseColor("#0C3B95"));
                btmiddle.setTextColor(Color.parseColor("#FFFFFF"));
                btHigh.setBackgroundColor(Color.parseColor("#FFFFFF"));
                btHigh.setTextColor(Color.parseColor("#E4E4E4"));
                btlow.setBackgroundColor(Color.parseColor("#FFFFFF"));
                btlow.setTextColor(Color.parseColor("#E4E4E4"));
                mConfirm.setBackgroundColor(Color.parseColor("#0C3B95"));
                mConfirm.setTextColor(Color.parseColor("#FFFFFF"));
            }
        });

        btlow.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View v) {mLowListener.onLow();
                btlow.setBackgroundColor(Color.parseColor("#0C3B95"));
                btlow.setTextColor(Color.parseColor("#FFFFFF"));
                btHigh.setBackgroundColor(Color.parseColor("#FFFFFF"));
                btHigh.setTextColor(Color.parseColor("#E4E4E4"));
                btmiddle.setBackgroundColor(Color.parseColor("#FFFFFF"));
                btmiddle.setTextColor(Color.parseColor("#E4E4E4"));
                mConfirm.setBackgroundColor(Color.parseColor("#0C3B95"));
                mConfirm.setTextColor(Color.parseColor("#FFFFFF"));
            }
        });
    }

}
