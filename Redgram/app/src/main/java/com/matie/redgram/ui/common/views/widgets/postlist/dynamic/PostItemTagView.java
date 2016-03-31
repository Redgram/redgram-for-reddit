package com.matie.redgram.ui.common.views.widgets.postlist.dynamic;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.text.Spannable;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.util.AttributeSet;
import android.widget.TextView;

import com.matie.redgram.R;
import com.matie.redgram.data.models.main.items.PostItem;
import com.matie.redgram.ui.common.utils.text.CustomClickable;
import com.matie.redgram.ui.common.utils.text.CustomSpanListener;
import com.matie.redgram.ui.common.utils.text.StringUtils;
import com.matie.redgram.ui.home.views.HomeView;
import com.matie.redgram.ui.posts.views.LinksView;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by matie on 21/06/15.
 */
public class PostItemTagView extends PostItemSubView implements CustomSpanListener {

    private static final String SPACE = " ";

    private static final int WEB_EVENT = 0;
    private static final int COMMENTS_EVENT = 1;

    @InjectView(R.id.tags_view)
    TextView tags;

    PostItem item;
    int position;
    LinksView listener;

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
    public void setupView(PostItem item, int position, LinksView listener) {
        this.item = item;
        this.position = position;
        this.listener = listener;

        String bullet = " "+res.getString(R.string.text_bullet)+" ";
        String comments = item.getNumComments() + " comments";
        String source = "[ "+item.getDomain()+" ]";

        CustomClickable commentsClickable = new CustomClickable(this, false);
        CustomClickable linkClickable = new CustomClickable(this, false);

        StringUtils.newSpannableBuilder(getContext())
                .setTextView(tags)
                .append(comments, commentsClickable, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
                .span(new ForegroundColorSpan(Color.rgb(204, 0, 0)), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                .append(SPACE + bullet + SPACE)
                .append(source, linkClickable, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
                .span(new ForegroundColorSpan(Color.rgb(204, 0, 0)), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                .clickable()
                .build();
    }

    @Override
    public void handleNsfwUpdate(boolean disabled) {

    }

    @Override
    public void onClickableEvent(CharSequence targetString) {
        if(targetString.toString().startsWith("[ ") && targetString.toString().endsWith(" ]")){
            //open webview
            listener.viewWebMedia(position);
        }else if(targetString.toString().endsWith("comments")){
            //navigate to comments
            listener.loadCommentsForPost(position);
        }
    }
}
