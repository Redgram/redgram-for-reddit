package com.matie.redgram.ui.common.utils.widgets;

import android.content.Context;
import com.afollestad.materialdialogs.MaterialDialog;

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

    public MaterialDialog.Builder build(){
        return new MaterialDialog.Builder(mContext);
    }

}
