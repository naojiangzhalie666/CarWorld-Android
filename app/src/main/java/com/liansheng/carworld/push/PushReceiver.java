package com.liansheng.carworld.push;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.liansheng.carworld.activity.MainActivity;
import com.liansheng.carworld.activity.OrderDetailsActivity;
import com.liansheng.carworld.bean.other.NoticeBean;

import cn.droidlover.xdroid.hold.info.SharedInfo;
import cn.droidlover.xdroid.tools.log.Logger;
import cn.droidlover.xdroid.tools.utils.ActivityManage;
import cn.droidlover.xdroid.tools.utils.AppUtil;
import cn.jpush.android.api.CmdMessage;
import cn.jpush.android.api.CustomMessage;
import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.JPushMessage;
import cn.jpush.android.api.NotificationMessage;
import cn.jpush.android.service.JPushMessageReceiver;

public class PushReceiver extends JPushMessageReceiver {

    private static final String TAG = "PushMessageReceiver";

    @Override
    public void onTagOperatorResult(Context context, JPushMessage jPushMessage) {
        super.onTagOperatorResult(context, jPushMessage);
    }

    @Override
    public void onCheckTagOperatorResult(Context context, JPushMessage jPushMessage) {
        super.onCheckTagOperatorResult(context, jPushMessage);
    }

    @Override
    public void onAliasOperatorResult(Context context, JPushMessage jPushMessage) {
        super.onAliasOperatorResult(context, jPushMessage);
    }

    @Override
    public void onMessage(Context context, CustomMessage customMessage) {
        super.onMessage(context, customMessage);
//        Intent intent = new Intent("com.liansheng.carworld.message");
//        intent.putExtra("msg", customMessage.message);
//        intent.putExtra("data", customMessage.extra);
//        context.sendBroadcast(intent);
//        SharedInfo.getInstance().saveValue(customMessage.messageId, customMessage.extra);
    }

    @Override
    public void onNotifyMessageOpened(Context context, NotificationMessage notificationMessage) {
        super.onNotifyMessageOpened(context, notificationMessage);
        String extra = notificationMessage.notificationExtras;
//        Logger.e(TAG, "extra:" + extra);
//        if (TextUtils.isEmpty(extra) || "{}".equals(extra)) {
//            extra = SharedInfo.getInstance().getValue(notificationMessage.msgId, "").toString();
//        }
        Logger.e(TAG, "extra:" + extra);
        if (!TextUtils.isEmpty(extra) && !"{}".equals(extra)) {
            NoticeBean noticeBean = new Gson().fromJson(extra, NoticeBean.class);
            if (noticeBean != null) {
                if (noticeBean.getType().equals("TransportOrder")) {
                    Intent bundle = new Intent();
                    bundle.putExtra("id", noticeBean.getOrderId());
                    ActivityManage.push(OrderDetailsActivity.class, bundle);
                } else {
                    if (!AppUtil.isAppRunning(context, context.getPackageName())) {
                        ActivityManage.push(MainActivity.class);
                    }
                }
            }
        } else {
            if (!AppUtil.isAppRunning(context, context.getPackageName())) {
                ActivityManage.push(MainActivity.class);
            }
        }
    }

    @Override
    public void onRegister(Context context, String s) {
        super.onRegister(context, s);
    }

    @Override
    public void onNotifyMessageArrived(Context context, NotificationMessage notificationMessage) {
        super.onNotifyMessageArrived(context, notificationMessage);
    }

    @Override
    public void onMultiActionClicked(Context context, Intent intent) {
        super.onMultiActionClicked(context, intent);
    }

}
