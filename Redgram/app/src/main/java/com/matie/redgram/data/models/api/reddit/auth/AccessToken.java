package com.matie.redgram.data.models.api.reddit.auth;

/**
 * Auth response to {@link AuthRequest}. Has to be stored.
 *
 * Created by matie on 2016-02-18.
 */
public class AccessToken {
    private String access_token;
    private String token_type;
    private String expires_in;
    private String scope;
    private String refresh_token;

    public String getAccessToken() {
        return access_token;
    }

    public String getTokenType() {
        return token_type;
    }

    public String getExpiresIn() {
        return expires_in;
    }

    public String getScope() {
        return scope;
    }

    public String getRefreshToken() {
        return refresh_token;
    }
}
