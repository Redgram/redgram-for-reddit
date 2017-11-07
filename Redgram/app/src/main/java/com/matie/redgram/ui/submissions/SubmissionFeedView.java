package com.matie.redgram.ui.submissions;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.matie.redgram.R;
import com.matie.redgram.data.models.main.items.PostItem;
import com.matie.redgram.ui.App;
import com.matie.redgram.ui.common.utils.widgets.DialogUtil;
import com.matie.redgram.ui.common.views.widgets.postlist.PostRecyclerView;
import com.matie.redgram.ui.submissions.links.LinksViewDelegate;
import com.matie.redgram.ui.submissions.views.LinksView;

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

    private LinksView linksViewDelegate;

    @Inject
    App app;
    @Inject
    DialogUtil dialogUtil;


    public SubmissionFeedView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setLinksViewDelegate(LinksView delegate) {
        this.linksViewDelegate = delegate;
    }

    @Override
    public void onFinishInflate() {
        super.onFinishInflate();
        ButterKnife.inject(this);
        init();
    }

    private void init() {
        setupLinksDelegate();
        fetchLinks();
    }

    private void fetchLinks() {
        if (linksViewDelegate instanceof LinksViewDelegate) {
            ((LinksViewDelegate) linksViewDelegate).fetchLinks(getContext());
        }
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
    public void reportPost(final Context context, int position) {
       linksViewDelegate.reportPost(context, position);
    }

    @Override
    public void deletePost(final Context context, int position) {
        linksViewDelegate.deletePost(context, position);
    }

    @Override
    public void visitSubreddit(final Context context, String subredditName) {
        linksViewDelegate.visitSubreddit(context, subredditName);
    }

    @Override
    public void visitProfile(final Context context, String username) {
        linksViewDelegate.visitProfile(context, username);
    }

    @Override
    public void openInBrowser(final Context context, int position) {
        linksViewDelegate.openInBrowser(context, position);
    }

    @Override
    public void copyItemLink(final Context context, int position){
        linksViewDelegate.copyItemLink(context, position);
    }

    @Override
    public void loadCommentsForPost(final Context context, int position) {
        linksViewDelegate.loadCommentsForPost(context, position);
    }

    @Override
    public void viewWebMedia(final Context context, int position) {
        linksViewDelegate.viewWebMedia(context, position);
    }

    @Override
    public void viewImageMedia(final Context context, int position, boolean loaded) {
        linksViewDelegate.viewImageMedia(context, position, loaded);
    }

    @Override
    public void callAgeConfirmDialog(final Context context) {
        linksViewDelegate.callAgeConfirmDialog(context);
    }

    @Override
    public void showLoading() {
        linksViewDelegate.showLoading();
    }

    @Override
    public void hideLoading() {
       linksViewDelegate.hideLoading();
    }

    @Override
    public void showInfoMessage() {
        linksViewDelegate.showInfoMessage();
    }

    @Override
    public void showErrorMessage(String error) {
        linksViewDelegate.showErrorMessage(error);
    }

    @Override
    public void showHideUndoOption(final Context context) {
        linksViewDelegate.showHideUndoOption(context);
    }

}
