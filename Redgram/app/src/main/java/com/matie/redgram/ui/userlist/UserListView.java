package com.matie.redgram.ui.userlist;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.matie.redgram.R;
import com.matie.redgram.data.managers.presenters.UserListPresenterImpl;
import com.matie.redgram.data.models.main.items.UserItem;
import com.matie.redgram.ui.auth.AuthActivity;
import com.matie.redgram.ui.base.BaseActivity;
import com.matie.redgram.ui.main.views.MainView;
import com.matie.redgram.ui.userlist.views.UserListControllerView;
import com.matie.redgram.ui.common.utils.widgets.DialogUtil;
import com.matie.redgram.ui.base.BaseView;
import com.matie.redgram.ui.userlist.views.adapters.UserAdapter;
import com.matie.redgram.ui.common.views.widgets.drawer.UserRecyclerView;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class UserListView extends FrameLayout implements UserListControllerView {

    public static final int ADD_ACCOUNT = 11;

    @InjectView(R.id.user_add_account)
    TextView addAccountView;
    @InjectView(R.id.user_recycler_view)
    UserRecyclerView userRecyclerView;
    @InjectView(R.id.user_cancel)
    ImageView cancelView;

    private final Context context;
    private UserListComponent component;
    private UserListPresenterImpl userListPresenter;

    public UserListView(Context context) {
        super(context);
        this.context = context;
    }

    public UserListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }

    public UserListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
    }

    @Override
    public void onFinishInflate(){
        super.onFinishInflate();
        ButterKnife.inject(this);
        init();
    }

    private void init() {
        userRecyclerView.setListener(this);
    }

    @OnClick(R.id.user_cancel)
    public void onCancelClick(View v){
        close();
    }

    @OnClick(R.id.user_add_account)
    public void onAddClick(View v){
        addAccount();
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void showInfoMessage() {

    }

    @Override
    public void showErrorMessage(String error) {
        DialogUtil.builder(getContext()).title("Oops!").content(error).show();
    }

    @Override
    public void addAccount() {
        if (context instanceof BaseActivity) {
            ((BaseActivity) context).openIntentForResult(AuthActivity.intent(context, false), ADD_ACCOUNT);
        }
    }

    @Override
    public void selectAccount(String id, int position) {
        UserItem userItem = getItem(position);
        if(!userItem.isSelected()){
            userListPresenter.switchUser(id, position);
        }
    }

    @Override
    public void removeAccount(String id, int position) {
        DialogUtil.builder(getContext())
                .title("Remove this account?")
                .content(getItem(position).getUserName())
                .onPositive((dialog, which) -> userListPresenter.removeUser(id, position))
                .onNegative((dialog1, which1) -> dialog1.dismiss())
                .positiveText("Yes")
                .negativeText("Cancel")
                .show();
    }

    @Override
    public void removeItem(int position) {
        RecyclerView.Adapter adapter = userRecyclerView.getAdapter();
        adapter.notifyItemRemoved(position);
        adapter.notifyItemRangeChanged(position, adapter.getItemCount() - position);
    }

    @Override
    public void close() {
        if (context instanceof MainView) {
            ((MainView) context).resetNavDrawer();
        }
    }

    @Override
    public UserItem getItem(int position) {
        return ((UserAdapter) userRecyclerView.getAdapter()).getItem(position);
    }

    @Override
    public List<UserItem> getItems() {
        return ((UserAdapter) userRecyclerView.getAdapter()).getItems();
    }

    @Override
    public void restartContext(){
        if (context instanceof BaseActivity) {
            ((BaseActivity) context).recreate();
        }
    }

    @Override
    public void populateView(List<UserItem> userItems) {
        userRecyclerView.replaceWith(userItems);
    }

    public void setComponent(UserListComponent component) {
        this.component = component;
        userListPresenter = (UserListPresenterImpl) component.getUserListPresenter();
    }

    public void setUp() {
        userListPresenter.getUsers();
    }

    @Override
    public BaseView getBaseInstance() {
        if (context instanceof BaseActivity) {
            return (BaseActivity) context;
        }

        return null;
    }
}
