package com.matie.redgram.data.models.api.reddit.base;

import com.matie.redgram.data.models.api.reddit.main.RedditComment;
import com.matie.redgram.data.models.api.reddit.main.RedditLink;
import com.matie.redgram.data.models.api.reddit.main.RedditListing;
import com.matie.redgram.data.models.api.reddit.main.RedditMore;
import com.matie.redgram.data.models.api.reddit.main.RedditSubreddit;
import com.matie.redgram.data.models.api.reddit.main.RedditUser;

/**
 * Created by matie on 16/04/15.
 */
public enum RedditType {

    Listing(RedditListing.class),
    UserList(RedditListing.class),
    User(RedditUser.class),
    more(RedditMore.class),
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
