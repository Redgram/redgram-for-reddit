package com.matie.redgram.ui.submission.links.views;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

import com.matie.redgram.ui.submission.SubmissionFeedView;
import com.matie.redgram.ui.submission.links.delegates.LinksFeedDelegate;

public class LinksFeedView extends SubmissionFeedView {

    private LinksFeedDelegate linksFeedDelegate;

    public LinksFeedView(@NonNull Context context) {
        super(context);
    }

    public LinksFeedView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public LinksFeedView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public LinksFeedDelegate getLinksFeedDelegate() {
        return linksFeedDelegate;
    }

    public void setLinksFeedDelegate(LinksFeedDelegate delegate) {
        this.linksFeedDelegate = delegate;
    }

    @Override
    public void populateView() {
        setupLinksDelegate();
        fetchData();
    }

    private void fetchData() {
        linksFeedDelegate.fetchLinks(getContext());
    }

    private void setupLinksDelegate() {
        linksFeedDelegate.setContentView(containerRecyclerView);
        linksFeedDelegate.setLoadingView(containerProgressBar);
    }

}
