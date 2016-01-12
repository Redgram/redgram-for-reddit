package com.matie.redgram.ui.comments.views;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;
import android.widget.TabHost;

import com.google.gson.Gson;
import com.matie.redgram.R;
import com.matie.redgram.data.models.main.items.PostItem;
import com.matie.redgram.ui.comments.views.adapters.CommentsPagerAdapter;
import com.matie.redgram.ui.comments.views.adapters.CommentsVerticalAdapter;
import com.matie.redgram.ui.comments.views.widgets.viewpager.VerticalViewPager;
import com.matie.redgram.ui.common.base.BaseActivity;
import com.matie.redgram.ui.common.base.BaseFragment;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by matie on 2016-01-04.
 */
public class CommentsFragment extends BaseFragment {
    @InjectView(R.id.vertical_container)
    VerticalViewPager mViewPager;

    private CommentsVerticalAdapter commentsVerticalAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_comments, container, false);
        ButterKnife.inject(this, view);

        String json = getArguments().getString(getResources().getString(R.string.main_data_key));
        boolean isText = ((new Gson().fromJson(json, PostItem.class)).getType() == PostItem.Type.SELF) ? true : false;

        commentsVerticalAdapter = new CommentsVerticalAdapter(getFragmentManager(), getArguments(), isText);

        setUpViewPager();

        return view;
    }

    @Override public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    protected void setupComponent() {

    }

    @Override
    protected void setupToolbar() {
        setToolbarTitle(0);
    }

    private void setToolbarTitle(int position){
        CharSequence title = commentsVerticalAdapter.getPageTitle(position);
        ((BaseActivity)getContext()).getSupportActionBar().setTitle(title);
    }

    private void setUpViewPager() {
        // Set up the ViewPager with the sections adapter.
        mViewPager.setAdapter(commentsVerticalAdapter);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                setToolbarTitle(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

}
