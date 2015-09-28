package com.matie.redgram.data.network.connection;

import com.matie.redgram.ui.App;
import com.matie.redgram.ui.common.utils.ToastHandler;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by matie on 06/06/15.
 */
@Module
public class ConnectionModule {
    @Singleton
    @Provides
    public ConnectionStatus provideConnectionStatus(App app, ToastHandler handler){
        return new ConnectionStatus(app.getApplicationContext(), handler);
    }
}
