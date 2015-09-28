package com.matie.redgram.ui.common.utils;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import javax.inject.Inject;

/**
 * Created by matie on 23/09/15.
 */
public class ToastHandler {

    private Context mContext;
    private Handler handler;

    @Inject
    public ToastHandler(Context context) {
        mContext = context;
        handler = new Handler(Looper.getMainLooper());
    }

    public void showToast(String msg, int length){
        Toast.makeText(mContext, msg, length).show();
    }

    public void showBackgroundToast(String msg, int length){
        handler.post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(mContext, msg, length).show();
            }
        });
    }


}
