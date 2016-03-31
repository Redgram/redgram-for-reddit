package com.matie.redgram.ui;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;

import com.matie.redgram.data.managers.storage.db.session.SessionManager;
import com.matie.redgram.data.managers.storage.preferences.PreferenceManager;
import com.matie.redgram.data.network.api.reddit.RedditClient;
import com.matie.redgram.data.network.connection.ConnectionManager;
import com.matie.redgram.ui.common.auth.AuthActivity;
import com.matie.redgram.ui.common.utils.widgets.ToastHandler;

import javax.inject.Inject;

import io.realm.Realm;

/**
 * Created by matie on 21/05/15.
 */
public class App extends Application {

    private AppComponent component;

    @Inject
    ToastHandler toastHandler;

    @Inject
    ConnectionManager connectionManager;
    String connectionMsg;

    @Inject
    PreferenceManager preferenceManager;

    @Inject
    SessionManager sessionManager;

    @Inject
    RedditClient redditClient;

    Resources mResources;

    @Override
    public void onCreate() {
        super.onCreate();
        mResources = getResources();
        setupGraph();
    }

    private void setupGraph() {
        component = DaggerAppComponent.builder()
                .appModule(new AppModule(this))
                .build();
        component.inject(this);
    }

    public AppComponent component() {
        return component;
    }

    /**
     * @param context - current activity/fragment context
     * @return Application context
     */
    public static App get(Context context){
        return (App) context.getApplicationContext();
    }

    public ConnectionManager getConnectionManager() {
        return connectionManager;
    }

    public PreferenceManager getPreferenceManager() {
        return preferenceManager;
    }

    public SessionManager getSessionManager() {
        return sessionManager;
    }

    public RedditClient getRedditClient() {
        return redditClient;
    }

    public ToastHandler getToastHandler() {
        return toastHandler;
    }

    public void startAuthActivity(){
        Intent intent = new Intent(getApplicationContext(), AuthActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(intent);
    }

}
