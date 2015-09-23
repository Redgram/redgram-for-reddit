package com.matie.redgram.ui.common.views.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.matie.redgram.R;
import com.matie.redgram.data.models.main.items.DrawerItem;
import com.matie.redgram.ui.common.views.widgets.drawer.DrawerItemView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by matie on 18/01/15.
 */
public class DrawerAdapter extends BindableAdapter<DrawerItem> {

    private List<DrawerItem> items = Collections.emptyList();

    public DrawerAdapter(Context context){
        super(context);
    }

    public void replaceWith(List<DrawerItem> items) {
        this.items = new ArrayList<>(items);
        notifyDataSetChanged();
    }

    public DrawerItem getItem(int pos){
        return items.get(pos);
    }

    public long getItemId(int pos){
        return pos;
    }

    public View newView(LayoutInflater inflater, int pos, ViewGroup container){
        return inflater.inflate(R.layout.navigation_drawer_item,container,false);
    }

    public int getCount(){
        return items.size();
    }

    //binds new item object to item view
    public void bindView(DrawerItem item, int pos, View view){
        //pos never used
        ((DrawerItemView)view).bindTo(item);
    }

}
