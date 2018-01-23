package com.matie.redgram.ui.feed.links;

import com.matie.redgram.data.managers.presenters.LinksPresenter;
import com.matie.redgram.ui.base.BaseView;
import com.matie.redgram.ui.feed.SubmissionFeedLayout;
import com.matie.redgram.ui.feed.links.views.LinksView;
import com.matie.redgram.ui.scopes.FeedScope;

import dagger.Subcomponent;

@FeedScope
@Subcomponent(modules = LinksModule.class)
public interface LinksComponent {
    void inject(SubmissionFeedLayout submissionFeedLayout);

    BaseView getBaseView();
    LinksView getLinksView();
    LinksPresenter getLinksPresenter();
}
