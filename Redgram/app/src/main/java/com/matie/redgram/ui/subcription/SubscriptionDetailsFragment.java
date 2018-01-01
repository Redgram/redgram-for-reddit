package com.matie.redgram.ui.subcription;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.FrameLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.matie.redgram.R;
import com.matie.redgram.ui.base.BaseFragment;
import com.matie.redgram.ui.common.utils.text.StringFormatter;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by matie on 2015-12-04.
 */
public class SubscriptionDetailsFragment extends BaseFragment {

    @InjectView(R.id.sub_scroll_view)
    ScrollView scrollView;

    @InjectView(R.id.subreddit_name)
    TextView subredditName;
    @InjectView(R.id.subscribersCount)
    TextView subscribersCount;
    @InjectView(R.id.accountActive)
    TextView accountsActive;

    @InjectView(R.id.subreddit_type)
    TextView subredditType;
    @InjectView(R.id.submission_type)
    TextView submissionType;
    @InjectView(R.id.subreddit_desc)
    WebView subredditDesc;

    Toolbar mToolbar;
    FrameLayout frameLayout;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sub_detail, container, false);
        ButterKnife.inject(this, view);

        mToolbar = (Toolbar)getActivity().findViewById(R.id.toolbar);

        if(getArguments() != null){
            setupDetails();
            setupWebViewSettings();
//            String temp = sp.toString();
//            sp = Html.fromHtml(temp);
//            subredditDesc.setText(sp);
//            subredditDesc.setMovementMethod(LinkMovementMethod.getBaseInstance());
        }

        return view;
    }

    private void setupWebViewSettings() {
        final WebSettings webSettings = subredditDesc.getSettings();
        Resources res = getResources();
        float fontSize = res.getDimension(R.dimen.web_text);
        webSettings.setDefaultFontSize((int) fontSize);

        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        webSettings.setAppCacheEnabled(false);
        webSettings.setBlockNetworkImage(true);
        webSettings.setLoadsImagesAutomatically(true);
        webSettings.setGeolocationEnabled(false);
        webSettings.setNeedInitialFocus(false);
        webSettings.setSaveFormData(false);
    }

    private void setupDetails() {
        String name = getArguments().getString("name");
        subredditName.setText(name);
        subscribersCount.setText(getArguments().getLong("subscribers_count")+ " subscribers");
        accountsActive.setText(getArguments().getInt("accounts_active") + " online");

        subredditType.setText(getArguments().getString("subreddit_type"));
        submissionType.setText(getArguments().getString("submission_type"));

        //do the same for PostItemText Content if needed...otherwise view
        String desc = getArguments().getString("description");
        desc = StringFormatter.prependRedditLinks(desc);
        Spanned sp = StringFormatter.formatFromHtml(desc);
        subredditDesc.loadData(sp.toString(), "text/html", "utf-8");
    }

    @Override
    protected void setupComponent() {
        //no implementation needed
    }

    @Override
    protected void setupToolbar() {
        // no implementation needed, clear only
        frameLayout = (FrameLayout)mToolbar.findViewById(R.id.toolbar_child_view);
        frameLayout.removeAllViews();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }

}
