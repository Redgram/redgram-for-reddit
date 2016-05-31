package com.matie.redgram.data.managers.presenters;

import com.matie.redgram.data.managers.storage.db.DatabaseManager;
import com.matie.redgram.data.models.db.Subreddit;
import com.matie.redgram.data.models.main.home.HomeViewWrapper;
import com.matie.redgram.data.models.main.items.SubredditItem;
import com.matie.redgram.data.models.main.reddit.RedditListing;
import com.matie.redgram.data.network.api.reddit.RedditClient;
import com.matie.redgram.ui.App;
import com.matie.redgram.ui.home.views.HomeView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;


/**
 * Home Presenter Implementation
 */

public class HomePresenterImpl implements HomePresenter{
    final private HomeView homeView;
    final private RedditClient redditClient;
    final private DatabaseManager databaseManager;

    //private LinksPresenter linksPresenter;
    private CompositeSubscription subscriptions;

    private List<SubredditItem> subredditItems;

    //global subscriptions
    private Subscription homeWrapperSubscription;


    /**
     * Called onCreate(View) of MainActivity/Fragment
     * @param homeView View for presenter to interact with
     */
    @Inject
    public HomePresenterImpl(HomeView homeView, App app) {
        this.homeView = homeView;
        this.redditClient = app.getRedditClient();
        this.databaseManager = app.getDatabaseManager();
        this.subredditItems = new ArrayList<>();
    }

    /**
     * Called onResume of MainActivity/Fragment
     */
    @Override
    public void registerForEvents() {
        if(subscriptions == null)
            subscriptions = new CompositeSubscription();

        if(!subscriptions.isUnsubscribed()){
            if(homeWrapperSubscription != null){
                subscriptions.add(homeWrapperSubscription);
            }
        }
    }
    /**
     * Called onPause of MainActivity/Fragment
     */
    @Override
    public void unregisterForEvents() {
        if(subscriptions.hasSubscriptions() && subscriptions != null){
            subscriptions.unsubscribe();
        }
    }

    /**
     * gets Subscriptions for now
     */
    @Override
    public void getHomeViewWrapper() {
        // TODO: 2016-05-11 find a way to make this wrapper subscribe on the links container call
        //homeView.showLoading();

        Map<String,String> subparams = new HashMap<>();
        subparams.put("limit", "100");

        RedditListing<SubredditItem> storedListing =  getSubredditsFromCache();
        Observable<RedditListing<SubredditItem>> subredditsObservable;
        if(storedListing != null){
            subredditsObservable = Observable.just(storedListing);
        }else{
            subredditsObservable = redditClient.getSubscriptions(subparams);
        }

        homeWrapperSubscription = Observable
                .zip(subredditsObservable, Observable.just((storedListing != null)), (subredditListing, inStore) -> {
                    HomeViewWrapper homeViewWrapper = new HomeViewWrapper();
                    homeViewWrapper.setSubreddits(subredditListing);
                    homeViewWrapper.setIsSubredditsCached(inStore);
                    return homeViewWrapper;
                })
                .compose(homeView.getBaseFragment().bindToLifecycle())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<HomeViewWrapper>() {
                    @Override
                    public void onCompleted() {
                        //homeView.hideLoading();
                    }

                    @Override
                    public void onError(Throwable e) {
                        //homeView.hideLoading();
                        homeView.showErrorMessage(e.toString());
                    }

                    @Override
                    public void onNext(HomeViewWrapper homeViewWrapper) {
                        //dealing with the subreddits
                        RedditListing<SubredditItem> subredditListing = homeViewWrapper.getSubreddits();
                        subredditItems.addAll(subredditListing.getItems());
                        Collections.sort(subredditItems, (lhs, rhs) -> lhs.getName().compareToIgnoreCase(rhs.getName()));
                        //add to db
                        if(!homeViewWrapper.getIsSubredditsCached()){
                            setSubredditsInCache(homeViewWrapper.getSubreddits());
                        }
                    }
                });
    }

    @Override
    public List<String> getSubreddits() {
        List<String> subredditNames = new ArrayList<>();

        if(subredditItems != null && subredditItems.size() > 0){
            for(SubredditItem item : subredditItems){
                subredditNames.add(item.getName());
            }
        }else{
            RedditListing<SubredditItem> storedListing = getSubredditsFromCache();
            if(storedListing != null){
                for(SubredditItem item : storedListing.getItems()){
                    subredditNames.add(item.getName());
                }
            }
        }

        return subredditNames;
    }

    private RedditListing<SubredditItem> getSubredditsFromCache() {
        List<Subreddit> subreddits = databaseManager.getSubreddits();
        if(!subreddits.isEmpty()){
            return buildSubredditListing(subreddits);
        }
        return null;
    }

    private void setSubredditsInCache(RedditListing<SubredditItem> listing) {
        databaseManager.setSubreddits(listing.getItems());
    }

    private RedditListing<SubredditItem> buildSubredditListing(List<Subreddit> subreddits) {
        RedditListing<SubredditItem> listing = new RedditListing<>();
        List<SubredditItem> items = new ArrayList<>();
        for(Subreddit subreddit : subreddits){
            SubredditItem item = new SubredditItem();
            item.setName(subreddit.getName());
            items.add(item);
        }
        listing.setItems(items);
        return listing;
    }
}
