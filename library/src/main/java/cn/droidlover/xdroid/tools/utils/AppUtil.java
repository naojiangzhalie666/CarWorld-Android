package cn.droidlover.xdroid.tools.utils;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;


import androidx.core.app.ActivityCompat;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.UUID;
import java.util.regex.Pattern;

import cn.droidlover.xdroid.tools.log.Logger;

import static android.content.Context.USAGE_STATS_SERVICE;

/**
 * Author: JinF
 * Date: 2018/3/20 10:19
 * <p/>
 * Description: 应用信息
 */
public class AppUtil {
    private static final String TAG = AppUtil.class.getSimpleName();

    private AppUtil() {

    }

    public static final String getUserAgent(Context context) {
        return "(Android " + Build.VERSION.RELEASE + ";" + "UIC/" + getAppVersionName(context) + ")";
    }

    /**
     * 获取应用名字
     *
     * @param context
     * @return
     */
    public static String getAppName(Context context) {
        PackageManager pm = context.getPackageManager();
        String appName = "";
        try {
            ApplicationInfo applicationInfo = pm.getApplicationInfo(getAppPackageName(context), 0);
            appName = String.valueOf(pm.getApplicationLabel(applicationInfo));
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return appName;
    }

    /*********
     * 获取屏幕宽度
     * @param context
     * @return
     */
    public static int getScreenWidth(Context context) {
        DisplayMetrics metric = context.getResources().getDisplayMetrics();
        int width = metric.widthPixels;     // 屏幕宽度（像素）
        return width;
    }


    /******
     * 获取状态栏高度
     * @param context
     * @return
     */
    public static int getStatusBarH(Context context) {
        int statusHeight = -1;
        try {
            Class<?> clazz = Class.forName("com.android.internal.R$dimen");
            Object object = clazz.newInstance();
            int height = Integer.parseInt(clazz.getField("status_bar_height")
                    .get(object).toString());
            statusHeight = context.getResources().getDimensionPixelSize(height);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return statusHeight;
    }

    /***********
     * 获取屏幕高度
     * @param context
     * @return
     */
    public static int getScreenHeight(Context context) {
        DisplayMetrics metric = context.getResources().getDisplayMetrics();
        int height = metric.heightPixels;     // 屏幕高度（像素）
        return height;
    }

    /**
     * 获取应用图标
     *
     * @param context
     * @return
     */
    public static Drawable getAppIcon(Context context) {
        PackageManager pm = context.getPackageManager();
        Drawable appIcon = null;
        try {
            ApplicationInfo applicationInfo = pm.getApplicationInfo(getAppPackageName(context), 0);
            appIcon = applicationInfo.loadIcon(pm);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return appIcon;
    }


    public static long getAppDate(Context context) {
        long lastUpdateTime = 0;
        try {
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(getAppPackageName(context), 0);
            lastUpdateTime = packageInfo.lastUpdateTime;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return lastUpdateTime;
    }


    public static long getAppSize(Context context) {
        long appSize = 0;
        try {
            ApplicationInfo applicationInfo = context.getPackageManager().getApplicationInfo(getAppPackageName(context), 0);
            appSize = new File(applicationInfo.sourceDir).length();
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return appSize;
    }

    public static String getAppApk(Context context) {
        String sourceDir = null;
        try {
            ApplicationInfo applicationInfo = context.getPackageManager().getApplicationInfo(getAppPackageName(context), 0);
            sourceDir = applicationInfo.sourceDir;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return sourceDir;
    }

    /** 应用版本名 （1.0.0） */
    public static String getAppVersionName(Context context) {
        String appVersion = "";
        try {
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(getAppPackageName(context), 0);
            appVersion = packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return appVersion;
    }

    /** 应用版本号 （1） */
    public static int getAppVersionCode(Context context) {
        int appVersionCode = 0;
        try {
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(getAppPackageName(context), 0);
            appVersionCode = packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return appVersionCode;
    }

    /**
     * 获取手机厂商
     *
     * @return 手机厂商
     */
    public static String getDeviceBrand() {
        return Build.BRAND;
    }


    public static String getAppInstaller(Context context) {
        return context.getPackageManager().getInstallerPackageName(getAppPackageName(context));
    }

    public static String getAppPackageName(Context context) {
        return context.getPackageName();
    }

    public static boolean hasPermission(Context context, String permission) {
        if (context != null && !TextUtils.isEmpty(permission)) {
            try {
                PackageManager packageManager = context.getPackageManager();
                if (packageManager != null) {
                    if (PackageManager.PERMISSION_GRANTED == packageManager.checkPermission(permission, context
                            .getPackageName())) {
                        return true;
                    }
                    Logger.d("AppUtils", "Have you  declared permission " + permission + " in AndroidManifest.xml ?");
                }
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }
        return false;
    }

    public static boolean isInstalled(Context context) {
        boolean installed = false;
        String packageName = getAppPackageName(context);
        if (TextUtils.isEmpty(packageName)) {
            return false;
        }
        List<ApplicationInfo> installedApplications = context.getPackageManager().getInstalledApplications(0);
        for (ApplicationInfo in : installedApplications) {
            if (packageName.equals(in.packageName)) {
                installed = true;
                break;
            } else {
                installed = false;
            }
        }
        return installed;
    }

    public static boolean installApk(Context context, String filePath) {
        File file = new File(filePath);
        if (!file.exists() || !file.isFile() || file.length() <= 0) {
            return false;
        }
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setDataAndType(Uri.parse("file://" + filePath), "application/vnd.android.package-archive");
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(i);
        return true;
    }

    public static boolean uninstallApk(Context context) {
        String packageName = getAppPackageName(context);
        if (TextUtils.isEmpty(packageName)) {
            return false;
        }
        Intent i = new Intent(Intent.ACTION_DELETE, Uri.parse("package:" + packageName));
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(i);
        return true;
    }


    public static boolean isSystemApp(Context context) {
        boolean isSys = false;
        PackageManager pm = context.getPackageManager();
        try {
            ApplicationInfo applicationInfo = pm.getApplicationInfo(getAppPackageName(context), 0);
            if (applicationInfo != null && (applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) > 0) {
                isSys = true;
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            isSys = false;
        }
        return isSys;
    }

    /**
     * className "com.xxx.xx..XXXService"
     */
    public static boolean isServiceRunning(Context context, String className) {
        boolean isRunning = false;
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> servicesList = activityManager.getRunningServices(Integer.MAX_VALUE);
        for (ActivityManager.RunningServiceInfo si : servicesList) {
            if (className.equals(si.service.getClassName())) {
                isRunning = true;
            }
        }
        return isRunning;
    }

    public static boolean stopRunningService(Context context, String className) {
        Intent intent_service = null;
        boolean ret = false;
        try {
            intent_service = new Intent(context, Class.forName(className));
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (intent_service != null) {
            ret = context.stopService(intent_service);
        }
        return ret;
    }

    public static int getNumCores() {
        try {
            File dir = new File("/sys/devices/system/cpu/");
            File[] files = dir.listFiles(new FileFilter() {

                @Override
                public boolean accept(File pathname) {
                    return Pattern.matches("cpu[0-9]", pathname.getName());
                }

            });
            return files.length;
        } catch (Exception e) {
            e.printStackTrace();
            return 1;
        }
    }

    public static void killProcesses(Context context, int pid, String processName) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        String packageName;
        try {
            if (!processName.contains(":")) {
                packageName = processName;
            } else {
                packageName = processName.split(":")[0];
            }
            activityManager.killBackgroundProcesses(packageName);
            Method forceStopPackage = activityManager.getClass().getDeclaredMethod("forceStopPackage", String.class);
            forceStopPackage.setAccessible(true);
            forceStopPackage.invoke(activityManager, packageName);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String runScript(String script) {
        String sRet;
        try {
            final Process m_process = Runtime.getRuntime().exec(script);
            final StringBuilder sbread = new StringBuilder();
            Thread tout = new Thread(new Runnable() {
                public void run() {
                    BufferedReader bufferedReader = new BufferedReader(
                            new InputStreamReader(m_process.getInputStream()),
                            8192);
                    String ls_1;
                    try {
                        while ((ls_1 = bufferedReader.readLine()) != null) {
                            sbread.append(ls_1).append("\n");
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    } finally {
                        try {
                            bufferedReader.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
            tout.start();

            final StringBuilder sberr = new StringBuilder();
            Thread terr = new Thread(new Runnable() {
                public void run() {
                    BufferedReader bufferedReader = new BufferedReader(
                            new InputStreamReader(m_process.getErrorStream()),
                            8192);
                    String ls_1;
                    try {
                        while ((ls_1 = bufferedReader.readLine()) != null) {
                            sberr.append(ls_1).append("\n");
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    } finally {
                        try {
                            bufferedReader.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
            terr.start();

            m_process.waitFor();
            while (tout.isAlive()) {
                Thread.sleep(50);
            }
            if (terr.isAlive())
                terr.interrupt();
            String stdout = sbread.toString();
            String stderr = sberr.toString();
            sRet = stdout + stderr;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return sRet;
    }

    public static boolean getRootPermission(Context context) {
        String packageCodePath = context.getPackageCodePath();
        Process process = null;
        DataOutputStream os = null;
        try {
            String cmd = "chmod 777 " + packageCodePath;
            process = Runtime.getRuntime().exec("su");
            os = new DataOutputStream(process.getOutputStream());
            os.writeBytes(cmd + "\n");
            os.writeBytes("exit\n");
            os.flush();
            process.waitFor();
        } catch (Exception e) {
            return false;
        } finally {
            try {
                if (os != null) {
                    os.close();
                }
                if (process != null) {
                    process.destroy();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return true;
    }

    /**
     * 获取设备的唯一id 无需权限
     *
     * @return PsuedoID
     */
    public static String getUniquePsuedoID() {
        String serial;
        String m_szDevIDShort = "35" +
                Build.BOARD.length() % 10 + Build.BRAND.length() % 10 +
                Build.CPU_ABI.length() % 10 + Build.DEVICE.length() % 10 +
                Build.DISPLAY.length() % 10 + Build.HOST.length() % 10 +
                Build.ID.length() % 10 + Build.MANUFACTURER.length() % 10 +
                Build.MODEL.length() % 10 + Build.PRODUCT.length() % 10 +
                Build.TAGS.length() % 10 + Build.TYPE.length() % 10 +
                Build.USER.length() % 10; //13 位

        try {
            serial = Build.class.getField("SERIAL").get(null).toString();
            //API>=9 使用serial号
            return new UUID(m_szDevIDShort.hashCode(), serial.hashCode()).toString();
        } catch (Exception exception) {
            //serial需要一个初始化
            serial = "serial"; // 随便一个初始化
        }
        //使用硬件信息拼凑出来的15位号码
        return new UUID(m_szDevIDShort.hashCode(), serial.hashCode()).toString();
    }

    /**
     * 获取设备的唯一id
     *
     * @return
     */
    @Deprecated //权限隐私申请引起用户反感
    @SuppressLint("HardwareIds")
    public static String getClientId(Context context) {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
            TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            if (tm != null) {
                //少部分厂商的手机可能获取不到
                String imei = "";
                if (!TextUtils.isEmpty(tm.getDeviceId())) {
                    //少部分厂商的手机可能获取不到
                    imei = tm.getDeviceId();
                } else if (!TextUtils.isEmpty(tm.getSimSerialNumber())) {
                    //只针对有sim卡的设备才能获取到该值
                    imei = tm.getSimSerialNumber();
                }
                if (imei.equals("0")) {
                    imei = "000000000000000";
                }
                int len = 15 - imei.length();
                for (int i = 0; i < len; i++) {
                    imei += "0";
                }
                return imei;
            }
        }
        return "";
    }

    /**
     * 检查包是否存在
     *
     * @param packname
     * @return
     */
    public static boolean checkPackInfo(Context context, String packname) {
        PackageInfo packageInfo = null;
        try {
            packageInfo = context.getPackageManager().getPackageInfo(packname, 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return packageInfo != null;
    }

    /**
     * 判断当前应用是否在运行
     *
     * @param context
     * @param packageName
     * @return
     */
    public static boolean isAppRunning(Context context, String packageName) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> list = am.getRunningTasks(100);
        boolean isAppRunning = false;
        for (ActivityManager.RunningTaskInfo info : list) {
            if (info.topActivity.getPackageName().equals(packageName) || info.baseActivity.getPackageName().equals(packageName)) {
                Log.d(TAG, info.topActivity.getPackageName() + " info.baseActivity.getPackageName()=" + info.baseActivity.getPackageName());
                isAppRunning = true;
                break;
            }
        }
        return isAppRunning;
    }

    /**
     * 判断当前程序是否在栈顶
     *
     * @param context
     * @param packageName
     * @return
     */
    public static boolean isTopActivy(Context context, String packageName) {
        String topPackageName = null;
        UsageStatsManager mUsageStatsManager = null;
        try {
            mUsageStatsManager = (UsageStatsManager) context.getSystemService(USAGE_STATS_SERVICE);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (mUsageStatsManager == null) {
            return false;
        }

        long time = System.currentTimeMillis();
        // We get usage stats for the last 10 seconds
        List<UsageStats> stats = mUsageStatsManager.queryUsageStats(UsageStatsManager.INTERVAL_DAILY, time - 1000 * 10, time);
        // Sort the stats by the last time used
        if (stats != null) {
            SortedMap<Long, UsageStats> mySortedMap = new TreeMap<Long, UsageStats>();
            for (UsageStats usageStats : stats) {
                mySortedMap.put(usageStats.getLastTimeUsed(), usageStats);
            }
            if (!mySortedMap.isEmpty()) {
                topPackageName = mySortedMap.get(mySortedMap.lastKey()).getPackageName();
            }
        }

        if (TextUtils.equals(packageName, topPackageName)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 获取IP地址
     */
    public static String getIP(Context context) {
        int WIFI_IP = getWIFIIP(context);
        String GPRS_IP = getGPRSIP();
        String ip = "0.0.0.0";
        if (WIFI_IP != 0) {
            ip = intToIP(WIFI_IP);
        } else if (!TextUtils.isEmpty(GPRS_IP)) {
            ip = GPRS_IP;
        }
        return ip;
    }

    /**
     * 获得wifi的IP地址
     */
    private static int getWIFIIP(Context context) {
        // 获取wifi服务
        WifiManager wifiManager = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        // 判断wifi是否开启
        if (!wifiManager.isWifiEnabled()) {
            wifiManager.setWifiEnabled(true);
        }
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        return wifiInfo.getIpAddress();
    }

    /**
     * 整型IP地址转成String的
     */
    private static String intToIP(int IPAddress) {
        return (IPAddress & 0xFF) + "." + ((IPAddress >> 8) & 0xFF) + "." + ((IPAddress >> 16) & 0xFF) + "." + (IPAddress >> 24 & 0xFF);
    }

    /**
     * 获取数据网络的IP地址
     */
    private static String getGPRSIP() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
                NetworkInterface networkInterface = en.nextElement();
                for (Enumeration<InetAddress> addresses = networkInterface.getInetAddresses(); addresses.hasMoreElements(); ) {
                    InetAddress inetAddress = addresses.nextElement();
                    if (!inetAddress.isLoopbackAddress()) {
                        return inetAddress.getHostAddress();
                    }
                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 获取AndroidManifest.xml中指定的meta-data
     *
     * @param channelKey 渠道的key
     * @return 如果没有获取成功(没有对应值, 或者异常)，则返回值为空
     */
    public static String getAppMetaData(Context context, String channelKey) {
        if (context == null) {
            return "";
        }
        String resultData = "";
        try {
            PackageManager packageManager = context.getPackageManager();
            if (packageManager != null) {
                ApplicationInfo applicationInfo = packageManager.getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
                if (applicationInfo != null) {
                    if (applicationInfo.metaData != null) {
                        if (applicationInfo.metaData.get(channelKey) != null) {
                            resultData = applicationInfo.metaData.get(channelKey).toString();
                        }
                    }
                }
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return resultData;
    }
}