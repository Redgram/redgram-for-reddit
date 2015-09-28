package com.matie.redgram.ui.common.base;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.matie.redgram.ui.App;
import com.matie.redgram.ui.AppComponent;
import com.matie.redgram.ui.common.utils.DialogUtil;

import javax.inject.Inject;

/**
 * Created by matie on 09/06/15.
 */
public abstract class BaseActivity extends AppCompatActivity {

    BaseActivityComponent component;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fresco.initialize(this);

        AppComponent appComponent = (AppComponent) App.get(this).component();
//        component = DaggerBaseActivityComponent.builder()
//                .appComponent(appComponent)
//                .baseActivityModule(new BaseActivityModule(this))
//                .build();
//
//        component.inject(this);

        setupComponent(appComponent);
    }

    @Override protected void onResume(){
        super.onResume();

        //onResume of any activity, show connection status
        ((App)getApplication()).getConnectionStatus().showConnectionStatus(true);
    }
    protected abstract void setupComponent(AppComponent appComponent);

    public abstract AppComponent component();
}
