package com.matie.redgram.data.network.api.util.subscriber;

import rx.Subscriber;

/**
 * Created by matie on 2016-11-04.
 */
public class NullCheckSubscriber<T> extends Subscriber<T> {
    private NullSubscriptionExecutor<T> performer;

    public NullCheckSubscriber(NullSubscriptionExecutor<T> performer) {
        this.performer = performer;
    }

    @Override
    public void onCompleted() {
        performer.executeOnCompleted();
    }

    @Override
    public void onError(Throwable e) {
        performer.executeOnError(e);
    }

    @Override
    public void onNext(T t) {
        if(t != null){
            performer.executeOnNext(t);
        }
    }

}

