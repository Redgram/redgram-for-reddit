package com.matie.redgram.ui.home.views.widgets.postlist.dynamic;

import android.content.Context;
import android.util.AttributeSet;

import com.matie.redgram.ui.common.views.widgets.DynamicView;

/**
 * Created by matie on 19/05/15.
 *
 * todo: This view is supposed to either be {@link PostItemImageView} or {@link PostItemGifView}
 *
 * Obtained through the Imgur API. Current implementation will direct the user to Imgur official website.
 */
public class PostItemImgurView extends DynamicView {
    public PostItemImgurView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
}
