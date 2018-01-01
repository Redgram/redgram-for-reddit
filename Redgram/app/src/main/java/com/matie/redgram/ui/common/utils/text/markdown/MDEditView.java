package com.matie.redgram.ui.common.utils.text.markdown;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.EditText;

/**
 * Modified EditText that listens to changes and renders Markdown
 *
 * Created by matie on 2016-11-17.
 */
public class MDEditView extends EditText {
    public MDEditView(Context context) {
        super(context);
    }

    public MDEditView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MDEditView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public MDEditView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }
}
