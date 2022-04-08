package com.liansheng.carworld.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.amap.api.services.core.PoiItem;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.Gson;
import com.liansheng.carworld.R;
import com.liansheng.carworld.activity.home.ChooseAreaActivity;
import com.liansheng.carworld.activity.logic.OtherLogic;
import com.liansheng.carworld.activity.me.CardListAct;
import com.liansheng.carworld.activity.me.SignAuthAct;
import com.liansheng.carworld.bean.UserInfo;
import com.liansheng.carworld.bean.me.AddressBean;
import com.liansheng.carworld.bean.me.LicensesBean;
import com.liansheng.carworld.bean.other.OssBean;
import com.liansheng.carworld.net.JsonCallback;
import com.liansheng.carworld.net.NetApi;
import com.liansheng.carworld.net.UrlKit;
import com.liansheng.carworld.oss.OssServiceUtil;
import com.liansheng.carworld.utils.DateParserHelper;
import com.liansheng.carworld.utils.GlideEngine;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.qmuiteam.qmui.widget.QMUIRadiusImageView;
import com.xuexiang.xui.widget.layout.ExpandableLayout;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.droidlover.xdroid.hold.info.SharedInfo;
import cn.droidlover.xdroid.tools.utils.ActivityManage;
import cn.droidlover.xdroid.views.LeftRightLayout;
import okhttp3.Call;

public class InfoActivity extends BaseActivity {

    @BindView(R.id.btn_back)
    ImageView btnBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.iv_header)
    QMUIRadiusImageView ivHeader;
    @BindView(R.id.llyt_name)
    LeftRightLayout llytName;
    //    @BindView(R.id.tv_wx)
//    TextView tvWx;
    @BindView(R.id.llyt_wx)
    LinearLayout llytWx;
    @BindView(R.id.tv_city)
    TextView tvCity;
    @BindView(R.id.llyt_city)
    LinearLayout llytCity;
    @BindView(R.id.llyt_mobile)
    LeftRightLayout llytMobile;
    @BindView(R.id.llyt_smrz)
    LeftRightLayout llytSmrz;
    @BindView(R.id.llyt_address)
    LeftRightLayout llytAddress;
    public String name = "";
    public String address = "";
    public String lon = "";
    public String lat = "";
    public String province = "";
    public String city = "";
    public String county = "";
    public String stree = "";

    public final static int REQUESTCODE = 2;

    @BindView(R.id.llyt_bc)
    LinearLayout llytBc;
    @BindView(R.id.main)
    LinearLayout main;
    @BindView(R.id.tv_sfz)
    TextView tvSfz;
    @BindView(R.id.llyt_sfz)
    LinearLayout llytSfz;
    @BindView(R.id.tv_jsz)
    TextView tvJsz;
    @BindView(R.id.llyt_jsz)
    LinearLayout llytJsz;
    @BindView(R.id.tv_jqx)
    TextView tvJqx;
    @BindView(R.id.llyt_jqx)
    LinearLayout llytJqx;
    @BindView(R.id.tv_bc)
    TextView tvBc;
    @BindView(R.id.llyt_driver)
    ExpandableLayout llytDriver;
    //    @BindView(R.id.iv_arrow)
//    ImageView ivArrow;
    public int sfzTime = 0;
    public int jszTime = 0;
    public int jqxTime = 0;
    public boolean isSfz = false;
    public boolean isJsz = false;
    public boolean isJqx = false;
    public int isSm = 0;
    UserInfo userInfo;

    @Override
    public void initData(Bundle savedInstanceState) {
        tvTitle.setText("个人信息");
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        initView();
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_info;
    }//布局文件

    private void initView() {
        NetApi.get(UrlKit.USER_INFORM, null, new JsonCallback() {

            @Override
            public void onSuccess(String response, int id) {
                Gson gson = new Gson();
                userInfo = gson.fromJson(response, UserInfo.class);
                if (userInfo.getHeadphoto() != null) {
                    RequestOptions myOptions = new RequestOptions().fitCenter().error(R.mipmap.header);
                    Glide.with(context).load(UrlKit.getImgUrl(userInfo.getHeadphoto())).apply(myOptions).into(ivHeader);
                }
                llytName.setRightText(userInfo.getMobile());
//                    llytSmrz.setRightText("已上传");
//                    llytSmrz.setEnabled(false);
                llytSmrz.setEnabled(true);
                for (int i = 0; i < userInfo.getLicenses().size(); i++) {
                    LicensesBean licensesBean = userInfo.getLicenses().get(i);
                    if ("IdentityCard".equals(licensesBean.getType())) {
                        llytName.setRightText(licensesBean.getOwner());
                        if (licensesBean.getExpiration() != null) {
                            try {
                                setStatus(licensesBean.getExpiration(), tvSfz, 1);
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                        } else {
                            if (!"passed".equals(licensesBean.getStatus())) {
                                tvSfz.setText("认证中");
                            }
                        }
                    } else if ("BusinessLicense".equals(licensesBean.getType())) {
                    } else if ("CompulsoryInsurance".equals(licensesBean.getType())) {
                        if (licensesBean != null) {
                            if (licensesBean.getExpiration() != null) {
                                try {
                                    setStatus(licensesBean.getExpiration(), tvJqx, 3);
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                            } else {
                                if (!"passed".equals(licensesBean.getStatus())) {
                                    tvJqx.setText("认证中");
                                }
                            }
                        }
                    } else if ("DrivingLicense".equals(licensesBean.getType())) {
                        if (licensesBean.getExpiration() != null) {
                            try {
                                setStatus(licensesBean.getExpiration(), tvJsz, 2);
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                        } else {
                            if (!"passed".equals(licensesBean.getStatus())) {
                                tvJsz.setText("认证中");
                            }
                        }
                    }
                }
                if (userInfo.getAddress() != null) {
                    llytAddress.setRightText(userInfo.getAddress().getCity() + userInfo.getAddress().getCounty() + userInfo.getAddress().getName());
                } else {
                    llytAddress.setRightText("未设置");
                }
                llytMobile.setRightText(userInfo.getMobile());
            }
        });
    }

    public void setStatus(String time, TextView tv, int isGo) throws ParseException {
        int days = DateParserHelper.daysBetween(time);
        if (days > 0) {
            isSm++;
            tv.setText("距离到期时间还有" + days + "天");
        } else {
            if (isGo == 1) {
                isSfz = true;
            } else if (isGo == 2) {
                isJsz = true;
            } else if (isGo == 3) {
                isJqx = true;
            }
            tv.setText("已到期，请重新上传");
        }
//        if (isSm == 3) {
//            llytSmrz.setRightText("已认证");
//        } else {
//            llytSmrz.setRightText("您有信息已过期");
//        }
    }

    @OnClick({R.id.iv_show, R.id.iv_header, R.id.llyt_name, R.id.llyt_wx, R.id.llyt_city, R.id.llyt_cards, R.id.llyt_smrz, R.id.llyt_address, R.id.llyt_bc, R.id.llyt_sfz, R.id.llyt_jsz, R.id.llyt_jqx})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_show:
                if (userInfo != null && !TextUtils.isEmpty(userInfo.getHeadphoto())) {
                    List<LocalMedia> imgList = new ArrayList<>();
                    LocalMedia localMedia = new LocalMedia(UrlKit.getImgUrl(userInfo.getHeadphoto()), 0, 0, "");
                    imgList.add(localMedia);
                    PictureSelector.create(this)
                            .themeStyle(R.style.picture_default_style)
                            .isNotPreviewDownload(true)
                            .imageEngine(GlideEngine.createGlideEngine())
                            .openExternalPreview(0, imgList);
                }
                break;
            case R.id.iv_header:
                PictureSelector.create(this)
                        .openGallery(PictureMimeType.ofImage())
                        .imageEngine(GlideEngine.createGlideEngine())
                        .selectionMode(PictureConfig.SINGLE)
                        .isSingleDirectReturn(true)
                        .isWeChatStyle(true)
                        .isCompress(true)
                        .minimumCompressSize(256)
                        .compressQuality(90)
                        .forResult(PictureConfig.CHOOSE_REQUEST);
                break;
            case R.id.llyt_address:
                Intent intent = new Intent();
                intent.putExtra("type", "start");
                ActivityManage.push(ChooseAreaActivity.class, intent, 1000);
                break;
            case R.id.llyt_smrz:
//                if (getLoginResult().get.equals("unAuth")) {
//                    Bundle bundle = new Bundle();
//                    bundle.putString("from","1");//1是已登录用户去认证
//                    toActivity(AuthActivity.class, null);
                ActivityManage.push(SignAuthAct.class);
//                }
//                if (llytDriver.isExpanded()) {
//                    llytDriver.collapse(true);
//                    ivArrow.setSelected(false);
//                } else {
//                    llytDriver.expand(true);
//                    ivArrow.setSelected(true);
//                }
                break;
            case R.id.llyt_cards:
                ActivityManage.push(CardListAct.class);
                break;
            case R.id.llyt_sfz:
                if (isSfz) {
                    String type = "3";
                    Bundle bundle = new Bundle();
                    bundle.putString("type", type);
                    toActivity(AuthUpdateActivity.class, bundle);
                }
                break;
            case R.id.llyt_jsz:
                if (isJsz) {
                    String type = "1";
                    Bundle bundle = new Bundle();
                    bundle.putString("type", type);
                    toActivity(AuthUpdateActivity.class, bundle);
                }
                break;
            case R.id.llyt_jqx:
                if (isJqx) {
                    String type = "2";
                    Bundle bundle = new Bundle();
                    bundle.putString("type", type);
                    toActivity(AuthUpdateActivity.class, bundle);
                }
                break;
            case R.id.llyt_bc:
                toActivity(AuthChooseActivity.class, null);
                break;
        }
    }

    public PoiItem poiItemStart = null;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case PictureConfig.CHOOSE_REQUEST:
                    String path = PictureSelector.obtainMultipleResult(data).get(0).getCompressPath();
                    OssServiceUtil.getInstance().asyncPutImage(OtherLogic.getImgUrl(), path, (objectResult, currentPath) -> {
                        Glide.with(context).load(UrlKit.getImgUrl(currentPath)).into(ivHeader);
                        changeHead(currentPath);
                    });
                    break;
                case 1000:
                    if (data.getParcelableExtra("start") != null) {
                        poiItemStart = (PoiItem) data.getParcelableExtra("start");
                        for (int i = 0; i < getLoginResult().getLicenses().size(); i++) {
                            LicensesBean licensesBean = getLoginResult().getLicenses().get(i);
                            if ("IdentityCard".equals(licensesBean.getType()) && "passed".equals(licensesBean.getStatus())) {
                                llytName.setRightText(licensesBean.getOwner());
                                name = licensesBean.getOwner();
                            }
                        }
                        changeCompany(poiItemStart.getTitle(), poiItemStart.getLatLonPoint().getLongitude() + "", poiItemStart.getLatLonPoint().getLatitude() + "", poiItemStart.getProvinceName(),
                                poiItemStart.getCityName(), poiItemStart.getAdName(), poiItemStart.getSnippet());
                    }
                    break;
            }
        }
    }

    public void changeHead(String headphoto) {
        NetApi.putHeadPhoto(getLoginResult().getToken(), headphoto, new JsonCallback() {

            @Override
            public void onSuccess(String response, int id) {
//                    showToast("修改头像成功");
                showSnackToast(main, "修改头像成功");
                RequestOptions myOptions = new RequestOptions().fitCenter().error(R.mipmap.header);
                Glide.with(context).load(UrlKit.getImgUrl(headphoto)).apply(myOptions).into(ivHeader);
                getLoginResult().setHeadphoto(headphoto);
            }
        });
    }

    public void changeCompany(String address, String log, String lat, String province, String city, String county, String street) {
        NetApi.putCompany(getLoginResult().getToken(), address, log, lat, province, city, county, street, new JsonCallback() {

            @Override
            public void onSuccess(String response, int id) {
                showSnackToast(main, "修改成功");
                if (getLoginResult().getAddress() == null) {
                    AddressBean addressBean = new AddressBean("", "", "", "", "", "", "");
                    UserInfo userInfo = getLoginResult();
                    userInfo.setAddress(addressBean);
                    setLoginResult(userInfo, false);
                    SharedInfo.getInstance().saveEntity(userInfo);
                }
                getLoginResult().getAddress().setName(address);
                getLoginResult().getAddress().setLongitude(log);
                getLoginResult().getAddress().setLatitude(lat);
                getLoginResult().getAddress().setProvince(province);
                getLoginResult().getAddress().setCity(city);
                getLoginResult().getAddress().setCounty(county);
                getLoginResult().getAddress().setStreet(street);
                llytAddress.setRightText(city + county + address);
            }
        });
    }

}