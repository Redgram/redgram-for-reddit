package com.matie.redgram.data.network.api.reddit;

import android.app.Application;

import com.matie.redgram.data.network.api.reddit.base.RedditServiceBase;
import com.matie.redgram.data.network.connection.ConnectionStatus;
import com.matie.redgram.ui.App;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by matie on 06/06/15.
 */
@Module
public class RedditModule {
    @Provides
    public RedditClient provideRedditClient(App app){
        return new RedditClient(app);
    }

}
