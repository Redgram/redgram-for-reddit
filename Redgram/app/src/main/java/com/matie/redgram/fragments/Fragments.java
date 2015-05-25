package com.matie.redgram.fragments;

import android.support.v4.app.Fragment;

import com.matie.redgram.fragments.DrawerFragments.FragmentHome;

/**
 * Created by matie on 30/03/15.
 */
public enum Fragments {

    HOME(FragmentHome.class);

    final Class<? extends Fragment> fragment;

    private Fragments(Class<? extends Fragment> fragment){
        this.fragment = fragment;
    }

    public String getFragment() {
        return fragment.getName();
    }
}
