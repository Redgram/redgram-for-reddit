package com.matie.redgram.ui.common.base;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.util.Pair;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;

import com.matie.redgram.R;
import com.matie.redgram.ui.common.utils.display.CoordinatorLayoutInterface;
import com.matie.redgram.ui.common.utils.display.CustomFragmentManager;

import java.util.EmptyStackException;

import butterknife.ButterKnife;
import butterknife.InjectView;

public abstract class BottomNavigationActivity extends BaseActivity
        implements CoordinatorLayoutInterface {
    protected int currentSelectedMenuId;

    @InjectView(R.id.app_bar)
    AppBarLayout appBarLayout;
    @InjectView(R.id.coordinator_layout)
    CoordinatorLayout coordinatorLayout;
    @InjectView(R.id.toolbar_layout)
    CollapsingToolbarLayout collapsingToolbarLayout;
    @InjectView(R.id.toolbar)
    Toolbar toolbar;
    @InjectView(R.id.container)
    FrameLayout frameLayout;
    @InjectView(R.id.bottom_navigation)
    BottomNavigationView bottomNavigationView;

    private CustomFragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.inject(this);

        if (getIntent() != null) {
            checkIntent();
        }

        setup();
    }

    protected void setup() {
        setupFragmentManager();
        setupBottomNavigation();
        setupToolbar();
    }

    protected void setupFragmentManager() {
        fragmentManager = new CustomFragmentManager();
        getSupportFragmentManager().registerFragmentLifecycleCallbacks(fragmentManager, true);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getSupportFragmentManager().registerFragmentLifecycleCallbacks(fragmentManager, true);
    }

    @Override
    protected void onPause() {
        super.onPause();
        getSupportFragmentManager().unregisterFragmentLifecycleCallbacks(fragmentManager);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.reset(this);
    }

    @Override
    protected int getContainerId() {
        return R.id.container;
    }

    protected void checkIntent() {};

    protected void setupBottomNavigation() {
        bottomNavigationView.setOnNavigationItemSelectedListener(
                item -> {
                    setContainerLayout(item);
                    return true;
                }
        );
    }

    private void setContainerLayout(MenuItem item) {
        setContainerLayout(item.getItemId());
    }

    protected void setContainerLayout(int itemId) {
        Pair<String, Fragment> destination = null;
        try {
            Fragment fragment = fragmentManager.getActiveFragment();
            destination = getDestinationFragmentInformation(itemId, fragment);
        } catch (EmptyStackException e) {
            destination = getDefaultDestinationFragmentInformation();
        } finally {
            if (destination != null) {
                openFragment(destination.second, destination.first);
                currentSelectedMenuId = itemId;
            }
        }
    }

    protected abstract Pair<String, Fragment> getDefaultDestinationFragmentInformation();

    protected abstract Pair<String, Fragment>
        getDestinationFragmentInformation(int itemId, Fragment fragment);

    protected void setSelectedMenuItemId(int itemId) {
        bottomNavigationView.setSelectedItemId(itemId);
    }

    protected void setupToolbar() {
        collapsingToolbarLayout.setTitleEnabled(false);
        setSupportActionBar(toolbar);
    }

    @Override
    public CoordinatorLayout coordinatorLayout() {
        return coordinatorLayout;
    }

    @Override
    public void showSnackBar(String msg, int length, @Nullable String actionText, @Nullable View.OnClickListener onClickListener, @Nullable Snackbar.Callback callback) {
        if(coordinatorLayout() != null){

            Snackbar snackbar = Snackbar.make(coordinatorLayout(), msg, length);

            if(actionText != null && onClickListener != null){
                snackbar.setAction(actionText, onClickListener);
            }

            if(callback != null) {
                snackbar.addCallback(callback);
            }

            snackbar.show();
        }
    }
}
