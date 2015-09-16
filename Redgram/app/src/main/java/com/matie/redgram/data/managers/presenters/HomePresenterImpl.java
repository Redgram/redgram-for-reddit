package com.matie.redgram.data.managers.presenters;

import android.util.Log;
import android.widget.Toast;

import com.matie.redgram.data.models.PostItem;
import com.matie.redgram.data.models.events.SubredditEvent;
import com.matie.redgram.data.models.main.reddit.PostItemWrapper;
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

    private CompositeSubscription subscriptions;

    private List<PostItem> items;

    private Subscription listingSubscription;


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
        if(subscriptions == null)
            subscriptions = new CompositeSubscription();

        if(subscriptions.isUnsubscribed()){
            subscriptions.add(listingSubscription);
        }
    }
    /**
     * Called onPause of Activity/Fragment
     */
    @Override
    public void unregisterForEvents() {
        if(subscriptions.hasSubscriptions() || subscriptions != null)
            subscriptions.unsubscribe();

        //homeRecyclerView.clearOnScrollListeners();
    }

    /**
     * List populated onCreate(View)
     */
    @Override
    public void getListing(String front, Map<String,String> params) {
        items = new ArrayList<PostItem>();
        homeView.showLoading();
        listingSubscription = getListingSubscription(front, params, REFRESH);
    }

    @Override
    public void getMoreListing(String front, Map<String, String> params) {
        homeView.showLoadMoreIndicator();
        listingSubscription = getListingSubscription(front, params, LOAD_MORE);
    }

    private void hideLoadingEvent(int loadingEvent){
        if(loadingEvent == REFRESH)
            homeView.hideLoading();
        else if(loadingEvent == LOAD_MORE)
            homeView.hideLoadMoreIndicator();
    }

    private Subscription getListingSubscription(String front, Map<String,String> params, int loadingEvent){

        return (Subscription)bindFragment(homeView.getFragment(), redditClient.getListing(front, params))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<PostItemWrapper>() {
                    @Override
                    public void onCompleted() {
                        hideLoadingEvent(loadingEvent);
//                        for(PostItem item : items){
//                            Log.d("ITEM URL", item.getAuthor() + "--" + item.getType() + "--" + item.getId());
//                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        hideLoadingEvent(loadingEvent);
                        homeView.showErrorMessage();
                    }

                    @Override
                    public void onNext(PostItemWrapper wrapper) {
                        items.addAll(wrapper.getItems());
                        //todo: replaceWith should be in a new view interface method
                        homeRecyclerView.replaceWith(items);
                        // TODO: 29/08/15 send the last item name to fragment to use for loading more.
                    }
                });
    }

    //    private Subscription getEventHandler(){
//        return (Subscription)bindFragment(homeView.getFragment(), rxBus.toObservable())
//                .subscribe(event -> identifyAndPerformEvent(event));
//    }

    private void identifyAndPerformEvent(Object event){
        if(event instanceof SubredditEvent){
            Toast.makeText(homeView.getContext(), "subscribed!", Toast.LENGTH_LONG).show();
        }

    }

}
