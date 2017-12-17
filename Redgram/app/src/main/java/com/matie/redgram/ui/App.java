package com.matie.redgram.ui;

import android.app.Application;
import android.content.Context;

import com.matie.redgram.data.managers.storage.db.DatabaseManager;
import com.matie.redgram.data.network.api.reddit.RedditClientInterface;
import com.matie.redgram.data.network.connection.ConnectionManager;
import com.matie.redgram.ui.common.utils.widgets.ToastHandler;

/**
 * Created by matie on 21/05/15.
 */
public class App extends Application {

    private AppComponent component;

    @Override
    public void onCreate() {
        super.onCreate();
        setupGraph();
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

    public ToastHandler getToastHandler() {
        return component.getToastHandler();
    }

}
