package com.matie.redgram.ui.profile;

import android.content.Context;

import com.matie.redgram.data.managers.presenters.ProfileOverviewPresenterImpl;
import com.matie.redgram.ui.common.base.BaseActivity;
import com.matie.redgram.ui.common.base.BaseFragment;
import com.matie.redgram.ui.common.utils.widgets.DialogUtil;
import com.matie.redgram.ui.common.views.BaseContextView;
import com.matie.redgram.ui.profile.components.DaggerProfileOverviewComponent;
import com.matie.redgram.ui.profile.components.ProfileComponent;
import com.matie.redgram.ui.profile.components.ProfileOverviewComponent;
import com.matie.redgram.ui.profile.modules.ProfileOverviewModule;
import com.matie.redgram.ui.profile.views.ProfileOverviewView;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;
import com.trello.rxlifecycle.components.support.RxFragment;

import javax.inject.Inject;

/**
 * Created by matie on 2016-07-26.
 */
public class ProfileOverviewFragment extends BaseFragment implements ProfileOverviewView {

    private ProfileOverviewComponent profileOverviewComponent;

    @Inject
    DialogUtil dialogUtil;
    @Inject
    ProfileOverviewPresenterImpl overviewPresenter;

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

    }

    @Override
    public BaseContextView getContentContext() {
        return getBaseFragment();
    }

    @Override
    protected void setupComponent() {
        ProfileComponent profileComponent = ((ProfileComponent)((ProfileActivity)getActivity()).component());
        profileOverviewComponent = DaggerProfileOverviewComponent.builder()
                                    .profileComponent(profileComponent)
                                    .profileOverviewModule(new ProfileOverviewModule(this))
                                    .build();
        profileOverviewComponent.inject(this);
    }
}
