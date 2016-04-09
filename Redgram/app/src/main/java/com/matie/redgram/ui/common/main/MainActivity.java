package com.matie.redgram.ui.common.main;

import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.os.Bundle;
import android.support.v7.app.ActionBarDrawerToggle;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;
import android.widget.Toast;

import com.matie.redgram.R;
import com.matie.redgram.data.managers.storage.db.session.SessionHelper;
import com.matie.redgram.data.managers.storage.db.session.SessionManager;
import com.matie.redgram.data.models.db.User;
import com.matie.redgram.ui.App;
import com.matie.redgram.ui.AppComponent;
import com.matie.redgram.ui.common.base.BaseActivity;
import com.matie.redgram.ui.common.base.Fragments;
import com.matie.redgram.ui.common.base.SlidingUpPanelActivity;
import com.matie.redgram.ui.common.previews.BasePreviewFragment;
import com.matie.redgram.ui.common.utils.display.CoordinatorLayoutInterface;
import com.matie.redgram.ui.common.utils.widgets.DialogUtil;
import com.matie.redgram.ui.home.HomeFragment;
import com.matie.redgram.ui.search.SearchFragment;
import com.matie.redgram.ui.subcription.SubscriptionActivity;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.Optional;
import io.realm.Realm;


public class MainActivity extends SlidingUpPanelActivity implements CoordinatorLayoutInterface, NavigationView.OnNavigationItemSelectedListener{

    private static final int SUBSCRIPTION_REQUEST_CODE = 69;
    private int currentSelectedMenuId = R.id.nav_home;

    static final String STATE_SELECTED_POSITION = "selected_navigation_drawer_position";

    @InjectView(R.id.main_slide_up_panel)
    FrameLayout slidingUpFrameLayout;

    @InjectView(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;

    @InjectView(R.id.coordinator_layout)
    CoordinatorLayout coordinatorLayout;

    @InjectView(R.id.nav_view)
    NavigationView navigationView;

    @InjectView(R.id.toolbar)
    Toolbar toolbar;

    @Optional
    @InjectView(R.id.drawerUserName)
    TextView drawerUserName;

    BasePreviewFragment previewFragment;
    Fragments currentPreviewFragment;

    @Inject
    App app;
    @Inject
    DialogUtil dialogUtil;

    private ActionBarDrawerToggle mDrawerToggle;
    private CharSequence mTitle;
    private CharSequence mDrawerTitle;
    private MainComponent mainComponent;

    private Window window;
    private boolean isDrawerOpen = false;
    private Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.inject(this);

        mTitle = mDrawerTitle = getTitle();

        mDrawerLayout.setStatusBarBackgroundColor(
                getResources().getColor(R.color.material_red600));

        //this code causes the drawer to be drawn below the status bar as it clears FLAG_TRANSLUCENT_STATUS
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(getResources().getColor(R.color.material_red600));
        }

        // Possible work around for market launches. See http://code.google.com/p/android/issues/detail?id=2373
        // for more details. Essentially, the market launches the main activity on top of other activities.
        // we never want this to happen. Instead, we check if we are the root and if not, we finish.
        if (!isTaskRoot()) {
            final Intent intent = getIntent();
            if (intent.hasCategory(Intent.CATEGORY_LAUNCHER) && Intent.ACTION_MAIN.equals(intent.getAction())) {
                Log.w("LAUNCH_LOG", "Main MainActivity is not the root.  Finishing Main MainActivity instead of launching.");
                finish();
                return;
            }
        }

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().add(R.id.container,
                    Fragment.instantiate(MainActivity.this, Fragments.HOME.getFragment())).commit();
            selectItem(currentSelectedMenuId);
        } else {
            currentSelectedMenuId = savedInstanceState.getInt(STATE_SELECTED_POSITION);
        }

        setUpToolbar();
        setup();
        setUpPanel();
        setUpRealm();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected int getContainerId() {
        return R.id.container;
    }

    private void setUpToolbar() {
        //ActionBar setup
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    private void setUpRealm() {
        SessionManager sessionManager = app.getSessionManager();
        realm = sessionManager.getInstance();
        User user = SessionHelper.getSessionUser(realm);
        if(user != null){
//            navigationView.get(0)
//            drawerUserName.setText(user.getUserName());
        }

    }


    @Override
    protected void setupComponent(AppComponent appComponent) {
        mainComponent = DaggerMainComponent.builder()
                        .appComponent(appComponent)
                        .mainModule(new MainModule(this))
                        .build();
        mainComponent.inject(this);
    }

    @Override
    public AppComponent component() {
        return mainComponent;
    }

    @Override
    public BaseActivity activity() {
        return this;
    }

    private void setup(){
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close) {
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                supportInvalidateOptionsMenu();

                isDrawerOpen = false;
            }

            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                supportInvalidateOptionsMenu();

                isDrawerOpen = true;
            }

            @Override
            public void onDrawerStateChanged(int newState) {
                if (newState == DrawerLayout.STATE_SETTLING) {
                    if (!isDrawerOpen) {
                        setPanelHeight(0);
                        // starts opening
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && window != null) {
                            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                        }
                    } else {
                        // closing drawer
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && window != null) {
                            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                        }
                        setPanelHeight(48);
                    }
                    supportInvalidateOptionsMenu();
                }
            }

        };

        mDrawerLayout.setDrawerListener(mDrawerToggle);

        getSupportFragmentManager().addOnBackStackChangedListener(
                () -> { selectItem(currentSelectedMenuId); }
        );

        navigationView.setNavigationItemSelectedListener(this);

        if(getIntent().getStringExtra(SubscriptionActivity.RESULT_SUBREDDIT_NAME) != null){
            disableDrawer();
            String subredditName = getIntent().getStringExtra(SubscriptionActivity.RESULT_SUBREDDIT_NAME);

            Bundle bundle = new Bundle();
            bundle.putString(SubscriptionActivity.RESULT_SUBREDDIT_NAME, subredditName);

            HomeFragment homeFragment = (HomeFragment) Fragment
                    .instantiate(activity(), Fragments.HOME.getFragment());

            openFragmentWithResult(homeFragment, bundle);
        } // TODO: 2016-04-08 do the same for profile

    }

    private boolean checkIntentStatus(Intent intent){
        Uri data = intent.getData();
        if(data != null){
            Log.d("INTENT_DATA", data.toString());
            if(data.getPath().contains("/r/")){
                //open subreddit
                String path = data.getPath();
                String subredditName = path.substring(path.lastIndexOf('/')+1, path.length());
                // TODO: 2016-04-06 open another activity and check intent to hide navbar
                openActivityForSubreddit(subredditName);
            }else if(data.getPath().contains("/u/")){
                //open user
            }
            return true;
        }
        return false;
    }

    private void setUpPanel() {
        //fix the height of the panel to start below the status bar
        int statusBarHeight = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if(resourceId > 0){
            statusBarHeight = getResources().getDimensionPixelSize(resourceId);

            SlidingUpPanelLayout.LayoutParams fl = new SlidingUpPanelLayout.LayoutParams(slidingUpFrameLayout.getLayoutParams());
            fl.setMargins(0, statusBarHeight, 0, 0);
            slidingUpFrameLayout.setLayoutParams(fl);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.reset(this);
        SessionHelper.close(realm);
    }

    @Override
    public void onResume() {
        super.onResume();
        selectItem(currentSelectedMenuId);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(STATE_SELECTED_POSITION, currentSelectedMenuId);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        syncDrawerToggle();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        } else if (item.getItemId() == R.id.action_settings) {
            return true;
        }else if(item.getItemId() == android.R.id.home){
            //only when back button enabled for this activity
            finish();
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            closeDrawer();
        } else {
//            Log.d("back-stack", getSupportFragmentManager().getBackStackEntryCount()+"");
            super.onBackPressed();
//            Log.d("back-stack-after", getSupportFragmentManager().getBackStackEntryCount()+"");
            if(getSupportFragmentManager().getBackStackEntryCount() == 0){
                currentSelectedMenuId = R.id.nav_home;
                selectItem(currentSelectedMenuId);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == SUBSCRIPTION_REQUEST_CODE){
            if(resultCode == RESULT_OK){
                String subredditName = data.getStringExtra(SubscriptionActivity.RESULT_SUBREDDIT_NAME);
                if(!subredditName.isEmpty() || subredditName != null){
                    //select home fragment - first item
                    Bundle bundle = new Bundle();
                    bundle.putString(SubscriptionActivity.RESULT_SUBREDDIT_NAME, subredditName);

                    HomeFragment homeFragment = (HomeFragment) Fragment
                            .instantiate(activity(), Fragments.HOME.getFragment());

                    openFragmentWithResult(homeFragment, bundle);
                }
            }
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        checkIntentStatus(intent);
    }

    private void syncDrawerToggle(){
        if(mDrawerToggle != null) { //create this check to prevent the call of syncState() when the user is not logged in
            mDrawerToggle.syncState();
        }
    }

    private void selectItem(int position) {
        navigationView.setCheckedItem(position);
    }

    private void closeDrawer(){
        mDrawerLayout.closeDrawer(GravityCompat.START);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        int currentFragmentPos = getSupportFragmentManager().getFragments().size()-1;

        if(id == R.id.nav_home){
            if (!(getSupportFragmentManager().getFragments()
                    .get(currentFragmentPos) instanceof HomeFragment)) {
                //this fragment is added on activity entry, choosing it from the menu when not visible,
                //will pop all stack to return to it, and re-create it
                getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                currentSelectedMenuId = item.getItemId();
            }
        } else if(id == R.id.nav_search){
            if (!(getSupportFragmentManager().getFragments()
                    .get(currentFragmentPos) instanceof SearchFragment)) {
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container, Fragment
                                .instantiate(MainActivity.this, Fragments.SEARCH.getFragment()), Fragments.SEARCH.toString());
                //only add to back-stack if currentSelectedMenuId is the home menu's
                if(currentSelectedMenuId == R.id.nav_home){
                    transaction.addToBackStack("search_view");
                }
                transaction.commit();
                currentSelectedMenuId = item.getItemId();
            }
        } else if(id == R.id.nav_subs){
            Intent intent = new Intent(this, SubscriptionActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
            startActivityForResult(intent, SUBSCRIPTION_REQUEST_CODE);
        } else if(id == R.id.nav_logout){
            app.getToastHandler().showToast("Logout", Toast.LENGTH_SHORT);
            logoutCurrentUser();
        }

        closeDrawer();
        return true;
    }

    private void logoutCurrentUser() {
        app.getSessionManager().deleteSession(realm);
        app.startAuthActivity();
    }

    public App getApp() {
        return app;
    }

    public DialogUtil getDialogUtil() {
        return dialogUtil;
    }


    @Override
    public void togglePanel() {
        if(getSlidingUpPanelLayout().getPanelState() != SlidingUpPanelLayout.PanelState.EXPANDED){
            getSlidingUpPanelLayout().setPanelState(SlidingUpPanelLayout.PanelState.EXPANDED);
        }else{
            getSlidingUpPanelLayout().setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
        }
    }

    @Override
    public void showPanel() {
        if(getSlidingUpPanelLayout().getPanelState() == SlidingUpPanelLayout.PanelState.HIDDEN){
            getSlidingUpPanelLayout().setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
        }
    }

    @Override
    public void hidePanel() {
        if(getSlidingUpPanelLayout().getPanelState() != SlidingUpPanelLayout.PanelState.HIDDEN){
            getSlidingUpPanelLayout().setPanelState(SlidingUpPanelLayout.PanelState.HIDDEN);
        }
    }

    @Override
    public void setPanelHeight(int height) {
        float pixels = TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, height, getResources().getDisplayMetrics());
        getSlidingUpPanelLayout().setPanelHeight((int) pixels);
    }

    @Override
    public void setPanelView(Fragments fragmentEnum, Bundle bundle) {
        //show panel
        togglePanel();
        //load data
        if(getSupportFragmentManager().getFragments().contains(previewFragment) && previewFragment != null
                && currentPreviewFragment != null && fragmentEnum == currentPreviewFragment){
            previewFragment.refreshPreview(bundle);
        }else{

            previewFragment = (BasePreviewFragment)Fragment.instantiate(MainActivity.this, fragmentEnum.getFragment());
            previewFragment.setArguments(bundle);

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.main_slide_up_panel, previewFragment, fragmentEnum.getFragment())
                    .commit();
            //set currently loaded preview
            currentPreviewFragment = fragmentEnum;
        }

    }

    @Override
    public void setDraggable(View view) {
        getSlidingUpPanelLayout().setDragView(view);
    }

    @Override
    public SlidingUpPanelLayout.PanelState getPanelState() {
        return getSlidingUpPanelLayout().getPanelState();
    }

    @Override
    public void onPanelSlide(View panel, float slideOffset) {

    }

    @Override
    public void onPanelCollapsed(View panel) {

    }

    @Override
    public void onPanelExpanded(View panel) {

    }

    @Override
    public void onPanelAnchored(View panel) {

    }

    @Override
    public void onPanelHidden(View panel) {

    }

    //controlling the coordinator layout
    @Override
    public CoordinatorLayout getCoordinatorLayout() {
        return coordinatorLayout;
    }

    @Override
    public void showSnackBar(String msg, int length, @Nullable String actionText, @Nullable View.OnClickListener onClickListener, @Nullable Snackbar.Callback callback) {
        if(getCoordinatorLayout() != null){

            Snackbar snackbar = Snackbar.make(getCoordinatorLayout(), msg, length);

            if(actionText != null && onClickListener != null){
                snackbar.setAction(actionText, onClickListener);
            }

            if(callback != null) {
                snackbar.setCallback(callback);
            }else{
                snackbar.setCallback(new PanelSnackBarCallback());
            }
            //hide the panel before showing the snack bar
            setPanelHeight(0);
            snackbar.show();
        }
    }

    public void enableDrawer(){
        mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
        mDrawerToggle.setDrawerIndicatorEnabled(true);
    }

    public void disableDrawer(){
        mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        mDrawerToggle.setDrawerIndicatorEnabled(false);
    }
}

