package com.matie.redgram.ui.common.views.widgets.drawer;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.matie.redgram.R;
import com.matie.redgram.data.models.main.items.UserItem;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by matie on 2016-05-04.
 */
public class UserItemView extends FrameLayout {
    @InjectView(R.id.username)
    TextView userName;

    @InjectView(R.id.delete_user)
    ImageView deleteOption;

    public UserItemView(Context context) {
        super(context);
    }

    public UserItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public UserItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setup(UserItem userItem){
        userName.setText(userItem.getUserName());
    }

    @Override
    public void onFinishInflate(){
        super.onFinishInflate();
        ButterKnife.inject(this);
    }
}
