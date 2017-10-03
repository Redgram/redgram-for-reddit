package com.matie.redgram.data.managers.presenters;

import com.matie.redgram.data.models.db.Session;
import com.matie.redgram.data.network.api.reddit.RedditClient;
import com.matie.redgram.data.network.api.reddit.RedditClientInterface;
import com.matie.redgram.ui.App;
import com.matie.redgram.ui.profile.views.ProfileAboutView;

import javax.inject.Inject;

/**
 * Created by matie on 2017-09-27.
 */

public class ProfileAboutPresenterImpl implements ProfileAboutPresenter {

    private final RedditClientInterface redditClient;
    private final Session session;

    @Inject
    public ProfileAboutPresenterImpl(App app, ProfileAboutView view) {
        redditClient = app.getRedditClient();
        session = view.getContentContext().getBaseActivity().getSession();
    }

    @Override
    public void registerForEvents() {

    }

    @Override
    public void unregisterForEvents() {

    }

    @Override
    public String getUserDetails(String username) {
        return null;
    }
}
