package com.matie.redgram.ui.submission;

import android.view.View;

public abstract class SubmissionViewDelegate implements SubmissionView {
    protected View contentView;
    protected View loadingView;

    public void setContentView(View contentView) {
        this.contentView = contentView;
    }

    public void setLoadingView(View loadingView) {
        this.loadingView = loadingView;
    }

    @Override
    public void showLoading() {
        loadingView.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoading() {
        loadingView.setVisibility(View.GONE);
    }

    @Override
    public void showInfoMessage() {}

    @Override
    public void showErrorMessage(String error) {}
}
