package com.matie.redgram.ui.common.base;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.matie.redgram.R;
import com.matie.redgram.ui.App;
import com.matie.redgram.ui.AppComponent;
import com.matie.redgram.ui.common.main.MainActivity;
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

    public void openIntentForResult(Intent intent, int requestCode, int enterAnim, int exitAnim){
        startActivityForResult(intent, requestCode);
        overridePendingTransition(enterAnim, exitAnim);
    }

    public void openFragmentWithResult(Fragment fragment, Bundle bundle) {

        if(bundle != null){
            fragment.setArguments(bundle);
        }

        getSupportFragmentManager().beginTransaction()
                .replace(getContainerId(), fragment)
                        //important to avoid IllegalStateException
                .commitAllowingStateLoss();
    }

    /**
     * This will instruct the activity to get the listing of the subreddit
     *
     * NOTE: this is a new instance of MainActivity, the navdrawer should be disabled
     * @param subredditName
     */
    public void openActivityForSubreddit(String subredditName) {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.putExtra(SubscriptionActivity.RESULT_SUBREDDIT_NAME, subredditName);
        openIntent(intent, 0, 0);
    }

    /**
     * This will instruct the activity to open the profile fragment
     *
     * NOTE: this is a new instance of MainActivity, the navdrawer should be disabled
     * @param username
     */
    public void openActivityForProfile(String username) {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.putExtra(SubscriptionActivity.RESULT_SUBREDDIT_NAME, username);
//        openIntent(intent, R.anim.enter, R.anim.exit);
    }

}
