package com.matie.redgram.ui;

import android.app.Application;

import com.matie.redgram.data.managers.storage.db.DatabaseManager;
import com.matie.redgram.data.network.api.reddit.auth.RedditAuthInterface;
import com.matie.redgram.data.network.api.reddit.user.RedditClientInterface;
import com.matie.redgram.data.network.connection.ConnectionManager;
import com.matie.redgram.ui.auth.AuthActivity;
import com.matie.redgram.ui.base.BaseActivity;
import com.matie.redgram.ui.common.utils.widgets.ToastHandler;

public class App extends Application {

    private AppComponent component;

    @Override
    public void onCreate() {
        super.onCreate();

        setupGraph();
        setupListeners();
    }

    private void setupListeners() {
        // TODO: 2018-01-09
    }

    @Override
    public void onTerminate() {
        component.injector().clearUserComponent();

        destroyListeners();

        super.onTerminate();
    }

    private void destroyListeners() {
        // TODO: 2018-01-09
    }

    private void setupGraph() {
        component = DaggerAppComponent.builder()
                .appModule(new AppModule(this))
                .build();

        component.inject(this);
    }

    public AppComponent component(BaseActivity activity) {
        if (activity == null || component == null) return null;

        return component.injector().getParentComponent(activity);
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

    // todo create generic launcher for each screen that can be invoked at an app level
    private void startAuthActivity() {
        startActivity(AuthActivity.intent(this, true));
    }
}
