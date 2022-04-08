package com.liansheng.carworld.activity.logic;

import android.app.Activity;
import android.text.TextUtils;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.kongzue.dialog.interfaces.OnDialogButtonClickListener;
import com.kongzue.dialog.util.BaseDialog;
import com.kongzue.dialog.v3.MessageDialog;
import com.liansheng.carworld.App;
import com.liansheng.carworld.R;
import com.liansheng.carworld.activity.me.InfoAuthAct;
import com.liansheng.carworld.activity.user.LoginYzmActivity;
import com.liansheng.carworld.bean.UserInfo;
import com.liansheng.carworld.bean.me.CompanyBean;
import com.liansheng.carworld.bean.me.LicensesBean;
import com.liansheng.carworld.kit.Constant;
import com.liansheng.carworld.net.JsonCallback;
import com.liansheng.carworld.net.NetApi;
import com.liansheng.carworld.net.UrlKit;
import com.netease.nis.quicklogin.QuickLogin;
import com.netease.nis.quicklogin.listener.QuickLoginPreMobileListener;
import com.netease.nis.quicklogin.listener.QuickLoginTokenListener;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import cn.droidlover.xdroid.hold.info.SharedInfo;
import cn.droidlover.xdroid.tools.log.Logger;
import cn.droidlover.xdroid.tools.utils.ActivityManage;
import cn.droidlover.xdroid.tools.utils.NetworkUtil;
import cn.droidlover.xdroid.tools.utils.Util;

public class UserLogic {


    public static void checkLogin(Class cls) {
        checkLogin(cls, false);
    }

    public static void checkLogin(Class<? extends Activity> cls, boolean skip) {
        if (!TextUtils.isEmpty(SharedInfo.getInstance().getValue(Constant.KEY_TOKEN, "").toString())) {
            ActivityManage.push(cls);
        } else {
            if (skip) {
                ActivityManage.push(LoginYzmActivity.class);
            } else {
                Util.toast("请先登录");
            }
        }
    }

    //是否是汽贸或4S
    public static boolean isCarShop() {
        UserInfo userInfo = SharedInfo.getInstance().getEntity(UserInfo.class);
        if (userInfo == null || userInfo.getCompanys() == null) {
            return false;
        }
        for (int i = 0; i < userInfo.getCompanys().size(); i++) {
            CompanyBean bean = userInfo.getCompanys().get(i);
            if ((Constant.STORE_TYPE_2.equals(bean.getType()) || Constant.STORE_TYPE_1.equals(bean.getType())) && Constant.ID_STATUS_PASSED.equals(bean.getStatus())) {
                return true;
            }
        }
        return false;
    }
    public static String getRealName(){
        UserInfo userInfo = SharedInfo.getInstance().getEntity(UserInfo.class);
        if(userInfo.getLicenses().size()>0){
            for (int i = 0; i < userInfo.getLicenses().size(); i++) {
                LicensesBean bean = userInfo.getLicenses().get(i);
                if(bean.getType().equals("IdentityCard")){
                    return bean.getOwner();
                }
            }
        }
        return "";
    }

    //是否实名认证
    public static boolean isRealAuth() {
        UserInfo userInfo = SharedInfo.getInstance().getEntity(UserInfo.class);
        if (userInfo == null) {
            ActivityManage.push(LoginYzmActivity.class);
            return false;
        }
        for (int i = 0; i < userInfo.getLicenses().size(); i++) {
            LicensesBean bean = userInfo.getLicenses().get(i);
            if (Constant.AUTH_IDENTITY_CARD.equals(bean.getType()) && Constant.ID_STATUS_PASSED.equals(bean.getStatus())) {
                return true;
            }
        }
        MessageDialog.build((AppCompatActivity) ActivityManage.peek())
                .setMessage("您还未进行实名认证？暂时无法使用本APP功能，请进行认证\n")
                .setOkButton("去认证", new OnDialogButtonClickListener() {
                    @Override
                    public boolean onClick(BaseDialog baseDialog, View v) {
                        ActivityManage.push(InfoAuthAct.class);
                        return false;
                    }
                })
                .setOkButtonDrawable(R.drawable.shape_button_red_bg)
                .show();
        return false;
    }

    public interface LoginListener {
        void loginSuccess(String token, String data);
    }

    public static void login(HashMap<String, Object> map, LoginListener listener) {

        NetApi.post(UrlKit.LOGIN, map, new JsonCallback() {

            @Override
            public void onSuccess(String data, int id) {
                Map<String, String> hashMap = new Gson().fromJson(data, Map.class);
                SharedInfo.getInstance().saveValue(Constant.KEY_TOKEN, hashMap.get("token"));
                NetApi.get(UrlKit.USER_INFORM, null, new JsonCallback() {
                    @Override
                    public void onSuccess(String response, int id) {
                        if (listener != null) {
                            listener.loginSuccess(hashMap.get("token"), response);
                        }
                    }
                });
            }

        });
    }

    public static boolean isLogin() {
        return !TextUtils.isEmpty(SharedInfo.getInstance().getValue(Constant.KEY_TOKEN, "").toString());
    }

    public static void quickLogin(LoginResult loginResult) {
        final QuickLogin quickLogin = ((App) ActivityManage.peek().getApplication()).quickLogin;
        quickLogin.prefetchMobileNumber(new QuickLoginPreMobileListener() {
            @Override
            public void onGetMobileNumberSuccess(String YDToken, final String mobileNumber) {
                NetworkUtil.dismissCutscenes();
                // 在该成功回调中直接调用onePass接口进行一键登录即可
                quickLogin.onePass(new QuickLoginTokenListener() {
                    @Override
                    public void onGetTokenSuccess(final String YDToken, final String accessCode) {
                        Logger.e(String.format("yd token is:%s accessCode is:%s", YDToken, accessCode));
                        loginResult.login(YDToken, accessCode);
                        quickLogin.quitActivity();
                    }

                    @Override
                    public void onGetTokenError(String YDToken, String msg) {
                        Logger.e("获取运营商授权码失败:" + msg);
                    }

                    @Override
                    public void onCancelGetToken() {
                        super.onCancelGetToken();

                    }

                    @Override
                    public boolean onExtendMsg(JSONObject extendMsg) {
                        return super.onExtendMsg(extendMsg);
                    }
                });
            }

            @Override
            public void onGetMobileNumberError(String YDToken, final String msg) {
                //  在该错误回调中能够获取到此次请求的易盾token以及预取号获取手机掩码失败的原因
                NetworkUtil.dismissCutscenes();
                loginResult.onError(msg);
            }

            @Override
            public boolean onExtendMsg(JSONObject extendMsg) {
                Logger.e("获取的扩展字段内容为:" + extendMsg.toString());
                // 如果接入者自定义了preCheck接口，可在该方法中通过返回true或false来控制是否快速降级
                return super.onExtendMsg(extendMsg);
            }
        });
    }

}
