package com.matie.redgram.data.network.api.reddit;

import android.support.annotation.Nullable;

import com.google.gson.JsonElement;
import com.matie.redgram.data.models.api.reddit.auth.AccessToken;
import com.matie.redgram.data.models.api.reddit.auth.AuthPrefs;
import com.matie.redgram.data.models.api.reddit.auth.AuthUser;
import com.matie.redgram.data.models.api.reddit.auth.AuthWrapper;
import com.matie.redgram.data.models.main.items.PostItem;
import com.matie.redgram.data.models.main.items.SubredditItem;
import com.matie.redgram.data.models.main.items.UserItem;
import com.matie.redgram.data.models.main.items.comment.CommentsWrapper;
import com.matie.redgram.data.models.main.reddit.RedditListing;
import com.matie.redgram.data.models.main.reddit.RedditObject;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import rx.Observable;

/**
 * Implement them to return empty objects to aboid the use of nulls. If a service checks for null
 * then it's safe to return null
 */
public class RedditClientUserNullProxy implements RedditClientInterface {
    @Override
    public Observable<AuthWrapper> getAuthWrapper(String code) {
        return null;
    }

    @Override
    public Observable<AuthWrapper> getAuthWrapper() {
        return null;
    }

    @Override
    public Observable<RedditListing<RedditObject>> getUserOverview(String username) {
        return null;
    }

    @Override
    public Observable<RedditListing<PostItem>> getSubredditListing(String query, @Nullable Map<String, String> params, List<PostItem> postItems) {
        return null;
    }

    @Override
    public Observable<RedditListing<PostItem>> getSubredditListing(String query, @Nullable String filter, @Nullable Map<String, String> params, List<PostItem> postItems) {
        return null;
    }

    @Override
    public Observable<RedditListing<PostItem>> executeSearch(String subreddit, @Nullable Map<String, String> params, List<PostItem> postItems) {
        return null;
    }

    @Override
    public Observable<RedditListing<PostItem>> getListing(String front, @Nullable Map<String, String> params, List<PostItem> postItems) {
        return null;
    }

    @Override
    public Observable<RedditListing<SubredditItem>> getSubreddits(String filter, @Nullable Map<String, String> params) {
        return null;
    }

    @Override
    public Observable<CommentsWrapper> getCommentsByArticle(String article, @Nullable Map<String, String> params) {
        return null;
    }

    //users

    @Override
    public Observable<RedditListing<SubredditItem>> getSubscriptions(@Nullable Map<String, String> params) {
        return Observable.just(null);
    }

    @Override
    public Observable<AuthUser> getUser(@Nullable String accessToken) {
        return Observable.just(null);
    }

    @Override
    public Observable<AuthPrefs> getUserPrefs(@Nullable String accessToken) {
        return Observable.just(null);
    }

    @Override
    public Observable<JsonElement> delete(String id) {
        return Observable.just(null);
    }

    @Override
    public Observable<JsonElement> voteFor(String id, Integer direction) {
        return Observable.just(null);
    }

    @Override
    public Observable<JsonElement> hide(String id, boolean hide) {
        return Observable.just(null);
    }

    @Override
    public Observable<JsonElement> save(String id, boolean save) {
        return Observable.just(null);
    }

    @Override
    public Observable<JsonElement> report(String id) {
        return Observable.just(null);
    }

    @Override
    public Observable<AuthPrefs> updatePrefs(AuthPrefs prefs) {
        return Observable.just(null);
    }

    @Override
    public Observable<RedditListing<UserItem>> getFriends() {
        return Observable.just(null);
    }

    @Override
    public Observable<RedditListing<UserItem>> getBlockedUsers() {
        return Observable.just(null);
    }

    //end user

    //oauth

    @Override
    public Observable<AccessToken> getAccessToken(String code) {
        return null;
    }

    @Override
    public Observable<AccessToken> getAccessToken() {
        return null;
    }

    @Override
    public Call<AccessToken> refreshToken() {
        return null;
    }

    @Override
    public Observable<AccessToken> revokeToken(String tokenType) {
        return null;
    }

    @Override
    public Observable<AccessToken> revokeToken(String token, String tokenType) {
        return null;
    }

    //end oauth
}
