package com.matie.redgram.ui.submissions;

import com.matie.redgram.data.managers.presenters.LinksPresenter;
import com.matie.redgram.data.managers.presenters.LinksPresenterImpl;
import com.matie.redgram.ui.App;
import com.matie.redgram.ui.common.views.ContentView;
import com.matie.redgram.ui.submissions.views.LinksView;

import dagger.Module;
import dagger.Provides;

/**
 * Created by matie on 2016-03-16.
 */
@Module
public class SubmissionModule {

    private LinksView linksView;
    private ContentView containerView;

    public SubmissionModule(LinksView linksView, ContentView containerView) {
        this.linksView = linksView;
        this.containerView = containerView;
        this.linksView.setBaseContextView(containerView.getParentView());
    }

    @Provides
    public LinksView provideLinksView(){
        return linksView;
    }

    @Provides
    public LinksPresenter provideLinksPresenter(App app){
        return new LinksPresenterImpl(linksView, containerView, app);
    }

}
