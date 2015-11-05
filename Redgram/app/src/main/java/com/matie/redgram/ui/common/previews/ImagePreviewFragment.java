package com.matie.redgram.ui.common.previews;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.davemorrissey.labs.subscaleview.ImageSource;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;
import com.google.gson.Gson;
import com.matie.redgram.R;
import com.matie.redgram.data.models.main.items.PostItem;
import com.matie.redgram.ui.common.main.MainActivity;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by matie on 2015-11-03.
 */
public class ImagePreviewFragment extends BasePreviewFragment {

    public static final String LOCAL_CACHE_KEY = "image_preview_bundle";
    public static final String MAIN_DATA = "image_preview_data";

    @InjectView(R.id.image_preview)
    SubsamplingScaleImageView imagePreview;
    @InjectView(R.id.top_banner_title)
    TextView topBannerTitle;
    @InjectView(R.id.close_fragment)
    ImageView closeFragment;

    String filePath = "";
    PostItem postItem;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.image_preview_fragment, container, false);
        ButterKnife.inject(this, view);

        if(getArguments().containsKey(LOCAL_CACHE_KEY)){
            String path = getArguments().getString(LOCAL_CACHE_KEY);
            imagePreview.setImage(ImageSource.uri(path));
            imagePreview.setVisibility(View.VISIBLE);
            filePath = path;
        }

        if(getArguments().containsKey(MAIN_DATA)){
            String data = getArguments().getString(MAIN_DATA);
            postItem = new Gson().fromJson(data, PostItem.class);
            topBannerTitle.setText(postItem.getTitle());
        }

        return view;
    }

    @Override public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void refreshPreview(Bundle bundle) {

        if(bundle.containsKey(LOCAL_CACHE_KEY)){

            String path = bundle.getString(LOCAL_CACHE_KEY);
            if(!path.equalsIgnoreCase(filePath)){
                ImageSource imageSource = ImageSource.uri(path);
                imagePreview.setImage(imageSource);
                filePath = path;
            }

        }

        if(bundle.containsKey(MAIN_DATA)){
            String data = bundle.getString(MAIN_DATA);
            postItem = new Gson().fromJson(data, PostItem.class);
            if(!topBannerTitle.getText().toString().equalsIgnoreCase(postItem.getTitle()))
                topBannerTitle.setText(postItem.getTitle());
        }


        if(imagePreview.getVisibility() == View.GONE){
            imagePreview.setVisibility(View.VISIBLE);
        }

    }

    @OnClick(R.id.close_fragment)
    public void OnCloseFragment(){
        imagePreview.setVisibility(View.GONE);
        MainActivity activity = (MainActivity)getContext();
        activity.hidePanel();
    }
}
