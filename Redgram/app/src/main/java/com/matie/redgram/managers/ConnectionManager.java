package com.matie.redgram.managers;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.io.IOException;

/**
 * Created by matie on 22/05/15.
 */
public class ConnectionManager {

    private final Context mContext;

    private static ConnectivityManager cm;
    private static NetworkInfo netInfo;

    public ConnectionManager(Context mContext) {
        this.mContext = mContext;
    }

    //only accessible method.
    public boolean isOnline(){
        return isNetworkActive() && isPingable();
    }

    private boolean isNetworkActive() {
        cm = (ConnectivityManager)mContext.getSystemService(mContext.CONNECTIVITY_SERVICE);
        netInfo = cm.getActiveNetworkInfo();
        return (netInfo != null && netInfo.isConnected());
    }

    private boolean isPingable() {
        Runtime runtime = Runtime.getRuntime();
        try {

            Process ipProcess = runtime.exec("/system/bin/ping -c 1 8.8.8.8");
            int     exitValue = ipProcess.waitFor();
            return (exitValue == 0);

        } catch (IOException e)          { e.printStackTrace(); }
        catch (InterruptedException e) { e.printStackTrace(); }

        return false;
    }
}
