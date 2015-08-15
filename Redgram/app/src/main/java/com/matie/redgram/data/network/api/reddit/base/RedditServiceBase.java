package com.matie.redgram.data.network.api.reddit.base;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.matie.redgram.ui.App;
import com.matie.redgram.data.network.connection.ConnectionStatus;
import com.matie.redgram.data.models.reddit.base.RedditObject;
import com.matie.redgram.data.utils.reddit.DateTimeDeserializer;
import com.matie.redgram.data.utils.reddit.RedditObjectDeserializer;
import com.squareup.okhttp.Cache;
import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.joda.time.DateTime;

import java.io.File;
import java.io.IOException;

import javax.inject.Inject;

import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.client.OkClient;
import retrofit.converter.GsonConverter;

/**
 * Created by matie on 15/04/15.
 */
public class RedditServiceBase extends RedditBase {

    private static final int MAX_AGE = 60; //1 minute
    private static final int MAX_STALE = 60 * 60 * 24 * 28; //tolerate 4-weeks
    private static final int CACHE_DIR_SIZE = 10 * 1024 * 1024; //

    private final Context mContext;
    private final ConnectionStatus connectionStatus;
    private final RestAdapter.Builder adapterBuilder;

    @Inject
    public RedditServiceBase(App app, ConnectionStatus cs) {
        mContext = app.getApplicationContext();
        connectionStatus = cs;

        //create a builder once that has all the common build components
        adapterBuilder = getAdapterBuilder();
    }

    public RestAdapter getRestAdapter() {

        //todo: get rid of Request Interceptor and use okHttp request interceptor
         return adapterBuilder.setEndpoint(REDDIT_HOST)
                 .setRequestInterceptor(getInterceptor()).build();
    }

    private RestAdapter.Builder getAdapterBuilder(){
        return new RestAdapter.Builder()
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .setConverter(new GsonConverter(getGson()))
                .setClient(new OkClient(getHttpClient()));
    }

    private RequestInterceptor getInterceptor() {
        return new RequestInterceptor() {
            @Override
            public void intercept(RequestFacade request){

                request.addHeader("Accept", "application/json");

                if(!connectionStatus.isOnline()){
                    request.addHeader("Cache-Control",
                            "public, only-if-cached, max-stale=" + MAX_STALE);
                    Log.d("CSTATUS", "no connection!, stale = "+ MAX_STALE);
                }

                //todo: Auth headers
            }
        };
    }

    private static Gson getGson() {
        return new GsonBuilder()
                .registerTypeAdapter(RedditObject.class, new RedditObjectDeserializer())
                .registerTypeAdapter(DateTime.class, new DateTimeDeserializer())
                .create();
    }


    private OkHttpClient getHttpClient(){

        Cache cache = getCache();

        OkHttpClient client = new OkHttpClient();
        client.networkInterceptors().add(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {

                boolean isOnline = connectionStatus.isOnline();

                Response.Builder responseBuilder = chain.proceed(chain.request()).newBuilder();
                if (isOnline) {
                    responseBuilder.header("cache-control", "public, max-age=" + MAX_AGE);
                    Log.d("CSTATUS", "connected to internet! age = " + MAX_AGE + " seconds");
                }

                return responseBuilder.build();
            }
        });

        if(cache != null)
            client.setCache(cache);

        return client;
    }

    private Cache getCache(){
        //setup cache
        File httpCacheDir = new File(mContext.getCacheDir(), "HttpCacheDir");

        Cache httpResponseCache = null;

        try {
            httpResponseCache = new Cache(httpCacheDir, CACHE_DIR_SIZE);
        } catch (IOException e) {
            Log.e("Retrofit", "Could not create http cache", e);
        }

        return httpResponseCache;
    }

}
