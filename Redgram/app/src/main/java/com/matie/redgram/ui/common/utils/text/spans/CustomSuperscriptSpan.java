package com.matie.redgram.ui.common.utils.text.spans;

import android.text.TextPaint;
import android.text.style.SuperscriptSpan;

/**
 * Created by matie on 2017-02-11.
 */
public class CustomSuperscriptSpan extends SuperscriptSpan{

    private final int ascentMultiplier;
    private final int nestedLevel;

    public CustomSuperscriptSpan(int ascentMultiplier, int nestedLevel) {
        this.ascentMultiplier = ascentMultiplier;
        this.nestedLevel = nestedLevel;
    }

    @Override
    public void updateDrawState(TextPaint tp) {
        if(ascentMultiplier >= 1){
            //the bigger the multiplier, the smaller is the super shift
            tp.baselineShift += (int) (tp.ascent() / 2) * ascentMultiplier;
        }
    }

    @Override
    public void updateMeasureState(TextPaint tp) {
        //do nothing
    }

    public int getNestedLevel() {
        return nestedLevel;
    }
}
