package com.matie.redgram.ui.common.views.widgets.postlist.dynamic;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.PopupMenu;
import android.text.Spannable;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.matie.redgram.R;
import com.matie.redgram.data.models.main.items.PostItem;
import com.matie.redgram.ui.App;
import com.matie.redgram.ui.common.utils.text.CustomClickable;
import com.matie.redgram.ui.common.utils.text.CustomSpanListener;
import com.matie.redgram.ui.common.utils.text.StringUtils;
import com.matie.redgram.ui.common.utils.text.tags.AuthorTag;
import com.matie.redgram.ui.home.views.HomeView;
import com.matie.redgram.ui.posts.views.LinksView;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by matie on 04/04/15.
 */
public class PostItemHeaderView extends PostItemSubView implements CustomSpanListener {

    @InjectView(R.id.header_username_view)
    TextView headerUsernameView;
    @InjectView(R.id.header_time_subreddit_view)
    TextView headerTimeSubredditView;
    @InjectView(R.id.header_more_view)
    ImageView headerMoreView;

    PostItem postItem;
    PopupMenu popupMenu;
    LinksView listener;
    int position;

    private final Resources res;

    public PostItemHeaderView(Context context, AttributeSet attrs) {
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
        this.postItem = item;
        this.position = position;
        this.listener = listener;

        // TODO: 2015-12-14 click on user to view user activity
        setupAuthor(item);
        setupInfo(item);
        setupMoreView();
    }

    private void setupMoreView() {
        popupMenu = new PopupMenu(getContext(), headerMoreView);
        popupMenu.getMenuInflater().inflate(R.menu.header_more_menu, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.view_subreddit) {
                    listener.visitSubreddit(postItem.getSubreddit());
                    return true;
                }
                if (item.getItemId() == R.id.open_browser) {
                    listener.openInBrowser(position);
                    return true;
                }

                if (item.getItemId() == R.id.copy_link) {
                    listener.copyItemLink(position);
                    return true;
                }

                if (item.getItemId() == R.id.report) {
                    listener.reportPost(position);
                    return true;
                }

                return false;
            }
        });
    }

    @Override
    public void handleNsfwUpdate(boolean disabled) {

    }

    @OnClick(R.id.header_more_view)
    public void onHeaderMoreClick(){
        popupMenu.show();
    }


    @Override
    public void onClickableEvent(CharSequence targetString) {
        Log.d("CLICKABLE", "onClick [" + targetString + "]");
        String target = targetString.toString();
        if(target.contains("/r/")){
            String subredditName = target.substring(target.lastIndexOf('/')+1, target.length());
            listener.visitSubreddit(subredditName);
        }
    }

    public int getAuthorBackgroundColor(PostItem item) {
        int resourceId = 0;

        if(item.distinguished().equals("moderator")){
            resourceId = R.color.material_green400;
        }else if(item.distinguished().equals("admin")){
            resourceId = R.color.material_red400;
        }else{
            resourceId = R.color.material_blue400;
        }

        return ContextCompat.getColor(getContext(), resourceId);
    }

    private int getBackgroundTextColor() {
        return ContextCompat.getColor(getContext(), R.color.material_black);
    }

    private void setupAuthor(PostItem item) {
        if(item.distinguished() != null){
            final StyleSpan bss = new StyleSpan(android.graphics.Typeface.BOLD);
            final AuthorTag customReplacement = new AuthorTag(getAuthorBackgroundColor(item), getBackgroundTextColor());

            StringUtils.newSpannableBuilder(getContext())
                    .setTextView(headerUsernameView)
                    .append(item.getAuthor(), customReplacement, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
                    .span(bss, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
                    .buildSpannable();
        }else{
            headerUsernameView.setText(item.getAuthor());
        }
    }

    private void setupInfo(PostItem item) {
        String subreddit = "/r/"+item.getSubreddit();
        CustomClickable clickable = new CustomClickable(this, true);

        StringUtils.newSpannableBuilder(getContext())
                .setTextView(headerTimeSubredditView)
                .append("submitted " + item.getTime() + " hrs ago to ")
                .append(subreddit, clickable, Spannable.SPAN_INCLUSIVE_INCLUSIVE)
                .span(new ForegroundColorSpan(Color.rgb(204, 0, 0)), Spannable.SPAN_INCLUSIVE_INCLUSIVE)
                .clickable()
                .build();
    }
}
