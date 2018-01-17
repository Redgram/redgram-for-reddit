package com.matie.redgram.data.network.api.reddit;

import android.content.Context;

import com.matie.redgram.data.managers.storage.db.DatabaseManager;
import com.matie.redgram.data.network.api.reddit.auth.RedditAuthClient;
import com.matie.redgram.data.network.api.reddit.auth.RedditAuthInterface;
import com.matie.redgram.data.network.api.reddit.user.RedditClient;
import com.matie.redgram.data.network.api.reddit.user.RedditClientInterface;
import com.matie.redgram.data.network.connection.ConnectionManager;
import com.matie.redgram.ui.common.utils.widgets.ToastHandler;

import java.lang.reflect.Proxy;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class RedditModule {

    @Singleton
    @Provides
    RedditAuthInterface provideRedditAuthClient(Context context,
                                                       DatabaseManager databaseManager,
                                                       ConnectionManager connectionManager) {
        return new RedditAuthClient(context, databaseManager, connectionManager);
    }

    @Singleton
    @Provides
    RedditClientInterface provideRedditClient(Context context,
                                                     ToastHandler toastHandler,
                                                     DatabaseManager databaseManager,
                                                     ConnectionManager connectionManager,
                                                     RedditAuthInterface redditAuthClient) {
        RedditClient redditClient =
                new RedditClient(context, databaseManager, connectionManager, redditAuthClient);

        RedditServiceInvocationHandler handler =
                new RedditServiceInvocationHandler(redditClient, databaseManager, toastHandler);

        return (RedditClientInterface) Proxy
                .newProxyInstance(RedditClientInterface.class.getClassLoader(),
                        new Class[]{RedditClientInterface.class}, handler);
    }

}
