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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
    private List<LinkClickableSpan> linkSpans;
    private List<CustomClickable> usernameSpans;
    private List<CustomClickable> subredditSpans;
    private List<StrikethroughSpan> strikeThroughSpans;
    private List<StyleSpan> styleSpans;
    private List<CodeSpan> codeSpans;
    private MDStyle style;

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
                    Color.rgb(0, 0, 0),
                    Color.rgb(211,211,211));
        }
        return style;
    }

    public MDParser parseCode() {
        if(spannableStringBuilder != null) {
            Matcher matcher = getMatches(MDHighlights.CODE.getPattern());
            while (matcher.find()) {

                BackgroundColorSpan codeHighlighterSpan = new BackgroundColorSpan(getStyle().getCodeBackgroundColor());
                ForegroundColorSpan foregroundColorSpan = new ForegroundColorSpan(getStyle().getCodeTextColor());
                TypefaceSpan monospace = new TypefaceSpan("monospace");

                clearSpansInRange(matcher, Object.class);

                CodeSpan codeSpan = new CodeSpan(codeHighlighterSpan, foregroundColorSpan, monospace);
                setSpan(codeSpan, matcher);

                if(codeSpans == null){
                    codeSpans = new ArrayList<>();
                }
                codeSpans.add(codeSpan);
            }
        }
        return this;
    }

    public MDParser parseStrike() {
        if(spannableStringBuilder != null) {
            Matcher matcher = getMatches(MDHighlights.STRIKE.getPattern());
            while (matcher.find()) {
                StrikethroughSpan strikethroughSpan = new StrikethroughSpan();
                setSpan(strikethroughSpan, matcher);
                if(strikeThroughSpans == null){
                    strikeThroughSpans = new ArrayList<>();
                }
                strikeThroughSpans.add(strikethroughSpan);
            }
        }
        return this;
    }

    public MDParser parseItalic() {
        if(spannableStringBuilder != null) {
            Matcher matcher = getMatches(MDHighlights.ITALICS.getPattern());
            while (matcher.find()) {
                StyleSpan styleSpan = new StyleSpan(Typeface.ITALIC);
                setSpan(styleSpan, matcher);
                if(styleSpans == null){
                    styleSpans = new ArrayList<>();
                }
                styleSpans.add(styleSpan);
            }
        }
        return this;
    }

    public MDParser parseBold() {
        if(spannableStringBuilder != null){
            Matcher matcher = getMatches(MDHighlights.BOLD.getPattern());
            while(matcher.find()){
                StyleSpan styleSpan = new StyleSpan(Typeface.BOLD);
                setSpan(styleSpan, matcher);
                if(styleSpans == null){
                    styleSpans = new ArrayList<>();
                }
                styleSpans.add(styleSpan);
            }
        }
        return this;
    }

    public MDParser parseLink(CustomSpanListener clickable) {
        if(spannableStringBuilder != null){
            Matcher matcher = getMatches(MDHighlights.LINK.getPattern());
            boolean matchFound = false;
            while(matcher.find()){
                //pass the actual string as data to the span
                HashMap<String, String> dataMap = new HashMap<>();
                dataMap.put(SPAN_URL, matcher.group(2));

                LinkClickableSpan customClickable = new LinkClickableSpan(clickable, true, dataMap);

                clearSpansInRange(matcher, CustomClickable.class);

                setSpan(customClickable, matcher);
                setSpan(new ForegroundColorSpan(getStyle().getLinkColor()), matcher);

                if(linkSpans == null){
                    linkSpans = new ArrayList<>();
                }
                linkSpans.add(customClickable);
            }
        }
        return this;
    }

    public MDParser parseSub(CustomSpanListener clickable) {
        if(spannableStringBuilder != null) {
            Matcher matcher = getMatches(MDHighlights.SUB.getPattern());
            while (matcher.find()) {
                //if next match has a link span then skip to the next match
                if(isLinkSpanApplied(matcher) || isCodeSpanApplied(matcher)){
                    continue;
                }
                CustomClickable customClickable = new CustomClickable(clickable, false);
                setSpan(customClickable, matcher);
                setSpan(new ForegroundColorSpan(getStyle().getLinkColor()), matcher);
                if(subredditSpans == null){
                    subredditSpans = new ArrayList<>();
                }
                subredditSpans.add(customClickable);
            }
        }
        return this;
    }

    public MDParser parseUser(CustomSpanListener clickable) {
        if(spannableStringBuilder != null) {
            Matcher matcher = getMatches(MDHighlights.USER.getPattern());
            while (matcher.find()) {
//                if next match has a link span then skip to the next match
                if(isLinkSpanApplied(matcher) || isCodeSpanApplied(matcher)){
                    continue;
                }
                CustomClickable customClickable = new CustomClickable(clickable, false);
                setSpan(customClickable, matcher);
                setSpan(new ForegroundColorSpan(getStyle().getLinkColor()), matcher);
                if(usernameSpans == null){
                    usernameSpans = new ArrayList<>();
                }
                usernameSpans.add(customClickable);
            }
        }
        return this;
    }

    public MDParser parseHeader() {
        if(spannableStringBuilder != null) {
            Matcher matcher = getMatches(MDHighlights.HEADER.getPattern());
            while (matcher.find()) {
                setSpan(new ForegroundColorSpan(getStyle().getHeaderColor()), matcher);
            }
        }
        return this;
    }

    private boolean isLinkSpanApplied(Matcher matcher) {
        if(spannableStringBuilder != null && linkSpans != null) {
            for (LinkClickableSpan span : linkSpans) {
                if (matcher.start() >= spannableStringBuilder.getSpanStart(span)
                        && matcher.end() <= spannableStringBuilder.getSpanEnd(span)) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean isCodeSpanApplied(Matcher matcher) {
        if(spannableStringBuilder != null && codeSpans != null){
            for(CodeSpan span: codeSpans){
                if (matcher.start() >= spannableStringBuilder.getSpanStart(span)
                        && matcher.end() <= spannableStringBuilder.getSpanEnd(span)) {
                    return true;
                }
            }
        }
        return false;
    }

    private void clearSpansInRange(Matcher matcher, Class kind) {
        if(spannableStringBuilder != null){
            Object[] spans = spannableStringBuilder.getSpans(matcher.start(), matcher.end(), kind);
            for(Object span: spans){
                spannableStringBuilder.removeSpan(span);
            }
        }
    }

    private Matcher getMatches(final Pattern pattern){
        return pattern.matcher(spannableStringBuilder);
    }

    private void setSpan(Object spanObject, Matcher matcher){
        spannableStringBuilder
                .setSpan(spanObject, matcher.start(), matcher.end(),
                        SpannableStringBuilder.SPAN_EXCLUSIVE_EXCLUSIVE);
    }

    public void build(){
        if(view != null){
            if(spannableStringBuilder != null){
                view.setText(spannableStringBuilder, TextView.BufferType.SPANNABLE);
            }else if(stringToParse != null){
                view.setText(stringToParse, TextView.BufferType.NORMAL);
            }
            view.setTextColor(getStyle().getTextColor());
        }
    }

}
