package com.matie.redgram.ui.search.views;

import com.matie.redgram.ui.common.views.BaseContextView;
import com.matie.redgram.ui.common.views.ContentView;
import com.matie.redgram.ui.common.views.widgets.postlist.PostRecyclerView;

/**
 * Created by matie on 12/04/15.
 */
public interface SearchView extends ContentView {
    public void showToolbar();
    public void hideToolbar();
}
