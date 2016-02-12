package com.matie.redgram.ui.thread.modules;

import android.app.Activity;

import com.matie.redgram.data.managers.presenters.HomePresenter;
import com.matie.redgram.data.managers.presenters.HomePresenterImpl;
import com.matie.redgram.data.managers.presenters.ThreadPresenter;
import com.matie.redgram.data.managers.presenters.ThreadPresenterImpl;
import com.matie.redgram.ui.ActivityScope;
import com.matie.redgram.ui.App;
import com.matie.redgram.ui.FragmentScope;
import com.matie.redgram.ui.common.main.MainActivity;
import com.matie.redgram.ui.common.utils.widgets.DialogUtil;
import com.matie.redgram.ui.home.views.HomeView;
import com.matie.redgram.ui.thread.views.CommentsActivity;
import com.matie.redgram.ui.thread.views.ThreadView;

import dagger.Module;
import dagger.Provides;

/**
 * Created by matie on 2016-02-10.
 */
@Module
public class ThreadModule {

    private final Activity activity;
    private final ThreadView threadView;

    public ThreadModule(Activity activity, ThreadView threadView) {
        this.activity = activity;
        this.threadView = threadView;
    }

    @ActivityScope
    @Provides
    CommentsActivity activity(){
        return (CommentsActivity)activity;
    }

    @ActivityScope
    @Provides
    DialogUtil provideDialogUtil(){
        return new DialogUtil(activity);
    }

    @ActivityScope
    @Provides
    public ThreadView provideView(){return threadView;}

    @ActivityScope
    @Provides
    public ThreadPresenter provideThreadPresenter(App app){
        return new ThreadPresenterImpl(threadView, app);
    }
}
