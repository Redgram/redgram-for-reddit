package com.matie.redgram.ui.profile.views.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.matie.redgram.ui.common.views.adapters.SectionsPagerAdapter;

/**
 * Created by matie on 2016-08-06.
 */
public class ProfilePagerAdapter extends SectionsPagerAdapter{

    public ProfilePagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    protected int getPagerCount() {
        return 1;
    }

    @Override
    protected Fragment getFragmentByPosition(int position) {
        switch (position){
            case 1:
                return new ProfileOverviewFragment();
        }
        return null;
    }

    @Override
    protected CharSequence getFragmentTitle(int position) {
        switch (position) {
            case 0:
                return "Overview";
        }
        return null;
    }
}
