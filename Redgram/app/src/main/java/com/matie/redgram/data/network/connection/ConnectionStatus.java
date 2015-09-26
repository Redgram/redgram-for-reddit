package com.matie.redgram.data.network.connection;

import android.content.Context;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

import com.matie.redgram.R;
import com.matie.redgram.ui.common.utils.ToastHandler;

import java.io.IOException;

import javax.inject.Inject;

/**
 * Created by matie on 22/05/15.
 */
public class ConnectionStatus {

    private Context mContext;
    private Resources res;
    private ConnectivityManager cm;
    private NetworkInfo netInfo;
    private ToastHandler toastHandler;

    @Inject
    public ConnectionStatus(Context context, ToastHandler handler){
        mContext = context;
        res = mContext.getResources();
        toastHandler = handler;
    }

    public boolean isOnline(){
        return isNetworkActive() && isPingable();
    }

    public void showConnectionStatus(boolean isMain){
        String connectionMsg = res.getString(R.string.no_connection);
        if(!isOnline()){
            if(isMain){
                toastHandler.showToast(connectionMsg, Toast.LENGTH_SHORT);
            }else{
                connectionMsg = res.getString(R.string.no_connection_cache_loaded);
                toastHandler.showBackgroundToast(connectionMsg,Toast.LENGTH_SHORT);
            }
        }
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
