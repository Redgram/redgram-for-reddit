package com.matie.redgram.ui.thread.views;

import com.matie.redgram.data.models.main.items.comment.CommentBaseItem;
import com.matie.redgram.ui.common.views.BaseContextView;

import java.util.List;

/**
 * Created by matie on 2016-01-04.
 */
public interface CommentsView extends BaseContextView {
     //comments related
     void expandItem(int position);
     void collapseItem(int position);
     void expandAll(int position);
     void collapseAll(int position);
     void hideItem(int position);
     void scrollTo(int position);
     void loadMore(int position);
     void setItems(List<CommentBaseItem> items);
}
