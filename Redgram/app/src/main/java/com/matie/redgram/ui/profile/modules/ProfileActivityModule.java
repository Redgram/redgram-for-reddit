package com.matie.redgram.ui.profile.modules;

import com.matie.redgram.data.managers.presenters.ProfileActivityPresenter;
import com.matie.redgram.data.managers.presenters.ProfileActivityPresenterImpl;
import com.matie.redgram.data.managers.storage.db.DatabaseManager;
import com.matie.redgram.ui.profile.views.ProfileActivityView;
import com.matie.redgram.ui.scopes.ProfileScope;

import dagger.Module;
import dagger.Provides;

@Module
public class ProfileActivityModule {

    private ProfileActivityView profileActivityView;

    public ProfileActivityModule(ProfileActivityView profileActivityView) {
        this.profileActivityView = profileActivityView;
    }

    @ProfileScope
    @Provides
    public ProfileActivityView provideView(){return profileActivityView;}


    @ProfileScope
    @Provides
    public ProfileActivityPresenter provideProfileActivityPresenter(DatabaseManager databaseManager){
        return new ProfileActivityPresenterImpl(profileActivityView, databaseManager);
    }
}
