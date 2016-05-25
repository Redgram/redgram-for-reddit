package com.matie.redgram.ui.common.views.widgets.postlist.dynamic;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

import com.afollestad.materialdialogs.MaterialDialog;
import com.matie.redgram.data.managers.storage.db.DatabaseManager;
import com.matie.redgram.data.models.main.items.PostItem;
import com.matie.redgram.ui.common.base.BaseActivity;
import com.matie.redgram.ui.common.utils.widgets.DialogUtil;
import com.matie.redgram.ui.home.views.HomeView;
import com.matie.redgram.ui.posts.views.LinksView;

/**
 * Created by matie on 19/05/15.
 *
 * This class will be extended by all other view types that will inherit the
 * CardView layout and binding the items wil be according to the instance in hand.
 *
 * Common overlays could be inflated in this view
 */
public abstract class PostItemSubView extends RelativeLayout {

    private BaseActivity baseActivity;
    private SharedPreferences postPreferences;
    private DatabaseManager databaseManager;
    private HomeView listener;

    public PostItemSubView(Context context, AttributeSet attrs) {
        super(context, attrs);
        baseActivity = (BaseActivity) getContext();
        databaseManager = baseActivity.app().getDatabaseManager();
    }

    public abstract void setupView(PostItem item, int position, LinksView listener);
    public abstract void handleNsfwUpdate(boolean disabled);

    public boolean isNsfwDisabled(){
        //todo
        return true;
    }

    public void disableNsfw(){
        handleNsfwUpdate(true);
        // TODO: 09/10/15 notify data set changed
    }

    public void callNsfwDialog(){
        DialogUtil dialogUtil = baseActivity.getDialogUtil();
        if(dialogUtil != null){
            dialogUtil.build()
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


}
