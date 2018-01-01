package com.matie.redgram.data.managers.storage.db;

import com.matie.redgram.ui.App;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class DatabaseModule {
    @Singleton
    @Provides
    public DatabaseManager provideSessionManager(App app){
        return new DatabaseManager(app);
    }
}
