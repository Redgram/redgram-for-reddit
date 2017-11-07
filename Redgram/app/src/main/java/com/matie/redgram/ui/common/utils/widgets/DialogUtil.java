package com.matie.redgram.ui.common.utils.widgets;

import android.content.Context;

import com.afollestad.materialdialogs.MaterialDialog;

import javax.inject.Inject;


public class DialogUtil {

    private Context context;

    @Inject
    public DialogUtil(Context context) {
        this.context = context;
    }

    public MaterialDialog.Builder build(){
        return new MaterialDialog.Builder(context);
    }

    public static MaterialDialog.Builder builder(Context context) {
        return new MaterialDialog.Builder(context);
    }
}
