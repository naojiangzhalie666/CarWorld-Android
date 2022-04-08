package com.liansheng.carworld.activity.home;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;

import com.liansheng.carworld.R;
import com.liansheng.carworld.activity.BaseActivity;
import com.liansheng.carworld.kit.Constant;
import com.liansheng.carworld.wxapi.WxUtils;

import butterknife.OnClick;
import cn.droidlover.xdroid.tools.utils.BitmapUtil;

public class CarLoanResultAct extends BaseActivity {

    private Bitmap bitmap;

    @Override
    public void initData(Bundle savedInstanceState) {

    }

    @Override
    public int getLayoutId() {
        return R.layout.act_car_loan_result;
    }

    @OnClick({R.id.home,R.id.share_wx,R.id.share_kf})
    public void onViewClick(View view) {
        switch (view.getId()) {
            case R.id.home:
                finish();
                break;
            case R.id.share_wx:
                bitmap= BitmapUtil.readBitmap(context,R.mipmap.ic_loan_pa_bg);
                WxUtils.getInstance().jumpWx(bitmap);
                break;
            case R.id.share_kf:
                if (!WxUtils.getInstance().linkWXService()) {
                    callPhone(this, "联系客服电话：" + Constant.SERVICE_PHONE, Constant.SERVICE_PHONE);
                }
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(bitmap!=null){
            bitmap.recycle();
            bitmap=null;
        }
    }
}
