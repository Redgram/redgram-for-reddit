package com.matie.redgram.ui.common.utils.text;

import android.text.SpannableStringBuilder;

/**
 * Custom spannle builder that triggers a change listener on span addition
 *
 * Created by matie on 2017-02-27.
 */
public class ActiveSpannableStringBuilder extends SpannableStringBuilder {

    private ActiveSpanListener activeSpanListener;

    public ActiveSpannableStringBuilder(CharSequence text, ActiveSpanListener activeSpanListener) {
        super(text);
        this.activeSpanListener = activeSpanListener;
    }


    public SpannableStringBuilder setSpanAndReplaceText(Object what, int start, int end, CharSequence textToReplaceWith, int flags){
        setSpan(what, start, end, flags);
        SpannableStringBuilder replacedText =
                new ActiveSpannableStringBuilder(replace(start, end, textToReplaceWith), activeSpanListener);
        callSpanListener(what);
        return replacedText;
    }

    public void setSpan(Object what, int start, int end, int flags, boolean triggerListener) {
        setSpan(what, start, end, flags);
        if(triggerListener){
            callSpanListener(what);
        }
    }

    private void callSpanListener(Object what){
        if(activeSpanListener != null){
            activeSpanListener.onSpanAdded(what);
        }
    }

}
