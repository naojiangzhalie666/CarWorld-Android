package com.liansheng.carworld.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.liansheng.carworld.App;
import com.liansheng.carworld.R;
import com.liansheng.carworld.activity.user.LoginYzmActivity;
import com.liansheng.carworld.bean.event.UIEvent;
import com.liansheng.carworld.net.UrlKit;
import com.liansheng.carworld.utils.Utils;
import com.netease.nis.quicklogin.QuickLogin;
import com.netease.nis.quicklogin.helper.UnifyUiConfig;
import com.netease.nis.quicklogin.utils.LoginUiHelper;

import org.greenrobot.eventbus.EventBus;

import cn.droidlover.xdroid.tools.utils.ActivityManage;
import cn.droidlover.xdroid.tools.utils.Util;

/**
 * Created by hzhuqi on 2019/12/31
 */
public class QuickLoginUiConfig {
    public static UnifyUiConfig getUiConfig(final Context context) {
//        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(50, 50);
//        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.CENTER_VERTICAL);
//        layoutParams.topMargin = 30;
//        layoutParams.rightMargin = 50;
        LayoutInflater inflater = LayoutInflater.from(context);
        RelativeLayout otherLoginRel = (RelativeLayout) inflater.inflate(R.layout.custom_other_login, null);
        RelativeLayout.LayoutParams layoutParamsOther = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
//        layoutParamsOther.setMargins(0, Utils.dip2px(context, 40), 0, Utils.dip2px(context, 0));
        layoutParamsOther.addRule(RelativeLayout.CENTER_IN_PARENT);
//        layoutParamsOther.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        otherLoginRel.findViewById(R.id.relative).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                QuickLogin quickLogin = ((App) ActivityManage.peek().getApplication()).quickLogin;
                quickLogin.quitActivity();
                Activity activity = ActivityManage.peek();
                Intent intent = new Intent(activity, LoginYzmActivity.class);
                activity.startActivity(intent);
            }
        });
        otherLoginRel.findViewById(R.id.back).setOnClickListener(v -> {
            QuickLogin quickLogin = ((App) ActivityManage.peek().getApplication()).quickLogin;
            quickLogin.quitActivity();
        });
//        otherLoginRel.findViewById(R.id.login).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                EventBus.getDefault().post(new UIEvent(UIEvent.UPDATE_LOGIN));
//            }
//        });
        otherLoginRel.setLayoutParams(layoutParamsOther);
        int X_OFFSET = 0;
        int BOTTOM_OFFSET = 0;

        UnifyUiConfig uiConfig = new UnifyUiConfig.Builder()
                .addCustomView(otherLoginRel, "relative", UnifyUiConfig.POSITION_IN_TITLE_BAR, null)
                // 状态栏
                .setStatusBarColor(Color.WHITE)
                .setStatusBarDarkColor(true)
                // 设置导航栏
                .setNavigationTitle("登录")
                .setNavTitleSize(20)
                .setNavigationTitleColor(Color.BLACK)
                .setNavigationBackgroundColor(Color.WHITE)
                .setNavigationIcon("icon_back2")
                .setNavigationBackIconWidth(20)
                .setNavigationBackIconHeight(20)
                .setHideNavigationBackIcon(false)
                .setHideNavigation(false)
                // 设置logo
                .setLogoIconName("ic_login_logo")
                .setLogoWidth(120)
                .setLogoHeight(120)
                .setLogoXOffset(X_OFFSET)
                .setLogoTopYOffset(40)
                .setHideLogo(false)
                //手机掩码
                .setMaskNumberColor(Color.BLACK)
                .setMaskNumberSize(20)
                .setMaskNumberXOffset(X_OFFSET)
                .setMaskNumberTopYOffset(160)
                .setMaskNumberBottomYOffset(BOTTOM_OFFSET)
                // 认证品牌
                .setSloganSize(15)
                .setSloganColor(Color.BLACK)
                .setSloganXOffset(X_OFFSET)
                .setSloganTopYOffset(190)
                .setSloganBottomYOffset(BOTTOM_OFFSET)
                // 登录按钮
                .setLoginBtnText("本机号码一键登录")
                .setLoginBtnTextColor(Color.WHITE)
                .setLoginBtnBackgroundRes("shape_button_red_bg")
                .setLoginBtnWidth(280)
                .setLoginBtnHeight(50)
                .setLoginBtnTextSize(16)
                .setLoginBtnXOffset(X_OFFSET)
                .setLoginBtnBottomYOffset(BOTTOM_OFFSET)
                .setLoginBtnTopYOffset(255)
                // 隐私栏
                .setPrivacyTextStart("同意 ")
                .setProtocolText("隐私协议")
                .setProtocolLink(UrlKit.H5_SECRET_REGULAR)
                .setProtocol2Text("用户协议")
                .setProtocol2Link(UrlKit.H5_REGISTER_REGULAR)
                .setPrivacyTextEnd("")
                .setPrivacyTextColor(Color.BLACK)
                .setPrivacyProtocolColor(Color.RED)
//                .setHidePrivacyCheckBox(false)
                .setPrivacyMarginLeft(X_OFFSET)
                .setPrivacyState(true)
                .setPrivacySize(12)
//                .setPrivacyTopYOffset(510)
                .setPrivacyBottomYOffset(20)
                .setPrivacyMarginLeft(30)
                .setPrivacyMarginRight(20)
                .setPrivacyTextGravityCenter(true)
//                .setCheckedImageName("yd_checkbox_checked2")
//                .setUnCheckedImageName("yd_checkbox_unchecked2")
                // 协议详情页导航栏
                .setProtocolPageNavTitle("一键登录服务条款")
//                .setProtocolPageNavBackIcon("yd_checkbox_checked")
                .setProtocolPageNavColor(Color.WHITE)
//                .setBackgroundImage("bg1")
                // 自定义控件
                .build(context);
        return uiConfig;
    }

    public static UnifyUiConfig getDialogUiConfig(final Activity context) {
        ImageView closeBtn = new ImageView(context);
        closeBtn.setImageResource(R.drawable.close);
        closeBtn.setScaleType(ImageView.ScaleType.FIT_XY);
        closeBtn.setBackgroundColor(Color.TRANSPARENT);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(60, 60);
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.CENTER_VERTICAL);
        layoutParams.topMargin = 30;
        layoutParams.rightMargin = 50;
        closeBtn.setLayoutParams(layoutParams);

        LayoutInflater inflater = LayoutInflater.from(context);
        RelativeLayout otherLoginRel = (RelativeLayout) inflater.inflate(R.layout.custom_other_login, null);
        RelativeLayout.LayoutParams layoutParamsOther = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        layoutParamsOther.setMargins(0, 0, 0, Utils.dip2px(context, 100));
        layoutParamsOther.addRule(RelativeLayout.CENTER_HORIZONTAL);
        layoutParamsOther.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        otherLoginRel.setLayoutParams(layoutParamsOther);
        int X_OFFSET = 0;
        DisplayMetrics dm = new DisplayMetrics();
        context.getWindow().getWindowManager().getDefaultDisplay().getMetrics(dm);
        int dialogWidth = (int) (Utils.getScreenDpWidth(context) * 0.85);
        int dialogHeight = (int) (Utils.getScreenDpHeight(context) * 0.7);
        UnifyUiConfig uiConfig = new UnifyUiConfig.Builder()
                // 设置导航栏
                .setNavigationTitle("登录/注册")
                .setNavigationTitleColor(Color.RED)
                .setNavigationBackgroundColor(Color.BLUE)
                .setNavigationIcon("yd_checkbox_checked")
                .setHideNavigation(false)
                // 设置logo
                .setLogoIconName("ico_logo")
                .setLogoWidth(50)
                .setLogoHeight(50)
                .setLogoXOffset(X_OFFSET)
                .setLogoTopYOffset(15)
//                .setLogoBottomYOffset(300)
                .setHideLogo(false)
                //手机掩码
                .setMaskNumberColor(Color.RED)
                .setMaskNumberSize(15)
                .setMaskNumberTopYOffset(100)
                .setMaskNumberXOffset(X_OFFSET)
                // 认证品牌
                .setSloganSize(15)
                .setSloganColor(Color.RED)
                .setSloganTopYOffset(70)
                .setSloganXOffset(X_OFFSET)
                // 登录按钮
                .setLoginBtnText("易盾一键登录")
                .setLoginBtnTextColor(Color.WHITE)
                .setLoginBtnBackgroundRes("btn_shape_login_onepass")
                .setLoginBtnWidth(200)
                .setLoginBtnHeight(40)
                .setLoginBtnTextSize(15)
                .setLoginBtnXOffset(X_OFFSET)
                .setLoginBtnTopYOffset(150)
                // 隐私栏
                .setPrivacyTextStart("登录即同意")
                .setProtocolText("服务条款一")
                .setProtocolLink("https://www.baidu.com")
                .setProtocol2Text("服务条款二")
                .setProtocol2Link("https://www.baidu.com")
                .setPrivacyTextEnd("且授权易盾一键登录SDK使用本机号码")
                .setPrivacyTextColor(Color.GREEN)
                .setPrivacyProtocolColor(Color.RED)
                .setHidePrivacyCheckBox(false)
                .setPrivacyMarginLeft(X_OFFSET)
                .setPrivacyState(true)
                .setPrivacySize(12)
                .setPrivacyBottomYOffset(10)
                .setPrivacyTextGravityCenter(false)
                .setCheckedImageName("yd_checkbox_checked")
                .setUnCheckedImageName("yd_checkbox_unchecked")
                // 协议详情页导航栏
                .setProtocolPageNavTitle("易盾一键登录SDK服务条款")
                .setProtocolPageNavBackIcon("yd_checkbox_checked")
                .setProtocolPageNavColor(Color.BLUE)

                .addCustomView(otherLoginRel, "relative", UnifyUiConfig.POSITION_IN_BODY, null)
                .addCustomView(closeBtn, "close_btn", UnifyUiConfig.POSITION_IN_TITLE_BAR, new LoginUiHelper.CustomViewListener() {
                    @Override
                    public void onClick(Context context, View view) {
                        Toast.makeText(context.getApplicationContext(), "点击了右上角X按钮", Toast.LENGTH_SHORT).show();
                    }
                })
                .setDialogMode(true, dialogWidth, dialogHeight, 0, 0, false)
                .build(context.getApplicationContext());
        return uiConfig;
    }
}
