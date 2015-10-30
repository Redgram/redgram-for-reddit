package com.matie.redgram.ui.common.views.widgets.subreddit;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

import com.matie.redgram.data.models.main.items.SubredditItem;

/**
 * Created by matie on 2015-10-25.
 */
public class SubredditItemView extends RelativeLayout {

    public SubredditItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SubredditItemView(Context context) {
        super(context);
    }

    public void bindTo(SubredditItem subredditItem, int position) {

    }
}
