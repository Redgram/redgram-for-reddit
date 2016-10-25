package com.matie.redgram.data.managers.presenters;

import com.matie.redgram.data.managers.storage.db.DatabaseManager;
import com.matie.redgram.data.models.db.Subreddit;
import com.matie.redgram.data.models.main.items.SubredditItem;
import com.matie.redgram.data.models.main.reddit.RedditListing;
import com.matie.redgram.data.network.api.reddit.RedditClient;
import com.matie.redgram.ui.App;
import com.matie.redgram.ui.common.base.BaseFragment;
import com.matie.redgram.ui.common.views.widgets.subreddit.SubredditRecyclerView;
import com.matie.redgram.ui.subcription.views.SubscriptionView;

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
 * Subscription Presenter Implementation
 */
public class SubscriptionPresenterImpl implements SubscriptionPresenter {
    final private SubscriptionView subscriptionView;
    final private SubredditRecyclerView subredditRecyclerView;
    final private RedditClient redditClient;
    final private DatabaseManager databaseManager;

    private List<SubredditItem> subredditItems;
    private String loadMoreId = "";

    private CompositeSubscription subscriptions;
    private Subscription subredditSubscription;

    @Inject
    public SubscriptionPresenterImpl(SubscriptionView subscriptionView, App app) {
        this.subscriptionView = subscriptionView;
        this.subredditRecyclerView = subscriptionView.getRecyclerView();
        this.redditClient = app.getRedditClient();
        this.subredditItems = new ArrayList<>();
        this.databaseManager = app.getDatabaseManager();
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
        if(subscriptions != null && subscriptions.hasSubscriptions()){
            subscriptions.unsubscribe();
        }
    }

    @Override
    public void getSubreddits(boolean forceNetwork) {
        subredditItems.clear();
        subscriptionView.showLoading();

        Map<String,String> params = new HashMap<>();
        params.put("limit", "100");

        //check if subreddits are in db
        Observable<RedditListing<SubredditItem>> subredditsObservable;
        RedditListing<SubredditItem> storedListing = null;
        if(!forceNetwork){
            storedListing = getSubredditsFromCache();
        }

        if(storedListing != null){
            subredditsObservable = Observable.just(storedListing);
        }else{
            subredditsObservable = redditClient.getSubscriptions(params);
        }

        subredditSubscription = subredditsObservable
                .compose(((BaseFragment)subscriptionView.getContentContext()).bindToLifecycle())
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
                        Collections.sort(subredditItems, (lhs, rhs) -> lhs.getName().compareToIgnoreCase(rhs.getName()));

                        subredditRecyclerView.replaceWith(subredditItems);
                        if(subredditListing.getAfter() != null){
                            loadMoreId = subredditListing.getAfter(); //if needed
                        }
                        //add to db if from network
                        if(forceNetwork){
                            databaseManager.setSubreddits(subredditItems);
                        }
                    }
                });
    }

    private RedditListing<SubredditItem> getSubredditsFromCache() {
        List<Subreddit> subreddits = databaseManager.getSubreddits();
        if(!subreddits.isEmpty()){
            return buildSubredditListing(subreddits);
        }
        return null;
    }

    private RedditListing<SubredditItem> buildSubredditListing(List<Subreddit> subreddits) {
        RedditListing<SubredditItem> listing = new RedditListing<>();
        List<SubredditItem> items = new ArrayList<>();
        for(Subreddit subreddit : subreddits){
            SubredditItem sbItem = new SubredditItem();
            sbItem.setName(subreddit.getName());
            sbItem.setAccountsActive(subreddit.getAccountsActive());
            sbItem.setDescription(subreddit.getDescription());
            sbItem.setDescriptionHtml(subreddit.getDescription());
            sbItem.setSubscribersCount(subreddit.getSubscribersCount());
            sbItem.setSubredditType(subreddit.getSubredditType());
            sbItem.setSubmissionType(subreddit.getSubmissionType());
            items.add(sbItem);
        }
        listing.setItems(items);
        return listing;
    }
}
