package com.matie.redgram.ui.submission;

import android.view.View;

import com.matie.redgram.ui.common.views.ContentView;

public interface SubmissionFeedView extends ContentView {
    void fetchFeed();
    void setContentView(View contentView);
    void setLoadingView(View loadingView);
}
