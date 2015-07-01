package com.matie.redgram.data.managers.presenters;

import android.util.Log;

import com.matie.redgram.data.models.PostItem;
import com.matie.redgram.data.network.api.reddit.RedditClient;
import com.matie.redgram.ui.home.views.widgets.postlist.PostRecyclerView;
import com.matie.redgram.ui.search.views.SearchView;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

import static rx.android.app.AppObservable.bindFragment;

/**
 * Created by matie on 28/06/15.
 */
public class SearchPresenterImpl implements SearchPresenter {

    final private SearchView searchView;
    final private RedditClient redditClient;
    final private PostRecyclerView searchRecyclerView;
    private List<PostItem> items;

    CompositeSubscription subscriptions;
    Subscription searchSubscription;

    @Inject
    public SearchPresenterImpl(SearchView searchView, RedditClient redditClient) {
        this.searchView = searchView;
        this.redditClient = redditClient;

        searchRecyclerView = searchView.getRecyclerView();
    }

    @Override
    public void registerForEvents() {
        subscriptions = new CompositeSubscription();
    }


    @Override
    public void unregisterForEvents() {
        subscriptions.unsubscribe();
    }

    @Override
    public void executeSearch(String query) {
        //empty items
        items = new ArrayList<PostItem>();

        searchView.showProgress();

        searchSubscription = (Subscription)bindFragment(searchView.getFragment(), redditClient.getSubredditListing(query))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<PostItem>() {
                    @Override
                    public void onCompleted() {
                        searchView.hideProgress();
                        searchRecyclerView.replaceWith(items);
                    }

                    @Override
                    public void onError(Throwable e) {
                        searchView.showErrorMessage();
                    }

                    @Override
                    public void onNext(PostItem postItem) {
                        items.add(postItem);
                        Log.d("ITEM URL", postItem.getAuthor() + "--" + postItem.getType() + "--" + postItem.getUrl());
                    }
                });
        //todo: executes every time I search!! FIX
        if(!searchSubscription.isUnsubscribed()){
            subscriptions.add(searchSubscription);
        }
    }
}
