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
    @Singleton
    public ConnectionStatus provideConnectionStatus(Application app){
        return new ConnectionStatus(app.getApplicationContext());
    }
}
