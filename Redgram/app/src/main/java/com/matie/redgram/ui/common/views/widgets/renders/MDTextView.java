package com.matie.redgram.ui.common.views.widgets.renders;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.TextView;

import com.matie.redgram.ui.common.utils.text.CustomSpanListener;
import com.matie.redgram.ui.common.utils.text.MDStyle;

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
        //empty for now

    }

    /**
     * Parses the text for any Markdown and adds appropriate Markdown
     *
     * @param stringToParse
     */
    public void parse(String stringToParse, MDStyle style){
//        StringDecorator.newMDParser()
//                .setView(this)
//                .setText(stringToParse)
//                .parseBold(new StyleSpan(Typeface.BOLD),
//                        new ForegroundColorSpan(Color.rgb(204, 0, 0)))
//                .parseItalic(new StyleSpan(Typeface.ITALIC))
//                .parseStrike(new StrikethroughSpan())
//                .parseLink(new CustomClickable(this, true))
//                .build();
    }

    @Override
    public void onClickableEvent(CharSequence targetString) {
        Log.d("URL CLICKED", targetString.toString());
    }
}
