package com.matie.redgram.ui.search;

import com.matie.redgram.data.managers.presenters.SearchPresenter;
import com.matie.redgram.data.managers.presenters.SearchPresenterImpl;
import com.matie.redgram.data.managers.storage.db.DatabaseManager;
import com.matie.redgram.data.network.api.reddit.user.RedditClientInterface;
import com.matie.redgram.ui.scopes.FragmentScope;
import com.matie.redgram.ui.search.views.SearchView;

import dagger.Module;
import dagger.Provides;

@Module
public class SearchModule {
    private SearchView searchView;

    public SearchModule(SearchView searchView) {
        this.searchView = searchView;
    }

    @FragmentScope
    @Provides
    public SearchView provideView() {return searchView;}

    @FragmentScope
    @Provides
    public SearchPresenter provideSearchPresenter(DatabaseManager databaseManager, RedditClientInterface redditClient) {
        return new SearchPresenterImpl(searchView, databaseManager, redditClient);
    }
}
