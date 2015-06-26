package com.matie.redgram.ui.home.views.widgets.postlist.dynamic;

import android.content.Context;
import android.content.res.Resources;
import android.util.AttributeSet;
import android.widget.TextView;

import com.matie.redgram.R;
import com.matie.redgram.data.models.PostItem;
import com.matie.redgram.ui.home.views.widgets.postlist.PostBaseView;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by matie on 21/06/15.
 */
public class PostItemTagView extends PostBaseView {

    @InjectView(R.id.tag_nsfw)
    TextView adultTag;

    final Resources res;

    public PostItemTagView(Context context, AttributeSet attrs) {
        super(context, attrs);
        res = context.getResources();
    }

    @Override
    public void onFinishInflate(){
        super.onFinishInflate();
        ButterKnife.inject(this);
    }

    @Override
    public void setUpView(PostItem item) {
        boolean isVisible = false;

        if(item.isAdult()){
            adultTag.setVisibility(VISIBLE);
            isVisible = true;
        }else{
            adultTag.setVisibility(GONE);
            isVisible = false;
        }

        //always check at the end, default is false
        if(isVisible){
            this.setVisibility(VISIBLE);
        }else{
            this.setVisibility(GONE);
        }
    }
}
