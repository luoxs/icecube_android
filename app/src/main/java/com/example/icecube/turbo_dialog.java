package com.example.icecube;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import java.util.Objects;

public class turbo_dialog extends Dialog {

    private Button btTurbo;
    private Button btEco;
    private Button mConfirm;

    private int mLayoutId;
    private OnConfirmListener mConfirmListener;
    private OnTurboListener mTurboistener;
    private OnEcoListener mEcoListener;

    /**
     *
     * @param context
     * @param layoutId
     * @param confirmListener
     * @param turboListener
     * @param ecoListener
     */

    public turbo_dialog(Context context, int layoutId, OnConfirmListener confirmListener, OnTurboListener turboListener,OnEcoListener ecoListener) {
        super(context,R.style.Theme_IceCube);
        // super(context, androidx.appcompat.R.style.Base_Theme_AppCompat_Dialog);
        mConfirmListener=confirmListener;
        mTurboistener = turboListener;
        mEcoListener = ecoListener;
        mLayoutId=layoutId;
    }

    //点击对应的监听器
    public interface OnTurboListener {
        void onTurbo();
    }

    //点击对应的监听器
    public interface OnEcoListener {
        void onEco();
    }

    //点击确定对应的监听器
    public interface OnConfirmListener {
        void onConfirm();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(mLayoutId);

        btTurbo = (Button) findViewById(R.id.btturbo);
        btEco = (Button) findViewById(R.id.bteco);
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


        btTurbo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTurboistener.onTurbo();
            }
        });

        btEco.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {mEcoListener.onEco();}
        });
    }

}
