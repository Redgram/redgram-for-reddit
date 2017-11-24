package com.matie.redgram.ui.search;

import com.matie.redgram.data.managers.presenters.SearchPresenter;
import com.matie.redgram.ui.FragmentScope;
import com.matie.redgram.ui.common.main.MainComponent;
import com.matie.redgram.ui.links.LinksComponent;
import com.matie.redgram.ui.submission.links.LinksModule;
import com.matie.redgram.ui.search.views.SearchView;

import dagger.Component;

/**
 * Created by matie on 28/06/15.
 */
@FragmentScope
@Component(
        dependencies = MainComponent.class,
        modules = {
                SearchModule.class,
                LinksModule.class
        }
)
public interface SearchComponent{
    void inject(SearchFragment searchFragment);

    SearchView getSearchView();
    SearchPresenter getSearchPresenter();
    LinksComponent getLinksComponent(LinksModule linksModule);
}
