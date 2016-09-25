package com.matie.redgram.ui;

import android.app.Application;
import android.content.Context;
import android.content.res.Resources;

import com.matie.redgram.data.managers.storage.db.DatabaseManager;
import com.matie.redgram.data.models.db.Prefs;
import com.matie.redgram.data.models.db.Settings;
import com.matie.redgram.data.models.db.User;
import com.matie.redgram.data.network.api.reddit.RedditClient;
import com.matie.redgram.data.network.connection.ConnectionManager;
import com.matie.redgram.ui.common.utils.widgets.ToastHandler;

import javax.inject.Inject;

/**
 * Created by matie on 21/05/15.
 */
public class App extends Application {

    private AppComponent component;

    @Inject
    ToastHandler toastHandler;

    @Inject
    ConnectionManager connectionManager;

    @Inject
    DatabaseManager databaseManager;

    @Inject
    RedditClient redditClient;

    Resources mResources;

    Prefs authUserPrefs;
    Settings appSettings;

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
        setupUserPrefs();
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

    public DatabaseManager getDatabaseManager() {
        return databaseManager;
    }

    public RedditClient getRedditClient() {
        return redditClient;
    }

    public ToastHandler getToastHandler() {
        return toastHandler;
    }

    public Settings getSettings() {
        return appSettings;
    }

    public void setSettings(Settings settings) {
        this.appSettings = settings;
    }

    public Prefs getAuthUserPrefs() {
        if(authUserPrefs != null){
            return authUserPrefs;
        }
        return new Prefs();
    }

    public void setupUserPrefs() {
        User sessionUser = databaseManager.getSessionUser();
        if(sessionUser != null){
            authUserPrefs = sessionUser.getPrefs();
        }
    }

    public void setUserPrefs(Prefs prefs) {
        authUserPrefs = prefs;
    }

}
