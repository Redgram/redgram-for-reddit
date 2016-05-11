package com.matie.redgram.data.models.db;

import io.realm.RealmObject;

/**
 * Created by matie on 2016-05-08.
 */
public class Subreddit extends RealmObject {
    private String name;
    private boolean subscribed;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isSubscribed() {
        return subscribed;
    }

    public void setSubscribed(boolean subscribed) {
        this.subscribed = subscribed;
    }
}
