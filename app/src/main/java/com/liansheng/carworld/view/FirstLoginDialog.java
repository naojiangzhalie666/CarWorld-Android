package com.liansheng.carworld.view;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Display;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.gif.GifDrawable;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.liansheng.carworld.R;

public class FirstLoginDialog extends Dialog{

    private ImageView mImg;
    private Context mcontext;

    public FirstLoginDialog(Context context) {
        super(context,R.style.MyDialog);
        mcontext = context;
    }

    public FirstLoginDialog(Context context, int themeId) {
        super(context,themeId);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_first_login);
        //如果对话框宽度异常，可以通过下方代码根据设备的宽度来设置弹窗宽度
        WindowManager windowManager = getWindow().getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        WindowManager.LayoutParams params = getWindow().getAttributes();
        Point point=new Point();
        display.getSize(point);
        params.width= point.x;//(int) (point.x * 0.8);
        getWindow().setAttributes(params);
        mImg=findViewById(R.id.img);
        mImg.setOnClickListener(v -> dismiss());
        Glide.with(mcontext).load(R.drawable.ic_first_login).into(new SimpleTarget<Drawable>() {
            @Override
            public void onResourceReady(Drawable drawable, Transition<? super Drawable> transition) {
                if (drawable instanceof GifDrawable) {
                    GifDrawable gifDrawable = (GifDrawable) drawable;
                    gifDrawable.setLoopCount(1);
                    mImg.setImageDrawable(drawable);
                    gifDrawable.start();
                }
            }
        });

    }
}