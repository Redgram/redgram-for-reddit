package com.matie.redgram.ui.common.views.widgets.postlist;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

import com.matie.redgram.R;
import com.matie.redgram.data.models.main.items.PostItem;
import com.matie.redgram.ui.common.views.adapters.PostAdapter;
import com.matie.redgram.ui.home.views.HomeView;
import com.matie.redgram.ui.posts.views.LinksView;

import java.util.List;

import butterknife.ButterKnife;

/**
 * Created by matie on 04/04/15.
 */
public class PostRecyclerView extends RecyclerView {

    private final Context context;
    private final LayoutManager layoutManager;
    private final PostAdapter postAdapter;

    public PostRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
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

    public void setListener(LinksView linksView){
        postAdapter.setPostItemListener(linksView);
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
