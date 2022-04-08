package com.liansheng.carworld.activity.home;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.amap.api.services.core.PoiItem;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.Gson;
import com.liansheng.carworld.R;
import com.liansheng.carworld.activity.BaseActivity;
import com.liansheng.carworld.activity.ScanIdActivity;
import com.liansheng.carworld.activity.logic.OtherLogic;
import com.liansheng.carworld.bean.UserInfo;
import com.liansheng.carworld.bean.me.AddressBean;
import com.liansheng.carworld.bean.me.CompanyBean;
import com.liansheng.carworld.bean.me.LicensesBean;
import com.liansheng.carworld.bean.me.ReqAuthBean;
import com.liansheng.carworld.bean.me.ReqBean;
import com.liansheng.carworld.kit.Constant;
import com.liansheng.carworld.net.JsonCallback;
import com.liansheng.carworld.net.NetApi;
import com.liansheng.carworld.net.UrlKit;
import com.liansheng.carworld.oss.OssServiceUtil;
import com.liansheng.carworld.utils.Utils;
import com.liansheng.carworld.view.GlideRoundTransform;
import com.liansheng.carworld.view.SuccessDialog;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.entity.LocalMedia;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.droidlover.xdroid.tools.utils.ActivityManage;
import cn.droidlover.xdroid.tools.utils.Util;
import cn.droidlover.xdroid.views.appbar.ToolBar;
import okhttp3.Call;

public class AuthDispatchAct extends BaseActivity {

    @BindView(R.id.toolbar)
    ToolBar toolBar;
    @BindView(R.id.iv_sfz_zm)
    ImageView ivSfzZm;
    @BindView(R.id.iv_sfz_fm)
    ImageView ivSfzFm;
    @BindView(R.id.iv_sfz_zm_zt)
    ImageView ivSfzZmZt;
    @BindView(R.id.iv_sfz_fm_zt)
    ImageView ivSfzFmZt;
    @BindView(R.id.iv_scan)
    ImageView iv_Scan;
    @BindView(R.id.et_personal_name)
    EditText et_personal_name;
    @BindView(R.id.et_personal_card)
    EditText et_personal_card;
    @BindView(R.id.et_name)
    EditText etName;
    @BindView(R.id.llyt_name)
    LinearLayout llytName;
    @BindView(R.id.tv_address)
    TextView tvAddress;
    @BindView(R.id.iv_yyzz)
    ImageView ivYyzz;
    @BindView(R.id.iv_yyzz_zt)
    ImageView ivYyzzZt;
    @BindView(R.id.iv_mt)
    ImageView ivMt;
    @BindView(R.id.iv_mt_zt)
    ImageView ivMtZt;
    @BindView(R.id.tv_yyzz_bh)
    TextView tvYyzzBh;

    private int SCAN_ID = 1001;
    public int type = 1;//图片位置
    public String sfzZm = "";
    public String sfzFm = "";
    public String yyzz = "";
    private com.amap.api.services.core.PoiItem poiItemStart;
    private boolean isAuth;//是否存在
    private boolean isCompany = true;//是否重新提交
    private List<CompanyBean> companyBeans;
    private List<LicensesBean> licensesBeans;

    private List<ReqAuthBean> authBeans = new ArrayList<>();

    private SuccessDialog dialog;

    @Override
    public void initData(Bundle savedInstanceState) {
        toolBar.setTitle("调度认证");
        ivMt.setVisibility(View.GONE);
        initView();
        getUserInfo();
    }

    private void initView() {
        dialog = new SuccessDialog(context);
        dialog.setMessage("您已提交认证，请稍等\n客服会尽快审核")
                .setOnDismissListener(dialog1 -> finish());
    }


    private void getUserInfo() {
        NetApi.get(UrlKit.USER_INFORM, null, new JsonCallback() {

            @Override
            public void onSuccess(String response, int id) {
                UserInfo userInfo = new Gson().fromJson(response, UserInfo.class);
//                companyBeans = getIntent().getParcelableArrayListExtra("company");
//                licensesBeans = getIntent().getParcelableArrayListExtra("licenses");
                companyBeans = userInfo.getCompanys();
                licensesBeans = userInfo.getLicenses();
                if (companyBeans != null && companyBeans.size() > 0) {
                    for (int i = 0; i < companyBeans.size(); i++) {
                        CompanyBean bean = companyBeans.get(i);
                        if (Constant.STORE_TYPE_3.equals(bean.getType())) {
                            findViewById(R.id.et_company_hint).setVisibility(View.GONE);
                            Glide.with(context).load(UrlKit.getImgUrl(bean.getBusinessLicense())).transform(new GlideRoundTransform()).into(ivYyzz);
                            Glide.with(context).load(UrlKit.getImgUrl(bean.getFrontDoorPhoto())).transform(new GlideRoundTransform()).into(ivMt);
                            etName.setText(bean.getName());
                            tvAddress.setText(bean.getAddress().getAddress());
                            ivYyzzZt.setVisibility(View.VISIBLE);
                            if (Constant.ID_STATUS_REJECT.equals(bean.getStatus())) {
                                etName.setEnabled(true);
                                tvAddress.setEnabled(true);
                                ivYyzz.setEnabled(true);
                                isCompany = true;
                                ivYyzzZt.setImageResource(R.mipmap.ic_auth_bh);
                                tvYyzzBh.setVisibility(View.VISIBLE);
                                tvYyzzBh.setText(bean.getNote());
                            } else {
                                etName.setEnabled(false);
                                tvAddress.setEnabled(false);
                                ivYyzz.setEnabled(false);
                                isCompany = false;
                                if (Constant.ID_STATUS_PASSED.equals(bean.getStatus())) {
                                    ivYyzzZt.setImageResource(R.mipmap.ic_auth_yrz);
                                } else {
                                    ivYyzzZt.setImageResource(R.mipmap.ic_auth_shz);
                                }
                            }
                            break;
                        }
                    }
                }
                if (licensesBeans != null && licensesBeans.size() > 0) {
                    for (int i = 0; i < licensesBeans.size(); i++) {
                        LicensesBean bean = licensesBeans.get(i);
                        if (Constant.AUTH_IDENTITY_CARD.equals(bean.getType())) {
                            isAuth = true;
                            Glide.with(context).load(UrlKit.getImgUrl(bean.getPositive())).transform(new GlideRoundTransform()).into(ivSfzZm);
                            Glide.with(context).load(UrlKit.getImgUrl(bean.getBack())).transform(new GlideRoundTransform()).into(ivSfzFm);
                            et_personal_name.setText(bean.getOwner());
                            et_personal_card.setText(bean.getNum());
                            ivSfzFm.setEnabled(false);
                            ivSfzZm.setEnabled(false);
                            et_personal_name.setEnabled(false);
                            et_personal_card.setEnabled(false);
                            iv_Scan.setVisibility(View.GONE);
                            ivSfzZmZt.setVisibility(View.VISIBLE);
                            ivSfzFmZt.setVisibility(View.VISIBLE);
                            if (Constant.ID_STATUS_PASSED.equals(bean.getStatus())) {
                                ivSfzZmZt.setImageResource(R.mipmap.ic_auth_yrz);
                                ivSfzFmZt.setImageResource(R.mipmap.ic_auth_yrz);
                            } else if (Constant.ID_STATUS_VERIFYING.equals(bean.getStatus())) {
                                ivSfzZmZt.setImageResource(R.mipmap.ic_auth_shz);
                                ivSfzFmZt.setImageResource(R.mipmap.ic_auth_shz);
                            } else {
                                ivSfzZmZt.setImageResource(R.mipmap.ic_auth_bh);
                                ivSfzFmZt.setImageResource(R.mipmap.ic_auth_bh);
                            }
                            break;
                        }
                    }
                } else {
                    isAuth = false;
                }
            }
        });

    }

    @Override
    public int getLayoutId() {
        return R.layout.act_auth_qm;
    }

    @OnClick({R.id.iv_scan, R.id.iv_sfz_zm, R.id.iv_sfz_fm, R.id.iv_yyzz, R.id.tv_address, R.id.submit})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_scan:
                startActivityForResult(new Intent(context, ScanIdActivity.class), SCAN_ID);
                break;
            case R.id.iv_sfz_zm:
                type = 1;//图片位置
                Utils.getPhoto(this);
                break;
            case R.id.iv_sfz_fm:
                type = 2;//图片位置
                Utils.getPhoto(this);
                break;
            case R.id.iv_yyzz:
                type = 3;//图片位置
                Utils.getPhoto(this);
                break;
            case R.id.tv_address:
                Intent intent = new Intent();
                intent.putExtra("type", "start");
                ActivityManage.push(ChooseAreaActivity.class, intent, 1000);
                break;
            case R.id.submit:
                String name = etName.getText().toString();
                String address = tvAddress.getText().toString();
                String personalName = et_personal_name.getText().toString();
                String personalCard = et_personal_card.getText().toString();
                if (!isAuth) {
                    if (TextUtils.isEmpty(personalName)) {
                        Util.toast(et_personal_name.getHint().toString());
                        return;
                    }
                    if (personalName.length() < 2) {
                        Util.toast("名字不正确");
                        return;
                    }
                    if (TextUtils.isEmpty(personalCard)) {
                        Util.toast(et_personal_card.getHint().toString());
                        return;
                    }
                    if (personalCard.length() < 15) {
                        Util.toast("身份证不正确");
                        return;
                    }
                    if (TextUtils.isEmpty(sfzZm)) {
                        Util.toast("请拍摄身份证正面");
                        return;
                    }
                    if (TextUtils.isEmpty(sfzFm)) {
                        Util.toast("请拍摄身份证反面");
                        return;
                    }
                    if (isCompany) {
                        if (TextUtils.isEmpty(yyzz)) {
                            Util.toast("请选择营业执照正面");
                            return;
                        }
                        if (TextUtils.isEmpty(name)) {
                            Utils.showToast(etName.getHint().toString());
                            return;
                        }
                        if (name.length() < 7) {
                            Util.toast("企业名称不正确");
                            return;
                        }
                        if (poiItemStart == null) {
                            Util.toast("请选择公司地址");
                            return;
                        }
                    }
                    check();
                }
                if (isCompany) {
                    if (TextUtils.isEmpty(yyzz)) {
                        Util.toast("请选择营业执照正面");
                        return;
                    }
                    if (TextUtils.isEmpty(name)) {
                        Utils.showToast(etName.getHint().toString());
                        return;
                    }
                    if (name.length() < 7) {
                        Util.toast("企业名称不正确");
                        return;
                    }
                    if (poiItemStart == null) {
                        Util.toast("请选择公司地址");
                        return;
                    }
                    authQm(name);
                }
                break;
        }
    }

    public void check() {
        if (sfzZm.equals("")) {
            showToast("请选择身份证正面照片");
            return;
        }
        if (sfzFm.equals("")) {
            showToast("请选择身份证反面照片");
            return;
        }
        String idCard = et_personal_card.getText().toString();
        if (idCard.equals("")) {
            showToast("请填写身份证号码");
            return;
        }
        if (idCard.length() < 15) {
            Util.toast("身份证格式不正确");
            return;
        }
        String idName = et_personal_name.getText().toString();
        if (idName.equals("")) {
            showToast("请填写身份证姓名");
            return;
        }
        authBeans.add(new ReqAuthBean(Constant.AUTH_IDENTITY_CARD, sfzZm, sfzFm, idName, idCard, getCardId(Constant.AUTH_IDENTITY_CARD)));
        ReqBean reqBean = new ReqBean();
        reqBean.setItems(authBeans);
        NetApi.put(UrlKit.USER_LICENSES, new Gson().toJson(reqBean), new JsonCallback() {

            @Override
            public void onSuccess(String response, int id) {
                if (!dialog.isShowing()) {
                    dialog.show();
                }
            }
        });
    }

    private String getCardId(String type) {
        if (licensesBeans != null && licensesBeans.size() > 0) {
            for (int i = 0; i < licensesBeans.size(); i++) {
                LicensesBean bean = licensesBeans.get(i);
                if (type.equals(bean.getType())) {
                    return bean.getId();
                }
            }
        }
        return "0";
    }

    private long getCompanyId() {
        if (companyBeans != null && companyBeans.size() > 0) {
            for (int i = 0; i < companyBeans.size(); i++) {
                CompanyBean bean = companyBeans.get(i);
                if (Constant.STORE_TYPE_3.equals(bean.getType())) {
                    return bean.getId();
                }
            }
        }
        return 0;
    }

    private String getCompanyBean(String companyName) {
        ReqBean reqBean = new ReqBean();
        List<CompanyBean> list = new ArrayList<>();
        CompanyBean companyBean = new CompanyBean();
        AddressBean addressBean = new AddressBean();
        addressBean.setLongitude(String.valueOf(poiItemStart.getLatLonPoint().getLongitude()));
        addressBean.setLatitude(String.valueOf(poiItemStart.getLatLonPoint().getLatitude()));
        addressBean.setProvince(poiItemStart.getProvinceName());
        addressBean.setCity(poiItemStart.getCityName());
        addressBean.setCounty(poiItemStart.getAdName());
        addressBean.setStreet(poiItemStart.getSnippet());
        addressBean.setName(poiItemStart.getTitle());
        companyBean.setAddress(addressBean);
        companyBean.setName(companyName);
        companyBean.setType(Constant.STORE_TYPE_3);
        companyBean.setId(getCompanyId());
        companyBean.setBusinessLicense(yyzz);
        list.add(companyBean);
        reqBean.setItems(list);
        return new Gson().toJson(reqBean);
    }

    private void authQm(String name) {
        NetApi.put(UrlKit.USER_COMPANYS, getCompanyBean(name), new JsonCallback() {

            @Override
            public void onSuccess(String response, int id) {
                if (!dialog.isShowing()) {
                    dialog.show();
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == PictureConfig.CHOOSE_REQUEST) {
                String path = PictureSelector.obtainMultipleResult(data).get(0).getCompressPath();
                OssServiceUtil.getInstance().asyncPutImage(OtherLogic.getImgUrl(), path, (objectResult, currentPath) -> {
                    RequestOptions myOptions = new RequestOptions().fitCenter().error(R.mipmap.header);
                    if (type == 1) {
                        sfzZm = currentPath;
                        Glide.with(context).load(UrlKit.getImgUrl(currentPath)).apply(myOptions).into(ivSfzZm);
                    } else if (type == 2) {
                        sfzFm = currentPath;
                        Glide.with(context).load(UrlKit.getImgUrl(currentPath)).apply(myOptions).into(ivSfzFm);
                    } else if (type == 3) {
                        yyzz = currentPath;
                        Glide.with(context).load(UrlKit.getImgUrl(currentPath)).apply(myOptions).into(ivYyzz);
                    }
                });
            } else if (requestCode == SCAN_ID) {
                et_personal_name.setText(data.getStringExtra("name"));
                et_personal_card.setText(data.getStringExtra("id"));
            } else if (requestCode == 1000 && data.getParcelableExtra("start") != null) {
                poiItemStart = (PoiItem) data.getParcelableExtra("start");
                tvAddress.setText(poiItemStart.getProvinceName() + poiItemStart.getCityName() + poiItemStart.getAdName() + poiItemStart.getTitle());
            }
        }

    }

}
