package com.matie.redgram.ui.common.utils;

import android.content.Context;
import android.util.Log;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.matie.redgram.R;

import javax.inject.Inject;

/**
 * Created by matie on 16/07/15.
 */
public class DialogUtil {

    private Context mContext;
    private MaterialDialog.Builder mDialogBuilder;

    @Inject
    public DialogUtil(Context context) {
        mContext = context;
    }

    public void init(){
        mDialogBuilder = new MaterialDialog.Builder(mContext);
    }

    public MaterialDialog.Builder getDialogBuilder() {
        return mDialogBuilder;
    }

}
