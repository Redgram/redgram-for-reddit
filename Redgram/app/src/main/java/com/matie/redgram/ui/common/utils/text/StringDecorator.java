package com.matie.redgram.ui.common.utils.text;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.method.LinkMovementMethod;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.widget.TextView;

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

        private MDStyle style;

        public MDStyle getStyle() {
            return style;
        }

        public void setStyle(MDStyle style) {
            this.style = style;
        }

        public SpannableString parse(CharSequence stringToParse){

            SpannableString spannableString;

            if(stringToParse == null || stringToParse.length() == 0){
                return null;
            }else{
                spannableString = new SpannableString(stringToParse);
            }

            if(style == null){
                //default styling
                style = new MDStyle(Color.rgb(204, 0, 0), Color.rgb(204, 0, 0), Color.rgb(204, 0, 0));
            }

            parseHeader(spannableString, MDHighlights.HEADER, style);
            parseLink(spannableString, MDHighlights.LINK, style);
            parseBold(spannableString, MDHighlights.BOLD, style);
            parseItalic(spannableString, MDHighlights.ITALICS, style);
            parseStrike(spannableString, MDHighlights.STRIKE, style);
            parseUser(spannableString, MDHighlights.USER, style);
            parseSub(spannableString, MDHighlights.SUB, style);

            return spannableString;
        }

        private void parseUser(SpannableString stringToParse, MDHighlights user, MDStyle style) {
            parse(stringToParse, user.getPattern(), new ForegroundColorSpan(style.getTextColor()));
        }

        private void parseStrike(SpannableString stringToParse, MDHighlights strike, MDStyle style) {
            parse(stringToParse, strike.getPattern(), new ForegroundColorSpan(style.getTextColor()));
        }

        private void parseItalic(SpannableString stringToParse, MDHighlights italics, MDStyle style) {
            parse(stringToParse, italics.getPattern(), new ForegroundColorSpan(style.getTextColor()));
        }

        private void parseBold(SpannableString stringToParse, MDHighlights bold, MDStyle style) {
            parse(stringToParse, bold.getPattern(), new ForegroundColorSpan(style.getTextColor()));
        }

        private void parseLink(SpannableString stringToParse, MDHighlights link, MDStyle style) {
            parse(stringToParse, link.getPattern(), new ForegroundColorSpan(style.getTextColor()));
        }

        private void parseSub(SpannableString stringToParse, MDHighlights sub, MDStyle style) {
            parse(stringToParse, sub.getPattern(), new ForegroundColorSpan(style.getTextColor()));
        }

        private void parseHeader(SpannableString stringToParse, MDHighlights header, MDStyle style) {
            parse(stringToParse, header.getPattern(), new ForegroundColorSpan(style.getTextColor()));
         }

         /**
         * apply the span on all matches across the string
         * @param stringToParse
         * @param pattern
         * @param span
         */
        protected void parse(SpannableString stringToParse, final Pattern pattern, final Object span){
            for (Matcher m = pattern.matcher(stringToParse); m.find(); ) {
                stringToParse
                        .setSpan(span, m.start(), m.end(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }
    }

}
