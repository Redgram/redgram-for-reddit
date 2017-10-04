package com.matie.redgram.data.managers.presenters;

import com.matie.redgram.data.managers.storage.db.DatabaseHelper;
import com.matie.redgram.data.managers.storage.db.DatabaseManager;
import com.matie.redgram.data.models.db.Session;
import com.matie.redgram.data.models.db.User;
import com.matie.redgram.data.network.api.reddit.RedditClient;
import com.matie.redgram.data.network.api.reddit.RedditClientInterface;
import com.matie.redgram.ui.App;
import com.matie.redgram.ui.profile.views.ProfileAboutView;

import javax.inject.Inject;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by matie on 2017-09-27.
 */

public class ProfileAboutPresenterImpl implements ProfileAboutPresenter {

    private final RedditClientInterface redditClient;
    private final DatabaseManager databaseManager;
    private final ProfileAboutView view;
    private Realm realm;
    private Session session;

    @Inject
    public ProfileAboutPresenterImpl(App app, ProfileAboutView view) {
        this.view = view;
        redditClient = app.getRedditClient();
        databaseManager = app.getDatabaseManager();
        init();
    }

    private void init() {
        realm = databaseManager.getInstance();
        session = DatabaseHelper.getSession(realm);
        if (session.getUser() != null) {
            session.getUser().addChangeListener(() -> {
                // update UI
            });
        }
    }

    @Override
    public void registerForEvents() {
        init();
    }

    @Override
    public void unregisterForEvents() {
        DatabaseHelper.close(realm);
    }

    @Override
    public void getUserDetails(String username) {
        if (session == null || session.getUser() == null || username == null || username.isEmpty()) return;

        final boolean isAuthUser = session.getUser().isAuthUser();
        final User user = session.getUser();

        if (isAuthUser && username.equals(user.getUserName())) {
            // auth user calls
            redditClient.getUser(user.getTokenInfo().getToken())
                .compose(view.getContentContext().getBaseFragment().bindToLifecycle())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe();
        } else {
            // normal user calls
        }
    }
}
