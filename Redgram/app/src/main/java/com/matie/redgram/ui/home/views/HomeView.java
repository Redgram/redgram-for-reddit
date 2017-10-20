package com.matie.redgram.ui.home.views;

import com.matie.redgram.data.models.main.base.Listing;
import com.matie.redgram.data.models.main.items.PostItem;
import com.matie.redgram.ui.common.views.ContentView;

/**
 * Created by matie on 12/04/15.
 * // TODO: 2015-10-31 make it interact with the siding panel
 */
public interface HomeView extends ContentView {
    void showToolbar();
    void hideToolbar();
    void loadLinksContainer(Listing<PostItem> links);
}
