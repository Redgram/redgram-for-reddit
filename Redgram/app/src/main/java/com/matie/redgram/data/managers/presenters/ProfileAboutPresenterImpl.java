package com.matie.redgram.data.managers.presenters;

import com.matie.redgram.data.managers.storage.db.DatabaseHelper;
import com.matie.redgram.data.managers.storage.db.DatabaseManager;
import com.matie.redgram.data.models.api.reddit.auth.AuthUser;
import com.matie.redgram.data.models.api.reddit.main.RedditUser;
import com.matie.redgram.data.models.db.Session;
import com.matie.redgram.data.models.db.User;
import com.matie.redgram.data.models.main.profile.Karma;
import com.matie.redgram.data.models.main.profile.ProfileUser;
import com.matie.redgram.data.network.api.reddit.RedditClientInterface;
import com.matie.redgram.ui.App;
import com.matie.redgram.ui.profile.views.ProfileAboutView;

import javax.inject.Inject;

import io.realm.Realm;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

import static com.matie.redgram.data.models.db.User.USER_AUTH;

/**
 * Created by matie on 2017-09-27.
 */

public class ProfileAboutPresenterImpl implements ProfileAboutPresenter {

    private final RedditClientInterface redditClient;
    private final DatabaseManager databaseManager;
    private final ProfileAboutView view;
    private final CompositeSubscription subscriptions = new CompositeSubscription();
    private Subscription userSubscription;

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
    }

    @Override
    public void registerForEvents() {
        init();

        if (subscriptions.isUnsubscribed()) {
            if (userSubscription != null) {
                subscriptions.add(userSubscription);
            }
        }
    }

    @Override
    public void unregisterForEvents() {
        DatabaseHelper.close(realm);
        if (subscriptions.hasSubscriptions()) {
            subscriptions.unsubscribe();
        }
    }

    @Override
    public void getUserDetails(String username) {
        if (session == null || session.getUser() == null || username == null || username.isEmpty()) return;

        view.showLoading();

        final User user = session.getUser();
        if (isAuthUser(user, username)) {
            // auth user calls
            getAuthUserInformation(user);
        } else {
            // normal user calls
            getUserInformation(username);
        }
    }

    @Override
    public boolean isAuthUser(String username) {
        if (session == null || session.getUser() == null || username == null || username.isEmpty()) return false;

        final User user = session.getUser();
        return isAuthUser(user, username);
    }

    private boolean isAuthUser(User user, String username) {
        return USER_AUTH.equals(user.getUserType()) && username.equals(user.getUserName());
    }

    private void getUserInformation(String username) {
        userSubscription = redditClient.getUserDetails(username)
                .compose(view.getContentContext().getBaseFragment().bindToLifecycle())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<RedditUser>() {
                    @Override
                    public void onCompleted() {
                        // TODO: 10/19/17 see why this isn't being called
                        view.hideLoading();
                    }

                    @Override
                    public void onError(Throwable e) {
                        showError(e);
                    }

                    @Override
                    public void onNext(RedditUser redditUser) {
                        onSuccess(mapToProfileUser(redditUser));
                        view.hideLoading();
                    }
                });
    }

    private ProfileUser mapToProfileUser(RedditUser redditUser) {
        ProfileUser profileUser = new ProfileUser();

        profileUser.setId(redditUser.getId());
        profileUser.setUsername(redditUser.getName());
        profileUser.setCreatedDate(redditUser.getDate());

        Karma karma = new Karma();
        karma.setComment(redditUser.getCommentKarma());
        karma.setLink(redditUser.getLinkKarma());
        profileUser.setKarma(karma);

        return profileUser;
    }

    private void getAuthUserInformation(User user) {
        userSubscription = redditClient.getUser(user.getTokenInfo().getToken())
                .compose(view.getContentContext().getBaseFragment().bindToLifecycle())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<AuthUser>() {
                    @Override
                    public void onCompleted() {
                        view.hideLoading();
                    }

                    @Override
                    public void onError(Throwable e) {
                        showError(e);
                    }

                    @Override
                    public void onNext(AuthUser authUser) {
                        onSuccess(mapToProfileUser(authUser));
                        view.hideLoading();
                    }
                });
    }

    private ProfileUser mapToProfileUser(AuthUser authUser) {
        ProfileUser profileUser = new ProfileUser();

        profileUser.setId(authUser.getId());
        profileUser.setUsername(authUser.getName());
        profileUser.setCreatedDate(authUser.getCreateUtc());

        Karma karma = new Karma();
        karma.setComment(authUser.getCommentKarma());
        karma.setLink(authUser.getLinkKarma());
        profileUser.setKarma(karma);

        return profileUser;
    }

    private void showError(Throwable e) {
        view.showErrorMessage(e.toString());
    }

    private void onSuccess(ProfileUser profileUser) {
        if (profileUser == null) return;
        view.updateProfile(profileUser);
    }


}
