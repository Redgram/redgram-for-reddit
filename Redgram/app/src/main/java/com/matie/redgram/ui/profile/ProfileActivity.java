package com.matie.redgram.ui.profile;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.matie.redgram.R;
import com.matie.redgram.ui.App;
import com.matie.redgram.ui.AppComponent;
import com.matie.redgram.ui.common.base.BaseActivity;
import com.matie.redgram.ui.common.utils.widgets.DialogUtil;
import com.matie.redgram.ui.profile.components.DaggerProfileComponent;
import com.matie.redgram.ui.profile.components.ProfileComponent;
import com.matie.redgram.ui.profile.modules.ProfileModule;

import javax.inject.Inject;

public class ProfileActivity extends BaseActivity {

    private ProfileComponent profileComponent;

    @Inject
    App app;
    @Inject
    DialogUtil dialogUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void setupComponent(AppComponent appComponent) {
        profileComponent = DaggerProfileComponent.builder()
                            .appComponent(appComponent)
                            .profileModule(new ProfileModule(this))
                            .build();
        profileComponent.inject(this);
    }

    @Override
    public AppComponent component() {
        return profileComponent;
    }

    @Override
    public App app() {
        return null;
    }

    @Override
    public DialogUtil getDialogUtil() {
        return null;
    }

    @Override
    protected BaseActivity activity() {
        return this;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_profile;
    }

    @Override
    protected int getContainerId() {
        return R.id.container;
    }

    public static Intent intent(Context context){
        Intent intent = new Intent(context, ProfileActivity.class);
        return intent;
    }
}
