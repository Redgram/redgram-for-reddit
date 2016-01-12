package com.matie.redgram.ui.comments.views.widgets.viewpager;

import android.content.Context;
import android.support.design.widget.TabLayout;
import android.util.AttributeSet;

/**
 * Created by matie on 2016-01-09.
 */
public class VerticalTabLayout extends TabLayout {
    public VerticalTabLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public VerticalTabLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public VerticalTabLayout(Context context) {
        super(context);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }


}
