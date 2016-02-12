package com.matie.redgram.ui.thread.modules;

import com.matie.redgram.data.managers.presenters.CommentsPresenter;
import com.matie.redgram.data.managers.presenters.CommentsPresenterImpl;
import com.matie.redgram.data.managers.presenters.HomePresenter;
import com.matie.redgram.data.managers.presenters.HomePresenterImpl;
import com.matie.redgram.ui.App;
import com.matie.redgram.ui.FragmentScope;
import com.matie.redgram.ui.home.views.HomeView;
import com.matie.redgram.ui.thread.views.CommentsView;

import dagger.Module;
import dagger.Provides;

/**
 * Created by matie on 2016-01-04.
 */
@Module
public class CommentsModule {
    private CommentsView commentsView;

    public CommentsModule(CommentsView commentsView) {
        this.commentsView = commentsView;
    }

    @FragmentScope
    @Provides
    public CommentsView provideView(){return commentsView;}

    @FragmentScope
    @Provides
    public CommentsPresenter provideCommentsPresenter(App app){
        return new CommentsPresenterImpl(commentsView, app);
    }

}
