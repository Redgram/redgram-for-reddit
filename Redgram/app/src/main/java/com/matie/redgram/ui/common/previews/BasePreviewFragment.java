package com.matie.redgram.ui.common.previews;

import android.os.Bundle;

import com.matie.redgram.R;
import com.matie.redgram.ui.common.base.BaseFragment;

/**
 * Created by matie on 2015-11-03.
 */
public abstract class BasePreviewFragment extends BaseFragment {
    public abstract void refreshPreview(Bundle bundle);

    public String getMainKey(){
        return getResources().getString(R.string.main_data_key);
    }

    public String getMainPos(){
        return getResources().getString(R.string.main_data_position);
    }

    public String getLocalCacheKey(){
        return getResources().getString(R.string.local_cache_key);
    }

}
