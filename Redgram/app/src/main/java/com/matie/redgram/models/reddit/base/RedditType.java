package com.matie.redgram.models.reddit.base;

import com.matie.redgram.models.reddit.RedditLink;
import com.matie.redgram.models.reddit.RedditListing;

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
