package com.matie.redgram.ui;

import android.app.Application;

import com.matie.redgram.data.network.api.reddit.RedditClient;
import com.matie.redgram.data.network.api.reddit.RedditModule;
import com.matie.redgram.data.network.connection.ConnectionModule;
import com.matie.redgram.data.network.connection.ConnectionStatus;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by matie on 06/06/15.
 */
@Singleton
@Component(
        modules = {
                //add modules that require Application instance or used by the whole
                //application
                AppModule.class,
                ConnectionModule.class,
                RedditModule.class
        }
)
public interface AppComponent {
    void inject(App app);

    //add getters for the instances provided by the included
    //modules.
    App getApp();
    ConnectionStatus getConnectionStatus();
    RedditClient getRedditClient();
}
