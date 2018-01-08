package com.matie.redgram.ui;

import android.app.Application;
import android.content.Context;

import com.matie.redgram.data.managers.storage.db.DatabaseManager;
import com.matie.redgram.data.network.api.reddit.auth.RedditAuthInterface;
import com.matie.redgram.data.network.api.reddit.interceptors.RedditAuthenticator;
import com.matie.redgram.data.network.api.reddit.interceptors.RedditGeneralInterceptor;
import com.matie.redgram.data.network.api.reddit.user.RedditClientInterface;
import com.matie.redgram.data.network.connection.ConnectionManager;
import com.matie.redgram.ui.auth.AuthActivity;
import com.matie.redgram.ui.common.utils.widgets.ToastHandler;

public class App extends Application
        implements RedditGeneralInterceptor.InterceptorListener,
                    RedditAuthenticator.AuthenticatorListener {

    private AppComponent component;

    @Override
    public void onCreate() {
        super.onCreate();

        setupGraph();
        setupListeners();
    }

    private void setupListeners() {
        component.getRedditAuthenticator().addListener(this);
        component.getRedditInterceptor().addListener(this);
    }

    @Override
    public void onTerminate() {
        destroyListeners();

        super.onTerminate();
    }

    private void destroyListeners() {
        component.getRedditAuthenticator().removeListener(this);
        component.getRedditInterceptor().removeListener(this);
    }

    private void setupGraph() {
        component = DaggerAppComponent.builder()
                .appModule(new AppModule(this))
                .build();

        component.inject(this);
    }

    /**
     * @param context - current activity/fragment context
     * @return Application context
     */
    public static App get(Context context){
        return (App) context.getApplicationContext();
    }

    public AppComponent component() {
        return component;
    }

    public ConnectionManager getConnectionManager() {
        return component.getConnectionManager();
    }

    public DatabaseManager getDatabaseManager() {
        return component.getDatabaseManager();
    }

    public RedditClientInterface getRedditClient() {
        return component.getRedditClient();
    }

    public RedditAuthInterface getRedditAuthClient() {
        return component.getRedditAuthClient();
    }

    public ToastHandler getToastHandler() {
        return component.getToastHandler();
    }

    @Override
    public void onInterceptAuthRequest() {
        startAuthActivity();
    }

    @Override
    public void onAuthRequested() {
        startAuthActivity();
    }

    private void startAuthActivity() {
        startActivity(AuthActivity.intent(this, true));
    }
}
