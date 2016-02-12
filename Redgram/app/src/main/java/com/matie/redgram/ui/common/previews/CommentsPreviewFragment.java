package com.matie.redgram.ui.common.previews;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.Gson;
import com.matie.redgram.R;
import com.matie.redgram.data.managers.presenters.CommentsPresenterImpl;
import com.matie.redgram.data.models.main.items.PostItem;
import com.matie.redgram.data.models.main.items.comment.CommentBaseItem;
import com.matie.redgram.ui.AppComponent;
import com.matie.redgram.ui.common.base.BaseActivity;
import com.matie.redgram.ui.common.utils.widgets.DialogUtil;
import com.matie.redgram.ui.thread.components.CommentsComponent;
import com.matie.redgram.ui.thread.components.DaggerCommentsComponent;
import com.matie.redgram.ui.thread.components.ThreadComponent;
import com.matie.redgram.ui.thread.modules.CommentsModule;
import com.matie.redgram.ui.thread.views.CommentsActivity;
import com.matie.redgram.ui.thread.views.CommentsView;

import java.util.List;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by matie on 2016-01-07.
 */
public class CommentsPreviewFragment extends BasePreviewFragment implements CommentsView{

    @InjectView(R.id.title)
    TextView titleView;

    CommentsComponent component;

    @Inject
    CommentsPresenterImpl commentsPresenter;
    @Inject
    DialogUtil dialogUtil;

    List<CommentBaseItem> commentItems;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.preview_comments_fragment, container, false);
        ButterKnife.inject(this, view);

        String json = getArguments().getString(getMainKey());
        String title = (new Gson().fromJson(json, PostItem.class)).getTitle();
        titleView.setText(title);

        return view;
    }

    @Override public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    protected void setupComponent() {
        AppComponent appComponent = ((BaseActivity)getActivity()).component();
        ThreadComponent threadComponent = (ThreadComponent)appComponent;
        component = DaggerCommentsComponent.builder()
                .threadComponent(threadComponent)
                .commentsModule(new CommentsModule(this))
                .build();

        component.inject(this);
    }

    @Override
    protected void setupToolbar() {
        return;
    }

    @Override
    public void refreshPreview(Bundle bundle) {
        return;
    }

    public void refreshComments(List<CommentBaseItem> items){
        setComments(items);
        //refresh adapter
    }

    public void setComments(List<CommentBaseItem> items){
        this.commentItems = items;
    }

    @Override
    public void expandItem(int position) {

    }

    @Override
    public void collapseItem(int position) {

    }

    @Override
    public void expandAll(int position) {

    }

    @Override
    public void collapseAll(int position) {

    }

    @Override
    public void hideItem(int position) {

    }

    @Override
    public void scrollTo(int position) {

    }

    @Override
    public Fragment getFragment() {
        return this;
    }

}
