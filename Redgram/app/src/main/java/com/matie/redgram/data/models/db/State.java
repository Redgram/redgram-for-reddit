package com.matie.redgram.data.models.db;

import io.realm.RealmObject;

/**
 * This object depends on how the {@link Prefs} are set in order to take effect.
 *
 * For example, if a preference of loading the most recent thread on startup is switched on,
 * we obtain the thread id from this object
 */
public class State extends RealmObject {
    private String recentPost;

    public String getRecentPost() {
        return recentPost;
    }

    public void setRecentPost(String recentPost) {
        this.recentPost = recentPost;
    }
}
