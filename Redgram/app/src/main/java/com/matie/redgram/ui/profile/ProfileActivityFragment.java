package com.matie.redgram.ui.profile;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.matie.redgram.R;
import com.matie.redgram.data.managers.presenters.ProfileActivityPresenterImpl;
import com.matie.redgram.ui.AppComponent;
import com.matie.redgram.ui.common.base.BaseActivity;
import com.matie.redgram.ui.common.base.BaseFragment;
import com.matie.redgram.ui.common.views.BaseContextView;
import com.matie.redgram.ui.links.LinksContainerView;
import com.matie.redgram.ui.links.LinksControlView;
import com.matie.redgram.ui.profile.components.DaggerProfileActivityComponent;
import com.matie.redgram.ui.profile.components.ProfileActivityComponent;
import com.matie.redgram.ui.profile.components.ProfileComponent;
import com.matie.redgram.ui.profile.modules.ProfileActivityModule;
import com.matie.redgram.ui.profile.views.ProfileActivityView;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by matie on 2017-09-27.
 */

public class ProfileActivityFragment extends BaseFragment implements ProfileActivityView {

    @Inject
    ProfileActivityPresenterImpl activityPresenter;
    ProfileActivityComponent component;

    @InjectView(R.id.links_container_view)
    LinksContainerView linksContainerView;

    @InjectView(R.id.swipe_container)
    SwipeRefreshLayout swipeRefreshLayout;

    LinksControlView linksControlView;
    String username;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile_activity, container, false);
        ButterKnife.inject(this, view);

        return view;
    }


    @Override
    public void onResume() {
        super.onResume();
        activityPresenter.registerForEvents();
    }

    @Override
    public void onPause() {
        super.onPause();
        activityPresenter.unregisterForEvents();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }

    @Override
    protected void setupComponent() {
        AppComponent appComponent = ((BaseActivity)getActivity()).component();
        ProfileComponent profileComponent = (ProfileComponent) appComponent;
        component = DaggerProfileActivityComponent.builder()
                .profileComponent(profileComponent)
                .profileActivityModule(new ProfileActivityModule(this))
                .build();
        component.inject(this);
    }

    @Override
    protected void setup() {
        if (getArguments() == null) return;

        final String username = getArguments().getString(ProfileActivity.RESULT_USER_NAME);

        if(username == null) return;

        this.username = username;
        setupSwipeRefreshLayout();
        setupToolbar();
        fetchData();
    }

    private void setupSwipeRefreshLayout() {
        swipeRefreshLayout.setEnabled(false);
        swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_green_dark,
                android.R.color.holo_red_dark,
                android.R.color.holo_blue_dark,
                android.R.color.holo_orange_dark);
    }

    private void fetchData() {

    }

    @Override
    protected void setupToolbar() {
        ActionBar supportActionBar = ((BaseActivity) getActivity()).getSupportActionBar();
        if (supportActionBar != null) {
            supportActionBar.setDisplayShowCustomEnabled(true);
            supportActionBar.setCustomView(R.layout.links_control_view);

            View controlView = supportActionBar.getCustomView();
            if (controlView instanceof LinksControlView) {
                linksControlView = (LinksControlView) controlView;

                setupToolbarTitle();
                setupToolbarFeedPicker();
                setupToolbarFilter();
            }
        }
    }

    private void setupToolbarFilter() {
        if (linksContainerView == null) return;
    }

    private void setupToolbarFeedPicker() {
        if (linksContainerView == null) return;
    }

    private void setupToolbarTitle() {
        if (linksContainerView == null) return;
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void showInfoMessage() {

    }

    @Override
    public void showErrorMessage(String error) {

    }

    @Override
    public BaseContextView getContentContext() {
        return this;
    }
}
