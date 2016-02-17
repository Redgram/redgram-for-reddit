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
import com.matie.redgram.ui.thread.views.adapters.CommentsAdapter;
import com.matie.redgram.ui.thread.views.widgets.comment.CommentBaseItemView;
import com.matie.redgram.ui.thread.views.widgets.comment.CommentItemView;
import com.matie.redgram.ui.thread.views.widgets.comment.CommentRecyclerView;
import com.matie.redgram.ui.thread.views.widgets.comment.CommentRegularItemView;
import com.matie.redgram.ui.thread.views.widgets.comment.CommentViewHolder;

import java.util.List;

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
        CommentItem item = (CommentItem)getItem(position);
        if(item.hasReplies() && !item.isExpanded()){
            item.setIsExpanded(true);
            int count = (position+item.getChildCount());
            for(int i = position+1; i <= count; i++){
                getItem(i).setIsGrouped(false);
                if(!getItem(i).isExpanded()){ //means children are grouped, leave them
                    i += ((CommentItem)getItem(i)).getChildCount();
                }
            }
        }else{
            app.getToastHandler().showToast("do nothing", Toast.LENGTH_SHORT);
        }
        commentRecyclerView.getAdapter().notifyDataSetChanged();
    }

    @Override
    public void collapseItem(int position) {
        CommentItem item = (CommentItem)getItem(position);
        if(item.hasReplies() && item.isExpanded()){
            item.setIsExpanded(false);
            int count = (position+item.getChildCount());
            for(int i = position+1; i <= count; i++){
                getItem(i).setIsGrouped(true);
                if(!getItem(i).isExpanded()){
                    i += ((CommentItem)getItem(i)).getChildCount();
                }
            }
        }else{
            app.getToastHandler().showToast("do nothing", Toast.LENGTH_SHORT);
        }
        commentRecyclerView.getAdapter().notifyDataSetChanged();
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
    public Fragment getFragment() {
        return this;
    }

    private CommentBaseItem getItem(int position){
        return ((CommentsAdapter)commentRecyclerView.getAdapter()).getItem(position);
    }
    private List<CommentBaseItem> getCommentItems(){
        return ((CommentsAdapter)commentRecyclerView.getAdapter()).getCommentItems();
    }
}
