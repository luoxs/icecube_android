package com.example.icecube;

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
        super(context,R.style.CustomButtonStyle);
       // super(context, androidx.appcompat.R.style.Base_Theme_AppCompat_Dialog);
        mConfirmListener=confirmListener;
        mHighListener = highListener;
        mMiddleListener = middleListener;
        mLowListener = lowListener;
        mLayoutId=layoutId;
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
       // getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

    }

    //点击取消对应的监听器
    public interface OnHighListener {
        void onHigh();
    }

    //点击取消对应的监听器
    public interface OnmiddleListener {
        void onMiddle();
    }

    //点击取消对应的监听器
    public interface OnLowListener {
        void onLow();
    }

    //点击确定对应的监听器
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

        // 设置对话框样式，这里使用了无标题和透明的背景
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
        Objects.requireNonNull(getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        // 设置对话框外的背景变暗
        WindowManager.LayoutParams layoutParams = getWindow().getAttributes();
        layoutParams.dimAmount = 0.2f; // 设置背景变暗的程度，0.0表示不变暗，1.0表示全黑
        getWindow().setAttributes(layoutParams);

        mConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mConfirmListener.onConfirm();
            }
        });

        btHigh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mHighListener.onHigh();
            }
        });

        btmiddle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMiddleListener.onMiddle();
            }
        });

        btlow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {mLowListener.onLow();}
        });
    }

}
