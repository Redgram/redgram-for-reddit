package com.matie.redgram.data.models.api.reddit.auth;

/**
 * Returns all information needed if an access token is valid
 *
 * Created by matie on 2016-02-26.
 */
public class AuthWrapper {

    private AccessToken accessToken;
    private AuthUser authUser;
    private AuthPrefs authPrefs;

    public AccessToken getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(AccessToken accessToken) {
        this.accessToken = accessToken;
    }

    public AuthUser getAuthUser() {
        return authUser;
    }

    public void setAuthUser(AuthUser authUser) {
        this.authUser = authUser;
    }

    public AuthPrefs getAuthPrefs() {
        return authPrefs;
    }

    public void setAuthPrefs(AuthPrefs authPrefs) {
        this.authPrefs = authPrefs;
    }
}
