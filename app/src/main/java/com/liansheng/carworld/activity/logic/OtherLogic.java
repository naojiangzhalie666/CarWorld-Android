package com.liansheng.carworld.activity.logic;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.kongzue.dialog.interfaces.OnDialogButtonClickListener;
import com.kongzue.dialog.util.BaseDialog;
import com.kongzue.dialog.v3.MessageDialog;
import com.liansheng.carworld.R;
import com.liansheng.carworld.activity.home.FindCarDetailsAct;
import com.liansheng.carworld.activity.home.SaleCarDetailsAct;
import com.liansheng.carworld.bean.UserInfo;
import com.liansheng.carworld.bean.other.PageBean;
import com.liansheng.carworld.kit.Constant;
import com.liansheng.carworld.net.UrlKit;
import com.liansheng.carworld.utils.DateParserHelper;
import com.liansheng.carworld.wxapi.WxUtils;
import com.qmuiteam.qmui.skin.QMUISkinManager;
import com.qmuiteam.qmui.widget.dialog.QMUIBottomSheet;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import cn.droidlover.xdroid.hold.info.SharedInfo;
import cn.droidlover.xdroid.tools.utils.ActivityManage;
import cn.droidlover.xdroid.tools.utils.Util;
import cn.droidlover.xrecyclerview.XRecyclerContentLayout;
import pub.devrel.easypermissions.EasyPermissions;

public class OtherLogic {

    //更新列表
    public static void updateAdapter(int page, BaseQuickAdapter mAdapter, PageBean<?> data, XRecyclerContentLayout contentLayout) {
        updateAdapter(page,0,mAdapter,data,contentLayout);
    }

    public static void updateAdapter(int page, int pageSize, BaseQuickAdapter mAdapter, PageBean<?> data, XRecyclerContentLayout contentLayout) {
        if (data != null && data.getItems() != null) {
            if (page == 0) {
                mAdapter.getData().clear();
            }
            if (data.getItems().size() > 0) {
                mAdapter.addData(data.getItems());
            } else {
                if (page == 0) {
                    contentLayout.showEmpty();
                    return;
                }
            }
            int pageCount;
            if (pageSize > 0) {
                if (data.getItems().size() < pageSize) {
                    pageCount = page;
                } else {
                    pageCount = data.getPageCount();
                }
            } else {
                if (data.getItems().size() < Constant.DEFULT_RESULT) {
                    pageCount = page;
                } else {
                    pageCount = data.getPageCount();
                }
            }
            contentLayout.getRecyclerView().setPage(page, pageCount);
        }
    }

    //拨打电话
    public static void callPhone(AppCompatActivity context, String text, String tel) {
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
                        toCall(context, tel);
                        return false;
                    }
                })
                .show();
    }

    public static void callPhone(AppCompatActivity context, String title, String text, String tel) {
        MessageDialog.build(context)
                .setTitle(title)
                .setMessage(text)
                .setCancelButton("取消", new OnDialogButtonClickListener() {
                    @Override
                    public boolean onClick(BaseDialog baseDialog, View v) {
                        return false;
                    }
                })
//                        .setOtherButtonDrawable(R.drawable.shape_button_red_bg)
                .setOtherButton("联系客服", (baseDialog, v) -> {
                    toCall(context, Constant.SERVICE_PHONE);
                    return false;
                })
                .setOkButton("联系买家", new OnDialogButtonClickListener() {
                    @Override
                    public boolean onClick(BaseDialog baseDialog, View v) {
                        toCall(context, tel);
                        return false;
                    }
                })
                .setOkButtonDrawable(R.drawable.shape_button_red_bg)
                .show();
    }

    public static void toCall(Activity activity, String mobile) {
        if (EasyPermissions.hasPermissions(activity, Manifest.permission.CALL_PHONE)) {
            Intent intent = new Intent(Intent.ACTION_DIAL);
            Uri data = Uri.parse("tel:" + mobile);
            intent.setData(data);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            activity.startActivity(intent);
        } else {
            EasyPermissions.requestPermissions(activity, "我们需要您的允许才能拨打电话，请点击确定打开电话权限.", 1, Manifest.permission.CALL_PHONE);
        }
    }

    public static String carSelect(boolean isBig, String type) {
        if (isBig) {
            return "大板车";
        } else {
            if ("BlueOblique".equals(type)) {
                return "蓝牌斜板";
            } else if ("BlueGround".equals(type)) {
                return "蓝牌落地";
            } else if ("YellowGround".equals(type)) {
                return "黄牌落地";
            } else if ("Five".equals(type)) {
                return "5吨板";
            } else if ("Box".equals(type)) {
                return "厢车";
            }
        }
        return "";
    }

    public static String getImgUrl() {
        long timeMillis = System.currentTimeMillis();
        return "wwwroot/upload/" + DateParserHelper.getDateFormat(timeMillis) + "/" + timeMillis + ".jpg";
    }

    public static void showNav(Context context, String lon, String lat, String name) {
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
                .setOnSheetItemClickListener(new QMUIBottomSheet.BottomListSheetBuilder.OnSheetItemClickListener() {
                    @Override
                    public void onClick(QMUIBottomSheet dialog, View itemView, int position, String tag) {
                        dialog.dismiss();
                        if (tag.equals("高德地图")) {
                            toGaodeNavi(context, lon, lat, name);
                        } else if (tag.equals("腾讯地图")) {
                            toTencent(context, lon, lat, name);
                        } else if (tag.equals("百度地图")) {
                            toBaidu(context, lon, lat);
                        }
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
    public static List<String> hasMap(Context context) {
        List<String> mapsList = Arrays.asList("com.baidu.BaiduMap", "com.autonavi.minimap", "com.tencent.map");
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

    // 百度地图
    public static void toBaidu(Context context, String lon, String lat) {
        Intent intent = new Intent("android.intent.action.VIEW", android.net.Uri.parse("baidumap://map/geocoder?location=" + lat + "," + lon));
        context.startActivity(intent);
    }

    // 高德地图
    public static void toGaodeNavi(Context context, String lon, String lat, String name) {
        Intent intent = new Intent("android.intent.action.VIEW", android.net.Uri.parse("androidamap://route?sourceApplication=appName&slat=&slon=&sname=我的位置&dlat=" + lat + "&dlon=" + lon + "&dname=" + name + "&dev=0&t=2"));
        context.startActivity(intent);
    }

    // 腾讯地图
    public static void toTencent(Context context, String lon, String lat, String name) {
        Intent intent = new Intent("android.intent.action.VIEW", android.net.Uri.parse("qqmap://map/routeplan?type=drive&from=&fromcoord=&to=" + name + "&tocoord=" + lat + "," + lon + "&policy=0&referer=appName"));
        context.startActivity(intent);
    }

    public static void updateAdapterImg(ImageView imageView, String url) {
        if (!TextUtils.isEmpty(url)) {
            Object tag = imageView.getTag();
            if (!url.equals(tag)) {
                imageView.setTag(url);
                Glide.with(imageView).load(UrlKit.getImgUrl(url)).into(imageView);
            }
        }
    }

}
