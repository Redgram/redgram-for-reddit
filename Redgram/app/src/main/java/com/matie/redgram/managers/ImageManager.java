package com.matie.redgram.managers;

import android.graphics.PointF;
import android.net.Uri;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.backends.pipeline.PipelineDraweeControllerBuilder;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;

/**
 * Created by matie on 20/04/15.
 *
 * This class is used to render (animated) images set in {@link com.matie.redgram.views.widgets.PostList.PostItemView}
 * bindTo() method.
 */
public class ImageManager {

    private Uri uri;
    private SimpleDraweeView view;
    private ImageRequest request;
    private PipelineDraweeControllerBuilder builder;
    private DraweeController controller;

    public ImageManager(SimpleDraweeView view, Uri uri){
        this.uri = uri;
        this.view = view;
        request = getImageRequestBuilder();
        builder = getPiplineBuilder();
        controller = builder.build();
    }


    public ImageRequest getImageRequestBuilder() {
        return ImageRequestBuilder.newBuilderWithSource(uri)
                .setProgressiveRenderingEnabled(true)
                .build();
    }

    public PipelineDraweeControllerBuilder getPiplineBuilder() {
        PipelineDraweeControllerBuilder builder = Fresco.newDraweeControllerBuilder()
                .setImageRequest(request)
                .setOldController(view.getController());

        if(uri.toString().endsWith(".gif"))
            builder.setAutoPlayAnimations(true);

        return builder;
    }

    public void setImageController(){
        view.setController(controller);
    }
    public void setImageFocusPoint(PointF pf){
        view.getHierarchy().setActualImageFocusPoint(pf);
    }
}
