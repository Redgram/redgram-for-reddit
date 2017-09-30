package com.matie.redgram.data.managers.presenters;

import com.matie.redgram.ui.App;
import com.matie.redgram.ui.profile.views.ProfileAboutView;

import javax.inject.Inject;

/**
 * Created by matie on 2017-09-27.
 */

public class ProfileAboutPresenterImpl implements ProfileAboutPresenter {

    @Inject
    public ProfileAboutPresenterImpl(App app, ProfileAboutView view) {
    }

    @Override
    public void registerForEvents() {

    }

    @Override
    public void unregisterForEvents() {

    }

    @Override
    public String getTitle() {
        return "About";
    }
}
