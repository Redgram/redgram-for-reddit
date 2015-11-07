package com.matie.redgram.ui.common.previews;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.gson.Gson;
import com.matie.redgram.R;
import com.matie.redgram.data.models.main.items.PostItem;
import com.matie.redgram.ui.common.main.MainActivity;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by matie on 2015-11-04.
 */
public class WebPreviewFragment extends BasePreviewFragment{

    public static final String MAIN_DATA = "web_preview_data";

    @InjectView(R.id.web_progress_bar)
    ProgressBar progressBar;
    @InjectView(R.id.web_preview)
    WebView webView;
    @InjectView(R.id.top_banner)
    LinearLayout topBanner;
    @InjectView(R.id.top_banner_title)
    TextView topBannerTitle;
    @InjectView(R.id.close_fragment)
    ImageView closeFragment;

    PostItem postItem;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.preview_web_fragment, container, false);
        ButterKnife.inject(this, view);

        //setup
        webView.getSettings().setBuiltInZoomControls(true);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(getWebViewClient());
        webView.setWebChromeClient(getWebChromeClient());

        if(getArguments().containsKey(MAIN_DATA)){
            String data = getArguments().getString(MAIN_DATA);
            postItem = new Gson().fromJson(data, PostItem.class);

            webView.loadUrl(postItem.getUrl());

            topBannerTitle.setText(postItem.getTitle());
        }

        MainActivity mainActivity = (MainActivity)getContext();
        mainActivity.setDragable(topBanner);

        return view;
    }

    @Override public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void refreshPreview(Bundle bundle) {
        if(bundle.containsKey(MAIN_DATA)){
            String data = bundle.getString(MAIN_DATA);
            postItem = new Gson().fromJson(data, PostItem.class);
            //update if not equal to current URL
            if(!postItem.getUrl().equalsIgnoreCase(webView.getUrl()))
                webView.loadUrl(postItem.getUrl());
            if(!topBannerTitle.getText().toString().equalsIgnoreCase(postItem.getTitle()))
                topBannerTitle.setText(postItem.getTitle());
        }
    }

    @OnClick(R.id.close_fragment)
    public void OnCloseFragment(){
        MainActivity activity = (MainActivity)getContext();
        activity.hidePanel();
    }

    // TODO: 2015-11-06 create a separate if needed in future
    private WebViewClient getWebViewClient() {
        WebViewClient webViewClient = new WebViewClient(){
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                progressBar.setProgress(0);
                progressBar.setVisibility(View.VISIBLE);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                progressBar.setVisibility(View.GONE);
            }
        };
        return webViewClient;
    }

    private WebChromeClient getWebChromeClient() {
        WebChromeClient webChromeClient = new WebChromeClient(){
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                progressBar.setProgress(newProgress);
                if(newProgress == 100){
                    progressBar.setVisibility(View.GONE);
                }
            }
        };
        return webChromeClient;
    }
}
