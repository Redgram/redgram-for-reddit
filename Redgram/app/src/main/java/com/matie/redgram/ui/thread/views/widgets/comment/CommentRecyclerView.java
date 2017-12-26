package com.matie.redgram.ui.thread.views.widgets.comment;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;

import com.matie.redgram.data.models.main.items.submission.SubmissionItem;
import com.matie.redgram.ui.feed.adapters.SubmissionAdapter;
import com.matie.redgram.ui.feed.adapters.comment.CommentItemViewCreator;
import com.matie.redgram.ui.thread.views.CommentsView;

import java.util.List;

import butterknife.ButterKnife;

public class CommentRecyclerView extends RecyclerView {

    private final Context context;
    private final LayoutManager layoutManager;
    private final SubmissionAdapter adapter = new SubmissionAdapter(new CommentItemViewCreator());

    public CommentRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);

        this.context = context;
        this.layoutManager = new WrapContentLinearLayoutManager(context);
    }

    @Override
    public void onFinishInflate(){
        super.onFinishInflate();
        ButterKnife.inject(this);

        setLayoutManager(layoutManager);
        setAdapter(adapter);
    }

    public void replaceWith(List<SubmissionItem> items) {
        adapter.replaceWith(items);
    }

    @Override
    public LayoutManager getLayoutManager() {
        return layoutManager;
    }

    @Override
    public Adapter getAdapter() {
        return adapter;
    }

    public void setAdapterListener(CommentsView listener) {
//        commentsAdapter.setCommentListener(listener);
    }

    /**
     * Based on http://stackoverflow.com/a/33822747/2898754
     *
     * This prevents IndexOutOfBoundException when notifyItemRangeInserted is called
     */
    private class WrapContentLinearLayoutManager extends LinearLayoutManager{

        public WrapContentLinearLayoutManager(Context context) {
            super(context);
        }

        @Override
        public void onLayoutChildren(Recycler recycler, State state) {
            try{
                super.onLayoutChildren(recycler, state);
            }catch (IndexOutOfBoundsException e){
                Log.e("probe", "meet a IOOBE in RecyclerView");
            }
        }
    }

}
