package com.liansheng.carworld.activity.me;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.liansheng.carworld.R;
import com.liansheng.carworld.activity.BaseActivity;
import com.liansheng.carworld.activity.logic.OtherLogic;
import com.liansheng.carworld.bean.UserCarBean;
import com.liansheng.carworld.bean.UserInfo;
import com.liansheng.carworld.bean.me.LicensesBean;
import com.liansheng.carworld.bean.me.ReqAuthBean;
import com.liansheng.carworld.bean.me.ReqBean;
import com.liansheng.carworld.kit.Constant;
import com.liansheng.carworld.net.JsonCallback;
import com.liansheng.carworld.net.NetApi;
import com.liansheng.carworld.net.UrlKit;
import com.liansheng.carworld.oss.OssServiceUtil;
import com.liansheng.carworld.utils.GlideEngine;
import com.liansheng.carworld.view.GlideRoundTransform;
import com.liansheng.carworld.view.SuccessDialog;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.qmuiteam.qmui.skin.QMUISkinManager;
import com.qmuiteam.qmui.util.QMUIDisplayHelper;
import com.qmuiteam.qmui.widget.popup.QMUIPopup;
import com.qmuiteam.qmui.widget.popup.QMUIPopups;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.droidlover.xdroid.tools.utils.Util;
import okhttp3.Call;

public class DriverAuthAct extends BaseActivity {

    @BindView(R.id.et_cph)
    EditText etCph;
    @BindView(R.id.iv_jsz_zm)
    ImageView ivJszZm;
    @BindView(R.id.iv_jsz_zm_zt)
    ImageView ivJszZmZt;
    @BindView(R.id.iv_xsz_zm)
    ImageView ivXszZm;
    @BindView(R.id.iv_xsz_zm_zt)
    ImageView ivXszZmZt;
    @BindView(R.id.iv_xsz_fm)
    ImageView ivXszFm;
    @BindView(R.id.iv_xsz_fm_zt)
    ImageView ivXszFmZt;
    @BindView(R.id.iv_jqx)
    ImageView ivJqx;
    @BindView(R.id.iv_jqx_zt)
    ImageView ivJqxZt;
    @BindView(R.id.iv_syx)
    ImageView ivSyx;
    @BindView(R.id.iv_syx_zt)
    ImageView ivSyxZt;
    @BindView(R.id.iv_hyx)
    ImageView ivHyx;
    @BindView(R.id.iv_hyx_zt)
    ImageView ivHyxZt;
    @BindView(R.id.btn_go)
    Button btnGo;
    @BindView(R.id.tv_jsz_bh)
    TextView tvJszBh;
    @BindView(R.id.tv_xsz_bh)
    TextView tvXszBh;
    @BindView(R.id.tv_cx)
    TextView tvCx;
    @BindView(R.id.tv_jqx_bh)
    TextView tvJqxBh;
    @BindView(R.id.tv_hyx_bh)
    TextView tvHyxBh;
    @BindView(R.id.tv_syx_bh)
    TextView tvSyxBh;

    public int type = 1;//图片位置
    private String jszZm;
    private String xszZm;
    private String xszFm;
    private String jqx;
    private String hyx;
    public String companyName = "";
    public final int SCAN_ID = 1001;
    private List<LicensesBean> licensesBeans;

    private List<ReqAuthBean> authBeans = new ArrayList<>();

//    public String from = "0";//注册用户去认证

    private SuccessDialog dialog;
    private List<String> data = Arrays.asList("大板车", "蓝牌斜板", "蓝牌落地", "黄牌落地", "5吨板", "厢车");
    private QMUIPopup cxPopup;
    private boolean isBigCar = false;
    private String smallCarType = "";
    UserCarBean carBean;

    @Override
    public void initData(Bundle savedInstanceState) {
        dialog = new SuccessDialog(context);
        dialog.setMessage("您已提交认证，请稍等\n客服会尽快审核")
                .setOnDismissListener(dialog1 -> finish());
        initPop();
        getInfo();
    }

    private void getInfo() {
        NetApi.get(UrlKit.USER_INFORM, null, new JsonCallback() {

            @Override
            public void onSuccess(String response, int id) {
                Gson gson = new Gson();
                UserInfo userInfo = gson.fromJson(response, UserInfo.class);
                licensesBeans = userInfo.getLicenses();
                initView();
                if (userInfo.getUserCar() != null) {
                    carBean = userInfo.getUserCar();
                    if (!TextUtils.isEmpty(carBean.getNumber())) {
                        etCph.setText(carBean.getNumber());
                        etCph.setEnabled(false);
                    }
                    tvCx.setEnabled(false);
                    tvCx.setText(OtherLogic.carSelect(userInfo.getUserCar().isBigCar(), userInfo.getUserCar().getTruckRequire()));

                }
            }
        });
    }

    private void initPop() {
        cxPopup = QMUIPopups.listPopup(this,
                QMUIDisplayHelper.dp2px(this, 100),
                QMUIDisplayHelper.dp2px(this, 240),
                new ArrayAdapter(this, R.layout.simple_list_item, data), (adapterView, view, i, l) -> {
                    tvCx.setText(data.get(i));
                    cxPopup.dismiss();
                })
                .animStyle(QMUIPopup.ANIM_GROW_FROM_CENTER)
                .preferredDirection(QMUIPopup.DIRECTION_BOTTOM)
                .shadow(true)
                .offsetYIfTop(QMUIDisplayHelper.dp2px(this, 1))
                .skinManager(QMUISkinManager.defaultInstance(this));
    }

    @Override
    public int getLayoutId() {
        return R.layout.act_driver_auth;
    }

    private void initView() {
        if (licensesBeans != null) {
            for (int i = 0; i < licensesBeans.size(); i++) {
                LicensesBean bean = licensesBeans.get(i);
                if (Constant.AUTH_DRIVERS_LICENSE.equals(bean.getType())) {
                    Glide.with(context).load(UrlKit.getImgUrl(bean.getPositive())).transform(new GlideRoundTransform()).into(ivJszZm);
                    ivJszZmZt.setVisibility(View.VISIBLE);
                    if (Constant.ID_STATUS_PASSED.equals(bean.getStatus())) {
                        ivJszZmZt.setImageResource(R.mipmap.ic_auth_yrz);
                        ivJszZm.setEnabled(false);
                    } else if (Constant.ID_STATUS_VERIFYING.equals(bean.getStatus())) {
                        ivJszZmZt.setImageResource(R.mipmap.ic_auth_shz);
                        ivJszZm.setEnabled(false);
                    } else {
                        ivJszZmZt.setImageResource(R.mipmap.ic_auth_bh);
                        tvJszBh.setVisibility(View.VISIBLE);
                        tvJszBh.setText(bean.getNote());
                    }
                } else if (Constant.AUTH_DRIVING_LICENSE.equals(bean.getType())) {
                    Glide.with(context).load(UrlKit.getImgUrl(bean.getPositive())).transform(new GlideRoundTransform()).into(ivXszZm);
                    Glide.with(context).load(UrlKit.getImgUrl(bean.getBack())).transform(new GlideRoundTransform()).into(ivXszFm);
                    ivXszZmZt.setVisibility(View.VISIBLE);
                    ivXszFmZt.setVisibility(View.VISIBLE);
                    if (Constant.ID_STATUS_PASSED.equals(bean.getStatus())) {
                        ivXszZmZt.setImageResource(R.mipmap.ic_auth_yrz);
                        ivXszFmZt.setImageResource(R.mipmap.ic_auth_yrz);
                        ivXszZm.setEnabled(false);
                        ivXszFm.setEnabled(false);
                    } else if (Constant.ID_STATUS_VERIFYING.equals(bean.getStatus())) {
                        ivXszZmZt.setImageResource(R.mipmap.ic_auth_shz);
                        ivXszFmZt.setImageResource(R.mipmap.ic_auth_shz);
                        ivXszZm.setEnabled(false);
                        ivXszFm.setEnabled(false);
                    } else {
                        ivXszZmZt.setImageResource(R.mipmap.ic_auth_bh);
                        ivXszFmZt.setImageResource(R.mipmap.ic_auth_bh);
                        tvXszBh.setVisibility(View.VISIBLE);
                        tvXszBh.setText(bean.getNote());
                    }
                } else if (Constant.AUTH_COMPULSORY_INSURANCE.equals(bean.getType())) {
                    Glide.with(context).load(UrlKit.getImgUrl(bean.getPositive())).transform(new GlideRoundTransform()).into(ivJqx);
                    ivJqxZt.setVisibility(View.VISIBLE);
                    if (Constant.ID_STATUS_PASSED.equals(bean.getStatus())) {
                        ivJqxZt.setImageResource(R.mipmap.ic_auth_yrz);
                        ivJqx.setEnabled(false);
                    } else if (Constant.ID_STATUS_VERIFYING.equals(bean.getStatus())) {
                        ivJqxZt.setImageResource(R.mipmap.ic_auth_shz);
                        ivJqx.setEnabled(false);
                    } else {
                        ivJqxZt.setImageResource(R.mipmap.ic_auth_bh);
                        tvJqxBh.setVisibility(View.VISIBLE);
                        tvJqxBh.setText(bean.getNote());
                    }
                } else if (Constant.AUTH_CARGO_INSURANCE.equals(bean.getType())) {
                    Glide.with(context).load(UrlKit.getImgUrl(bean.getPositive())).transform(new GlideRoundTransform()).into(ivHyx);
                    ivHyxZt.setVisibility(View.VISIBLE);
                    if (Constant.ID_STATUS_PASSED.equals(bean.getStatus())) {
                        ivHyxZt.setImageResource(R.mipmap.ic_auth_yrz);
                        ivHyx.setEnabled(false);
                    } else if (Constant.ID_STATUS_VERIFYING.equals(bean.getStatus())) {
                        ivHyxZt.setImageResource(R.mipmap.ic_auth_shz);
                        ivHyx.setEnabled(false);
                    } else {
                        ivHyxZt.setImageResource(R.mipmap.ic_auth_bh);
                        tvHyxBh.setVisibility(View.VISIBLE);
                        tvHyxBh.setText(bean.getNote());
                    }
                } else if (Constant.AUTH_COMMERCIAL_INSURANCE.equals(bean.getType())) {
                    Glide.with(context).load(UrlKit.getImgUrl(bean.getPositive())).transform(new GlideRoundTransform()).into(ivSyx);
                    ivSyxZt.setVisibility(View.VISIBLE);
                    if (Constant.ID_STATUS_PASSED.equals(bean.getStatus())) {
                        ivSyxZt.setImageResource(R.mipmap.ic_auth_yrz);
                        ivSyx.setEnabled(false);
                    } else if (Constant.ID_STATUS_VERIFYING.equals(bean.getStatus())) {
                        ivSyxZt.setImageResource(R.mipmap.ic_auth_shz);
                        ivSyx.setEnabled(false);
                    } else {
                        ivSyxZt.setImageResource(R.mipmap.ic_auth_bh);
                        tvSyxBh.setVisibility(View.VISIBLE);
                        tvSyxBh.setText(bean.getNote());
                    }
                }
            }
        }
    }

    public void getPhoto() {
        PictureSelector.create(this)
                .openGallery(PictureMimeType.ofImage())
                .imageEngine(GlideEngine.createGlideEngine())
                .selectionMode(PictureConfig.SINGLE)
                .isSingleDirectReturn(true)
                .isWeChatStyle(true)
                .isCompress(true)
                .minimumCompressSize(256)
                .forResult(PictureConfig.CHOOSE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case PictureConfig.CHOOSE_REQUEST:
                    String path = PictureSelector.obtainMultipleResult(data).get(0).getCompressPath();
                    OssServiceUtil.getInstance().asyncPutImage(OtherLogic.getImgUrl(), path, (objectResult, currentPath) -> {
                        if (type == 21) {
                            jszZm = currentPath;
                            Glide.with(context).load(UrlKit.getImgUrl(currentPath)).into(ivJszZm);
                            addAuthData(Constant.AUTH_DRIVERS_LICENSE, currentPath);
                            ivJszZmZt.setVisibility(View.GONE);
                        } else if (type == 3) {
                            xszZm = currentPath;
                            Glide.with(context).load(UrlKit.getImgUrl(currentPath)).into(ivXszZm);
                            addAuthData(Constant.AUTH_DRIVING_LICENSE, currentPath, 1);
                            ivXszZmZt.setVisibility(View.GONE);
                        } else if (type == 4) {
                            xszFm = currentPath;
                            Glide.with(context).load(UrlKit.getImgUrl(currentPath)).into(ivXszFm);
                            addAuthData(Constant.AUTH_DRIVING_LICENSE, currentPath, 2);
                            ivXszFmZt.setVisibility(View.GONE);
                        } else if (type == 5) {
                            jqx = currentPath;
                            Glide.with(context).load(UrlKit.getImgUrl(currentPath)).into(ivJqx);
                            addAuthData(Constant.AUTH_COMPULSORY_INSURANCE, currentPath);
                            ivJqxZt.setVisibility(View.GONE);
                        } else if (type == 6) {
                            Glide.with(context).load(UrlKit.getImgUrl(currentPath)).into(ivSyx);
                            addAuthData(Constant.AUTH_COMMERCIAL_INSURANCE, currentPath);
                            ivSyxZt.setVisibility(View.GONE);
                        } else if (type == 7) {
                            hyx = currentPath;
                            Glide.with(context).load(UrlKit.getImgUrl(currentPath)).into(ivHyx);
                            addAuthData(Constant.AUTH_CARGO_INSURANCE, currentPath);
                            ivHyxZt.setVisibility(View.GONE);
                        }
                    });
                    break;
            }
        }
    }

    private void addAuthData(String type, String path, int flag) {
        for (int i = 0; i < authBeans.size(); i++) {
            if (authBeans.get(i).getType().equals(type)) {
                if (flag == 1) {
                    authBeans.get(i).setPositive(path);
                } else {
                    authBeans.get(i).setBack(path);
                }
                return;
            }
        }
        authBeans.add(new ReqAuthBean(type, path, getCardId(type)));
    }

    private void addAuthData(String type, String path) {
        addAuthData(type, path, 1);
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

    private void authInfo() {
//        HashMap<String, Object> map = new HashMap<>();
//        map.put("licenses", new Gson().toJson(authBeans));
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

    @OnClick({R.id.iv_xsz_zm, R.id.iv_xsz_fm, R.id.iv_jsz_zm, R.id.iv_jqx, R.id.iv_syx, R.id.iv_hyx, R.id.tv_cx, R.id.btn_go})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_jsz_zm:
                type = 21;
                getPhoto();
                break;
            case R.id.iv_xsz_zm:
                type = 3;
                getPhoto();
                break;
            case R.id.iv_xsz_fm:
                type = 4;
                getPhoto();
                break;
            case R.id.iv_jqx:
                type = 5;
                getPhoto();
                break;
            case R.id.iv_syx:
                type = 6;
                getPhoto();
                break;
            case R.id.iv_hyx:
                type = 7;
                getPhoto();
                break;
            case R.id.tv_cx:
                cxPopup.show(tvCx);
                break;
            case R.id.btn_go:
                if (licensesBeans != null && licensesBeans.size() > 1) {
                    for (int i = 0; i < licensesBeans.size(); i++) {
                        LicensesBean bean = licensesBeans.get(i);
                        if (Constant.ID_STATUS_REJECT.equals(bean.getStatus())) {
                            if (Constant.AUTH_DRIVERS_LICENSE.equals(bean.getType())) {
                                if (TextUtils.isEmpty(jszZm)) {
                                    Util.toast("请上传驾驶证正面");
                                    return;
                                }
                            } else if (Constant.AUTH_DRIVING_LICENSE.equals(bean.getType())) {
                                if (TextUtils.isEmpty(xszZm)) {
                                    Util.toast("请上传行驶证车辆面");
                                    return;
                                }
                                if (TextUtils.isEmpty(xszFm)) {
                                    Util.toast("请上传行驶证文字面");
                                    return;
                                }
                            } else if (Constant.AUTH_COMPULSORY_INSURANCE.equals(bean.getType())) {
                                if (TextUtils.isEmpty(jqx)) {
                                    Util.toast("请上传交强险");
                                    return;
                                }
                            } else if (Constant.AUTH_CARGO_INSURANCE.equals(bean.getType())) {
                                if (TextUtils.isEmpty(hyx)) {
                                    Util.toast("请上传货运险险");
                                    return;
                                }
                            }
                        }
                    }
                } else {
                    if (TextUtils.isEmpty(jszZm)) {
                        Util.toast("请上传驾驶证正面");
                        return;
                    }
                    if (TextUtils.isEmpty(xszZm)) {
                        Util.toast("请上传行驶证车辆面");
                        return;
                    }
                    if (TextUtils.isEmpty(xszFm)) {
                        Util.toast("请上传行驶证文字面");
                        return;
                    }
                    if (TextUtils.isEmpty(jqx)) {
                        Util.toast("请上传交强险");
                        return;
                    }
                    if (TextUtils.isEmpty(hyx)) {
                        Util.toast("请上传货运险");
                        return;
                    }
                }
                String cph = etCph.getText().toString();
                String ck = tvCx.getText().toString();
                if (TextUtils.isEmpty(cph)) {
                    Util.toast(etCph.getHint().toString());
                    return;
                }
                if (TextUtils.isEmpty(ck)) {
                    Util.toast(tvCx.getHint().toString());
                    return;
                }
                if (carBean == null) {
                    submitCar(ck, cph);
                } else {
                    updateCar(carBean.getId(), ck, cph);
                }
                if (authBeans.size() > 0) {
                    authInfo();
                }
                break;
        }
    }

    private void submitCar(String carType, String carNumber) {
        HashMap<String, Object> map = new HashMap<>();
        String smallCarType = "";
        if (data.get(0).equals(carType)) {
            map.put("bigCar", true);
        } else {
            map.put("bigCar", false);
            if (data.get(1).equals(carType)) {
                smallCarType = "BlueOblique";
            } else if (data.get(2).equals(carType)) {
                smallCarType = "BlueGround";
            } else if (data.get(3).equals(carType)) {
                smallCarType = "YellowGround";
            } else if (data.get(4).equals(carType)) {
                smallCarType = "Five";
            } else if (data.get(5).equals(carType)) {
                smallCarType = "Box";
            }
        }
        map.put("truckRequire", smallCarType);
        map.put("number", carNumber);
        NetApi.post(UrlKit.USER_CAR, map, new JsonCallback() {

            @Override
            public void onSuccess(String response, int id) {
            }
        });
    }

    private void updateCar(String id, String carType, String carNumber) {
        HashMap<String, Object> map = new HashMap<>();
        String smallCarType = "";
        if (data.get(0).equals(carType)) {
            map.put("bigCar", true);
        } else {
            map.put("bigCar", false);
            if (data.get(1).equals(carType)) {
                smallCarType = "BlueOblique";
            } else if (data.get(2).equals(carType)) {
                smallCarType = "BlueGround";
            } else if (data.get(3).equals(carType)) {
                smallCarType = "YellowGround";
            } else if (data.get(4).equals(carType)) {
                smallCarType = "Five";
            } else if (data.get(5).equals(carType)) {
                smallCarType = "Box";
            }
        }
        map.put("truckRequire", smallCarType);
        map.put("number", carNumber);
        NetApi.put(UrlKit.USER_CAR + "/" + id, map, new JsonCallback() {

            @Override
            public void onSuccess(String response, int id) {
            }
        });
    }
}
