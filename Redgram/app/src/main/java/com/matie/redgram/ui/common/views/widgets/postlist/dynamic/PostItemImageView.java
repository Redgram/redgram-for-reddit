package com.matie.redgram.ui.common.views.widgets.postlist.dynamic;

import android.content.Context;
import android.graphics.drawable.Animatable;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

import com.facebook.drawee.controller.BaseControllerListener;
import com.facebook.drawee.controller.ControllerListener;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.image.ImageInfo;
import com.matie.redgram.R;
import com.matie.redgram.data.managers.media.video.ImageManager;
import com.matie.redgram.data.models.main.items.PostItem;
import com.matie.redgram.ui.posts.views.LinksView;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * todo: replace error image to reddit "not found" icon :)
 * Created by matie on 04/04/15.
 */
public class PostItemImageView extends PostItemSubView{


    @InjectView(R.id.image_view)
    SimpleDraweeView imageView;

    @InjectView(R.id.image_text_view)
    PostItemTextView postItemTextView;

    @InjectView(R.id.image_overlay)
    RelativeLayout imageOverlay;

    PostItem postItem;
    int position;
    boolean imageLoaded;
    LinksView listener;

    private ControllerListener<? super ImageInfo> controllerListener;

    public PostItemImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void onFinishInflate(){
        super.onFinishInflate();
        ButterKnife.inject(this);
    }

    @Override
    public void setupView(PostItem item, int position, LinksView listener) {
        this.imageLoaded = false;
        this.postItem = item;
        this.listener = listener;
        this.position = position;

        postItemTextView.setupView(item, position, listener);

//        imageUrl = imageUrl.substring(0,imageUrl.lastIndexOf('.')) + "l" + imageUrl.substring(imageUrl.lastIndexOf('.'));

        ImageManager.newImageBuilder(getContext())
                .setImageView(imageView)
                .setImage(postItem.getUrl(), true)
                .includeOldController()
                .setListener(getControllerListener())
                .build();

        if(item.isAdult() && (!getUserPrefs().isOver18() || getUserPrefs().isDisableNsfwPreview())){
            imageOverlay.setVisibility(VISIBLE);
        }else{
            imageOverlay.setVisibility(GONE);
        }

    }

    private ControllerListener<? super ImageInfo> getControllerListener() {
        ControllerListener controllerListener = new BaseControllerListener<ImageInfo>(){

            @Override
            public void onFinalImageSet(String id, ImageInfo imageInfo, Animatable animatable) {
                imageLoaded = true;
            }

        };
        return controllerListener;
    }

    @OnClick(R.id.image_overlay)
    public void onOverlayClick(){
        listener.callAgeConfirmDialog();
    }

    @OnClick(R.id.image_view)
    public void onImageClick(){
        listener.viewImageMedia(position, imageLoaded);
    }


}
