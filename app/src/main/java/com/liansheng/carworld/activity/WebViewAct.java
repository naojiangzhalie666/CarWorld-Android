package com.liansheng.carworld.activity;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.liansheng.carworld.R;
import com.liansheng.carworld.kit.Constant;

import butterknife.BindView;
import cn.droidlover.xdroid.views.appbar.ToolBar;

public class WebViewAct extends BaseActivity {

    @BindView(R.id.toolbar)
    ToolBar toolBar;
    @BindView(R.id.progress)
    ProgressBar progressBar;
    @BindView(R.id.web_view)
    WebView webView;


    @SuppressLint("SetJavaScriptEnabled")
    @Override
    public void initData(Bundle savedInstanceState) {
        String title = getIntent().getStringExtra("title");
        String url = getIntent().getStringExtra("url");
        toolBar.setTitle(title);
        WebSettings settings = webView.getSettings();
        settings.setDefaultTextEncodingName("utf-8");
        settings.setLoadsImagesAutomatically(true);
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        settings.setJavaScriptEnabled(true);
        settings.setMixedContentMode(android.webkit.WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        settings.setCacheMode(WebSettings.LOAD_DEFAULT);
        webView.loadUrl(url);
        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                progressBar.setProgress(newProgress);
                if (newProgress == 100) {
                    progressBar.setVisibility(View.GONE);
                }

            }

        });
    }

    @Override
    public int getLayoutId() {
        return R.layout.act_web_view;
    }
}
