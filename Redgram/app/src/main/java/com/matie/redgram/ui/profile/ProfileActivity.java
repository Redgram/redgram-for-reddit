package com.matie.redgram.ui.profile;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.matie.redgram.R;
import com.matie.redgram.ui.App;
import com.matie.redgram.ui.AppComponent;
import com.matie.redgram.ui.common.base.BaseActivity;
import com.matie.redgram.ui.common.base.ViewPagerActivity;
import com.matie.redgram.ui.common.utils.display.CoordinatorLayoutInterface;
import com.matie.redgram.ui.common.utils.widgets.DialogUtil;
import com.matie.redgram.ui.common.views.adapters.SectionsPagerAdapter;
import com.matie.redgram.ui.profile.components.DaggerProfileComponent;
import com.matie.redgram.ui.profile.components.ProfileComponent;
import com.matie.redgram.ui.profile.modules.ProfileModule;
import com.matie.redgram.ui.profile.views.adapters.ProfilePagerAdapter;

import javax.inject.Inject;

import butterknife.ButterKnife;

public class ProfileActivity extends ViewPagerActivity implements CoordinatorLayoutInterface {

    private ProfileComponent profileComponent;

    @Inject
    App app;
    @Inject
    DialogUtil dialogUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.inject(this);

//        app.getRedditClient().getUserOverview("nullbell")
//                .compose(bindToLifecycle())
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe();
    }

    @Override
    protected void setupComponent(AppComponent appComponent) {
        profileComponent = DaggerProfileComponent.builder()
                            .appComponent(appComponent)
                            .profileModule(new ProfileModule(this))
                            .build();
        profileComponent.inject(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.reset(this);
    }

    @Override
    protected void checkIntent() {}

    @Override
    protected int getInitialPagerPosition() {
        return 0;
    }

    @Override
    protected SectionsPagerAdapter pagerAdapterInstance() {
        return new ProfilePagerAdapter(getSupportFragmentManager());
    }

    @Override
    protected void setupViewPager() {
        //call super to initialize to set the adapter and other common set ups
        super.setupViewPager();
        getViewPager().addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                setToolbarTitle(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }


    @Override
    public AppComponent component() {
        return profileComponent;
    }

    @Override
    public App app() {
        return null;
    }

    @Override
    public DialogUtil getDialogUtil() {
        return null;
    }

    @Override
    protected BaseActivity activity() {
        return this;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_profile;
    }

    @Override
    protected int getContainerId() {
        return R.id.container;
    }

    public static Intent intent(Context context){
        Intent intent = new Intent(context, ProfileActivity.class);
        return intent;
    }

    @Override
    public CoordinatorLayout coordinatorLayout() {
        return getCoordinatorLayout();
    }

    @Override
    public void showSnackBar(String msg, int length, @Nullable String actionText, @Nullable View.OnClickListener onClickListener, @Nullable Snackbar.Callback callback) {
        if(coordinatorLayout() != null){

            Snackbar snackbar = Snackbar.make(coordinatorLayout(), msg, length);

            if(actionText != null && onClickListener != null){
                snackbar.setAction(actionText, onClickListener);
            }

            if(callback != null) {
                snackbar.setCallback(callback);
            }
            //hide the panel before showing the snack bar
            snackbar.show();
        }
    }
}
