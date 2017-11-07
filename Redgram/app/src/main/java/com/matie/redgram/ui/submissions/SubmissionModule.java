package com.matie.redgram.ui.submissions;

import com.matie.redgram.data.managers.presenters.SubmissionFeedPresenter;
import com.matie.redgram.data.managers.presenters.SubmissionFeedPresenterImpl;
import com.matie.redgram.ui.App;
import com.matie.redgram.ui.common.views.ContentView;
import com.matie.redgram.ui.submissions.links.LinksViewDelegate;
import com.matie.redgram.ui.submissions.views.LinksView;

import dagger.Module;
import dagger.Provides;

@Module
public class SubmissionModule {

    private SubmissionFeedView sub;
    private ContentView parentView;

    public SubmissionModule(SubmissionFeedView submissionFeedView, ContentView containerView) {
        this.sub = submissionFeedView;
        this.parentView = containerView;
    }

    @Provides
    public SubmissionFeedPresenter provideSubmissionFeedPresenter(App app) {
        return new SubmissionFeedPresenterImpl(sub, parentView, app);
    }

    @Provides
    public LinksView provideLinksViewDelegate(SubmissionFeedPresenter presenter) {
        return new LinksViewDelegate(presenter);
    }
}
