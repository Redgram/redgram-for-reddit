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
        return 0;
    }

    @Override
    protected Fragment getFragmentByPosition(int position) {
        return null;
    }

    @Override
    protected CharSequence getFragmentTitle(int position) {
        return null;
    }
}
