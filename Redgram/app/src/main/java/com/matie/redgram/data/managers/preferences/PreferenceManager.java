package com.matie.redgram.data.managers.preferences;

import android.content.Context;
import android.content.SharedPreferences;

import javax.inject.Inject;

/**
 * Created by matie on 20/09/15.
 *
 * Singleton manager
 */
public class PreferenceManager {

    //preferences keys
    public static final String POSTS_PREF = "posts_pref_key";
    public static final String HOME_PREF = "home_pref_key";
    public static final String SEARCH_PREF = "search_pref_key";

    //object keys
    public static final String NSFW_KEY = "is_nsfw_enabled";

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
