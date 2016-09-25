package com.matie.redgram.ui.thread.views.adapters;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.matie.redgram.ui.common.views.adapters.SectionsPagerAdapter;
import com.matie.redgram.ui.thread.CommentsFragment;
import com.matie.redgram.ui.thread.PostFragment;

/**
 * Created by matie on 2015-12-21.
 */
public class CommentsPagerAdapter extends SectionsPagerAdapter {

    private Bundle bundle;
    private boolean isSelf;

    public CommentsPagerAdapter(FragmentManager fm, Bundle bundle, boolean isSelf) {
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
        if(!isSelf) {
            position += 1;
        }
        return getFragment(position);
    }

    @Override
    protected CharSequence getFragmentTitle(int position) {
        if(!isSelf) {
            position += 1;
        }
        return getTitle(position);
    }

    private Fragment getFragment(int position){
        switch (position){
            case 1:
                PostFragment postFragment = new PostFragment();
                postFragment.setArguments(bundle);
                return postFragment;
            case 2:
                return new CommentsFragment();
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
