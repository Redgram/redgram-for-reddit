package com.matie.redgram.ui.common.views.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.matie.redgram.R;
import com.matie.redgram.data.models.main.items.submission.PostItem;

public class PostAdapter extends PostAdapterBase {
    private static final int TYPE_DEFAULT = 0;
    private static final int TYPE_SELF = 1;
    private static final int TYPE_IMAGE = 2;
    private static final int TYPE_ANIMATED = 3;
    private static final int TYPE_GALLERY = 4;

    public PostAdapter(Context context, int layoutResId) {
        super(context, layoutResId);
    }

    @Override
    public View getDynamicView(int type, View dynamicView, ViewGroup dynamicParent) {
        if(dynamicView == null){
            dynamicView = inflateViewByType(type, dynamicParent);
        }

        return dynamicView;
    }

    private View inflateViewByType(int type, ViewGroup dynamicParent) {
        final LayoutInflater inflater = LayoutInflater.from(dynamicParent.getContext());

        switch (type) {
            case TYPE_DEFAULT:
                return inflater.inflate(R.layout.post_item_default_view, dynamicParent, false);
            case TYPE_SELF:
                return inflater.inflate(R.layout.post_item_text_view, dynamicParent, false);
            case TYPE_IMAGE:
                return inflater.inflate(R.layout.post_item_image_view, dynamicParent, false);
            case TYPE_GALLERY:
                return inflater.inflate(R.layout.post_item_gallery_view, dynamicParent, false);
            case TYPE_ANIMATED:
                return inflater.inflate(R.layout.post_item_animated_view, dynamicParent, false);
            default:
                return null;
        }
    }

    @Override
    public int getItemViewType(int position) {
        int id = 0; //default
        PostItem item = getItem(position);

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
}
