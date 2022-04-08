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
import com.liansheng.carworld.bean.company.SteamGuaranteeBean;
import com.liansheng.carworld.bean.company.SteamGuaranteeUpdateBean;
import com.liansheng.carworld.bean.home.ImageUploadBean;
import com.liansheng.carworld.bean.home.SaleCardBean;
import com.liansheng.carworld.kit.Constant;
import com.liansheng.carworld.net.JsonCallback;
import com.liansheng.carworld.net.NetApi;
import com.liansheng.carworld.net.UrlKit;
import com.liansheng.carworld.oss.OssServiceUtil;
import com.liansheng.carworld.utils.DateParserHelper;
import com.liansheng.carworld.utils.MoneyValueFilter;
import com.liansheng.carworld.utils.Utils;
import com.liansheng.carworld.view.GlideRoundTransform;
import com.liansheng.carworld.view.pop.PayPop;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.entity.LocalMedia;
import com.xgr.alipay.alipay.AliPay;
import com.xgr.alipay.alipay.AlipayInfoImpli;
import com.xgr.easypay.EasyPay;
import com.xgr.easypay.callback.IPayCallback;

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

public class SteamDetailsAct extends BaseActivity implements CompoundButton.OnCheckedChangeListener {

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
    @BindView(R.id.ll_ddxx)
    LinearLayout ll_ddxx;
    @BindView(R.id.ll_ztxx)
    RelativeLayout ll_ztxx;
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
    //订单信息
    @BindView(R.id.tv_htbh)
    TextView tv_htbh;
    @BindView(R.id.tv_cjr)
    TextView tv_cjr;
    @BindView(R.id.tv_mjbzj1)
    TextView tv_mjbzj1;
    @BindView(R.id.tv_mjbzj2)
    TextView tv_mjbzj2;
    @BindView(R.id.tv_dqzt)
    TextView tv_dqzt;
    @BindView(R.id.recycler_status_ll)
    RecyclerView recyclerStatusLl;
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
    @BindView(R.id.btn_ll)
    LinearLayout btn_ll;
    @BindView(R.id.btn_cancel)
    TextView btn_cancel;
    @BindView(R.id.btn_update)
    TextView btn_update;
    @BindView(R.id.btn_confirm)
    TextView btn_confirm;
    @BindView(R.id.btn_pay)
    TextView btn_pay;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    private SteamStatusAdapter statusAdapter;
    private String userCarResourceId;
    private List<ImageUploadBean> uploadBeans = new ArrayList<>();
    private MyAdapter myAdapter;
    private int index = 0;
    private String carUrl = "";
    private PayPop mPayPop;
    private SteamGuaranteeBean steamGuaranteeBean;

    @Override
    public void initData(Bundle savedInstanceState) {
        userCarResourceId = getIntent().getStringExtra(Constant.KEY_ID);
        et_jyzj.setFilters(new InputFilter[]{new MoneyValueFilter()});
        getDetails();
        ll_sale.setEnabled(false);
        ll_buy.setEnabled(false);
        statusAdapter = new SteamStatusAdapter(null);
        recyclerStatusLl.setLayoutManager(new LinearLayoutManager(this));
        recyclerStatusLl.setAdapter(statusAdapter);
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
        mPayPop = new PayPop(this, (view, content) -> {
            goAliPay();
        });
    }

    public void goAliPay() {
        String json = "{\"platformGuaranteeId\":" + userCarResourceId + ",\"payMethod\": \"Alipay\"}";
        NetApi.postJson(UrlKit.APP_PLATFORM_GUARANTEE_PAY, json, new JsonCallback() {

            @Override
            public void onSuccess(String response, int id) {
                AliPay aliPay = new AliPay();
                //构造支付宝订单实体。一般都是由服务端直接返回。
                AlipayInfoImpli alipayInfoImpli = new AlipayInfoImpli();
                alipayInfoImpli.setOrderInfo(response);
                //策略场景类调起支付方法开始支付，以及接收回调。
                EasyPay.pay(aliPay, context, alipayInfoImpli, new IPayCallback() {
                    @Override
                    public void success() {
                        Util.toast("支付成功");
                        finish();
//                        showSuccessDialog(orderId);
                    }

                    @Override
                    public void failed(int code, String msg) {
                        Util.toast(msg);
                    }

                    @Override
                    public void cancel() {
                        Util.toast("支付取消");
                        finish();
                    }
                });
            }
        });
    }

    private void setEditEnable(boolean enable) {
        et_ghcs.setEnabled(enable);
        et_keys.setEnabled(enable);
        et_jyzj.setEnabled(enable);
        et_bzj.setEnabled(enable);
        ck_et_bjbw_t.setEnabled(enable);
        ck_et_pqbw_t.setEnabled(enable);
        ck_et_hjbw_t.setEnabled(enable);
        ck_et_sxbw_t.setEnabled(enable);
        ck_et_lscx_t.setEnabled(enable);
        ck_et_nbms_t.setEnabled(enable);
        ck_et_gjss_t.setEnabled(enable);
        ck_et_gzbw_t.setEnabled(enable);
        ck_et_bjbw_f.setEnabled(enable);
        ck_et_pqbw_f.setEnabled(enable);
        ck_et_hjbw_f.setEnabled(enable);
        ck_et_sxbw_f.setEnabled(enable);
        ck_et_lscx_f.setEnabled(enable);
        ck_et_nbms_f.setEnabled(enable);
        ck_et_gjss_f.setEnabled(enable);
        ck_et_gzbw_f.setEnabled(enable);
        ck_et_ms.setEnabled(enable);
        et_qtyd.setEnabled(enable);
        ck_fdj_t.setEnabled(enable);
        ck_fdj_f.setEnabled(enable);
        ck_jgj_t.setEnabled(enable);
        ck_jgj_f.setEnabled(enable);
        ck_psc_t.setEnabled(enable);
        ck_psc_f.setEnabled(enable);
        ck_hsc_t.setEnabled(enable);
        ck_hsc_f.setEnabled(enable);
        ck_bsx_t.setEnabled(enable);
        ck_bsx_f.setEnabled(enable);
        ck_qn_t.setEnabled(enable);
        ck_qn_f.setEnabled(enable);
        ck_ghzt_t.setEnabled(enable);
        ck_ghzt_f.setEnabled(enable);
        ck_tdgh_t.setEnabled(enable);
        ck_tdgh_f.setEnabled(enable);
        ck_wlxx_t.setEnabled(enable);
        ck_wlxx_f.setEnabled(enable);
    }

    @Override
    public int getLayoutId() {
        return R.layout.act_steam_details;
    }

    @OnClick({R.id.tv_clxq, R.id.tv_ckxx, R.id.tv_clcl, R.id.tv_jyxx, R.id.tv_ddxx, R.id.tv_ztxx, R.id.steam_wbcx, R.id.steam_pzcx,
            R.id.btn_cancel, R.id.btn_confirm, R.id.btn_update, R.id.btn_pay, R.id.ll_sale})
    public void onViewClick(View view) {
        switch (view.getId()) {
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
            case R.id.tv_ddxx:
                ll_ddxx.setVisibility(ll_ddxx.getVisibility() == View.GONE ? View.VISIBLE : View.GONE);
                break;
            case R.id.tv_ztxx:
                ll_ztxx.setVisibility(ll_ztxx.getVisibility() == View.GONE ? View.VISIBLE : View.GONE);
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
            case R.id.btn_cancel:
                MessageDialog.build(this)
                        .setTitle("温馨提示")
                        .setMessage("确定要取消合同吗？")
                        .setCancelButton("取消", new OnDialogButtonClickListener() {
                            @Override
                            public boolean onClick(BaseDialog baseDialog, View v) {
                                return false;
                            }
                        })
                        .setOkButton("确定", new OnDialogButtonClickListener() {
                            @Override
                            public boolean onClick(BaseDialog baseDialog, View v) {
                                cancel();
                                return false;
                            }
                        })
                        .show();
                break;
//            case R.id.btn_update:
//                break;
            case R.id.btn_confirm:
                if (steamGuaranteeBean == null) {
                    return;
                }
                if (steamGuaranteeBean.getUserId().equals(SharedInfo.getInstance().getEntity(UserInfo.class).getId())) {
                    confirmSteam();
                } else {
                    MessageDialog.build(this)
                            .setTitle("温馨提示")
                            .setMessage("是否确认合同吗？(确认后不可修改)")
                            .setCancelButton("取消", new OnDialogButtonClickListener() {
                                @Override
                                public boolean onClick(BaseDialog baseDialog, View v) {
                                    return false;
                                }
                            })
                            .setOkButton("确定", new OnDialogButtonClickListener() {
                                @Override
                                public boolean onClick(BaseDialog baseDialog, View v) {
                                    confirm();
                                    return false;
                                }
                            })
                            .show();
                }
                break;
            case R.id.btn_pay:
                if (!mPayPop.isShowing())
                    mPayPop.showAtLocation(view, Gravity.CENTER, 0, 0);
                break;
        }
    }

    private void confirmSteam() {
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
        SteamGuaranteeUpdateBean guarantee = new SteamGuaranteeUpdateBean();
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
        guarantee.setPlatformGuaranteeId(userCarResourceId);

        MessageDialog.build(this)
                .setTitle("温馨提示")
                .setMessage("是否确认合同吗？(确认后不可修改)")
                .setCancelButton("取消", new OnDialogButtonClickListener() {
                    @Override
                    public boolean onClick(BaseDialog baseDialog, View v) {
                        return false;
                    }
                })
                .setOkButton("确定", new OnDialogButtonClickListener() {
                    @Override
                    public boolean onClick(BaseDialog baseDialog, View v) {
                        update(guarantee);
                        return false;
                    }
                })
                .show();
//        MessageDialog.build(this)
//                .setTitle("温馨提示")
//                .setMessage("是否修改合同？")
//                .setCancelButton("取消", new OnDialogButtonClickListener() {
//                    @Override
//                    public boolean onClick(BaseDialog baseDialog, View v) {
//                        return false;
//                    }
//                })
//                .setOkButton("修改", new OnDialogButtonClickListener() {
//                    @Override
//                    public boolean onClick(BaseDialog baseDialog, View v) {
//                        return false;
//                    }
//                })
//                .show();
    }

    private void update(SteamGuaranteeUpdateBean guarantee) {
        NetApi.put(UrlKit.APP_PLATFORM_GUARANTEE, new Gson().toJson(guarantee), new JsonCallback() {

            @Override
            public void onSuccess(String response, int id) {
//                Util.toast("修改成功");
                confirm();
//                finish();
            }
        });
    }

    private void cancel() {
        NetApi.put(String.format(UrlKit.APP_PLATFORM_GUARANTEE_CANCEL, userCarResourceId), "", new JsonCallback() {

            @Override
            public void onSuccess(String response, int id) {
                Util.toast("操作成功");
                finish();
            }
        });
    }

    private void confirm() {
        NetApi.put(String.format(UrlKit.APP_PLATFORM_GUARANTEE_CONFIRM, userCarResourceId), "", new JsonCallback() {

            @Override
            public void onSuccess(String response, int id) {
                Util.toast("确认成功");
                getDetails();
//                finish();
            }
        });
    }

    private void getDetails() {
        NetApi.get(UrlKit.APP_PLATFORM_GUARANTEE_ID + userCarResourceId, null, new JsonCallback() {

            @Override
            public void onSuccess(String response, int id) {
                steamGuaranteeBean = new Gson().fromJson(response, SteamGuaranteeBean.class);
                setData(steamGuaranteeBean);
            }
        });
    }

    private void setData(SteamGuaranteeBean bean) {
        if (bean.getStatusInfos().size() > 0) {
            for (int i = 0; i < bean.getStatusInfos().size(); i++) {
                SteamGuaranteeBean.StatusInfosBean infosBean = bean.getStatusInfos().get(i);
                infosBean.setShowUp(true);
                infosBean.setShowDown(true);
                if (i == 0) {
                    infosBean.setShowUp(false);
                }
                if (i == bean.getStatusInfos().size() - 1) {
                    infosBean.setShowDown(false);
                }
            }
            statusAdapter.replaceData(bean.getStatusInfos());
        }
        if (bean.getBuyer() != null) {
            tv_buy.setText(bean.getBuyer().getName());
            tv_buy_phone.setText(bean.getBuyer().getMobile());
            if (!TextUtils.isEmpty(bean.getBuyer().getHeadphoto())) {
                Glide.with(this).load(UrlKit.getImgUrl(bean.getBuyer().getHeadphoto())).into(iv_buy_img);
            }
        }
        if (bean.getSeller() != null) {
            tv_sale.setText(bean.getSeller().getName());
            tv_sale_phone.setText(bean.getSeller().getMobile());
            if (!TextUtils.isEmpty(bean.getSeller().getHeadphoto())) {
                Glide.with(this).load(UrlKit.getImgUrl(bean.getSeller().getHeadphoto())).into(iv_sale_img);
            }
        }
        //订单信息
        ck_fdj_t.setChecked(bean.isEngine());
        ck_jgj_t.setChecked(bean.isStructure());
        ck_psc_t.setChecked(!bean.getCarResource().isInWater());//泡水车
        ck_hsc_t.setChecked(!bean.getCarResource().isInFire());//火烧车
        ck_bsx_t.setChecked(bean.isGearbox());
        ck_qn_t.setChecked(bean.isAIRBAG());
        ck_et_bjbw_t.setChecked(bean.isPlate());
        ck_et_pqbw_t.setChecked(bean.isPainting());
        ck_et_hjbw_t.setChecked(bean.isOverhaul());
        ck_et_sxbw_t.setChecked(bean.isRust());
        ck_et_lscx_t.setChecked(bean.isScrew());
        ck_et_nbms_t.setChecked(bean.isInterior());
        ck_et_gjss_t.setChecked(bean.isSkeleton());
        ck_et_gzbw_t.setChecked(bean.isRefit());
        ck_et_ms.setText(bean.getDescribe());
        //交易信息
        ck_ghzt_t.setChecked(bean.isTransfer());
        ck_tdgh_t.setChecked(bean.isFileUp());
        ck_wlxx_t.setChecked(bean.isLogistics());
        et_qtyd.setText(bean.getOtherAgreements());
        //合同
        tv_htbh.setText(bean.getNumber());
        tv_cjr.setText(bean.getUser());
        tv_mjbzj1.setText(bean.isBuyerSecurityFund() ? "已支付" : "未支付");
        tv_mjbzj2.setText(bean.isSellerSecurityFund() ? "已支付" : "未支付");
        UserInfo userInfo = SharedInfo.getInstance().getEntity(UserInfo.class);
        if (!bean.isCancelation()) {
            btn_ll.setVisibility(View.VISIBLE);
            if (userInfo.getId().equals(bean.getUserId())) {
                if (bean.getBuyer() != null && userInfo.getId().equals(bean.getBuyer().getId()) && bean.isBuyerConfirm()) {
                    myAdapter.setOnItemChildClickListener(null);
                    setEditEnable(false);
                    btn_cancel.setVisibility(View.GONE);
                    btn_confirm.setVisibility(View.GONE);
                    btn_pay.setVisibility(View.VISIBLE);
                } else if (bean.getSeller() != null && userInfo.getId().equals(bean.getSeller().getId()) && bean.isSellerConfirm()) {
                    myAdapter.setOnItemChildClickListener(null);
                    setEditEnable(false);
                    btn_cancel.setVisibility(View.GONE);
                    btn_confirm.setVisibility(View.GONE);
                    btn_pay.setVisibility(View.VISIBLE);
                } else {
                    btn_cancel.setVisibility(View.VISIBLE);
//                    btn_update.setVisibility(View.VISIBLE);
                    btn_confirm.setVisibility(View.VISIBLE);
                }
//                if (bean.isBuyerConfirm() && bean.isSellerConfirm()) {
//                    if (bean.isBuyerSecurityFund() && bean.isSellerSecurityFund()) {
//                        btn_ll.setVisibility(View.GONE);
//                    } else {
//                        btn_pay.setVisibility(View.VISIBLE);
//                    }
//                }
            } else {
                setEditEnable(false);
                myAdapter.setOnItemChildClickListener(null);
                if (bean.isBuyerConfirm() && bean.isSellerConfirm()) {
                    btn_confirm.setVisibility(View.GONE);
                    if (bean.isBuyerSecurityFund() && bean.isSellerSecurityFund()) {
                        btn_ll.setVisibility(View.GONE);
                    } else {
                        btn_pay.setVisibility(View.VISIBLE);
                    }
                } else {
                    btn_confirm.setVisibility(View.VISIBLE);
                }
            }
        } else {
            setEditEnable(false);
            myAdapter.setOnItemChildClickListener(null);
        }
        et_keys.setText(String.valueOf(bean.getKeys()));
        et_jyzj.setText(StringFormat.doubleFormatForW(bean.getTotal()).replace("万", ""));
        et_bzj.setText(String.valueOf(bean.getSecurityFund()));
        List<String> images = bean.getMaterials();
        if (images != null && images.size() > 0) {
            for (int i = 0; i < images.size(); i++) {
                changeImage(i, images.get(i));
            }
        }
        setCarData(bean.getCarResource());
    }

    //车辆信息
    private void setCarData(SaleCardBean bean) {
        //基本信息
        tv_car.setText(bean.getBrand_fct() + " " + bean.getBrand_serie());
        if (bean.getRegistration_date() != null && bean.getRegistration_date().length() > 4) {
            tv_nf.setText(bean.getRegistration_date().substring(0, 4) + "年 | " + StringFormat.doubleFormatForW2(bean.getMileage()) + "公里");
        } else {
            tv_nf.setText(bean.getRegistration_date() + "年 | " + StringFormat.doubleFormatForW2(bean.getMileage()) + "公里");
        }
        tv_szd.setText(bean.getLocation());
        tv_jg.setText(StringFormat.doubleFormatForW2(bean.getSell_price()));
        if (TextUtils.isEmpty(bean.getUpdateDate())) {
            tv_pass.setText("");
        } else {
            tv_pass.setText(DateParserHelper.getYearAndMonthAndDay2(bean.getUpdateDate()) + "发布");
        }
        //详情信息
        tv_xcj.setText(String.valueOf(bean.getGuidance_price()));
        tv_ys.setText(bean.getColor());
        tvPl.setText(bean.getVolume());
        tv_pf.setText(bean.getStandard());
        et_gls.setText(StringFormat.doubleFormatForW2(bean.getMileage()) + "公里");
//        et_csjg.setText(String.valueOf(bean.getSell_price()));
//        tvGh.setText(String.valueOf(bean.getTransfer_times()));
        tvSprq.setText(DateParserHelper.getYearMonthAndDay(bean.getRegistration_date()));
//        tvScrq.setText(DateParserHelper.getYearMonthAndDay(bean.getProduction_date()));
        tvNjdq.setText(DateParserHelper.getYearMonthAndDay(bean.getYearlyInspectionExpiration()));
        tvQxdq.setText(DateParserHelper.getYearMonthAndDay(bean.getCompulsoryInsuranceExpiration()));
        tv_vin.setText(bean.getVin());
        tvSzd2.setText(bean.getLocation());
        tvSpd.setText(bean.getRegistration_city());
        et_ghcs.setText(String.valueOf(bean.getTransfer_times()));
//        tvSjsj.setText(DateParserHelper.getYearMonthAndDay(bean.getPassedDate()));
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
                }else {
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
