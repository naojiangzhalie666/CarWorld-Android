package com.liansheng.carworld.view;

import android.app.Dialog;
import android.content.Context;
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

public class MyDialog extends Dialog implements View.OnClickListener {

    private TextView mTitle, mMessage, mConfirm;
    private ImageView mImg, mCanel;
    private Context mcontext;
    private String title, message, cancel, confirm;
    private OnCancelListtener onCancelListtener;
    private OnConfirmListtener onConfirmListtener;
    private int type = 1;//1订单 2其他

    public MyDialog setTitle(String title) {
        this.title = title;
        return this;
    }

    public MyDialog setMessage(String message) {
        this.message = message;
        return this;
    }

    public MyDialog setCancel(String cancel, OnCancelListtener onCancelListtener) {
        this.cancel = cancel;
        this.onCancelListtener = onCancelListtener;
        return this;
    }

    public MyDialog setConfirm(String confirm, OnConfirmListtener onConfirmListtener) {
        this.confirm = confirm;
        this.onConfirmListtener = onConfirmListtener;
        return this;
    }

    public MyDialog(Context context) {
        super(context, R.style.MyDialog);
        mcontext = context;
    }

    public MyDialog(Context context, int type) {
        super(context, R.style.MyDialog);
        mcontext = context;
        this.type = type;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_order);
        //如果对话框宽度异常，可以通过下方代码根据设备的宽度来设置弹窗宽度
        WindowManager windowManager = getWindow().getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        WindowManager.LayoutParams params = getWindow().getAttributes();
        Point point = new Point();
        display.getSize(point);
        params.width = (int) (point.x * 0.8);
        getWindow().setAttributes(params);

        mTitle = findViewById(R.id.title);
        mMessage = findViewById(R.id.message);
        mCanel = findViewById(R.id.cancel);
        mConfirm = findViewById(R.id.confirm);
        mImg = findViewById(R.id.img);

        mCanel.setOnClickListener(this);
        if(type==1){
            mConfirm.setOnClickListener(this);
            mConfirm.setText(confirm);
        }else {
            mConfirm.setVisibility(View.GONE);
        }

        mTitle.setText(title);
        mMessage.setText(message);
        Glide.with(mcontext).load(R.drawable.order).into(new SimpleTarget<Drawable>() {
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cancel:
                if (onCancelListtener != null) {
                    onCancelListtener.onCancel(this);
                }
                dismiss();
                break;

            case R.id.confirm:
                if (onConfirmListtener != null) {
                    onConfirmListtener.onConfirm(this);
                }
                dismiss();
                break;
        }
    }

    //自定义接口形式提供回调方法
    public interface OnCancelListtener {
        void onCancel(MyDialog myDialog);
    }

    public interface OnConfirmListtener {
        void onConfirm(MyDialog myDialog);
    }
}