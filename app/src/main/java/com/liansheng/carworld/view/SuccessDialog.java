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

public class SuccessDialog extends Dialog{

    private ImageView mImg;
    private TextView dialogTxt;
    private Context mcontext;
    private String message;

    public SuccessDialog setMessage(String message){
        this.message=message;
        return this;
    }

    public SuccessDialog(Context context) {
        super(context,R.style.MyDialog);
        mcontext = context;
    }

    public SuccessDialog(Context context, int themeId) {
        super(context,themeId);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_success);
        //如果对话框宽度异常，可以通过下方代码根据设备的宽度来设置弹窗宽度
        WindowManager windowManager = getWindow().getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        WindowManager.LayoutParams params = getWindow().getAttributes();
        Point point=new Point();
        display.getSize(point);
        params.width= point.x;//(int) (point.x * 0.8);
        getWindow().setAttributes(params);
        dialogTxt=findViewById(R.id.dialog_content);
        mImg=findViewById(R.id.dialog_success);
        dialogTxt.setText(message);
        findViewById(R.id.dialog_close).setOnClickListener(v ->dismiss());
        Glide.with(mcontext).load(R.mipmap.ic_success_act).into(new SimpleTarget<Drawable>() {
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