package com.matie.redgram.ui.home;

import com.matie.redgram.data.managers.presenters.HomePresenter;
import com.matie.redgram.ui.FragmentScope;
import com.matie.redgram.ui.common.main.MainComponent;
import com.matie.redgram.ui.home.views.HomeView;
import com.matie.redgram.ui.feed.links.LinksComponent;
import com.matie.redgram.ui.feed.links.LinksModule;

import dagger.Component;

@FragmentScope
@Component(
        dependencies = MainComponent.class,
        modules = {
                HomeModule.class,
                LinksModule.class,
        }
)
public interface HomeComponent {

    void inject(HomeFragment homeFragment);

    HomeView getHomeView();
    HomePresenter getHomePresenter();
    LinksComponent getLinksComponent(LinksModule linksModule);
}
