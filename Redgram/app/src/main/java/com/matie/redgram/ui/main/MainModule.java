package com.matie.redgram.ui.main;

import com.matie.redgram.data.managers.presenters.MainPresenter;
import com.matie.redgram.data.managers.presenters.MainPresenterImpl;
import com.matie.redgram.data.managers.storage.db.DatabaseManager;
import com.matie.redgram.ui.common.utils.widgets.DialogUtil;
import com.matie.redgram.ui.main.views.MainView;
import com.matie.redgram.ui.scopes.ActivityScope;

import dagger.Module;
import dagger.Provides;

@Module
public class MainModule {

    private final MainActivity activity;
    private final MainView mainView;

    public MainModule(MainActivity activity) {
        this.activity = activity;
        this.mainView = activity;
    }

    @ActivityScope
    @Provides
    MainActivity activity(){
        return activity;
    }

    @ActivityScope
    @Provides
    MainPresenter provideMainPresenter(DatabaseManager databaseManager) {
        return new MainPresenterImpl(activity, databaseManager);
    }

    @ActivityScope
    @Provides
    MainView provideView(){return mainView;}

    @ActivityScope
    @Provides
    DialogUtil provideDialogUtil(){
        return new DialogUtil(activity);
    }

}
