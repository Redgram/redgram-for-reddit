package com.matie.redgram.ui.common.base;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.matie.redgram.ui.App;
import com.matie.redgram.ui.AppComponent;
import com.matie.redgram.ui.common.main.MainComponent;

/**
 * Created by matie on 09/06/15.
 */
public abstract class BaseActivity extends AppCompatActivity {

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fresco.initialize(this);
        setupComponent((AppComponent) App.get(this).component());
    }

    @Override protected void onResume(){
        super.onResume();

        //onResume of any activity, show connection status
        ((App)getApplication()).showConnectionStatus(((App)getApplication()).getConnectionStatus().isOnline());
    }
    protected abstract void setupComponent(AppComponent appComponent);

    public abstract MainComponent component();
}
