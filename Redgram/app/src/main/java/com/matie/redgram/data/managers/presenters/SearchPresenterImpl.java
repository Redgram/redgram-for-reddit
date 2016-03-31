package com.matie.redgram.data.managers.presenters;

import com.matie.redgram.data.models.main.items.PostItem;
import com.matie.redgram.data.models.main.reddit.RedditListing;
import com.matie.redgram.data.network.api.reddit.RedditClient;
import com.matie.redgram.ui.App;
import com.matie.redgram.ui.common.views.widgets.postlist.PostRecyclerView;
import com.matie.redgram.ui.posts.views.LinksView;
import com.matie.redgram.ui.search.views.SearchView;

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
 * Created by matie on 28/06/15.
 */
public class SearchPresenterImpl implements SearchPresenter {

    private final SearchView searchView;
    private final RedditClient redditClient;

    CompositeSubscription subscriptions;

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
        if(subscriptions.hasSubscriptions() || subscriptions != null)
            subscriptions.unsubscribe();
    }


}
