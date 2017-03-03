package com.matie.redgram.data.network.api.reddit.base;

import com.matie.redgram.data.models.api.reddit.auth.AccessToken;
import com.matie.redgram.data.network.api.utils.AccessLevel;
import com.matie.redgram.data.network.api.utils.Security;

import retrofit2.Call;
import rx.Observable;

/**
 * Created by matie on 2016-11-01.
 */
public interface RedditServiceInterface {

    @Security(accessLevel = AccessLevel.ANY)
    Observable<AccessToken> getAccessToken(String code);

    @Security(accessLevel = AccessLevel.ANY)
    Observable<AccessToken> getAccessTokenObservable();

    @Security(accessLevel = AccessLevel.ANY)
    Call<AccessToken> getAccessToken();

    @Security(accessLevel = AccessLevel.USER)
    Call<AccessToken> refreshToken();

    @Security(accessLevel = AccessLevel.ANY)
    Observable<AccessToken> revokeToken(String tokenType);

    @Security(accessLevel = AccessLevel.ANY)
    Observable<AccessToken> revokeToken(String token, String tokenType);
}
