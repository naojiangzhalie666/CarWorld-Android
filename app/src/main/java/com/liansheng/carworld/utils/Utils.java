package com.liansheng.carworld.utils;

import android.Manifest;
import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.kongzue.dialog.interfaces.OnDialogButtonClickListener;
import com.kongzue.dialog.util.BaseDialog;
import com.kongzue.dialog.util.DialogSettings;
import com.kongzue.dialog.util.TextInfo;
import com.kongzue.dialog.v3.MessageDialog;
import com.liansheng.carworld.App;
import com.liansheng.carworld.R;
import com.liansheng.carworld.activity.logic.OtherLogic;
import com.liansheng.carworld.bean.Directory;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;

import org.apache.commons.mycodec.digest.DigestUtils;

import java.util.Arrays;
import java.util.Map;

import cn.droidlover.xdroid.tools.utils.Util;
import pub.devrel.easypermissions.EasyPermissions;

/**
 * Created by hzhuqi on 2019/1/29.
 */
public class Utils {

    /**
     * @param secretKey
     * @param params
     * @return
     */
    public static String generateSign(String secretKey, Map<String, String> params) {
        String signature = "";
        if (TextUtils.isEmpty(secretKey) || params.size() == 0) {
            return signature;
        }
        try {
            // 1. 参数名按照ASCII码表升序排序
            String[] keys = params.keySet().toArray(new String[0]);
            Arrays.sort(keys);
            // 2. 按照排序拼接参数名与参数值
            StringBuilder paramBuffer = new StringBuilder();
            for (String key : keys) {
                paramBuffer.append(key).append(params.get(key) == null ? "" : params.get(key));
            }
            // 3. 将secretKey拼接到最后
            paramBuffer.append(secretKey);
            // 4. MD5是128位长度的摘要算法，用16进制表示，一个十六进制的字符能表示4个位，所以签名后的字符串长度固定为32个十六进制字符。
            signature = DigestUtils.md5Hex(paramBuffer.toString().getBytes("UTF-8"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return signature;
    }

    public static void showToast(final Activity act, final String tip) {
        act.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(act.getApplicationContext(), tip, Toast.LENGTH_LONG).show();
            }
        });

    }

    public static void showToast(String msg) {
        Toast.makeText(App.getContext(), msg, Toast.LENGTH_LONG).show();
    }

    public static String getRandomString(int length) {
        String KeyString = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        StringBuffer sb = new StringBuffer();
        int len = KeyString.length();
        for (int i = 0; i < length; i++) {
            sb.append(KeyString.charAt((int) Math.round(Math.random() * (len - 1))));
        }
        return sb.toString();
    }

    public static void copy(Activity activity, String text) {
        //获取剪贴版
        ClipboardManager clipboard = (ClipboardManager) activity.getSystemService(Context.CLIPBOARD_SERVICE);
        //创建ClipData对象
        //第一个参数只是一个标记，随便传入。
        //第二个参数是要复制到剪贴版的内容
        ClipData clip = ClipData.newPlainText("wx", text);
        //传入clipdata对象.
        clipboard.setPrimaryClip(clip);
        showToast("已复制微信名到剪切板");
    }

    /**
     * 将dp值转换为px值
     *
     * @param dipValue dp值
     * @return px
     */
    public static int dip2px(Context context, float dipValue) {
        try {
            final float scale = context.getResources().getDisplayMetrics().density;
            return (int) (dipValue * scale + 0.5f);
        } catch (Exception e) {
            return (int) dipValue;
        }
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static float px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return pxValue / scale + 0.5f;
    }

    public static int getScreenPxWidth(Context context) {
        WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        return outMetrics.widthPixels;
    }

    public static int getScreenDpWidth(Context context) {
        int pxWidth = getScreenPxWidth(context);
        return (int) px2dip(context, pxWidth);
    }

    public static int getScreenPxHeight(Context context) {
        WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        return outMetrics.heightPixels;
    }

    public static int getScreenDpHeight(Context context) {
        int pxHeight = getScreenPxHeight(context);
        return (int) px2dip(context, pxHeight);
    }


    public static void getPhoto(Activity activity) {
        getPhoto(activity, PictureConfig.SINGLE, 6);
    }

    public static void getPhoto(Activity activity, int requesCode) {
        getPhoto(activity, PictureConfig.SINGLE, 6, requesCode);
    }

    public static void getPhoto(Activity activity, int selectionMode, int maxSelectNum) {
        getPhoto(activity, selectionMode, maxSelectNum, PictureConfig.CHOOSE_REQUEST);
    }

    public static void getPhoto(Activity activity, int selectionMode, int maxSelectNum, int requestCode) {
        PictureSelector.create(activity)
                .openGallery(PictureMimeType.ofImage())
                .imageEngine(GlideEngine.createGlideEngine())
                .selectionMode(selectionMode)
//                .isSingleDirectReturn(true)
                .isWeChatStyle(true)
                .isCompress(true)
                .maxSelectNum(maxSelectNum)
                .minimumCompressSize(256)
                .compressQuality(90)
                .forResult(requestCode);
    }

    public static void getCamera(Activity activity) {
        PictureSelector.create(activity)
                .openCamera(PictureMimeType.ofImage())
                .imageEngine(GlideEngine.createGlideEngine())
                .selectionMode(PictureConfig.SINGLE)
                .isSingleDirectReturn(true)
                .isWeChatStyle(true)
                .isCompress(true)
                .minimumCompressSize(256)
                .compressQuality(90)
                .forResult(PictureConfig.CHOOSE_REQUEST);
    }

    public static void callPhone(AppCompatActivity context, String text, Directory.ItemsBean item) {
        DialogSettings.init();
        DialogSettings.checkRenderscriptSupport(context);
        DialogSettings.DEBUGMODE = true;
        DialogSettings.isUseBlur = true;
        DialogSettings.autoShowInputKeyboard = true;
        DialogSettings.style = DialogSettings.STYLE.STYLE_MIUI;
        DialogSettings.theme = DialogSettings.THEME.LIGHT;
        TextInfo textInfo = new TextInfo();
        textInfo.setFontColor(ContextCompat.getColor(context, R.color.white));
        MessageDialog.build(context)
                .setCancelButtonDrawable(R.drawable.shape_button_red_bg)
                .setTitle("温馨提示")
                .setMessage(text)
                .setButtonTextInfo(textInfo)
                .setCancelButton("展厅座机", new OnDialogButtonClickListener() {
                    @Override
                    public boolean onClick(BaseDialog baseDialog, View v) {
                        if (TextUtils.isEmpty(item.getPhone())) {
                            Util.toast("暂无联系方式");
                            return false;
                        }
                        OtherLogic.toCall(context,item.getPhone());
                        return false;
                    }
                })
                .setOkButtonDrawable(R.drawable.shape_button_red_bg)
                .setOkButton("二网手机", new OnDialogButtonClickListener() {
                    @Override
                    public boolean onClick(BaseDialog baseDialog, View v) {
                        if (TextUtils.isEmpty(item.getMobile())) {
                            Util.toast("暂无联系方式");
                            return false;
                        }
                        OtherLogic.toCall(context,item.getMobile());
                        return false;
                    }
                })
                .show();
    }
}
