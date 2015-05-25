package com.matie.redgram.models.reddit.base;

/**
 * Created by matie on 17/04/15.
 */
public class RedditResponse<T> {
    RedditResponse(T data) {
        this.data = data;
    }

    T data;

    public T getData() {
        return data;
    }
}
