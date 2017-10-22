package com.matie.redgram.ui.links;

import com.matie.redgram.data.managers.presenters.LinksPresenter;
import com.matie.redgram.ui.links.views.LinksView;

import dagger.Subcomponent;

/**
 * Created by matie on 2016-03-17.
 */
@Subcomponent(modules = LinksModule.class)
public interface LinksComponent {
    void inject(LinksContainerView linksContainerView);

    LinksView getLinksView();
    LinksPresenter getLinksPresenter();
}
