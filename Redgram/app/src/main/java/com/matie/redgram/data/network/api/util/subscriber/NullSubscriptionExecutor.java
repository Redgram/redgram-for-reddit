package com.matie.redgram.data.network.api.util.subscriber;

public interface NullSubscriptionExecutor<T>{
    void executeOnCompleted();
    void executeOnNext(T data);
    void executeOnError(Throwable e);
}
