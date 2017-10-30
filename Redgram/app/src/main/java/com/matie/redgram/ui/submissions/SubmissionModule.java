package com.matie.redgram.ui.submissions;

import com.matie.redgram.data.managers.presenters.SubmissionFeedPresenter;
import com.matie.redgram.data.managers.presenters.SubmissionFeedPresenterImpl;
import com.matie.redgram.ui.App;
import com.matie.redgram.ui.common.views.ContentView;

import dagger.Module;
import dagger.Provides;

/**
 * Created by matie on 2016-03-16.
 */
@Module
public class SubmissionModule {

    private SubmissionFeedView sub;
    private ContentView parentView;

    public SubmissionModule(SubmissionFeedView linksView, ContentView containerView) {
        this.sub = linksView;
        this.parentView = containerView;
    }

    @Provides
    public SubmissionFeedView provideSubmissionFeedView(){
        return sub;
    }

    @Provides
    public SubmissionFeedPresenter provideSubmissionFeedPresenter(App app){
        return new SubmissionFeedPresenterImpl(sub, parentView, app);
    }

}
