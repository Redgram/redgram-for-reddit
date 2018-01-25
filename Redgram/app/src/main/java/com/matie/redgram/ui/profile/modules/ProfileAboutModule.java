package com.matie.redgram.ui.profile.modules;

import com.matie.redgram.data.managers.presenters.ProfileAboutPresenter;
import com.matie.redgram.data.managers.presenters.ProfileAboutPresenterImpl;
import com.matie.redgram.data.managers.storage.db.DatabaseManager;
import com.matie.redgram.data.network.api.reddit.user.RedditClientInterface;
import com.matie.redgram.ui.profile.views.ProfileAboutView;
import com.matie.redgram.ui.scopes.ProfileScope;

import dagger.Module;
import dagger.Provides;

@Module
public class ProfileAboutModule {
    private ProfileAboutView profileAboutView;

    public ProfileAboutModule(ProfileAboutView profileAboutView) {
        this.profileAboutView = profileAboutView;
    }

    @ProfileScope
    @Provides
    public ProfileAboutView provideView(){return profileAboutView;}

    @ProfileScope
    @Provides
    public ProfileAboutPresenter provideProfileAboutPresenter(DatabaseManager databaseManager,
                                                              RedditClientInterface redditClient){
        return new ProfileAboutPresenterImpl(profileAboutView, databaseManager, redditClient);
    }
}
