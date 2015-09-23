package com.matie.redgram.data.managers.preferences;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by matie on 20/09/15.
 *
 * Singleton manager
 */
public class PreferenceManager {

    public static final String HOME_SP = "home_pref_key";
    public static final String SEARCH_SP = "search_pref_key";

    private Context mContext;

    public PreferenceManager(Context context){
        mContext = context;
    }

    /**
     * Obtains a specified preference by passing a key.
     * @param key
     * @return The specified preference
     */
    public SharedPreferences getSharedPreferences(String context, String key){
        return mContext.getSharedPreferences(key, mContext.MODE_PRIVATE);
    }

    /**
     *
     * @param prefs
     * @param key
     * @param value
     */
    public void setString(SharedPreferences prefs, String key, String value){
        prefs.edit().putString(key, value).apply();
    }

    /**
     *
     * @param prefs
     * @param key
     * @param defaultValue
     * @return Returns the string paired with the key, or the defaultValue if nothing is returned.
     */
    public String getString(SharedPreferences prefs, String key, String defaultValue){
        return prefs.getString(key, defaultValue);
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
