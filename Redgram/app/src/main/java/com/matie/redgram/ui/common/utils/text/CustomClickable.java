package com.matie.redgram.ui.common.utils.text;

import android.text.Spanned;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.TextView;

/**
 * Created by matie on 2015-12-13.
 */
public class CustomClickable extends ClickableSpan {

    private CustomSpanListener clickableListener;
    private boolean underlineText;

    public CustomClickable(CustomSpanListener clickableListener, boolean underlineText) {
        this.clickableListener = clickableListener;
        this.underlineText = underlineText;
    }

    @Override
    public void onClick(View widget) {

        if(widget instanceof TextView){
            TextView tv = (TextView) widget;
            if(tv.getText() instanceof Spanned){
                Spanned s = (Spanned) tv.getText();
                int start = s.getSpanStart(this);
                int end = s.getSpanEnd(this);
                if(clickableListener != null){
                    clickableListener.onClickableEvent(s.subSequence(start, end));
                }
            }
        }
    }

    @Override
    public void updateDrawState(TextPaint ds) {
        ds.setUnderlineText(underlineText);
    }
}
