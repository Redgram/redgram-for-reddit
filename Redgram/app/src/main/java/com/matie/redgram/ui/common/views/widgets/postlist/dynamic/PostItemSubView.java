package com.matie.redgram.ui.common.views.widgets.postlist.dynamic;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;

import com.afollestad.materialdialogs.MaterialDialog;
import com.matie.redgram.data.managers.preferences.PreferenceManager;
import com.matie.redgram.data.models.main.items.PostItem;
import com.matie.redgram.ui.App;
import com.matie.redgram.ui.common.main.MainActivity;
import com.matie.redgram.ui.common.views.widgets.postlist.PostItemView;
import com.matie.redgram.ui.common.views.widgets.postlist.PostRecyclerView;

/**
 * Created by matie on 19/05/15.
 *
 * This class will be extended by all other view types that will inherit the
 * CardView layout and binding the items wil be according to the instance in hand.
 *
 * Common overlays could be inflated in this view
 */
public abstract class PostItemSubView extends RelativeLayout {

    MainActivity mainActivity;
    SharedPreferences postPreferences;

    public PostItemSubView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mainActivity = (MainActivity)getContext();
        postPreferences = (mainActivity.getApp()).getPreferenceManager().getSharedPreferences(PreferenceManager.POSTS_PREF);

        setGeneralClickListener();
    }

    private void setGeneralClickListener() {
        this.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isNsfwDisabled()){
                    callNsfwDialog();
                }
            }
        });
    }

    public abstract void setupView(PostItem item);
    public abstract void handleNsfwUpdate(boolean disabled);

    public boolean isNsfwDisabled(){
        return postPreferences.getBoolean(PreferenceManager.NSFW_KEY, false);
    }

    public void disableNsfw(){

        postPreferences.edit().putBoolean(PreferenceManager.NSFW_KEY, true).commit();
        handleNsfwUpdate(true);
        // TODO: 09/10/15 notify data set changed
    }

    private void callNsfwDialog(){
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


}
