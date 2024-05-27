package com.example.icecube;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


public class mode_dialog extends Dialog {

    private Button btHigh;
    private Button btmiddle;
    private Button btlow;
    private Button mConfirm;

    private int mLayoutId;
    private OnConfirmListener mConfirmListener;
    private OnHighListener mHighListener;
    private OnmiddleListener mMiddleListener;
    private OnLowLstener mLowLstener;

    /**
     *
     * @param context
     * @param layoutId
     * @param confirmListener
     * @param highListener
     * @param middleListener
     * @param lowLstener
     */

    public mode_dialog(Context context, int layoutId, OnConfirmListener confirmListener, OnHighListener highListener,OnmiddleListener middleListener,OnLowLstener lowLstener) {
        super(context,R.style.CustomButtonStyle);
       // super(context, androidx.appcompat.R.style.Base_Theme_AppCompat_Dialog);
        mConfirmListener=confirmListener;
        mHighListener = highListener;
        mMiddleListener = middleListener;
        mLowLstener = lowLstener;
        mLayoutId=layoutId;
    }

    //点击取消对应的监听器
    public interface OnHighListener {
        void onHigh();
    }

    //点击取消对应的监听器
    public interface OnmiddleListener {
        void onHigh();
    }

    //点击取消对应的监听器
    public interface OnLowLstener {
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
    }

}
