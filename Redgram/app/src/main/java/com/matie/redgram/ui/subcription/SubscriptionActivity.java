package com.matie.redgram.ui.subcription;

import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.matie.redgram.R;
import com.matie.redgram.ui.AppComponent;
import com.matie.redgram.ui.common.base.BaseActivity;
import com.matie.redgram.ui.common.base.Fragments;
import com.matie.redgram.ui.common.base.SlidingUpPanelFragment;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by matie on 2015-11-24.
 */
public class SubscriptionActivity extends BaseActivity{

    SubscriptionActivityComponent subscriptionActivityComponent;

    @InjectView(R.id.toolbar)
    Toolbar toolbar;
    @InjectView(R.id.sub_linear_layout)
    LinearLayout linearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub);
        ButterKnife.inject(this);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(getResources().getColor(R.color.material_red600));
        }

        getSupportFragmentManager().beginTransaction().add(R.id.container,
                Fragment.instantiate(SubscriptionActivity.this, Fragments.SUBREDDITS.getFragment()))
                .commit();
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            onBackPressed();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void setupComponent(AppComponent appComponent) {
        subscriptionActivityComponent = DaggerSubscriptionActivityComponent.builder()
                .appComponent(appComponent)
                .subscriptionActivityModule(new SubscriptionActivityModule(this))
                .build();
        subscriptionActivityComponent.inject(this);
    }

    @Override
    public AppComponent component() {
        return subscriptionActivityComponent;
    }
}
