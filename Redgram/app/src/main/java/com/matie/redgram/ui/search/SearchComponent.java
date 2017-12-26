package com.matie.redgram.ui.search;

import com.matie.redgram.data.managers.presenters.SearchPresenter;
import com.matie.redgram.ui.FragmentScope;
import com.matie.redgram.ui.common.main.MainComponent;
import com.matie.redgram.ui.search.views.SearchView;
import com.matie.redgram.ui.feed.links.LinksComponent;
import com.matie.redgram.ui.feed.links.LinksModule;

import dagger.Component;

@FragmentScope
@Component(
        dependencies = MainComponent.class,
        modules = {
                SearchModule.class,
                LinksModule.class
        }
)
interface SearchComponent {
    void inject(SearchFragment searchFragment);

    SearchView getSearchView();
    SearchPresenter getSearchPresenter();
    LinksComponent getLinksComponent(LinksModule linksModule);
}
