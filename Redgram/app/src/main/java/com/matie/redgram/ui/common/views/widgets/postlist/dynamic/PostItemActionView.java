package com.matie.redgram.ui.common.views.widgets.postlist.dynamic;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.matie.redgram.R;
import com.matie.redgram.data.models.main.items.PostItem;
import com.matie.redgram.ui.App;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by matie on 04/04/15.
 */
public class PostItemActionView extends PostItemSubView {


    @InjectView(R.id.action_vote_up)
    ImageView voteUp;
    @InjectView(R.id.action_vote_down)
    ImageView voteDown;
    @InjectView(R.id.action_score_view)
    TextView scoreView;
    @InjectView(R.id.action_share)
    ImageView shareView;
    @InjectView(R.id.action_favorite)
    ImageView favoriteView;
    @InjectView(R.id.action_hide)
    ImageView hideView;

    PostItem postItem;

    public PostItemActionView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void onFinishInflate(){
        super.onFinishInflate();
        ButterKnife.inject(this);
    }

    @Override
    public void setupView(PostItem item) {
        postItem = item;
        scoreView.setText(item.getScore() + "");
    }

    @Override
    public void handleNsfwUpdate(boolean disabled) {

    }

    @Override
    public void handleMainClickEvent() {

    }

    @OnClick(R.id.action_share)
    public void onActionShare(){
        getMainActivity().getDialogUtil().build()
                .title("Share")
                .items(R.array.shareOptions)
                .itemsCallback(new MaterialDialog.ListCallback() {
                    @Override
                    public void onSelection(MaterialDialog materialDialog, View view, int i, CharSequence charSequence) {
                        String urlToShare = "";
                        if(charSequence.toString().equalsIgnoreCase("Link")){
                            urlToShare = postItem.getUrl();
                        }

                        if(charSequence.toString().equalsIgnoreCase("Comments")){
                            urlToShare = "https://reddit.com/"+postItem.getPermalink();
                        }

                        callShareDialog(urlToShare);
                    }
                })
                .show();
    }

    private void callShareDialog(String contentToShare){
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, contentToShare);
        sendIntent.setType("text/plain");
        getMainActivity().startActivity(Intent.createChooser(sendIntent, getResources().getText(R.string.send_to)));
    }

}
