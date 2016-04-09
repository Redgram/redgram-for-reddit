package com.matie.redgram.ui.thread.views.widgets.comment;

import android.content.Context;
import android.os.Build;
import android.support.v7.widget.CardView;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.matie.redgram.R;
import com.matie.redgram.data.models.main.items.comment.CommentBaseItem;
import com.matie.redgram.data.models.main.items.comment.CommentItem;


import java.util.Map;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * common click events
 *
 * Created by matie on 2016-01-30.
 */
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
        //resolve the following only if this view is visible/not grouped - it is not grouped by default
        if(!resolveGroupState(item.isGrouped(), position)){
            resolveLevelDimension(item.getLevel());
            resolveLevelColor(item.getLevel(), levelToColorMap);
            ((CommentItemView)dynamicView).setUpView(item);
        }
        requestLayout();
    }

    private boolean resolveGroupState(boolean isGrouped, int position) {
        RelativeLayout.LayoutParams lp = null;

        if(isGrouped){
            lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, 0);
        }else{
            lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,
                                        RelativeLayout.LayoutParams.WRAP_CONTENT);
        }

        setLayoutParams(lp);
        return isGrouped;
    }

    private void resolveLevelColor(int level, Map<Integer, Integer> levelToColorMap) {
        if(level > 0){
            for(Integer key : levelToColorMap.keySet()){
                if(level == key){
                    levelView.setBackgroundColor(levelToColorMap.get(key));
                    break;
                }
            }
        }
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
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(level*10, LinearLayout.LayoutParams.MATCH_PARENT);
        levelView.setLayoutParams(lp);
    }

//    private void resolveDimensions(int position) {
//
//        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(this.getLayoutParams());
//
//        if(position != 0){
//            lp.setMargins(0, 0, 0, 0);
//        }else{
//            lp.setMargins(0, 100, 0, 0);
//        }
//
//        this.setLayoutParams(lp);
//
//    }

    public CommentBaseItem getItem() {
        return baseItem;
    }

    public int getItemPosition() {
        return itemPosition;
    }
}
