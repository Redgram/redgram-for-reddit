package com.matie.redgram.data.models.api.reddit.auth;

/**
 * Auth response to {@link Request}. Has to be stored.
 *
 * Created by matie on 2016-02-18.
 */
public class AccessToken {
    String access_token;
    String token_type;
    String expires_in;
    String scope;
    String refresh_token;
}
