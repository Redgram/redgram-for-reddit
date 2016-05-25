package com.matie.redgram.data.models.main.reddit;

import java.util.List;

/**
 * This is class is used for final representation of a listing, and not for API return values.
 *
 * Created by matie on 13/09/15.
 */
public class RedditListing<T> {
    private List<T> items;
    private String modHash;
    private String after;
    private String before;

    public List<T> getItems() {
        return items;
    }

    public void setItems(List<T> items) {
        this.items = items;
    }

    public String getModHash() {
        return modHash;
    }

    public void setModHash(String modHash) {
        this.modHash = modHash;
    }

    public String getAfter() {
        return after;
    }

    public void setAfter(String after) {
        this.after = after;
    }

    public String getBefore() {
        return before;
    }

    public void setBefore(String before) {
        this.before = before;
    }
}
