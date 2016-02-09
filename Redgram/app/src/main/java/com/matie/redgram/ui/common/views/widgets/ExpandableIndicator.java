package com.matie.redgram.ui.common.views.widgets;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Animatable;
import android.os.Build;
import android.os.Parcelable;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.matie.redgram.R;

/**
 * Created by matie on 2016-02-01.
 */
public class ExpandableIndicator extends FrameLayout {

    private Impl impl;

    public ExpandableIndicator(Context context) {
        super(context);
        init(context, null, 0);
    }

    public ExpandableIndicator(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0);
    }

    public ExpandableIndicator(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            // NOTE: VectorDrawable only supports API level 21 or later
            impl = new ExpandableIndicatorAnim();
        } else {
            impl = new ExpandableIndicatorNoAnim();
        }
        impl.init(context, attrs, defStyleAttr, this);
    }

    @Override
    protected void dispatchSaveInstanceState(SparseArray<Parcelable> container) {
        super.dispatchFreezeSelfOnly(container);
    }

    @Override
    protected void dispatchRestoreInstanceState(SparseArray<Parcelable> container) {
        super.dispatchThawSelfOnly(container);
    }

    public void setExpandedState(boolean isExpanded, boolean animate) {
        impl.setExpandedState(isExpanded, animate);
    }

    static abstract class Impl {

        public abstract void init(Context context, AttributeSet attrs, int defStyleAttr, ExpandableIndicator indicator);
        public abstract void setExpandedState(boolean isExpanded, boolean animate);

    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    protected class ExpandableIndicatorAnim extends Impl{

        private ImageView imageView;

        @Override
        public void init(Context context, AttributeSet attrs, int defStyleAttr, ExpandableIndicator indicator) {
            View v = LayoutInflater.from(context).inflate(R.layout.expand_indicator_widget, indicator, true);
            imageView = (ImageView)v.findViewById(R.id.image_view);
        }

        @Override
        public void setExpandedState(boolean isExpanded, boolean animate) {
            int resId = (isExpanded) ? R.drawable.ic_action_arrow_drop_up : R.drawable.ic_action_arrow_drop_down;
            imageView.setImageResource(resId);
            //add animation
        }
    }

    protected class ExpandableIndicatorNoAnim extends Impl{

        private ImageView imageView;

        @Override
        public void init(Context context, AttributeSet attrs, int defStyleAttr, ExpandableIndicator indicator) {
            View v = LayoutInflater.from(context).inflate(R.layout.expand_indicator_widget, indicator, true);
            imageView = (ImageView)v.findViewById(R.id.image_view);
        }

        @Override
        public void setExpandedState(boolean isExpanded, boolean animate) {
            int resId = (isExpanded) ? R.drawable.ic_action_arrow_drop_up : R.drawable.ic_action_arrow_drop_down;
            imageView.setImageResource(resId);
        }
    }
}
