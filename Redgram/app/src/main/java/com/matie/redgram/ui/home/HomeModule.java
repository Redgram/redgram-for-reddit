package com.matie.redgram.ui.home;

import com.matie.redgram.data.managers.presenters.HomePresenter;
import com.matie.redgram.data.managers.presenters.HomePresenterImpl;
import com.matie.redgram.data.network.api.reddit.RedditClient;
import com.matie.redgram.ui.home.views.HomeView;

import javax.inject.Inject;

import dagger.Module;
import dagger.Provides;

/**
 * Created by matie on 09/06/15.
 */
@Module
public class HomeModule {

    private HomeView homeView;

    public HomeModule(HomeView homeView) {
        this.homeView = homeView;
    }

    @Provides
    public HomeView provideView(){return homeView;}

    @Provides
    public HomePresenter provideHomePresenter(HomeView homeView, RedditClient redditClient){
        return new HomePresenterImpl(homeView,redditClient);
    }
}
