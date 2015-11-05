package com.matie.redgram.data.managers.images;

import android.content.Context;
import android.graphics.PointF;
import android.net.Uri;
import android.support.annotation.Nullable;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.backends.pipeline.PipelineDraweeControllerBuilder;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.generic.GenericDraweeHierarchyBuilder;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;

/**
 * Created by matie on 2015-11-01.
 */
public class ImageManager {

    private Context context;
    private final GenericDraweeHierarchy genericDraweeHierarchy;

    public ImageManager(Context context) {
        this.context = context;
        this.genericDraweeHierarchy = buildGenericDraweeHierarchy();
    }

    public GenericDraweeHierarchy getGenericDraweeHierarchy() {
        return genericDraweeHierarchy;
    }

    //base builder
    public PipelineDraweeControllerBuilder getControllerBuilder(SimpleDraweeView view, String imgUrl, @Nullable String thumbUrl){
        PipelineDraweeControllerBuilder builder = Fresco.newDraweeControllerBuilder();

        //low res image
        if(thumbUrl != null){
            Uri thumbnailUri = Uri.parse(thumbUrl);
            ImageRequest thumbnail = ImageRequestBuilder.newBuilderWithSource(thumbnailUri)
                    .build();
            builder.setLowResImageRequest(thumbnail);
        }

        //main image
        Uri uri = Uri.parse(imgUrl);
        ImageRequest request = ImageRequestBuilder.newBuilderWithSource(uri)
                .build();
        builder.setImageRequest(request);

        if(view.hasController()){
            builder.setOldController(view.getController());
        }

        return builder;
    }

    private final GenericDraweeHierarchy buildGenericDraweeHierarchy() {
        GenericDraweeHierarchyBuilder builder =
                new GenericDraweeHierarchyBuilder(context.getResources());
        GenericDraweeHierarchy hierarchy = builder
                .setFadeDuration(300)
                .setActualImageFocusPoint(new PointF(0.5f, 0f))
                .build();
        return hierarchy;
    }


}
