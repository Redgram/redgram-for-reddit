package com.matie.redgram.ui.common.base;

import android.support.v4.app.Fragment;

import com.matie.redgram.ui.comments.views.CommentsFragment;
import com.matie.redgram.ui.common.previews.ImagePreviewFragment;
import com.matie.redgram.ui.common.previews.PostPreviewFragment;
import com.matie.redgram.ui.common.previews.WebPreviewFragment;
import com.matie.redgram.ui.home.HomeFragment;
import com.matie.redgram.ui.search.SearchFragment;
import com.matie.redgram.ui.subcription.SubscriptionDetailsFragment;
import com.matie.redgram.ui.subcription.SubscriptionFragment;

/**
 * Created by matie on 30/03/15.
 */
public enum Fragments {

    HOME(HomeFragment.class), SEARCH(SearchFragment.class), SUBREDDITS(SubscriptionFragment.class),

    SUBREDDITS_DETAILS(SubscriptionDetailsFragment.class),

    COMMENTS(CommentsFragment.class),

    POST_PREVIEW(PostPreviewFragment.class) ,IMAGE_PREVIEW(ImagePreviewFragment.class), WEB_PREVIEW(WebPreviewFragment.class);

    final Class<? extends Fragment> fragment;

    private Fragments(Class<? extends Fragment> fragment){
        this.fragment = fragment;
    }

    public String getFragment() {
        return fragment.getName();
    }

}
