package com.matie.redgram.ui;

import android.app.Application;
import android.content.Context;

import com.matie.redgram.data.network.connection.ConnectionStatus;

/**
 * Created by matie on 21/05/15.
 */
public class App extends Application {

    private AppComponent component;

    @Override
    public void onCreate() {
        super.onCreate();
        setupGraph();
    }

    private void setupGraph() {
        component = DaggerAppComponent.builder()
                .appModule(new AppModule(this))
                .build();
       // component.inject(this);
    }

    public AppComponent component() {
        return component;
    }

    /**
     * @param context - current activity/fragment context
     * @return Application context
     */
    public static App get(Context context){
        return (App) context.getApplicationContext();
    }



}
