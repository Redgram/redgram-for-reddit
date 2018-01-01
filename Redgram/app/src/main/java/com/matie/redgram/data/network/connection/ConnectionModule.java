package com.matie.redgram.data.network.connection;

import com.matie.redgram.ui.App;
import com.matie.redgram.ui.common.utils.widgets.ToastHandler;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class ConnectionModule {
    @Singleton
    @Provides
    public ConnectionManager provideConnectionStatus(App app, ToastHandler handler){
        return new ConnectionManager(app.getApplicationContext(), handler);
    }
}
