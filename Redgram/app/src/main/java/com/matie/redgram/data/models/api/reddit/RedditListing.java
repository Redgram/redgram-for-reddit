package com.matie.redgram.data.models.api.reddit;

import com.matie.redgram.data.models.api.reddit.base.RedditObject;

import java.util.List;

/**
 * This is a generic representation of a Listing that is returned by the API.
 *
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
