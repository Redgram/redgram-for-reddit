package com.matie.redgram.data.network.connection;

import android.app.Application;

import com.matie.redgram.ui.App;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by matie on 06/06/15.
 */
@Module
public class ConnectionModule {
    @Provides
    public ConnectionStatus provideConnectionStatus(App app){
        return new ConnectionStatus(app.getApplicationContext());
    }
}
