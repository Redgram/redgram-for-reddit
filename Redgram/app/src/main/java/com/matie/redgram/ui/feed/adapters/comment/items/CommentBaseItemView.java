package com.matie.redgram.ui.feed.adapters.comment.items;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.matie.redgram.R;
import com.matie.redgram.data.models.main.items.submission.comment.CommentBaseItem;

import java.util.Map;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class CommentBaseItemView extends RelativeLayout{

    @InjectView(R.id.level_view)
    View levelView;

    //inflate views here based on type
    @InjectView(R.id.container)
    FrameLayout container;

    View dynamicView;
    Context context;
    CommentBaseItem baseItem;
    int itemPosition;

    public CommentBaseItemView(Context context) {
        super(context);
        this.context = context;
    }

    public CommentBaseItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }

    public CommentBaseItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
    }

    @Override
    public void onFinishInflate(){
        super.onFinishInflate();
        ButterKnife.inject(this);
    }

    public void setUp(CommentBaseItem item, int position, Map<Integer, Integer> levelToColorMap){
        baseItem = item;
        itemPosition = position;

        resolveLevelDimension(item.getLevel());
        resolveLevelColor(item.getLevel(), levelToColorMap);

        ((CommentItemView)dynamicView).setUpView(item);

        requestLayout();
    }

    private void resolveLevelColor(int level, Map<Integer, Integer> levelToColorMap) {
        if (level <= 0 || !levelToColorMap.containsKey(level)) return;

        levelView.setBackgroundColor(levelToColorMap.get(level));
    }

    public FrameLayout getContainer() {
        return container;
    }

    public View getDynamicView() {
        return dynamicView;
    }

    public void setDynamicView(View dynamicView) {
        this.dynamicView = dynamicView;
    }

    private void resolveLevelDimension(int level){
        LinearLayout.LayoutParams lp =
                new LinearLayout.LayoutParams((level * 10), LinearLayout.LayoutParams.MATCH_PARENT);

        levelView.setLayoutParams(lp);
    }

    public CommentBaseItem getItem() {
        return baseItem;
    }

    public int getItemPosition() {
        return itemPosition;
    }
}