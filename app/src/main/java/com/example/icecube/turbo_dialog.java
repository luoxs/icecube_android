package com.example.icecube;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class turbo_dialog extends Dialog {

    private Button btturbo;
    private Button bteco;
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

    //点击取消对应的监听器
    public interface OnTurboListener {
        void onHigh();
    }

    //点击取消对应的监听器
    public interface OnEcoListener {
        void onHigh();
    }

    //点击确定对应的监听器
    public interface OnConfirmListener {
        void onConfirm();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(mLayoutId);

        btturbo = (Button) findViewById(R.id.btturbo);
        bteco = (Button) findViewById(R.id.bteco);
        mConfirm= (Button) findViewById(R.id.btcomfirm);

        mConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mConfirmListener.onConfirm();
            }
        });
    }

}
