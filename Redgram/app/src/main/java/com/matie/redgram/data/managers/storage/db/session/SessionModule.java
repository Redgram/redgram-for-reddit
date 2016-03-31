package com.matie.redgram.data.managers.storage.db.session;

import com.matie.redgram.ui.App;

import javax.inject.Singleton;
import dagger.Module;
import dagger.Provides;

/**
 * SESSION includes session information
 *
 * Created by matie on 2016-02-26.
 */
@Module
public class SessionModule {
    @Singleton
    @Provides
    public SessionManager provideSessionManager(App app){
        return new SessionManager(app);
    }
}
