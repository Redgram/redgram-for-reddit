package com.matie.redgram.ui.common.utils.text;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.text.SpannableStringBuilder;
import android.text.method.LinkMovementMethod;
import android.text.style.ForegroundColorSpan;
import android.text.style.StrikethroughSpan;
import android.text.style.StyleSpan;
import android.util.Log;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by matie on 2015-11-22.
 *
 * Responsible for all text decorations
 */
public class StringDecorator {

    //cannot be instantiated
    private StringDecorator(){}

    public static SpannableBuilder newSpannableBuilder(Context context){
        return new SpannableBuilder(context);
    }
    public static SpannableBuilder newSpannableBuilder(Context context, CharSequence charSequence){
        return new SpannableBuilder(context, charSequence);
    }
    public static SpannableBuilder newSpannableBuilder(Context context, CharSequence charSequence, int start, int end){
        return new SpannableBuilder(context, charSequence, start, end);
    }
    public static SpanContainer newSpanContainer(Object resource, int flag){
        return new SpanContainer(resource, flag);
    }
    public static MDParser newMDParser(){
        return new MDParser();
    }

    /**
     * Holds the span and the flag and could be used multiple times
     */
    public static class SpanContainer{
        private Object span;
        private int flag;

        public SpanContainer(Object span, int flag) {
            this.flag = flag;
            this.span = span;
        }

        public Object getSpan() {
            return span;
        }

        public int getFlag() {
            return flag;
        }
    }

    public static class SpannableBuilder {

        private Context context;
        private TextView textView;
        private SpannableStringBuilder sb;
        private int lastAddedOffset;

        public SpannableBuilder(Context context) {
            this.context = context;
            this.sb = new SpannableStringBuilder();
            this.lastAddedOffset = sb.length(); //starts 0, always update before append
        }

        public SpannableBuilder(Context context, CharSequence charSequence) {
            this.context = context;
            this.sb = new SpannableStringBuilder(charSequence);
        }

        public SpannableBuilder(Context context, CharSequence charSequence, int start, int end) {
            this.context = context;
            this.sb = new SpannableStringBuilder(charSequence, start, end);
        }

        public SpannableStringBuilder getBuilder() {
            return sb;
        }

        public SpannableBuilder setBuilder(SpannableStringBuilder sb) {
            this.sb = sb;
            return this;
        }

        //has to be set before calling build
        public SpannableBuilder setTextView(TextView textView) {
            this.textView = textView;
            return this;
        }

        /**
         * Updates lastAddedOffset and append new text
         * @param text
         * @return
         */
        public SpannableBuilder append(String text){
            lastAddedOffset = sb.length();
            sb.append(text);
            return this;
        }

        /**
         * Appends text with a specified span and flags
         * @param text
         * @param resource
         * @param flag
         * @return
         */
        public SpannableBuilder append(String text, Object resource, int flag){

            int start = sb.length();
            lastAddedOffset = start;

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                sb.append(text, resource, flag);
            }else{
                //todo: alternative - from source code of append?
                sb.append(text);
                sb.setSpan(resource, start, sb.length(), flag);
            }
            return this;
        }

        /**
         * Spans on the last appended text with flags
         * @param resource
         * @param flag
         * @return
         */
        public SpannableBuilder span(Object resource, int flag) {
            sb.setSpan(resource, lastAddedOffset, sb.length(), flag);
            return this;
        }

        /**
         * Spans on a subset of existing text/recently appended text with flags
         * @param resource
         * @param start
         * @param end
         * @param flag
         * @return
         */
        public SpannableBuilder span(Object resource, int start, int end, int flag){
            if(start >= 0 && end <= sb.length() && start <= end){
                sb.setSpan(resource, start, end, flag);
            }else{
                Log.d("StringUtil", "Check indexes passed to span");
            }
            return this;
        }

        public SpannableBuilder appendList(String text, List<SpanContainer> spanContainers){
            return append(text).spanList(spanContainers);
        }

        public SpannableBuilder spanList(List<SpanContainer> spanContainers){
            for(SpanContainer spanContainer : spanContainers){
                span(spanContainer.getSpan(), spanContainer.getFlag());
            }
            return this;
        }

        public SpannableBuilder spanRangeList(List<SpanContainer> spanContainers, int start, int end){
            if(start >= 0 && end <= sb.length() && start <= end){
                for(SpanContainer spanContainer : spanContainers){
                    span(spanContainer.getSpan(), start, end, spanContainer.getFlag());
                }
            }else{
                Log.d("StringUtil", "Check indexes passed to spanRangeList");
            }
            return this;
        }

        public SpannableBuilder clickable() {
            if(textView != null){
                textView.setMovementMethod(LinkMovementMethod.getInstance());
            }
            return this;
        }

        public void build() {
            build(TextView.BufferType.NORMAL);
        }

        public void buildEditable() {
            build(TextView.BufferType.EDITABLE);
        }

        public void buildSpannable() {
            build(TextView.BufferType.SPANNABLE);
        }

        private void build(TextView.BufferType type){
            try{
                textView.setText(sb, type);
            }catch (NullPointerException e){
                Log.d("NullPointer", SpannableBuilder.class.getName() + "#build - text view is not set");
            }
        }

    }

    public static class MDParser{
        public static final String SPAN_URL = "SPAN_URL";
        //note EditText is a TextView
        private TextView view;
        private SpannableStringBuilder spannableStringBuilder;
        private CharSequence stringToParse;

        private MDParser() {}

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

        public MDParser parseStrike() {
            if(spannableStringBuilder != null) {
                Matcher matcher = getMatches(MDHighlights.STRIKE.getPattern());
                while (matcher.find()) {
                    setSpan(new StrikethroughSpan(), matcher);
                }
            }
            return this;
        }

        public MDParser parseItalic() {
            if(spannableStringBuilder != null) {
                Matcher matcher = getMatches(MDHighlights.ITALICS.getPattern());
                while (matcher.find()) {
                    setSpan(new StyleSpan(Typeface.ITALIC), matcher);
                    setSpan(new ForegroundColorSpan(Color.rgb(100, 20, 150)), matcher);
                }
            }
            return this;
        }

        public MDParser parseBold() {
            if(spannableStringBuilder != null){
                Matcher matcher = getMatches(MDHighlights.BOLD.getPattern());
                while(matcher.find()){
                    setSpan(new StyleSpan(Typeface.BOLD), matcher);
                    setSpan(new ForegroundColorSpan(Color.rgb(140, 0, 50)), matcher);
                }
            }
            return this;
        }

        public MDParser parseLink(CustomSpanListener clickable) {
            if(spannableStringBuilder != null){
                Matcher matcher = getMatches(MDHighlights.LINK.getPattern());
                while(matcher.find()){
                    //pass the actual string as data to the span
                    HashMap<String, String> dataMap = new HashMap<>();
                    dataMap.put(SPAN_URL, matcher.group(2));

                    CustomClickable customClickable = new CustomClickable(clickable, dataMap, true);
                    customClickable.setType(CustomSpanListener.URL);

                    setSpan(customClickable, matcher);
                    setSpan(new ForegroundColorSpan(Color.rgb(204, 0, 0)), matcher);
                }
            }
            return this;
        }

        public MDParser parseSub(CustomSpanListener clickable) {
            if(spannableStringBuilder != null) {
                Matcher matcher = getMatches(MDHighlights.SUB.getPattern());
                while (matcher.find()) {
                    //if next match has a link span then skip to the next match
                    if(isLinkSpanApplied(matcher)){
                        continue;
                    }
                    setSpan(new CustomClickable(clickable, false), matcher);
                    setSpan(new ForegroundColorSpan(Color.rgb(204, 100, 0)), matcher);
                }
            }
            return this;
        }

        public MDParser parseUser(CustomSpanListener clickable) {
            if(spannableStringBuilder != null) {
                Matcher matcher = getMatches(MDHighlights.USER.getPattern());
                while (matcher.find()) {
//                    if next match has a link span then skip to the next match
                    if(isLinkSpanApplied(matcher)){
                        continue;
                    }
                    setSpan(new CustomClickable(clickable, false), matcher);
                    setSpan(new ForegroundColorSpan(Color.rgb(204, 200, 0)), matcher);
                }
            }
            return this;
        }

        private boolean isLinkSpanApplied(Matcher matcher) {
            CustomClickable[] customClickables = spannableStringBuilder.getSpans(0, spannableStringBuilder.length(), CustomClickable.class);
            for(CustomClickable span : customClickables){
                if(CustomSpanListener.URL.equalsIgnoreCase(span.getType())
                        && (matcher.start() >= spannableStringBuilder.getSpanStart(span)
                        &&  matcher.end() <= spannableStringBuilder.getSpanEnd(span))){
                    return true;
                }
            }
            return false;
        }

        public MDParser parseHeader() {
            if(spannableStringBuilder != null) {
                Matcher matcher = getMatches(MDHighlights.HEADER.getPattern());
                while (matcher.find()) {
                    setSpan(new ForegroundColorSpan(Color.rgb(204, 0, 0)), matcher);
                }
            }
            return this;
        }

        private Matcher getMatches(final Pattern pattern){
            return pattern.matcher(spannableStringBuilder);
        }

        private void setSpan(Object spanObject, Matcher matcher){
            spannableStringBuilder.setSpan(spanObject, matcher.start(), matcher.end(), SpannableStringBuilder.SPAN_EXCLUSIVE_EXCLUSIVE);
        }

        public void build(){
            if(view != null){
                if(spannableStringBuilder != null){
                    view.setText(spannableStringBuilder, TextView.BufferType.SPANNABLE);
                }else if(stringToParse != null){
                    view.setText(stringToParse, TextView.BufferType.NORMAL);
                }
            }
        }
    }

}
