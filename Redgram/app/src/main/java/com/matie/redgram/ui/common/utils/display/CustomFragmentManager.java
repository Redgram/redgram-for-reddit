package com.matie.redgram.ui.common.utils.display;

import android.content.Context;
import android.support.v4.app.Fragment;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.EmptyStackException;
import java.util.Iterator;
import java.util.List;

public class CustomFragmentManager extends android.support.v4.app.FragmentManager.FragmentLifecycleCallbacks {

    private List<WeakReference<Fragment>> fragmentList = new ArrayList<>();

    @Override
    public void onFragmentAttached(android.support.v4.app.FragmentManager fm, Fragment f, Context context) {
        fragmentList.add(new WeakReference<>(f));
    }

    @Override
    public void onFragmentDetached(android.support.v4.app.FragmentManager fm, Fragment fragment) {
        for (Iterator<WeakReference<Fragment>> iterator = fragmentList.iterator() ; iterator.hasNext();) {
            WeakReference<Fragment> reference = iterator.next();
            if (reference.get() == fragment) {
                iterator.remove();
                break;
            }
        }
    }

    public List<WeakReference<Fragment>> getFragments() {
        return fragmentList;
    }

    public Fragment getActiveFragment() {
        if (fragmentList == null || fragmentList.isEmpty()) throw new EmptyStackException();
        int currentFragmentPos = fragmentList.size() - 1;
        return fragmentList.get(currentFragmentPos).get();
    }

    public boolean contains(Fragment fragment) {
        for (WeakReference<Fragment> reference : fragmentList) {
            if (reference.get() == fragment) {
                return true;
            }
        }
        return false;
    }

}
