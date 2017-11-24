package com.matie.redgram.ui.submission.links.views;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

import com.matie.redgram.ui.submission.SubmissionFeedView;
import com.matie.redgram.ui.submission.links.delegates.LinksViewDelegate;

public class LinksFeedView extends SubmissionFeedView {

    private LinksViewDelegate linksViewDelegate;

    public LinksFeedView(@NonNull Context context) {
        super(context);
    }

    public LinksFeedView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public LinksFeedView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public LinksViewDelegate getLinksViewDelegate() {
        return linksViewDelegate;
    }

    public void setLinksViewDelegate(LinksViewDelegate delegate) {
        this.linksViewDelegate = delegate;
    }

    @Override
    public void populateView() {
        setupLinksDelegate();
        fetchData();
    }

    private void fetchData() {
        linksViewDelegate.fetchLinks(getContext());
    }

    private void setupLinksDelegate() {
        linksViewDelegate.setContentView(containerRecyclerView);
        linksViewDelegate.setLoadingView(containerProgressBar);
    }

}
