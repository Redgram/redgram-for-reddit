package com.matie.redgram.ui.subcription;

import com.matie.redgram.data.managers.presenters.SubscriptionPresenter;
import com.matie.redgram.data.managers.presenters.SubscriptionPresenterImpl;
import com.matie.redgram.ui.App;
import com.matie.redgram.ui.scopes.FragmentScope;
import com.matie.redgram.ui.subcription.views.SubscriptionView;

import dagger.Module;
import dagger.Provides;

/**
 * Created by matie on 2015-11-24.
 */
@Module
public class SubscriptionModule {
    private SubscriptionView subscriptionView;

    public SubscriptionModule(SubscriptionView subscriptionView) {
        this.subscriptionView = subscriptionView;
    }

    @FragmentScope
    @Provides
    public SubscriptionView provideView(){return subscriptionView;}

    @FragmentScope
    @Provides
    public SubscriptionPresenter provideSubscriptionPresenter(App app){
        return new SubscriptionPresenterImpl(subscriptionView, app);
    }
}
