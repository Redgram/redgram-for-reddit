package com.matie.redgram.ui.thread.modules;

import android.app.Activity;

import com.matie.redgram.data.managers.presenters.ThreadPresenter;
import com.matie.redgram.data.managers.presenters.ThreadPresenterImpl;
import com.matie.redgram.ui.ActivityScope;
import com.matie.redgram.ui.App;
import com.matie.redgram.ui.common.utils.widgets.DialogUtil;
import com.matie.redgram.ui.thread.ThreadActivity;
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

    public ThreadModule(Activity activity) {
        this.activity = activity;
        this.threadView = activity();
    }

    @ActivityScope
    @Provides
    ThreadActivity activity(){
        return (ThreadActivity)activity;
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
