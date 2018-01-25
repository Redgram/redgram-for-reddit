package com.matie.redgram.ui.subcription;

import com.matie.redgram.data.managers.presenters.SubscriptionPresenter;
import com.matie.redgram.data.managers.presenters.SubscriptionPresenterImpl;
import com.matie.redgram.data.managers.storage.db.DatabaseManager;
import com.matie.redgram.data.network.api.reddit.user.RedditClientInterface;
import com.matie.redgram.ui.scopes.SubscriptionScope;
import com.matie.redgram.ui.subcription.views.SubscriptionView;

import dagger.Module;
import dagger.Provides;

@Module
public class SubscriptionModule {

    private SubscriptionView subscriptionView;

    public SubscriptionModule(SubscriptionView subscriptionView) {
        this.subscriptionView = subscriptionView;
    }

    @SubscriptionScope
    @Provides
    public SubscriptionView provideView() {
        return subscriptionView;
    }

    @SubscriptionScope
    @Provides
    public SubscriptionPresenter provideSubscriptionPresenter(DatabaseManager databaseManager,
                                                              RedditClientInterface redditClient) {
        return new SubscriptionPresenterImpl(subscriptionView, databaseManager, redditClient);
    }
}
