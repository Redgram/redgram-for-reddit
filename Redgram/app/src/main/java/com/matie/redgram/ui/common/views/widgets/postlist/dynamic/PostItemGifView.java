package com.matie.redgram.ui.common.views.widgets.postlist.dynamic;

import android.content.Context;
import android.util.AttributeSet;

import com.matie.redgram.R;
import com.matie.redgram.data.models.PostItem;
import com.matie.redgram.ui.common.views.widgets.postlist.PostBaseView;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by matie on 19/05/15.
 */
public class PostItemGifView extends PostBaseView {
    @InjectView(R.id.gif_text_view)
    PostItemTextView postItemTextView;

    @InjectView(R.id.gif_tag_view)
    PostItemTagView postItemTagView;

    public PostItemGifView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    @Override
    public void onFinishInflate(){
        super.onFinishInflate();
        ButterKnife.inject(this);
    }
    @Override
    public void setUpView(PostItem item) {
        postItemTextView.setUpView(item);
        postItemTagView.setUpView(item);
    }
}
