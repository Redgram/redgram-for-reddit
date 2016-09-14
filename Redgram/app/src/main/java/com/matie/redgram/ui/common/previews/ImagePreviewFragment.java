package com.matie.redgram.ui.common.previews;

import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.davemorrissey.labs.subscaleview.ImageSource;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;
import com.facebook.binaryresource.BinaryResource;
import com.facebook.binaryresource.FileBinaryResource;
import com.facebook.cache.common.CacheKey;
import com.facebook.datasource.BaseDataSubscriber;
import com.facebook.datasource.DataSource;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.imagepipeline.cache.DefaultCacheKeyFactory;
import com.facebook.imagepipeline.core.ImagePipelineFactory;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.google.gson.Gson;
import com.matie.redgram.R;
import com.matie.redgram.data.models.main.items.PostItem;
import com.matie.redgram.ui.common.base.SlidingUpPanelActivity;
import com.matie.redgram.ui.common.main.MainActivity;
import com.matie.redgram.ui.thread.ThreadActivity;

import java.io.File;
import java.util.concurrent.Executor;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by matie on 2015-11-03.
 */
public class ImagePreviewFragment extends BasePreviewFragment {


    @InjectView(R.id.image_preview)
    SubsamplingScaleImageView imagePreview;
    @InjectView(R.id.top_banner)
    LinearLayout topBanner;
    @InjectView(R.id.top_banner_title)
    TextView topBannerTitle;
    @InjectView(R.id.close_fragment)
    ImageView closeFragment;

    String filePath = "";
    PostItem postItem;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.preview_image_fragment, container, false);
        ButterKnife.inject(this, view);

        if(getArguments().containsKey(getMainKey())){
            String data = getArguments().getString(getMainKey());
            postItem = new Gson().fromJson(data, PostItem.class);
            topBannerTitle.setText(postItem.getTitle());
        }

        if(getArguments().containsKey(getLocalCacheKey())){
            String path = getArguments().getString(getLocalCacheKey());
            imagePreview.setImage(ImageSource.uri(path));
            imagePreview.setVisibility(View.VISIBLE);
            filePath = path;
        }else{
            preFetchToDiskCacheAndDisplay();
        }

        if(getContext() instanceof MainActivity){
            MainActivity mainActivity = (MainActivity)getContext();
            mainActivity.setDraggable(topBanner);
        }

        if(getContext() instanceof ThreadActivity){
            topBanner.setVisibility(View.GONE);
        }

        return view;
    }

    private void preFetchToDiskCacheAndDisplay() {
        Uri uri = Uri.parse(postItem.getUrl());
        ImageRequest request = ImageRequestBuilder.newBuilderWithSource(uri)
                .build();
        DataSource<Void> dataSource
                = Fresco.getImagePipeline().prefetchToDiskCache(request, getContext());
        dataSource.subscribe(new BaseDataSubscriber<Void>() {
            @Override
            protected void onNewResultImpl(DataSource<Void> dataSource) {
                displayCachedImageFromBackgroundThread(request);
            }

            @Override
            protected void onFailureImpl(DataSource<Void> dataSource) {

            }
        }, new Executor() {
            @Override
            public void execute(Runnable command) {
                command.run();
            }
        });
    }

    @Override public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    protected void setupComponent() {

    }

    @Override
    protected void setupToolbar() {

    }

    @Override
    public void refreshPreview(Bundle bundle) {

        if(bundle.containsKey(getLocalCacheKey())){

            String path = bundle.getString(getLocalCacheKey());
            if(!path.equalsIgnoreCase(filePath)){
                ImageSource imageSource = ImageSource.uri(path);
                imagePreview.setImage(imageSource);
                filePath = path;
            }

        }

        if(bundle.containsKey(getMainKey())){
            String data = bundle.getString(getMainKey());
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
        SlidingUpPanelActivity activity = (SlidingUpPanelActivity)getContext();
        activity.hidePanel();
    }

    private void displayCachedImageFromBackgroundThread(ImageRequest request){
        CacheKey cacheKey = DefaultCacheKeyFactory.getInstance().getEncodedCacheKey(ImageRequest.fromUri(request.getSourceUri()));

        if(cacheKey != null){
            BinaryResource resource = ImagePipelineFactory.getInstance().getMainDiskStorageCache().getResource(cacheKey);
            if(resource != null){
                File localFile = ((FileBinaryResource) resource).getFile();
                if(localFile != null){
                    Handler handler = new Handler(Looper.getMainLooper());
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            imagePreview.setImage(ImageSource.uri(localFile.getPath()));
                        }
                    });
                }
            }

        }
    }

}
