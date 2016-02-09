package com.matie.redgram.data.models.api.reddit.base;

/**
 * Created by matie on 17/04/15.
 */
public class RedditResponse<T> {

    T data;

    RedditResponse(T data) {
        this.data = data;
    }

    public T getData() {
        return data;
    }
}
