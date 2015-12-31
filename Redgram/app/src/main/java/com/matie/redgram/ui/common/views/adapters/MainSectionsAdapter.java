package com.matie.redgram.ui.common.views.adapters;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.matie.redgram.ui.common.base.BaseFragment;
import com.matie.redgram.ui.home.HomeFragment;
import com.matie.redgram.ui.subcription.SubscriptionDetailsFragment;

/**
 * Created by matie on 2015-12-21.
 */
public class MainSectionsAdapter extends SectionsPagerAdapter {

    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";

    public MainSectionsAdapter(FragmentManager fm) {
        super(fm);
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
                return new HomeFragment();
            case 2:
                return new SubscriptionDetailsFragment();
        }
        return null;
    }

    private Bundle getArguments(int position) {
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, position);
        return args;
    }

    @Override
    protected CharSequence getFragmentTitle(int position) {
        switch (position) {
            case 0:
                return "FEED";
            case 1:
                return "COMMENTS";
        }
        return null;
    }
}
