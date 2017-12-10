package com.matie.redgram.ui.submission.links;

import com.matie.redgram.data.managers.presenters.LinksPresenter;
import com.matie.redgram.data.managers.presenters.LinksPresenterImpl;
import com.matie.redgram.ui.App;
import com.matie.redgram.ui.common.views.BaseView;
import com.matie.redgram.ui.submission.links.delegates.LinksFeedDelegate;
import com.matie.redgram.ui.submission.links.views.LinksView;

import dagger.Module;
import dagger.Provides;

@Module
public class LinksModule {

    private final BaseView baseView;

    public LinksModule(BaseView baseView) {
        this.baseView = baseView;
    }

    @Provides
    public BaseView provideBaseView() {
        return baseView;
    }

    @Provides
    public LinksView provideLinksView() {
        return new LinksFeedDelegate(baseView);
    }

    @Provides
    public LinksPresenter provideLinksPresenter(LinksView linksView, App app) {
        return new LinksPresenterImpl(linksView, app);
    }
}
