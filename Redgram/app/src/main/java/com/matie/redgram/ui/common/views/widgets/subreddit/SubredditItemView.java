package com.matie.redgram.ui.common.views.widgets.subreddit;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.matie.redgram.R;
import com.matie.redgram.data.models.main.items.SubredditItem;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by matie on 2015-10-25.
 */
public class SubredditItemView extends RelativeLayout {
    @InjectView(R.id.subreddit_text)
    TextView subredditText;

    private SubredditItem item;

    public SubredditItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SubredditItemView(Context context) {
        super(context);
    }

    public void bindTo(SubredditItem subredditItem, int position) {
        item = subredditItem;
        subredditText.setText(subredditItem.getName());
    }
    @Override
    public void onFinishInflate(){
        super.onFinishInflate();
        ButterKnife.inject(this);
    }

    public String getSubredditName() {
        return subredditText.getText().toString();
    }

    public SubredditItem getItem() {
        return item;
    }
}
