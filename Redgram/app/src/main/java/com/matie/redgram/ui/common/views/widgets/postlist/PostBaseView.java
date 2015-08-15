package com.matie.redgram.ui.common.views.widgets.postlist;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

import com.matie.redgram.data.models.PostItem;

/**
 * Created by matie on 19/05/15.
 *
 * This class will be extended by all other view types that will inherit the
 * CardView layout and binding the items wil be according to the instance in hand.
 *
 * Common overlays could be inflated in this view
 */
public abstract class PostBaseView extends RelativeLayout {

    public PostBaseView(Context context) {
        super(context);
    }

    public PostBaseView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PostBaseView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public PostBaseView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public abstract void setUpView(PostItem item);
}
