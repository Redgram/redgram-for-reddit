package com.matie.redgram.ui.feed.adapters;

import android.view.ViewGroup;

import com.matie.redgram.data.models.main.items.submission.PostItem;
import com.matie.redgram.data.models.main.items.submission.SubmissionItem;
import com.matie.redgram.data.models.main.items.submission.comment.CommentBaseItem;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class DefaultSubmissionViewCreator implements SubmissionViewCreator {

    private static final Set<Integer> POST_TYPE_SET =
            new HashSet<>(Arrays.asList(
                    POST_TYPE_DEFAULT, POST_TYPE_SELF, POST_TYPE_IMAGE,
                    POST_TYPE_ANIMATED, POST_TYPE_GALLERY));

    private static final Set<Integer> COMMENT_TYPE_SET =
            new HashSet<>(Arrays.asList(COMMENT_TYPE_REGULAR, COMMENT_TYPE_MORE));

    private SubmissionViewCreator postItemViewCreator;
    private SubmissionViewCreator commentItemViewCreator;

    public DefaultSubmissionViewCreator(SubmissionViewCreator postItemViewCreator,
                                        SubmissionViewCreator commentItemViewCreator) {
        this.postItemViewCreator = postItemViewCreator;
        this.commentItemViewCreator = commentItemViewCreator;
    }

    @Override
    public SubmissionViewHolder createViewHolder(ViewGroup parent, int viewType) {
        if (POST_TYPE_SET.contains(viewType) && postItemViewCreator != null) {
            return postItemViewCreator.createViewHolder(parent, viewType);
        } else if(COMMENT_TYPE_SET.contains(viewType) && commentItemViewCreator != null) {
            return commentItemViewCreator.createViewHolder(parent, viewType);
        } else {
            return null;
        }
    }

    @Override
    public int getItemViewType(int position, SubmissionItem item) {
        if (item instanceof PostItem && postItemViewCreator != null) {
            return postItemViewCreator.getItemViewType(position, item);
        } else if (item instanceof CommentBaseItem && commentItemViewCreator != null) {
            return commentItemViewCreator.getItemViewType(position, item);
        }

        return -1;
    }

}
