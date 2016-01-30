package com.matie.redgram.ui.comments.views;


import android.content.Context;
import android.os.Build;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.Toolbar;


import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;

import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.google.gson.Gson;
import com.matie.redgram.R;
import com.matie.redgram.data.managers.media.images.ImageManager;
import com.matie.redgram.data.models.main.items.PostItem;
import com.matie.redgram.ui.AppComponent;
import com.matie.redgram.ui.comments.views.adapters.CommentsPagerAdapter;
import com.matie.redgram.ui.common.base.BaseActivity;
import com.matie.redgram.ui.common.previews.BasePreviewFragment;
import com.matie.redgram.ui.common.previews.ImagePreviewFragment;
import com.matie.redgram.ui.common.previews.WebPreviewFragment;
import com.r0adkll.slidr.Slidr;
import com.r0adkll.slidr.model.SlidrInterface;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import butterknife.OnLongClick;

public class CommentsActivity extends BaseActivity {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private CommentsPagerAdapter commentsPagerAdapter;

    @InjectView(R.id.app_bar)
    AppBarLayout appBarLayout;
    @InjectView(R.id.toolbar_layout)
    CollapsingToolbarLayout collapsingToolbarLayout;
    @InjectView(R.id.container)
    ViewPager mViewPager;
    @InjectView(R.id.toolbar)
    Toolbar toolbar;
    @InjectView(R.id.commentsFab)
    FloatingActionButton commentsFab;
    @InjectView(R.id.image)
    SimpleDraweeView imageView;
    @InjectView(R.id.title)
    TextView title;

    private PostItem postItem;
    private boolean isSelf;
    private SlidrInterface mSlidrInterface;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comments);
        ButterKnife.inject(this);

        if(getIntent() != null){
            String key = getResources().getString(R.string.main_data_key);
            postItem = new Gson().fromJson(getIntent().getStringExtra(key), PostItem.class);
            isSelf = (postItem.getType() == PostItem.Type.SELF) ? true : false;
            title.setText(postItem.getTitle());
        }

        //this code causes the drawer to be drawn below the status bar as it clears FLAG_TRANSLUCENT_STATUS
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(getResources().getColor(R.color.material_red600));
        }

        setupToolbar();
        setupViewPager();
        setupToolbarImage();
        setToolbarTitle(0);

//        resolveAppBarHeight();
        mSlidrInterface = Slidr.attach(this);
    }

    private void setupToolbarImage() {
        ImageManager.SingleImageBuilder builder = ImageManager.newImageBuilder(this)
                .setImageView(imageView)
                .includeOldController();

        if(!postItem.getThumbnail().isEmpty() && postItem.getThumbnail().length() > 0 && !isSelf){
            builder.setThumbnail(postItem.getThumbnail());
        }else{
            builder.setImageFromRes(R.drawable.reddit_nf_cropped, false);
        }

        builder.build();
    }

    private void setupToolbar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setShowHideAnimationEnabled(true);
    }

    private void setupViewPager() {
        commentsPagerAdapter = new CommentsPagerAdapter(getSupportFragmentManager(), provideFragmentBundle(), isSelf);
        mViewPager.setAdapter(commentsPagerAdapter);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position > 0) {
                    appBarLayout.setExpanded(false, true);
                    mSlidrInterface.lock();
                } else {
                    appBarLayout.setExpanded(true, true);
                    mSlidrInterface.unlock();
                }
                setToolbarTitle(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
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
        if(isSelf && mViewPager.getCurrentItem() > 0){
            mViewPager.setCurrentItem(0, true);
        }else{
            super.onBackPressed();
            overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
        }
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

    public BasePreviewFragment providePreviewFragment() {
        if(isWebPreview(postItem)){
            return new WebPreviewFragment();
        }
        if(isImagePreview(postItem)){
            return new ImagePreviewFragment();
        }
        return new WebPreviewFragment();
    }

    private Bundle provideFragmentBundle() {
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

    private void setToolbarTitle(int position) {
        getSupportActionBar().setTitle(commentsPagerAdapter.getPageTitle(position));
    }

    @OnClick(R.id.commentsFab)
    public void onCommentsClick(){
        onCommentsFabClicked(false);
    }

    @OnLongClick(R.id.commentsFab)
    public boolean onCommentsLongClick(){
        onCommentsFabClicked(true);
        return true;
    }

    private void onCommentsFabClicked(boolean longClick) {
        if(longClick){
            onCommentsLongClicked();
        }else{
            onCommentsClicked();
        }

    }

    private void onCommentsLongClicked() {
        // TODO: 2016-01-27 list logged in user comments in the thread if any, if user already in comments, skip scrolling
    }

    private void onCommentsClicked() {
        if(isSelf){
            //if already in comments section, could never happen since it is collapsed automatically
            if(mViewPager.getCurrentItem() == 1){
                // TODO: 2016-01-27 focus on comments edit box
            }else{
                //go to comments
                mViewPager.setCurrentItem(1, true);
            }

        }else{
            //focus on edit text
        }
    }

//    private void resolveAppBarHeight() {
//        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
//        float dpWidth = displayMetrics.widthPixels / displayMetrics.density;
//
//        int titleHeight = getTextViewHeight(title, (int)dpWidth);
//
//        CoordinatorLayout.LayoutParams lp = (CoordinatorLayout.LayoutParams)appBarLayout.getLayoutParams();
//        lp.height += (int)titleHeight;
//    }
//
//    public static int getTextViewHeight(TextView textView , int deviceWidth) {
//        int widthMeasureSpec = View.MeasureSpec.makeMeasureSpec(deviceWidth, View.MeasureSpec.AT_MOST);
//        int heightMeasureSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
//        textView.measure(widthMeasureSpec, heightMeasureSpec);
//        return textView.getMeasuredHeight();
//    }
}
