package com.matie.redgram.ui.feed;

import com.matie.redgram.ui.base.BaseView;
import com.matie.redgram.ui.common.views.ContentView;

public abstract class SubmissionFeedDelegate implements ContentView {

    protected final BaseView baseView;

    public SubmissionFeedDelegate(BaseView baseView) {
        this.baseView = baseView;
    }

    @Override
    public BaseView getBaseInstance() {
        return baseView;
    }
}
