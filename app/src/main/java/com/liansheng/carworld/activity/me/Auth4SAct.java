package com.liansheng.carworld.activity.me;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.amap.api.services.core.PoiItem;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.Gson;
import com.liansheng.carworld.R;
import com.liansheng.carworld.activity.BaseActivity;
import com.liansheng.carworld.activity.home.ChooseAreaActivity;
import com.liansheng.carworld.activity.logic.OtherLogic;
import com.liansheng.carworld.bean.UserInfo;
import com.liansheng.carworld.bean.me.AddressBean;
import com.liansheng.carworld.bean.me.ReqBean;
import com.liansheng.carworld.bean.me.CompanyBean;
import com.liansheng.carworld.bean.me.LicensesBean;
import com.liansheng.carworld.bean.me.ReqAuthBean;
import com.liansheng.carworld.kit.Constant;
import com.liansheng.carworld.net.JsonCallback;
import com.liansheng.carworld.net.NetApi;
import com.liansheng.carworld.net.UrlKit;
import com.liansheng.carworld.oss.OssServiceUtil;
import com.liansheng.carworld.utils.Utils;
import com.liansheng.carworld.view.GlideRoundTransform;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.entity.LocalMedia;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.droidlover.xdroid.tools.utils.ActivityManage;
import cn.droidlover.xdroid.tools.utils.Util;
import cn.droidlover.xdroid.views.textView.NoDoubleClickTextView;
import okhttp3.Call;

public class Auth4SAct extends BaseActivity {

    @BindView(R.id.iv_sfz_zm)
    ImageView ivSfzZm;
    @BindView(R.id.iv_sfz_fm)
    ImageView ivSfzFm;
    @BindView(R.id.iv_sfz_zm_zt)
    ImageView ivSfzZmZt;
    @BindView(R.id.iv_sfz_fm_zt)
    ImageView ivSfzFmZt;
    @BindView(R.id.et_personal_name)
    EditText et_personal_name;
    @BindView(R.id.et_personal_card)
    EditText et_personal_card;
    @BindView(R.id.iv_scan)
    ImageView iv_Scan;
    @BindView(R.id.et_name)
    EditText etName;
    @BindView(R.id.et_brand)
    EditText etBrand;
    @BindView(R.id.tv_address)
    TextView tvAddress;
    @BindView(R.id.submit)
    NoDoubleClickTextView submit;

    public int type = 1;//图片位置
    public String sfzZm = "";
    public String sfzFm = "";
    private PoiItem poiItemStart;
    private boolean isAuth;//是否存在
    private List<CompanyBean> companyBeans;

    @Override
    public void initData(Bundle savedInstanceState) {
        initView();
    }

    @Override
    public int getLayoutId() {
        return R.layout.act_auth_4s;
    }

    private void initView() {
        NetApi.get(UrlKit.USER_INFORM, null, new JsonCallback() {

            @Override
            public void onSuccess(String response, int id) {
                UserInfo userInfo = new Gson().fromJson(response, UserInfo.class);
                companyBeans = userInfo.getCompanys();
                List<LicensesBean> licensesBeans = userInfo.getLicenses();
                if (companyBeans != null && companyBeans.size() > 0) {
                    for (int i = 0; i < companyBeans.size(); i++) {
                        CompanyBean bean = companyBeans.get(i);
                        if (Constant.STORE_TYPE_2.equals(bean.getType())) {
                            findViewById(R.id.et_company_hint).setVisibility(View.GONE);
                            etName.setText(bean.getName());
                            etBrand.setText(bean.getMainBrand());
                            if (bean.getAddress() != null)
                                tvAddress.setText(bean.getAddress().getAddress());
                            if (Constant.ID_STATUS_REJECT.equals(bean.getStatus())) {
                                etName.setEnabled(true);
                                etBrand.setEnabled(true);
                                tvAddress.setEnabled(true);
                                submit.setVisibility(View.VISIBLE);

//                                    isCompany = true;
                            } else {
                                etName.setEnabled(false);
                                etBrand.setEnabled(false);
                                tvAddress.setEnabled(false);
                                submit.setVisibility(View.GONE);
//                                    isCompany = false;
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

    @OnClick({R.id.iv_sfz_zm, R.id.iv_sfz_fm, R.id.tv_address, R.id.submit})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_sfz_zm:
                type = 1;//图片位置
//                Utils.getCamera(this);
                Utils.getPhoto(this);
                break;
            case R.id.iv_sfz_fm:
                type = 2;//图片位置
//                Utils.getCamera(this);
                Utils.getPhoto(this);
                break;
            case R.id.tv_address:
                Intent intent = new Intent();
                intent.putExtra("type", "start");
                ActivityManage.push(ChooseAreaActivity.class, intent, 1000);
                break;
            case R.id.submit:
                String name = etName.getText().toString();
                String brand = etBrand.getText().toString();
//                String twoName = etTwoName.getText().toString();
//                String twoPhone = etTwoPhone.getText().toString();
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
                        Util.toast("身份证格式不正确");
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
                }
                if (TextUtils.isEmpty(name)) {
                    Utils.showToast(etName.getHint().toString());
                    return;
                }
                if (name.length() < 7) {
                    Util.toast("企业名称不正确");
                    return;
                }
                if (TextUtils.isEmpty(brand)) {
                    Utils.showToast(etBrand.getHint().toString());
                    return;
                }
                if (TextUtils.isEmpty(address)) {
                    Utils.showToast("请选择公司地址");
                    return;
                }
//                if (TextUtils.isEmpty(twoName)) {
//                    Utils.showToast(etTwoName.getHint().toString());
//                    return;
//                }
//                if (TextUtils.isEmpty(twoPhone)) {
//                    Utils.showToast(etTwoPhone.getHint().toString());
//                    return;
//                }
                authOther(sfzZm, sfzFm, personalName, personalCard);
                break;
        }
    }

    private void authOther(String zm, String fm, String personalName, String card) {
        if (!isAuth) {
            List<ReqAuthBean> authBeans = new ArrayList<>();
            authBeans.add(new ReqAuthBean(Constant.AUTH_IDENTITY_CARD, zm, fm, personalName, card));
            ReqBean reqBean = new ReqBean();
            reqBean.setItems(authBeans);
            NetApi.put(UrlKit.USER_LICENSES, new Gson().toJson(reqBean), new JsonCallback() {

                @Override
                public void onSuccess(String response, int id) {
                    authQm();
                }
            });
        } else {
            authQm();
        }
    }

    private long getCompanyId() {
        if (companyBeans != null && companyBeans.size() > 0) {
            for (int i = 0; i < companyBeans.size(); i++) {
                CompanyBean bean = companyBeans.get(i);
                if (Constant.STORE_TYPE_2.equals(bean.getType())) {
                    return bean.getId();
                }
            }
        }
        return 0;
    }

    private String getCompanyBean() {
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
        companyBean.setName(etName.getText().toString());
        companyBean.setType(Constant.STORE_TYPE_2);
        if (getCompanyId() != 0) {
            companyBean.setId(getCompanyId());
        }
        companyBean.setMainBrand(etBrand.getText().toString());
        list.add(companyBean);
        reqBean.setItems(list);
        return new Gson().toJson(reqBean);
    }

    private void authQm() {
        if (poiItemStart == null) {
            Util.toast("请定位公司地址");
            return;
        }
        NetApi.put(UrlKit.USER_COMPANYS, getCompanyBean(), new JsonCallback() {

            @Override
            public void onSuccess(String response, int id) {
                Util.toast("提交成功");
                finish();
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
                        Glide.with(context).load(UrlKit.getImgUrl(path)).apply(myOptions).into(ivSfzZm);
                    } else if (type == 2) {
                        sfzFm = currentPath;
                        Glide.with(context).load(UrlKit.getImgUrl(path)).apply(myOptions).into(ivSfzFm);
                    }
                });
            }
            if (data.getParcelableExtra("start") != null) {
                poiItemStart = (PoiItem) data.getParcelableExtra("start");
                tvAddress.setText(poiItemStart.getProvinceName() + poiItemStart.getCityName() + poiItemStart.getAdName() + poiItemStart.getTitle());
            }
        }
    }
}
