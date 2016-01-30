package com.matie.redgram.ui.common.previews;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.Gson;
import com.matie.redgram.R;
import com.matie.redgram.data.models.main.items.PostItem;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by matie on 2016-01-07.
 */
public class CommentsPreviewFragment extends BasePreviewFragment {

    @InjectView(R.id.title)
    TextView titleView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.preview_comments_fragment, container, false);
        ButterKnife.inject(this, view);

        String json = getArguments().getString(getMainKey());
        String title = (new Gson().fromJson(json, PostItem.class)).getTitle();
        titleView.setText(title);

        return view;
    }

    @Override public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void refreshPreview(Bundle bundle) {

    }

}
