package com.matie.redgram.data.managers.presenters;

import com.matie.redgram.ui.App;
import com.matie.redgram.ui.profile.views.ProfileActivityView;

import java.util.Map;

import javax.inject.Inject;

/**
 * Created by matie on 2017-09-27.
 */

public class ProfileActivityPresenterImpl implements ProfileActivityPresenter {

    @Inject
    public ProfileActivityPresenterImpl(App app, ProfileActivityView view) {
    }

    @Override
    public void registerForEvents() {

    }

    @Override
    public void unregisterForEvents() {

    }

    @Override
    public void getListing(String listingType, Map<String, String> params) {
        // // TODO: 10/24/17  
    }
}
