package com.matie.redgram.ui.comments.views.adapters;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.matie.redgram.ui.common.previews.CommentsPreviewFragment;
import com.matie.redgram.ui.common.previews.PostPreviewFragment;
import com.matie.redgram.ui.common.views.adapters.SectionsPagerAdapter;

/**
 * Created by matie on 2016-01-07.
 */
public class CommentsVerticalAdapter extends SectionsPagerAdapter {

    private Bundle bundle;
    private boolean isSelf;


    public CommentsVerticalAdapter(FragmentManager fm, Bundle bundle, boolean isSelf) {
        super(fm);
        this.bundle = bundle;
        this.isSelf = isSelf;
    }

    @Override
    protected int getPagerCount() {
        //return 2 is not a text
        if(isSelf)
            return 2;
        else
            return 1;
    }

    @Override
    protected Fragment getFragmentByPosition(int position) {
        if(isSelf){
            return getFragment(position);
        }else{
            CommentsPreviewFragment commentsPreviewFragment = new CommentsPreviewFragment();
            commentsPreviewFragment.setArguments(bundle);
            return commentsPreviewFragment;
        }
    }

    @Override
    protected CharSequence getFragmentTitle(int position) {
        if(isSelf){
            return getTitle(position);
        }else{
            return "Comments";
        }
    }

    private Fragment getFragment(int position){
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

    private CharSequence getTitle(int position){
        switch (position) {
            case 0:
                return "Post";
            case 1:
                return "Comments";
        }
        return null;
    }

}

