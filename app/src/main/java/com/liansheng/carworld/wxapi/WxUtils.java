package com.liansheng.carworld.wxapi;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.liansheng.carworld.App;
import com.liansheng.carworld.R;
import com.liansheng.carworld.net.UrlKit;
import com.liansheng.carworld.utils.Utils;
import com.tencent.mm.opensdk.modelbiz.WXOpenCustomerServiceChat;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXImageObject;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.modelmsg.WXWebpageObject;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import java.io.ByteArrayOutputStream;

import cn.droidlover.xdroid.tools.utils.BitmapUtil;
import cn.droidlover.xdroid.tools.utils.StringFormat;

public class WxUtils {

    private IWXAPI api;

    private static WxUtils wxUtils = new WxUtils();

    private WxUtils() {
        api = WXAPIFactory.createWXAPI(App.getContext(), UrlKit.APP_ID);
    }

    public static WxUtils getInstance() {
        return wxUtils;
    }

    public void wxWeb(String url, String title, String desc) {
        wxWeb(url, title, desc, SendMessageToWX.Req.WXSceneSession);
    }

    public void wxWeb(String url, String title, String desc, Bitmap bitmap) {
        wxWeb(url, title, desc, bitmap, SendMessageToWX.Req.WXSceneSession);
    }

    public void wxWeb(String url, String title, String desc, int type) {
        //初始化一个WXWebpageObject，填写url
        wxWeb(url, title, desc, null, type);
    }

    public void jumpWx(Bitmap bmp) {
        WXImageObject imgObj = new WXImageObject(bmp);
        WXMediaMessage msg = new WXMediaMessage();
        msg.mediaObject = imgObj;
        Bitmap thumbBmp = Bitmap.createScaledBitmap(bmp, 220, 260, true);
//        bmp.recycle();
        msg.thumbData = BitmapUtil.bitmapToBytes(thumbBmp, true);
        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = buildTransaction("img");
        req.message = msg;
        req.scene = SendMessageToWX.Req.WXSceneSession;
        req.userOpenId = UrlKit.APP_ID;
        api.sendReq(req);
    }

    public void wxWeb(String url, String title, String desc, Bitmap bmp, int type) {
        //初始化一个WXWebpageObject，填写url
        WXWebpageObject webpage = new WXWebpageObject();
        webpage.webpageUrl = url;
        WXMediaMessage msg = new WXMediaMessage(webpage);
        msg.title = title;
        msg.description = desc;
        if (bmp == null) {
            Bitmap thumbBmp = BitmapFactory.decodeResource(App.getContext().getResources(), R.mipmap.logo);
            msg.thumbData = BitmapUtil.bitmapToBytes(thumbBmp, true);
        } else {
            msg.thumbData = BitmapUtil.bmpToByteArray(bmp, 32);
        }
        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = buildTransaction("webpage");
        req.message = msg;
        req.scene = type;
        req.userOpenId = UrlKit.APP_ID;
        api.sendReq(req);
    }

    public boolean linkWXService() {
        IWXAPI api = WXAPIFactory.createWXAPI(App.getContext(), UrlKit.APP_ID);
// 判断当前版本是否支持拉起客服会话
        if (api.getWXAppSupportAPI() >= com.tencent.mm.opensdk.constants.Build.SUPPORT_OPEN_CUSTOMER_SERVICE_CHAT) {
//            String url = kfUrl.getText().toString();
            WXOpenCustomerServiceChat.Req req = new WXOpenCustomerServiceChat.Req();
            req.corpId = "ww19967aaac78d6ee7";                                  // 企业ID
            req.url = "https://work.weixin.qq.com/kfid/kfc02138400da6f0561";    // 客服URL
            api.sendReq(req);
            return true;
        } else {
            return false;
        }
    }

    private String buildTransaction(final String type) {
        return (type == null) ? String.valueOf(System.currentTimeMillis()) : type + System.currentTimeMillis();
    }
}
