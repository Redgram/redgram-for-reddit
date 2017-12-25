package com.matie.redgram.ui.common.views.widgets.postlist;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

import com.matie.redgram.data.models.main.items.submission.SubmissionItem;
import com.matie.redgram.ui.submission.adapters.SubmissionAdapter;
import com.matie.redgram.ui.submission.adapters.link.PostItemViewCreator;
import com.matie.redgram.ui.submission.links.views.SingleLinkView;

import java.util.List;

import butterknife.ButterKnife;

public class PostRecyclerView extends RecyclerView {

    private final LayoutManager layoutManager;
//    private final PostAdapter postAdapter;
    private final SubmissionAdapter adapter = new SubmissionAdapter(new PostItemViewCreator());

    public PostRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.layoutManager = new LinearLayoutManager(context);
//        this.postAdapter = new PostAdapter(context, R.layout.post_item_view);
    }

    @Override
    public void onFinishInflate() {
        super.onFinishInflate();
        ButterKnife.inject(this);

        setLayoutManager(layoutManager);
        setAdapter(adapter);
//        setAdapter(postAdapter);
    }

    public void setListener(SingleLinkView singleLinkView) {
//        postAdapter.setPostItemListener(singleLinkView);
    }

    public void replaceWith(List<SubmissionItem> items) {
        adapter.replaceWith(items);
//        postAdapter.replaceWith(items);
    }

    @Override
    public LayoutManager getLayoutManager() {
        return layoutManager;
    }

    @Override
    public Adapter getAdapter() {
        return adapter;
//        return postAdapter;
    }
}
