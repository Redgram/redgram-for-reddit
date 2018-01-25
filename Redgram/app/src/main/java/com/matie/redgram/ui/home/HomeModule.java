package com.matie.redgram.ui.home;

import com.matie.redgram.data.managers.presenters.HomePresenter;
import com.matie.redgram.data.managers.presenters.HomePresenterImpl;
import com.matie.redgram.data.managers.storage.db.DatabaseManager;
import com.matie.redgram.data.network.api.reddit.user.RedditClientInterface;
import com.matie.redgram.ui.home.views.HomeView;
import com.matie.redgram.ui.scopes.MainScope;

import dagger.Module;
import dagger.Provides;

@Module
public class HomeModule {

    private HomeView homeView;

    public HomeModule(HomeView homeView) {
        this.homeView = homeView;
    }

    @MainScope
    @Provides
    public HomeView provideView(){return homeView;}

    @MainScope
    @Provides
    public HomePresenter provideHomePresenter(DatabaseManager databaseManager, RedditClientInterface redditClient){
        return new HomePresenterImpl(homeView, databaseManager, redditClient);
    }

}
