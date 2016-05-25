package com.matie.redgram.ui.common.previews;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.matie.redgram.R;
import com.matie.redgram.data.managers.presenters.CommentsPresenterImpl;
import com.matie.redgram.data.models.main.items.comment.CommentBaseItem;
import com.matie.redgram.data.models.main.items.comment.CommentItem;
import com.matie.redgram.data.models.main.items.comment.CommentMoreItem;
import com.matie.redgram.ui.App;
import com.matie.redgram.ui.AppComponent;
import com.matie.redgram.ui.common.base.BaseActivity;
import com.matie.redgram.ui.common.base.BaseFragment;
import com.matie.redgram.ui.common.utils.widgets.DialogUtil;
import com.matie.redgram.ui.thread.components.CommentsComponent;
import com.matie.redgram.ui.thread.components.DaggerCommentsComponent;
import com.matie.redgram.ui.thread.components.ThreadComponent;
import com.matie.redgram.ui.thread.modules.CommentsModule;
import com.matie.redgram.ui.thread.views.CommentsActivity;
import com.matie.redgram.ui.thread.views.CommentsView;
import com.matie.redgram.ui.thread.views.adapters.CommentsAdapter;
import com.matie.redgram.ui.thread.views.widgets.comment.CommentRecyclerView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by matie on 2016-01-07.
 */
public class CommentsPreviewFragment extends BasePreviewFragment implements CommentsView{

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
    Map<String, List<CommentBaseItem>> collapsedComments = new HashMap<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.preview_comments_fragment, container, false);
        ButterKnife.inject(this, view);

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

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        ((CommentsActivity)context).commentsView = this;
    }

    private void refreshComments(List<CommentBaseItem> items){
        setComments(items);
        //refresh adapter
        commentRecyclerView.replaceWith(commentItems);
    }

    public void setComments(List<CommentBaseItem> items){
        this.commentItems = items;
    }

    @Override
    public void expandItem(int position) {
        CommentItem item = (CommentItem)getItem(position);
        if(item.hasReplies() && !item.isExpanded()){
            item.setIsExpanded(true);
            int start = position+1;
            int count = (position+item.getChildCount());
            getCommentItems().addAll(start, collapsedComments.get(item.getId()));
            collapsedComments.remove(item.getId());
            commentRecyclerView.getAdapter().notifyItemRangeInserted(start, count);
            commentRecyclerView.getAdapter().notifyItemChanged(position);
        }else{
            app.getToastHandler().showToast("do nothing", Toast.LENGTH_SHORT);
        }
    }

    @Override
    public void collapseItem(int position) {
        CommentItem item = (CommentItem)getItem(position);
        if(item.hasReplies() && item.isExpanded()){
            item.setIsExpanded(false);
            int start = position + 1;
            int count = (position+item.getChildCount());
            List<CommentBaseItem> targetItems = new ArrayList<>();
            for(int i = start; (i <= count && (getItem(i).getLevel() > item.getLevel())); i++){
                targetItems.add(getItem(i));
            }
            //add removed items into map
            collapsedComments.put(item.getId(), targetItems);
            //remove items and notify adapter
            getCommentItems().removeAll(targetItems);
            commentRecyclerView.getAdapter().notifyItemRangeRemoved(start, count);
            commentRecyclerView.getAdapter().notifyItemChanged(position);
        }else{
            app.getToastHandler().showToast("do nothing", Toast.LENGTH_SHORT);
        }
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
    public void loadMore(int position) {
        if(commentItems.get(position) instanceof CommentMoreItem){
            app.getToastHandler().showToast("load more", Toast.LENGTH_SHORT);
        }
    }

    @Override
    public BaseActivity getBaseActivity() {
        return (BaseActivity)getActivity();
    }

    @Override
    public BaseFragment getBaseFragment() {
        return this;
    }

    @Override
    public void setItems(List<CommentBaseItem> items) {
        refreshComments(items);
    }

    private CommentBaseItem getItem(int position){
        return ((CommentsAdapter)commentRecyclerView.getAdapter()).getItem(position);
    }
    private List<CommentBaseItem> getCommentItems(){
        return ((CommentsAdapter)commentRecyclerView.getAdapter()).getCommentItems();
    }

}
