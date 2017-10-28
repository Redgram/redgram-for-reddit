package com.matie.redgram.ui.common.base;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.matie.redgram.R;
import com.matie.redgram.data.managers.storage.db.DatabaseHelper;
import com.matie.redgram.data.managers.storage.db.DatabaseManager;
import com.matie.redgram.data.models.db.Session;
import com.matie.redgram.ui.App;
import com.matie.redgram.ui.AppComponent;
import com.matie.redgram.ui.common.utils.widgets.DialogUtil;
import com.matie.redgram.ui.common.views.BaseView;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;

import icepick.Icepick;
import io.realm.Realm;
import io.realm.RealmChangeListener;


public abstract class BaseActivity extends RxAppCompatActivity implements BaseView {

    private Realm realm;
    private Session session;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fresco.initialize(this);
        Icepick.restoreInstanceState(this, savedInstanceState);
        setContentView(getLayoutId());

        AppComponent appComponent = App.get(this).component();
        setupRealm(appComponent.getApp());
        setupComponent(appComponent);
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        Icepick.saveInstanceState(this, outState);
    }

    @Override
    protected void onResume(){
        super.onResume();

        //onResume of any activity, show connection status
        ((App)getApplication()).getConnectionManager().showConnectionStatus(true);
    }

    @Override
    protected void onPause(){
        super.onPause();
        if(session != null){
            session.removeChangeListeners();
        }
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        DatabaseHelper.close(realm);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
    }

    public abstract AppComponent component();
    public abstract DialogUtil getDialogUtil();
    protected abstract void setupComponent(AppComponent appComponent);
    protected abstract int getLayoutId();
    protected abstract int getContainerId();

    private void setupRealm(App app) {
        DatabaseManager databaseManager = app.getDatabaseManager();
        realm = databaseManager.getInstance();
        session = DatabaseHelper.getSession(realm);
    }

    public Realm getRealm() {
        return realm;
    }

    public Session getSession() {
        return session;
    }

    public void addSessionListener(RealmChangeListener listener) {
        if (session == null) return;
        session.addChangeListener(listener);
    }

    public void removeSessionListener(RealmChangeListener listener) {
        if (session == null) return;
        session.removeChangeListener(listener);
    }

    public void openIntent(Intent intent) {
        openIntent(intent, R.anim.enter, R.anim.exit);
    }

    public void openIntent(Intent intent, int enterAnim, int exitAnim){
        startActivity(intent);
        overridePendingTransition(enterAnim, exitAnim);
    }

    public void openIntentForResult(Intent intent, int requestCode){
        openIntentForResult(intent, requestCode, R.anim.enter, R.anim.exit);
    }

    public void openIntentForResult(Intent intent, int requestCode, int enterAnim, int exitAnim){
        startActivityForResult(intent, requestCode);
        overridePendingTransition(enterAnim, exitAnim);
    }

    public void openFragment(Fragment fragment, String tag) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if(tag != null){
            transaction = transaction.replace(getContainerId(), fragment, tag);
        }else{
            transaction = transaction.replace(getContainerId(), fragment);
        }

        transaction.commit();
    }

}
