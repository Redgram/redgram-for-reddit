//package com.matie.redgram.ui.comments.views.adapters;
//
//import android.content.Context;
//import android.os.Bundle;
//import android.support.v4.view.ViewPager;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ScrollView;
//import android.widget.TabHost;
//
//import com.matie.redgram.R;
//import com.matie.redgram.ui.comments.views.widgets.viewpager.VerticalViewPager;
//import com.matie.redgram.ui.common.base.BaseActivity;
//import com.matie.redgram.ui.common.base.BaseFragment;
//
//import butterknife.ButterKnife;
//import butterknife.InjectView;
//
///**
// * Created by matie on 2016-01-04.
// */
//public class CommentsFragment2 extends BaseFragment {
//    @InjectView(R.id.vertical_container)
//    VerticalViewPager mViewPager;
//    @InjectView(R.id.tabHost)
//    TabHost mTabHost;
//    @InjectView(R.id.tabScroll)
//    ScrollView mScrollView;
//
//    private CommentsVerticalAdapter commentsVerticalAdapter;
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//
//        View view = inflater.inflate(R.layout.fragment_comments, container, false);
//        ButterKnife.inject(this, view);
//
//        commentsVerticalAdapter = new CommentsVerticalAdapter(getFragmentManager(), getArguments());
//
//        setUpViewPager();
//        setUpTabHost();
//
//        return view;
//    }
//
//    @Override public void onActivityCreated(Bundle savedInstanceState) {
//        super.onActivityCreated(savedInstanceState);
//    }
//
//    @Override
//    protected void setupComponent() {
//
//    }
//
//    @Override
//    protected void setupToolbar() {
//        setToolbarTitle(0);
//    }
//
//    private void setToolbarTitle(int position){
//        CharSequence title = commentsVerticalAdapter.getPageTitle(position);
//        ((BaseActivity)getContext()).getSupportActionBar().setTitle(title);
//    }
//
//    private void setUpViewPager() {
//        // Set up the ViewPager with the sections adapter.
//        mViewPager.setAdapter(commentsVerticalAdapter);
//        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
//            @Override
//            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//
//            }
//
//            @Override
//            public void onPageSelected(int position) {
//                setToolbarTitle(position);
//                mTabHost.setCurrentTab(position);
//            }
//
//            @Override
//            public void onPageScrollStateChanged(int state) {
//
//            }
//        });
//    }
//
//    private void setUpTabHost() {
//        mTabHost.setup();
//
//        for(int i = 0; i < commentsVerticalAdapter.getCount(); i++){
//            String tag = commentsVerticalAdapter.getPageTitle(i).toString();
//            TabHost.TabSpec tabSpec = mTabHost.newTabSpec(tag);
//            tabSpec.setIndicator(tag);
//            tabSpec.setContent(new FakeContent(getContext()));
//            mTabHost.addTab(tabSpec);
//        }
//
//        mTabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
//            @Override
//            public void onTabChanged(String tabId) {
//                int pos = mTabHost.getCurrentTab();
//                mViewPager.setCurrentItem(pos);
//
//                View tabView = mTabHost.getCurrentTabView();
//                int scrollPos = tabView.getTop()
//                        - (mScrollView.getHeight() - tabView.getHeight()) / 2;
//                mScrollView.smoothScrollTo(scrollPos, mScrollView.getHeight());
//            }
//        });
//    }
//
//    // fake content for tabhost
//    private class FakeContent implements TabHost.TabContentFactory {
//        private final Context mContext;
//
//        public FakeContent(Context context) {
//            mContext = context;
//        }
//
//        @Override
//        public View createTabContent(String tag) {
//            View v = new View(mContext);
//            v.setMinimumHeight(10);
//            v.setMinimumWidth(10);
//            return v;
//        }
//    }
//}
