package com.matie.redgram.ui.common.utils.text;

import android.graphics.Color;
import android.graphics.Typeface;
import android.text.SpannableStringBuilder;
import android.text.style.BackgroundColorSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.StrikethroughSpan;
import android.text.style.StyleSpan;
import android.text.style.TypefaceSpan;
import android.widget.TextView;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by matie on 2017-01-08.
 */
public class MDParser {

    public static final String SPAN_URL = "SPAN_URL";
    //note EditText is a TextView
    private TextView view;
    private SpannableStringBuilder spannableStringBuilder;
    private CharSequence stringToParse;
    private CustomSpanListener spanListener;
    private MDStyle style;

    boolean codeSpansApplied = false;
    boolean linkSpansApplied = false;

    public MDParser() {}

    public TextView getView() {
        return view;
    }

    public MDParser setView(TextView view) {
        this.view = view;
        return this;
    }

    public SpannableStringBuilder getSpannableStringBuilder() {
        return spannableStringBuilder;
    }

    public CharSequence getStringToParse() {
        return stringToParse;
    }

    public MDParser setText(CharSequence stringToParse){
        if(stringToParse == null || stringToParse.length() == 0){
            return this;
        }else{
            this.stringToParse = stringToParse;
            spannableStringBuilder = new SpannableStringBuilder(stringToParse);
        }

        return this;
    }


    public MDParser addSpanListener(CustomSpanListener listener) {
        this.spanListener = listener;
        return this;
    }

    protected void parse(){
        int length = spannableStringBuilder.length();
        parseLink(0, length);
        parseUser(0, length);
        parseSub(0, length);
        parseBold(0, length);
        parseItalic(0, length);
        parseStrike(0, length);
        parseCode(0, length);
    }

    public MDParser setStyle(MDStyle style){
        this.style = style;
        return this;
    }

    public MDStyle getStyle(){
        if(style == null){
            //default style
            style = new MDStyle(Color.rgb(128,128,128),
                    Color.rgb(0, 0, 0),
                    Color.rgb(6,69,173),
                    false,
                    Color.rgb(0, 0, 0),
                    Color.rgb(211,211,211));
        }
        return style;
    }

    protected MDParser parseCode(int start, int end) {
        if(spannableStringBuilder != null) {
            Matcher matcher = getMatches(MDHighlights.CODE.getPattern(), start, end);
            while (matcher.find()) {

                BackgroundColorSpan codeHighlighterSpan = new BackgroundColorSpan(getStyle().getCodeBackgroundColor());
                ForegroundColorSpan foregroundColorSpan = new ForegroundColorSpan(getStyle().getCodeTextColor());
                TypefaceSpan monospace = new TypefaceSpan("monospace");

                clearSpansInCodeRange(matcher);

                CodeSpan codeSpan;
                if(isLinkSpanApplied(matcher)){
                    codeSpan = new CodeSpan(codeHighlighterSpan, monospace);
                }else{
                    codeSpan = new CodeSpan(codeHighlighterSpan, foregroundColorSpan, monospace);
                }

                setSpan(codeSpan, matcher.start(), matcher.end());
            }
        }
        codeSpansApplied = true;
        return this;
    }

    protected MDParser parseStrike(int start, int end) {
        if(spannableStringBuilder != null) {
            Matcher matcher = getMatches(MDHighlights.STRIKE.getPattern(), start, end);
            while (matcher.find()) {
                if(codeSpansApplied && isCodeSpanApplied(matcher)){
                    continue;
                }
                StrikethroughSpan strikethroughSpan = new StrikethroughSpan();
                setSpan(strikethroughSpan, matcher.start(), matcher.end());
            }
        }
        return this;
    }

    protected MDParser parseItalic(int start, int end) {
        if(spannableStringBuilder != null) {
            Matcher matcher = getMatches(MDHighlights.ITALICS.getPattern(), start, end);
            while (matcher.find()) {
                if(codeSpansApplied && isCodeSpanApplied(matcher)){
                    continue;
                }
                StyleSpan styleSpan = new StyleSpan(Typeface.ITALIC);
                setSpan(styleSpan, matcher.start(), matcher.end());
            }
        }
        return this;
    }

    protected MDParser parseBold(int start, int end) {
        if(spannableStringBuilder != null){
            Matcher matcher = getMatches(MDHighlights.BOLD.getPattern(), start, end);
            while(matcher.find()){
                if(codeSpansApplied && isCodeSpanApplied(matcher)){
                    continue;
                }
                StyleSpan styleSpan = new StyleSpan(Typeface.BOLD);
                setSpan(styleSpan, matcher.start(), matcher.end());
            }
        }
        return this;
    }

    protected MDParser parseLink(int start, int end) {
        if(spannableStringBuilder != null){
            Matcher matcher = getMatches(MDHighlights.LINK.getPattern(), start, end);
            while(matcher.find()){
                if(codeSpansApplied && isCodeSpanApplied(matcher)){
                    continue;
                }
                //pass the actual string as data to the span
                HashMap<String, String> dataMap = new HashMap<>();
                dataMap.put(SPAN_URL, matcher.group(2));

                LinkClickableSpan customClickable;
                if(spanListener != null){
                    customClickable = new LinkClickableSpan(spanListener, getStyle().isUnderlineLink(), getStyle().getLinkColor(), dataMap);
                }else{
                    customClickable = new LinkClickableSpan(getStyle().isUnderlineLink(), getStyle().getLinkColor(), dataMap);
                }

                clearSpansInRange(matcher, CustomClickable.class);

                setSpan(customClickable, matcher.start(), matcher.end());
            }
        }
        linkSpansApplied = true;
        return this;
    }

    protected MDParser parseSub(int start, int end) {
        if(spannableStringBuilder != null) {
            Matcher matcher = getMatches(MDHighlights.SUB.getPattern(), start, end);
            while (matcher.find()) {
                //if next match has a link span then skip to the next match
                if((linkSpansApplied && isLinkSpanApplied(matcher)) || (codeSpansApplied && isCodeSpanApplied(matcher))){
                    continue;
                }

                CustomClickable customClickable;
                if(spanListener != null){
                    customClickable = new CustomClickable(spanListener, true, getStyle().getLinkColor());
                }else{
                    customClickable = new CustomClickable(true, getStyle().getLinkColor());
                }

                setSpan(customClickable, matcher.start(), matcher.end());
            }
        }
        return this;
    }

    protected MDParser parseUser(int start, int end) {
        if(spannableStringBuilder != null) {
            Matcher matcher = getMatches(MDHighlights.USER.getPattern(), start, end);
            while (matcher.find()) {
//                if next match has a link span then skip to the next match
                if((linkSpansApplied && isLinkSpanApplied(matcher)) || (codeSpansApplied && isCodeSpanApplied(matcher))){
                    continue;
                }

                CustomClickable customClickable;
                if(spanListener != null){
                    customClickable = new CustomClickable(spanListener, true, getStyle().getLinkColor());
                }else{
                    customClickable = new CustomClickable(true, getStyle().getLinkColor());
                }

                setSpan(customClickable, matcher.start(), matcher.end());
            }
        }
        return this;
    }

    protected MDParser parseHeader(int start, int end) {
        if(spannableStringBuilder != null) {
            Matcher matcher = getMatches(MDHighlights.HEADER.getPattern(), start, end);
            while (matcher.find()) {
                setSpan(new ForegroundColorSpan(getStyle().getHeaderColor()), matcher.start(), matcher.end());
            }
        }
        return this;
    }

    private boolean isLinkSpanApplied(Matcher matcher) {
        LinkClickableSpan[] spans = spannableStringBuilder.getSpans(matcher.start(), matcher.end(), LinkClickableSpan.class);
        if(spannableStringBuilder != null) {
            for (LinkClickableSpan span : spans) {
                if (spannableStringBuilder.getSpanStart(span) <= matcher.start()
                        && spannableStringBuilder.getSpanEnd(span) >= matcher.end()) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean isCodeSpanApplied(Matcher matcher) {
        CodeSpan[] spans = spannableStringBuilder.getSpans(matcher.start(), matcher.end(), CodeSpan.class);
        if(spannableStringBuilder != null){
            for(CodeSpan span: spans){
                if (spannableStringBuilder.getSpanStart(span) <= matcher.start()
                        && spannableStringBuilder.getSpanEnd(span) >= matcher.end()) {
                    return true;
                }
            }
        }
        return false;
    }

    private void clearSpansInCodeRange(Matcher matcher) {
        if(spannableStringBuilder != null){
            Object[] spans = spannableStringBuilder.getSpans(matcher.start(), matcher.end(), Object.class);
            for(Object span: spans){
                if(span instanceof StyleSpan
                        && (spannableStringBuilder.getSpanStart(span) <= matcher.start()
                            && spannableStringBuilder.getSpanEnd(span) >= matcher.end())){
                    int spanStart = spannableStringBuilder.getSpanStart(span);
                    int spanEnd = spannableStringBuilder.getSpanEnd(span);
                    spannableStringBuilder.removeSpan(span);
                    if(Typeface.BOLD == ((StyleSpan) span).getStyle()){
                        setSpan(new StyleSpan(Typeface.BOLD), spanStart, matcher.start());
                        setSpan(new StyleSpan(Typeface.BOLD), matcher.end(), spanEnd);
                    }else if(Typeface.ITALIC == ((StyleSpan) span).getStyle()){
                        setSpan(new StyleSpan(Typeface.ITALIC), spanStart, matcher.start());
                        setSpan(new StyleSpan(Typeface.ITALIC), matcher.end(), spanEnd);
                    }
                }else if(!(span instanceof LinkClickableSpan)
                            && !(span instanceof StrikethroughSpan
                                    && spannableStringBuilder.getSpanStart(span) <= matcher.start()
                                    && spannableStringBuilder.getSpanEnd(span) >= matcher.end())){
                    spannableStringBuilder.removeSpan(span);
                }
            }
        }
    }

    private void clearSpansInRange(Matcher matcher, Class kind) {
        if(spannableStringBuilder != null){
            Object[] spans = spannableStringBuilder.getSpans(matcher.start(), matcher.end(), kind);
            for(Object span: spans){
                spannableStringBuilder.removeSpan(span);
            }
        }
    }

    private Matcher getMatches(final Pattern pattern, final int start, final int end){
        return pattern.matcher(spannableStringBuilder.subSequence(start, end));
    }

    private void setSpan(Object spanObject, int start, int end){
        spannableStringBuilder
                .setSpan(spanObject, start, end,
                        SpannableStringBuilder.SPAN_EXCLUSIVE_EXCLUSIVE);
    }

    public void build(){
        if(view != null){
            if(spannableStringBuilder != null){
                parse();

                view.setText(spannableStringBuilder, TextView.BufferType.SPANNABLE);
            }else if(stringToParse != null){
                view.setText(stringToParse, TextView.BufferType.NORMAL);
            }
            view.setTextColor(getStyle().getTextColor());
        }
    }

    public CustomSpanListener getSpanListener() {
        return spanListener;
    }

}
