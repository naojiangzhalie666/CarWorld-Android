package cn.droidlover.xdroid.tools.utils;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;

import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;

import java.io.File;

/**
 * Description:自定义android Intent类，
 * 可用于获取打开以下文件的intent
 * PDF,PPT,WORD,EXCEL,CHM,HTML,TEXT,AUDIO,VIDEO
 * 包含各种系统自带意图的工具类
 */
public class IntentUtil {
    // 拨打电话权限申请
    public static final int PERMISSIONS_REQUEST_CALL_PHONE = 1;
    // SD卡读写权限
    public static final int PERMISSIONS_READ_EXTERNAL_STORAGE = 2;
    // 发送短信权限
    public static final int PERMISSIONS_REQUEST_SEND_SMS = 3;

    public static void callPhone(Activity activity, String phone) {
        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.CALL_PHONE}, PERMISSIONS_REQUEST_CALL_PHONE);
        } else {
            Intent intent = new Intent(Intent.ACTION_CALL);
            Uri data = Uri.parse("tel:" + phone);
            intent.setData(data);
            activity.startActivity(intent);
        }
    }

    public static void gotoCallPhoneActivity(Activity activity, String phone) {
        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.CALL_PHONE}, PERMISSIONS_REQUEST_CALL_PHONE);
        } else {
            Intent intent = new Intent(Intent.ACTION_DIAL);
            Uri data = Uri.parse("tel:" + phone);
            intent.setData(data);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            activity.startActivity(intent);
        }
    }

    public static void sendSMS(Activity activity, String phone, String content) {
        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.SEND_SMS}, PERMISSIONS_REQUEST_SEND_SMS);
        } else {
            Intent intent = new Intent(Intent.ACTION_SENDTO);
            Uri data = Uri.parse("smsto:" + phone);
            intent.setData(data);
            intent.putExtra("sms_body", content);
            activity.startActivity(intent);
        }
    }

    /**
     * 安装界面
     *
     * @param filePath
     * @return
     */
    public static void install(Activity activity, String filePath) {
        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSIONS_READ_EXTERNAL_STORAGE);
        } else {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            // 版本在7.0以上是不能直接通过uri访问的
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {

                // 上述方法
                Uri uri = FileProvider.getUriForFile(activity, "cn.droidlover.xdroid.provider.fileprovider", new File(filePath));
                intent.setDataAndType(uri, "application/vnd.android.package-archive");
                // 安装完成会有提示
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            } else {
                Uri uri = Uri.fromFile(new File(filePath));
                intent.setDataAndType(uri, "application/vnd.android.package-archive");
                // 安装完成会有提示
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            }
            activity.startActivity(intent);
        }
    }
    /**
     * 尝试打开此文件
     *
     * @param filePath
     *         文件路径
     */
    public static Intent openFile(String filePath) {
        File file = new File(filePath);
        if (!file.exists())
            return null;
        /** 取得扩展名 */
        String end = file.getName().substring(file.getName().lastIndexOf(".") + 1, file.getName().length()).toLowerCase();
        /** 依扩展名的类型决定MimeType */
        if (end.equals("m4a") || end.equals("mp3") || end.equals("mid") || end.equals("xmf") || end.equals("ogg") || end.equals("wav")) {
            return getAudioFileIntent(filePath);
        } else if (end.equals("3gp") || end.equals("mp4")) {
            return getAudioFileIntent(filePath);
        } else if (end.equals("jpg") || end.equals("gif") || end.equals("png") || end.equals("jpeg") || end.equals("bmp")) {
            return getImageFileIntent(filePath);
        } else if (end.equals("apk")) {
            return getApkFileIntent(filePath);
        } else if (end.equals("ppt")) {
            return getPPTFileIntent(filePath);
        } else if (end.equals("xls")) {
            return getExcelFileIntent(filePath);
        } else if (end.equals("doc")) {
            return getWordFileIntent(filePath);
        } else if (end.equals("pdf")) {
            return getPDFFileIntent(filePath);
        } else if (end.equals("chm")) {
            return getCHMFileIntent(filePath);
        } else if (end.equals("txt")) {
            return getTextFileIntent(filePath);
        } else {
            return getAllIntent(filePath);
        }
    }

    /**
     * 获取一个用于打开所有文件的intent
     */
    public static Intent getAllIntent(String param) {
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(Intent.ACTION_VIEW);
        Uri uri = Uri.fromFile(new File(param));
        intent.setDataAndType(uri, "*/*");
        return intent;
    }

    /**
     * 获取一个用于打开APK文件的intent
     */
    public static Intent getApkFileIntent(String param) {
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(Intent.ACTION_VIEW);
        Uri uri = Uri.fromFile(new File(param));
        intent.setDataAndType(uri, "application/vnd.android.package-archive");
        return intent;
    }

    /**
     * 获取一个用于打开VIDEO文件的intent
     */
    public static Intent getVideoFileIntent(String param) {
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("oneshot", 0);
        intent.putExtra("configchange", 0);
        Uri uri = Uri.fromFile(new File(param));
        intent.setDataAndType(uri, "video/*");
        return intent;
    }

    /**
     * 获取一个用于打开AUDIO文件的intent
     */
    public static Intent getAudioFileIntent(String param) {
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("oneshot", 0);
        intent.putExtra("configchange", 0);
        Uri uri = Uri.fromFile(new File(param));
        intent.setDataAndType(uri, "audio/*");
        return intent;
    }

    /**
     * 获取一个用于打开Html文件的intent
     */
    public static Intent getHtmlFileIntent(String param) {
        Uri    uri    = Uri.parse(param).buildUpon().encodedAuthority("com.android.htmlfileprovider").scheme("content").encodedPath(param).build();
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.setDataAndType(uri, "text/html");
        return intent;
    }

    /**
     * 获取一个用于打开图片文件的intent
     */
    public static Intent getImageFileIntent(String param) {
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Uri uri = Uri.fromFile(new File(param));
        intent.setDataAndType(uri, "image/*");
        return intent;
    }

    /**
     * 获取一个用于打开PPT文件的intent
     */
    public static Intent getPPTFileIntent(String param) {
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Uri uri = Uri.fromFile(new File(param));
        intent.setDataAndType(uri, "application/vnd.ms-powerpoint");
        return intent;
    }

    /**
     * 获取一个用于打开Excel文件的intent
     */
    public static Intent getExcelFileIntent(String param) {
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Uri uri = Uri.fromFile(new File(param));
        intent.setDataAndType(uri, "application/vnd.ms-excel");
        return intent;
    }

    /**
     * 获取一个用于打开Word文件的intent
     */
    public static Intent getWordFileIntent(String param) {
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Uri uri = Uri.fromFile(new File(param));
        intent.setDataAndType(uri, "application/msword");
        return intent;
    }

    /**
     * 获取一个用于打开CHM文件的intent
     */
    public static Intent getCHMFileIntent(String param) {
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Uri uri = Uri.fromFile(new File(param));
        intent.setDataAndType(uri, "application/x-chm");
        return intent;
    }

    /**
     * 获取一个用于打开文本文件的intent
     */
    public static Intent getTextFileIntent(String param) {
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Uri uri = Uri.fromFile(new File(param));
        intent.setDataAndType(uri, "text/plain");
        return intent;
    }

    /**
     * 获取一个用于打开PDF文件的intent
     */
    public static Intent getPDFFileIntent(String param) {
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Uri uri = Uri.fromFile(new File(param));
        intent.setDataAndType(uri, "application/pdf");
        return intent;
    }
}