package com.matie.redgram.data.managers.presenters;

import com.matie.redgram.data.managers.presenters.base.BasePresenterImpl;
import com.matie.redgram.data.managers.storage.db.DatabaseManager;
import com.matie.redgram.data.network.api.reddit.user.RedditClientInterface;
import com.matie.redgram.ui.search.views.SearchView;

import javax.inject.Inject;

public class SearchPresenterImpl extends BasePresenterImpl implements SearchPresenter {

    private final SearchView searchView;
    private final RedditClientInterface redditClient;

    @Inject
    public SearchPresenterImpl(SearchView searchView, DatabaseManager databaseManager, RedditClientInterface redditClient) {
        super(searchView, databaseManager);

        this.searchView = searchView;
        this.redditClient = redditClient;
    }

}
