package com.matie.redgram.data.managers.presenters;

import com.matie.redgram.data.managers.presenters.base.BasePresenter;
import com.matie.redgram.data.models.api.reddit.auth.AuthWrapper;
import com.matie.redgram.data.models.db.Prefs;
import com.matie.redgram.data.models.db.User;

import io.realm.RealmResults;

public interface AuthPresenter extends BasePresenter {
    void getAccessToken(String url);
    void getAccessToken();
    void updateSession(AuthWrapper wrapper);
    RealmResults<User> getExistingUsers();
    Prefs getPrefsByUserId(String userId);
}
