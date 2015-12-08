package com.matie.redgram.ui.subcription;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Html;
import android.text.Spanned;
import android.text.SpannedString;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;
import android.widget.TextView;

import com.matie.redgram.R;
import com.matie.redgram.ui.common.base.BaseFragment;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
    @InjectView(R.id.subreddit_type)
    TextView subredditType;
    @InjectView(R.id.submission_type)
    TextView submissionType;
    @InjectView(R.id.subreddit_desc)
    TextView subredditDesc;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sub_detail, container, false);
        ButterKnife.inject(this, view);

        if(getArguments() != null){
            subredditName.setText(getArguments().getString("name"));
            subredditType.setText(getArguments().getString("subreddit_type"));
            submissionType.setText(getArguments().getString("submission_type"));

            //do the same for PostItemText Content
            String desc = getArguments().getString("description");
            desc = desc.replaceAll("href=\"(/[ru]/[a-zA-Z0-9]+)\"", "href=\"com.matie.redgram://$1\"");

            Spanned sp = Html.fromHtml(Html.fromHtml((String) desc).toString());
            subredditDesc.setText(sp);
            subredditDesc.setMovementMethod(LinkMovementMethod.getInstance());

        }

        return view;
    }

    @Override
    protected void setupComponent() {
        //no implementation needed
    }

    @Override
    protected void setupToolbar() {
        //no implementation needed
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }
}
