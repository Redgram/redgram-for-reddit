package com.matie.redgram.ui;

import android.app.Application;
import android.content.Context;

import com.matie.redgram.data.managers.connection.ConnectionManager;

/**
 * Created by matie on 21/05/15.
 */
public class App extends Application {

    private static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();

        //init singletons that require context
        ConnectionManager.getInstance().init(mContext);
    }

    public static Context getContext() {
        return mContext;
    }



}
