package com.matie.redgram.data.models.main.home;

import com.matie.redgram.data.models.main.base.Listing;
import com.matie.redgram.data.models.main.items.PostItem;
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
    private Listing<PostItem> links;

    private Boolean isSubredditsCached;

    public Listing<SubredditItem> getSubreddits() {
        return subreddits;
    }

    public void setSubreddits(Listing<SubredditItem> subreddits) {
        this.subreddits = subreddits;
    }

    public Listing<PostItem> getLinks() {
        return links;
    }

    public void setLinks(Listing<PostItem> links) {
        this.links = links;
    }

    public Boolean getIsSubredditsCached() {
        return isSubredditsCached;
    }
    public void setIsSubredditsCached(Boolean isSubredditsCached) {
        this.isSubredditsCached = isSubredditsCached;
    }
}
