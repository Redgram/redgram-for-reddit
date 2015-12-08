package com.matie.redgram.ui;

import com.matie.redgram.ui.common.utils.widgets.ToastHandler;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by matie on 06/06/15.
 */
@Module
public class AppModule {

    private App app;

    public AppModule(App app){
        this.app = app;
    }

    @Singleton
    @Provides
    public App provideApplication(){
        return app;
    }

    @Singleton
    @Provides
    public ToastHandler provideToastHandler(){
        return new ToastHandler(app.getApplicationContext());
    }

}
