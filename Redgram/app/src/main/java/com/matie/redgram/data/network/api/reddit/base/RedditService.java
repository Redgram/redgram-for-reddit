package com.matie.redgram.data.network.api.reddit.base;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.matie.redgram.data.managers.storage.db.DatabaseManager;
import com.matie.redgram.data.models.api.reddit.auth.AccessToken;
import com.matie.redgram.data.models.api.reddit.base.BooleanDate;
import com.matie.redgram.data.models.api.reddit.base.RedditObject;
import com.matie.redgram.data.network.connection.ConnectionManager;
import com.matie.redgram.data.utils.reddit.BooleanDateDeserializer;
import com.matie.redgram.data.utils.reddit.DateTimeDeserializer;
import com.matie.redgram.data.utils.reddit.RedditObjectDeserializer;
import com.matie.redgram.ui.App;
import com.matie.redgram.ui.common.auth.AuthActivity;

import org.joda.time.DateTime;

import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.inject.Inject;

import okhttp3.Authenticator;
import okhttp3.Cache;
import okhttp3.Challenge;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.Route;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;

/**
 * Created by matie on 15/04/15.
 */
public class RedditService extends RedditServiceBase {

    private static final int MAX_AGE = 60; //1 minute
    private static final int MAX_STALE = 60 * 60 * 24 * 28; //tolerate 4-weeks
    private static final int CACHE_DIR_SIZE = 10 * 1024 * 1024; //
    private static final String REFRESH_HEADER_TAG = "redgram-refresh-header"; //

    private final App app;
    private final Context mContext;
    private final ConnectionManager connectionManager;
    private final Retrofit.Builder retrofitBuilder;
    private final DatabaseManager sm;
    private final RedditAuthProvider authProvider;

    @Inject
    public RedditService(App application) {
        app = application;
        mContext = app.getApplicationContext();
        connectionManager = app.getConnectionManager();
        sm = app.getDatabaseManager();
        retrofitBuilder = getRetrofitBuilder();
        authProvider = buildRetrofit(REDDIT_HOST_ABSOLUTE).create(RedditAuthProvider.class);
    }

    public Retrofit buildRetrofit(String host) {
        return retrofitBuilder.baseUrl(host).build();
    }

    private Retrofit.Builder getRetrofitBuilder() {
        return new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create(getGson()))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .client(myHttpClient());
    }

    private OkHttpClient myHttpClient(){
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY));
        builder.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Interceptor.Chain chain) throws IOException {
                boolean isOnline = connectionManager.isOnline();
                Request originalRequest = chain.request();
                String authHeader = originalRequest.header("Authorization");
                String host = originalRequest.url().host();
                Request.Builder requestBuilder = originalRequest.newBuilder();
                if(host.equalsIgnoreCase(REDDIT_HOST)){
                    requestBuilder.addHeader("content-type", "application/x-www-form-urlencoded");
                    requestBuilder.addHeader("accept", "application/json");
                    requestBuilder.addHeader("Authorization", getCredentials());
                }else if(host.equalsIgnoreCase(OAUTH_HOST)){
                    requestBuilder.addHeader("content-type", "application/x-www-form-urlencoded");
                    requestBuilder.addHeader("accept", "application/json");
                    if(sm.getCurrentToken() != null){
                        requestBuilder.addHeader("Authorization", "bearer "+ sm.getCurrentToken());
                    }else if(authHeader == null){
                        app.startActivity(AuthActivity.intent(app, true));
                        return null;
                    }
                    if(!isOnline){
                        connectionManager.showConnectionStatus(false);
                        requestBuilder.addHeader("Cache-Control",
                                "public, only-if-cached, max-stale=" + MAX_STALE);
                        Log.d("CSTATUS", "no connection!, stale = " + MAX_STALE);
                    }
                }
                Request request = requestBuilder.build();

                Response.Builder responseBuilder = chain.proceed(request).newBuilder();
                if(originalRequest.url().host().equalsIgnoreCase(OAUTH_HOST)){
                    if(isOnline){
                        responseBuilder.header("cache-control", "public, max-age=" + MAX_AGE);
                        responseBuilder.header("Connection-Status", "Connected");
                        Log.d("CSTATUS", "connected to internet! age = " + MAX_AGE + " seconds");
                    }
                }
                return responseBuilder.build();
            }
        });

        builder.authenticator(new MyAuthenticator());
        builder.cache(myCache());
        return builder.build();
    }

    private static Gson getGson() {
        return new GsonBuilder()
                .registerTypeAdapter(BooleanDate.class, new BooleanDateDeserializer())
                .registerTypeAdapter(RedditObject.class, new RedditObjectDeserializer())
                .registerTypeAdapter(DateTime.class, new DateTimeDeserializer())
                .create();
    }

    private Cache myCache(){
        //setup cache
        File httpCacheDir = new File(mContext.getCacheDir(), "HttpCacheDir");
        Cache httpResponseCache = new Cache(httpCacheDir, CACHE_DIR_SIZE);
        return httpResponseCache;
    }

    private class MyAuthenticator implements Authenticator {

        @Override
        public Request authenticate(Route route, Response response) throws IOException {
            if((response.code() == 401 || response.code() == 403) && response.message() != null){ //unauthorized
                List<Challenge> challenges = response.challenges();
                for(Challenge challenge : challenges){
                    if(challenge.scheme().equalsIgnoreCase("Bearer")){
                        if(getRefreshToken() != null){
                            //get new access token and retry
                            retrofit2.Response<AccessToken> accessToken = refreshToken().execute();
                            if(accessToken.body().getAccessToken() != null){
                                sm.setTokenInfo(accessToken.body());
                                return response.request().newBuilder()
                                        .header("Authorization", "bearer " + sm.getCurrentToken())
                                        .build();
                            }
                        }else{
                            //start auth activity and return null
                            app.startActivity(AuthActivity.intent(app, true));
                            return null;
                        }

                    }

                    //rare cases
                    if(challenge.scheme().equalsIgnoreCase("Basic")){
                        Log.d("401 Unauthorized", "Basic Auth failed.");
                        if (response.request().header(REFRESH_HEADER_TAG).equalsIgnoreCase(REFRESH_HEADER_TAG)){
                            //if refresh token mechanism is unauthorized return a message
                            app.getToastHandler().showBackgroundToast("401 Unauthorized Refresh Token", Toast.LENGTH_LONG);
                            app.startActivity(AuthActivity.intent(app, true));
                        }
                        return null;
                    }
                }
            }
            return null;
        }
    }


    public Observable<AccessToken> getAccessToken(String code){
        return authProvider.obtainAccessToken(GRANT_TYPE_AUTHORIZE, code, REDIRECT_URI);
    }

    public Call<AccessToken> refreshToken(){
        return authProvider.refreshAccessToken(REFRESH_HEADER_TAG, GRANT_TYPE_REFRESH, getRefreshToken());
    }

    private String getRefreshToken(){
        return sm.getRefreshToken();
    }

}
