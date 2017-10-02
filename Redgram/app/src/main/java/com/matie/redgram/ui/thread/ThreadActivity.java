package com.matie.redgram.ui.thread;


import android.annotation.TargetApi;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
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
import com.matie.redgram.ui.common.base.ViewPagerActivity;
import com.matie.redgram.ui.common.utils.display.CoordinatorLayoutInterface;
import com.matie.redgram.ui.common.utils.widgets.DialogUtil;
import com.matie.redgram.ui.common.utils.widgets.LinksHelper;
import com.matie.redgram.ui.common.views.BaseContextView;
import com.matie.redgram.ui.common.views.adapters.SectionsPagerAdapter;
import com.matie.redgram.ui.thread.components.DaggerThreadComponent;
import com.matie.redgram.ui.thread.components.ThreadComponent;
import com.matie.redgram.ui.thread.modules.ThreadModule;
import com.matie.redgram.ui.thread.views.CommentsView;
import com.matie.redgram.ui.thread.views.ThreadView;
import com.matie.redgram.ui.thread.views.adapters.CommentsPagerAdapter;
import com.matie.redgram.ui.thread.views.widgets.OptionsView;
import com.r0adkll.slidr.Slidr;
import com.r0adkll.slidr.model.SlidrConfig;
import com.r0adkll.slidr.model.SlidrInterface;
import com.r0adkll.slidr.model.SlidrListener;

import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import butterknife.OnLongClick;
import io.realm.RealmChangeListener;

public class ThreadActivity extends ViewPagerActivity implements ThreadView, CoordinatorLayoutInterface{

    public static final int REQ_CODE = 99;
    public static final String RESULT_POST_CHANGE = "result_post_change";
    public static final String RESULT_POST_POS = "post_position";

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

        //this code causes the drawer to be drawn below the status bar as it clears FLAG_TRANSLUCENT_STATUS
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(getResources().getColor(R.color.material_blue_grey_900));
        }

        setupToolbarImage();
        setupSwipeLayout();
        setupFabs();

        SlidrConfig config = new SlidrConfig.Builder().listener(new SlidrListener() {
            @Override
            public void onSlideStateChanged(int state) {
            }

            @Override
            public void onSlideChange(float percent) {

            }

            @Override
            public void onSlideOpened() {

            }

            @Override
            public void onSlideClosed() {
                setResult();
            }
        }).build();

        mSlidrInterface = Slidr.attach(this, config);
        threadPresenter.getThread(postItem.getId(), new HashMap<>());

        title.setText(postItem.getTitle());
        // if position is passed, call other method with position as param
        optionsView.setup(postItem, this);
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
    protected void onDestroy() {
        threadPresenter.unregisterForEvents();
        ButterKnife.reset(this);
        super.onDestroy();
    }

    private void setupToolbarImage() {
        ImageManager.SingleImageBuilder builder = ImageManager.newImageBuilder(this)
                .setImageView(imageView)
                .includeOldController();

        if(!postItem.getThumbnail().isEmpty() && !isSelf){
            builder.setThumbnail(postItem.getThumbnail());
        }else{
            builder.setImageFromRes(R.drawable.reddit_nf_cropped, false);
        }

        builder.build();
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    protected void setupToolbar() {
        super.setupToolbar();
        getAppBarLayout().addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            FrameLayout toolbarContent = (FrameLayout) getCollapsingToolbarLayout().findViewById(R.id.toolbar_layout_content);

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

    @Override
    protected void setupViewPager() {
        super.setupViewPager();
        getViewPager().addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position > 0) {
                    getAppBarLayout().setExpanded(false, true);
                    mSlidrInterface.lock();
                } else {
                    getAppBarLayout().setExpanded(true, true);
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
    protected void checkIntent() {
        String key = getResources().getString(R.string.main_data_key);
        String data = getIntent().getStringExtra(key);
        if(data != null){
            postItem = new Gson().fromJson(data, PostItem.class);
            isSelf = postItem.getType() == PostItem.Type.SELF;
        }
    }

    @Override
    protected int getInitialPagerPosition() {
        return 0;
    }

    @Override
    protected SectionsPagerAdapter pagerAdapterInstance() {
        return new CommentsPagerAdapter(getSupportFragmentManager(), provideFragmentBundle(), isSelf);
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
    public DialogUtil getDialogUtil() {
        return dialogUtil;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_comments;
    }

    @Override
    protected int getContainerId() {
        return R.id.container;
    }

    @Override
    protected RealmChangeListener getRealmSessionChangeListener() {
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
        if(isSelf && getViewPager().getCurrentItem() > 0){
            getViewPager().setCurrentItem(0, true);
        }else{
            setResult();
            super.onBackPressed();
        }
    }

    // set the changes before closing the activity
    // TODO: 2016-04-17 move this to post fragment
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

    // TODO: 2016-01-06 For now, provide only WebPreview for anything that is NOT an image
    private boolean isWebPreview(PostItem postItem) {
        PostItem.Type type = postItem.getType();
        if(type != PostItem.Type.IMAGE)
            return true;
        else
            return false;
    }

    private void setupFabs() {
        if(TRUE.equalsIgnoreCase(postItem.getLikes())){
            upFab.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.material_green700)));
        }else if(FALSE.equalsIgnoreCase(postItem.getLikes())){
            downFab.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.material_red700)));
        }else{
            upFab.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.material_bluegrey900)));
            downFab.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.material_bluegrey900)));
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
            threadPresenter.vote(postItem, UP_VOTE);
        }else{
            threadPresenter.vote(postItem, UN_VOTE);
        }
    }

    @OnClick(R.id.downFab)
    public void onDownVote(){
        if (!FALSE.equalsIgnoreCase(postItem.getLikes())){
            threadPresenter.vote(postItem, DOWN_VOTE);
        }else{
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
            if(getViewPager().getCurrentItem() == 1){
                // TODO: 2016-01-27 focus on comments edit box
            }else{
                //go to comments
                getViewPager().setCurrentItem(1, true);
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
    public BaseContextView getContentContext() {
        return getBaseActivity();
    }

    @Override
    public void toggleVote(@Nullable int direction) {
        setupFabs();
    }

    @Override
    public void toggleSave(boolean save) {
        optionsView.toggleSave(save);
    }

    @Override
    public void toggleUnHide() {
        String msg = getResources().getString(R.string.item_hidden);
        String actionMsg = getResources().getString(R.string.undo);
        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                threadPresenter.hide(postItem, false);
            }
        };
        showSnackBar(msg, Snackbar.LENGTH_LONG, actionMsg, onClickListener, null);
    }

    @Override
    public void passDataToCommentsView(List<CommentBaseItem> commentItems) {
        commentsView.setItems(commentItems);
    }

    //presenter
    @Override
    public void savePost(){
        if(postItem.isSaved()){
            threadPresenter.save(postItem, false);
        }else{
            threadPresenter.save(postItem, true);
        }
    }

    //presenter
    @Override
    public void hidePost(){
        threadPresenter.hide(postItem, true);
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
    public CoordinatorLayout coordinatorLayout() {
        return getCoordinatorLayout();
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
            }
            //hide the panel before showing the snack bar
            snackbar.show();
        }
    }
}
