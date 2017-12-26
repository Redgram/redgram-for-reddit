package com.matie.redgram.ui.feed.adapters;

import android.view.ViewGroup;

import com.matie.redgram.data.models.main.items.submission.SubmissionItem;

public interface SubmissionViewCreator {

    int POST_TYPE_DEFAULT = 0;
    int POST_TYPE_SELF = 1;
    int POST_TYPE_IMAGE = 2;
    int POST_TYPE_ANIMATED = 3;
    int POST_TYPE_GALLERY = 4;

    int COMMENT_TYPE_REGULAR = 5;
    int COMMENT_TYPE_MORE = 6;

    SubmissionViewHolder createViewHolder(ViewGroup parent, int viewType);

    int getItemViewType(int position, SubmissionItem item);
}
