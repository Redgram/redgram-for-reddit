package com.matie.redgram.ui.home;

import com.matie.redgram.data.managers.presenters.HomePresenter;
import com.matie.redgram.ui.home.views.HomeView;
import com.matie.redgram.ui.main.MainComponent;
import com.matie.redgram.ui.scopes.MainScope;
import com.matie.redgram.ui.user.UserComponentInjector;

import dagger.Component;

@MainScope
@Component(
        dependencies = MainComponent.class,
        modules = {
                HomeModule.class
        }
)
public interface HomeComponent {

    void inject(HomeFragment homeFragment);

    HomeView getHomeView();
    HomePresenter getHomePresenter();

    UserComponentInjector userComponentInjector();
}
