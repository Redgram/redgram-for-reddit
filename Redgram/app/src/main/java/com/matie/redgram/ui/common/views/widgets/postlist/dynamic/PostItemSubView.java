package com.matie.redgram.ui.common.views.widgets.postlist.dynamic;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

import com.matie.redgram.data.models.db.Prefs;
import com.matie.redgram.data.models.db.Settings;
import com.matie.redgram.data.models.main.items.PostItem;
import com.matie.redgram.ui.App;
import com.matie.redgram.ui.common.base.BaseActivity;
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
    private App app;

    public PostItemSubView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.baseActivity = (BaseActivity) getContext();
        this.app = baseActivity.app();
    }

    public abstract void setupView(PostItem item, int position, LinksView listener);

    public Settings getSettings() {
        return app.getSettings();
    }

    public Prefs getUserPrefs() {
        return app.getAuthUserPrefs();
    }

}
