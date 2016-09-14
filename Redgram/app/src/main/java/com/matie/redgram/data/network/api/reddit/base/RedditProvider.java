package com.matie.redgram.data.network.api.reddit.base;


import com.google.gson.JsonElement;
import com.matie.redgram.data.models.api.reddit.auth.AuthPrefs;
import com.matie.redgram.data.models.api.reddit.base.RedditObject;
import com.matie.redgram.data.models.api.reddit.main.RedditListing;
import com.matie.redgram.data.models.api.reddit.base.RedditResponse;
import com.matie.redgram.data.models.api.reddit.auth.AuthUser;

import java.util.List;
import java.util.Map;


import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;
import rx.Observable;

/**
 * Created by matie on 15/04/15.
 */
public interface RedditProvider {
    //oauth
    String API = "/api";
    String NAMESPACE = API+"/v1";

    //common
    String FILTER = "/{filter}";
    String JSON = ".json";

    //https://github.com/Redgram/redgram-for-reddit/issues/30
    //account
    String OAUTH_USER = NAMESPACE+"/me";
    String PREFS = OAUTH_USER +"/prefs";
    String FRIENDS = OAUTH_USER +"/friends"; //UserList
    String FRIEND = FRIENDS+"/{username}";
    String BLOCKED = OAUTH_USER +"/blocked"; //UserList
    String KARMA = OAUTH_USER +"/karma";
    String TROPHIES = OAUTH_USER +"/trophies";
    //users
    String USER = "/user/{username}"; //common prefix
    String USER_TROPHIES = NAMESPACE+USER+"/trophies";
    String USER_ABOUT = USER+"/about";
    String USER_COMMENTS = USER+"/comments";
    String USER_SUBMITTED = USER+"/submitted";
    String USER_UPVOTED = USER+"/upvoted";
    String USER_DOWNVOTED = USER+"/downvoted";
    String USER_OVERVIEW = USER+"/overview";
    String USER_SAVED = USER+"/saved";
    String USER_GILDED = USER+"/gilded";
    // end

    //subreddits
    String MINE = "/mine";
    String SUBSCRIBER = "/subscriber";
    String SUBREDDIT = "/r/{subreddit}"; //only used in case of requesting specific subreddits
    String SUBREDDITS = "/subreddits";
    String SEARCH = "/search";

    //links
    String VOTE = API+"/vote";
    String COMMENT = API+"/comment";
    String EDIT_TEXT = API+"/editusertext";
    String DELETE = API+"/del";
    String REPORT = API+"/report";
    String SAVE = API+"/save";
    String UN_SAVE = API+"/unsave";
    String HIDE = API+"/hide";
    String UN_HIDE = API+"/unhide";
    String MORE_CHILDREN = API+"/morechildren";
    String MARK_NSFW = API+"/marknsfw";
    String UNMARK_NSFW = API+"/unmarknsfw";

    //comments
    String COMMENTS = "/comments";
    String ARTICLE = "/{article}";
    String FRIENDS_COMMENTS = "/r/friends/comments";
    String FRIENDS_GILDED_COMMENTS = "/r/friends/gilded";

    /**
     * Get authenticated user information. The header is optional.
     *
     * @param accessToken Null in the case where token is automatically included in the header
     * @return
     */
    @GET(OAUTH_USER)
    Observable<AuthUser> getAuthUser(@Header("Authorization") String accessToken);

    /**
     * Get a list of friends
     *
     * @return List with two Listings of type UserList
     */
    @GET(FRIENDS)
    Observable<RedditObject> getFriends();

    /**
     * Get a specific user by username
     *
     * @param username A valid username
     * @return Returns an AuthUser
     */
    @GET(FRIEND)
    Observable<RedditResponse<RedditListing>> getFriend(@Path("username") String username);

    /**
     * Get a list of blocked users
     *
     * @return Listing of type UserList
     */
    @GET(BLOCKED)
    Observable<RedditObject> getBlockedUsers();

    /**
     * Karma details of the authenticated user
     *
     * @return
     */
    @GET(KARMA)
    Observable<AuthUser> getKarmaDetails();

    /**
     * Trophies of the authenticated user
     *
     * @return
     */
    @GET(TROPHIES)
    Observable<AuthUser> getTrophies();

    /**
     * Returns user preferences
     *
     * @param accessToken Pass null if accessToken is automatically included in header
     * @return
     */
    @GET(PREFS)
    Observable<AuthPrefs> getUserPrefs(@Header("Authorization") String accessToken);

    /**
     * Update user preferences
     *
     * @param prefs It will update any attributes set in {@link AuthPrefs}
     * @return
     */
    @PATCH(PREFS)
    Observable<AuthPrefs> updatePrefs(@Body AuthPrefs prefs);

    /**
     * Get user details like username, karma, date created, etc
     *
     * @param username
     * @return
     */
    @GET(USER_ABOUT)
    Observable<AuthUser> getUserDetails(@Path("username") String username);

    /**
     * Overview of links and comments submitted
     *
     * @param username
     * @return
     */
    @GET(USER_OVERVIEW)
    Observable<RedditResponse<RedditListing>> getUserOverview(@Path("username") String username);

    /**
     * User comments
     *
     * @param username
     * @return
     */
    @GET(USER_COMMENTS)
    Observable<RedditResponse<RedditListing>> getUserComments(@Path("username") String username);

    /**
     * User submitted links
     *
     * @param username
     * @return
     */
    @GET(USER_SUBMITTED)
    Observable<RedditResponse<RedditListing>> getUserSubmitted(@Path("username") String username);

    /**
     * User saved links and comments
     *
     * @param username
     * @return
     */
    @GET(USER_SAVED)
    Observable<RedditResponse<RedditListing>> getUserSaved(@Path("username") String username);

    /**
     * Get an upvoted links
     *
     * @param username
     * @return
     */
    @GET(USER_UPVOTED)
    Observable<RedditResponse<RedditListing>> getUserUpvoted(@Path("username") String username);

    /**
     * Get downvoted links
     *
     * @param username
     * @return
     */
    @GET(USER_DOWNVOTED)
    Observable<RedditResponse<RedditListing>> getUserDownvoted(@Path("username") String username);

    /**
     * NOT SURE
     *
     * @param username
     * @return
     */
    @GET(USER_GILDED)
    Observable<RedditResponse<RedditListing>> getUserGilded(@Path("username") String username);

    /**
     * NOT SURE
     *
     * @param username
     * @return
     */
    @GET(USER_TROPHIES)
    Observable<AuthUser> getUserTrophies(@Path("username") String username);

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

    @GET(FRIENDS_COMMENTS+JSON)
    Observable<List<RedditResponse<RedditListing>>> getFriendsComments(@QueryMap Map<String, String> params);


    @GET(FRIENDS_GILDED_COMMENTS+JSON)
    Observable<List<RedditResponse<RedditListing>>> getFriendsGildedComments(@QueryMap Map<String, String> params);

    /**
     * get comments by link id (article)
     * @param article The link ID
     * @param params
     * @return an array of 2 listings: the link and its comments
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
