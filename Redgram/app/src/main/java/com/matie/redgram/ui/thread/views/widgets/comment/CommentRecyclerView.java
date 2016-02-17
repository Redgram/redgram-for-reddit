package com.matie.redgram.ui.thread.views.widgets.comment;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

import com.matie.redgram.R;
import com.matie.redgram.data.models.main.items.PostItem;
import com.matie.redgram.data.models.main.items.comment.CommentBaseItem;
import com.matie.redgram.ui.common.previews.CommentsPreviewFragment;
import com.matie.redgram.ui.common.views.adapters.PostAdapter;
import com.matie.redgram.ui.thread.views.CommentsView;
import com.matie.redgram.ui.thread.views.adapters.CommentsAdapter;
import com.matie.redgram.ui.thread.views.adapters.CommentsPagerAdapter;

import java.util.List;

import butterknife.ButterKnife;

/**
 * Created by matie on 2016-02-12.
 */
public class CommentRecyclerView extends RecyclerView {

    private final Context context;
    private final LayoutManager layoutManager;
    private final CommentsAdapter commentsAdapter;

    public CommentRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);

        this.context = context;
        this.layoutManager = new LinearLayoutManager(context);
        this.commentsAdapter = new CommentsAdapter(context);
    }

    @Override
    public void onFinishInflate(){
        super.onFinishInflate();
        ButterKnife.inject(this);

        setLayoutManager(layoutManager);
        setAdapter(commentsAdapter);
    }

    public void replaceWith(List<CommentBaseItem> items){
        commentsAdapter.replaceWith(items);
    }

    @Override
    public LayoutManager getLayoutManager() {
        return layoutManager;
    }

    @Override
    public Adapter getAdapter() {
        return commentsAdapter;
    }

    public void setAdapterListener(CommentsView listener) {
        commentsAdapter.setCommentListener(listener);
    }
}
