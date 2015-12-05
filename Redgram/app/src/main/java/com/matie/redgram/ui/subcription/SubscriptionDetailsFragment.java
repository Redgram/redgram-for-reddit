package com.matie.redgram.ui.subcription;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Html;
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

    @InjectView(R.id.subreddit_desc)
    TextView subredditDesc;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sub_detail, container, false);
        ButterKnife.inject(this, view);

        if(getArguments() != null){
            String name = getArguments().getString("name");
            subredditName.setText(name + " - " + getArguments().getString("subreddit_type"));
            subredditDesc.setText(getArguments().getString("description"));

//            // TODO: 2015-12-04 use proper REGEX
//            String str = getArguments().getString("description");
//            Pattern pattern = Pattern.compile("&lt;!-- SC_OFF --&gt;");
//            String[] result = pattern.split(str);
//            subredditDesc.setText(Html.fromHtml(result[1]));
//            subredditDesc.setText(Html.fromHtml("<h2>Title</h2><br><p>Description here</p>"));
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
