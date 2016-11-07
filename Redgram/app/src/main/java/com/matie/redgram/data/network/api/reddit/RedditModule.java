package com.matie.redgram.data.network.api.reddit;

import android.widget.Toast;

import com.matie.redgram.data.managers.storage.db.DatabaseManager;
import com.matie.redgram.data.models.db.User;
import com.matie.redgram.data.network.api.utils.AccessLevel;
import com.matie.redgram.data.network.api.utils.Security;
import com.matie.redgram.ui.App;
import com.matie.redgram.ui.common.utils.widgets.ToastHandler;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by matie on 06/06/15.
 */
@Module
public class RedditModule {
    @Singleton
    @Provides
    public RedditClientInterface provideRedditClient(App app){
        RedditClient redditClient = new RedditClient(app);
        RedditClientInvocationHandler handler = new RedditClientInvocationHandler(redditClient,
                app.getDatabaseManager(), app.getToastHandler());
        return (RedditClientInterface) Proxy
                .newProxyInstance(RedditClientInterface.class.getClassLoader(),
                        new Class[]{RedditClientInterface.class}, handler);
    }

    class RedditClientInvocationHandler implements InvocationHandler{

        private final RedditClient redditClient;
        private final DatabaseManager databaseManager;
        private final RedditClientUserNullProxy nullProxy;
        private ToastHandler toastHandler;

        public RedditClientInvocationHandler(RedditClient redditClient, DatabaseManager databaseManager, ToastHandler toastHandler) {
            this.redditClient = redditClient;
            this.databaseManager = databaseManager;
            this.nullProxy = new RedditClientUserNullProxy();
            this.toastHandler = toastHandler;
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            if(databaseManager.getCurrentToken() != null
                    && User.USER_GUEST.equalsIgnoreCase(databaseManager.getCurrentToken().getHolderType())
                    && checkAnnotationAccessLevel(method.getAnnotation(Security.class), AccessLevel.USER)){
                if(toastHandler != null){
                    toastHandler.showBackgroundToast("Please log in to perform this action", Toast.LENGTH_SHORT);
                    //follow Null Object Pattern here - call on the null proxy
                    return method.invoke(nullProxy, args);
                }
            }
            return method.invoke(redditClient, args);
        }

        private boolean checkAnnotationAccessLevel(Security annotation, AccessLevel level) {
            return annotation != null && annotation.accessLevel() == level;
        }
    }
}
