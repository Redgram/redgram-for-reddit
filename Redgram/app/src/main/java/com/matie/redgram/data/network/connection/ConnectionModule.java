package com.matie.redgram.data.network.connection;

import android.content.Context;

import com.matie.redgram.ui.common.utils.widgets.ToastHandler;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class ConnectionModule {
    @Singleton
    @Provides
    public ConnectionManager provideConnectionStatus(Context context, ToastHandler handler){
        return new ConnectionManager(context, handler);
    }
}
