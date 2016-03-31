package com.matie.redgram.ui.common.base;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.matie.redgram.R;
import com.matie.redgram.data.managers.storage.preferences.PreferenceManager;
import com.matie.redgram.ui.App;
import com.matie.redgram.ui.AppComponent;
import com.matie.redgram.ui.common.auth.AuthActivity;
import com.matie.redgram.ui.home.HomeFragment;
import com.matie.redgram.ui.subcription.SubscriptionActivity;

import icepick.Icepick;

/**
 * Created by matie on 09/06/15.
 */
public abstract class BaseActivity extends AppCompatActivity {

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fresco.initialize(this);
        Icepick.restoreInstanceState(this, savedInstanceState);
        setContentView(getLayoutId());

        AppComponent appComponent = (AppComponent) App.get(this).component();
        setupComponent(appComponent);
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        Icepick.saveInstanceState(this, outState);
    }

    @Override protected void onResume(){
        super.onResume();

        //onResume of any activity, show connection status
        ((App)getApplication()).getConnectionManager().showConnectionStatus(true);

    }
    protected abstract void setupComponent(AppComponent appComponent);
    public abstract AppComponent component();
    protected abstract BaseActivity activity();
    protected abstract int getLayoutId();
    protected abstract int getContainerId();

    public void openIntent(Intent intent, int enterAnim, int exitAnim){
        startActivity(intent);
        overridePendingTransition(enterAnim, exitAnim);
    }

    public void openFragmentWithResult(String subredditName) {
        HomeFragment homeFragment = (HomeFragment) Fragment
                .instantiate(activity(), Fragments.HOME.getFragment());

        Bundle bundle = new Bundle();
        bundle.putString(SubscriptionActivity.RESULT_SUBREDDIT_NAME, subredditName);
        homeFragment.setArguments(bundle);


        getSupportFragmentManager().beginTransaction()
                .replace(getContainerId(), homeFragment)
                        //important to avoid IllegalStateException
                .commitAllowingStateLoss();
    }

}
