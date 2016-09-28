package com.matie.redgram.ui.common.auth.views;

import com.matie.redgram.data.models.api.reddit.auth.AuthWrapper;
import com.matie.redgram.ui.common.views.BaseContextView;
import com.matie.redgram.ui.common.views.ContentView;

/**
 * Created by matie on 2016-02-21.
 */
public interface AuthView extends ContentView {
    void showPreferencesOptions(AuthWrapper wrapper);
    void transitionToMainActivity(boolean resultIncluded, boolean isSuccess);
}
