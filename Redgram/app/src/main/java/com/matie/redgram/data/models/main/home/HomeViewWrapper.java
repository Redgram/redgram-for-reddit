package com.matie.redgram.data.models.main.home;

import com.matie.redgram.data.models.main.base.Listing;
import com.matie.redgram.data.models.main.items.SubredditItem;

/**
 * This object's intention is to hold the initial sub models that are responsible to represent the
 * initial home view
 *
 * Created by matie on 2015-10-27.
 */
public class HomeViewWrapper {
    // TODO: 2016-06-04 also get trending if enabled in settings
    private Listing<SubredditItem> subreddits;
    private Boolean isSubredditsCached;

    public Listing<SubredditItem> getSubreddits() {
        return subreddits;
    }

    public void setSubreddits(Listing<SubredditItem> subreddits) {
        this.subreddits = subreddits;
    }

    public Boolean getIsSubredditsCached() {
        return isSubredditsCached;
    }

    public void setIsSubredditsCached(Boolean isSubredditsCached) {
        this.isSubredditsCached = isSubredditsCached;
    }
}
