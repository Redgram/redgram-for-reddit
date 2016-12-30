package com.matie.redgram.ui.common.utils.text;

import android.text.Spanned;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.TextView;

import java.util.HashMap;

/**
 * Created by matie on 2015-12-13.
 */
public class CustomClickable extends ClickableSpan {

    private CustomSpanListener clickableListener;
    private boolean underlineText;
    private HashMap<String, String> dataMap;
    private String type = CustomSpanListener.NORMAL;

    public CustomClickable(CustomSpanListener clickableListener, boolean underlineText) {
        this.clickableListener = clickableListener;
        this.underlineText = underlineText;
    }

    public CustomClickable(CustomSpanListener clickableListener, HashMap<String,String> dataMap, boolean underlineText) {
        this.clickableListener = clickableListener;
        this.dataMap = dataMap;
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
                    clickableListener.onClickableEvent(dataMap);
                }
            }
        }
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public HashMap<String, String> getDataMap() {
        return dataMap;
    }

    @Override
    public void updateDrawState(TextPaint ds) {
        ds.setUnderlineText(underlineText);
    }
}
