package com.matie.redgram.data.models.api.reddit.base;

import com.matie.redgram.data.models.api.reddit.RedditLink;
import com.matie.redgram.data.models.api.reddit.RedditListing;

/**
 * Created by matie on 16/04/15.
 */
public enum RedditType {

    Listing(RedditListing.class),
    t3(RedditLink.class);

    private final Class mClass;

    RedditType(Class mClass){
        this.mClass = mClass;
    }

    public Class getDerivedClass(){
        return mClass;
    }
}
