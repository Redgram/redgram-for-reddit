package com.matie.redgram.data.models.api.reddit.main;

import com.matie.redgram.data.models.api.reddit.base.RedditObject;

import org.joda.time.DateTime;

/**
 * Created by matie on 2016-08-07.
 */
public class RedditUser extends RedditObject {
    private DateTime date;
    private String name;
    private String id;

    public DateTime getDate() {
        return date;
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }
}
