package com.matie.redgram.ui.common.base;

import android.support.v4.app.Fragment;

import com.matie.redgram.ui.common.previews.ImagePreviewFragment;
import com.matie.redgram.ui.common.previews.WebPreviewFragment;
import com.matie.redgram.ui.home.HomeFragment;
import com.matie.redgram.ui.search.SearchFragment;
import com.matie.redgram.ui.subcription.SubscriptionDetailsFragment;
import com.matie.redgram.ui.subcription.SubscriptionFragment;
import com.matie.redgram.ui.thread.CommentsFragment;
import com.matie.redgram.ui.thread.PostFragment;

/**
 * Created by matie on 30/03/15.
 */
public enum Fragments {

    HOME(HomeFragment.class), SEARCH(SearchFragment.class), SUBREDDITS(SubscriptionFragment.class),

    SUBREDDITS_DETAILS(SubscriptionDetailsFragment.class),

    COMMENTS_PREVIEW(CommentsFragment.class),
    POST_PREVIEW(PostFragment.class) ,IMAGE_PREVIEW(ImagePreviewFragment.class), WEB_PREVIEW(WebPreviewFragment.class);

    final Class<? extends Fragment> fragment;

    private Fragments(Class<? extends Fragment> fragment){
        this.fragment = fragment;
    }

    public String getFragment() {
        return fragment.getName();
    }

}
