package com.matie.redgram.ui.common.views.widgets.renders;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.text.SpannableString;
import android.util.AttributeSet;
import android.widget.TextView;

import com.matie.redgram.ui.common.utils.text.MDStyle;
import com.matie.redgram.ui.common.utils.text.StringDecorator;

/**
 * Renders Markdown
 *
 * Created by matie on 2016-11-17.
 */
public class MDTextView extends TextView {

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
        //empty for now

    }

    /**
     * Parses the text for any Markdown and adds appropriate Markdown
     *
     * @param stringToParse
     */
    public void parse(String stringToParse, MDStyle style){
        StringDecorator.MDParser parser = StringDecorator.newMDParser();
        parser.setStyle(style);
        //modifies the string by adding spans
        SpannableString spannableString = parser.parse(stringToParse);
        if(spannableString != null){
            setText(spannableString, BufferType.SPANNABLE);
        }
    }
}
