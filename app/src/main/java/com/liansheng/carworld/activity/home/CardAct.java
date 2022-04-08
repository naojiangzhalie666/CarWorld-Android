package com.liansheng.carworld.activity.home;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.liansheng.carworld.R;
import com.liansheng.carworld.activity.BaseActivity;
import com.liansheng.carworld.activity.ScanIdActivity;
import com.liansheng.carworld.activity.logic.OtherLogic;
import com.liansheng.carworld.bean.me.ReqBean;
import com.liansheng.carworld.bean.me.LicensesBean;
import com.liansheng.carworld.bean.me.ReqAuthBean;
import com.liansheng.carworld.kit.Constant;
import com.liansheng.carworld.net.JsonCallback;
import com.liansheng.carworld.net.NetApi;
import com.liansheng.carworld.net.UrlKit;
import com.liansheng.carworld.oss.OssServiceUtil;
import com.liansheng.carworld.utils.DateParserHelper;
import com.liansheng.carworld.utils.Utils;
import com.liansheng.carworld.view.GlideRoundTransform;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.entity.LocalMedia;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.droidlover.xdroid.hold.info.SharedInfo;
import cn.droidlover.xdroid.tools.utils.Util;
import okhttp3.Call;

public class CardAct extends BaseActivity {

    @BindView(R.id.iv_sfz_zm)
    ImageView ivSfzZm;
    @BindView(R.id.iv_sfz_zm_zt)
    ImageView ivSfzZmZt;
    @BindView(R.id.iv_sfz_fm)
    ImageView ivSfzFm;
    @BindView(R.id.iv_sfz_fm_zt)
    ImageView ivSfzFmZt;
    @BindView(R.id.et_personal_name)
    EditText etPersonalName;
    @BindView(R.id.iv_scan)
    ImageView ivScan;
    @BindView(R.id.et_personal_card)
    EditText etPersonalCard;
    @BindView(R.id.iv_xsz)
    ImageView ivXsz;
    @BindView(R.id.iv_xsz_zt)
    ImageView ivXszZt;
    @BindView(R.id.card_xsz_name)
    TextView cardXszName;
    @BindView(R.id.card_xsz_num)
    TextView cardXszNum;
    @BindView(R.id.card_xsz_date)
    TextView cardXszDate;
    @BindView(R.id.card_ll_xsz)
    LinearLayout cardLlXsz;
    @BindView(R.id.iv_jsz)
    ImageView ivJsz;
    @BindView(R.id.iv_jsz_zt)
    ImageView ivJszZt;
    @BindView(R.id.card_jsz_name)
    TextView cardJszName;
    @BindView(R.id.card_jsz_num)
    TextView cardJszNum;
    @BindView(R.id.card_jsz_date)
    TextView cardJszDate;
    @BindView(R.id.card_ll_jsz)
    LinearLayout cardLlJsz;
    @BindView(R.id.iv_jqx)
    ImageView ivJqx;
    @BindView(R.id.iv_jqx_zt)
    ImageView ivJqxZt;
    @BindView(R.id.card_ll_jqx)
    LinearLayout cardLlJqx;
    @BindView(R.id.iv_syx)
    ImageView ivSyx;
    @BindView(R.id.iv_syx_zt)
    ImageView ivSyxZt;
    @BindView(R.id.card_ll_syx)
    LinearLayout cardLlSyx;
    @BindView(R.id.iv_hyx)
    ImageView ivHyx;
    @BindView(R.id.iv_hyx_zt)
    ImageView ivHyxZt;
    @BindView(R.id.card_ll_hyx)
    LinearLayout cardLlHyx;
    @BindView(R.id.card_other_name)
    TextView cardOtherName;
    @BindView(R.id.card_other_date)
    TextView cardOtherDate;
    @BindView(R.id.card_ll_other)
    LinearLayout cardLlOther;
    @BindView(R.id.btn_go)
    Button btnGo;
    @BindView(R.id.tv_sfz_zm_bh)
    TextView tvSfzZmBh;
    @BindView(R.id.tv_sfz_fm_bh)
    TextView tvSfzFmBh;
    @BindView(R.id.tv_jsz_bh)
    TextView tvJszBh;
    @BindView(R.id.tv_xsz_bh)
    TextView tvXszBh;
    @BindView(R.id.tv_other_bh)
    TextView tvOtherBh;

    public final int SCAN_ID = 1001;
    private int type;
    public String sfzZm = "";
    public String sfzFm = "";

    private List<ReqAuthBean> authBeans = new ArrayList<>();
    LicensesBean item;

    @Override
    public void initData(Bundle savedInstanceState) {
        item = getIntent().getParcelableExtra(Constant.KEY_BEAN);
        if (Constant.AUTH_IDENTITY_CARD.equals(item.getType())) {
            findViewById(R.id.card_ll_id).setVisibility(View.VISIBLE);
            ivSfzZmZt.setVisibility(View.VISIBLE);
            ivSfzFmZt.setVisibility(View.VISIBLE);
            ivScan.setVisibility(View.GONE);
            Glide.with(context).load(UrlKit.getImgUrl(item.getPositive())).transform(new GlideRoundTransform()).into(ivSfzZm);
            Glide.with(context).load(UrlKit.getImgUrl(item.getBack())).transform(new GlideRoundTransform()).into(ivSfzFm);
            etPersonalName.setText(item.getOwner());
            etPersonalCard.setText(item.getNum());
            if (Constant.ID_STATUS_PASSED.equals(item.getStatus())) {
                ivSfzZmZt.setImageResource(R.mipmap.ic_auth_yrz);
                ivSfzFmZt.setImageResource(R.mipmap.ic_auth_yrz);
                ivSfzZm.setEnabled(false);
                ivSfzFm.setEnabled(false);
            } else if (Constant.ID_STATUS_VERIFYING.equals(item.getStatus())) {
                ivSfzZmZt.setImageResource(R.mipmap.ic_auth_shz);
                ivSfzFmZt.setImageResource(R.mipmap.ic_auth_shz);
                ivSfzZm.setEnabled(false);
                ivSfzFm.setEnabled(false);
            } else {
                ivSfzZm.setEnabled(true);
                ivSfzFm.setEnabled(true);
                ivSfzZmZt.setImageResource(R.mipmap.ic_auth_bh);
                ivSfzFmZt.setImageResource(R.mipmap.ic_auth_bh);
                tvSfzZmBh.setVisibility(View.VISIBLE);
                tvSfzZmBh.setText(item.getNote());
                tvSfzZmBh.setVisibility(View.VISIBLE);
                tvSfzFmBh.setText(item.getNote());
                findViewById(R.id.btn_go).setVisibility(View.VISIBLE);
            }
        } else if (Constant.AUTH_DRIVERS_LICENSE.equals(item.getType())) {
            ivJszZt.setVisibility(View.VISIBLE);
            cardLlJsz.setVisibility(View.VISIBLE);
            cardJszName.setText(item.getOwner());
            cardJszNum.setText(item.getNum());
            Glide.with(context).load(UrlKit.getImgUrl(item.getPositive())).transform(new GlideRoundTransform()).into(ivJsz);
            if (!TextUtils.isEmpty(item.getExpiration())) {
                cardJszDate.setText(DateParserHelper.getYearAndMonthAndDay(item.getExpiration()));
            }
            if (Constant.ID_STATUS_PASSED.equals(item.getStatus())) {
                ivJszZt.setImageResource(R.mipmap.ic_auth_yrz);
                ivJsz.setEnabled(false);
            } else if (Constant.ID_STATUS_VERIFYING.equals(item.getStatus())) {
                ivJszZt.setImageResource(R.mipmap.ic_auth_shz);
                ivJsz.setEnabled(false);
            } else {
                ivJsz.setEnabled(true);
                ivJszZt.setImageResource(R.mipmap.ic_auth_bh);
                tvJszBh.setVisibility(View.VISIBLE);
                tvJszBh.setText(item.getNote());
            }
        } else if (Constant.AUTH_DRIVING_LICENSE.equals(item.getType())) {
            ivXszZt.setVisibility(View.VISIBLE);
            cardLlXsz.setVisibility(View.VISIBLE);
            cardXszName.setText(item.getOwner());
            cardXszNum.setText(item.getNum());
            Glide.with(context).load(UrlKit.getImgUrl(item.getPositive())).transform(new GlideRoundTransform()).into(ivXsz);
            if (Constant.ID_STATUS_PASSED.equals(item.getStatus())) {
                ivXszZt.setImageResource(R.mipmap.ic_auth_yrz);
                ivXsz.setEnabled(false);
            } else if (Constant.ID_STATUS_VERIFYING.equals(item.getStatus())) {
                ivXszZt.setImageResource(R.mipmap.ic_auth_shz);
                ivXsz.setEnabled(false);
            } else {
                ivXszZt.setImageResource(R.mipmap.ic_auth_bh);
                ivXsz.setEnabled(true);
            }
            if (!TextUtils.isEmpty(item.getExpiration())) {
                cardXszDate.setText(DateParserHelper.getYearAndMonthAndDay(item.getExpiration()));
            }
            tvXszBh.setVisibility(View.VISIBLE);
            tvXszBh.setText(item.getNote());
        } else if (Constant.AUTH_COMPULSORY_INSURANCE.equals(item.getType())) {
            cardLlOther.setVisibility(View.VISIBLE);
            cardLlJqx.setVisibility(View.VISIBLE);
            ivJqxZt.setVisibility(View.VISIBLE);
            Glide.with(context).load(UrlKit.getImgUrl(item.getPositive())).transform(new GlideRoundTransform()).into(ivJqx);
            cardOtherName.setText(item.getOwner());
            if (Constant.ID_STATUS_PASSED.equals(item.getStatus())) {
                ivJqxZt.setImageResource(R.mipmap.ic_auth_yrz);
                ivJqx.setEnabled(false);
            } else if (Constant.ID_STATUS_VERIFYING.equals(item.getStatus())) {
                ivJqxZt.setImageResource(R.mipmap.ic_auth_shz);
                ivJqx.setEnabled(false);
            } else {
                ivJqxZt.setImageResource(R.mipmap.ic_auth_bh);
                ivJqx.setEnabled(true);
                tvOtherBh.setVisibility(View.VISIBLE);
                tvOtherBh.setText(item.getNote());
            }
            if (!TextUtils.isEmpty(item.getExpiration())) {
                cardOtherDate.setText(DateParserHelper.getYearAndMonthAndDay(item.getExpiration()));
            }
        } else if (Constant.AUTH_CARGO_INSURANCE.equals(item.getType())) {
            cardLlOther.setVisibility(View.VISIBLE);
            cardLlHyx.setVisibility(View.VISIBLE);
            ivHyxZt.setVisibility(View.VISIBLE);
            Glide.with(context).load(UrlKit.getImgUrl(item.getPositive())).transform(new GlideRoundTransform()).into(ivHyx);
            cardOtherName.setText(item.getOwner());
            if (Constant.ID_STATUS_PASSED.equals(item.getStatus())) {
                ivHyxZt.setImageResource(R.mipmap.ic_auth_yrz);
                ivHyx.setEnabled(false);
            } else if (Constant.ID_STATUS_VERIFYING.equals(item.getStatus())) {
                ivHyxZt.setImageResource(R.mipmap.ic_auth_shz);
                ivHyx.setEnabled(false);
            } else {
                ivHyxZt.setImageResource(R.mipmap.ic_auth_bh);
                ivHyx.setEnabled(true);
                tvOtherBh.setVisibility(View.VISIBLE);
                tvOtherBh.setText(item.getNote());
            }
            if (!TextUtils.isEmpty(item.getExpiration())) {
                cardOtherDate.setText(DateParserHelper.getYearAndMonthAndDay(item.getExpiration()));
            }
        } else if (Constant.AUTH_COMMERCIAL_INSURANCE.equals(item.getType())) {
            cardLlOther.setVisibility(View.VISIBLE);
            cardLlSyx.setVisibility(View.VISIBLE);
            cardOtherName.setText(item.getOwner());
            ivSyxZt.setVisibility(View.VISIBLE);
            if (Constant.ID_STATUS_PASSED.equals(item.getStatus())) {
                ivSyxZt.setImageResource(R.mipmap.ic_auth_yrz);
                ivSyx.setEnabled(false);
            } else if (Constant.ID_STATUS_VERIFYING.equals(item.getStatus())) {
                ivSyxZt.setImageResource(R.mipmap.ic_auth_shz);
                ivSyx.setEnabled(false);
            } else {
                ivSyxZt.setImageResource(R.mipmap.ic_auth_bh);
                ivSyx.setEnabled(true);
                tvOtherBh.setVisibility(View.VISIBLE);
                tvOtherBh.setText(item.getNote());
            }
            Glide.with(context).load(UrlKit.getImgUrl(item.getPositive())).transform(new GlideRoundTransform()).into(ivSyx);
            if (!TextUtils.isEmpty(item.getExpiration())) {
                cardOtherDate.setText(DateParserHelper.getYearAndMonthAndDay(item.getExpiration()));
            }
        }
    }

    @Override
    public int getLayoutId() {
        return R.layout.act_card;
    }


    @OnClick({R.id.iv_sfz_zm, R.id.iv_sfz_fm, R.id.iv_scan, R.id.iv_xsz, R.id.iv_jsz, R.id.iv_jqx, R.id.iv_syx, R.id.iv_hyx,R.id.btn_go})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_sfz_zm:
                type = 1;
                Utils.getCamera(context);
                break;
            case R.id.iv_sfz_fm:
                type = 2;
                Utils.getCamera(context);
                break;
            case R.id.iv_jsz:
                type = 21;
                Utils.getPhoto(context);
                break;
            case R.id.iv_xsz:
                type = 3;
                Utils.getPhoto(context);
                break;
            case R.id.iv_jqx:
                type = 5;
                Utils.getPhoto(context);
                break;
            case R.id.iv_syx:
                type = 6;
                Utils.getPhoto(context);
                break;
            case R.id.iv_hyx:
                type = 7;
                Utils.getPhoto(context);
                break;
            case R.id.iv_scan:
                Intent intent = new Intent(context, ScanIdActivity.class);
                startActivityForResult(intent, SCAN_ID);
                break;
            case R.id.btn_go:
                if (sfzZm.equals("")) {
                    showToast("请选择身份证正面照片");
                    return;
                }
                if (sfzFm.equals("")) {
                    showToast("请选择身份证反面照片");
                    return;
                }
                String idCard = etPersonalCard.getText().toString();
                if (idCard.equals("")) {
                    showToast("请填写身份证号码");
                    return;
                }
                String idName = etPersonalName.getText().toString();
                if (idName.equals("")) {
                    showToast("请填写身份证姓名");
                    return;
                }

                authBeans.add(new ReqAuthBean(Constant.AUTH_IDENTITY_CARD, sfzZm, sfzFm, idName, idCard,item.getId()));
                authInfo();
                break;
        }


    }

    private void addAuthData(String type, String path) {
//        for (int i = 0; i < authBeans.size(); i++) {
//            if (authBeans.get(i).getType().equals(type)) {
//                authBeans.get(i).setPositive(path);
//                return;
//            }
//        }
        authBeans.add(new ReqAuthBean(type, path,item.getId()));
        authInfo();
    }

    private void authInfo() {
//        HashMap<String, Object> map = new HashMap<>();
//        map.put("licenses", new Gson().toJson(authBeans));
        ReqBean<ReqAuthBean> reqBean = new ReqBean();
//        authBean.setLicenses(authBeans);
        reqBean.setItems(authBeans);
        NetApi.put(UrlKit.USER_LICENSES, new Gson().toJson(reqBean), new JsonCallback() {

            @Override
            public void onSuccess(String response, int id) {
                Util.toast("提交成功");
                finish();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case PictureConfig.CHOOSE_REQUEST:
                    String path = PictureSelector.obtainMultipleResult(data).get(0).getCompressPath();
                    OssServiceUtil.getInstance().asyncPutImage(OtherLogic.getImgUrl(), path, (objectResult, currentPath) -> {
                        authBeans.clear();
                        if (type == 1) {
                            sfzZm = currentPath;
                            Glide.with(context).load(UrlKit.getImgUrl(currentPath)).into(ivSfzZm);
                            ivSfzZmZt.setVisibility(View.GONE);
                        } else if (type == 2) {
                            sfzFm = currentPath;
                            Glide.with(context).load(UrlKit.getImgUrl(currentPath)).into(ivSfzFm);
                            ivSfzFmZt.setVisibility(View.GONE);
                        } else if (type == 21) {
                            Glide.with(context).load(UrlKit.getImgUrl(currentPath)).into(ivJsz);
                            addAuthData(Constant.AUTH_DRIVERS_LICENSE, currentPath);
                            ivJszZt.setVisibility(View.GONE);
                        } else if (type == 3) {
                            Glide.with(context).load(UrlKit.getImgUrl(currentPath)).into(ivXsz);
                            addAuthData(Constant.AUTH_DRIVING_LICENSE, currentPath);
                            ivXszZt.setVisibility(View.GONE);
                        } else if (type == 5) {
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
                        }
                    });
                    break;
                case SCAN_ID:
                    etPersonalName.setText(data.getStringExtra("name").toString());
                    etPersonalCard.setText(data.getStringExtra("id").toString());
                    break;
            }
        }
    }
}
