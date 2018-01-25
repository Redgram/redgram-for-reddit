package com.matie.redgram.ui.search;

import com.matie.redgram.data.managers.presenters.SearchPresenter;
import com.matie.redgram.ui.main.MainComponent;
import com.matie.redgram.ui.scopes.MainScope;
import com.matie.redgram.ui.search.views.SearchView;
import com.matie.redgram.ui.user.UserComponentInjector;

import dagger.Component;

@MainScope
@Component(
        dependencies = MainComponent.class,
        modules = {
                SearchModule.class
        }
)
interface SearchComponent {
    void inject(SearchFragment searchFragment);

    SearchView getSearchView();
    SearchPresenter getSearchPresenter();

    UserComponentInjector userComponentInjector();
}
