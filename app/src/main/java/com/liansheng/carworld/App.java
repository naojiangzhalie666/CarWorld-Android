package com.liansheng.carworld;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import androidx.multidex.MultiDexApplication;

import com.baidu.idl.face.platform.LivenessTypeEnum;
import com.baidu.ocr.sdk.OCR;
import com.baidu.ocr.sdk.OnResultListener;
import com.baidu.ocr.sdk.exception.OCRError;
import com.baidu.ocr.sdk.model.AccessToken;
import com.kongzue.dialog.util.DialogSettings;
import com.liansheng.carworld.face.model.Const;
import com.liansheng.carworld.net.UrlKit;
import com.liansheng.carworld.oss.OssServiceUtil;
import com.liansheng.carworld.view.QuickLoginUiConfig;
import com.netease.nis.quicklogin.QuickLogin;
import com.pgyersdk.crash.PgyCrashManager;
import com.pgyersdk.feedback.PgyerFeedbackManager;
import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.umeng.analytics.MobclickAgent;
import com.umeng.commonsdk.UMConfigure;
import com.xuexiang.xui.XUI;
import com.zhy.http.okhttp.OkHttpUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import cn.droidlover.xdroid.hold.info.SharedInfo;
import cn.jpush.android.api.JPushInterface;
import okhttp3.OkHttpClient;


public class App extends MultiDexApplication {

    private static Context context;
    private static App self;
    // IWXAPI 是第三方app和微信通信的openApi接口
    private IWXAPI api;
    public QuickLogin quickLogin;
    // 动作活体条目集合
    public static List<LivenessTypeEnum> livenessList = new ArrayList<>();
    // 活体随机开关
    public static boolean isLivenessRandom = true;
    // 语音播报开关
    public static boolean isOpenSound = true;
    // 活体检测开关
    public static boolean isActionLive = true;
    // 质量等级（0：正常、1：宽松、2：严格、3：自定义）
    public static int qualityLevel = Const.QUALITY_NORMAL;

    public static App getAppSelf() {
        return self;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        SharedInfo.init("car_world");
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(10000L, TimeUnit.MILLISECONDS)
                .readTimeout(10000L, TimeUnit.MILLISECONDS)
                //其他配置
                .build();
        OkHttpUtils.initClient(okHttpClient);
        XUI.init(this);
        regToWx();
//        EnvUtils.setEnv(EnvUtils.EnvEnum.SANDBOX);//用于切换沙箱环境与生产环境；
//        setDefaultFont(this, "SERIF", "font/SourceHanSansCN-Regular.ttf");
        initFastLogin();
        dialogInit();
        if (!UrlKit.IS_DEBUG) {
            UMConfigure.init(this, UrlKit.UMENG_KEY, "default-channel", UMConfigure.DEVICE_TYPE_PHONE, "");
            MobclickAgent.setPageCollectionMode(MobclickAgent.PageMode.AUTO);
        }
        initAccessTokenWithAkSk();
//        OssServiceUtil.getInstance().init();
    }

    private void initAccessTokenWithAkSk() {
        OCR.getInstance(this).initAccessTokenWithAkSk(new OnResultListener<AccessToken>() {
            @Override
            public void onResult(AccessToken result) {
                String token = result.getAccessToken();
            }

            @Override
            public void onError(OCRError error) {
                error.printStackTrace();
//                alertText("AK，SK方式获取token失败", error.getMessage());
            }
        }, getApplicationContext(), "hg0HaAziM4xzxxVWGmovADIr", "4TaPnq012rWbtddYzGMLFgHKfDagG1ev");
    }


    public void dialogInit() {
        DialogSettings.init();
        DialogSettings.checkRenderscriptSupport(this);
        DialogSettings.isUseBlur = true;
        DialogSettings.autoShowInputKeyboard = true;
        DialogSettings.style = DialogSettings.STYLE.STYLE_MIUI;
        DialogSettings.theme = DialogSettings.THEME.LIGHT;
    }

    private void initFastLogin() {
        quickLogin = QuickLogin.getInstance(getApplicationContext(), UrlKit.NETESA_ID);
        quickLogin.setUnifyUiConfig(QuickLoginUiConfig.getUiConfig(getApplicationContext()));
//        quickLogin.setFetchNumberTimeout(3000);
//        quickLogin.setPrefetchNumberTimeout(3000);
        quickLogin.setDebugMode(false);
    }

    private void regToWx() {
        // 通过WXAPIFactory工厂，获取IWXAPI的实例
        api = WXAPIFactory.createWXAPI(this, UrlKit.APP_ID, true);
        // 将应用的appId注册到微信
        api.registerApp(UrlKit.APP_ID);
        //建议动态监听微信启动广播进行注册到微信
        registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                // 将该app注册到微信
                api.registerApp(UrlKit.APP_ID);
            }
        }, new IntentFilter(ConstantsAPI.ACTION_REFRESH_WXAPP));

    }

    public static Context getContext() {
        return context;
    }
}
