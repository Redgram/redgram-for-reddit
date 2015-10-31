package com.matie.redgram.data.models.main.home;

import com.matie.redgram.data.models.main.reddit.RedditListing;

/**
 * This object's intention is to hold the initial sub models that are responsible to represent the
 * initial home view
 *
 * Created by matie on 2015-10-27.
 */
public class HomeViewWrapper {
    private RedditListing redditListing;
    private RedditListing subreddits;

    public RedditListing getRedditListing() {
        return redditListing;
    }

    public void setRedditListing(RedditListing redditListing) {
        this.redditListing = redditListing;
    }

    public RedditListing getSubreddits() {
        return subreddits;
    }

    public void setSubreddits(RedditListing subreddits) {
        this.subreddits = subreddits;
    }
}
