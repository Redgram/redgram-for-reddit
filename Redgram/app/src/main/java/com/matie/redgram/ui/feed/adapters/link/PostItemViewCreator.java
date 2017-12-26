package com.matie.redgram.ui.feed.adapters.link;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.matie.redgram.R;
import com.matie.redgram.data.models.main.items.submission.PostItem;
import com.matie.redgram.data.models.main.items.submission.SubmissionItem;
import com.matie.redgram.ui.feed.adapters.SubmissionViewCreator;
import com.matie.redgram.ui.feed.adapters.SubmissionViewHolder;
import com.matie.redgram.ui.feed.adapters.link.items.PostItemView;

public class PostItemViewCreator implements SubmissionViewCreator {

    @Override
    public SubmissionViewHolder createViewHolder(ViewGroup parent, int viewType) {
        PostItemView v = (PostItemView) LayoutInflater
                            .from(parent.getContext())
                            .inflate(R.layout.post_item_view, parent, false);

        View dynamicView = getDynamicView(viewType, v.getDynamicView(), v.getDynamicParent());

        if (dynamicView != null) {
            if (!dynamicView.equals(v.getDynamicView())) {
                v.getDynamicParent().removeAllViews();
                v.getDynamicParent().addView(dynamicView);
            }

            v.setDynamicView(dynamicView);
        }

//        if (singleLinkView != null) {
//            return new PostViewHolder(v, singleLinkView);
//        }

        return new PostViewHolder(v);
    }

    private View getDynamicView(int viewType, View dynamicView, ViewGroup dynamicParent) {
        if(dynamicView == null){
            dynamicView = inflateViewByType(viewType, dynamicParent);
        }

        return dynamicView;
    }

    private View inflateViewByType(int type, ViewGroup dynamicParent) {
        final LayoutInflater inflater = LayoutInflater.from(dynamicParent.getContext());

        switch (type) {
            case POST_TYPE_DEFAULT:
                return inflater.inflate(R.layout.post_item_default_view, dynamicParent, false);
            case POST_TYPE_SELF:
                return inflater.inflate(R.layout.post_item_text_view, dynamicParent, false);
            case POST_TYPE_IMAGE:
                return inflater.inflate(R.layout.post_item_image_view, dynamicParent, false);
            case POST_TYPE_GALLERY:
                return inflater.inflate(R.layout.post_item_gallery_view, dynamicParent, false);
            case POST_TYPE_ANIMATED:
                return inflater.inflate(R.layout.post_item_animated_view, dynamicParent, false);
            default:
                return null;
        }
    }

    @Override
    public int getItemViewType(int position, SubmissionItem item) {
        if (!(item instanceof PostItem)) return -1;

        PostItem postItem = (PostItem) item;

        switch (postItem.getType()) {
            case DEFAULT:
                return POST_TYPE_DEFAULT;
            case SELF:
                return POST_TYPE_SELF;
            case IMAGE:
                return POST_TYPE_IMAGE;
            case IMGUR_GALLERY:
            case IMGUR_CUSTOM_GALLERY:
            case IMGUR_ALBUM:
                return POST_TYPE_GALLERY;
            case GIF:
            case YOUTUBE:
            case GFYCAT:
                return POST_TYPE_ANIMATED;
            default:
                return -1;
        }
    }
}
