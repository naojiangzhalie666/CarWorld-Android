package com.liansheng.carworld.wxapi;

import android.app.Activity;
import android.os.Bundle;

import com.liansheng.carworld.bean.event.EventShare;
import com.liansheng.carworld.net.UrlKit;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import org.greenrobot.eventbus.EventBus;

import cn.droidlover.xdroid.tools.utils.Util;

public class WXEntryActivity extends Activity implements IWXAPIEventHandler {
    // IWXAPI 是第三方app和微信通信的openapi接口
    private IWXAPI api;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        api = WXAPIFactory.createWXAPI(this, UrlKit.APP_ID, false);
        api.handleIntent(getIntent(), this);
    }
    @Override
    public void onReq(BaseReq arg0) {
    }


    @Override
    public void onResp(BaseResp resp) {
        switch (resp.errCode) {
            case BaseResp.ErrCode.ERR_OK:
                Util.toast("分享成功");
                EventBus.getDefault().post(new EventShare());
                this.finish();
                //分享成功
                break;
            case BaseResp.ErrCode.ERR_USER_CANCEL:
                //分享取消
                Util.toast("分享取消");
//                Toast.makeText(getApplicationContext(), "分享取消", 2000).show();
                this.finish();
                break;
            case BaseResp.ErrCode.ERR_AUTH_DENIED:
//                Toast.makeText(getApplicationContext(), "分享拒绝", 2000).show();
                this.finish();
                //分享拒绝
                break;
        }
    }
}