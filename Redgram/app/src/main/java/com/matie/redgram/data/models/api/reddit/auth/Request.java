package com.matie.redgram.data.models.api.reddit.auth;

/**
 * Created by matie on 2016-02-18.
 */
public class Request {
    String grantType;
    String code;
    String redirectUri;

    public Request(String grantType, String code, String redirectUri){
        this.grantType = grantType;
        this.code = code;
        this.redirectUri = redirectUri;
    }
}
