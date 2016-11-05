package com.matie.redgram.data.network.api.reddit;

import android.support.annotation.Nullable;

import com.google.gson.JsonElement;
import com.matie.redgram.data.models.api.reddit.auth.AuthPrefs;
import com.matie.redgram.data.models.api.reddit.auth.AuthUser;
import com.matie.redgram.data.models.api.reddit.auth.AuthWrapper;
import com.matie.redgram.data.models.main.items.PostItem;
import com.matie.redgram.data.models.main.items.SubredditItem;
import com.matie.redgram.data.models.main.items.UserItem;
import com.matie.redgram.data.models.main.items.comment.CommentsWrapper;
import com.matie.redgram.data.models.main.reddit.RedditListing;
import com.matie.redgram.data.network.api.reddit.base.RedditServiceInterface;
import com.matie.redgram.data.network.api.util.AccessLevel;
import com.matie.redgram.data.network.api.util.Security;

import java.util.List;
import java.util.Map;

import rx.Observable;

/**
 * Created by matie on 2016-10-29.
 */
public interface RedditClientInterface extends RedditServiceInterface {

    //auth
    @Security(accessLevel = AccessLevel.ANY)
    Observable<AuthWrapper> getAuthWrapper(String code);

    @Security(accessLevel = AccessLevel.ANY)
    Observable<AuthWrapper> getAuthWrapper();

    //public + user
    @Security(accessLevel = AccessLevel.ANY)
    Observable<RedditListing<com.matie.redgram.data.models.main.reddit.RedditObject>> getUserOverview(String username);

    @Security(accessLevel = AccessLevel.ANY)
    Observable<RedditListing<PostItem>> getSubredditListing(String query, @Nullable Map<String, String> params, List<PostItem> postItems);

    @Security(accessLevel = AccessLevel.ANY)
    Observable<RedditListing<PostItem>> getSubredditListing(String query, @Nullable String filter, @Nullable Map<String, String> params, List<PostItem> postItems);

    @Security(accessLevel = AccessLevel.ANY)
    Observable<RedditListing<PostItem>> executeSearch(String subreddit, @Nullable Map<String, String> params, List<PostItem> postItems);

    @Security(accessLevel = AccessLevel.ANY)
    Observable<RedditListing<PostItem>> getListing(String front, @Nullable Map<String, String> params, List<PostItem> postItems);

    @Security(accessLevel = AccessLevel.ANY)
    Observable<RedditListing<SubredditItem>> getSubreddits(String filter, @Nullable Map<String, String> params);

    @Security(accessLevel = AccessLevel.ANY)
    Observable<CommentsWrapper> getCommentsByArticle(String article, @Nullable Map<String, String> params);

    //require users
    @Security(accessLevel = AccessLevel.USER)
    Observable<RedditListing<SubredditItem>> getSubscriptions(@Nullable Map<String, String> params);

    @Security(accessLevel = AccessLevel.USER)
    Observable<AuthUser> getUser(@Nullable String accessToken);

    @Security(accessLevel = AccessLevel.USER)
    Observable<AuthPrefs> getUserPrefs(@Nullable String accessToken);

    @Security(accessLevel = AccessLevel.USER)
    Observable<JsonElement> delete(String id);

    @Security(accessLevel = AccessLevel.USER)
    Observable<JsonElement> voteFor(String id, Integer direction);

    @Security(accessLevel = AccessLevel.USER)
    Observable<JsonElement> hide(String id, boolean hide);

    @Security(accessLevel = AccessLevel.USER)
    Observable<JsonElement> save(String id, boolean save);

    @Security(accessLevel = AccessLevel.USER)
    Observable<JsonElement> report(String id);

    @Security(accessLevel = AccessLevel.USER)
    Observable<AuthPrefs> updatePrefs(AuthPrefs prefs);

    @Security(accessLevel = AccessLevel.USER)
    Observable<RedditListing<UserItem>> getFriends();

    @Security(accessLevel = AccessLevel.USER)
    Observable<RedditListing<UserItem>> getBlockedUsers();

}
