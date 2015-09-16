package com.matie.redgram.data.models.api.reddit.base;

import com.google.gson.JsonElement;

/**
 * Created by matie on 16/04/15.
 */
public class RedditObjectWrapper {

    RedditType kind;
    JsonElement data;

    public RedditType getKind(){
        return kind;
    }

    public JsonElement getData(){
        return data;
    }

}
