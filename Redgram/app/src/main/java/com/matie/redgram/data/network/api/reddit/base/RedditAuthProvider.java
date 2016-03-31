package com.matie.redgram.data.network.api.reddit.base;

import com.matie.redgram.data.models.api.reddit.auth.AccessToken;


import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Header;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by matie on 2016-02-20.
 */
public interface RedditAuthProvider {
    //oauth
    String NAMESPACE = "api/v1/";
    String ACCESS_TOKEN = "access_token";

    /**
     * Get the access token after allowing access to user data
     *
     * @param grantType
     * @param code
     * @param redirectUri
     * @return
     */
    @FormUrlEncoded
    @POST(NAMESPACE+ACCESS_TOKEN)
    Observable<AccessToken> obtainAccessToken(
            @Field("grant_type") String grantType,
            @Field("code") String code,
            @Field("redirect_uri") String redirectUri
    );

    /**
     * Get a new access token by passing the grant type refresh_token and the actual
     * refresh token obtained from
     * {@link RedditAuthProvider#obtainAccessToken(String, String, String)}
     *
     * @param grantType
     * @param refreshToken
     * @return
     */
    @FormUrlEncoded
    @POST(NAMESPACE+ACCESS_TOKEN)
    Call<AccessToken> refreshAccessToken(
            @Header("redgram-refresh-header") String msg,
            @Field("grant_type") String grantType,
            @Field("refresh_token") String refreshToken);
}
