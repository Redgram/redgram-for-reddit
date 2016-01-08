package com.matie.redgram.ui.comments.views;

import android.os.Build;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.google.gson.Gson;
import com.matie.redgram.R;
import com.matie.redgram.data.models.main.items.PostItem;
import com.matie.redgram.ui.AppComponent;
import com.matie.redgram.ui.comments.views.adapters.CommentsPagerAdapter;
import com.matie.redgram.ui.common.base.BaseActivity;
import com.matie.redgram.ui.common.previews.BasePreviewFragment;
import com.matie.redgram.ui.common.previews.ImagePreviewFragment;
import com.matie.redgram.ui.common.previews.WebPreviewFragment;
import com.r0adkll.slidr.Slidr;
import com.r0adkll.slidr.model.SlidrInterface;

public class CommentsActivity extends BaseActivity implements CommentsPagerAdapter.CommentsPreviewDetector {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private CommentsPagerAdapter commentsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;
    private int viewPagerPosition = 0;
    private PostItem postItem;

    private SlidrInterface mSlidrInterface;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comments);

        if(getIntent() != null){
            String key = getResources().getString(R.string.main_data_key);
            postItem = new Gson().fromJson(getIntent().getStringExtra(key), PostItem.class);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(getResources().getColor(R.color.material_red600));}

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setShowHideAnimationEnabled(true);

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        commentsPagerAdapter = new CommentsPagerAdapter(getSupportFragmentManager(), this);

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(commentsPagerAdapter);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if(position > 0){
                    mSlidrInterface.lock();
                    getSupportActionBar().hide();
                }else{
                    mSlidrInterface.unlock();
                    getSupportActionBar().show();
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        mSlidrInterface = Slidr.attach(this);
    }

    @Override
    protected void setupComponent(AppComponent appComponent) {

    }

    @Override
    public AppComponent component() {
        return null;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_comments, menu);
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
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
    public BasePreviewFragment providePreviewFragment() {
        if(isWebPreview(postItem)){
            return new WebPreviewFragment();
        }
        if(isImagePreview(postItem)){
            return new ImagePreviewFragment();
        }
        return new WebPreviewFragment();
    }

    @Override
    public Bundle provideFragmentBundle() {
        //add extra by identifying the type of the post
        Bundle bundle = new Bundle();
        bundle.putString(getResources().getString(R.string.main_data_key), new Gson().toJson(postItem));
        return bundle;
    }

    private boolean isImagePreview(PostItem postItem) {
        PostItem.Type type = postItem.getType();
        if(type == PostItem.Type.IMAGE)
            return true;
        else
            return false;
    }

    // TODO: 2016-01-06 For now, provide only WebPreview for anything that is NOT an image
    private boolean isWebPreview(PostItem postItem) {
        PostItem.Type type = postItem.getType();
        if(type != PostItem.Type.IMAGE)
            return true;
        else
            return false;
    }

    private void setToolbarTitle(int position){
        CharSequence title = commentsPagerAdapter.getPageTitle(position);
        getSupportActionBar().setTitle(title);
    }

}
