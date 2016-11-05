package com.matie.redgram.ui.common.utils.display;

import android.os.Bundle;
import android.view.View;

import com.matie.redgram.ui.common.base.Fragments;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

/**
 * Created by matie on 2015-10-31.
 */
public interface SlidingPanelControllerInterface {

    /**
     *  Shows the collapsed panel
     */
    public void showPanel();

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
    public void setPanelView(Fragments fragmentEnum, Bundle bundle);

    /**
     * Sets the dragable
     * @param view
     */
    public void setDraggable(View view);

    /**
     * Panel State
     *
     * @return
     */
    public SlidingUpPanelLayout.PanelState getPanelState();

}
