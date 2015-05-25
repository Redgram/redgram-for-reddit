package com.matie.redgram.models.reddit;

import com.matie.redgram.models.reddit.base.RedditObject;

import java.util.List;

/**
 * Created by matie on 17/04/15.
 */
public class RedditListing extends RedditObject {
    String modhash;
    String after;
    String before;
    List<RedditObject> children;

    public String getModhash() {
        return modhash;
    }

    public String getAfter() {
        return after;
    }

    public String getBefore() {
        return before;
    }

    public List<RedditObject> getChildren() {
        return children;
    }
}
