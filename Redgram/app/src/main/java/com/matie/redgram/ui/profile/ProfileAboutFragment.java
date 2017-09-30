package com.matie.redgram.ui.profile;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.matie.redgram.R;
import com.matie.redgram.data.managers.presenters.ProfileAboutPresenterImpl;
import com.matie.redgram.ui.AppComponent;
import com.matie.redgram.ui.common.base.BaseActivity;
import com.matie.redgram.ui.common.base.BaseFragment;
import com.matie.redgram.ui.common.views.BaseContextView;
import com.matie.redgram.ui.profile.components.DaggerProfileAboutComponent;
import com.matie.redgram.ui.profile.components.ProfileAboutComponent;
import com.matie.redgram.ui.profile.components.ProfileComponent;
import com.matie.redgram.ui.profile.modules.ProfileAboutModule;
import com.matie.redgram.ui.profile.views.ProfileAboutView;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by matie on 2017-09-27.
 */

public class ProfileAboutFragment extends BaseFragment implements ProfileAboutView {

    @Inject
    ProfileAboutPresenterImpl aboutPresenter;
    ProfileAboutComponent component;

    @InjectView(R.id.text)
    TextView textView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile_about, container, false);
        ButterKnife.inject(this, view);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        aboutPresenter.registerForEvents();
    }

    @Override
    public void onPause() {
        aboutPresenter.unregisterForEvents();
        super.onPause();
    }

    @Override
    public void onDestroyView() {
        ButterKnife.reset(this);
        super.onDestroyView();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        textView.setText(aboutPresenter.getTitle());
    }

    @Override
    protected void setupComponent() {
        AppComponent appComponent = ((BaseActivity)getActivity()).component();
        ProfileComponent profileComponent = (ProfileComponent) appComponent;
        component = DaggerProfileAboutComponent.builder()
                .profileComponent(profileComponent)
                .profileAboutModule(new ProfileAboutModule(this))
                .build();
        component.inject(this);
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

    }

    @Override
    public BaseContextView getContentContext() {
        return this;
    }
}
