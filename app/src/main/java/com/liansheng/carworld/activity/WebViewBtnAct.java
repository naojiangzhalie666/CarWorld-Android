package com.liansheng.carworld.activity;

import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ProgressBar;

import com.liansheng.carworld.R;
import com.liansheng.carworld.kit.Constant;
import com.liansheng.carworld.net.UrlKit;
import com.liansheng.carworld.view.pop.CompanyWechatPop;
import com.liansheng.carworld.wxapi.WxUtils;
import com.tencent.mm.opensdk.constants.Build;
import com.tencent.mm.opensdk.modelbiz.WXOpenCustomerServiceChat;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import butterknife.BindView;
import butterknife.OnClick;
import cn.droidlover.xdroid.tools.utils.Util;
import cn.droidlover.xdroid.views.appbar.ToolBar;

public class WebViewBtnAct extends BaseActivity {

    @BindView(R.id.toolbar)
    ToolBar toolBar;
    @BindView(R.id.progress)
    ProgressBar progressBar;
    @BindView(R.id.web_view)
    WebView webView;

    CompanyWechatPop wechatPop;

    @Override
    public void initData(Bundle savedInstanceState) {
        String title = getIntent().getStringExtra("title");
        String url = getIntent().getStringExtra("url");
        toolBar.setTitle(title);
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
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
        wechatPop = new CompanyWechatPop(context);
    }

    @Override
    public int getLayoutId() {
        return R.layout.act_web_view_btn;
    }


    @OnClick({R.id.submit})
    public void onViewClick() {
        if (!WxUtils.getInstance().linkWXService()) {
            wechatPop.showAtLocation(toolBar,Gravity.CENTER,0,0);
        }
    }


}
