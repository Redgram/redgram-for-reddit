package com.matie.redgram.ui.common.previews;

import android.app.Activity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.matie.redgram.R;
import com.matie.redgram.data.models.main.items.PostItem;
import com.matie.redgram.ui.common.base.BaseActivity;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by matie on 2016-01-06.
 */
public class PostPreviewFragment extends BasePreviewFragment {

    Activity activity;

    @InjectView(R.id.content)
    TextView mContent;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.preview_post_fragment, container, false);
        ButterKnife.inject(this, view);

        String json = getArguments().getString(getMainKey());
        String content = (new Gson().fromJson(json, PostItem.class)).getText();
        if(content.length() > 0){
            mContent.setText(content);
        }else{
            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
            params.gravity = Gravity.CENTER_HORIZONTAL;
            params.setMargins(0, 100, 0, 0);
            mContent.setLayoutParams(params);
        }

        return view;
    }



    @Override public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        this.activity = (BaseActivity)getActivity();
    }


    @Override
    public void refreshPreview(Bundle bundle) {

    }


}
