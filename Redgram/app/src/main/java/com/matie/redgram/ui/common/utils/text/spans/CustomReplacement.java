package com.matie.redgram.ui.common.utils.text.spans;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.text.style.ReplacementSpan;

/**
 * Created by matie on 2015-12-14.
 */
public abstract class CustomReplacement extends ReplacementSpan {

    private int backgroundColor;
    private int textColor;

    public CustomReplacement(@NonNull int backgroundColor, @NonNull int textColor) {
        super();
        this.backgroundColor = backgroundColor;
        this.textColor = textColor;
    }

    protected abstract int getCustomSize(Paint paint, CharSequence text, int start, int end, Paint.FontMetricsInt fm);
    protected abstract void customDraw(Canvas canvas, CharSequence text, int start, int end, float x, int top, int y, int bottom, Paint paint);

    @Override
    public int getSize(Paint paint, CharSequence text, int start, int end, Paint.FontMetricsInt fm) {
        return getCustomSize(paint, text, start, end, fm);
    }

    @Override
    public void draw(Canvas canvas, CharSequence text, int start, int end, float x, int top, int y, int bottom, Paint paint) {
        customDraw(canvas, text, start, end, x, top, y, bottom, paint);
    }

    public float measureText(Paint paint, CharSequence text, int start, int end) {
        return paint.measureText(text, start, end);
    }

    public int getBackgroundColor() {
        return backgroundColor;
    }

    public int getTextColor() {
        return textColor;
    }
}
