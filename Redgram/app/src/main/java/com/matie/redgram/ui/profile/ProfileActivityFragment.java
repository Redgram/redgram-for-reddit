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
import com.matie.redgram.ui.common.utils.widgets.DialogUtil;
import com.matie.redgram.ui.links.LinksContainerView;
import com.matie.redgram.ui.links.LinksControlView;
import com.matie.redgram.ui.profile.components.DaggerProfileActivityComponent;
import com.matie.redgram.ui.profile.components.ProfileActivityComponent;
import com.matie.redgram.ui.profile.components.ProfileComponent;
import com.matie.redgram.ui.profile.modules.ProfileActivityModule;
import com.matie.redgram.ui.profile.views.ProfileActivityView;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;

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
    List<String> profileListings;
    List<String> profileListingsFilter;

    @Inject
    DialogUtil dialogUtil;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile_activity, container, false);
        ButterKnife.inject(this, view);

        setupSwipeRefreshLayout();

        profileListings = Arrays.asList(getViewContext().getResources().getStringArray(R.array.profileListing));
        profileListingsFilter = Arrays.asList(getViewContext().getResources().getStringArray(R.array.profileListingFilter));

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
        if (linksControlView == null) return;

    }

    private void setupToolbarFeedPicker() {
        if (linksControlView == null) return;
        linksControlView.setItemPickerListener(view -> {
            dialogUtil.build()
                    .items(profileListings)
                    .itemsCallback((dialog, itemView, pos, text) -> {
                        final String targetListing = text.toString().toLowerCase();

                        final Map<String, String> queryMap = new HashMap<>();
                        queryMap.put("sort", profileListingsFilter.get(0));

                        setToolbarTitle(text.toString());

                        activityPresenter.getListing(targetListing, queryMap);
                    })
                    .show();
        });
    }

    private void setupToolbarTitle() {
        if (linksControlView == null) return;
        setToolbarTitle(profileListings.get(0));
    }

    private void setToolbarTitle(String title) {
        linksControlView.setTitle(title);
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
}
