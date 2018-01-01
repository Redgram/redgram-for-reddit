package com.matie.redgram.ui.profile;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.matie.redgram.R;
import com.matie.redgram.data.managers.presenters.ProfileAboutPresenterImpl;
import com.matie.redgram.data.models.main.profile.ProfileUser;
import com.matie.redgram.ui.AppComponent;
import com.matie.redgram.ui.base.BaseActivity;
import com.matie.redgram.ui.base.BaseFragment;
import com.matie.redgram.ui.profile.components.DaggerProfileAboutComponent;
import com.matie.redgram.ui.profile.components.ProfileAboutComponent;
import com.matie.redgram.ui.profile.components.ProfileComponent;
import com.matie.redgram.ui.profile.modules.ProfileAboutModule;
import com.matie.redgram.ui.profile.views.ProfileAboutView;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class ProfileAboutFragment extends BaseFragment implements ProfileAboutView {

    @Inject
    ProfileAboutPresenterImpl aboutPresenter;
    ProfileAboutComponent component;

    @InjectView(R.id.progress_bar)
    ProgressBar progressBar;

    @InjectView(R.id.content)
    LinearLayout contentView;

    @InjectView(R.id.link_karma)
    TextView linkKarmaTextView;

    @InjectView(R.id.comment_karma)
    TextView commentKarmaTextView;

    @InjectView(R.id.auth_user_options)
    LinearLayout userOptions;

    String username;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile_about, container, false);
        ButterKnife.inject(this, view);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        aboutPresenter.registerForEvents();
    }

    @Override
    public void onPause() {
        super.onPause();
        aboutPresenter.unregisterForEvents();
    }

    @Override
    public void onDestroyView() {
        ButterKnife.reset(this);
        super.onDestroyView();
    }

    @Override
    protected void setup() {
        if (getArguments() == null) return;

        final String username = getArguments().getString(ProfileActivity.RESULT_USER_NAME);

        if(username == null) return;

        this.username = username;
        populateViews();
        setupUserOptions();
        setupToolbar();
    }

    private void populateViews() {
        aboutPresenter.getUserDetails(username);
    }


    private void setupUserOptions() {
        if (aboutPresenter.isAuthUser(username)) {
            userOptions.setVisibility(View.VISIBLE);
        } else {
            userOptions.setVisibility(View.GONE);
        }
    }

    @Override
    protected void setupComponent() {
        AppComponent appComponent = ((BaseActivity) getActivity()).component();
        ProfileComponent profileComponent = (ProfileComponent) appComponent;
        component = DaggerProfileAboutComponent.builder()
                .profileComponent(profileComponent)
                .profileAboutModule(new ProfileAboutModule(this))
                .build();
        component.inject(this);
    }

    @Override
    public void updateProfile(ProfileUser profileUser) {
        linkKarmaTextView.setText(profileUser.getKarma().getLink() + "");
        commentKarmaTextView.setText(profileUser.getKarma().getComment() + "");
    }

    @Override
    protected void setupToolbar() {
        ActionBar supportActionBar = ((BaseActivity) getActivity()).getSupportActionBar();
        if (supportActionBar != null) {
            supportActionBar.setTitle(username);
        }
    }

    @Override
    public void showLoading() {
        contentView.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoading() {
        progressBar.setVisibility(View.GONE);
        contentView.setVisibility(View.VISIBLE);
    }

    @Override
    public void showInfoMessage() {

    }

    @Override
    public void showErrorMessage(String error) {

    }
}
