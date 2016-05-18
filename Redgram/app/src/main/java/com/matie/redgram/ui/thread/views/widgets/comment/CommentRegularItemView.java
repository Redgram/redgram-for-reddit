package com.matie.redgram.ui.thread.views.widgets.comment;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;
import com.matie.redgram.R;
import com.matie.redgram.data.models.api.reddit.base.BooleanDate;
import com.matie.redgram.data.models.main.items.comment.CommentBaseItem;
import com.matie.redgram.data.models.main.items.comment.CommentItem;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by matie on 2016-02-12.
 */
public class CommentRegularItemView extends CommentItemView{

    @InjectView(R.id.reg_body)
    TextView textView;
    @InjectView(R.id.author)
    TextView authorView;
    @InjectView(R.id.indicator)
    ExpandableIndicator indicator;
//    @InjectView(R.id.time)
//    TextView timeView;

    public CommentRegularItemView(Context context) {
        super(context);
    }

    public CommentRegularItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CommentRegularItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void setUpView(CommentBaseItem item) {

        CommentItem commentItem = (CommentItem)item;
        textView.setText(commentItem.getBody());
        authorView.setText(getAuthor(commentItem));

        indicator.setExpandedState(commentItem.isExpanded(), commentItem.getChildCount());
        if(!commentItem.isExpanded()){
            setBackgroundColor(getResources().getColor(R.color.material_grey100));
        }else{
            setBackgroundColor(getResources().getColor(R.color.material_white));
        }

        if(commentItem.hasReplies()){
            indicator.setVisibility(VISIBLE);
        }else{
            indicator.setVisibility(GONE);
        }
    }

    private String getAuthor(CommentItem commentItem) {
        if(commentItem.getEdited() instanceof BooleanDate.DateInstance){
            return commentItem.getAuthor() + " *";
        }else if(commentItem.getEdited() instanceof BooleanDate.BooleanInstance){
            if(((BooleanDate.BooleanInstance) commentItem.getEdited()).getData() == true){
                return commentItem.getAuthor() + " *";
            }else{
                return commentItem.getAuthor();
            }
        }
        return commentItem.getAuthor();
    }

    @Override
    public void onFinishInflate(){
        super.onFinishInflate();
        ButterKnife.inject(this);
    }

    public ExpandableIndicator getIndicator() {
        return indicator;
    }
}
