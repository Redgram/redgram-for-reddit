package com.matie.redgram.ui.common.previews;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.matie.redgram.R;

/**
 * Created by matie on 2015-11-03.
 */
public abstract class BasePreviewFragment extends Fragment {
    public abstract void refreshPreview(Bundle bundle);

    public String getMainKey(){
        return getResources().getString(R.string.main_data_key);
    }

    public String getLocalCacheKey(){
        return getResources().getString(R.string.local_cache_key);
    }
}
