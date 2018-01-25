package com.matie.redgram.ui.thread.modules;

import com.matie.redgram.data.managers.presenters.CommentsPresenter;
import com.matie.redgram.data.managers.presenters.CommentsPresenterImpl;
import com.matie.redgram.data.managers.storage.db.DatabaseManager;
import com.matie.redgram.ui.scopes.ThreadScope;
import com.matie.redgram.ui.thread.views.CommentsView;

import dagger.Module;
import dagger.Provides;

@Module
public class CommentsModule {
    private CommentsView commentsView;

    public CommentsModule(CommentsView commentsView) {
        this.commentsView = commentsView;
    }

    @ThreadScope
    @Provides
    public CommentsView provideView(){return commentsView;}

    @ThreadScope
    @Provides
    public CommentsPresenter provideCommentsPresenter(DatabaseManager databaseManager){
        return new CommentsPresenterImpl(commentsView, databaseManager);
    }

}
