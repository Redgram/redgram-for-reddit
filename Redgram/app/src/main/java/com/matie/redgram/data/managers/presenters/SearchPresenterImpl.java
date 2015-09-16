package com.matie.redgram.data.managers.presenters;

import android.util.Log;

import com.matie.redgram.data.models.PostItem;
import com.matie.redgram.data.models.main.reddit.PostItemWrapper;
import com.matie.redgram.data.network.api.reddit.RedditClient;
import com.matie.redgram.ui.common.views.widgets.postlist.PostRecyclerView;
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

    final private SearchView searchView;
    final private RedditClient redditClient;
    final private PostRecyclerView searchRecyclerView;
    private List<PostItem> items;

    CompositeSubscription subscriptions;
    Subscription searchSubscription;

    int loadingSource;

    @Inject
    public SearchPresenterImpl(SearchView searchView, RedditClient redditClient) {
        this.searchView = searchView;
        this.redditClient = redditClient;

        searchRecyclerView = searchView.getRecyclerView();
        this.items = new ArrayList<PostItem>();
        this.loadingSource = 0;
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

//        searchRecyclerView.clearOnScrollListeners();
    }

    @Override
    public void executeSearch(String subreddit, Map<String, String> params) {
        items = new ArrayList<PostItem>();
        searchView.showLoading();
        searchSubscription = getSearchSubscription(subreddit, params, REFRESH);

        if(!searchSubscription.isUnsubscribed()){
            subscriptions.add(searchSubscription);
        }
    }

    @Override
    public void loadMoreResults(String subreddit, Map<String, String> params) {
        searchView.showLoadMoreIndicator();
        searchSubscription = getSearchSubscription(subreddit, params, LOAD_MORE);

        if(!searchSubscription.isUnsubscribed()){
            subscriptions.add(searchSubscription);
        }
    }

    private Subscription getSearchSubscription(String subreddit, Map<String, String> params, int loadingEvent) {
        return (Subscription)bindFragment(searchView.getFragment(), redditClient.executeSearch(subreddit, params))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<PostItemWrapper>() {
                    @Override
                    public void onCompleted() {
                        //hide progress and show list
                        hideLoadingEvent(loadingEvent);
//                        for(PostItem item : items){
//                            Log.d("ITEM URL", item.getAuthor() + "--" + item.getType() + "--" + item.getId());
//                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        hideLoadingEvent(loadingEvent);
                        searchView.showErrorMessage();
                    }

                    @Override
                    public void onNext(PostItemWrapper wrapper) {
                        items.addAll(wrapper.getItems());
                        searchRecyclerView.replaceWith(items);
                    }
                });
    }

    private void hideLoadingEvent(int loadingEvent){
        if(loadingEvent == REFRESH)
            searchView.hideLoading();
        else if(loadingEvent == LOAD_MORE)
            searchView.hideLoadMoreIndicator();
    }

}
