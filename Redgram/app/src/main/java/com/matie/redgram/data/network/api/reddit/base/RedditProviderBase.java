package com.matie.redgram.data.network.api.reddit.base;

import com.matie.redgram.data.models.reddit.RedditListing;
import com.matie.redgram.data.models.reddit.base.RedditResponse;

import java.util.Map;

import retrofit.http.GET;
import retrofit.http.Path;
import retrofit.http.QueryMap;
import rx.Observable;

/**
 * Created by matie on 15/04/15.
 */
public interface RedditProviderBase {

    static final String SUBREDDIT = "/r/{subreddit}"; //only used in case of requesting specific subreddits
    static final String SEARCH = "/search";

    //Available filters: hot, new, rising, (top, controversial, can be mixed with TIME)
    static final String FILTER = "/{filter}";
    static final String JSON = ".json";


    /**
     * @see <a href="https://www.reddit.com/dev/api#section_listings">Listing Section</a>
     *
     * @param filter
     * @param params
     * @return a listing
     */
    @GET(FILTER+JSON)
    Observable<RedditResponse<RedditListing>> getListing(
            @Path("filter") String filter, @QueryMap Map<String, String> params);

    /**
     * @see <a href="https://www.reddit.com/dev/api#section_listings">Listing Section</a>
     *
     * @param subreddit
     * @param params
     * @return a listing
     */
    @GET(SUBREDDIT+JSON)
    Observable<RedditResponse<RedditListing>> getSubreddit(
            @Path("subreddit") String subreddit, @QueryMap Map<String, String> params);

    /**
     * Provides filters to subreddit
     * @see <a href="https://www.reddit.com/dev/api#section_listings">Listing Section</a>
     *
     * @param subreddit
     * @param filter
     * @param params
     * @return a listing
     */
    @GET(SUBREDDIT+FILTER+JSON)
    Observable<RedditResponse<RedditListing>> getSubreddit(
            @Path("subreddit") String subreddit, @Path("filter") String filter, @QueryMap Map<String, String> params);

    /**
     * As per the API docs:
     * @see <a href="https://www.reddit.com/dev/api#GET_search">Search links page</a>
     *
     * @param params
     * @return a listing
     */
    @GET(SEARCH+JSON)
    Observable<RedditResponse<RedditListing>> executeSearch(
            @QueryMap Map<String, String> params);

    /**
     * search is limited to a subreddit
     * @see <a href="https://www.reddit.com/dev/api#GET_search">Search links page</a>
     *
     * @param subreddit
     * @param params
     * @return a listing
     */
    @GET(SUBREDDIT+SEARCH+JSON)
    Observable<RedditResponse<RedditListing>> executeSearch(
            @Path("subreddit") String subreddit, @QueryMap Map<String, String> params);

}
