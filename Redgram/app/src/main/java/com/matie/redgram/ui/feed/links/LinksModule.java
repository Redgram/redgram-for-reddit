package com.matie.redgram.ui.feed.links;

import com.matie.redgram.data.managers.presenters.LinksPresenter;
import com.matie.redgram.data.managers.presenters.LinksPresenterImpl;
import com.matie.redgram.ui.App;
import com.matie.redgram.ui.base.BaseView;
import com.matie.redgram.ui.feed.links.views.LinksView;

import dagger.Module;
import dagger.Provides;

@Module
public class LinksModule {

    private final BaseView baseView;
    private final LinksView linksView;

    public LinksModule(BaseView baseView, LinksView linksView) {
        this.baseView = baseView;
        this.linksView = linksView;
    }

    @Provides
    public BaseView provideBaseView() {
        return baseView;
    }

    @Provides
    public LinksView provideLinksView() {
        return linksView;
    }

    @Provides
    public LinksPresenter provideLinksPresenter(App app) {
        return new LinksPresenterImpl(linksView, app);
    }
}
