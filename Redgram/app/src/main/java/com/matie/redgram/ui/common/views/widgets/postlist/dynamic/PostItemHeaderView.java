package com.matie.redgram.ui.common.views.widgets.postlist.dynamic;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.PopupMenu;
import android.text.Spannable;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.matie.redgram.R;
import com.matie.redgram.data.models.main.items.PostItem;
import com.matie.redgram.ui.common.utils.text.CustomClickable;
import com.matie.redgram.ui.common.utils.text.CustomReplacement;
import com.matie.redgram.ui.common.utils.text.CustomSpanListener;
import com.matie.redgram.ui.common.utils.text.StringUtils;
import com.matie.redgram.ui.common.utils.text.tags.AuthorTag;

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

    private  final Resources res;

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
    public void setupView(PostItem item) {
        postItem = item;
        // TODO: 2015-12-14 click on user to view user activity
        setupAuthor(item);
        setupInfo(item);
        setupMoreView();
    }

    private void setupMoreView() {
        popupMenu = new PopupMenu(getMainActivity(), headerMoreView);
        popupMenu.getMenuInflater().inflate(R.menu.header_more_menu, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.view_subreddit) {
                    getMainActivity().openFragmentWithResult(postItem.getSubreddit());
                    return true;
                }
                if (item.getItemId() == R.id.open_browser) {
                    buildBrowserDialog();
                    return true;
                }

                if (item.getItemId() == R.id.copy_link) {
                    buildCopyDialog();
                    return true;
                }

                getMainActivity().getApp().getToastHandler().showToast(item.toString(), Toast.LENGTH_SHORT);
                return false;
            }
        });
    }

    private void buildCopyDialog() {
        getMainActivity().getDialogUtil().build()
                .title("Copy")
                .items(R.array.shareOptions)
                .itemsCallback(new MaterialDialog.ListCallback() {
                    @Override
                    public void onSelection(MaterialDialog materialDialog, View view, int i, CharSequence charSequence) {
                        Uri urlToOpen = getUriToOpen(charSequence);
                        if (urlToOpen != null) {
                            ClipboardManager clipboard = (ClipboardManager)
                                    getMainActivity().getSystemService(Context.CLIPBOARD_SERVICE);
                            ClipData clip = ClipData.newUri(getContext().getContentResolver(), "URI", urlToOpen);
                            clipboard.setPrimaryClip(clip);
                            getMainActivity().getApp().getToastHandler().showToast("Link Copied", Toast.LENGTH_SHORT);
                        }
                    }
                }).show();
    }

    private void buildBrowserDialog() {
        getMainActivity().getDialogUtil().build()
                .title("Open")
                .items(R.array.shareOptions)
                .itemsCallback(new MaterialDialog.ListCallback() {
                    @Override
                    public void onSelection(MaterialDialog materialDialog, View view, int i, CharSequence charSequence) {
                        Uri urlToOpen = getUriToOpen(charSequence);
                        if (urlToOpen != null) {
                            Intent browserIntent = new Intent(Intent.ACTION_VIEW, urlToOpen);
                            getMainActivity().startActivity(browserIntent);
                        }
                    }
                }).show();
    }

    private Uri getUriToOpen(CharSequence charSequence) {
        Uri urlToOpen = null;
        if (charSequence.toString().equalsIgnoreCase("Link")) {
            urlToOpen = Uri.parse(postItem.getUrl());
        }
        if (charSequence.toString().equalsIgnoreCase(("Comments"))) {
            urlToOpen = Uri.parse("https://reddit.com/" + postItem.getPermalink());
        }
        return urlToOpen;
    }

    @Override
    public void handleNsfwUpdate(boolean disabled) {

    }

    @Override
    public void handleMainClickEvent() {

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
            getMainActivity().openFragmentWithResult(subredditName);
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

//    private String getAuthor(PostItem item) {
//        String author = item.getAuthor();
//        if(item.distinguished().equals("moderator")){
//            author += " [M]";
//        }else if(item.distinguished().equals("admin")){
//            author += " [A]";
//        }else{
//            author += " [S]";
//        }
//        return author;
//    }

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
