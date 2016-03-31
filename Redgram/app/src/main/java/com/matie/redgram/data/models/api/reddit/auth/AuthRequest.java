package com.matie.redgram.data.models.api.reddit.auth;

/**
 * Created by matie on 2016-02-18.
 */
public class AuthRequest {
    String grant_type;
    String code;
    String redirect_uri;

    public AuthRequest(String grantType, String code, String redirectUri){
        this.grant_type = grantType;
        this.code = code;
        this.redirect_uri = redirectUri;
    }
}
