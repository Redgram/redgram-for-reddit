package com.matie.redgram.ui.common.views.widgets.renders;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.text.method.LinkMovementMethod;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.TextView;

import com.matie.redgram.ui.common.utils.text.CustomSpanListener;
import com.matie.redgram.ui.common.utils.text.MDStyle;
import com.matie.redgram.ui.common.utils.text.StringDecorator;

import java.util.HashMap;

/**
 * Renders Markdown
 *
 * Created by matie on 2016-11-17.
 */
public class MDTextView extends TextView implements CustomSpanListener {

    public MDTextView(Context context) {
        super(context);
        init();
    }

    public MDTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MDTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public MDTextView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        setMovementMethod(LinkMovementMethod.getInstance());
    }

    /**
     * Parses the text for any Markdown and adds appropriate Markdown
     *
     * @param stringToParse
     */
    public void parse(String stringToParse, MDStyle style){
        StringDecorator.newMDParser()
                .setView(this)
                .setText(stringToParse)
                .parseLink(this)
                .parseUser(this)
                .parseSub(this)
                .parseBold()
                .parseItalic()
                .parseStrike()
                .build();

    }

    @Override
    public void onClickableEvent(CharSequence targetString) {
        Log.d("URL CLICKED", targetString.toString());
    }

    @Override
    public void onClickableEvent(HashMap<String, String> data) {
        if(data != null && data.containsKey(StringDecorator.MDParser.SPAN_URL)){
            Log.d("URL CLICKED", data.get(StringDecorator.MDParser.SPAN_URL));
        }
    }
}
