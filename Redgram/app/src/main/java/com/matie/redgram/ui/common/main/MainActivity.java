package com.matie.redgram.ui.common.main;

import android.content.res.Configuration;
import android.graphics.Rect;
import android.os.Build;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.os.Bundle;
import android.support.v7.app.ActionBarDrawerToggle;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.support.v7.widget.Toolbar;

import com.matie.redgram.R;
import com.matie.redgram.data.managers.media.images.ImageManager;
import com.matie.redgram.ui.App;
import com.matie.redgram.ui.AppComponent;
import com.matie.redgram.ui.common.base.BaseActivity;
import com.matie.redgram.ui.common.base.Fragments;
import com.matie.redgram.ui.common.previews.BasePreviewFragment;
import com.matie.redgram.ui.common.utils.DialogUtil;
import com.matie.redgram.ui.common.utils.ScrimInsetsFrameLayout;
import com.matie.redgram.ui.common.utils.SlidingPanelControllerInterface;
import com.matie.redgram.ui.common.utils.StringUtils;
import com.matie.redgram.ui.home.HomeFragment;
import com.matie.redgram.data.models.main.items.DrawerItem;
import com.matie.redgram.ui.common.views.widgets.drawer.DrawerView;
import com.matie.redgram.ui.search.SearchFragment;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnItemClick;


public class MainActivity extends BaseActivity implements ScrimInsetsFrameLayout.OnInsetsCallback,
        SlidingPanelControllerInterface, SlidingUpPanelLayout.PanelSlideListener {

    private int currentSelectedPosition = 0;

    static final String STATE_SELECTED_POSITION = "selected_navigation_drawer_position";

    @InjectView(R.id.slide_up_panel_layout)
    SlidingUpPanelLayout slidingUpPanelLayout;

    @InjectView(R.id.main_slide_up_panel)
    FrameLayout slidingUpFrameLayout;

    @InjectView(R.id.navigationDrawerListViewWrapper)
    DrawerView mNavigationDrawerListViewWrapper;

    @InjectView(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;

    @InjectView(R.id.scrimInsetsFrameLayout)
    ScrimInsetsFrameLayout scrimInsetsFrameLayout;

    @InjectView(R.id.leftDrawerListView)
    ListView leftDrawerListView;

    @InjectView(R.id.toolbar)
    Toolbar toolbar;

    BasePreviewFragment previewFragment;
    Fragments currentPreviewFragment;

    @Inject
    App app;
    @Inject
    DialogUtil dialogUtil;
    @Inject
    ImageManager imageManager;

    private ActionBarDrawerToggle mDrawerToggle;

    private CharSequence mTitle;

    private CharSequence mDrawerTitle;

    private List<DrawerItem> navigationItems;

    private MainComponent mainComponent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);

        mTitle = mDrawerTitle = getTitle();

        mDrawerLayout.setStatusBarBackgroundColor(
                getResources().getColor(R.color.material_red900));

        if (savedInstanceState == null) {
          getSupportFragmentManager().beginTransaction().add(R.id.container,
          Fragment.instantiate(MainActivity.this, Fragments.HOME.getFragment())).commit();
        } else {
          currentSelectedPosition = savedInstanceState.getInt(STATE_SELECTED_POSITION);
        }

        setup();
        setUpPanel();
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

    private void setup(){

        //ActionBar setup
        this.setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            scrimInsetsFrameLayout.setOnInsetsCallback(this);
        }

        navigationItems = new ArrayList<DrawerItem>();

        //menu items
        navigationItems.add(new DrawerItem(getString(R.string.fragment_home), R.drawable.ic_action_public, true));
        navigationItems.add(new DrawerItem(getString(R.string.fragment_search), R.drawable.ic_action_search, true));

        //sub-menu items
        navigationItems.add(new DrawerItem(getString(R.string.fragment_about), 0, false));

        mNavigationDrawerListViewWrapper.replaceWith(navigationItems);

        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close) {
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                supportInvalidateOptionsMenu();

                // TODO: 2015-11-06 get dimension height from resources
//                float pixels = TypedValue.applyDimension(
//                        TypedValue.COMPLEX_UNIT_DIP, 48, getResources().getDisplayMetrics());
//
                setPanelHeight(48);
            }

            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                supportInvalidateOptionsMenu();

                setPanelHeight(0);
            }
        };
        mDrawerLayout.setDrawerListener(mDrawerToggle);

        selectItem(currentSelectedPosition);
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
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(STATE_SELECTED_POSITION, currentSelectedPosition);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        if(mDrawerToggle != null) { //create this check to prevent the call of syncState() when the user is not logged in
            mDrawerToggle.syncState();
        }
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
        }
        return super.onOptionsItemSelected(item);
    }

    public int getCurrentSelectedPosition() {
        return currentSelectedPosition;
    }

    public List<DrawerItem> getNavigationItems() {
        return navigationItems;
    }

    @OnItemClick(R.id.leftDrawerListView)
    public void OnItemClick(int position, long id) {
        if (mDrawerLayout.isDrawerOpen(scrimInsetsFrameLayout)) {
            mDrawerLayout.closeDrawer(scrimInsetsFrameLayout);
            onNavigationDrawerItemSelected(position);

            selectItem(position);
        }
    }

    private void selectItem(int position) {

        if (leftDrawerListView != null) {
            leftDrawerListView.setItemChecked(position, true);

            navigationItems.get(currentSelectedPosition).setSelected(false);
            navigationItems.get(position).setSelected(true);

            currentSelectedPosition = position;
        }

        if (scrimInsetsFrameLayout != null) {
            mDrawerLayout.closeDrawer(scrimInsetsFrameLayout);
        }
    }

    private void onNavigationDrawerItemSelected(int position) {
        switch (position) {
            case 0:
                if (!(getSupportFragmentManager().getFragments()
                        .get(0) instanceof HomeFragment)) {
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.container, Fragment
                                    .instantiate(MainActivity.this, Fragments.HOME.getFragment()))
                            .commit();
                }
                break;

            //add one for each navigation item
            case 1:
                if (!(getSupportFragmentManager().getFragments()
                        .get(0) instanceof SearchFragment)) {
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.container, Fragment
                                    .instantiate(MainActivity.this, Fragments.SEARCH.getFragment()))
                            .commit();
                }
                break;
        }

    }

    public App getApp() {
        return app;
    }

    public DialogUtil getDialogUtil() {
        return dialogUtil;
    }

    public ImageManager getImageManager() {
        return imageManager;
    }

    @Override
    public void onInsetsChanged(Rect insets) {
        Toolbar toolbar = this.toolbar;
        ViewGroup.MarginLayoutParams lp = (ViewGroup.MarginLayoutParams)
                                                    toolbar.getLayoutParams();
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP)
            lp.topMargin = insets.top;

        int top = insets.top;
        insets.top += toolbar.getHeight();
        toolbar.setLayoutParams(lp);
        insets.top = top; // revert
    }

    @Override
    public void togglePanel() {
        if(slidingUpPanelLayout.getPanelState() != SlidingUpPanelLayout.PanelState.EXPANDED){
            slidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.EXPANDED);
        }else{
            slidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
        }
    }

    @Override
    public void showPanel() {
        if(slidingUpPanelLayout.getPanelState() == SlidingUpPanelLayout.PanelState.HIDDEN){
            slidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
        }
    }

    @Override
    public void hidePanel() {
        if(slidingUpPanelLayout.getPanelState() != SlidingUpPanelLayout.PanelState.HIDDEN){
            slidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.HIDDEN);
        }
    }

    @Override
    public void setPanelHeight(int height) {
        float pixels = TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, height, getResources().getDisplayMetrics());
        slidingUpPanelLayout.setPanelHeight((int) pixels);
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
    public void setDragable(View view) {
        slidingUpPanelLayout.setDragView(view);
    }

    @Override
    public SlidingUpPanelLayout.PanelState getPanelState() {
        return slidingUpPanelLayout.getPanelState();
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
}

