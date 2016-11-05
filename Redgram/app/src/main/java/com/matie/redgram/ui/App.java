package com.matie.redgram.ui;

import android.app.Application;
import android.content.Context;
import android.content.res.Resources;

import com.matie.redgram.data.managers.storage.db.DatabaseManager;
import com.matie.redgram.data.network.api.reddit.RedditClient;
import com.matie.redgram.data.network.api.reddit.RedditClientInterface;
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
    RedditClientInterface redditClient;

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

    public DatabaseManager getDatabaseManager() {
        return databaseManager;
    }

    public RedditClientInterface getRedditClient() {
        return redditClient;
    }

    public ToastHandler getToastHandler() {
        return toastHandler;
    }

}
