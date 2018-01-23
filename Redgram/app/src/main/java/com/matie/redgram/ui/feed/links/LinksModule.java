package com.matie.redgram.ui.feed.links;

import com.matie.redgram.data.managers.presenters.LinksPresenter;
import com.matie.redgram.data.managers.presenters.LinksPresenterImpl;
import com.matie.redgram.data.managers.storage.db.DatabaseManager;
import com.matie.redgram.data.network.api.reddit.user.RedditClientInterface;
import com.matie.redgram.ui.base.BaseView;
import com.matie.redgram.ui.feed.links.views.LinksView;
import com.matie.redgram.ui.scopes.FeedScope;

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

    @Provides @FeedScope
    public BaseView provideBaseView() {
        return baseView;
    }

    @Provides @FeedScope
    public LinksView provideLinksView() {
        return linksView;
    }

    @Provides @FeedScope
    public LinksPresenter provideLinksPresenter(DatabaseManager databaseManager,
                                                RedditClientInterface redditClient) {
        return new LinksPresenterImpl(linksView, databaseManager, redditClient);
    }
}
