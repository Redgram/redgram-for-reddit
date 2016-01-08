package com.matie.redgram.ui.common.views.widgets.postlist.dynamic;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Spannable;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.TextView;

import com.google.gson.Gson;
import com.matie.redgram.R;
import com.matie.redgram.data.models.main.items.PostItem;
import com.matie.redgram.ui.App;
import com.matie.redgram.ui.comments.views.CommentsActivity;
import com.matie.redgram.ui.common.base.Fragments;
import com.matie.redgram.ui.common.previews.WebPreviewFragment;
import com.matie.redgram.ui.common.utils.text.CustomClickable;
import com.matie.redgram.ui.common.utils.text.CustomSpanListener;
import com.matie.redgram.ui.common.utils.text.StringUtils;

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
    int eventCode;

    final Resources res;

    public PostItemTagView(Context context, AttributeSet attrs) {
        super(context, attrs);
        res = context.getResources();
        eventCode = WEB_EVENT;
    }

    @Override
    public void onFinishInflate(){
        super.onFinishInflate();
        ButterKnife.inject(this);
    }

    @Override
    public void setupView(PostItem item) {
        this.item = item;

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
    public void handleMainClickEvent() {
        if(eventCode == WEB_EVENT){
            Bundle bundle = new Bundle();
            String key = getResources().getString(R.string.main_data_key);
            bundle.putString(key, new Gson().toJson(item));
            getMainActivity().setPanelView(Fragments.WEB_PREVIEW, bundle);
            return;
        }

        if(eventCode == COMMENTS_EVENT){
            // TODO: 2015-12-26 open comments activity
            Intent intent = new Intent(getMainActivity(), CommentsActivity.class);

            String key = getResources().getString(R.string.main_data_key);
            intent.putExtra(key, new Gson().toJson(item));
            getMainActivity().openIntent(intent, R.anim.enter, R.anim.exit);

            return;
        }

    }

    @Override
    public void onClickableEvent(CharSequence targetString) {
        if(targetString.toString().startsWith("[ ") && targetString.toString().endsWith(" ]")){
            //open webview
            Log.d("CLICKABLE", "web view [" + targetString + "]");
            eventCode = WEB_EVENT;
            handleMainClickEvent();
        }else if(targetString.toString().endsWith("comments")){
            //navigate to comments
            Log.d("CLICKABLE", "comments view [" + targetString + "]");
            eventCode = COMMENTS_EVENT;
            handleMainClickEvent();
        }
    }
}
