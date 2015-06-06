package com.matie.redgram.ui.common.views.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

/**
 * Created by matie on 19/01/15.
 */
public abstract class BindableAdapter<T> extends BaseAdapter {
    private final Context context;
    private final LayoutInflater inflater;

    public BindableAdapter(Context context){
        this.context = context;
        this.inflater = LayoutInflater.from(context);
    }

    public Context getContext(){return context;};

    /** Create a new instance of a view for the specified position. */
    public abstract View newView(LayoutInflater inflater, int position, ViewGroup container);
    /** Bind the data for the specified {@code position} to the view. */
    public abstract void bindView(T item, int position, View view);

    @Override
    public abstract T getItem(int pos);


    @Override
    public final View getView(int pos, View view, ViewGroup container){
        if(view == null){
            view = newView(inflater, pos, container);
            if(view == null){
                throw new IllegalStateException("new view should not be null.");
            }
        }
        bindView(getItem(pos),pos, view);
        return view;
    }

    /** Create a new instance of a drop-down view for the specified position. */
    public View newDropDownView(LayoutInflater inflater, int position, ViewGroup container) {
        return newView(inflater, position, container);
    }

    /** Bind the data for the specified {@code position} to the drop-down view. */
    public void bindDropDownView(T item, int position, View view) {
        bindView(item, position, view);
    }

    @Override
    public final View getDropDownView(int pos, View view, ViewGroup container){
        if(view == null){
            view = newDropDownView(inflater,pos,container);
            if(view == null){
                throw new IllegalStateException("newdropdownview result must not be null");
            }
        }
        bindDropDownView(getItem(pos), pos, view);
        return view;
    }

}
