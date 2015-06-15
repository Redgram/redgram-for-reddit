package com.matie.redgram.ui.home.views.widgets.postlist.dynamic;

import android.content.Context;
import android.content.res.Resources;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.matie.redgram.R;
import com.matie.redgram.data.models.PostItem;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by matie on 04/04/15.
 */
public class PostItemTextView extends DynamicView {

    @InjectView(R.id.text_title_view)
    TextView textTitleView;
    @InjectView(R.id.text_content_view)
    TextView textContentView;

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
    public void setUpView(PostItem item) {

        textTitleView.setText(item.getTitle());

        if(item.getText().length() > 0){
            textContentView.setText(item.getText());
            textContentView.setVisibility(VISIBLE);
        }

        if(item.getType().equals(PostItem.Type.SELF)){

            int dpValue = 8; // padding in dips
            float d = res.getDisplayMetrics().density;
            int pad = (int)(dpValue * d); // padding in pixels
            this.setPadding(pad, 0, pad, pad);

            textTitleView.setTextSize(TypedValue.COMPLEX_UNIT_PX, res.getDimension(R.dimen.text_size_large));
            textTitleView.setTextColor(res.getColor(R.color.material_red900));

            textContentView.setTextSize(TypedValue.COMPLEX_UNIT_PX, res.getDimension(R.dimen.text_size_small));
        }

    }

}
