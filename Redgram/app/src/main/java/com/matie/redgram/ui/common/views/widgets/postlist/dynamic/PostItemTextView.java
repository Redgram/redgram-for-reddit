package com.matie.redgram.ui.common.views.widgets.postlist.dynamic;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.TextView;

import com.matie.redgram.R;
import com.matie.redgram.data.models.main.items.PostItem;
import com.matie.redgram.ui.common.main.MainActivity;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by matie on 04/04/15.
 */
public class PostItemTextView extends PostItemSubView {

    @InjectView(R.id.text_title_view)
    TextView textTitleView;
    @InjectView(R.id.text_content_view)
    TextView textContentView;
    @InjectView(R.id.text_tag_view)
    PostItemTagView textTagView;


    final Resources res;
    MainActivity mainActivity;
    SharedPreferences sharedPreferences;

    String contentText;

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
    public void setupView(PostItem item) {
        textTagView.setupView(item);

        textTitleView.setText(item.getTitle());

        if(item.getText().length() > 0){

            textContentView.setText(item.getText());

            if(!isNsfwDisabled()){
                textContentView.setText(res.getString(R.string.nsfw_material));
            }else{
                textContentView.setText(textContentView.getText());
            }

            textContentView.setVisibility(VISIBLE);
        }

        if(item.getType().equals(PostItem.Type.SELF)){
            textTitleView.setTextSize(TypedValue.COMPLEX_UNIT_PX, res.getDimension(R.dimen.text_size_xlarge));
        }

        if(item.getType().equals(PostItem.Type.DEFAULT)) {
            textTitleView.setTextSize(TypedValue.COMPLEX_UNIT_PX, res.getDimension(R.dimen.text_size_small));
        }

    }

    @Override
    public void handleNsfwUpdate(boolean disabled) {
        if(disabled){
            textContentView.setText(textContentView.getText());
        }else{
            textContentView.setText(res.getString(R.string.nsfw_material));
        }
    }

    @OnClick(R.id.text_content_view)
    public void onClick(){
        if(textContentView.getText().equals(contentText)){
            // TODO: 09/10/15 Formatted view of content
        }
    }


}
