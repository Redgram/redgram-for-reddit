package com.matie.redgram.data.models.main.items;

import com.matie.redgram.data.models.main.reddit.RedditObject;

/**
 * Created by matie on 2015-10-25.
 */
public class SubredditItem extends RedditObject{
    String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
