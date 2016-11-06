package com.matie.redgram.ui.thread.views.widgets.comment;

import android.content.Context;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.TextView;

import com.matie.redgram.R;
import com.matie.redgram.data.models.api.reddit.base.BooleanDate;
import com.matie.redgram.data.models.main.items.comment.CommentBaseItem;
import com.matie.redgram.data.models.main.items.comment.CommentItem;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
        String parsedComment = parseLinks(commentItem.getBody());
        textView.setText(Html.fromHtml(parsedComment));
        textView.setMovementMethod(LinkMovementMethod.getInstance());
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

    private String parseLinks(String commentBody){
        Pattern linkPattern = Pattern.compile("\\[.*\\]\\(.*\\)");
        Matcher m = linkPattern.matcher(commentBody);
        String parsedCommentBody = commentBody;
        try {
            while (m.find()){
                int startIndexLinkText = m.group().indexOf("]");
                int startIndexLinkHref = m.group().indexOf("(");
                String linkText = m.group().substring(1, startIndexLinkText);
                String linkHref = m.group().substring(startIndexLinkHref+1, m.group().length()-1);
                String completeLink = "<a href=\"" +linkHref+ "\">" +linkText+ "</a>";
                parsedCommentBody = parsedCommentBody.replace(m.group(), completeLink);
            }
        }catch (Exception e){
            Log.d("CommentRegularItemView", e.getMessage());
        }
        return parsedCommentBody;
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
