package com.matie.redgram.ui.submission.links;

import com.matie.redgram.data.managers.presenters.SubmissionFeedPresenter;
import com.matie.redgram.ui.submission.SubmissionFeedView;
import com.matie.redgram.ui.submission.links.views.LinksView;

import dagger.Subcomponent;

@Subcomponent(modules = LinksModule.class)
public interface LinksComponent {
    void inject(SubmissionFeedView submissionFeedView);

    LinksView getLinksViewDelegate();
    SubmissionFeedPresenter getSubmissionFeedPresenter();
}
