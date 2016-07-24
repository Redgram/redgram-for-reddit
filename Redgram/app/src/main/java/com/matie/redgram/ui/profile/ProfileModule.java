package com.matie.redgram.ui.profile;

import com.matie.redgram.data.managers.presenters.ProfilePresenter;
import com.matie.redgram.data.managers.presenters.ProfilePresenterImpl;
import com.matie.redgram.ui.App;
import com.matie.redgram.ui.FragmentScope;
import com.matie.redgram.ui.profile.views.ProfileView;

import dagger.Module;
import dagger.Provides;

/**
 * Created by matie on 2016-07-21.
 */
@Module
public class ProfileModule {
    private ProfileView profileView;

    public ProfileModule(ProfileView profileView){
        this.profileView = profileView;
    }

    @FragmentScope
    @Provides
    public ProfileView provideView(){
        return profileView;
    }

    @FragmentScope
    @Provides
    public ProfilePresenter provideProfilePresenter(App app){
        return new ProfilePresenterImpl(profileView, app);
    }


}
