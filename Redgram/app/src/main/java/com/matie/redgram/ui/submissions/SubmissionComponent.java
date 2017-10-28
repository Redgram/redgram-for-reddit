package com.matie.redgram.ui.submissions;

import com.matie.redgram.data.managers.presenters.LinksPresenter;
import com.matie.redgram.ui.submissions.views.LinksView;

import dagger.Subcomponent;

/**
 * Created by matie on 2016-03-17.
 */
@Subcomponent(modules = SubmissionModule.class)
public interface SubmissionComponent {
    void inject(SubmissionFeedView submissionFeedView);

    LinksView getLinksView();
    LinksPresenter getLinksPresenter();
}
