package com.matie.redgram.ui.common.utils.text;

import android.content.Context;
import android.os.Build;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.widget.TextView;

import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;

/**
 * Created by matie on 2015-11-22.
 *
 * Responsible for all text decorations
 */
public class StringUtils {

    //cannot be instantiated
    private StringUtils(){}

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
            try{
                textView.setText(sb, TextView.BufferType.NORMAL);
            }catch (NullPointerException e){
                Log.d("NullPointer", SpannableBuilder.class.getName() + "#build - text view is not set");
            }
        }

        public void buildEditable() {
            try{
                textView.setText(sb, TextView.BufferType.EDITABLE);
            }catch (NullPointerException e){
                Log.d("NullPointer", SpannableBuilder.class.getName() + "#build - text view is not set");
            }
        }

        public void buildSpannable() {
            try{
                textView.setText(sb, TextView.BufferType.SPANNABLE);
            }catch (NullPointerException e){
                Log.d("NullPointer", SpannableBuilder.class.getName() + "#build - text view is not set");
            }
        }

    }
}
