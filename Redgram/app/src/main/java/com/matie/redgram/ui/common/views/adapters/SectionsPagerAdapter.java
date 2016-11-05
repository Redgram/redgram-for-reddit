package com.matie.redgram.ui.common.views.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

/**
 * Created by matie on 2015-12-20.
 */
public abstract class SectionsPagerAdapter extends FragmentStatePagerAdapter {

    private FragmentManager fm;

    public SectionsPagerAdapter(FragmentManager fm) {
        super(fm);
        this.fm = fm;
    }


    protected abstract int getPagerCount();
    protected abstract Fragment getFragmentByPosition(int position);
    protected abstract CharSequence getFragmentTitle(int position);

    @Override
    public Fragment getItem(int position) {
        // getItem is called to instantiate the fragment for the given page.
        // Return a PlaceholderFragment (defined as a static inner class below).
        return getFragmentByPosition(position + 1);
    }

    @Override
    public int getCount() {
        // Show n total pages.
        return getPagerCount();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return getFragmentTitle(position);
    }

    public FragmentManager getFragmentManager(){
        return fm;
    }
}
