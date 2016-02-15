package com.matie.redgram.ui.common.previews;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.matie.redgram.R;
import com.matie.redgram.data.managers.presenters.CommentsPresenterImpl;
import com.matie.redgram.data.models.main.items.PostItem;
import com.matie.redgram.data.models.main.items.comment.CommentBaseItem;
import com.matie.redgram.data.models.main.items.comment.CommentItem;
import com.matie.redgram.data.models.main.items.comment.CommentMoreItem;
import com.matie.redgram.ui.App;
import com.matie.redgram.ui.AppComponent;
import com.matie.redgram.ui.common.base.BaseActivity;
import com.matie.redgram.ui.common.utils.widgets.DialogUtil;
import com.matie.redgram.ui.common.utils.widgets.ToastHandler;
import com.matie.redgram.ui.thread.components.CommentsComponent;
import com.matie.redgram.ui.thread.components.DaggerCommentsComponent;
import com.matie.redgram.ui.thread.components.ThreadComponent;
import com.matie.redgram.ui.thread.modules.CommentsModule;
import com.matie.redgram.ui.thread.views.CommentsActivity;
import com.matie.redgram.ui.thread.views.CommentsView;
import com.matie.redgram.ui.thread.views.widgets.comment.CommentBaseItemView;
import com.matie.redgram.ui.thread.views.widgets.comment.CommentItemView;
import com.matie.redgram.ui.thread.views.widgets.comment.CommentRecyclerView;
import com.matie.redgram.ui.thread.views.widgets.comment.CommentViewHolder;

import java.util.List;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by matie on 2016-01-07.
 */
public class CommentsPreviewFragment extends BasePreviewFragment implements CommentsView, CommentViewHolder.CommentListener{

    @InjectView(R.id.comment_recycler_view)
    CommentRecyclerView commentRecyclerView;

    CommentsComponent component;

    @Inject
    App app;
    @Inject
    CommentsPresenterImpl commentsPresenter;
    @Inject
    DialogUtil dialogUtil;

    List<CommentBaseItem> commentItems;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.preview_comments_fragment, container, false);
        ButterKnife.inject(this, view);

        // TODO: 2016-02-15 clean up?
        String json = getArguments().getString(getMainKey());
//        String title = (new Gson().fromJson(json, PostItem.class)).getTitle();
//        titleView.setText(title);

        commentRecyclerView.setAdapterListener(this);
        return view;
    }

    @Override public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    protected void setupComponent() {
        AppComponent appComponent = ((BaseActivity)getActivity()).component();
        ThreadComponent threadComponent = (ThreadComponent)appComponent;
        component = DaggerCommentsComponent.builder()
                .threadComponent(threadComponent)
                .commentsModule(new CommentsModule(this))
                .build();

        component.inject(this);
    }

    @Override
    protected void setupToolbar() {
        return;
    }

    @Override
    public void refreshPreview(Bundle bundle) {
        return;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }


    public void refreshComments(List<CommentBaseItem> items){
        setComments(items);
        //refresh adapter
        commentRecyclerView.replaceWith(commentItems);
    }

    public void setComments(List<CommentBaseItem> items){
        this.commentItems = items;
    }

    @Override
    public void expandItem(int position) {

    }

    @Override
    public void collapseItem(int position) {

    }

    @Override
    public void expandAll(int position) {

    }

    @Override
    public void collapseAll(int position) {

    }

    @Override
    public void hideItem(int position) {

    }

    @Override
    public void scrollTo(int position) {

    }

    @Override
    public Fragment getFragment() {
        return this;
    }

    @Override
    public void onClick(CommentBaseItem item) {
//        if(item instanceof CommentItem){
//            app.getToastHandler().showToast(((CommentItem) item).getAuthor(), Toast.LENGTH_SHORT);
//        }
//        if(item instanceof CommentMoreItem){
//            app.getToastHandler().showToast(((CommentMoreItem) item).getCount()+"", Toast.LENGTH_SHORT);
//        }
    }

    @Override
    public void onLongClick(CommentBaseItem item) {
//        if(item instanceof CommentItem){
//            app.getToastHandler().showToast(item.getLevel()+" - Reg", Toast.LENGTH_SHORT);
//        }
//        if(item instanceof CommentMoreItem){
//            app.getToastHandler().showToast(item.getLevel()+" - More", Toast.LENGTH_SHORT);
//        }
    }
}
