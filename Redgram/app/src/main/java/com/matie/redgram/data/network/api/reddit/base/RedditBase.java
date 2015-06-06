package com.matie.redgram.data.network.api.reddit.base;

import com.matie.redgram.data.network.api.ApiBase;

/**
 * Created by matie on 15/04/15.
 * Check for nulls before using the methods.
 */
public class RedditBase extends ApiBase {

    protected static final String REDDIT_HOST = "http://www.reddit.com";
    protected static final String OAUTH_URL = "https://www.reddit.com/api/v1/authorize";
    protected static final String scopeList = "identity,edit,history,mysubreddits, privatemessages," +
            "read,report,save,submit, subscribe, vote, wikiread";

    private static String API_KEY = "";
    private static String API_SECRET = "";

    @Override
    public String getKey() {
        return API_KEY;
    }

    @Override
    public String getSecret() {
        return API_SECRET;
    }
}
