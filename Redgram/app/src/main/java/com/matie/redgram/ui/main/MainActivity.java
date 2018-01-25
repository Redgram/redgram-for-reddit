package com.matie.redgram.ui.main;

import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.matie.redgram.R;
import com.matie.redgram.data.models.db.User;
import com.matie.redgram.ui.AppComponent;
import com.matie.redgram.ui.auth.AuthActivity;
import com.matie.redgram.ui.base.Fragments;
import com.matie.redgram.ui.base.SlidingUpPanelActivity;
import com.matie.redgram.ui.common.previews.BasePreviewFragment;
import com.matie.redgram.ui.common.utils.display.CoordinatorLayoutInterface;
import com.matie.redgram.ui.common.utils.display.CustomFragmentManager;
import com.matie.redgram.ui.common.utils.widgets.DialogUtil;
import com.matie.redgram.ui.common.utils.widgets.ToastHandler;
import com.matie.redgram.ui.home.HomeFragment;
import com.matie.redgram.ui.main.views.MainView;
import com.matie.redgram.ui.profile.ProfileActivity;
import com.matie.redgram.ui.search.SearchFragment;
import com.matie.redgram.ui.settings.SettingsActivity;
import com.matie.redgram.ui.subcription.SubscriptionActivity;
import com.matie.redgram.ui.user.UserComponent;
import com.matie.redgram.ui.userlist.UserListComponent;
import com.matie.redgram.ui.userlist.UserListView;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.Optional;


public class MainActivity extends SlidingUpPanelActivity implements CoordinatorLayoutInterface,
                                                    NavigationView.OnNavigationItemSelectedListener, MainView {

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
    UserListView userListLayout;

    @Inject
    DialogUtil dialogUtil;

    private ActionBarDrawerToggle mDrawerToggle;
    private MainComponent mainComponent;
    private CustomFragmentManager mainFragmentManager;

    private Window window;
    private boolean isDrawerOpen = false;
    private String subredditToVisitOnResult;
    private UserListComponent userListComponent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (mainComponent.getMainPresenter().getSessionUser() == null) {
            //launch auth activity with specific flags and create a guest user
            startActivity(AuthActivity.intent(this, true));
        } else {

            ButterKnife.inject(this);

            mDrawerLayout.setStatusBarBackgroundColor(
                    getResources().getColor(R.color.material_red600));

            // this code causes the drawer to be drawn below the status bar as it clears FLAG_TRANSLUCENT_STATUS
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

            setUpToolbar();
            setup(savedInstanceState);
            setUpPanel();
            setupNavUserLayout();
        }
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

    private void setupNavUserLayout() {
        final User user = mainComponent.getMainPresenter().getSessionUser();

        FrameLayout headerView = (FrameLayout) navigationView.getHeaderView(0);

        ImageView accountsView = ((ImageView) headerView.findViewById(R.id.drawerAccounts));
        accountsView.setOnClickListener(v -> {
            if(userListLayout != null && userListLayout.getParent() != null){
                return;
            }
            modifyNavDrawer(userListLayout, R.color.material_bluegrey900);
        });

        TextView username = (TextView) headerView.findViewById(R.id.drawerUserName);
        username.setText(user != null ? user.getUserName() : "Guest");
        if(user != null){
            if(User.USER_AUTH.equalsIgnoreCase(user.getUserType())){
                username.setOnClickListener(view -> {
                    Intent intent = ProfileActivity.intent(this);
                    intent.putExtra(ProfileActivity.RESULT_USER_NAME, user.getUserName());
                    openIntent(intent);
                });
            }else if(User.USER_GUEST.equalsIgnoreCase(user.getUserType())){
                navigationView.getMenu().findItem(R.id.nav_logout).setVisible(false);
            }
        }

        setUpNavUserList();
    }

    private void setUpNavUserList() {
        userListLayout.setUp();
    }

    @Override
    protected void setupComponent(AppComponent appComponent) {
        if (!(appComponent instanceof UserComponent)) return;

        final UserComponent userComponent = (UserComponent) appComponent;

        userListLayout = (UserListView) getLayoutInflater().inflate(R.layout.nav_user_list, null, false);

        mainComponent = DaggerMainComponent.builder()
                        .userComponent(userComponent)
                        .mainModule(new MainModule(this))
                        .build();

        mainComponent.inject(this);

        userListComponent = mainComponent.userComponentInjector()
                .plusUserListComponent(userListLayout, this, true);
        userListComponent.inject(userListLayout);

        userListLayout.setComponent(userListComponent);
    }

    @Override
    public AppComponent component() {
        return mainComponent;
    }

    private void setup(Bundle savedInstanceState){
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
                        resetNavDrawer();
                        setPanelHeight(48);
                    }
                    supportInvalidateOptionsMenu();
                }
            }

        };

        mDrawerLayout.setDrawerListener(mDrawerToggle);

        mainFragmentManager = new CustomFragmentManager();
        getSupportFragmentManager().registerFragmentLifecycleCallbacks(mainFragmentManager, true);

        getSupportFragmentManager().addOnBackStackChangedListener(
                () -> { selectItem(currentSelectedMenuId); }
        );

        navigationView.setNavigationItemSelectedListener(this);

        if(!checkIntentStatus(getIntent())){
            if (savedInstanceState == null) {
                getSupportFragmentManager().beginTransaction().add(R.id.container,
                        Fragment.instantiate(MainActivity.this, Fragments.HOME.getFragment()), Fragments.HOME.toString()).commit();
                selectItem(currentSelectedMenuId);
            } else {
                currentSelectedMenuId = savedInstanceState.getInt(STATE_SELECTED_POSITION);
            }
        }
    }

    private boolean checkIntentStatus(Intent intent){
        if(getIntent().getStringExtra(SubscriptionActivity.RESULT_SUBREDDIT_NAME) != null){
            String subredditName = getIntent().getStringExtra(SubscriptionActivity.RESULT_SUBREDDIT_NAME);
            launchFragmentForSubreddit(subredditName);
            return true;
        }else if(intent.getData() != null){
            Uri data = intent.getData();
            if(data.getPath().contains("/r/")){
                //open subreddit
                String path = data.getPath();
                String subredditName = path.substring(path.lastIndexOf('/')+1, path.length());
                launchFragmentForSubreddit(subredditName);
                return true;
            }
        }
        return false;
    }

    private void launchFragmentForSubreddit(String subredditName) {
        disableDrawer();
        Bundle bundle = new Bundle();
        bundle.putString(SubscriptionActivity.RESULT_SUBREDDIT_NAME, subredditName);

        HomeFragment homeFragment = (HomeFragment) Fragment
                .instantiate(this, Fragments.HOME.getFragment());
        homeFragment.setArguments(bundle);

        openFragment(homeFragment, Fragments.HOME.toString());
    }

    private void setUpPanel() {
        // fix the height of the panel to start below the status bar
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if(resourceId > 0){
            int statusBarHeight = getResources().getDimensionPixelSize(resourceId);

            SlidingUpPanelLayout.LayoutParams fl = new SlidingUpPanelLayout.LayoutParams(slidingUpFrameLayout.getLayoutParams());
            fl.setMargins(0, statusBarHeight, 0, 0);
            slidingUpFrameLayout.setLayoutParams(fl);
        }
    }

    @Override
    protected void onDestroy() {
        userListComponent.getUserListPresenter().unregisterForEvents();

        ButterKnife.reset(this);

        mainComponent.userComponentInjector().clearUserListComponent();

        super.onDestroy();
    }

    @Override
    public void onResume() {
        super.onResume();
        userListComponent.getUserListPresenter().registerForEvents();
        selectItem(currentSelectedMenuId);
        getSupportFragmentManager().registerFragmentLifecycleCallbacks(mainFragmentManager, true);
    }

    @Override
    protected void onPause() {
        super.onPause();
        getSupportFragmentManager().unregisterFragmentLifecycleCallbacks(mainFragmentManager);
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
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
        }else if(item.getItemId() == android.R.id.home){
            //only when back button enabled for this activity
            finish();
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onBackPressed() {
        if (getPanelState() == SlidingUpPanelLayout.PanelState.EXPANDED) {
            hidePanel();
        } else if(mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            closeDrawer();
        } else {
            super.onBackPressed();
            if (getSupportFragmentManager().getBackStackEntryCount() == 0) {
                currentSelectedMenuId = R.id.nav_home;
                selectItem(currentSelectedMenuId);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == SUBSCRIPTION_REQUEST_CODE){
            if(resultCode == RESULT_OK){
                String subredditName = data.getStringExtra(SubscriptionActivity.RESULT_SUBREDDIT_NAME);
                subredditToVisitOnResult = subredditName;
            }
        }else if(requestCode == UserListView.ADD_ACCOUNT){
            if(resultCode == RESULT_OK){
                String userId = data.getStringExtra(AuthActivity.RESULT_USER_ID);
                String username = data.getStringExtra(AuthActivity.RESULT_USER_NAME);
                if(userId != null && username != null){
                    recreate();
                }
            }
        }
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        if(subredditToVisitOnResult != null){
            visitSubredditOnResult();
            subredditToVisitOnResult = null;
        }
    }

    /**
     * Only called when onActivityResult returns the name of the subreddit
     */
    private void visitSubredditOnResult() {
        //select home fragment - first item
        Bundle bundle = new Bundle();
        bundle.putString(SubscriptionActivity.RESULT_SUBREDDIT_NAME, subredditToVisitOnResult);

        HomeFragment homeFragment = (HomeFragment) Fragment
                .instantiate(this, Fragments.HOME.getFragment());
        homeFragment.setArguments(bundle);

        // make sure only one fragment is in the stack
        if(!getSupportFragmentManager().findFragmentByTag(Fragments.HOME.toString()).isVisible()){
            getSupportFragmentManager().popBackStack(null, android.support.v4.app.FragmentManager.POP_BACK_STACK_INCLUSIVE);
            currentSelectedMenuId = R.id.nav_home;
            selectItem(currentSelectedMenuId);
        }

        openFragment(homeFragment, Fragments.HOME.toString());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        checkIntentStatus(intent);
    }

    private void syncDrawerToggle(){
        if(mDrawerToggle != null) { //create this check to prevent the call of syncState() when the session is not logged in
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
        Fragment fragment = mainFragmentManager.getActiveFragment();

        if(id == R.id.nav_home){
            if (!(fragment instanceof HomeFragment)) {
                // this fragment is added on activity entry, choosing it from the menu when not visible,
                // will pop all stack to return to it, and re-create it
                getSupportFragmentManager().popBackStack(null, android.support.v4.app.FragmentManager.POP_BACK_STACK_INCLUSIVE);
                currentSelectedMenuId = item.getItemId();
            }
        } else if(id == R.id.nav_search){
            if (!(fragment instanceof SearchFragment)) {
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
            startActivityForResult(intent, SUBSCRIPTION_REQUEST_CODE);
        } else if(id == R.id.nav_settings){
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
        } else if(id == R.id.nav_logout){
            //logout is only visible to non-guest type and should revoke the access token of the current user
            ToastHandler.showToast(this, "Switching to Guest", Toast.LENGTH_SHORT);
            logoutCurrentUser();
        } else if(id == R.id.nav_about) {
            dialogUtil.build()
                    .title(R.string.about_dialog_title)
                    .content(R.string.about_dialog_content)
                    .positiveText(R.string.dialog_close)
                    .show();
        }

        closeDrawer();
        return true;
    }

    private void logoutCurrentUser() {
        //switches to Guest user automatically - default
        userListComponent.getUserListPresenter().switchUser();
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
        if(mainFragmentManager.contains(previewFragment) && previewFragment != null
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



    /**
     * Adds a view to the navDrawer and changes status color
     * @param view
     * @param colorId
     */
    @Override
    public void modifyNavDrawer(View view, int colorId) {
        navigationView.addView(view);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.setStatusBarColor(getResources().getColor(colorId));
        }
    }

    /**
     * Resets Drawer to initial state
     */
    @Override
    public void resetNavDrawer() {
        if(navigationView.getChildCount() > 1){
            for(int i = 1; i <= navigationView.getChildCount(); i++){
                navigationView.removeViewAt(i);
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                window.setStatusBarColor(getResources().getColor(R.color.material_red600));
            }
        }
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

