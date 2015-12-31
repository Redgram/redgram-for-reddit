package com.matie.redgram.ui.common.utils.text.tags;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.NonNull;

import com.matie.redgram.ui.common.utils.text.CustomReplacement;

/**
 * Created by matie on 2015-12-17.
 */
public class AuthorTag extends CustomReplacement {

    private static int CORNER_RADIUS = 10;
    private static int PADDING = 30;

    public AuthorTag(@NonNull int backgroundColor, @NonNull int textColor) {
        super(backgroundColor, textColor);
    }

    @Override
    protected int getCustomSize(Paint paint, CharSequence text, int start, int end, Paint.FontMetricsInt fm) {
        return PADDING + Math.round(measureText(paint, text, start, end)) + PADDING;
    }

    @Override
    protected void customDraw(Canvas canvas, CharSequence text, int start, int end, float x, int top, int y, int bottom, Paint paint) {
        RectF rect = new RectF(x, top, (x + measureText(paint, text, start, end) + PADDING), bottom+PADDING);
        paint.setColor(getBackgroundColor());
        canvas.drawRoundRect(rect, CORNER_RADIUS, CORNER_RADIUS, paint);
        paint.setColor(getTextColor());
        canvas.drawText(text, start, end, x+(PADDING/2), y+(PADDING/2), paint);
    }
}
