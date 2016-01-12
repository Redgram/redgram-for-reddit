package com.matie.redgram.ui.comments.views;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.graphics.Color;
import android.os.Build;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
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

import android.view.ViewPropertyAnimator;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
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
import com.matie.redgram.ui.common.utils.display.reveal.AnimatorPath;
import com.matie.redgram.ui.common.utils.display.reveal.PathEvaluator;
import com.matie.redgram.ui.common.utils.display.reveal.PathPoint;
import com.r0adkll.slidr.Slidr;
import com.r0adkll.slidr.model.SlidrInterface;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class CommentsActivity extends BaseActivity implements CommentsPagerAdapter.CommentsPreviewDetector {

    public final static float SCALE_FACTOR      = 13f;
    public final static int ANIMATION_DURATION  = 300;
    public final static int MINIMUN_X_DISTANCE  = 380;

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
    @InjectView(R.id.container)
    ViewPager mViewPager;
    @InjectView(R.id.tabs)
    TabLayout mTabLayout;
    @InjectView(R.id.toolbar)
    Toolbar toolbar;
    @InjectView(R.id.fabContainer)
    FrameLayout fabContainer;
    @InjectView(R.id.fabActionContainer)
    LinearLayout fabActionContainer;
    @InjectView(R.id.fab)
    ImageButton mFab;

    private PostItem postItem;
    private SlidrInterface mSlidrInterface;
    private boolean mRevealFlag;
    private float mFabSize;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comments);
        ButterKnife.inject(this);

        if(getIntent() != null){
            String key = getResources().getString(R.string.main_data_key);
            postItem = new Gson().fromJson(getIntent().getStringExtra(key), PostItem.class);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(getResources().getColor(R.color.material_red600));}

        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setShowHideAnimationEnabled(true);

//        fabActionContainer.setVisibility(View.GONE);

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        commentsPagerAdapter = new CommentsPagerAdapter(getSupportFragmentManager(), this);

        // Set up the ViewPager with the sections adapter.
        mViewPager.setAdapter(commentsPagerAdapter);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position > 0) {
                    mSlidrInterface.lock();
                    getSupportActionBar().hide();
                } else {
                    mSlidrInterface.unlock();
                    getSupportActionBar().show();
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        mTabLayout.setupWithViewPager(mViewPager);

        mFabSize = getResources().getDimensionPixelSize(R.dimen.fab_size);

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


    private void onFabClicked() {

        final float startX = mFab.getX();

        AnimatorPath path = new AnimatorPath();
        path.moveTo(0, 0);
        path.lineTo(-450, 0);

        final ObjectAnimator anim = ObjectAnimator.ofObject(this, "fabLoc",
                new PathEvaluator(), path.getPoints().toArray());

        anim.setInterpolator(new AccelerateInterpolator());
        anim.setDuration(ANIMATION_DURATION);
        anim.start();

        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                if (Math.abs(startX - mFab.getX()) > 300) {
                    mFab.setImageResource(R.color.transparent);
                }
                if (Math.abs(startX - mFab.getX()) > MINIMUN_X_DISTANCE) {
                    if (!mRevealFlag) {

                        mFab.animate()
                                .scaleXBy(SCALE_FACTOR)
                                .scaleYBy(SCALE_FACTOR)
                                .setListener(mEndRevealListener)
                                .setDuration(ANIMATION_DURATION);

                        mRevealFlag = true;
                    }
                }
            }
        });
    }

    private AnimatorListenerAdapter mEndRevealListener = new AnimatorListenerAdapter() {

        @Override
        public void onAnimationEnd(Animator animation) {
            super.onAnimationEnd(animation);

            mFab.setVisibility(View.INVISIBLE);
            fabContainer.setBackgroundColor(getResources()
                    .getColor(R.color.material_red400));

            for (int i = 0; i < fabActionContainer.getChildCount(); i++) {
                View v = fabActionContainer.getChildAt(i);
                ViewPropertyAnimator animator = v.animate()
                        .scaleX(1).scaleY(1)
                        .setDuration(ANIMATION_DURATION);

                animator.setStartDelay(i * 50);
                animator.start();
            }
        }
    };

    /**
     * We need this setter to translate between the information the animator
     * produces (a new "PathPoint" describing the current animated location)
     * and the information that the button requires (an xy location). The
     * setter will be called by the ObjectAnimator given the 'fabLoc'
     * property string.
     */
    public void setFabLoc(PathPoint newLoc) {
        mFab.setTranslationX(newLoc.mX);

        if (mRevealFlag)
            mFab.setTranslationY(newLoc.mY - (mFabSize / 2));
        else
            mFab.setTranslationY(newLoc.mY);
    }

    private void hideContainer() {
        final float startX = mFab.getX();

        AnimatorPath path = new AnimatorPath();
        path.moveTo(-450, 0);
        path.lineTo(0, 0);

        final ObjectAnimator anim = ObjectAnimator.ofObject(this, "fabLoc",
                new PathEvaluator(), path.getPoints().toArray());

        anim.setInterpolator(new DecelerateInterpolator());
        anim.setDuration(ANIMATION_DURATION);
        anim.start();

        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                if (Math.abs(startX - mFab.getX()) > 300) {
                    mFab.setImageResource(R.drawable.ic_action_clear);
                }
                if (Math.abs(startX - mFab.getX()) < MINIMUN_X_DISTANCE) {
                    if (mRevealFlag) {

                        mFab.animate()
                                .scaleXBy(-SCALE_FACTOR)
                                .scaleYBy(-SCALE_FACTOR)
                                .setListener(mStartHideListener)
                                .setDuration(ANIMATION_DURATION);

                        mRevealFlag = false;
                    }
                }
            }
        });
    }

    private AnimatorListenerAdapter mStartHideListener = new AnimatorListenerAdapter() {
        @Override
        public void onAnimationStart(Animator animation) {
            super.onAnimationStart(animation);

            for (int i = 0; i < fabActionContainer.getChildCount(); i++) {
                View v = fabActionContainer.getChildAt(i);
                ViewPropertyAnimator animator = v.animate()
                        .scaleX(0).scaleY(0)
                        .setDuration(ANIMATION_DURATION/2);

                animator.setStartDelay(ANIMATION_DURATION/4);
                animator.start();
            }

            fabContainer.setBackgroundColor(getResources()
                    .getColor(R.color.transparent));
            mFab.setVisibility(View.VISIBLE);

        }
    };

    @OnClick(R.id.toolbar)
    public void onContainerClick(){
        hideContainer();
    }

    @OnClick(R.id.fab)
    public void onFabClick(){
        onFabClicked();
    }

}
