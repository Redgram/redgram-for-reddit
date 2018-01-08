package com.matie.redgram.data.managers.storage.db;

import android.content.Context;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class DatabaseModule {
    @Singleton
    @Provides
    public DatabaseManager provideSessionManager(Context context){
        return new DatabaseManager(context);
    }
}
