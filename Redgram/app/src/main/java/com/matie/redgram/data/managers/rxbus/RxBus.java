package com.matie.redgram.data.managers.rxbus;

import rx.Observable;
import rx.subjects.PublishSubject;
import rx.subjects.SerializedSubject;
import rx.subjects.Subject;

/**
 * Created by matie on 12/05/15.
 */
public class RxBus {

    private static final RxBus defaultBus = new RxBus();
    private static final Subject<Object, Object> mbus = new SerializedSubject<>(PublishSubject.create());

    public static RxBus getDefault(){
        return defaultBus;
    }
    public static void send(Object o){
        mbus.onNext(o);
    }
    public static Observable<Object> toObservable(){
        return mbus;
    }
    public static boolean hasObservers(){
        return mbus.hasObservers();
    }
}
