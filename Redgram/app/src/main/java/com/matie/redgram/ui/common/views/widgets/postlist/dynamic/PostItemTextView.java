package com.matie.redgram.ui.common.views.widgets.postlist.dynamic;

import android.content.Context;
import android.content.res.Resources;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.TextView;

import com.matie.redgram.R;
import com.matie.redgram.data.models.main.items.PostItem;
import com.matie.redgram.ui.submission.links.views.SingleLinkView;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class PostItemTextView extends PostItemSubView {

    @InjectView(R.id.text_title_view)
    TextView textTitleView;
    @InjectView(R.id.text_content_view)
    TextView textContentView;
    @InjectView(R.id.text_tag_view)
    PostItemTagView textTagView;

    PostItem postItem;
    int position;
    SingleLinkView listener;

    final Resources res;


    public PostItemTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        res = context.getResources();
    }

    @Override
    public void onFinishInflate(){
        super.onFinishInflate();
        ButterKnife.inject(this);
    }

    @Override
    public void setupView(PostItem item, int position, SingleLinkView listener) {
        this.postItem = item;
        this.position = position;
        this.listener = listener;

        textTagView.setupView(item, position, listener);

        textTitleView.setText(item.getTitle());

        if(item.getText().length() > 0){

            if(isNsfw()){
                textContentView.setText(res.getString(R.string.nsfw_material));
            }else{
                textContentView.setText(item.getText());
            }

            textContentView.setVisibility(VISIBLE);
        }else{
            textContentView.setVisibility(GONE);
        }

        if(item.getType().equals(PostItem.Type.SELF)){
            textTitleView.setTextSize(TypedValue.COMPLEX_UNIT_PX, res.getDimension(R.dimen.text_size_large));
        }else if(item.getType().equals(PostItem.Type.DEFAULT) || item.getType().equals(PostItem.Type.IMGUR)) {
            // TODO: 2016-04-21 IMGUR type should be later look like IMAGE, YOUTUBE, etc
            textTitleView.setTextSize(TypedValue.COMPLEX_UNIT_PX, res.getDimension(R.dimen.text_size_small));
        }else{
            textTitleView.setTextSize(TypedValue.COMPLEX_UNIT_PX, res.getDimension(R.dimen.text_size_medium));
        }

    }

    @OnClick({R.id.text_title_view, R.id.text_content_view})
    public void onClick(){
        if(isNsfw()){
            listener.callAgeConfirmDialog(getContext());
        }else{
            listener.loadCommentsForPost(getContext(), position);
        }
    }

    private boolean isNsfw(){
        return postItem.isAdult() && (!getSessionPrefs().isOver18() || getSessionPrefs().isDisableNsfwPreview());
    }

}
