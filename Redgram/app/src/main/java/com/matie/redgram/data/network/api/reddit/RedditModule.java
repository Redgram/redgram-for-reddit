package com.matie.redgram.data.network.api.reddit;

import android.content.Context;

import com.matie.redgram.data.managers.storage.db.DatabaseManager;
import com.matie.redgram.data.network.api.reddit.auth.RedditAuthClient;
import com.matie.redgram.data.network.api.reddit.auth.RedditAuthInterface;
import com.matie.redgram.data.network.api.reddit.interceptors.RedditAuthenticator;
import com.matie.redgram.data.network.api.reddit.interceptors.RedditGeneralInterceptor;
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
    public RedditAuthInterface provideRedditAuthClient(Context context,
                                                       DatabaseManager databaseManager,
                                                       RedditAuthenticator authenticator,
                                                       RedditGeneralInterceptor interceptor) {
        return new RedditAuthClient(context, databaseManager, authenticator, interceptor);
    }

    @Singleton
    @Provides
    public RedditClientInterface provideRedditClient(Context context,
                                                     ToastHandler toastHandler,
                                                     DatabaseManager databaseManager,
                                                     RedditAuthenticator authenticator,
                                                     RedditGeneralInterceptor interceptor) {
        RedditClient redditClient =
                new RedditClient(context, databaseManager, authenticator, interceptor);
        RedditServiceInvocationHandler handler =
                new RedditServiceInvocationHandler(redditClient, databaseManager, toastHandler);

        return (RedditClientInterface) Proxy
                .newProxyInstance(RedditClientInterface.class.getClassLoader(),
                        new Class[]{RedditClientInterface.class}, handler);
    }

    @Provides
    public RedditAuthenticator provideRedditAuthenticator(ToastHandler toastHandler,
                                                          DatabaseManager databaseManager) {
        return new RedditAuthenticator(toastHandler, databaseManager);
    }

    @Provides
    public RedditGeneralInterceptor provideRedditInterceptor(ConnectionManager connectionManager,
                                                             DatabaseManager databaseManager) {
        return new RedditGeneralInterceptor(connectionManager, databaseManager);
    }
}
