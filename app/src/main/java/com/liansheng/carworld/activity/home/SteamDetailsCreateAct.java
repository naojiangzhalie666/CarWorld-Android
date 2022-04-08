package com.liansheng.carworld.activity.home;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.google.gson.Gson;
import com.kongzue.dialog.interfaces.OnDialogButtonClickListener;
import com.kongzue.dialog.util.BaseDialog;
import com.kongzue.dialog.v3.MessageDialog;
import com.liansheng.carworld.R;
import com.liansheng.carworld.activity.BaseActivity;
import com.liansheng.carworld.activity.logic.OtherLogic;
import com.liansheng.carworld.adapter.company.SteamStatusAdapter;
import com.liansheng.carworld.bean.UserInfo;
import com.liansheng.carworld.bean.company.ReqSteamGuarantee;
import com.liansheng.carworld.bean.company.SteamGuaranteeBean;
import com.liansheng.carworld.bean.home.CarDetailsBean;
import com.liansheng.carworld.bean.home.ImageUploadBean;
import com.liansheng.carworld.bean.home.SaleCardBean;
import com.liansheng.carworld.bean.me.LicensesBean;
import com.liansheng.carworld.kit.Constant;
import com.liansheng.carworld.net.JsonCallback;
import com.liansheng.carworld.net.NetApi;
import com.liansheng.carworld.net.UrlKit;
import com.liansheng.carworld.oss.OssServiceUtil;
import com.liansheng.carworld.utils.DateParserHelper;
import com.liansheng.carworld.utils.MoneyValueFilter;
import com.liansheng.carworld.utils.Utils;
import com.liansheng.carworld.view.GlideRoundTransform;
import com.liansheng.carworld.view.PopAddDialog;
import com.liansheng.carworld.view.pop.AddPop;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.entity.LocalMedia;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.droidlover.xdroid.hold.info.SharedInfo;
import cn.droidlover.xdroid.tools.utils.ActivityManage;
import cn.droidlover.xdroid.tools.utils.NetworkUtil;
import cn.droidlover.xdroid.tools.utils.StringFormat;
import cn.droidlover.xdroid.tools.utils.Util;
import okhttp3.Call;

public class SteamDetailsCreateAct extends BaseActivity implements CompoundButton.OnCheckedChangeListener {

    final int CAR_PICK = 1001;

    @BindView(R.id.ll_buy)
    LinearLayout ll_buy;
    @BindView(R.id.ll_sale)
    LinearLayout ll_sale;
    @BindView(R.id.iv_buy_img)
    ImageView iv_buy_img;
    @BindView(R.id.tv_buy)
    TextView tv_buy;
    @BindView(R.id.tv_buy_phone)
    TextView tv_buy_phone;
    @BindView(R.id.iv_sale_img)
    ImageView iv_sale_img;
    @BindView(R.id.tv_sale)
    TextView tv_sale;
    @BindView(R.id.tv_sale_phone)
    TextView tv_sale_phone;
    @BindView(R.id.ll_clxq)
    LinearLayout ll_clxq;
    @BindView(R.id.ll_ckxx)
    LinearLayout ll_ckxx;
    @BindView(R.id.ll_clcl)
    LinearLayout ll_clcl;
    @BindView(R.id.ll_jyxx)
    LinearLayout ll_jyxx;
    //车基本信息
    @BindView(R.id.tv_car)
    TextView tv_car;
    @BindView(R.id.tv_nf)
    TextView tv_nf;
    @BindView(R.id.tv_szd)
    TextView tv_szd;
    @BindView(R.id.tv_jg)
    TextView tv_jg;
    @BindView(R.id.tv_pass)
    TextView tv_pass;
    //车辆详情
    @BindView(R.id.tv_vin)
    TextView tv_vin;
    @BindView(R.id.tv_xcj)
    TextView tv_xcj;
    @BindView(R.id.tv_pf)
    TextView tv_pf;
    @BindView(R.id.tv_ys)
    TextView tv_ys;
    @BindView(R.id.et_gls)
    TextView et_gls;
    @BindView(R.id.tv_sprq)
    TextView tvSprq;
    @BindView(R.id.tv_njdq)
    TextView tvNjdq;
    @BindView(R.id.tv_qxdq)
    TextView tvQxdq;
    @BindView(R.id.tv_pl)
    TextView tvPl;
    @BindView(R.id.tv_szd2)
    TextView tvSzd2;
    @BindView(R.id.tv_spd)
    TextView tvSpd;
    @BindView(R.id.et_ghcs)
    TextView et_ghcs;
    @BindView(R.id.et_keys)
    EditText et_keys;
    @BindView(R.id.et_jyzj)
    EditText et_jyzj;
    @BindView(R.id.et_bzj)
    EditText et_bzj;
    //车况信息
    @BindView(R.id.ck_fdj_t)
    CheckBox ck_fdj_t;
    @BindView(R.id.ck_fdj_f)
    CheckBox ck_fdj_f;
    @BindView(R.id.ck_jgj_t)
    CheckBox ck_jgj_t;
    @BindView(R.id.ck_jgj_f)
    CheckBox ck_jgj_f;
    @BindView(R.id.ck_psc_t)
    CheckBox ck_psc_t;
    @BindView(R.id.ck_psc_f)
    CheckBox ck_psc_f;
    @BindView(R.id.ck_hsc_t)
    CheckBox ck_hsc_t;
    @BindView(R.id.ck_hsc_f)
    CheckBox ck_hsc_f;
    @BindView(R.id.ck_bsx_t)
    CheckBox ck_bsx_t;
    @BindView(R.id.ck_bsx_f)
    CheckBox ck_bsx_f;
    @BindView(R.id.ck_qn_t)
    CheckBox ck_qn_t;
    @BindView(R.id.ck_qn_f)
    CheckBox ck_qn_f;
    @BindView(R.id.ck_et_bjbw_t)
    CheckBox ck_et_bjbw_t;
    @BindView(R.id.ck_et_pqbw_t)
    CheckBox ck_et_pqbw_t;
    @BindView(R.id.ck_et_hjbw_t)
    CheckBox ck_et_hjbw_t;
    @BindView(R.id.ck_et_sxbw_t)
    CheckBox ck_et_sxbw_t;
    @BindView(R.id.ck_et_lscx_t)
    CheckBox ck_et_lscx_t;
    @BindView(R.id.ck_et_nbms_t)
    CheckBox ck_et_nbms_t;
    @BindView(R.id.ck_et_gjss_t)
    CheckBox ck_et_gjss_t;
    @BindView(R.id.ck_et_gzbw_t)
    CheckBox ck_et_gzbw_t;
    @BindView(R.id.ck_et_bjbw_f)
    CheckBox ck_et_bjbw_f;
    @BindView(R.id.ck_et_pqbw_f)
    CheckBox ck_et_pqbw_f;
    @BindView(R.id.ck_et_hjbw_f)
    CheckBox ck_et_hjbw_f;
    @BindView(R.id.ck_et_sxbw_f)
    CheckBox ck_et_sxbw_f;
    @BindView(R.id.ck_et_lscx_f)
    CheckBox ck_et_lscx_f;
    @BindView(R.id.ck_et_nbms_f)
    CheckBox ck_et_nbms_f;
    @BindView(R.id.ck_et_gjss_f)
    CheckBox ck_et_gjss_f;
    @BindView(R.id.ck_et_gzbw_f)
    CheckBox ck_et_gzbw_f;
    @BindView(R.id.ck_et_ms)
    EditText ck_et_ms;
    //交易信息
    @BindView(R.id.ck_ghzt_t)
    CheckBox ck_ghzt_t;
    @BindView(R.id.ck_ghzt_f)
    CheckBox ck_ghzt_f;
    @BindView(R.id.ck_tdgh_t)
    CheckBox ck_tdgh_t;
    @BindView(R.id.ck_tdgh_f)
    CheckBox ck_tdgh_f;
    @BindView(R.id.ck_wlxx_t)
    CheckBox ck_wlxx_t;
    @BindView(R.id.ck_wlxx_f)
    CheckBox ck_wlxx_f;
    @BindView(R.id.et_qtyd)
    EditText et_qtyd;
    //按钮
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    private String userType;
    private String buyerId = "";
    private String sellerId = "";
    private String userCarResourceId;
    private List<ImageUploadBean> uploadBeans = new ArrayList<>();
    private MyAdapter myAdapter;
    private int index = 0;
    private String carUrl = "";
    private boolean buyer;

    @Override
    public void initData(Bundle savedInstanceState) {
        userType = getIntent().getStringExtra(Constant.KEY_TYPE);
        userCarResourceId = getIntent().getStringExtra(Constant.KEY_ID);
        UserInfo userInfo = SharedInfo.getInstance().getEntity(UserInfo.class);
        et_jyzj.setFilters(new InputFilter[]{new MoneyValueFilter()});
        String name = "";
        for (int i = 0; i < userInfo.getLicenses().size(); i++) {
            LicensesBean bean = userInfo.getLicenses().get(i);
            if (Constant.AUTH_IDENTITY_CARD.equals(bean.getType())) {
                name = bean.getOwner();
            }
        }
        if ("buy".equals(userType)) {
            buyerId = userInfo.getId();
            sellerId = "0";
            tv_buy.setText(name);
            tv_buy_phone.setText(userInfo.getMobile());
            if (!TextUtils.isEmpty(userInfo.getHeadphoto())) {
                Glide.with(this).load(UrlKit.getImgUrl(userInfo.getHeadphoto())).into(iv_buy_img);
            }
            ll_buy.setEnabled(false);
        } else if ("sale".equals(userType)) {
            buyerId = "0";
            sellerId = userInfo.getId();
            tv_sale.setText(name);
            tv_sale_phone.setText(userInfo.getMobile());
            if (!TextUtils.isEmpty(userInfo.getHeadphoto())) {
                Glide.with(this).load(UrlKit.getImgUrl(userInfo.getHeadphoto())).into(iv_sale_img);
            }
            ll_sale.setEnabled(false);
        }
        getDetails();
        ck_fdj_t.setOnCheckedChangeListener(this);
        ck_fdj_f.setOnCheckedChangeListener(this);
        ck_jgj_t.setOnCheckedChangeListener(this);
        ck_jgj_f.setOnCheckedChangeListener(this);
        ck_psc_t.setOnCheckedChangeListener(this);
        ck_psc_f.setOnCheckedChangeListener(this);
        ck_hsc_t.setOnCheckedChangeListener(this);
        ck_hsc_f.setOnCheckedChangeListener(this);
        ck_bsx_t.setOnCheckedChangeListener(this);
        ck_bsx_f.setOnCheckedChangeListener(this);
        ck_qn_t.setOnCheckedChangeListener(this);
        ck_qn_f.setOnCheckedChangeListener(this);
        ck_et_bjbw_t.setOnCheckedChangeListener(this);
        ck_et_pqbw_t.setOnCheckedChangeListener(this);
        ck_et_hjbw_t.setOnCheckedChangeListener(this);
        ck_et_sxbw_t.setOnCheckedChangeListener(this);
        ck_et_lscx_t.setOnCheckedChangeListener(this);
        ck_et_nbms_t.setOnCheckedChangeListener(this);
        ck_et_gjss_t.setOnCheckedChangeListener(this);
        ck_et_gzbw_t.setOnCheckedChangeListener(this);
        ck_et_bjbw_f.setOnCheckedChangeListener(this);
        ck_et_pqbw_f.setOnCheckedChangeListener(this);
        ck_et_hjbw_f.setOnCheckedChangeListener(this);
        ck_et_sxbw_f.setOnCheckedChangeListener(this);
        ck_et_lscx_f.setOnCheckedChangeListener(this);
        ck_et_nbms_f.setOnCheckedChangeListener(this);
        ck_et_gjss_f.setOnCheckedChangeListener(this);
        ck_et_gzbw_f.setOnCheckedChangeListener(this);
        ck_ghzt_t.setOnCheckedChangeListener(this);
        ck_ghzt_f.setOnCheckedChangeListener(this);
        ck_tdgh_t.setOnCheckedChangeListener(this);
        ck_tdgh_f.setOnCheckedChangeListener(this);
        ck_wlxx_t.setOnCheckedChangeListener(this);
        ck_wlxx_f.setOnCheckedChangeListener(this);
        uploadBeans.add(new ImageUploadBean());
        myAdapter = new MyAdapter(uploadBeans);
        myAdapter.addChildClickViewIds(R.id.item_img_close, R.id.item_img_add);
        myAdapter.setOnItemChildClickListener((adapter, view, position) -> {
            ImageUploadBean item = (ImageUploadBean) adapter.getItem(position);
            switch (view.getId()) {
                case R.id.item_img_add:
                    index = position;
                    if (TextUtils.isEmpty(item.getUrl())) {
                        Utils.getPhoto(context, PictureConfig.MULTIPLE, 3 - myAdapter.getData().size() + 1);
                    }
                    break;
                case R.id.item_img_close:
                    adapter.remove(position);
                    if (adapter.getData().size() == 2) {
                        ImageUploadBean uploadBean = (ImageUploadBean) adapter.getData().get(adapter.getData().size() - 1);
                        if (!TextUtils.isEmpty(uploadBean.getUrl())) {
                            adapter.addData(new ImageUploadBean());
                        }
                    }
                    break;
            }
        });
        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        recyclerView.setAdapter(myAdapter);
    }

    private void getDetails() {
        NetApi.get(UrlKit.CAR_RESOURCE_DETAILS + userCarResourceId, null, new JsonCallback() {

            @Override
            public void onSuccess(String response, int id) {
                CarDetailsBean carDetailsBean = new Gson().fromJson(response, CarDetailsBean.class);
                setCarData(carDetailsBean);
            }
        });
    }


    //车辆信息
    private void setCarData(CarDetailsBean bean) {
        //基本信息
        tv_car.setText(bean.getBrand_fct() + " " + bean.getBrand_serie());
        if (bean.getRegistration_date() != null && bean.getRegistration_date().length() > 4) {
            tv_nf.setText(bean.getRegistration_date().substring(0, 4) + "年 | " + StringFormat.doubleFormatForW2(bean.getMileage()) + "公里");
        } else {
            tv_nf.setText(bean.getRegistration_date() + "年 | " + StringFormat.doubleFormatForW2(bean.getMileage()) + "公里");
        }
        tv_szd.setText(bean.getLocation());
        tv_jg.setText(StringFormat.doubleFormatForW2(bean.getSell_price()));
//        if (TextUtils.isEmpty(bean.getPassedDate())) {
//            tv_pass.setText("");
//        } else {
//            tv_pass.setText(DateParserHelper.getYearAndMonthAndDay2(bean.getPassedDate()) + "发布");
//        }
        //详情信息
        tv_xcj.setText(String.valueOf(bean.getGuidance_price()));
        tv_ys.setText(bean.getColor());
        tvPl.setText(bean.getVolume());
        tv_pf.setText(bean.getStandard());
        et_gls.setText(StringFormat.doubleFormatForW2(bean.getMileage()) + "公里");
//        et_csjg.setText(String.valueOf(bean.getSell_price()));
//        tvGh.setText(String.valueOf(bean.getTransfer_times()));
        if (0 == bean.getTransfer_times()) {
            et_ghcs.setText("未填写");
        } else {
            et_ghcs.setText(String.valueOf(bean.getTransfer_times()));
        }
        tvSprq.setText(DateParserHelper.getYearMonthAndDay(bean.getRegistration_date()));
//        tvScrq.setText(DateParserHelper.getYearMonthAndDay(bean.getProduction_date()));
        tvNjdq.setText(DateParserHelper.getYearMonthAndDay(bean.getYearlyInspectionExpiration()));
        tvQxdq.setText(DateParserHelper.getYearMonthAndDay(bean.getCompulsoryInsuranceExpiration()));
        tv_vin.setText(bean.getVin());
        tvSzd2.setText(bean.getLocation());
        tvSpd.setText(bean.getRegistration_city());
//        tvSjsj.setText(DateParserHelper.getYearMonthAndDay(bean.getPassedDate()));
    }


    private void getUser(String content) {
        NetApi.get(UrlKit.USER_BASIC_BY_MOBILE + content, null, new JsonCallback() {

            @Override
            public void onSuccess(String response, int id) {
                UserInfo userInfo = new Gson().fromJson(response, UserInfo.class);
                if (userInfo != null) {
                    if (TextUtils.isEmpty(userInfo.getName())) {
                        Util.toast("对方暂未实名认证");
                        return;
                    }
                    if (buyer) {
                        Glide.with(context).load(UrlKit.getImgUrl(userInfo.getHeadphoto())).into(iv_buy_img);
                        tv_buy.setText(userInfo.getName());
                        tv_buy_phone.setText(userInfo.getMobile());
                        buyerId = userInfo.getId();
                    } else {
                        Glide.with(context).load(UrlKit.getImgUrl(userInfo.getHeadphoto())).into(iv_sale_img);
                        tv_sale.setText(userInfo.getName());
                        tv_sale_phone.setText(userInfo.getMobile());
                        sellerId = userInfo.getId();
                    }
                }
            }
        });
    }

    @Override
    public int getLayoutId() {
        return R.layout.act_steam_details_create;
    }

    @OnClick({R.id.tv_clxq, R.id.tv_ckxx, R.id.tv_clcl, R.id.tv_jyxx, R.id.steam_wbcx, R.id.steam_pzcx,
            R.id.btn_submit, R.id.ll_buy, R.id.ll_sale})
    public void onViewClick(View view) {
        switch (view.getId()) {
            case R.id.ll_buy:
                buyer = true;
                PopAddDialog.show(getSupportFragmentManager())
                        .setTitle("被邀请者账号（手机号）")
                        .setCallback((view1, content) -> {
                            if (content.length() != 11) {
                                Util.toast("手机号格式不正确");
                                return;
                            }
                            getUser(content);
                        });
                break;
            case R.id.ll_sale:
                buyer = false;
                PopAddDialog.show(getSupportFragmentManager())
                        .setTitle("被邀请者账号（手机号）")
                        .setCallback((view1, content) -> {
                            if (content.length() != 11) {
                                Util.toast("手机号格式不正确");
                                return;
                            }
                            getUser(content);
                        });
                break;
            case R.id.tv_clxq:
                ll_clxq.setVisibility(ll_clxq.getVisibility() == View.GONE ? View.VISIBLE : View.GONE);
                break;
            case R.id.tv_ckxx:
                ll_ckxx.setVisibility(ll_ckxx.getVisibility() == View.GONE ? View.VISIBLE : View.GONE);
                break;
            case R.id.tv_clcl:
                ll_clcl.setVisibility(ll_clcl.getVisibility() == View.GONE ? View.VISIBLE : View.GONE);
                break;
            case R.id.tv_jyxx:
                ll_jyxx.setVisibility(ll_jyxx.getVisibility() == View.GONE ? View.VISIBLE : View.GONE);
                break;
            case R.id.steam_wbcx:
                Intent intent1 = new Intent();
                intent1.putExtra(Constant.KEY_TYPE, 1);
                intent1.putExtra(Constant.KEY_TITLE, tv_vin.getText().toString());
                ActivityManage.push(SecondhandSearchAct.class, intent1);
                break;
            case R.id.steam_pzcx:
                Intent intent2 = new Intent();
                intent2.putExtra(Constant.KEY_TYPE, 2);
                intent2.putExtra(Constant.KEY_TITLE, tv_vin.getText().toString());
                ActivityManage.push(SecondhandSearchAct.class, intent2);
                break;
            case R.id.btn_submit:
                if ("0".equals(buyerId)) {
                    Util.toast("请选择买家");
                    return;
                }
                if ("0".equals(sellerId)) {
                    Util.toast("请邀请卖家");
                    return;
                }
                String keys = et_keys.getText().toString();
                String jyzj = et_jyzj.getText().toString();
                String bzj = et_bzj.getText().toString();
                if (TextUtils.isEmpty(keys) || TextUtils.isEmpty(jyzj) || TextUtils.isEmpty(bzj)) {
                    Util.toast("车辆信息未填写完整");
                    return;
                }
                if (Integer.parseInt(bzj) < 3000) {
                    Util.toast("保障金不得低于3000");
                    return;
                }
                String et_ms = ck_et_ms.getText().toString();
                if (TextUtils.isEmpty(et_ms)) {
                    Util.toast("请填写车况描述");
                    return;
                }
                List<ImageUploadBean> data = myAdapter.getData();
                List<String> imgs = new ArrayList<>();
                for (int i = 0; i < data.size(); i++) {
                    if (!TextUtils.isEmpty(data.get(i).getUrl())) {
                        imgs.add(data.get(i).getUrl());
                    }
                }
                if (imgs.size() == 0) {
                    Util.toast("请上传车辆材料");
                    return;
                }
                String qtyd = et_qtyd.getText().toString();
//                if (TextUtils.isEmpty(qtyd)) {
//                    Util.toast("请填写交易信息(其他约定)");
//                    return;
//                }
                ReqSteamGuarantee guarantee = new ReqSteamGuarantee();
                guarantee.setUserCarResourceId(userCarResourceId);
                guarantee.setKeys(Integer.parseInt(keys));
                guarantee.setTotal((int) (Double.valueOf(jyzj) * 10000));//总价
                guarantee.setSecurityFund(Integer.parseInt(bzj));//保障金
                //
                guarantee.setEngine(ck_fdj_t.isChecked());
                guarantee.setStructure(ck_jgj_t.isChecked());
                guarantee.setGearbox(ck_bsx_t.isChecked());
                guarantee.setAIRBAG(ck_qn_t.isChecked());
                guarantee.setPlate(ck_et_bjbw_t.isChecked());
                guarantee.setPainting(ck_et_pqbw_t.isChecked());
                guarantee.setOverhaul(ck_et_hjbw_t.isChecked());
                guarantee.setRust(ck_et_sxbw_t.isChecked());
                guarantee.setScrew(ck_et_lscx_t.isChecked());
                guarantee.setInterior(ck_et_nbms_t.isChecked());
                guarantee.setSkeleton(ck_et_gjss_t.isChecked());
                guarantee.setRefit(ck_et_gzbw_t.isChecked());
                guarantee.setDescribe(et_ms);
                guarantee.setMaterials(imgs);
                //
                guarantee.setTransfer(ck_ghzt_t.isChecked());
                guarantee.setFileUp(ck_tdgh_t.isChecked());
                guarantee.setLogistics(ck_wlxx_t.isChecked());
                guarantee.setOtherAgreements(qtyd);
                guarantee.setSellerId(sellerId);
                guarantee.setBuyerId(buyerId);
                MessageDialog.build(this)
                        .setTitle("温馨提示")
                        .setMessage("请确认合同信息是否无误")
                        .setCancelButton("取消", new OnDialogButtonClickListener() {
                            @Override
                            public boolean onClick(BaseDialog baseDialog, View v) {
                                return false;
                            }
                        })
                        .setOkButton("确认", new OnDialogButtonClickListener() {
                            @Override
                            public boolean onClick(BaseDialog baseDialog, View v) {
                                createGuarantee(guarantee);
                                return false;
                            }
                        })
                        .show();
                break;
        }
    }

    private void createGuarantee(ReqSteamGuarantee guarantee) {
        NetApi.postJson(UrlKit.APP_PLATFORM_GUARANTEE, new Gson().toJson(guarantee), new JsonCallback() {

            @Override
            public void onSuccess(String response, int id) {
                SteamGuaranteeBean guaranteeBean = new Gson().fromJson(response, SteamGuaranteeBean.class);
                if (guaranteeBean != null) {
                    Util.toast("合同创建成功");
                    Intent intent = new Intent();
                    intent.putExtra(Constant.KEY_ID, String.valueOf(guaranteeBean.getId()));
                    ActivityManage.push(SteamDetailsAct.class, intent);
                    finish();
                }
            }
        });
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()) {
            case R.id.ck_fdj_t:
                ck_fdj_f.setChecked(!isChecked);
                break;
            case R.id.ck_fdj_f:
                ck_fdj_t.setChecked(!isChecked);
                break;
            case R.id.ck_jgj_t:
                ck_jgj_f.setChecked(!isChecked);
                break;
            case R.id.ck_jgj_f:
                ck_jgj_t.setChecked(!isChecked);
                break;
            case R.id.ck_psc_t:
                ck_psc_f.setChecked(!isChecked);
                break;
            case R.id.ck_psc_f:
                ck_psc_t.setChecked(!isChecked);
                break;
            case R.id.ck_hsc_t:
                ck_hsc_f.setChecked(!isChecked);
                break;
            case R.id.ck_hsc_f:
                ck_hsc_t.setChecked(!isChecked);
                break;
            case R.id.ck_bsx_t:
                ck_bsx_f.setChecked(!isChecked);
                break;
            case R.id.ck_bsx_f:
                ck_bsx_t.setChecked(!isChecked);
                break;
            case R.id.ck_qn_t:
                ck_qn_f.setChecked(!isChecked);
                break;
            case R.id.ck_qn_f:
                ck_qn_t.setChecked(!isChecked);
                break;
            case R.id.ck_et_bjbw_t:
                ck_et_bjbw_f.setChecked(!isChecked);
                break;
            case R.id.ck_et_bjbw_f:
                ck_et_bjbw_t.setChecked(!isChecked);
                break;
            case R.id.ck_et_pqbw_t:
                ck_et_pqbw_f.setChecked(!isChecked);
                break;
            case R.id.ck_et_pqbw_f:
                ck_et_pqbw_t.setChecked(!isChecked);
                break;
            case R.id.ck_et_hjbw_t:
                ck_et_hjbw_f.setChecked(!isChecked);
                break;
            case R.id.ck_et_hjbw_f:
                ck_et_hjbw_t.setChecked(!isChecked);
                break;
            case R.id.ck_et_sxbw_t:
                ck_et_sxbw_f.setChecked(!isChecked);
                break;
            case R.id.ck_et_sxbw_f:
                ck_et_sxbw_t.setChecked(!isChecked);
                break;
            case R.id.ck_et_lscx_t:
                ck_et_lscx_f.setChecked(!isChecked);
                break;
            case R.id.ck_et_lscx_f:
                ck_et_lscx_t.setChecked(!isChecked);
                break;
            case R.id.ck_et_nbms_t:
                ck_et_nbms_f.setChecked(!isChecked);
                break;
            case R.id.ck_et_nbms_f:
                ck_et_nbms_t.setChecked(!isChecked);
                break;
            case R.id.ck_et_gjss_t:
                ck_et_gjss_f.setChecked(!isChecked);
                break;
            case R.id.ck_et_gjss_f:
                ck_et_gjss_t.setChecked(!isChecked);
                break;
            case R.id.ck_et_gzbw_t:
                ck_et_gzbw_f.setChecked(!isChecked);
                break;
            case R.id.ck_et_gzbw_f:
                ck_et_gzbw_t.setChecked(!isChecked);
                break;
            case R.id.ck_ghzt_t:
                ck_ghzt_f.setChecked(!isChecked);
                break;
            case R.id.ck_ghzt_f:
                ck_ghzt_t.setChecked(!isChecked);
                break;
            case R.id.ck_tdgh_t:
                ck_tdgh_f.setChecked(!isChecked);
                break;
            case R.id.ck_tdgh_f:
                ck_tdgh_t.setChecked(!isChecked);
                break;
            case R.id.ck_wlxx_t:
                ck_wlxx_f.setChecked(!isChecked);
                break;
            case R.id.ck_wlxx_f:
                ck_wlxx_t.setChecked(!isChecked);
                break;
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && (requestCode == PictureConfig.CHOOSE_REQUEST || requestCode == CAR_PICK)) {
            List<LocalMedia> selectList = PictureSelector.obtainMultipleResult(data);
            if (selectList != null && selectList.size() > 0) {
                List<String> strings = new ArrayList<>();
                for (int i = 0; i < selectList.size(); i++) {
                    strings.add(selectList.get(i).getCompressPath());
                }
                upload(strings, requestCode);
            }
        }
    }

    public void upload(List<String> urls, int requestCode) {
        NetworkUtil.showCutscenes("正在上传...", "剩余" + urls.size() + "张");
        OssServiceUtil.getInstance().asyncPutImage(OtherLogic.getImgUrl(), urls.get(0), (objectResult, currentPath) -> {
            if (requestCode == CAR_PICK) {
                carUrl = currentPath;
                NetworkUtil.dismissCutscenes();
            } else {
                changeImage(index, currentPath);
                index++;
                urls.remove(0);
                if (urls.size() > 0) {
                    upload(urls, requestCode);
                } else {
                    NetworkUtil.dismissCutscenes();
                }
            }
        });
    }

    private void changeImage(int position, String url) {
        List<ImageUploadBean> data = myAdapter.getData();
        for (int i = 0; i < data.size(); i++) {
            if (position == i) {
                ImageUploadBean uploadBean = data.get(i);
                uploadBean.setUrl(url);
                if (data.size() != 3)
                    data.add(new ImageUploadBean());
            }
        }
        myAdapter.replaceData(data);
    }

    private class MyAdapter extends BaseQuickAdapter<ImageUploadBean, BaseViewHolder> {

        public MyAdapter(List<ImageUploadBean> imgs) {
            super(R.layout.item_img, imgs);
        }

        @Override
        protected void convert(@NotNull BaseViewHolder holder, ImageUploadBean item) {
            ImageView view = holder.getView(R.id.item_img);
            if (TextUtils.isEmpty(item.getUrl())) {
                holder.setGone(R.id.item_img_close, true);
                view.setVisibility(View.GONE);
            } else {
                holder.setGone(R.id.item_img_close, false);
                view.setVisibility(View.VISIBLE);
                Glide.with(ActivityManage.peek()).load(UrlKit.getImgUrl(item.getUrl())).transform(new GlideRoundTransform()).into(view);
            }
        }
    }
}
