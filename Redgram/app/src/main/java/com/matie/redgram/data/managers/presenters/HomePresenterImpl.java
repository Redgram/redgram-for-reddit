package com.matie.redgram.data.managers.presenters;

import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.matie.redgram.R;
import com.matie.redgram.data.managers.storage.db.DatabaseManager;
import com.matie.redgram.data.models.db.Subreddit;
import com.matie.redgram.data.models.db.User;
import com.matie.redgram.data.models.main.home.HomeViewWrapper;
import com.matie.redgram.data.models.main.items.PostItem;
import com.matie.redgram.data.models.main.items.SubredditItem;
import com.matie.redgram.data.models.main.reddit.RedditListing;
import com.matie.redgram.data.network.api.reddit.RedditClient;
import com.matie.redgram.ui.App;
import com.matie.redgram.ui.home.views.HomeView;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import io.realm.RealmList;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func2;
import rx.functions.Func3;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

import static rx.android.app.AppObservable.bindFragment;


/**
 * Created by matie on 12/04/15.
 */

public class HomePresenterImpl implements HomePresenter{
    final private HomeView homeView;
    final private RedditClient redditClient;
    final private DatabaseManager databaseManager;

    private LinksPresenter linksPresenter;
    private CompositeSubscription subscriptions;

    private List<SubredditItem> subredditItems;

    //global subscriptions
    private Subscription homeWrapperSubscription;


    /**
     * Called onCreate(View) of MainActivity/Fragment
     * @param homeView
     */
    @Inject
    public HomePresenterImpl(HomeView homeView, App app) {
        this.homeView = homeView;
        this.redditClient = app.getRedditClient();
        this.databaseManager = app.getDatabaseManager();
        this.subredditItems = new ArrayList<SubredditItem>();
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
        //make sure it is a new set of items
        homeView.showLoading();

        Map<String,String> params = new HashMap<String, String>();

        Map<String,String> subparams = new HashMap<String, String>();
        subparams.put("limit", "100");

        RedditListing<SubredditItem> storedListing =  getSubredditsFromCache();
        Observable<RedditListing<SubredditItem>> subredditsObservable;
        if(storedListing != null){
            subredditsObservable = Observable.just(storedListing);
        }else{
            subredditsObservable = redditClient.getSubscriptions(subparams);
        }

        homeWrapperSubscription = bindFragment(homeView.getBaseFragment(), Observable
                .zip(subredditsObservable, Observable.just((storedListing != null)), new Func2<RedditListing<SubredditItem>, Boolean, HomeViewWrapper>() {
                            @Override
                            public HomeViewWrapper call(RedditListing<SubredditItem> subredditListing, Boolean inStore) {
                                HomeViewWrapper homeViewWrapper = new HomeViewWrapper();
                                homeViewWrapper.setSubreddits(subredditListing);
                                homeViewWrapper.setIsSubredditsCached(inStore);
                                return homeViewWrapper;
                            }
                        }))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<HomeViewWrapper>() {
                    @Override
                    public void onCompleted() {
                        homeView.hideLoading();
                    }

                    @Override
                    public void onError(Throwable e) {
                        homeView.hideLoading();
                        homeView.showErrorMessage(e.toString());
                    }

                    @Override
                    public void onNext(HomeViewWrapper homeViewWrapper) {
                        //dealing with the subreddits
                        RedditListing<SubredditItem> subredditListing = homeViewWrapper.getSubreddits();
                        subredditItems.addAll(subredditListing.getItems());
                        Collections.sort(subredditItems, new Comparator<SubredditItem>() {
                            @Override
                            public int compare(SubredditItem lhs, SubredditItem rhs) {
                                return lhs.getName().compareToIgnoreCase(rhs.getName());
                            }
                        });
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

    public RedditListing<SubredditItem> getSubredditsFromCache() {
        return databaseManager.getSubreddits();
    }

    private void setSubredditsInCache(RedditListing<SubredditItem> listing) {
        databaseManager.setSubreddits(listing.getItems());
    }
}
