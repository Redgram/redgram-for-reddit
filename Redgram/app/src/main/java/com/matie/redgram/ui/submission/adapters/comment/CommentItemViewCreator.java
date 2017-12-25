package com.matie.redgram.ui.submission.adapters.comment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.matie.redgram.R;
import com.matie.redgram.data.models.main.items.submission.SubmissionItem;
import com.matie.redgram.data.models.main.items.submission.comment.CommentBaseItem;
import com.matie.redgram.ui.submission.adapters.SubmissionViewCreator;
import com.matie.redgram.ui.submission.adapters.SubmissionViewHolder;
import com.matie.redgram.ui.submission.adapters.comment.items.CommentBaseItemView;

public class CommentItemViewCreator implements SubmissionViewCreator {

    @Override
    public SubmissionViewHolder createViewHolder(ViewGroup parent, int viewType) {
        final LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        final CommentBaseItemView v =
                (CommentBaseItemView)inflater.inflate(R.layout.comment_item_view, parent, false);

        View dynamicView = getCommentView(viewType, v.getDynamicView(), v.getContainer());

        if (!dynamicView.equals(v.getDynamicView())) {
            v.getContainer().removeAllViews();
            v.getContainer().addView(dynamicView);
        }

        v.setDynamicView(dynamicView);

//        if (commentListener != null) {
//            return new CommentViewHolder(v, commentListener);
//        }

        return new CommentViewHolder(v);
    }

    private View getCommentView(int viewType, View dynamicView, FrameLayout container) {
        if (dynamicView == null) {
            dynamicView = inflateViewByType(viewType, container);
        }

        return dynamicView;
    }

    private View inflateViewByType(int viewType, FrameLayout container) {
        final LayoutInflater inflater = LayoutInflater.from(container.getContext());

        switch (viewType) {
            case COMMENT_TYPE_REGULAR:
                return inflater.inflate(R.layout.comment_item_regular_view, container, false);
            case COMMENT_TYPE_MORE:
                return inflater.inflate(R.layout.comment_item_more_item, container, false);
            default:
                return null;
        }
    }

    @Override
    public int getItemViewType(int position, SubmissionItem item) {
        CommentBaseItem commentBaseItem = (CommentBaseItem) item;

        switch (commentBaseItem.getCommentType()) {
            case REGULAR:
                return COMMENT_TYPE_REGULAR;
            case MORE:
                return COMMENT_TYPE_MORE;
            default:
                return COMMENT_TYPE_REGULAR;
        }
    }
}
