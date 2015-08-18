package com.matie.redgram.ui.common.views.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.matie.redgram.R;
import com.matie.redgram.data.models.PostItem;

/**
 * Created by matie on 18/05/15.
 */
public class PostAdapter extends PostAdapterBase {
    private static final int TYPE_DEFAULT = 0;
    private static final int TYPE_SELF = 1;
    private static final int TYPE_IMAGE = 2;
    private static final int TYPE_GIF = 3;
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
//        Log.d(" GET DYNAMIC VIEW", dynamicView + "");
        return dynamicView;
    }

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
            case TYPE_GIF:
                dynamicView = getInflater().inflate(R.layout.post_item_gif_view, dynamicParent, false);
                break;
        }
        return dynamicView;
    }

    @Override
    public int getItemType(int position) {
        int id = 0; //default
        if(getItem(position).getType() == PostItem.Type.DEFAULT) {
            id = TYPE_DEFAULT;
            return id;
        }
        if(getItem(position).getType() == PostItem.Type.SELF) {
            id = TYPE_SELF;
            return id;
        }
        //todo: IMGUR images should be of this type, and not default. Modify when integrating IMGUR API
        if(getItem(position).getType() == PostItem.Type.IMAGE){
            id = TYPE_IMAGE;
            return id;
        }
        if(getItem(position).getType() == PostItem.Type.IMGUR_GALLERY || getItem(position).getType() == PostItem.Type.IMGUR_ALBUM ||
                getItem(position).getType() == PostItem.Type.IMGUR_CUSTOM_GALLERY || getItem(position).getType() == PostItem.Type.IMGUR_TAG ||
                getItem(position).getType() == PostItem.Type.IMGUR_SUBREDDIT){
            id = TYPE_GALLERY;
            return id;
        }
        if(getItem(position).getType() == PostItem.Type.GIF){
            id = TYPE_GIF;
            return id;
        }
        return id;
    }

    @Override
    public int getTypeCount() {
        return TYPE_MAX_COUNT;
    }
}
