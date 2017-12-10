package com.matie.redgram.ui.submission;

import android.view.View;

import com.matie.redgram.ui.common.views.BaseView;

public abstract class SubmissionViewDelegate implements SubmissionView {

    protected final BaseView baseView;
    protected View contentView;
    protected View loadingView;

    public SubmissionViewDelegate(BaseView baseView) {
        this.baseView = baseView;
    }

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

    @Override
    public BaseView getBaseInstance() {
        return baseView;
    }
}
