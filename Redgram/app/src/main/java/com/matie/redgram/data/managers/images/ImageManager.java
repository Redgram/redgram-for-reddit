package com.matie.redgram.data.managers.images;

import android.content.Context;
import android.graphics.PointF;
import android.net.Uri;
import android.support.annotation.Nullable;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.backends.pipeline.PipelineDraweeControllerBuilder;
import com.facebook.drawee.drawable.ProgressBarDrawable;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.generic.GenericDraweeHierarchyBuilder;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;

/**
 * Created by matie on 2015-11-01.
 */
public class ImageManager {

    public static Builder newImageBuilder(Context context){
        return new Builder(context);
    }

    public static class Builder{

        private PipelineDraweeControllerBuilder controllerBuilder;
        private GenericDraweeHierarchy genericDraweeHierarchy;
        private Context context;

        private Builder(Context context) {
            this.context = context;
            this.genericDraweeHierarchy = buildGenericDraweesHierarchy(context);
            this.controllerBuilder = getControllerBuilderInstance();
        }

        private static GenericDraweeHierarchy buildGenericDraweesHierarchy(Context context) {
            GenericDraweeHierarchyBuilder builder =
                    new GenericDraweeHierarchyBuilder(context.getResources());
            GenericDraweeHierarchy hierarchy = builder
                    .setFadeDuration(300)
                    .setActualImageFocusPoint(new PointF(0.5f, 0f))
                    .setProgressBarImage(new ProgressBarDrawable())
                    .build();
            return hierarchy;
        }

        private static PipelineDraweeControllerBuilder getControllerBuilderInstance(){
            return Fresco.newDraweeControllerBuilder();
        }

    }
}
