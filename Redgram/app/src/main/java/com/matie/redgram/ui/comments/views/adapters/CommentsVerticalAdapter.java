package com.matie.redgram.ui.comments.views.adapters;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.matie.redgram.data.models.main.items.PostItem;
import com.matie.redgram.ui.comments.views.CommentsFragment;
import com.matie.redgram.ui.common.previews.BasePreviewFragment;
import com.matie.redgram.ui.common.previews.CommentsPreviewFragment;
import com.matie.redgram.ui.common.previews.PostPreviewFragment;
import com.matie.redgram.ui.common.views.adapters.SectionsPagerAdapter;

/**
 * Created by matie on 2016-01-07.
 */
public class CommentsVerticalAdapter extends SectionsPagerAdapter {

    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";
    private Bundle bundle;


    public CommentsVerticalAdapter(FragmentManager fm, Bundle bundle) {
        super(fm);
        this.bundle = bundle;
    }


    @Override
    protected int getPagerCount() {
        //return 2
        return 2;
    }

    @Override
    protected Fragment getFragmentByPosition(int position) {
        switch (position){
            case 1:
                PostPreviewFragment postPreviewFragment = new PostPreviewFragment();
                postPreviewFragment.setArguments(bundle);
                return postPreviewFragment;
            case 2:
                CommentsPreviewFragment commentsPreviewFragment = new CommentsPreviewFragment();
                commentsPreviewFragment.setArguments(bundle);
                return commentsPreviewFragment;
        }
        return null;
    }

    @Override
    protected CharSequence getFragmentTitle(int position) {
        switch (position) {
            case 0:
                return "Post";
            case 1:
                return "Comments";
        }
        return null;
    }

}

