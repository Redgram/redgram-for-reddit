package com.matie.redgram.ui.thread.modules;

import android.app.Activity;

import com.matie.redgram.data.managers.presenters.ThreadPresenter;
import com.matie.redgram.data.managers.presenters.ThreadPresenterImpl;
import com.matie.redgram.data.managers.storage.db.DatabaseManager;
import com.matie.redgram.data.network.api.reddit.user.RedditClientInterface;
import com.matie.redgram.ui.common.utils.widgets.DialogUtil;
import com.matie.redgram.ui.scopes.ActivityScope;
import com.matie.redgram.ui.thread.ThreadActivity;
import com.matie.redgram.ui.thread.views.ThreadView;

import dagger.Module;
import dagger.Provides;

@Module
public class ThreadModule {

    private final Activity activity;
    private final ThreadView threadView;

    public ThreadModule(Activity activity) {
        this.activity = activity;
        this.threadView = activity();
    }

    @ActivityScope
    @Provides
    ThreadActivity activity(){
        return (ThreadActivity) activity;
    }

    @ActivityScope
    @Provides
    DialogUtil provideDialogUtil(){
        return new DialogUtil(activity);
    }

    @ActivityScope
    @Provides
    public ThreadView provideView() {
        return threadView;
    }

    @ActivityScope
    @Provides
    public ThreadPresenter provideThreadPresenter(DatabaseManager databaseManager,
                                                  RedditClientInterface redditClient){
        return new ThreadPresenterImpl(threadView, databaseManager, redditClient);
    }
}
