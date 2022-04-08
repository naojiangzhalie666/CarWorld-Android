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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.amap.api.services.core.PoiItem;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.liansheng.carworld.R;
import com.liansheng.carworld.activity.BaseActivity;
import com.liansheng.carworld.activity.ScanIdActivity;
import com.liansheng.carworld.activity.home.ChooseAreaActivity;
import com.liansheng.carworld.activity.logic.OtherLogic;
import com.liansheng.carworld.bean.UserCarBean;
import com.liansheng.carworld.bean.UserInfo;
import com.liansheng.carworld.bean.me.AddressBean;
import com.liansheng.carworld.bean.me.ReqBean;
import com.liansheng.carworld.bean.me.CompanyBean;
import com.liansheng.carworld.bean.me.LicensesBean;
import com.liansheng.carworld.bean.me.ReqAuthBean;
import com.liansheng.carworld.bean.other.PageBean;
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
import cn.droidlover.xdroid.hold.info.SharedInfo;
import cn.droidlover.xdroid.tools.utils.ActivityManage;
import cn.droidlover.xdroid.tools.utils.Util;
import okhttp3.Call;

public class AuthActivity extends BaseActivity {

    //    @BindView(R.id.topbar)
//    QMUITopBar topbar;
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
    @BindView(R.id.iv_yyzz)
    ImageView ivYyzz;
    @BindView(R.id.iv_yyzz_zt)
    ImageView ivYyzzZt;
    @BindView(R.id.et_campany)
    EditText etCampany;
    @BindView(R.id.btn_go)
    Button btnGo;
    @BindView(R.id.et_personal_card)
    EditText etId;
    @BindView(R.id.et_personal_name)
    EditText etName;
    @BindView(R.id.tv_address)
    TextView tvAddress;
    @BindView(R.id.tv_sfz_zm_bh)
    TextView tvSfzZmBh;
    @BindView(R.id.tv_sfz_fm_bh)
    TextView tvSfzFmBh;
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
    @BindView(R.id.tv_yyzz_bh)
    TextView tvYyzzBh;
    @BindView(R.id.main)
    LinearLayout main;
    @BindView(R.id.btn_back)
    ImageView btnBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;


    public int type = 1;//图片位置
    public String sfzZm = "";
    public String sfzFm = "";
    //    public String xszFm = "";
    private String jszZm;
    private String xszZm;
    private String xszFm;
    private String jqx;
    public String yyzz = "";
    public String companyName = "";
    public final int SCAN_ID = 1001;
    private boolean isAuth = true;//是否重新提交
    private boolean isCompany = true;//是否重新提交
    private PoiItem poiItemStart;
    private List<CompanyBean> companyBeans;
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
        tvTitle.setText("司机认证");
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        dialog = new SuccessDialog(context);
        dialog.setMessage("您已提交认证，请稍等\n客服会尽快审核")
                .setOnDismissListener(dialog1 -> finish());
        initView();
        initPop();
        getInfo();
    }

    private void getInfo() {
        NetApi.get(UrlKit.USER_INFORM, null, new JsonCallback() {

            @Override
            public void onSuccess(String response, int id) {
                Gson gson = new Gson();
                UserInfo userInfo = gson.fromJson(response, UserInfo.class);
                licensesBeans=userInfo.getLicenses();
                companyBeans=userInfo.getCompanys();
                initView();
                if (userInfo != null && userInfo.getUserCar() != null) {
                    carBean = userInfo.getUserCar();
                    tvCx.setEnabled(false);
                    tvCx.setText(OtherLogic.carSelect(userInfo.getUserCar().isBigCar(), userInfo.getUserCar().getTruckRequire()));
                }
            }
        });
    }

    private void initPop() {
        ArrayAdapter adapter = new ArrayAdapter(this, R.layout.simple_list_item, data);
        AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 0) {
                    isBigCar = true;
                } else if (i == 1) {
                    smallCarType = "BlueOblique";
                } else if (i == 2) {
                    smallCarType = "BlueGround";
                } else if (i == 3) {
                    smallCarType = "YellowGround";
                } else if (i == 4) {
                    smallCarType = "Five";
                } else if (i == 5) {
                    smallCarType = "Box";
                }
                tvCx.setText(data.get(i));
                cxPopup.dismiss();
            }
        };
        cxPopup = QMUIPopups.listPopup(this,
                QMUIDisplayHelper.dp2px(this, 100),
                QMUIDisplayHelper.dp2px(this, 240),
                adapter, onItemClickListener)
                .animStyle(QMUIPopup.ANIM_GROW_FROM_CENTER)
                .preferredDirection(QMUIPopup.DIRECTION_BOTTOM)
                .shadow(true)
                .offsetYIfTop(QMUIDisplayHelper.dp2px(this, 1))
                .skinManager(QMUISkinManager.defaultInstance(this));
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_auth;
    }

    private void initView() {
//        companyBeans = getIntent().getParcelableArrayListExtra("company");
//        licensesBeans = getIntent().getParcelableArrayListExtra("licenses");
        if (companyBeans != null) {
            for (int i = 0; i < companyBeans.size(); i++) {
                CompanyBean bean = companyBeans.get(i);
                if (Constant.STORE_TYPE_3.equals(bean.getType())) {
                    findViewById(R.id.et_company_hint).setVisibility(View.GONE);
                    etCampany.setText(bean.getName());
                    if (bean.getAddress() != null)
                    tvAddress.setText(bean.getAddress().getAddress());
                    ivYyzzZt.setVisibility(View.VISIBLE);
                    Glide.with(context).load(UrlKit.getImgUrl(bean.getBusinessLicense())).transform(new GlideRoundTransform()).into(ivYyzz);
                    if (Constant.ID_STATUS_REJECT.equals(bean.getStatus())) {
                        etCampany.setEnabled(true);
                        tvAddress.setEnabled(true);
                        ivYyzz.setEnabled(true);
                        isCompany = true;
                        ivYyzzZt.setImageResource(R.mipmap.ic_auth_bh);
                        tvYyzzBh.setVisibility(View.VISIBLE);
                        tvYyzzBh.setText(bean.getNote());
                    } else {
                        etCampany.setEnabled(false);
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
        if (licensesBeans != null) {
            for (int i = 0; i < licensesBeans.size(); i++) {
                LicensesBean bean = licensesBeans.get(i);
                if (Constant.AUTH_IDENTITY_CARD.equals(bean.getType())) {
                    Glide.with(context).load(UrlKit.getImgUrl(bean.getPositive())).transform(new GlideRoundTransform()).into(ivSfzZm);
                    Glide.with(context).load(UrlKit.getImgUrl(bean.getBack())).transform(new GlideRoundTransform()).into(ivSfzFm);
                    etName.setText(bean.getOwner());
                    etId.setText(bean.getNum());
                    iv_Scan.setVisibility(View.GONE);
                    ivSfzZmZt.setVisibility(View.VISIBLE);
                    ivSfzFmZt.setVisibility(View.VISIBLE);
                    if (Constant.ID_STATUS_PASSED.equals(bean.getStatus())) {
                        isAuth = false;
                        ivSfzFm.setEnabled(false);
                        ivSfzZm.setEnabled(false);
                        etName.setEnabled(false);
                        etId.setEnabled(false);
                        ivSfzZmZt.setImageResource(R.mipmap.ic_auth_yrz);
                        ivSfzFmZt.setImageResource(R.mipmap.ic_auth_yrz);
                    } else if (Constant.ID_STATUS_VERIFYING.equals(bean.getStatus())) {
                        isAuth = false;
                        ivSfzFm.setEnabled(false);
                        ivSfzZm.setEnabled(false);
                        etName.setEnabled(false);
                        etId.setEnabled(false);
                        ivSfzZmZt.setImageResource(R.mipmap.ic_auth_shz);
                        ivSfzFmZt.setImageResource(R.mipmap.ic_auth_shz);
                    } else {
                        ivSfzZmZt.setImageResource(R.mipmap.ic_auth_bh);
                        ivSfzFmZt.setImageResource(R.mipmap.ic_auth_bh);
                        tvSfzZmBh.setVisibility(View.VISIBLE);
                        tvSfzZmBh.setText(bean.getNote());
                        tvSfzZmBh.setVisibility(View.VISIBLE);
                        tvSfzFmBh.setText(bean.getNote());
                    }
                } else if (Constant.AUTH_DRIVERS_LICENSE.equals(bean.getType())) {
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
                        if (type == 1) {
                            sfzZm = currentPath;
                            Glide.with(context).load(UrlKit.getImgUrl(currentPath)).into(ivSfzZm);
                            ivSfzZmZt.setVisibility(View.GONE);
                        } else if (type == 2) {
                            sfzFm = currentPath;
                            Glide.with(context).load(UrlKit.getImgUrl(currentPath)).into(ivSfzFm);
                            ivSfzFmZt.setVisibility(View.GONE);
                        } else if (type == 21) {
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
                            Glide.with(context).load(UrlKit.getImgUrl(currentPath)).into(ivHyx);
                            addAuthData(Constant.AUTH_CARGO_INSURANCE, currentPath);
                            ivHyxZt.setVisibility(View.GONE);
                        } else if (type == 8) {
                            yyzz = currentPath;
                            Glide.with(context).load(UrlKit.getImgUrl(currentPath)).into(ivYyzz);
                            ivYyzzZt.setVisibility(View.GONE);
                        }
                    });
                    break;
                case SCAN_ID:
                    etId.setText(data.getStringExtra("id").toString());
                    etName.setText(data.getStringExtra("name").toString());
                    break;
                case 1000:
                    poiItemStart = data.getParcelableExtra("start");
                    if (poiItemStart != null)
                        tvAddress.setText(poiItemStart.getProvinceName() + poiItemStart.getCityName() + poiItemStart.getAdName() + poiItemStart.getTitle());
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

    public void check() {
        if (sfzZm.equals("")) {
            showToast("请选择身份证正面照片");
            return;
        }
        if (sfzFm.equals("")) {
            showToast("请选择身份证反面照片");
            return;
        }
        String idCard = etId.getText().toString();
        if (idCard.equals("")) {
            showToast("请填写身份证号码");
            return;
        }
        if (idCard.length() < 15) {
            Util.toast("身份证格式不正确");
            return;
        }
        String idName = etName.getText().toString();
        if (idName.equals("")) {
            showToast("请填写身份证姓名");
            return;
        }
        if (idName.length() < 2) {
            Util.toast("名字不正确");
            return;
        }
        authBeans.add(new ReqAuthBean(Constant.AUTH_IDENTITY_CARD, sfzZm, sfzFm, idName, idCard, getCardId(Constant.AUTH_IDENTITY_CARD)));
        if (authBeans.size() > 0) {
            authInfo();
        }
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
        companyBean.setName(companyName);
        companyBean.setType(Constant.STORE_TYPE_3);
        companyBean.setId(getCompanyId());
        companyBean.setBusinessLicense(yyzz);
        list.add(companyBean);
        reqBean.setItems(list);
        return new Gson().toJson(reqBean);
    }

    private void authCompany() {
        NetApi.put(UrlKit.USER_COMPANYS, getCompanyBean(), new JsonCallback() {

            @Override
            public void onSuccess(String response, int id) {
                if (!dialog.isShowing()) {
                    dialog.show();
                }
            }
        });
    }

    @OnClick({R.id.iv_sfz_zm, R.id.iv_sfz_fm, R.id.iv_xsz_zm, R.id.iv_xsz_fm, R.id.iv_jsz_zm, R.id.iv_jqx, R.id.iv_syx, R.id.iv_hyx, R.id.tv_cx, R.id.iv_yyzz, R.id.iv_scan, R.id.btn_go, R.id.tv_address})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_sfz_zm:
                type = 1;
                getPhoto();
                break;
            case R.id.iv_sfz_fm:
                type = 2;
                getPhoto();
                break;
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
            case R.id.iv_yyzz:
                type = 8;
                getPhoto();
                break;
            case R.id.tv_cx:
                cxPopup.show(tvCx);
                break;
            case R.id.iv_scan:
                Intent intent = new Intent(context, ScanIdActivity.class);
                startActivityForResult(intent, SCAN_ID);
                break;
            case R.id.tv_address:
                Intent intent1 = new Intent();
                intent1.putExtra("type", "start");
                ActivityManage.push(ChooseAreaActivity.class, intent1, 1000);
                break;
            case R.id.btn_go:
                if (licensesBeans != null && licensesBeans.size() > 0) {
                    for (int i = 0; i < licensesBeans.size(); i++) {
                        LicensesBean bean = licensesBeans.get(i);
                        if (Constant.AUTH_DRIVERS_LICENSE.equals(bean.getType()) && Constant.ID_STATUS_REJECT.equals(bean.getStatus())) {
                            if (TextUtils.isEmpty(jszZm)) {
                                Util.toast("请上传驾驶证正面");
                                return;
                            }
                        } else if (Constant.AUTH_DRIVING_LICENSE.equals(bean.getType()) && Constant.ID_STATUS_REJECT.equals(bean.getStatus())) {
                            if (TextUtils.isEmpty(xszZm)) {
                                Util.toast("请上传行驶证车辆面");
                                return;
                            }
                            if (TextUtils.isEmpty(xszFm)) {
                                Util.toast("请上传行驶证文字面");
                                return;
                            }
                        } else if (Constant.AUTH_COMPULSORY_INSURANCE.equals(bean.getType()) && Constant.ID_STATUS_REJECT.equals(bean.getStatus())) {
                            if (TextUtils.isEmpty(jqx)) {
                                Util.toast("请上传交强险");
                                return;
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
                }
                String ck = tvCx.getText().toString();
                if (TextUtils.isEmpty(ck)) {
                    Util.toast(tvCx.getHint().toString());
                    return;
                }
                if (isAuth) {
                    check();
                } else {
                    if (authBeans.size() > 0) {
                        authInfo();
                    }
                }
                if (carBean == null) {
                    updateCar();
                }
                if (isCompany) {
                    companyName = etCampany.getText().toString();
                    if (!TextUtils.isEmpty(companyName) || poiItemStart != null || !TextUtils.isEmpty(yyzz)) {
                        if (TextUtils.isEmpty(companyName)) {
                            Util.toast("请填写公司名称");
                            return;
                        }
                        if (poiItemStart == null) {
                            Util.toast("请选择公司地址");
                            return;
                        }
                        if (TextUtils.isEmpty(yyzz)) {
                            Util.toast("请选择公司营业执照");
                            return;
                        }
                        authCompany();
                    }
                }
                break;
        }
    }

    private void updateCar() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("bigCar", isBigCar);
        map.put("truckRequire", smallCarType);
        NetApi.post(UrlKit.USER_CAR, map, new JsonCallback() {

            @Override
            public void onSuccess(String response, int id) {
//                if (!dialog.isShowing()) {
//                    Util.toast("提交成功");
//                }
            }
        });
    }
}
