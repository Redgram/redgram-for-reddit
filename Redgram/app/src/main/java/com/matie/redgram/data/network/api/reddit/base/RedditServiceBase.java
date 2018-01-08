package com.matie.redgram.data.network.api.reddit.base;

import com.matie.redgram.data.network.api.ApiBase;

public abstract class RedditServiceBase extends ApiBase {

    public static final String SSL = "https://";
    public static final String REDDIT_HOST = "www.reddit.com";
    public static final String OAUTH_HOST = "oauth.reddit.com";
    public static final String REDDIT_HOST_ABSOLUTE = SSL+REDDIT_HOST;
    public static final String OAUTH_HOST_ABSOLUTE = SSL+OAUTH_HOST;
    public static final String NAMESPACE = "/api/v1";
    public static final String OAUTH_URL = "/authorize.compact?client_id={client_id}&response_type=code&state=TEST&redirect_uri={redirect_uri}&duration={duration}&scope={scope}";
    public static final String scopeList =
            "account,identity,edit,history,mysubreddits,privatemessages," +
            "read,report,save,submit,subscribe,vote,wikiread";
    public static final String REDIRECT_URI = "http://localhost";
    public static final String DURATION = "permanent";
    public static final String GRANT_TYPE_REFRESH="refresh_token";
    public static final String GRANT_TYPE_AUTHORIZE="authorization_code";
    public static final String GRANT_TYPE_INSTALLED="https://oauth.reddit.com/grants/installed_client";

    public static final String API_KEY = "SWISrdD3qV882w";
    private static final String API_SECRET = "";
    public static final String CREDENTIALS = API_KEY+":"+API_SECRET;

    public static final String BASIC = "basic";
    public static final String BEARER = "bearer";

    @Override
    public String getKey() {
        return API_KEY;
    }

    @Override
    public String getSecret() {
        return API_SECRET;
    }
}
