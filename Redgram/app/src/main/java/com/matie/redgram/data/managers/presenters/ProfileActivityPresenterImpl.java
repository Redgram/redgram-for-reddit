package com.matie.redgram.data.managers.presenters;

import com.matie.redgram.data.managers.presenters.base.BasePresenterImpl;
import com.matie.redgram.ui.App;
import com.matie.redgram.ui.profile.views.ProfileActivityView;

import java.util.Map;

import javax.inject.Inject;

public class ProfileActivityPresenterImpl extends BasePresenterImpl implements ProfileActivityPresenter {

    @Inject
    public ProfileActivityPresenterImpl(App app, ProfileActivityView view) {
        super(view, app);
    }

    @Override
    public void getListing(String listingType, Map<String, String> params) {
        // // TODO: 10/24/17  
    }
}
