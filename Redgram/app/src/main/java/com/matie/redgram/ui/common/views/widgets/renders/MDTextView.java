package com.matie.redgram.ui.common.views.widgets.renders;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.text.method.LinkMovementMethod;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.TextView;

import com.matie.redgram.ui.common.utils.text.CustomSpanListener;
import com.matie.redgram.ui.common.utils.text.MDParser;
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

//        stringToParse = "~~*italicstrike*~~ \n" +
//                "\n" +
//                "or\n" +
//                "\n" +
//                ">hello\n" +
//                ">>world\n" +
//                "\n" +
//                "Test 2\n" +
//                "\n" +
//                "[Maybe a link too and `some code`..[another link](www.google.com)..I guess](www.google.com)\n" +
//                "\n" +
//                "`[Maybe a link too and `some code`..[another link](www.google.com)..I guess](www.google.com)`\n" +
//                "\n" +
//                "##hello\n" +
//                "\n" +
//                "`##hello`\n" +
//                "\n" +
//                "Test:\n" +
//                "\n" +
//                "[this /r/redditdev post **hello**](https://www.reddit.com/r/redditdev/comments/37nmt7/upcoming_changes_to_subreddit_and_user_links_in/)\n" +
//                "\n" +
//                "/r/redditdev\n" +
//                "\\/r/redditdev\n" +
//                "r/redditdev\n" +
//                "\\r/redditdev\n" +
//                "\n" +
//                "`*italic* within code" +
//                "" +
//                "" +
//                " and in addition there is **bold** and ~~a strike~~, is it working?`\n" +
//                "\n" +
//                "*`code` within italic* " +
//                "" +
//                "\n" +
//                "\n" +
//                "[here is a link with `code` in it](http://www.google.com)\n" +
//                "\n" +
//                "[here is a link with `code and *italic*` in it](http://www.google.com)\n" +
//                "\n" +
//                "[here is a link with *italic and `code`* in it](http://www.google.com) \n" +
//                "" +
//                "~~stike with `code`~~";


        StringDecorator.newMDParser()
                .setView(this)
                .setText(stringToParse)
                .setStyle(style)
                .addSpanListener(this)
                .build();

    }

    @Override
    public void onClickableEvent(CharSequence targetString) {
        Log.d("URL CLICKED", targetString.toString());
    }

    @Override
    public void onClickableEvent(HashMap<String, String> data) {
        if(data != null && data.containsKey(MDParser.SPAN_URL)){
            Log.d("URL CLICKED", data.get(MDParser.SPAN_URL));
        }
    }
}
