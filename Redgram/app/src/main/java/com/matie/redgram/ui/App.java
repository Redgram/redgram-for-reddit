package com.matie.redgram.ui;

import android.app.Application;
import android.content.Context;
import android.content.res.Resources;
import android.widget.Toast;

import com.matie.redgram.R;
import com.matie.redgram.data.managers.preferences.PreferenceManager;
import com.matie.redgram.data.network.api.reddit.RedditClient;
import com.matie.redgram.data.network.connection.ConnectionStatus;
import com.matie.redgram.ui.common.utils.ToastHandler;

import javax.inject.Inject;

/**
 * Created by matie on 21/05/15.
 */
public class App extends Application {

    private AppComponent component;

    @Inject
    ToastHandler toastHandler;

    @Inject
    ConnectionStatus connectionStatus;
    String connectionMsg;

    @Inject
    PreferenceManager preferenceManager;

    @Inject
    RedditClient redditClient;

    Resources mResources;

    @Override
    public void onCreate() {
        super.onCreate();
        mResources = getResources();
        setupGraph();

        showConnectionStatus(connectionStatus.isOnline());
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

    public ConnectionStatus getConnectionStatus() {
        return connectionStatus;
    }

    public PreferenceManager getPreferenceManager() {
        return preferenceManager;
    }

    public RedditClient getRedditClient() {
        return redditClient;
    }

    public ToastHandler getToastHandler() {
        return toastHandler;
    }

    public void showConnectionStatus(boolean isConnected){
        if(!isConnected){
            connectionMsg = mResources.getString(R.string.no_connection);
            toastHandler.showToast(connectionMsg, Toast.LENGTH_SHORT);
        }
    }

}
