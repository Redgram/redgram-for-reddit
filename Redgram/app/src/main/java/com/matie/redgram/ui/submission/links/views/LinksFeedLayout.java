package com.matie.redgram.ui.submission.links.views;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

import com.matie.redgram.ui.submission.SubmissionFeedLayout;
import com.matie.redgram.ui.submission.links.delegates.LinksFeedDelegate;

public class LinksFeedLayout extends SubmissionFeedLayout {

    private LinksView linksView;

    public LinksFeedLayout(@NonNull Context context) {
        super(context);
    }

    public LinksFeedLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public LinksFeedLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setLinksDelegate(LinksView delegate) {
        this.linksView = delegate;
        setupLinksDelegate();
    }

    @Override
    public void populateView() {
        fetchFeed();
    }

    private void fetchFeed() {
        linksView.fetchFeed();
    }

    private void setupLinksDelegate() {
        if (!(linksView instanceof LinksFeedDelegate)) return;

        ((LinksFeedDelegate) linksView).setContentView(containerRecyclerView);
        ((LinksFeedDelegate) linksView).setLoadingView(containerProgressBar);
    }

}
