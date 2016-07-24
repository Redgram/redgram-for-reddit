package com.matie.redgram.data.managers.presenters;

import com.matie.redgram.ui.App;
import com.matie.redgram.ui.profile.views.ProfileView;

import javax.inject.Inject;

/**
 * Created by matie on 2016-07-24.
 */
public class ProfilePresenterImpl implements ProfilePresenter {

    @Inject
    public ProfilePresenterImpl(ProfileView profileView, App app) {

    }

    @Override
    public void registerForEvents() {

    }

    @Override
    public void unregisterForEvents() {

    }
}
