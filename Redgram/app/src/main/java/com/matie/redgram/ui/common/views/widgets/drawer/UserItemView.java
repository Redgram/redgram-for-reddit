package com.matie.redgram.ui.common.views.widgets.drawer;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.matie.redgram.R;
import com.matie.redgram.data.models.main.items.UserItem;
import com.matie.redgram.ui.common.user.views.UserListControllerView;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by matie on 2016-05-04.
 */
public class UserItemView extends FrameLayout {

    @InjectView(R.id.username_wrapper)
    RelativeLayout userNameWrapper;
    @InjectView(R.id.username)
    TextView userName;
    @InjectView(R.id.delete_user)
    ImageView deleteOption;

    private UserListControllerView listener;
    private UserItem userItem;
    private int position;

    public UserItemView(Context context) {
        super(context);
    }

    public UserItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public UserItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setup(UserItem userItem, int position){
        this.userItem = userItem;
        this.position = position;
        userName.setText(userItem.getUserName());
    }

    @Override
    public void onFinishInflate(){
        super.onFinishInflate();
        ButterKnife.inject(this);
    }

    public void setListener(UserListControllerView listener) {
        this.listener = listener;
    }

    public UserListControllerView getListener() {
        return listener;
    }

    @OnClick({R.id.username_wrapper, R.id.username})
    public void onUserWrapperClick(View v){
        if(listener != null){
            listener.selectAccount(position);
        }
    }

    @OnClick(R.id.delete_user)
    public void onUserRemoveClick(View v){
        if(listener != null){
            listener.removeAccount(position);
        }
    }
}
