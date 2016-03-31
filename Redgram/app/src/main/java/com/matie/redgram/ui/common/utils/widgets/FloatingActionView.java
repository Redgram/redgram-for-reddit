package com.matie.redgram.ui.common.utils.widgets;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.matie.redgram.R;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by matie on 2016-03-25.
 */
public class FloatingActionView extends FrameLayout {

    @InjectView(R.id.action)
    TextView actionView;

    @InjectView(R.id.message)
    TextView messageView;

    private FloatingActionListener floatingActionListener;
    private boolean isShown;

    public FloatingActionView(Context context) {
        super(context);
    }

    public FloatingActionView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FloatingActionView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public FloatingActionListener getFloatingActionListener() {
        return floatingActionListener;
    }

    public void setFloatingActionListener(FloatingActionListener floatingActionListener) {
        this.floatingActionListener = floatingActionListener;
    }

    @Override
    public void onFinishInflate(){
        super.onFinishInflate();
        ButterKnife.inject(this);
        init();
    }

    private void init(){
        setVisibility(GONE);
        isShown = false;
    }

    public void show(boolean withTimer){
        if(!isShown){
            requestLayout();

            setVisibility(VISIBLE);
            isShown = true;
            if(withTimer){
                postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        hide();
                    }
                }, 3000);
            }

        }
    }

    public void hide(){
        if(isShown){
            setVisibility(GONE);
            isShown = false;
            floatingActionListener.onActionDismiss();
        }
    }

    @OnClick(R.id.action)
    public void onActionClick(){
        try {
            floatingActionListener.onActionClick();
            hide();
        }catch (NullPointerException e){
            Log.d(getClass().getName() + " : NullPointerException", "Set Action Listener");
        }
    }

    public interface FloatingActionListener{
        void onActionClick();
        void onActionDismiss();
    }
}
