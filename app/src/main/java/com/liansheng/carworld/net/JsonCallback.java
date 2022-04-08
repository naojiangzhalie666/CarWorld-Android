package com.liansheng.carworld.net;

import com.google.gson.Gson;
import com.liansheng.carworld.activity.me.InfoAuthAct;
import com.liansheng.carworld.activity.user.LoginYzmActivity;
import com.liansheng.carworld.bean.UserInfo;
import com.liansheng.carworld.bean.other.ResultBean;
import com.liansheng.carworld.kit.Constant;
import com.zhy.http.okhttp.callback.Callback;

import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import cn.droidlover.xdroid.hold.info.SharedInfo;
import cn.droidlover.xdroid.tools.log.Logger;
import cn.droidlover.xdroid.tools.utils.ActivityManage;
import cn.droidlover.xdroid.tools.utils.ContextHolder;
import cn.droidlover.xdroid.tools.utils.NetUtil;
import cn.droidlover.xdroid.tools.utils.Util;
import cn.jpush.android.api.JPushInterface;
import okhttp3.Call;
import okhttp3.Response;


public abstract class JsonCallback extends Callback<ResultBean> {

    @Override
    public void onError(Call call, Exception e, int id) {
        if (e instanceof UnknownHostException) {
            //无网络接
            Util.toast("服务器连接异常");
        } else if (!NetUtil.isConnected(ContextHolder.getContext())) {
            Util.toast("网络连接异常,请检查网络");
        } else if (e instanceof SocketTimeoutException) {
            Util.toast("服务器连接超时");
        } else {
            Util.toast(e.getMessage());
        }
    }

    @Override
    public boolean validateReponse(Response response, int id) {
        return true;
    }

    @Override
    public ResultBean parseNetworkResponse(Response response, int id) throws Exception {
        assert response.body() != null;
        String json=response.body().string();
        Logger.e("response=",json);
        if (response.code() == 200) {
            return new ResultBean(response.code(), json);
        } else {
            try {
                return new Gson().fromJson(json, ResultBean.class);
            }catch (Exception e){
                return null;
            }
        }
    }

    @Override
    public void onResponse(ResultBean resultBean, int id) {
        if (resultBean == null) {
            Util.toast("数据请求异常，请稍后再试");
        } else if (resultBean.getCode() == 200) {
            onSuccess(resultBean.getData(), id);
        } else if (resultBean.getCode() == 400) {
            Util.toast(resultBean.getMessage());
        } else if (resultBean.getCode() == 401) {
            SharedInfo.getInstance().remove(UserInfo.class);
            SharedInfo.getInstance().remove(Constant.KEY_TOKEN);
            SharedInfo.getInstance().remove(Constant.CHILD_TYPE);
            JPushInterface.deleteAlias(ActivityManage.peek(), Constant.JPUSH_MESSAGE);
            if (ActivityManage.peek() instanceof LoginYzmActivity) {
                return;
            }
            ActivityManage.push(LoginYzmActivity.class);
        } else if (resultBean.getCode() == 403) {
            if ("请完成司机认证后接单".equals(resultBean.getMessage())) {
                ActivityManage.push(InfoAuthAct.class);
            }
        } else if (resultBean.getCode() >= 500) {
            Util.toast("服务器连接异常，请稍后再试");
        }
    }

    public abstract void onSuccess(String data, int id);
}