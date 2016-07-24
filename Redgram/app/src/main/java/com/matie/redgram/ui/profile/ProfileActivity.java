package com.matie.redgram.ui.profile;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.matie.redgram.R;
import com.matie.redgram.ui.App;
import com.matie.redgram.ui.AppComponent;
import com.matie.redgram.ui.common.base.BaseActivity;
import com.matie.redgram.ui.common.main.MainActivity;
import com.matie.redgram.ui.common.utils.widgets.DialogUtil;

public class ProfileActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void setupComponent(AppComponent appComponent) {

    }

    @Override
    public AppComponent component() {
        return null;
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
