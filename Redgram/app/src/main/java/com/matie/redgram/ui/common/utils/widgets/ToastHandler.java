package com.matie.redgram.ui.common.utils.widgets;

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

    @Deprecated
    public void showToast(String msg, int length){
        Toast.makeText(mContext, msg, length).show();
    }

    @Deprecated
    public void showBackgroundToast(String msg, int length){
        handler.post(() -> Toast.makeText(mContext, msg, length).show());
    }

    public static void showToast(Context context, String msg, int length) {
        Toast.makeText(context, msg, length).show();
    }

    public static void showBackgroundToast(Context context, String msg, int length) {
        new Handler(Looper.getMainLooper()).post(() -> Toast.makeText(context, msg, length).show());
    }


}
