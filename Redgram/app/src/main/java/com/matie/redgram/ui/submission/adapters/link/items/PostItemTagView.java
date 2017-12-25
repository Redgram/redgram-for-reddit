package com.matie.redgram.ui.submission.adapters.link.items;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.text.Spanned;
import android.util.AttributeSet;
import android.widget.TextView;

import com.matie.redgram.R;
import com.matie.redgram.data.models.main.items.submission.PostItem;
import com.matie.redgram.ui.common.utils.text.CustomSpanListener;
import com.matie.redgram.ui.common.utils.text.StringDecorator;
import com.matie.redgram.ui.common.utils.text.spans.CustomClickable;
import com.matie.redgram.ui.submission.links.views.SingleLinkView;

import java.util.HashMap;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class PostItemTagView extends PostItemSubView implements CustomSpanListener {

    private static final String SPACE = " ";

    private static final int WEB_EVENT = 0;
    private static final int COMMENTS_EVENT = 1;

    @InjectView(R.id.tags_view)
    TextView tags;

    PostItem item;
    int position;
    SingleLinkView listener;

    final Resources res;

    public PostItemTagView(Context context, AttributeSet attrs) {
        super(context, attrs);
        res = context.getResources();
    }

    @Override
    public void onFinishInflate(){
        super.onFinishInflate();
        ButterKnife.inject(this);
    }

    @Override
    public void setupView(PostItem item, int position, SingleLinkView listener) {
        this.item = item;
        this.position = position;
        this.listener = listener;

        String bullet = " "+res.getString(R.string.text_bullet)+" ";
        String comments = item.getNumComments() + " comments";
        String source = "[ "+item.getDomain()+" ]";

        CustomClickable commentsClickable = new CustomClickable(this, false, Color.rgb(204, 0, 0));
        CustomClickable linkClickable = new CustomClickable(this, false, Color.rgb(204, 0, 0));

        StringDecorator.newSpannableBuilder()
                .setTextView(tags)
                .append(comments, commentsClickable, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
                .append(SPACE + bullet + SPACE)
                .append(source, linkClickable, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
                .clickable()
                .build();
    }

    @Override
    public void onClickableEvent(CharSequence targetString) {
        if(targetString.toString().startsWith("[ ") && targetString.toString().endsWith(" ]")){
            //open webview
            listener.viewWebMedia(getContext(), position);
        }else if(targetString.toString().endsWith("comments")){
            //navigate to comments
            listener.loadCommentsForPost(getContext(), position);
        }
    }

    @Override
    public void onClickableEvent(HashMap<String, String> data) {
        //do nothing
    }
}
