package com.matie.redgram.ui.common.main;

import android.content.res.Configuration;
import android.graphics.Rect;
import android.os.Build;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.os.Bundle;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.support.v7.widget.Toolbar;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.matie.redgram.R;
import com.matie.redgram.ui.AppComponent;
import com.matie.redgram.ui.common.base.BaseActivity;
import com.matie.redgram.ui.common.base.Fragments;
import com.matie.redgram.ui.common.utils.ScrimInsetsFrameLayout;
import com.matie.redgram.ui.home.HomeFragment;
import com.matie.redgram.data.models.DrawerItem;
import com.matie.redgram.ui.common.views.widgets.drawer.DrawerView;
import com.matie.redgram.ui.search.SearchFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnItemClick;


public class MainActivity extends BaseActivity implements ScrimInsetsFrameLayout.OnInsetsCallback{

    private int currentSelectedPosition = 0;

    static final String STATE_SELECTED_POSITION = "selected_navigation_drawer_position";

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

    private ActionBarDrawerToggle mDrawerToggle;

    private CharSequence mTitle;

    private CharSequence mDrawerTitle;

    private List<DrawerItem> navigationItems;

    private MainComponent mainComponent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fresco.initialize(this);
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
    }

    @Override
    protected void setupComponent(AppComponent appComponent) {
        mainComponent = DaggerMainComponent.builder()
                        .appComponent(appComponent)
                        .mainModule(new MainModule(this))
                        .build();
       // mainComponent.inject(this);
    }

    @Override
    public MainComponent component() {
        return mainComponent;
    }

    private void setup(){

        //ActionBar setup
        this.setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            scrimInsetsFrameLayout.setOnInsetsCallback(this);
        }

        navigationItems = new ArrayList<DrawerItem>();

        //menu items
        navigationItems.add(new DrawerItem(getString(R.string.fragment_home), R.drawable.ic_home_black_48dp, true));
        navigationItems.add(new DrawerItem(getString(R.string.fragment_search), R.drawable.ic_search_black_48dp, true));

        //sub-menu items
        navigationItems.add(new DrawerItem(getString(R.string.fragment_about), R.drawable.ic_help_black_48dp, false));

        mNavigationDrawerListViewWrapper.replaceWith(navigationItems);

        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close) {
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                getSupportActionBar()
                        .setTitle(navigationItems.get(currentSelectedPosition).getItemName());
                supportInvalidateOptionsMenu();
            }

            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                supportInvalidateOptionsMenu();
            }
        };
        mDrawerLayout.setDrawerListener(mDrawerToggle);

        selectItem(currentSelectedPosition);
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
//            getSupportActionBar()
//                    .setTitle(navigationItems.get(currentSelectedPosition).getItemName());
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

}

