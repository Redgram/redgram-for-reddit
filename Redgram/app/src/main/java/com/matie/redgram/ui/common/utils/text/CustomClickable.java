package com.matie.redgram.ui.common.utils.text;

import android.text.Spanned;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

/**
 * Created by matie on 2015-12-13.
 */
public class CustomClickable extends ClickableSpan {

    private CustomClickableListener clickableListener;

    public CustomClickable(CustomClickableListener clickableListener) {
        this.clickableListener = clickableListener;
    }

    @Override
    public void onClick(View widget) {

        if(widget instanceof TextView){
            TextView tv = (TextView) widget;
            if(tv.getText() instanceof Spanned){
                Spanned s = (Spanned) tv.getText();
                int start = s.getSpanStart(this);
                int end = s.getSpanEnd(this);
                clickableListener.onClickableEvent(s.subSequence(start, end));
            }
        }
    }
}
