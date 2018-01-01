package com.matie.redgram.ui.feed.comments.views;

import com.matie.redgram.data.models.main.items.submission.comment.CommentBaseItem;
import com.matie.redgram.ui.base.BaseView;

import java.util.List;

public interface CommentsView extends BaseView {
     void expandItem(int position);
     void collapseItem(int position);
     void expandAll(int position);
     void collapseAll(int position);
     void hideItem(int position);
     void scrollTo(int position);
     void loadMore(int position);
     void setItems(List<CommentBaseItem> items);
}
