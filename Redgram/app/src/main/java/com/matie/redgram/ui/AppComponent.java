package com.matie.redgram.ui;

import com.matie.redgram.data.managers.storage.db.DatabaseManager;
import com.matie.redgram.data.managers.storage.db.DatabaseModule;
import com.matie.redgram.data.network.api.reddit.RedditClientInterface;
import com.matie.redgram.data.network.api.reddit.RedditModule;
import com.matie.redgram.data.network.connection.ConnectionManager;
import com.matie.redgram.data.network.connection.ConnectionModule;
import com.matie.redgram.ui.common.utils.widgets.ToastHandler;

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
                RedditModule.class,
                DatabaseModule.class
        }
)
public interface AppComponent {
    void inject(App app);

    //add getters for the instances provided by the included
    //modules.
    App getApp();
    ToastHandler getToastHandler();
    ConnectionManager getConnectionStatus();
    RedditClientInterface getRedditClient();
    DatabaseManager getSessionManager();
}
