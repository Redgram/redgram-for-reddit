package com.matie.redgram.ui.home;

import com.matie.redgram.data.managers.presenters.HomePresenter;
import com.matie.redgram.ui.ActivityScope;
import com.matie.redgram.ui.FragmentScope;
import com.matie.redgram.ui.common.BaseComponent;
import com.matie.redgram.ui.common.MainComponent;
import com.matie.redgram.ui.home.views.HomeView;

import dagger.Component;

/**
 * Created by matie on 06/06/15.
 */
@FragmentScope
@Component(
        dependencies = MainComponent.class,
        modules = {
                HomeModule.class
        }
)
public interface HomeComponent extends BaseComponent{

    void inject(HomeFragment homeFragment);

    HomeView getHomeView();
    HomePresenter getHomePresenter();

}
