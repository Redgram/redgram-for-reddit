package com.matie.redgram.ui.submissions;

import com.matie.redgram.data.managers.presenters.SubmissionFeedPresenter;
import com.matie.redgram.ui.submissions.views.LinksView;

import dagger.Subcomponent;

@Subcomponent(modules = SubmissionModule.class)
public interface SubmissionComponent {
    void inject(SubmissionFeedView submissionFeedView);

    LinksView getLinksViewDelegate();
    SubmissionFeedPresenter getSubmissionFeedPresenter();
}
