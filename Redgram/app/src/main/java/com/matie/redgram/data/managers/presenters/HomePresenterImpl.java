package com.matie.redgram.data.managers.presenters;

import android.util.Log;
import android.widget.Toast;

import com.matie.redgram.data.managers.rxbus.RxBus;
import com.matie.redgram.data.models.PostItem;
import com.matie.redgram.data.models.events.SubredditEvent;
import com.matie.redgram.data.network.api.reddit.RedditClient;
import com.matie.redgram.ui.home.views.HomeView;
import com.matie.redgram.ui.common.views.widgets.postlist.PostRecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

import static rx.android.app.AppObservable.bindFragment;

/**
 * Created by matie on 12/04/15.
 */

public class HomePresenterImpl implements HomePresenter{

    final private HomeView homeView;
    final private PostRecyclerView homeRecyclerView;
    final private RedditClient redditClient;

    static private RxBus rxBus;

    private CompositeSubscription subscriptions;

    private List<PostItem> items;

    private Subscription subredditSubscription;

    /**
     * Called onCreate(View) of Activity/Fragment
     * @param homeView
     */
    @Inject
    public HomePresenterImpl(HomeView homeView, RedditClient redditClient) {
        this.homeView = homeView;
        this.homeRecyclerView = homeView.getRecyclerView();
        this.redditClient = redditClient;
        this.items = new ArrayList<PostItem>();
    }

    /**
     * Called onResume of Activity/Fragment
     */
    @Override
    public void registerForEvents() {

        if(rxBus == null)
            rxBus = RxBus.getDefault();

        if(subscriptions == null)
            subscriptions = new CompositeSubscription();

        if(subscriptions.isUnsubscribed()){
            subscriptions.add(subredditSubscription);
            subscriptions.add(getEventHandler());
        }

    }
    /**
     * Called onPause of Activity/Fragment
     */
    @Override
    public void unregisterForEvents() {
        if(subscriptions.hasSubscriptions() || subscriptions != null)
            subscriptions.unsubscribe();
    }

    /**
     * List populated onCreate(View)
     *
     * todo: Check if it's better to populate onCreate or onStart!!!
     */
    @Override
    public void getListing(String front, Map<String,String> params) {//empty items and hide list
        items = new ArrayList<PostItem>();
        homeView.showProgress();
        subredditSubscription =
                (Subscription)bindFragment(homeView.getFragment(), redditClient.getListing(front, params))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<PostItem>() {
                    @Override
                    public void onCompleted() {
                        homeView.hideProgress();
                        homeRecyclerView.replaceWith(items);
                    }

                    @Override
                    public void onError(Throwable e) {
                        homeView.hideProgress();
                        homeView.showErrorMessage();
                    }

                    @Override
                    public void onNext(PostItem postItem) {
                        items.add(postItem);
                        Log.d("ITEM URL", postItem.getAuthor() + "--" + postItem.getType() + "--" + postItem.getUrl());
                    }
                });

    }

    private Subscription getEventHandler(){
        return (Subscription)bindFragment(homeView.getFragment(), rxBus.toObservable())
                .subscribe(event -> identifyAndPerformEvent(event));
    }

    private void identifyAndPerformEvent(Object event){
        if(event instanceof SubredditEvent){
            Toast.makeText(homeView.getContext(), "subscribed!", Toast.LENGTH_LONG).show();
        }

    }

}
