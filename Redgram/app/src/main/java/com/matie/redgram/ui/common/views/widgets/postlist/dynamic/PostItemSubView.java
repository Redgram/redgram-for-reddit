package com.matie.redgram.ui.common.views.widgets.postlist.dynamic;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

import com.afollestad.materialdialogs.MaterialDialog;
import com.google.gson.Gson;
import com.matie.redgram.R;
import com.matie.redgram.data.managers.preferences.PreferenceManager;
import com.matie.redgram.data.models.main.items.PostItem;
import com.matie.redgram.ui.comments.views.CommentsActivity;
import com.matie.redgram.ui.common.main.MainActivity;

/**
 * Created by matie on 19/05/15.
 *
 * This class will be extended by all other view types that will inherit the
 * CardView layout and binding the items wil be according to the instance in hand.
 *
 * Common overlays could be inflated in this view
 */
public abstract class PostItemSubView extends RelativeLayout {

    private MainActivity mainActivity;
    private SharedPreferences postPreferences;

    public PostItemSubView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mainActivity = (MainActivity)getContext();
        postPreferences = (mainActivity.getApp()).getPreferenceManager().getSharedPreferences(PreferenceManager.POSTS_PREF);
    }

    public MainActivity getMainActivity() {
        return mainActivity;
    }

    public abstract void setupView(PostItem item);
    public abstract void handleNsfwUpdate(boolean disabled);
    public abstract void handleMainClickEvent();

    public boolean isNsfwDisabled(){
        return postPreferences.getBoolean(PreferenceManager.POSTS_NSFW_KEY, false);
    }

    public void disableNsfw(){
        postPreferences.edit().putBoolean(PreferenceManager.POSTS_NSFW_KEY, true).commit();
        handleNsfwUpdate(true);
        // TODO: 09/10/15 notify data set changed
    }

    public void callNsfwDialog(){
        mainActivity.getDialogUtil().build()
                .title("Disable NSFW setting?")
                .positiveText("Yes")
                .negativeText("Cancel")
                .callback(new MaterialDialog.ButtonCallback() {
                    @Override
                    public void onPositive(MaterialDialog dialog) {
                        super.onPositive(dialog);
                        disableNsfw();
                    }

                    @Override
                    public void onNegative(MaterialDialog dialog) {
                        super.onNegative(dialog);
                    }
                })
                .show();
    }

    public void loadComments(PostItem item){
        Intent intent = new Intent(getMainActivity(), CommentsActivity.class);

        String key = getResources().getString(R.string.main_data_key);
        intent.putExtra(key, new Gson().toJson(item));
        mainActivity.openIntent(intent, R.anim.enter, R.anim.exit);
    }




}
