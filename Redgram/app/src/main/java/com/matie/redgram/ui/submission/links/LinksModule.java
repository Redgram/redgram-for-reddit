package com.matie.redgram.ui.submission.links;

import com.matie.redgram.data.managers.presenters.LinksPresenter;
import com.matie.redgram.data.managers.presenters.LinksPresenterImpl;
import com.matie.redgram.data.managers.presenters.SubmissionFeedPresenter;
import com.matie.redgram.ui.App;
import com.matie.redgram.ui.submission.SubmissionView;
import com.matie.redgram.ui.submission.links.delegates.LinksViewDelegate;
import com.matie.redgram.ui.submission.links.delegates.SingleLinkViewDelegate;
import com.matie.redgram.ui.submission.links.views.LinksView;

import dagger.Module;
import dagger.Provides;

@Module
public class LinksModule {
    @Provides
    public LinksView provideLinksViewDelegate(LinksPresenter linksPresenter){
        return new LinksViewDelegate(linksPresenter);
    }

    @Provides
    public SubmissionView provideSingleLinkViewDelegate(LinksPresenter linksPresenter){
        return new SingleLinkViewDelegate(linksPresenter);
    }

    @Provides
    public SubmissionFeedPresenter provideLinksPresenter(LinksView linksView, App app){
        return new LinksPresenterImpl(linksView, app);
    }
}
