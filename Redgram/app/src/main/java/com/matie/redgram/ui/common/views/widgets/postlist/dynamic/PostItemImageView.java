package com.matie.redgram.ui.common.views.widgets.postlist.dynamic;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.PointF;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;

import com.afollestad.materialdialogs.MaterialDialog;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.backends.pipeline.PipelineDraweeControllerBuilder;
import com.facebook.drawee.drawable.ProgressBarDrawable;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.generic.GenericDraweeHierarchyBuilder;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.matie.redgram.R;
import com.matie.redgram.data.managers.preferences.PreferenceManager;
import com.matie.redgram.data.models.main.items.PostItem;
import com.matie.redgram.ui.App;
import com.matie.redgram.ui.common.main.MainActivity;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * todo: replace error image to reddit "not found" icon :)
 * Created by matie on 04/04/15.
 */
public class PostItemImageView extends PostItemSubView {


    @InjectView(R.id.image_view)
    SimpleDraweeView imageView;

    @InjectView(R.id.image_text_view)
    PostItemTextView postItemTextView;

    @InjectView(R.id.image_overlay)
    RelativeLayout imageOverlay;

    MainActivity mainActivity;
    SharedPreferences sharedPreferences;


    public PostItemImageView(Context context, AttributeSet attrs) {
        super(context, attrs);

        mainActivity = (MainActivity)getContext();
        sharedPreferences = ((App)mainActivity.getApplication()).getPreferenceManager().getSharedPreferences(PreferenceManager.POSTS_PREF);
    }

    @Override
    public void onFinishInflate(){
        super.onFinishInflate();
        ButterKnife.inject(this);
    }

    @Override
    public void setupView(PostItem item) {

        postItemTextView.setupView(item);

        imageView.setHierarchy(getDraweeHierarchy(item));
        imageView.setController(getDraweeController(item));

        if(item.isAdult() && !isNsfwEnabled()){
            imageOverlay.setVisibility(VISIBLE);
        }else{
            imageOverlay.setVisibility(GONE);
        }

    }

    private GenericDraweeHierarchy getDraweeHierarchy(PostItem item) {
        GenericDraweeHierarchyBuilder builder =
                new GenericDraweeHierarchyBuilder(getResources());
        GenericDraweeHierarchy hierarchy = builder
                .setFadeDuration(300)
                .setActualImageFocusPoint(new PointF(0.5f, 0f))
//                .setOverlay(getResources().getDrawable(R.drawable.image_nsfw_overlay))
                .build();
        return hierarchy;
    }

    private DraweeController getDraweeController(PostItem item) {
        Uri thumbnailUri = Uri.parse(item.getThumbnail());
        ImageRequest thumbnail = ImageRequestBuilder.newBuilderWithSource(thumbnailUri)
                .build();

        // TODO: 07/10/15 USE SHARED PREFERENCES TO STORE DEFAULT QUALITY, SEE IMGUR API
        if(item.isImgurContent()){

            int dot = item.getUrl().lastIndexOf('.');
            String newUrl = item.getUrl().substring(0, dot) + "l" + item.getUrl().substring(dot,item.getUrl().length());
            item.setUrl(newUrl);

            Log.d("IMGUR URL", item.getUrl());
        }

        Uri uri = Uri.parse(item.getUrl());
        ImageRequest request = ImageRequestBuilder.newBuilderWithSource(uri)
                .build();

        PipelineDraweeControllerBuilder builder = Fresco.newDraweeControllerBuilder()
                .setLowResImageRequest(thumbnail)
                .setImageRequest(request)
                .setOldController(imageView.getController());
        ;
        if(item.getType() == PostItem.Type.ANIMATED)
            builder.setAutoPlayAnimations(true);

        DraweeController controller = builder.build();
        return controller;
    }


    private boolean isNsfwEnabled(){
        return sharedPreferences.getBoolean(PreferenceManager.NSFW_KEY, false);
    }

    @OnClick(R.id.image_overlay)
    public void onClick(){
        handleOverlayClickEvent();
    }

    private void handleOverlayClickEvent(){
        if(imageOverlay.getVisibility() == VISIBLE){
            if(!isNsfwEnabled()){
                mainActivity.getDialogUtil().build()
                        .title("Disable NSFW setting?")
                        .positiveText("Yes")
                        .negativeText("Cancel")
                        .callback(new MaterialDialog.ButtonCallback() {
                            @Override
                            public void onPositive(MaterialDialog dialog) {
                                super.onPositive(dialog);

                                sharedPreferences.edit().putBoolean(PreferenceManager.NSFW_KEY, true).commit();
                                imageOverlay.setVisibility(GONE);
                            }

                            @Override
                            public void onNegative(MaterialDialog dialog) {
                                super.onNegative(dialog);
                            }
                        })
                        .show();
            }
            // TODO: 21/09/15 add animation, ex: fade in/out
        }
    }

}
