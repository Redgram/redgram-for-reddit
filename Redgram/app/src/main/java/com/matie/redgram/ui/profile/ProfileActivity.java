package com.matie.redgram.ui.profile;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.util.Pair;
import android.view.MenuItem;
import android.view.WindowManager;

import com.matie.redgram.R;
import com.matie.redgram.ui.App;
import com.matie.redgram.ui.AppComponent;
import com.matie.redgram.ui.common.base.BottomNavigationActivity;
import com.matie.redgram.ui.common.base.Fragments;
import com.matie.redgram.ui.common.utils.widgets.DialogUtil;
import com.matie.redgram.ui.profile.components.DaggerProfileComponent;
import com.matie.redgram.ui.profile.components.ProfileComponent;
import com.matie.redgram.ui.profile.modules.ProfileModule;

import javax.inject.Inject;

import butterknife.ButterKnife;
import io.realm.RealmChangeListener;

public class ProfileActivity extends BottomNavigationActivity {

    @Inject
    App app;
    @Inject
    DialogUtil dialogUtil;

    public static final String RESULT_USER_NAME = "result_user_name";

    private ProfileComponent profileComponent;
    private String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.inject(this);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }


        Log.d("username", username);
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
    protected void setup() {
        super.setup();
        setSelectedMenuItemId(R.id.profile_about);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.reset(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        if (id == android.R.id.home) {
            //if no fragments in stack close activity
            if(!getSupportFragmentManager().popBackStackImmediate()){
                onBackPressed();
            }
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void checkIntent() {
        if (getIntent().getStringExtra(RESULT_USER_NAME) != null) {
            username = getIntent().getStringExtra(RESULT_USER_NAME);
        } else if (getIntent().getData() != null) {
            Uri data = getIntent().getData();
            if (data.getPath().contains("/u/")) {
                //open user
                String path = data.getPath();
                username = path.substring(path.lastIndexOf('/') + 1, path.length());
            }
        }
    }

    @Override
    protected Pair<String, Fragment> getDestinationFragmentInformation(int itemId, Fragment fragment) {
        if (!(fragment instanceof ProfileAboutFragment)
                && itemId == R.id.profile_about) {
            return buildDestinationFragment(Fragments.PROFILE_ABOUT);
        } else if (!(fragment instanceof ProfileActivityFragment)
                && itemId == R.id.profile_activity) {
            return buildDestinationFragment(Fragments.PROFILE_ACTIVITY);
        } else {
            return buildDestinationFragment(Fragments.PROFILE_ABOUT);
        }
    }

    @Override
    protected Fragment getDefaultFragment() {
        return instantiateFragment(Fragments.PROFILE_ABOUT.getFragment());
    }

    private Pair<String, Fragment> buildDestinationFragment(Fragments fragmentEnum) {
        return new Pair<>(fragmentEnum.toString(), instantiateFragment(fragmentEnum.getFragment()));
    }

    private Fragment instantiateFragment(String name) {
        return Fragment.instantiate(ProfileActivity.this, name);
    }

    @Override
    public AppComponent component() {
        return profileComponent;
    }

    @Override
    public DialogUtil getDialogUtil() {
        return null;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_profile;
    }

    @Override
    protected RealmChangeListener getRealmSessionChangeListener() {
        return null;
    }

    public static Intent intent(Context context){
        return new Intent(context, ProfileActivity.class);
    }
}
