package com.matie.redgram.ui.submission;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.matie.redgram.R;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class SubmissionControlView extends RelativeLayout {

    @InjectView(R.id.control_title_linear_layout)
    LinearLayout itemPicker;

    @InjectView(R.id.listing_filter)
    ImageView filterView;

    @InjectView(R.id.listing_refresh)
    ImageView refreshView;

    @InjectView(R.id.toolbar_title)
    TextView titleView;

    @InjectView(R.id.toolbar_subtitle)
    TextView subtitleView;

    public SubmissionControlView(Context context) {
        super(context);
    }

    public SubmissionControlView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SubmissionControlView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public SubmissionControlView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public void onFinishInflate(){
        super.onFinishInflate();
        ButterKnife.inject(this);
    }

    public void setTitle(String title) {
        setText(titleView, title);
    }

    public TextView getTitleView() {
        return titleView;
    }

    public void setSubTitle(String subText) {
        setText(subtitleView, subText);
    }

    public TextView getSubtitleView() {
        return subtitleView;
    }

    private void setText(TextView view, String text) {
        if (view == null || text == null || text.isEmpty()) return;
        view.setText(text);
    }

    public void setRefreshListener(OnClickListener onClickListener) {
        if (onClickListener == null) return;
        refreshView.setOnClickListener(onClickListener);
    }

    public void setFilterListener(OnClickListener onClickListener) {
        if (onClickListener == null) return;
        filterView.setOnClickListener(onClickListener);
    }

    public void setItemPickerListener(OnClickListener onClickListener) {
        if (onClickListener == null) return;
        itemPicker.setOnClickListener(onClickListener);
    }

}
