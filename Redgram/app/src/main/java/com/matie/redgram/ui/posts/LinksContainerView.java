package com.matie.redgram.ui.posts;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.facebook.binaryresource.BinaryResource;
import com.facebook.binaryresource.FileBinaryResource;
import com.facebook.cache.common.CacheKey;
import com.facebook.imagepipeline.cache.DefaultCacheKeyFactory;
import com.facebook.imagepipeline.core.ImagePipelineFactory;
import com.facebook.imagepipeline.request.ImageRequest;
import com.google.gson.Gson;
import com.matie.redgram.R;
import com.matie.redgram.data.managers.presenters.LinksPresenter;
import com.matie.redgram.data.managers.presenters.LinksPresenterImpl;
import com.matie.redgram.data.models.main.items.PostItem;
import com.matie.redgram.ui.App;
import com.matie.redgram.ui.common.base.BaseActivity;
import com.matie.redgram.ui.common.base.BaseFragment;
import com.matie.redgram.ui.common.base.Fragments;
import com.matie.redgram.ui.common.base.SlidingUpPanelActivity;
import com.matie.redgram.ui.common.utils.display.CoordinatorLayoutInterface;
import com.matie.redgram.ui.common.utils.widgets.DialogUtil;
import com.matie.redgram.ui.common.utils.widgets.LinksHelper;
import com.matie.redgram.ui.common.views.adapters.PostAdapterBase;
import com.matie.redgram.ui.common.views.widgets.postlist.PostRecyclerView;
import com.matie.redgram.ui.posts.views.LinksView;
import com.matie.redgram.ui.thread.views.CommentsActivity;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by matie on 2016-03-16.
 */
public class LinksContainerView extends FrameLayout implements LinksView {

    @InjectView(R.id.container_linear_layout)
    LinearLayout containerLinearLayout;
    @InjectView(R.id.container_recycler_view)
    PostRecyclerView containerRecyclerView;
    @InjectView(R.id.container_load_more_bar)
    ProgressBar containerProgressBar;

    //recycler view listeners to add.
    private RecyclerView.OnScrollListener loadMoreListener;
    private LinearLayoutManager mLayoutManager;
    private LinksComponent component;
    private final Context context;

    private String subredditChoice = null;
    private String filterChoice = null;
    private Map<String,String> params = new HashMap<>();
    private int hiddenItemPos = -1;

    @Inject
    App app;
    @Inject
    LinksPresenter linksPresenter;
    @Inject
    DialogUtil dialogUtil;


    public LinksContainerView(Context context) {
        super(context);
        this.context = context;
    }

    public LinksContainerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }

    public LinksContainerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
    }

    private void init(){
        filterChoice = getResources().getString(R.string.default_filter).toLowerCase();
        setupListeners();
        setupRecyclerView();
    }

    @Override
    public void onFinishInflate(){
        super.onFinishInflate();
        ButterKnife.inject(this);
        init();
    }


    public PostItem getItem(int position){
        return ((PostAdapterBase)containerRecyclerView.getAdapter()).getItem(position);
    }

    public List<PostItem> getItems(){
        return ((PostAdapterBase)containerRecyclerView.getAdapter()).getItems();
    }

    @Override
    public void updateList() {
        containerRecyclerView.getAdapter().notifyDataSetChanged();
    }

    @Override
    public void updateList(List<PostItem> items) {
        containerRecyclerView.replaceWith(items);
    }

    @Override
    public void refreshView() {
        linksPresenter.getListing(subredditChoice, filterChoice, params);
    }

    @Override
    public void refreshView(String subreddit, String filter, Map<String, String> urlParams) {
        //could be null
        this.subredditChoice = subreddit;

        if(filterChoice != null){
            this.filterChoice = filter;
        }
        if(params != null){
            this.params = urlParams;
        }
        linksPresenter.getListing(subredditChoice, filterChoice, params);
    }

    @Override
    public void search(String subredditChoice, Map<String, String> params) {
        this.filterChoice = null;
        this.subredditChoice = subredditChoice;
        if(params != null){
            this.params = params;
        }
        linksPresenter.searchListing(subredditChoice, params);
    }

    @Override
    public void sortView(@NonNull String filterChoice, @Nullable Map<String, String> params) {
        if(params == null){
            this.params.clear();
        }else{
            this.params = params;
        }

        this.filterChoice = filterChoice;
        linksPresenter.getListing(subredditChoice, filterChoice, this.params);
    }

    @Override
    public void votePost(int position, Integer dir) {
        linksPresenter.voteFor(position, getItem(position).getName(), dir);
    }

    @Override
    public void sharePost(int position) {
        PostItem item = getItem(position);
        MaterialDialog.ListCallback callback = LinksHelper.getShareCallback(context, item);
        LinksHelper.showExternalDialog(dialogUtil, "Share" ,callback);
    }

    @Override
    public void savePost(int position, boolean save) {
        linksPresenter.save(position, getItem(position).getName(), save);
    }

    @Override
    public void hidePost(int position) {
        linksPresenter.hide(position, getItem(position).getName(), true);
    }

    @Override
    public void reportPost(int position) {
        MaterialDialog.SingleButtonCallback callback = new MaterialDialog.SingleButtonCallback() {
            @Override
            public void onClick(@NonNull MaterialDialog materialDialog, @NonNull DialogAction dialogAction) {
                linksPresenter.report(position);
            }
        };
        LinksHelper.callReportDialog(dialogUtil, callback);
    }

    @Override
    public void deletePost(int position) {
        linksPresenter.delete(position);
    }

    @Override
    public void visitSubreddit(String subredditName) {
        filterChoice = getResources().getString(R.string.default_filter).toLowerCase();
        params.clear();
        subredditChoice = subredditName;
        LinksHelper.openResult(context, subredditName, LinksHelper.SUB);
    }

    @Override
    public void visitProfile(int position) {

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
    public void loadCommentsForPost(int position) {
        Intent intent = new Intent(context, CommentsActivity.class);
        String key = getResources().getString(R.string.main_data_key);
        String posKey = getResources().getString(R.string.main_data_position);
        intent.putExtra(key, new Gson().toJson(getItem(position)));
        intent.putExtra(posKey, position);
        ((BaseActivity)context).openIntentForResult(intent, CommentsActivity.REQ_CODE, R.anim.enter, R.anim.exit);
    }

    @Override
    public void viewWebMedia(int position) {
        Bundle bundle = new Bundle();
        String key = getResources().getString(R.string.main_data_key);
        bundle.putString(key, new Gson().toJson(getItem(position)));
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

                    bundle.putString(getResources().getString(R.string.main_data_key), new Gson().toJson(item));

                    ((SlidingUpPanelActivity)context).setPanelView(Fragments.IMAGE_PREVIEW, bundle);
                }
            }
        }
    }

    public LinearLayout getContainerLinearLayout() {
        return containerLinearLayout;
    }

    public PostRecyclerView getContainerRecyclerView() {
        return containerRecyclerView;
    }

    public ProgressBar getContainerProgressBar() {
        return containerProgressBar;
    }

    public RecyclerView.OnScrollListener getLoadMoreListener() {
        return loadMoreListener;
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
                        if(mLayoutManager.findLastCompletelyVisibleItemPosition() == lastItemPosition) {
                            linksPresenter.getMoreListing(subredditChoice, filterChoice , params);
                        }
                    }
                }
            }
        };

    }

    private void setupRecyclerView(){
        mLayoutManager = (LinearLayoutManager)containerRecyclerView.getLayoutManager();
        containerRecyclerView.addOnScrollListener(loadMoreListener);
        containerRecyclerView.setListener(this);
    }

    public void setComponent(LinksComponent component) {
        this.component = component;
        this.component.inject(this);
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

    @Override
    public BaseActivity getBaseActivity() {
        return null;
    }

    @Override
    public BaseFragment getBaseFragment() {
        return null;
    }

    public LinksPresenter getLinksPresenter() {
        return linksPresenter;
    }

    @Override
    public void showHideUndoOption() {
        if(getContext() instanceof CoordinatorLayoutInterface){
            String msg = getResources().getString(R.string.item_hidden);
            String actionMsg = getResources().getString(R.string.undo);
            View.OnClickListener onClickListener = new OnClickListener() {
                @Override
                public void onClick(View v) {
                    linksPresenter.unHide();
                }
            };

            ((CoordinatorLayoutInterface) getContext())
                .showSnackBar(msg, Snackbar.LENGTH_LONG, actionMsg, onClickListener, new UnHidePanelSnackBarCallback());
        }

    }

    public class UnHidePanelSnackBarCallback extends Snackbar.Callback{
        @Override
        public void onDismissed(Snackbar snackbar, int event) {

            ((LinksPresenterImpl)linksPresenter).setRemovedItem(null);
            ((LinksPresenterImpl)linksPresenter).setRemovedItemPosition(-1);

            super.onDismissed(snackbar, event);
            ((SlidingUpPanelActivity)getContext()).setPanelHeight(48);
        }
    }

}
