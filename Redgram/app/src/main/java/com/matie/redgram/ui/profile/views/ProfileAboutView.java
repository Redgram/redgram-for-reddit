package com.matie.redgram.ui.profile.views;


import com.matie.redgram.data.models.main.profile.ProfileUser;
import com.matie.redgram.ui.common.views.ContentView;

public interface ProfileAboutView extends ContentView {
    void updateProfile(ProfileUser profileUser);
}
