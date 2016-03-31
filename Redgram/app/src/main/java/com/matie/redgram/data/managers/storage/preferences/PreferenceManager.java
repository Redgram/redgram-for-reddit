package com.matie.redgram.data.managers.storage.preferences;

import android.content.Context;
import android.content.SharedPreferences;

import javax.inject.Inject;

/**
 * Created by matie on 20/09/15.
 *
 * Singleton manager
 */
public class PreferenceManager implements Constants {


    private Context mContext;

    @Inject
    public PreferenceManager(Context context){
        mContext = context;
    }

    /**
     * Obtains a specified preference by passing a key.
     * @param key
     * @return The specified preference
     */
    public SharedPreferences getSharedPreferences(String key){
        return mContext.getSharedPreferences(key, mContext.MODE_PRIVATE);
    }

    /**
     *
     * @param prefs
     * @param key
     */
    public void remove(SharedPreferences prefs, String key){
        prefs.edit().remove(key).commit();
    }

    /**
     *
     * @param prefs
     * @return
     */
    public void clear(SharedPreferences prefs){
        prefs.edit().clear().apply();
    }


}
