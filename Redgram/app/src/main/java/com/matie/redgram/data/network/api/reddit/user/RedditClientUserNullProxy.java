package com.matie.redgram.data.network.api.reddit.user;

import android.support.annotation.Nullable;

import com.google.gson.JsonElement;
import com.matie.redgram.data.models.api.reddit.auth.AuthPrefs;
import com.matie.redgram.data.models.api.reddit.auth.AuthUser;
import com.matie.redgram.data.models.api.reddit.main.RedditUser;
import com.matie.redgram.data.models.main.base.BaseModel;
import com.matie.redgram.data.models.main.base.Listing;
import com.matie.redgram.data.models.main.items.SubredditItem;
import com.matie.redgram.data.models.main.items.UserItem;
import com.matie.redgram.data.models.main.items.submission.PostItem;
import com.matie.redgram.data.models.main.items.submission.comment.CommentsWrapper;

import java.util.List;
import java.util.Map;

import rx.Observable;

/**
 * Implement them to return empty objects to aboid the use of nulls. If a service checks for null
 * then it's safe to return null
 *
 */
public class RedditClientUserNullProxy implements RedditClientInterface {

    @Override
    public Observable<RedditUser> getUserDetails(String username) {
        return null;
    }

    @Override
    public Observable<Listing<BaseModel>> getUserOverview(String username) {
        return null;
    }

    @Override
    public Observable<Listing<PostItem>> getSubredditListing(String query, @Nullable Map<String, String> params, List<PostItem> postItems) {
        return null;
    }

    @Override
    public Observable<Listing<PostItem>> getSubredditListing(String query, @Nullable String filter, @Nullable Map<String, String> params, List<PostItem> postItems) {
        return null;
    }

    @Override
    public Observable<Listing<PostItem>> executeSearch(String subreddit, @Nullable Map<String, String> params, List<PostItem> postItems) {
        return null;
    }

    @Override
    public Observable<Listing<PostItem>> getListing(String front, @Nullable Map<String, String> params, List<PostItem> postItems) {
        return null;
    }

    @Override
    public Observable<Listing<SubredditItem>> getSubreddits(String filter, @Nullable Map<String, String> params) {
        return null;
    }

    @Override
    public Observable<CommentsWrapper> getCommentsByArticle(String article, @Nullable Map<String, String> params) {
        return null;
    }

    //users

    @Override
    public Observable<Listing<SubredditItem>> getSubscriptions(@Nullable Map<String, String> params) {
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
    public Observable<Listing<UserItem>> getFriends() {
        return Observable.just(null);
    }

    @Override
    public Observable<Listing<UserItem>> getBlockedUsers() {
        return Observable.just(null);
    }

    //end user
}
