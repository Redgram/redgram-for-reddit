package com.matie.redgram.data.network.api.reddit.base;

import com.matie.redgram.data.models.api.reddit.RedditListing;
import com.matie.redgram.data.models.api.reddit.base.RedditResponse;

import java.util.List;
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
    static final String SUBREDDITS = "/subreddits"; //only used in case of requesting specific subreddits
    static final String SEARCH = "/search";
    static final String COMMENTS = "/comments";
    static final String ARTICLE = "/{article}";

    //Available filters: hot, new, rising, (top, controversial, can be mixed with TIME)
    static final String FILTER = "/{filter}";
    static final String JSON = ".json";


    /**
     * @see <a href="https://www.reddit.com/dev/api#GET_subreddits_{where}">Subreddits Listing Section</a>
     *
     * @param filter
     * @param params
     * @return a listing of subreddit listing
     */
    @GET(SUBREDDITS+FILTER+JSON)
    Observable<RedditResponse<RedditListing>> getSubredditsListing(
            @Path("filter") String filter, @QueryMap Map<String, String> params);

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

    /**
     * get comments by link id (article)
     * @param article The link ID
     * @param params
     * @return a array of 2 listings: the link and its comments
     */
    @GET(COMMENTS+ARTICLE+JSON)
    Observable<List<RedditResponse<RedditListing>>> getCommentsByArticle(
            @Path("article") String article, @QueryMap Map<String, String> params);
}
