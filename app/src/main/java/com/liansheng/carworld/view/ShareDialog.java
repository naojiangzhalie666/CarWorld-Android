package com.liansheng.carworld.view;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.liansheng.carworld.R;
import com.liansheng.carworld.wxapi.WxUtils;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;

public class ShareDialog extends Dialog implements View.OnClickListener {

    String url;
    String title;
    String content;
    String message;
    Bitmap bmp;

    public ShareDialog(@NonNull Context context, String url, String title, String content) {
        this(context, url, title, content, "");
    }

    public ShareDialog(@NonNull Context context, String url, String title, String content, String message) {
        this(context, url, title, content, message, null);
    }

    public ShareDialog(@NonNull Context context, String url, String title, String content, String message, Bitmap bmp) {
        super(context);
        this.url = url;
        this.title = title;
        this.content = content;
        this.message = message;
        this.bmp = bmp;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_share);
//        WindowManager windowManager = getWindow().getWindowManager();
//        Display display = windowManager.getDefaultDisplay();
//        WindowManager.LayoutParams params = getWindow().getAttributes();
//        Point point = new Point();
//        display.getSize(point);
//        params.width = (int) (point.x * 0.8);
//        getWindow().setAttributes(params);
        findViewById(R.id.cancel).setOnClickListener(this);
        findViewById(R.id.share_wx).setOnClickListener(this);
        findViewById(R.id.share_circle).setOnClickListener(this);
        if (!TextUtils.isEmpty(message)) {
            TextView msg = findViewById(R.id.message);
            msg.setText(Html.fromHtml(message));
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.share_wx:
                WxUtils.getInstance().wxWeb(url, title, content, bmp);
                break;
            case R.id.share_circle:
                WxUtils.getInstance().wxWeb(url, title, content, bmp, SendMessageToWX.Req.WXSceneTimeline);
                break;
        }
        dismiss();
    }
}