package com.matie.redgram.ui.home.views.widgets.postlist;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.util.AttributeSet;

import com.github.ksoichiro.android.observablescrollview.ObservableRecyclerView;
import com.matie.redgram.R;
import com.matie.redgram.data.models.PostItem;
import com.matie.redgram.ui.home.views.adapters.PostAdapter;
import com.matie.redgram.ui.home.views.adapters.PostAdapterBase;

import java.util.List;

import butterknife.ButterKnife;

/**
 * Created by matie on 04/04/15.
 */
public class PostRecyclerView extends ObservableRecyclerView {

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

        this.setLayoutManager(layoutManager);
        this.setAdapter(postAdapter);
    }

    public void replaceWith(List<PostItem> items){
        postAdapter.replaceWith(items);
    }

    @Override
    public LayoutManager getLayoutManager() {
        return layoutManager;
    }

    public PostAdapterBase getPostAdapter() {
        return postAdapter;
    }
}
