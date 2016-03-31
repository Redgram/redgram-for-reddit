package com.matie.redgram.ui.common.views.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.matie.redgram.R;
import com.matie.redgram.data.models.main.items.PostItem;

/**
 * Created by matie on 18/05/15.
 */
public class PostAdapter extends PostAdapterBase {
    private static final int TYPE_DEFAULT = 0;
    private static final int TYPE_SELF = 1;
    private static final int TYPE_IMAGE = 2;
    private static final int TYPE_ANIMATED = 3;
    private static final int TYPE_GALLERY = 4;
    private static final int TYPE_MAX_COUNT = TYPE_GALLERY+1;


    public PostAdapter(Context context, int layoutResId) {
        super(context, layoutResId);
    }

    @Override
    public View getDynamicView(int type, View dynamicView, ViewGroup dynamicParent) {
        if(dynamicView == null){
            dynamicView = inflateViewByType(type, dynamicView, dynamicParent);
        }
        return dynamicView;
    }

    /**
     * Click events
     * -----------------------------------------------------
     * default uses a web view
     * imgur fetches the image URL from IMGUR api
     * imgur gallery fetches set of images and creates a slideshow
     *
     * GIF - video sign
     * youtube calls youtube app
     * gifv becomes .mp4
     * gfycat fetches .mp4 url
     *
     * GALLERY - icon - different layout
     * loads the set of images from imgur
     *
     * @param type
     * @param dynamicView
     * @param dynamicParent
     * @return
     */
    private View inflateViewByType(int type, View dynamicView, ViewGroup dynamicParent) {
        switch (type){
            case TYPE_DEFAULT:
                dynamicView = getInflater().inflate(R.layout.post_item_default_view, dynamicParent, false);
                break;
            case TYPE_SELF:
                dynamicView = getInflater().inflate(R.layout.post_item_text_view, dynamicParent, false);
                break;
            case TYPE_IMAGE:
                dynamicView = getInflater().inflate(R.layout.post_item_image_view, dynamicParent, false);
                break;
            case TYPE_GALLERY:
                dynamicView = getInflater().inflate(R.layout.post_item_gallery_view, dynamicParent, false);
                break;
            case TYPE_ANIMATED:
                dynamicView = getInflater().inflate(R.layout.post_item_animated_view, dynamicParent, false);
                break;
        }
        return dynamicView;
    }

    @Override
    public int getItemType(int position) {
        int id = 0; //default
        PostItem item = ((PostItem)getItem(position));

        if(item.getType() == PostItem.Type.DEFAULT) {
            id = TYPE_DEFAULT;
            return id;
        }
        if(item.getType() == PostItem.Type.SELF) {
            id = TYPE_SELF;
            return id;
        }
        //todo: IMGUR images should be of this type, and not default. Modify when integrating IMGUR API
        if(item.getType() == PostItem.Type.IMAGE){
            id = TYPE_IMAGE;
            return id;
        }
        if(item.getType() == PostItem.Type.IMGUR_GALLERY || item.getType() == PostItem.Type.IMGUR_ALBUM ||
                item.getType() == PostItem.Type.IMGUR_CUSTOM_GALLERY){
            id = TYPE_GALLERY;
            return id;
        }
        if(item.getType() == PostItem.Type.GIF || item.getType() == PostItem.Type.YOUTUBE
                || item.getType() == PostItem.Type.GFYCAT){
            id = TYPE_ANIMATED;
            return id;
        }
        return id;
    }

    @Override
    public int getTypeCount() {
        return TYPE_MAX_COUNT;
    }
}
