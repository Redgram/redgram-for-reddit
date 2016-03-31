package com.matie.redgram.data.managers.storage.preferences;

/**
 * Created by matie on 2016-02-19.
 */
public interface Constants {
    //preferences keys
    String OAUTH_PREF = "oauth_pref_key";
    String POSTS_PREF = "posts_pref_key";
    String SUBREDDIT_PREF = "subr_pref_key";
    String HOME_PREF = "home_pref_key";
    String SEARCH_PREF = "search_pref_key";

    //object keys
    String ACCESS_TOKEN_KEY = "access_token";
    String REFRESH_TOKEN_KEY = "refresh_token";
    String POSTS_NSFW_KEY = "is_nsfw_enabled";
    String POSTS_MOST_RECENT = "most_recent_post";
    String SUBREDDIT_LIST = "updated_subreddit_list";
}
