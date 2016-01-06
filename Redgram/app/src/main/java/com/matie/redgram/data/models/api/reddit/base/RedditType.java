package com.matie.redgram.data.models.api.reddit.base;

import com.matie.redgram.data.models.api.reddit.RedditComment;
import com.matie.redgram.data.models.api.reddit.RedditLink;
import com.matie.redgram.data.models.api.reddit.RedditListing;
import com.matie.redgram.data.models.api.reddit.RedditSubreddit;

/**
 * Created by matie on 16/04/15.
 */
public enum RedditType {

    Listing(RedditListing.class),
    t1(RedditComment.class),
    t3(RedditLink.class),
    t5(RedditSubreddit.class);


    private final Class mClass;

    RedditType(Class mClass){
        this.mClass = mClass;
    }

    public Class getDerivedClass(){
        return mClass;
    }
}
