package com.matie.redgram.network.api.reddit.base;

import com.matie.redgram.models.reddit.RedditListing;
import com.matie.redgram.models.reddit.base.RedditResponse;

import retrofit.http.GET;
import retrofit.http.Path;
import rx.Observable;

/**
 * Created by matie on 15/04/15.
 */
public interface RedditProviderBase {
    static final String SUBREDDIT = "/r/{subreddit}.json";

    @GET(SUBREDDIT)
    Observable<RedditResponse<RedditListing>> getSubreddit(
            @Path("subreddit") String subreddit);

}
