package com.matie.redgram.ui.common.utils.text;

import android.view.View;

import java.util.HashMap;

/**
 * Created by matie on 2017-01-10.
 */
public class LinkClickableSpan extends CustomClickable {

    private HashMap<String, String> dataMap;

    public LinkClickableSpan(CustomSpanListener clickableListener, boolean underlineText, HashMap<String, String> dataMap) {
        super(clickableListener, underlineText);
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
