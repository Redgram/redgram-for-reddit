package com.matie.redgram.ui.common.utils.text.spans;

import android.view.View;

import com.matie.redgram.ui.common.utils.text.CustomSpanListener;
import com.matie.redgram.ui.common.utils.text.spans.CustomClickable;

import java.util.HashMap;

/**
 * Created by matie on 2017-01-10.
 */
public class LinkClickableSpan extends CustomClickable {

    private HashMap<String, String> dataMap;

    public LinkClickableSpan(boolean underlineText, int textColor, HashMap<String, String> dataMap) {
        super(underlineText, textColor);
        this.dataMap = dataMap;
    }

    public LinkClickableSpan(CustomSpanListener clickableListener, boolean underlineText, int textColor, HashMap<String, String> dataMap) {
        super(clickableListener, underlineText, textColor);
        this.dataMap = dataMap;
    }

    @Override
    public void onClick(View widget) {
        super.onClick(widget);
        if(getClickableListener() != null){
            getClickableListener().onClickableEvent(dataMap);
        }
    }

    public HashMap<String, String> getDataMap() {
        return dataMap;
    }
}
