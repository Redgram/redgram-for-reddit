package com.matie.redgram.ui.home.views;

import android.os.Bundle;

import com.matie.redgram.ui.common.base.Fragments;
import com.matie.redgram.ui.common.views.BaseContextView;
import com.matie.redgram.ui.common.views.ContentView;
import com.matie.redgram.ui.common.views.widgets.postlist.PostRecyclerView;

/**
 * Created by matie on 12/04/15.
 * // TODO: 2015-10-31 make it interact with the siding panel
 */
public interface HomeView extends ContentView {
    void showToolbar();
    void hideToolbar();
}
