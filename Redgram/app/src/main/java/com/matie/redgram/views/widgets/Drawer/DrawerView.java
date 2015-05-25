package com.matie.redgram.views.widgets.Drawer;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

import com.matie.redgram.R;
import com.matie.redgram.models.DrawerItem;
import com.matie.redgram.views.adapters.DrawerAdapter;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by matie on 18/01/15.
 */
public class DrawerView extends DrawerViewAnimator {
    //get the ListView associated with the fragment
    @InjectView(R.id.leftDrawerListView)
    ListView leftDrawerListView;

    private final DrawerAdapter adapter;

    public DrawerView(Context context, AttributeSet attrs){
        super(context, attrs);
        adapter = new DrawerAdapter(context);
    }
    public void replaceWith(List<DrawerItem> items){
      adapter.replaceWith(items);
      setDisplayedChildId(R.id.leftDrawerListView);
    }

    @Override
    protected void onFinishInflate(){
        super.onFinishInflate();
        //inject views
        ButterKnife.inject(this);
        //then set adapter setAdapter()
        leftDrawerListView.setAdapter(adapter);
    }

    public DrawerAdapter getAdapter() {
        return adapter;
    }

}
