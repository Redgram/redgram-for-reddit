package com.matie.redgram.ui.submission.adapters.link;

import com.matie.redgram.data.models.main.items.submission.PostItem;
import com.matie.redgram.data.models.main.items.submission.SubmissionItem;
import com.matie.redgram.ui.submission.adapters.SubmissionViewHolder;
import com.matie.redgram.ui.submission.adapters.link.items.PostItemView;
import com.matie.redgram.ui.submission.links.views.SingleLinkView;


public class PostViewHolder extends SubmissionViewHolder {

    private PostItemView itemView;

    public PostViewHolder(PostItemView itemView) {
        super(itemView);
        this.itemView = itemView;
    }

    public PostViewHolder(PostItemView itemView, SingleLinkView listener) {
        super(itemView);
        this.itemView = itemView;

        itemView.setListener(listener);
    }

    public PostItemView getItemView() {
        return itemView;
    }

    @Override
    public void bind(int position, SubmissionItem item) {
        PostItem postItem = (PostItem) item;
        itemView.bindTo(postItem, position);
    }
}
