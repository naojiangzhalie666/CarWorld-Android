package com.liansheng.carworld.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.reflect.TypeToken;
import com.kongzue.dialog.interfaces.OnDialogButtonClickListener;
import com.kongzue.dialog.util.BaseDialog;
import com.kongzue.dialog.v3.MessageDialog;
import com.liansheng.carworld.activity.logic.OtherLogic;
import com.liansheng.carworld.bean.UserInfo;
import com.liansheng.carworld.net.UrlKit;
import com.liansheng.carworld.utils.JSONUtils;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;
import com.umeng.analytics.MobclickAgent;
import com.xuexiang.xui.utils.SnackbarUtils;

import cn.droidlover.xdroid.base.UiCallback;
import cn.droidlover.xdroid.base.UiDelegate;
import cn.droidlover.xdroid.base.UiDelegateBase;
import cn.droidlover.xdroid.event.BusFactory;
import cn.droidlover.xdroid.kit.KnifeKit;
import cn.droidlover.xdroid.tools.utils.ActivityManage;
import pub.devrel.easypermissions.EasyPermissions;


public abstract class BaseActivity extends AppCompatActivity implements UiCallback {
    protected Activity context;
    protected UiDelegate uiDelegate;
    public static int MAX_PAGE = 20;//默认最大页数
    public static final int DEFULT_RESULT = 20;//默认返回条数
    public final static int PER_LOACTION = 0X003;
    public final static String[] PERMS_LOACTION_STATE = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.context = this;
        ActivityManage.push(this);
        if (getLayoutId() > 0) {
            setContentView(getLayoutId());
            KnifeKit.bind(this);
        }
        setListener();
        QMUIStatusBarHelper.translucent(this);
        QMUIStatusBarHelper.setStatusBarLightMode(this);
        initData(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    protected UiDelegate getUiDelegate() {
        if (uiDelegate == null) {
            uiDelegate = UiDelegateBase.create(this);
        }
        return uiDelegate;
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (useEventBus()) {
            BusFactory.getBus().register(this);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        getUiDelegate().resume();
        if (!UrlKit.IS_DEBUG) {
            MobclickAgent.onResume(this);
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        getUiDelegate().pause();
        if (!UrlKit.IS_DEBUG) {
            MobclickAgent.onPause(this);
        }
    }

    @Override
    public boolean useEventBus() {
        return false;
    }

    @Override
    public void setListener() {
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        BusFactory.getBus().unregister(this);
//        getUiDelegate().destory();
        ActivityManage.remove(this);
    }

    public void showToast(String text) {
        showToast(text, false);
    }

    public void showToast(String text, boolean isLong) {
        if (text != null) {
            Toast.makeText(this, text,
                    isLong ? Toast.LENGTH_LONG : Toast.LENGTH_SHORT).show();
        }
    }

    public void showSnackToast(View view, String text) {
        SnackbarUtils.Custom(view, text, 3000)
                .radius(30, 1, Color.parseColor("#f26868"))
                .backColor(Color.parseColor("#f26868"))
                .messageCenter()
                .show();
    }

    public void jumpToActivityAndClearTask(Class activity) {
        Intent intent = new Intent(this, activity);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    public void toActivity(Class<?> cls, Bundle bundle) {
        Intent intent = new Intent(this, cls);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivity(intent);
    }

    public UserInfo getLoginResult() {
        SharedPreferences sp = getSharedPreferences(UrlKit.LOGIN_STATE,
                Context.MODE_PRIVATE);
        String json = sp.getString(UrlKit.LOGIN_RESULT, null);
        if (json == null) {
            return null;
        }
        UserInfo loginResult = JSONUtils.fromJson(json,
                new TypeToken<UserInfo>() {
                });
        return loginResult;
    }

    public void setLoginResult(UserInfo loginResult, boolean isFinish) {
        SharedPreferences sp = getSharedPreferences(UrlKit.LOGIN_STATE,
                Context.MODE_PRIVATE);
        String json = JSONUtils.toJson(loginResult,
                new TypeToken<UserInfo>() {
                }.getType());
        sp.edit().putString(UrlKit.LOGIN_RESULT, json).commit();
        if (isFinish) {
            finish();
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        if (newConfig.fontScale != 1)//非默认值
            getResources();
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public Resources getResources() {
        Resources res = super.getResources();
        if (res.getConfiguration().fontScale != 1) {//非默认值
            Configuration newConfig = new Configuration();
            newConfig.setToDefaults();//设置默认
            res.updateConfiguration(newConfig, res.getDisplayMetrics());
        }
        return res;
    }//设置字体不跟随系统字体大小改变

    public void callPhone(AppCompatActivity context, String text, String tel) {
        MessageDialog.build(context)
                .setTitle("温馨提示")
                .setMessage(text)
                .setCancelButton("取消", new OnDialogButtonClickListener() {
                    @Override
                    public boolean onClick(BaseDialog baseDialog, View v) {
                        return false;
                    }
                })
                .setOkButton("联系", new OnDialogButtonClickListener() {
                    @Override
                    public boolean onClick(BaseDialog baseDialog, View v) {
                        OtherLogic.toCall(context, tel);
                        return false;
                    }
                })
                .show();
    }

    public static boolean isMobilPhone(String phone) {
//        String regex = "^((13[0-9])|(14[5|7])|(15([0-3]|[5-9]))|(17[013678])|(18[0,5-9]))\\d{8}$";
        if (phone.length() != 11) {
            return false;
//        } else {
//            Pattern p = Pattern.compile(regex);
//            Matcher m = p.matcher(phone);
//            boolean isMatch = m.matches();
//            return isMatch;
        }
        if (!phone.startsWith("1")) {
            return false;
        }
        return true;
    }

}
