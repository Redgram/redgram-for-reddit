package com.matie.redgram.data.models.main.home;

import com.matie.redgram.data.models.main.reddit.RedditListing;

import rx.Observable;

/**
 * This object's intention is to hold the initial sub models that are responsible to represent the
 * initial home view
 *
 * Created by matie on 2015-10-27.
 */
public class HomeViewWrapper {
    private RedditListing redditListingObservable;
    private RedditListing subredditsObservable;

    public RedditListing getRedditListingObservable() {
        return redditListingObservable;
    }

    public void setRedditListingObservable(RedditListing redditListingObservable) {
        this.redditListingObservable = redditListingObservable;
    }

    public RedditListing getSubredditsObservable() {
        return subredditsObservable;
    }

    public void setSubredditsObservable(RedditListing subredditsObservable) {
        this.subredditsObservable = subredditsObservable;
    }
}
