package com.matie.redgram.data.managers.presenters;

import com.matie.redgram.data.network.api.reddit.RedditClient;
import com.matie.redgram.ui.App;
import com.matie.redgram.ui.search.views.SearchView;

import javax.inject.Inject;

import rx.subscriptions.CompositeSubscription;

/**
 * Search Presenter Implementation
 */
public class SearchPresenterImpl implements SearchPresenter {

    private final SearchView searchView;
    private final RedditClient redditClient;

    private CompositeSubscription subscriptions;

    @Inject
    public SearchPresenterImpl(SearchView searchView, App app) {
        this.searchView = searchView;
        this.redditClient = app.getRedditClient();
    }

    @Override
    public void registerForEvents() {
        if(subscriptions == null)
            subscriptions = new CompositeSubscription();
    }

    @Override
    public void unregisterForEvents() {
        if(subscriptions != null && subscriptions.hasSubscriptions())
            subscriptions.unsubscribe();
    }


}
