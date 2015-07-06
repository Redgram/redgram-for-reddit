package com.matie.redgram.ui.search;

import com.matie.redgram.data.managers.presenters.SearchPresenter;
import com.matie.redgram.data.managers.presenters.SearchPresenterImpl;
import com.matie.redgram.data.network.api.reddit.RedditClient;
import com.matie.redgram.ui.search.views.SearchView;

import dagger.Module;
import dagger.Provides;

/**
 * Created by matie on 28/06/15.
 */
@Module
public class SearchModule {
    private SearchView searchView;

    public SearchModule(SearchView searchView){
        this.searchView = searchView;
    }

    @Provides
    public SearchView provideView(){return searchView;}

    @Provides
    public SearchPresenter provideSearchPresenter(RedditClient redditClient){
        return new SearchPresenterImpl(searchView, redditClient);
    }
}
