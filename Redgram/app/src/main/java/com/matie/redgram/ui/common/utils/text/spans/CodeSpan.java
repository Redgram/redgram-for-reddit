package com.matie.redgram.ui.common.utils.text.spans;

import android.text.TextPaint;
import android.text.style.CharacterStyle;
import android.text.style.MetricAffectingSpan;

/**
 * Created by matie on 2017-01-10.
 */
public class CodeSpan extends MetricAffectingSpan {

    CharacterStyle[] spans;

    public CodeSpan(CharacterStyle... spans) {
        this.spans = spans;
    }

    @Override
    public void updateMeasureState(TextPaint p) {
        for(CharacterStyle span : spans) {
            if(span instanceof MetricAffectingSpan){
               ((MetricAffectingSpan) span).updateMeasureState(p);
            }
        }
    }

    @Override
    public void updateDrawState(TextPaint tp) {
        for(CharacterStyle span : spans) {
            span.updateDrawState(tp);
        }
    }
}
