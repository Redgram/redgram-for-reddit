package com.matie.redgram.data.managers.presenters;

import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.matie.redgram.R;
import com.matie.redgram.data.managers.preferences.PreferenceManager;
import com.matie.redgram.data.models.main.items.SubredditItem;
import com.matie.redgram.data.models.main.reddit.RedditListing;
import com.matie.redgram.data.network.api.reddit.RedditClient;
import com.matie.redgram.ui.App;
import com.matie.redgram.ui.common.views.widgets.subreddit.SubredditRecyclerView;
import com.matie.redgram.ui.subcription.views.SubscriptionView;

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
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

import static rx.android.app.AppObservable.bindFragment;

/**
 * Created by matie on 2015-11-29.
 */
public class SubscriptionPresenterImpl implements SubscriptionPresenter {
    final private SubscriptionView subscriptionView;
    final private SubredditRecyclerView subredditRecyclerView;
    final private RedditClient redditClient;
    final private PreferenceManager preferenceManager;

    private List<SubredditItem> subredditItems;
    private String loadMoreId = "";

    private CompositeSubscription subscriptions;
    private Subscription subredditSubscription;

    @Inject
    public SubscriptionPresenterImpl(SubscriptionView subscriptionView, App app) {
        this.subscriptionView = subscriptionView;
        this.subredditRecyclerView = subscriptionView.getRecyclerView();
        this.redditClient = app.getRedditClient();
        this.preferenceManager = app.getPreferenceManager();
        this.subredditItems = new ArrayList<SubredditItem>();
    }

    @Override
    public void registerForEvents() {
        if(subscriptions == null){
            subscriptions = new CompositeSubscription();
        }
        if(subscriptions.isUnsubscribed()){
            subscriptions.add(subredditSubscription);
        }
    }

    @Override
    public void unregisterForEvents() {
        if(subscriptions.hasSubscriptions() || subscriptions != null){
            subscriptions.unsubscribe();
        }
    }

    @Override
    public void getSubreddits() {
        subredditItems.clear();
        subscriptionView.showLoading();

        Map<String,String> params = new HashMap<String, String>();
        params.put("limit", "100");

        String subredditFilterChoice = subscriptionView.getContext().getResources().getString(R.string.default_subreddit_filter).toLowerCase();

        //check if subreddits are in shared preferences
        SharedPreferences sharedPreferences = preferenceManager
                .getSharedPreferences(PreferenceManager.SUBREDDIT_PREF);
        Observable<RedditListing<SubredditItem>> subredditsObservable = null;
        boolean isSubredditsCached = sharedPreferences.getString(PreferenceManager.SUBREDDIT_LIST, null) != null;
        if(isSubredditsCached){
            String storedListingObject = sharedPreferences.getString(PreferenceManager.SUBREDDIT_LIST, null);

            Type listType = new TypeToken<RedditListing<SubredditItem>>(){}.getType();
            RedditListing<SubredditItem> storedListing = new Gson().fromJson(storedListingObject, listType);

            subredditsObservable = Observable.just(storedListing);

        }else{
            subredditsObservable = redditClient.getSubreddits(subredditFilterChoice, params);
        }

        subredditSubscription = (Subscription)bindFragment(subscriptionView.getFragment(), subredditsObservable)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<RedditListing<SubredditItem>>() {
                    @Override
                    public void onCompleted() {
                        subscriptionView.hideLoading();
                    }

                    @Override
                    public void onError(Throwable e) {
                        subscriptionView.showErrorMessage(e.toString());
                    }

                    @Override
                    public void onNext(RedditListing<SubredditItem> subredditListing) {
                        subredditItems.addAll(subredditListing.getItems());

                        //todo optimize
                        Collections.sort(subredditItems, new Comparator<SubredditItem>() {
                            @Override
                            public int compare(SubredditItem lhs, SubredditItem rhs) {
                                return lhs.getName().compareToIgnoreCase(rhs.getName());
                            }
                        });

                        subredditRecyclerView.replaceWith(subredditItems);
                        loadMoreId = subredditListing.getAfter(); //if needed
                    }
                });
    }
}
