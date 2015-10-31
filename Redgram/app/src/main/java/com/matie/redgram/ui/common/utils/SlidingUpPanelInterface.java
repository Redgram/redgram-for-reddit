package com.matie.redgram.ui.common.utils;

import android.support.v4.app.Fragment;

import com.matie.redgram.ui.common.base.Fragments;

/**
 * Created by matie on 2015-10-31.
 */
public interface SlidingUpPanelInterface {
    /**
     *  Hides the panel
     */
    public void hidePanel();

    /**
     * Reveals the panel. If the isAnchored is set to true, show the panel to a specified height
     */
    public void togglePanel();

    /**
     * Sets the panel new height, this is different from hiding the panel
     * @param height
     */
    public void setPanelHeight(int height);

    /**
     * Changes the panel view
     */
    public void setPanelView(Fragments fragmentName);

}
