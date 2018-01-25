package com.matie.redgram.ui;

import android.content.Context;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class AppModule {

    private App app;

    public AppModule(App app){
        this.app = app;
    }

    @Singleton
    @Provides
    public AppComponentInjector provideInjector(AppComponent component) {
        return new AppComponentInjector(component);
    }

    @Singleton
    @Provides
    public Context provideContext(){
        return app.getApplicationContext();
    }

}
