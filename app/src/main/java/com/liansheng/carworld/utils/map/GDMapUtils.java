package com.liansheng.carworld.utils.map;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.util.Log;

import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.qmuiteam.qmui.skin.QMUISkinManager;
import com.qmuiteam.qmui.widget.dialog.QMUIBottomSheet;

import java.util.ArrayList;
import java.util.List;

import cn.droidlover.xdroid.tools.utils.ContextHolder;
import cn.droidlover.xdroid.tools.utils.Util;

public class GDMapUtils {

    private AMapLocationClientOption mLocationOption;
    private AMapLocationClient mLocationClient;


    public void location(LocationListener listener) {
        location(listener, true);
    }


    public void location(LocationListener listener, boolean isOnce) {
        if (mLocationClient == null) {
            //初始化定位
            mLocationClient = new AMapLocationClient(ContextHolder.getContext());
        }
        //设置定位回调监听
        mLocationClient.setLocationListener(aMapLocation -> {
            if (aMapLocation != null) {
                if (aMapLocation.getErrorCode() == 0) {
                    Log.e("位置：", aMapLocation.getAddress() + aMapLocation.getLatitude());
                    listener.success(aMapLocation);
//                    showToast(code);
                } else {
                    //定位失败时，可通过ErrCode（错误码）信息来确定失败的原因，errInfo是错误信息，详见错误码表。
                    Log.e("AmapError", "location Error, ErrCode:" + aMapLocation.getErrorCode() + ", errInfo:" + aMapLocation.getErrorInfo());
                    Util.toast("请打开定位权限");
                }
            }
        });
        if (mLocationOption == null) {
            //初始化AMapLocationClientOption对象
            mLocationOption = new AMapLocationClientOption();
        }
        //设置定位模式为AMapLocationMode.Hight_Accuracy，高精度模式。
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        //获取一次定位结果：
        if(isOnce){
            //该方法默认为false。
            mLocationOption.setOnceLocation(true);
        }else {
            mLocationOption.setInterval(10000);
        }
        //获取最近3s内精度最高的一次定位结果：
        //设置setOnceLocationLatest(boolean b)接口为true，启动定位时SDK会返回最近3s内精度最高的一次定位结果。如果设置其为true，setOnceLocation(boolean b)接口也会被设置为true，反之不会，默认为false。
        mLocationOption.setOnceLocationLatest(true);
        //设置是否返回地址信息（默认返回地址信息）
        mLocationOption.setNeedAddress(true);
        //设置是否允许模拟位置,默认为false，不允许模拟位置
        mLocationOption.setMockEnable(true);
        //关闭缓存机制
        mLocationOption.setLocationCacheEnable(false);
        //给定位客户端对象设置定位参数
        mLocationClient.setLocationOption(mLocationOption);
        //启动定位
        mLocationClient.startLocation();
    }


    public void showNav(Context context,String lon, String lat, String name) {
        List<String> hasMap = hasMap(context);
        List<String> mapList = new ArrayList<>();
        for (int i = 0; i < hasMap.size(); i++) {
            if (hasMap.get(i).contains("com.autonavi.minimap")) {
                mapList.add("高德地图");
            } else if (hasMap.get(i).contains("com.baidu.BaiduMap")) {
                mapList.add("百度地图");
            } else if (hasMap.get(i).contains("com.tencent.map")) {
                mapList.add("腾讯地图");
            }
        }
        if (hasMap.size() == 0) {
            Util.toast("您还没安装地图软件");
            return;
        }
        QMUIBottomSheet.BottomListSheetBuilder builder = new QMUIBottomSheet.BottomListSheetBuilder(context);
        builder.setGravityCenter(true)
                .setSkinManager(QMUISkinManager.defaultInstance(context))
                .setTitle("请选择导航软件")
                .setAddCancelBtn(true)
                .setAllowDrag(true)
                .setNeedRightMark(false)
                .setOnSheetItemClickListener((dialog, itemView, position, tag) -> {
                    dialog.dismiss();
                    if (tag.equals("高德地图")) {
                        Intent intent = new Intent("android.intent.action.VIEW", android.net.Uri.parse("androidamap://route?sourceApplication=appName&slat=&slon=&sname=我的位置&dlat=" + lat + "&dlon=" + lon + "&dname=" + name + "&dev=0&t=2"));
                        context.startActivity(intent);
                    } else if (tag.equals("腾讯地图")) {
                        Intent intent = new Intent("android.intent.action.VIEW", android.net.Uri.parse("qqmap://map/routeplan?type=drive&from=&fromcoord=&to=" + name + "&tocoord=" + lat + "," + lon + "&policy=0&referer=appName"));
                        context.startActivity(intent);
                    } else if (tag.equals("百度地图")) {
                        Intent intent = new Intent("android.intent.action.VIEW", android.net.Uri.parse("baidumap://map/geocoder?location=" + lat + "," + lon));
                        context.startActivity(intent);
                    }
                });
        for (int i = 0; i < mapList.size(); i++) {
//            if(withIcon){
//                builder.addItem(ContextCompat.getDrawable(getContext(), R.mipmap.icon_tabbar_lab), "Item " + i);
//            }else{
            builder.addItem(mapList.get(i).toString());
//            }
        }
        builder.build().show();
    }

    /**
     * 指定地图
     * 百度地图包名：com.baidu.BaiduMap
     * 高德地图包名：com.autonavi.minimap
     * 腾讯地图包名：com.tencent.map
     * 谷歌地图 com.google.android.apps.maps
     */
    // 检索筛选后返回
    public List<String> hasMap(Context context) {
        List<String> mapsList = new ArrayList<>();
        mapsList.add("com.baidu.BaiduMap");
        mapsList.add("com.autonavi.minimap");
        mapsList.add("com.tencent.map");
        List<String> backList = new ArrayList<>();
        for (int i = 0; i < mapsList.size(); i++) {
            boolean avilible = isAvilible(context, mapsList.get(i));
            if (avilible) {
                backList.add(mapsList.get(i));
            }
        }
        return backList;
    }

    // 检索地图软件
    public static boolean isAvilible(Context context, String packageName) {
        final PackageManager packageManager = context.getPackageManager();
        List<PackageInfo> packageInfos = packageManager.getInstalledPackages(0);
        List<String> packageNames = new ArrayList<>();
        if (packageInfos != null) {
            for (int i = 0; i < packageInfos.size(); i++) {
                String packName = packageInfos.get(i).packageName;
                packageNames.add(packName);
            }
        }
        return packageNames.contains(packageName);
    }
}
