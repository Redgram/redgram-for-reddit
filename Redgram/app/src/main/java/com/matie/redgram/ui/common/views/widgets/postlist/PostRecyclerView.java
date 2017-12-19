package com.matie.redgram.ui.common.views.widgets.postlist;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

import com.matie.redgram.R;
import com.matie.redgram.data.models.main.items.submission.PostItem;
import com.matie.redgram.ui.common.views.adapters.PostAdapter;
import com.matie.redgram.ui.submission.links.views.SingleLinkView;

import java.util.List;

import butterknife.ButterKnife;

public class PostRecyclerView extends RecyclerView {

    private final LayoutManager layoutManager;
    private final PostAdapter postAdapter;

    public PostRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.layoutManager = new LinearLayoutManager(context);
        this.postAdapter = new PostAdapter(context, R.layout.post_item_view);
    }

    @Override
    public void onFinishInflate(){
        super.onFinishInflate();
        ButterKnife.inject(this);

        setLayoutManager(layoutManager);
        setAdapter(postAdapter);
    }

    public void setListener(SingleLinkView singleLinkView){
        postAdapter.setPostItemListener(singleLinkView);
    }

    public void replaceWith(List<PostItem> items){
        postAdapter.replaceWith(items);
    }

    @Override
    public LayoutManager getLayoutManager() {
        return layoutManager;
    }

    @Override
    public Adapter getAdapter() {
        return postAdapter;
    }
}
