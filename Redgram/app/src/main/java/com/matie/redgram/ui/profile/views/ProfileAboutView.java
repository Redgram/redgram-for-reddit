package com.matie.redgram.ui.profile.views;


import com.matie.redgram.data.models.main.profile.ProfileUser;
import com.matie.redgram.ui.common.views.ContentView;

/**
 * Created by matie on 2017-09-27.
 */

public interface ProfileAboutView extends ContentView {
    void updateProfile(ProfileUser profileUser);
}
