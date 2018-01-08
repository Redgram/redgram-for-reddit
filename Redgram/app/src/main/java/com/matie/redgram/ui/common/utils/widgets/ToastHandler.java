package com.matie.redgram.ui.common.utils.widgets;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import javax.inject.Inject;

public class ToastHandler {

    private final Context context;
    private final Handler handler;

    @Inject
    public ToastHandler(Context context) {
        this.context = context;
        this.handler = new Handler(Looper.getMainLooper());
    }

    @Deprecated
    public void showToast(String msg, int length){
        Toast.makeText(context, msg, length).show();
    }

    @Deprecated
    public void showBackgroundToast(String msg, int length){
        handler.post(() -> Toast.makeText(context, msg, length).show());
    }

    public static void showToast(Context context, String msg, int length) {
        Toast.makeText(context, msg, length).show();
    }

    public static void showBackgroundToast(Context context, String msg, int length) {
        new Handler(Looper.getMainLooper()).post(() -> Toast.makeText(context, msg, length).show());
    }


}
