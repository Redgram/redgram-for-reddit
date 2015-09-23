package com.matie.redgram.ui.common.views.widgets.postlist.dynamic;

import android.content.Context;
import android.content.res.Resources;
import android.util.AttributeSet;
import android.widget.TextView;

import com.matie.redgram.R;
import com.matie.redgram.data.models.main.items.PostItem;
import com.matie.redgram.ui.common.views.widgets.postlist.PostBaseView;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by matie on 21/06/15.
 */
public class PostItemTagView extends PostBaseView {

    @InjectView(R.id.tags_view)
    TextView tags;

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
        String bullet = " "+res.getString(R.string.text_bullet)+" ";
        String comments = item.getNumComments() + " comments";
        String source = "[ "+item.getDomain()+" ]";

        tags.setText(comments+" "+bullet+" "+source);
    }

}
