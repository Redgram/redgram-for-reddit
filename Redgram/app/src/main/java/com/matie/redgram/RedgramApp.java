package com.matie.redgram;

import android.app.Application;
import android.content.Context;

import com.matie.redgram.managers.ConnectionManager;

/**
 * Created by matie on 21/05/15.
 */
public class RedgramApp extends Application {

    private static Context mContext;
    private static ConnectionManager mConnectionManager;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();
        mConnectionManager = new ConnectionManager(mContext);
    }

    public static Context getContext() {
        return mContext;
    }

    public static boolean isOnline(){
        return mConnectionManager.isOnline();
    }


}
