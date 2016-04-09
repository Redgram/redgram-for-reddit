package com.matie.redgram.ui.thread.views;


import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.Toolbar;


import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;

import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.facebook.drawee.view.SimpleDraweeView;
import com.google.gson.Gson;
import com.matie.redgram.R;
import com.matie.redgram.data.managers.media.video.ImageManager;
import com.matie.redgram.data.managers.presenters.ThreadPresenterImpl;
import com.matie.redgram.data.models.main.items.PostItem;
import com.matie.redgram.data.models.main.items.comment.CommentBaseItem;
import com.matie.redgram.ui.App;
import com.matie.redgram.ui.AppComponent;
import com.matie.redgram.ui.common.base.BaseFragment;
import com.matie.redgram.ui.common.utils.widgets.DialogUtil;
import com.matie.redgram.ui.common.utils.widgets.LinksHelper;
import com.matie.redgram.ui.thread.components.DaggerThreadComponent;
import com.matie.redgram.ui.thread.components.ThreadComponent;
import com.matie.redgram.ui.thread.modules.ThreadModule;
import com.matie.redgram.ui.thread.views.adapters.CommentsPagerAdapter;
import com.matie.redgram.ui.common.base.BaseActivity;
import com.matie.redgram.ui.common.previews.BasePreviewFragment;
import com.matie.redgram.ui.common.previews.ImagePreviewFragment;
import com.matie.redgram.ui.common.previews.WebPreviewFragment;
import com.matie.redgram.ui.thread.views.widgets.OptionsView;
import com.r0adkll.slidr.Slidr;
import com.r0adkll.slidr.model.SlidrInterface;

import java.util.List;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import butterknife.OnLongClick;

public class CommentsActivity extends BaseActivity implements ThreadView{

    public static final int REQ_CODE = 99;
    public static final String RESULT_POST_CHANGE = "result_post_change";
    public static final String RESULT_POST_POS = "post_position";

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
    @InjectView(R.id.upFab)
    FloatingActionButton upFab;
    @InjectView(R.id.downFab)
    FloatingActionButton downFab;
    @InjectView(R.id.image)
    SimpleDraweeView imageView;
    @InjectView(R.id.title)
    TextView title;
    @InjectView(R.id.details_card)
    OptionsView optionsView;
    @InjectView(R.id.comments_swipe_container)
    SwipeRefreshLayout commentsSwipeContainer;

    public CommentsView commentsView;
    private PostItem postItem;
    private boolean isSelf;
    private SlidrInterface mSlidrInterface;


    //dagger
    ThreadComponent threadComponent;

    @Inject
    App app;
    @Inject
    ThreadPresenterImpl threadPresenter;
    @Inject
    DialogUtil dialogUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.inject(this);

        if (getIntent() != null) {
            String key = getResources().getString(R.string.main_data_key);
            postItem = new Gson().fromJson(getIntent().getStringExtra(key), PostItem.class);
            isSelf = (postItem.getType() == PostItem.Type.SELF) ? true : false;
            title.setText(postItem.getTitle());
            //if position is passed, call other method with position as param
            optionsView.setup(postItem, this);
        }

        //this code causes the drawer to be drawn below the status bar as it clears FLAG_TRANSLUCENT_STATUS
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(getResources().getColor(R.color.material_blue_grey_900));
        }

        setupToolbar();
        setupViewPager();
        setupToolbarImage();
        setToolbarTitle(0);
        setupSwipeLayout();
        setupFabs();

        mSlidrInterface = Slidr.attach(this);
        threadPresenter.getThread(postItem.getId());
    }


    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        threadPresenter.registerForEvents();
    }

    @Override
    protected void onPause() {
        super.onPause();
        threadPresenter.unregisterForEvents();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.reset(this);
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

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void setupToolbar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setShowHideAnimationEnabled(true);

        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            FrameLayout toolbarContent = (FrameLayout) collapsingToolbarLayout.findViewById(R.id.toolbar_layout_content);

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (toolbarContent.getHeight() + verticalOffset < 2 * ViewCompat.getMinimumHeight(toolbarContent)) {
                    //gone
                    upFab.hide();
                    downFab.hide();
                    commentsFab.hide();
                } else {
                    //shown
                    upFab.show();
                    downFab.show();
                    commentsFab.show();
                }
            }
        });
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

    private void setupSwipeLayout() {
        commentsSwipeContainer.setEnabled(false);
        commentsSwipeContainer.setColorSchemeResources(android.R.color.holo_green_dark,
                android.R.color.holo_red_dark,
                android.R.color.holo_blue_dark,
                android.R.color.holo_orange_dark);

        TypedValue tv = new TypedValue();
        if (getContext().getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true))
        {
            int actionBarHeight = TypedValue.complexToDimensionPixelSize(tv.data,
                    getContext().getResources().getDisplayMetrics());
            //push it down to the same position as the first item to be loaded
            commentsSwipeContainer.setProgressViewOffset(false, 0 , actionBarHeight);
        }
    }

    @Override
    protected void setupComponent(AppComponent appComponent) {
        threadComponent = DaggerThreadComponent.builder()
                .appComponent(appComponent)
                .threadModule(new ThreadModule(this))
                .build();
        threadComponent.inject(this);
    }

    @Override
    public AppComponent component() {
        return threadComponent;
    }

    @Override
    protected BaseActivity activity() {
        return getBaseActivity();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_comments;
    }

    @Override
    protected int getContainerId() {
        return 0;
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
            setResult();
            super.onBackPressed();
            overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
        }
    }

    // set the changes before closing the activity
    private void setResult() {
        Intent resultIntent = new Intent();
        resultIntent.putExtra(RESULT_POST_CHANGE, new Gson().toJson(postItem));
        resultIntent.putExtra(RESULT_POST_POS, getIntent().getIntExtra(getResources().getString(R.string.main_data_position), -1));
        setResult(RESULT_OK, resultIntent);
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

    private void setupFabs() {
        if(TRUE.equalsIgnoreCase(postItem.getLikes())){
            upFab.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.material_green700)));
        }else if(FALSE.equalsIgnoreCase(postItem.getLikes())){
            downFab.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.material_red700)));
        }
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

    @OnClick(R.id.upFab)
    public void onUpVote(){
        if (!TRUE.equalsIgnoreCase(postItem.getLikes())){
            upFab.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.material_green700)));
            downFab.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.material_bluegrey900)));
            threadPresenter.vote(postItem, UP_VOTE);
        }else{
            upFab.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.material_bluegrey900)));
            threadPresenter.vote(postItem, UN_VOTE);
        }
    }

    @OnClick(R.id.downFab)
    public void onDownVote(){
        if (!FALSE.equalsIgnoreCase(postItem.getLikes())){
            downFab.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.material_red700)));
            upFab.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.material_bluegrey900)));
            threadPresenter.vote(postItem, DOWN_VOTE);
        }else{
            downFab.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.material_bluegrey900)));
            threadPresenter.vote(postItem, UN_VOTE);
        }
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

    @Override
    public void showLoading() {
        commentsSwipeContainer.post(new Runnable() {
            @Override
            public void run() {
                commentsSwipeContainer.setRefreshing(true);
            }
        });
    }

    @Override
    public void hideLoading() {
        commentsSwipeContainer.setRefreshing(false);
    }

    @Override
    public void showInfoMessage() {

    }

    @Override
    public void showErrorMessage(String error) {
        dialogUtil.build().title("Error Message").content(error).show();
    }

    @Override
    public void toggleVote(@Nullable int direction) {
        if(direction == UP_VOTE){
            upFab.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.material_green700)));
        }else if(direction == DOWN_VOTE){
            downFab.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.material_red700)));
        }else{
            downFab.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.material_bluegrey900)));
        }
    }

    @Override
    public void passDataToCommentsView(List<CommentBaseItem> commentItems) {
        commentsView.setItems(commentItems);
    }

    //presenter
    @Override
    public void savePost(){

    }
    //presenter
    @Override
    public void hidePost(){

    }

    @Override
    public void viewMedia() {

    }

    //options, presenter
    @Override
    public void reportPost(){

    }

    //options, presenter, oauth post
    @Override
    public void deletePost(){

    }

    @Override
    public void sharePost() {
        MaterialDialog.ListCallback callback = LinksHelper.getShareCallback(this, postItem);
        LinksHelper.showExternalDialog(dialogUtil, "Share" , callback);
    }
    @Override
    public void visitSubreddit() {
        LinksHelper.openResult(this, postItem.getSubreddit(), LinksHelper.SUB);
    }
    @Override
    public void visitProfile() {
        LinksHelper.openResult(this, postItem.getAuthor(), LinksHelper.PROFILE);
    }
    @Override
    public void openInBrowser() {
        MaterialDialog.ListCallback callback = LinksHelper.getBrowseCallback(this, postItem);
        LinksHelper.showExternalDialog(dialogUtil, "Open in Browser" ,callback);
    }
    @Override
    public void copyItemLink() {
        MaterialDialog.ListCallback callback = LinksHelper.getCopyCallback(this, app.getToastHandler(), postItem);
        LinksHelper.showExternalDialog(dialogUtil, "Copy" ,callback);
    }

    @Override
    public Context getContext() {
        return this.getApplicationContext();
    }

    @Override
    public BaseActivity getBaseActivity() {
        return this;
    }

    @Override
    public BaseFragment getBaseFragment() {
        return null;
    }


}
