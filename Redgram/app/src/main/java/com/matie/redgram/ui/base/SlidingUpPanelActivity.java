package com.matie.redgram.ui.base;

import android.os.Bundle;
import android.support.design.widget.Snackbar;

import com.matie.redgram.R;
import com.matie.redgram.ui.common.utils.display.SlidingPanelControllerInterface;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by matie on 2016-03-09.
 */
public abstract class SlidingUpPanelActivity extends BaseActivity
        implements SlidingUpPanelLayout.PanelSlideListener, SlidingPanelControllerInterface {

    @InjectView(R.id.slide_up_panel_layout)
    SlidingUpPanelLayout slidingUpPanelLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.inject(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.reset(this);
    }

    public SlidingUpPanelLayout getSlidingUpPanelLayout() {
        return slidingUpPanelLayout;
    }

    public class PanelSnackBarCallback extends Snackbar.Callback{
        @Override
        public void onDismissed(Snackbar snackbar, int event) {
            super.onDismissed(snackbar, event);
            setPanelHeight(48);
        }
    }

}
