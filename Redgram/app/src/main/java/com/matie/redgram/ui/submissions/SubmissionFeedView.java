package com.matie.redgram.ui.submissions;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.afollestad.materialdialogs.MaterialDialog;
import com.facebook.binaryresource.BinaryResource;
import com.facebook.binaryresource.FileBinaryResource;
import com.facebook.cache.common.CacheKey;
import com.facebook.imagepipeline.cache.DefaultCacheKeyFactory;
import com.facebook.imagepipeline.core.ImagePipelineFactory;
import com.facebook.imagepipeline.request.ImageRequest;
import com.matie.redgram.R;
import com.matie.redgram.data.managers.presenters.LinksPresenterImpl;
import com.matie.redgram.data.managers.presenters.SubmissionFeedPresenter;
import com.matie.redgram.data.models.db.Prefs;
import com.matie.redgram.data.models.main.items.PostItem;
import com.matie.redgram.ui.App;
import com.matie.redgram.ui.common.base.BaseActivity;
import com.matie.redgram.ui.common.base.BaseFragment;
import com.matie.redgram.ui.common.base.Fragments;
import com.matie.redgram.ui.common.base.SlidingUpPanelActivity;
import com.matie.redgram.ui.common.utils.display.CoordinatorLayoutInterface;
import com.matie.redgram.ui.common.utils.widgets.DialogUtil;
import com.matie.redgram.ui.common.utils.widgets.LinksHelper;
import com.matie.redgram.ui.common.views.widgets.postlist.PostRecyclerView;
import com.matie.redgram.ui.links.LinksComponent;
import com.matie.redgram.ui.submissions.links.LinksViewDelegate;
import com.matie.redgram.ui.submissions.views.LinksView;
import com.matie.redgram.ui.thread.ThreadActivity;

import java.io.File;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class SubmissionFeedView extends FrameLayout implements LinksView {

    @InjectView(R.id.container_linear_layout)
    LinearLayout containerLinearLayout;
    @InjectView(R.id.container_recycler_view)
    PostRecyclerView containerRecyclerView;
    @InjectView(R.id.container_load_more_bar)
    ProgressBar containerProgressBar;

    //recycler view listeners to add.
    private RecyclerView.OnScrollListener loadMoreListener;
    private LinearLayoutManager layoutManager;
    private LinksComponent component;
    String hostingFragmentTag;
    private final Context context;

    @Inject
    App app;
    @Inject
    LinksView linksViewDelegate;
    @Inject
    DialogUtil dialogUtil;


    public SubmissionFeedView(Context context) {
        super(context);
        this.context = context;
    }

    public SubmissionFeedView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }

    public SubmissionFeedView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
    }

    @Override
    public void onFinishInflate(){
        super.onFinishInflate();
        ButterKnife.inject(this);
        init();
    }

    private void init() {
        filterChoice = getResources().getString(R.string.default_filter).toLowerCase();
        setupListeners();
        setupRecyclerView();
        setupLinksDelegate();
    }

    private void setupLinksDelegate() {
        if (linksViewDelegate instanceof LinksViewDelegate) {
            ((LinksViewDelegate) linksViewDelegate).setContainerRecyclerView(containerRecyclerView);
            ((LinksViewDelegate) linksViewDelegate).setContainerProgressBar(containerProgressBar);
        }
    }

    public PostItem getItem(int position) {
        return linksViewDelegate.getItem(position);
    }

    public List<PostItem> getItems() {
        return linksViewDelegate.getItems();
    }

    @Override
    public void updateList() {
        linksViewDelegate.updateList();
    }

    @Override
    public void updateRestOfList(int position) {
        linksViewDelegate.updateRestOfList(position);
    }

    @Override
    public void updateItem(int position, PostItem postItem) {
        linksViewDelegate.updateItem(position, postItem);
    }

    @Override
    public PostItem removeItem(int position) {
        return linksViewDelegate.removeItem(position);
    }

    @Override
    public void insertItem(int position, PostItem removedPost) {
        linksViewDelegate.insertItem(position, removedPost);
    }

    @Override
    public void updateList(List<PostItem> items) {
        linksViewDelegate.updateList(items);
    }

    @Override
    public void refreshView() {
        linksViewDelegate.refreshView();
    }

    @Override
    public void refreshView(String subreddit, @NonNull String filter, @NonNull Map<String, String> urlParams) {
        linksViewDelegate.refreshView(subreddit, filter, urlParams);
    }

    @Override
    public void search(String subredditChoice, Map<String, String> urlParams) {
        linksViewDelegate.search(subredditChoice, urlParams);
    }

    @Override
    public void sortView(@NonNull String filterChoice, @Nullable Map<String, String> params) {
        linksViewDelegate.sortView(filterChoice, params);
    }

    @Override
    public void votePost(int position, Integer dir) {
        linksViewDelegate.votePost(position, dir);
    }

    @Override
    public void sharePost(final Context context, int position) {
        linksViewDelegate.sharePost(context, position);
    }

    @Override
    public void savePost(int position, boolean save) {
        linksViewDelegate.savePost(position, save);
    }

    @Override
    public void hidePost(int position) {
        linksViewDelegate.hidePost(position);
    }

    @Override
    public void reportPost(int position) {
       linksViewDelegate.reportPost(position);
    }

    @Override
    public void deletePost(int position) {
        linksViewDelegate.deletePost(position);
    }

    @Override
    public void visitSubreddit(String subredditName) {
        LinksHelper.openResult(context, subredditName, LinksHelper.SUB);
    }

    @Override
    public void visitProfile(String username) {
        LinksHelper.openResult(context, username, LinksHelper.PROFILE);
    }

    @Override
    public void openInBrowser(int position) {
        buildBrowserDialog(position);
    }

    @Override
    public void copyItemLink(int position) {
        buildCopyDialog(position);
    }

    private void buildBrowserDialog(int position) {
        PostItem item = getItem(position);
        MaterialDialog.ListCallback callback = LinksHelper.getBrowseCallback(context, item);
        LinksHelper.showExternalDialog(dialogUtil, "Open in Browser",callback);
    }

    private void buildCopyDialog(int position) {
        PostItem item = getItem(position);
        MaterialDialog.ListCallback callback = LinksHelper.getCopyCallback(context, app.getToastHandler(), item);
        LinksHelper.showExternalDialog(dialogUtil, "Copy" ,callback);
    }

    @Override
    public void loadCommentsForPost(final Context context, int position) {
        linksViewDelegate.loadCommentsForPost(context, position);
    }

    @Override
    public void viewWebMedia(int position) {
        Bundle bundle = new Bundle();
        String key = getResources().getString(R.string.main_data_key);
        bundle.putString(key, gson.toJson(getItem(position)));
        ((SlidingUpPanelActivity)context).setPanelView(Fragments.WEB_PREVIEW, bundle);
    }

    @Override
    public void viewImageMedia(int position, boolean loaded) {
        if(loaded){
            PostItem item = getItem(position);
            CacheKey cacheKey = DefaultCacheKeyFactory.getInstance()
                    .getEncodedCacheKey(ImageRequest
                            .fromUri(Uri.parse(item.getUrl())));
            if(cacheKey != null){
                BinaryResource resource = ImagePipelineFactory.getInstance().getMainDiskStorageCache().getResource(cacheKey);

                File localFile;
                if(resource != null){
                    localFile = ((FileBinaryResource) resource).getFile();

                    Bundle bundle = new Bundle();

                    bundle.putString(getResources().getString(R.string.local_cache_key), localFile.getPath());

                    bundle.putString(getResources().getString(R.string.main_data_key), gson.toJson(item));

                    ((SlidingUpPanelActivity)context).setPanelView(Fragments.IMAGE_PREVIEW, bundle);
                }
            }
        }
    }

    @Override
    public void callAgeConfirmDialog() {
        MaterialDialog.SingleButtonCallback callback = (materialDialog, dialogAction) -> {
            final Prefs prefs = getPrefs();
            if (!prefs.isOver18()) {
                submissionFeedPresenter.confirmAge();
            } else if (prefs.isDisableNsfwPreview()) {
                //change preferences
                submissionFeedPresenter.enableNsfwPreview();
            }
        };

        LinksHelper.callAgeConfirmDialog(dialogUtil, callback);
    }

    private void setupListeners() {
        //this listener is responsible for LOAD MORE. This listener object will be added on startup. Removed when
        // a network call is done and added again when the requested network call is either COMPLETED or had an ERROR.
        loadMoreListener = new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                if(newState == RecyclerView.SCROLL_STATE_IDLE){
                    if(containerRecyclerView != null && containerRecyclerView.getChildCount() > 0){
                        int lastItemPosition = containerRecyclerView.getAdapter().getItemCount() - 1;
                        if(layoutManager.findLastCompletelyVisibleItemPosition() == lastItemPosition) {
                            submissionFeedPresenter.getMoreListing(subredditChoice, filterChoice , params);
                        }
                    }
                }
            }
        };
    }

    private void setupRecyclerView(){
        containerRecyclerView.getItemAnimator().setChangeDuration(0);
        layoutManager = (LinearLayoutManager) containerRecyclerView.getLayoutManager();
        containerRecyclerView.addOnScrollListener(loadMoreListener);
//        containerRecyclerView.setListener(linksViewDelegate);
    }

    public void setComponent(LinksComponent component) {
        this.component = component;
//        this.component.inject(this);
    }

    public void setHostingFragmentTag(String hostingFragmentTag) {
        this.hostingFragmentTag = hostingFragmentTag;
    }

    public String getHostingFragmentTag() {
        if(hostingFragmentTag != null){
            return hostingFragmentTag;
        }
        return "";
    }

    @Override
    public void showLoading() {
        containerRecyclerView.removeOnScrollListener(loadMoreListener);
        containerProgressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoading() {
        containerRecyclerView.addOnScrollListener(loadMoreListener);
        containerProgressBar.setVisibility(View.GONE);
    }

    @Override
    public void showInfoMessage() {

    }

    @Override
    public void showErrorMessage(String error) {

    }

    public SubmissionFeedPresenter getSubmissionFeedPresenter() {
        return submissionFeedPresenter;
    }

    @Override
    public void showHideUndoOption() {
        if(getContext() instanceof CoordinatorLayoutInterface){
            String msg = getResources().getString(R.string.item_hidden);
            String actionMsg = getResources().getString(R.string.undo);
            View.OnClickListener onClickListener = v -> submissionFeedPresenter.unHide();

            ((CoordinatorLayoutInterface) getContext())
                    .showSnackBar(msg, Snackbar.LENGTH_LONG, actionMsg, onClickListener, null);
        }

    }

    public void setLoadMoreId(String id){
        ((LinksPresenterImpl) submissionFeedPresenter).setLoadMoreId(id);
    }

    private Prefs getPrefs(){
        return submissionFeedPresenter.databaseManager().getSessionPreferences();
    }

}
