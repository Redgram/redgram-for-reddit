package com.matie.redgram.ui.common.utils.widgets;

import android.content.Context;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

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
        impl = new ExpandableIndicatorAnim();
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

    public void setExpandedState(boolean isExpanded, int count) {
        impl.setExpandedState(isExpanded, count);
    }

    public boolean isExpanded(){
        return impl.isExpanded();
    }

    static abstract class Impl {
        public abstract void init(Context context, AttributeSet attrs, int defStyleAttr, ExpandableIndicator indicator);
        public abstract void setExpandedState(boolean isExpanded, int count);
        public abstract boolean isExpanded();
    }

    protected class ExpandableIndicatorAnim extends Impl{

        private TextView textView;
        private boolean isExpanded = true;

        @Override
        public void init(Context context, AttributeSet attrs, int defStyleAttr, ExpandableIndicator indicator) {
            View v = LayoutInflater.from(context).inflate(R.layout.expand_indicator_widget, indicator, true);
            textView = (TextView)v.findViewById(R.id.text_view);
        }

        @Override
        public void setExpandedState(boolean isExpanded, int count) {
            String num = (isExpanded) ? "" : count+"";
            textView.setText(num);

            this.isExpanded = isExpanded;
        }

        @Override
        public boolean isExpanded() {
            return isExpanded;
        }
    }

}
