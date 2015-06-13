package com.matie.redgram.ui.common.base;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

import com.matie.redgram.ui.App;
import com.matie.redgram.ui.AppComponent;
import com.matie.redgram.ui.common.main.MainComponent;

/**
 * Created by matie on 09/06/15.
 */
public abstract class BaseActivity extends ActionBarActivity {

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupComponent((AppComponent) App.get(this).component());
    }

    protected abstract void setupComponent(AppComponent appComponent);

    public abstract MainComponent component();
}
