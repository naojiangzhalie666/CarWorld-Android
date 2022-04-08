package com.liansheng.carworld.view;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.gif.GifDrawable;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.liansheng.carworld.R;
import com.liansheng.carworld.activity.WebViewAct;
import com.liansheng.carworld.kit.Constant;
import com.liansheng.carworld.net.UrlKit;
import com.liansheng.carworld.view.pop.PopClick;

import cn.droidlover.xdroid.hold.info.SharedInfo;
import cn.droidlover.xdroid.tools.utils.ActivityManage;

public class FirstRegularDialog extends Dialog implements View.OnClickListener {

    private Activity activity;

    PopClick popClick;

    public FirstRegularDialog(Activity context, PopClick popClick) {
        super(context, R.style.MyDialog);
        activity = context;
        this.popClick = popClick;
    }

    public FirstRegularDialog(Context context, int themeId) {
        super(context, themeId);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_first_regular);
        //如果对话框宽度异常，可以通过下方代码根据设备的宽度来设置弹窗宽度
        WindowManager windowManager = getWindow().getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        WindowManager.LayoutParams params = getWindow().getAttributes();
        Point point = new Point();
        display.getSize(point);
        params.width = point.x;//(int) (point.x * 0.8);
        setCancelable(false);
        getWindow().setAttributes(params);
        findViewById(R.id.tv_regular1).setOnClickListener(this);
        findViewById(R.id.tv_regular2).setOnClickListener(this);
        findViewById(R.id.cancel).setOnClickListener(this);
        findViewById(R.id.confirm).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_regular1:
                Intent intent1 = new Intent(activity,WebViewAct.class);
                intent1.putExtra(Constant.KEY_TITLE, "用户服务协议");
                intent1.putExtra(Constant.KEY_URL, UrlKit.H5_REGISTER_REGULAR);
                activity.startActivity(intent1);
                break;
            case R.id.tv_regular2:
                Intent intent = new Intent(activity,WebViewAct.class);
                intent.putExtra(Constant.KEY_TITLE, "隐私协议");
                intent.putExtra(Constant.KEY_URL, UrlKit.H5_SECRET_REGULAR);
                activity.startActivity(intent);
                break;
            case R.id.cancel:
                dismiss();
                activity.finish();
                break;
            case R.id.confirm:
                SharedInfo.getInstance().saveValue(Constant.REGULAR_STATE, true);
                popClick.chick(view, "");
                dismiss();
                break;
        }
    }
}