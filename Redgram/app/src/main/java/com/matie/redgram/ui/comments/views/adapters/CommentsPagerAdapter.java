package com.matie.redgram.ui.comments.views.adapters;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.matie.redgram.ui.comments.views.CommentsFragment;
import com.matie.redgram.ui.common.base.BaseFragment;
import com.matie.redgram.ui.common.previews.BasePreviewFragment;
import com.matie.redgram.ui.common.previews.PostPreviewFragment;
import com.matie.redgram.ui.common.views.adapters.SectionsPagerAdapter;
import com.matie.redgram.ui.home.HomeFragment;
import com.matie.redgram.ui.subcription.SubscriptionDetailsFragment;

/**
 * Created by matie on 2015-12-21.
 */
public class CommentsPagerAdapter extends SectionsPagerAdapter {

    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";
    private CommentsPreviewDetector previewDetector;


    public CommentsPagerAdapter(FragmentManager fm, CommentsPreviewDetector detector) {
        super(fm);
        previewDetector = detector;
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
                CommentsFragment commentsFragment = new CommentsFragment();
                commentsFragment.setArguments(previewDetector.provideFragmentBundle());
                return commentsFragment;
            case 2:
                BasePreviewFragment fragment = previewDetector.providePreviewFragment();
                fragment.setArguments(previewDetector.provideFragmentBundle());
                return fragment;
        }
        return null;
    }

    @Override
    protected CharSequence getFragmentTitle(int position) {
        return null;
    }

    public interface CommentsPreviewDetector{
        public BasePreviewFragment providePreviewFragment();
        public Bundle provideFragmentBundle();
    }
}
