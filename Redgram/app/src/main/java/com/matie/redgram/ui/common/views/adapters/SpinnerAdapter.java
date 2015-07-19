package com.matie.redgram.ui.common.views.adapters;

import android.content.Context;
import android.widget.ArrayAdapter;

import java.util.List;

/**
 * Created by matie on 18/07/15.
 */
public class SpinnerAdapter extends ArrayAdapter<String> {
    public SpinnerAdapter(Context context, int resource) {
        super(context, resource);
    }

    public SpinnerAdapter(Context context, int resource, int textViewResourceId) {
        super(context, resource, textViewResourceId);
    }

    public SpinnerAdapter(Context context, int resource, int textViewResourceId, List<String> objects) {
        super(context, resource, textViewResourceId, objects);
    }

    public SpinnerAdapter(Context context, int resource, String[] objects) {
        super(context, resource, objects);
    }

    public SpinnerAdapter(Context context, int resource, int textViewResourceId, String[] objects) {
        super(context, resource, textViewResourceId, objects);
    }

    public SpinnerAdapter(Context context, int resource, List<String> objects) {
        super(context, resource, objects);
    }
}
