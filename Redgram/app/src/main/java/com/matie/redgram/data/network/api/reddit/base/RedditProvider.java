package com.matie.redgram.data.network.api.reddit.base;


import com.google.gson.JsonElement;
import com.matie.redgram.data.models.api.reddit.auth.AuthPrefs;
import com.matie.redgram.data.models.api.reddit.base.RedditObject;
import com.matie.redgram.data.models.api.reddit.main.RedditListing;
import com.matie.redgram.data.models.api.reddit.base.RedditResponse;
import com.matie.redgram.data.models.api.reddit.auth.AuthUser;

import java.util.List;
import java.util.Map;


import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;
import rx.Observable;

/**
 * Created by matie on 15/04/15.
 */
public interface RedditProvider {

    //oauth
    String API = "api/";
    String NAMESPACE = API + "v1/";
    String USER = NAMESPACE+"me";
    String PREFS = USER+"/prefs";
    String MINE = "mine";
    String SUBSCRIBER = "/subscriber";

    String VOTE = API+"vote";
    String COMMENT = API+"comment";
    String EDIT_TEXT = API+"editusertext";
    String DELETE = API+"del";
    String REPORT = API+"report";
    String SAVE = API+"save";
    String UN_SAVE = API+"unsave";
    String HIDE = API+"hide";
    String UN_HIDE = API+"unhide";
    String MORE_CHILDREN = API+"morechildren";
    String MARK_NSFW = API+"marknsfw";
    String UNMARK_NSFW = API+"unmarknsfw";

    String SUBREDDIT = "r/{subreddit}/"; //only used in case of requesting specific subreddits
    String SUBREDDITS = "subreddits/"; //only used in case of requesting specific subreddits
    String SEARCH = "search";
    String COMMENTS = "comments/";
    String ARTICLE = "{article}";

    //common
    String FILTER = "{filter}";
    String JSON = ".json";

    /**
     * Get authenticated user information. The header is optional.
     *
     * @param accessToken Null in the case where token is automatically included in the header
     * @return
     */
    @GET(USER)
    Observable<AuthUser> getUser(@Header("Authorization") String accessToken);

    @GET(PREFS)
    Observable<AuthPrefs> getUserPrefs(@Header("Authorization") String accessToken);

    /**
     * @see <a href="https://www.reddit.com/dev/api#GET_subreddits_{where}">Subreddit Listing Section</a>
     *
     * @param filter
     * @param params
     * @return a listing of subreddit listing
     */
    @GET(SUBREDDITS+FILTER+JSON)
    Observable<RedditResponse<RedditListing>> getSubredditsListing(
            @Path("filter") String filter, @QueryMap Map<String, String> params);



    @GET(SUBREDDITS+MINE+SUBSCRIBER)
    Observable<RedditResponse<RedditListing>> getSubscriptions(@QueryMap Map<String, String> params);

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
    @GET(SUBREDDIT)
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

    /**
     * Vote a link
     * @param id the fullname of a link
     * @param direction Direction of vote, 1 for upvote, -1 for downvote and 0 to unvote
     * @return On success, it returns and empty object
     */
    @FormUrlEncoded
    @POST(VOTE)
    Observable<JsonElement> voteFor(@Field("id") String id, @Field("dir") Integer direction);

    @FormUrlEncoded
    @POST(HIDE)
    Observable<JsonElement> hide(@Field("id") String id);

    @FormUrlEncoded
    @POST(UN_HIDE)
    Observable<JsonElement> unhide(@Field("id") String id);

    @FormUrlEncoded
    @POST(SAVE)
    Observable<JsonElement> save(@Field("id") String id);

    @FormUrlEncoded
    @POST(UN_SAVE)
    Observable<JsonElement> unsave(@Field("id") String id);

    /**
     * Report also hides the thing for [link and comments].
     * Call to {@link RedditProvider#hide(String)} should be called on success.
     *
     * @param id
     * @return
     */
    @FormUrlEncoded
    @POST(REPORT)
    Observable<JsonElement> report(@Field("api_type") String json, @Field("thing_id") String id, @Field("reason") String reason);

    @FormUrlEncoded
    @POST(DELETE)
    Observable<JsonElement> delete(@Field("id") String id);
}
