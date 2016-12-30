package com.matie.redgram.ui.common.utils.text;

import java.util.HashMap;

/**
 * Created by matie on 2015-12-13.
 *
 * Generic span listener. Only useful if it is triggered by a Span event.
 */
public interface CustomSpanListener {

    public static final String URL = "type-url";
    public static final String NORMAL = "type-normal";

    /**
     *
     * @param targetString actual string on the spanned string
     */
    void onClickableEvent(CharSequence targetString);

    /**
     *
     * @param data data passed with the custom span in a form of a HashMap
     */
    void onClickableEvent(HashMap<String, String> data);
}
