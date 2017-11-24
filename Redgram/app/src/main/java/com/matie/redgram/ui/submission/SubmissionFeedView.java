package com.matie.redgram.ui.submission;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.matie.redgram.R;
import com.matie.redgram.ui.common.views.widgets.postlist.PostRecyclerView;

import butterknife.ButterKnife;
import butterknife.InjectView;

public abstract class SubmissionFeedView extends FrameLayout {

    @InjectView(R.id.container_linear_layout)
    protected LinearLayout containerLinearLayout;
    @InjectView(R.id.container_recycler_view)
    protected PostRecyclerView containerRecyclerView;
    @InjectView(R.id.container_load_more_bar)
    protected ProgressBar containerProgressBar;

    public SubmissionFeedView(@NonNull Context context) {
        super(context);
    }

    public SubmissionFeedView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public SubmissionFeedView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void onFinishInflate() {
        super.onFinishInflate();
        ButterKnife.inject(this);
    }

    public abstract void populateView();
}
