package com.matie.redgram.ui;

import android.content.Context;

import com.matie.redgram.data.managers.storage.db.DatabaseManager;
import com.matie.redgram.data.managers.storage.db.DatabaseModule;
import com.matie.redgram.data.network.api.reddit.RedditModule;
import com.matie.redgram.data.network.api.reddit.auth.RedditAuthInterface;
import com.matie.redgram.data.network.api.reddit.user.RedditClientInterface;
import com.matie.redgram.data.network.connection.ConnectionManager;
import com.matie.redgram.data.network.connection.ConnectionModule;
import com.matie.redgram.data.utils.Logger;
import com.matie.redgram.ui.common.utils.widgets.ToastHandler;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(
        modules = {
                AppModule.class,
                ConnectionModule.class,
                DatabaseModule.class,
                RedditModule.class,
                UtilModule.class
        }
)
public interface AppComponent {
    void inject(App app);

    //add getters for the instances provided by the included
    //modules.
    Context getContext();

    ConnectionManager getConnectionManager();

    DatabaseManager getDatabaseManager();

    RedditClientInterface getRedditClient();
    RedditAuthInterface getRedditAuthClient();

    Logger getLogger();
    ToastHandler getToastHandler();
}
