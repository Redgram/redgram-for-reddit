package com.matie.redgram.ui.common.utils.text.markdown;

import android.text.SpannableStringBuilder;

/**
 * Custom spannable builder that triggers a change listener on span addition
 *
 * Created by matie on 2017-02-27.
 */
public class MDSpannableStringBuilder extends SpannableStringBuilder {

    private MDSpanListener mdSpanListener;

    public MDSpannableStringBuilder(CharSequence text, MDSpanListener mdSpanListener) {
        super(text);
        this.mdSpanListener = mdSpanListener;
    }

    public void setSpan(Object what, int start, int end, int flags, boolean triggerListener) {
        setSpan(what, start, end, flags);
        if(triggerListener){
            callSpanListener(what);
        }
    }

    public MDSpannableStringBuilder setSpanAndReplaceText(Object what, int start, int end, String textToReplaceWith, int flags, boolean triggerListener) {
        setSpan(what, start, end, flags, triggerListener);
        return replace(start, end, textToReplaceWith);
    }

    @Override
    public MDSpannableStringBuilder replace(int start, int end, CharSequence tb) {
        SpannableStringBuilder spannableStringBuilder = super.replace(start, end, tb);
        return new MDSpannableStringBuilder(spannableStringBuilder, mdSpanListener);
    }

    @Override
    public CharSequence subSequence(int start, int end) {
        CharSequence charSequence = super.subSequence(start, end);
        return new MDSpannableStringBuilder(charSequence, mdSpanListener);
    }

    private void callSpanListener(Object what){
        if(mdSpanListener != null){
            mdSpanListener.onSpanAdded(what);
        }
    }
}
