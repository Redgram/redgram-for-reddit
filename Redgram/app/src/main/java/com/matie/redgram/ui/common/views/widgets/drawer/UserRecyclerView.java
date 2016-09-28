package com.matie.redgram.ui.common.views.widgets.drawer;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

import com.matie.redgram.data.models.main.items.UserItem;
import com.matie.redgram.ui.common.user.views.UserListControllerView;
import com.matie.redgram.ui.common.views.adapters.UserAdapter;

import java.util.List;

import butterknife.ButterKnife;

/**
 * Created by matie on 2016-05-04.
 */
public class UserRecyclerView extends RecyclerView {

    private final LayoutManager layoutManager;
    private final UserAdapter userAdapter;
    private int selectedItem;

    public UserRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.layoutManager = new LinearLayoutManager(context);
        this.userAdapter = new UserAdapter(context);
    }

    @Override
    public void onFinishInflate(){
        super.onFinishInflate();
        ButterKnife.inject(this);

        setLayoutManager(layoutManager);
        setAdapter(userAdapter);
    }

    public void replaceWith(List<UserItem> items){
        userAdapter.replaceWith(items);
    }

    public void setListener(UserListControllerView listener){
        userAdapter.setListener(listener);
    }

    @Override
    public LayoutManager getLayoutManager() {
        return layoutManager;
    }

    @Override
    public Adapter getAdapter() {
        return userAdapter;
    }

    public void setSelectedItem(int selectedItem) {
        this.selectedItem = selectedItem;
    }

    public int getSelectedItem() {
        return selectedItem;
    }
}
