package com.matie.redgram.ui.common.utils.text;

import android.graphics.drawable.Drawable;

/**
 * Created by matie on 2015-12-13.
 *
 * Generic span listener. Only useful if it is triggered by a Span event.
 */
public interface CustomSpanListener {
    void onClickableEvent(CharSequence targetString);
}
