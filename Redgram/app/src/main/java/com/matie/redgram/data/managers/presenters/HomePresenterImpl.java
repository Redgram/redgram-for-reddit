package com.matie.redgram.data.managers.presenters;

import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.matie.redgram.R;
import com.matie.redgram.data.managers.storage.preferences.PreferenceManager;
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

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
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
    final private PreferenceManager preferenceManager;

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
        this.preferenceManager = app.getPreferenceManager();
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
     * Only called once on activity started, unlike getListing. So it is safe to add the subscription here
     */
    @Override
    public void getHomeViewWrapper() {
        //make sure it is a new set of items
        homeView.showLoading();

        Map<String,String> params = new HashMap<String, String>();

        Map<String,String> subparams = new HashMap<String, String>();
        subparams.put("limit", "100");

        String filterChoice = homeView.getContext().getResources().getString(R.string.default_filter).toLowerCase();

        RedditListing<SubredditItem> storedListing = getSubredditsFromCache();
        Observable<RedditListing<SubredditItem>> subredditsObservable = null;
        if(storedListing != null){
            subredditsObservable = Observable.just(storedListing);
        }else{
            subredditsObservable = redditClient.getSubscriptions(subparams);
        }

        homeWrapperSubscription = (Subscription)bindFragment(homeView.getBaseFragment(), Observable
                .zip(redditClient.getListing(filterChoice, params, null),
                        subredditsObservable, Observable.just((storedListing != null) ? true : false), new Func3<RedditListing<PostItem>, RedditListing<SubredditItem>, Boolean, HomeViewWrapper>() {
                            @Override
                            public HomeViewWrapper call(RedditListing<PostItem> listing, RedditListing<SubredditItem> subredditListing, Boolean inStore) {
                                HomeViewWrapper homeViewWrapper = new HomeViewWrapper();
                                homeViewWrapper.setRedditListing(listing);
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
                        //dealing with the posts
                        RedditListing<PostItem> redditListing = homeViewWrapper.getRedditListing();

                        //dealing with the subreddits
                        RedditListing<SubredditItem> subredditListing = homeViewWrapper.getSubreddits();

                        subredditItems.addAll(subredditListing.getItems());

                        Collections.sort(subredditItems, new Comparator<SubredditItem>() {
                            @Override
                            public int compare(SubredditItem lhs, SubredditItem rhs) {
                                return lhs.getName().compareToIgnoreCase(rhs.getName());
                            }
                        });

                        //add to preferences
                        if(!homeViewWrapper.getIsSubredditsCached()){
                            String gson = new Gson().toJson(subredditListing);
                            preferenceManager.getSharedPreferences(PreferenceManager.SUBREDDIT_PREF)
                                    .edit().putString(PreferenceManager.SUBREDDIT_LIST, gson)
                                    .commit();
                        }
                    }
                });
    }

    // TODO: 2015-12-03 ENHANCE RE-USABILITY
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


    private RedditListing<SubredditItem> getSubredditsFromCache(){
        RedditListing<SubredditItem> storedListing = null;
        //check if subreddits are in shared preferences
        SharedPreferences sharedPreferences = preferenceManager
                .getSharedPreferences(PreferenceManager.SUBREDDIT_PREF);
        boolean isSubredditsCached = sharedPreferences.getString(PreferenceManager.SUBREDDIT_LIST, null) != null;
        if(isSubredditsCached) {
            String storedListingObject = sharedPreferences.getString(PreferenceManager.SUBREDDIT_LIST, null);

            Type listType = new TypeToken<RedditListing<SubredditItem>>() {}.getType();
            storedListing = new Gson().fromJson(storedListingObject, listType);
        }
        return storedListing;
    }

    public void setLinksPresenter(LinksPresenter linksPresenter) {
        this.linksPresenter = linksPresenter;
    }
}
