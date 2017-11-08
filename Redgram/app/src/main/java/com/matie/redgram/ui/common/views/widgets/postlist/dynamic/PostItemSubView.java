package com.matie.redgram.ui.common.views.widgets.postlist.dynamic;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

import com.matie.redgram.data.managers.storage.db.DatabaseManager;
import com.matie.redgram.data.models.db.Prefs;
import com.matie.redgram.data.models.db.Session;
import com.matie.redgram.data.models.main.items.PostItem;
import com.matie.redgram.ui.common.base.BaseActivity;
import com.matie.redgram.ui.links.views.LinksView;

/**
 * Created by matie on 19/05/15.
 *
 * This class will be extended by all other view types that will inherit the
 * CardView layout and binding the items wil be according to the instance in hand.
 *
 * Common overlays could be inflated in this view
 */
public abstract class PostItemSubView extends RelativeLayout {

    private DatabaseManager databaseManager;

    public PostItemSubView(Context context, AttributeSet attrs) {
        super(context, attrs);

        if (getContext() instanceof  BaseActivity) {
            BaseActivity baseActivity = (BaseActivity) getContext();
            databaseManager = baseActivity.component().getDatabaseManager();
        }
    }

    public abstract void setupView(PostItem item, int position, LinksView listener);

    public Prefs getSessionPrefs() {
        if (databaseManager == null) return null;
        return databaseManager.getSessionPreferences();
    }

}
