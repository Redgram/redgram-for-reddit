package com.matie.redgram.ui.home;

import com.matie.redgram.data.managers.presenters.HomePresenter;
import com.matie.redgram.ui.FragmentScope;
import com.matie.redgram.ui.common.main.MainComponent;
import com.matie.redgram.ui.home.views.HomeView;
import com.matie.redgram.ui.links.LinksComponent;
import com.matie.redgram.ui.links.LinksModule;
import com.matie.redgram.ui.submissions.SubmissionComponent;
import com.matie.redgram.ui.submissions.SubmissionModule;

import dagger.Component;

@FragmentScope
@Component(
        dependencies = MainComponent.class,
        modules = {
                HomeModule.class,
                LinksModule.class,
                SubmissionModule.class
        }
)
public interface HomeComponent {

    void inject(HomeFragment homeFragment);

    HomeView getHomeView();
    HomePresenter getHomePresenter();
    LinksComponent getLinksComponent(LinksModule linksModule);
    SubmissionComponent getSubmissionComponent(SubmissionModule linksModule);
}
