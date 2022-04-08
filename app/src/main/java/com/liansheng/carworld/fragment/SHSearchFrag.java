package com.liansheng.carworld.fragment;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.view.Gravity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.baidu.ocr.ui.camera.CameraActivity;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.liansheng.carworld.R;
import com.liansheng.carworld.activity.WebViewAct;
import com.liansheng.carworld.activity.logic.OtherLogic;
import com.liansheng.carworld.bean.ScanVinBean;
import com.liansheng.carworld.bean.company.AccidentBean;
import com.liansheng.carworld.bean.other.MtnceBean;
import com.liansheng.carworld.kit.Constant;
import com.liansheng.carworld.net.JsonCallback;
import com.liansheng.carworld.net.NetApi;
import com.liansheng.carworld.net.UrlKit;
import com.liansheng.carworld.oss.OssServiceUtil;
import com.liansheng.carworld.utils.Utils;
import com.liansheng.carworld.view.pop.PayPop;
import com.liansheng.carworld.wxapi.RecognizeService;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.xgr.alipay.alipay.AliPay;
import com.xgr.alipay.alipay.AlipayInfoImpli;
import com.xgr.easypay.EasyPay;
import com.xgr.easypay.callback.IPayCallback;

import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import cn.droidlover.xdroid.tools.utils.ActivityManage;
import cn.droidlover.xdroid.tools.utils.FileUtil;
import cn.droidlover.xdroid.tools.utils.Util;
import okhttp3.Call;

public class SHSearchFrag extends BaseFragment {

    private static final int REQUEST_CODE_VINCODE = 134;

    @BindView(R.id.search_vin_number)
    EditText search_vin_number;
    @BindView(R.id.search_img)
    ImageView search_img;
    @BindView(R.id.search_all_ll)
    LinearLayout searchAllLl;
    @BindView(R.id.search_all)
    Switch search_all;
    @BindView(R.id.search_regular)
    CheckBox search_regular;
    private String shPath;
    PayPop mPayPop;
    @BindView(R.id.tv_tip)
    TextView tv_tip;

    private int mType;
    private AccidentBean accidentBean;

    public static SHSearchFrag newInstance(int type, String vin) {
        Bundle args = new Bundle();
        args.putInt(Constant.KEY_TYPE, type);
        args.putString(Constant.KEY_TITLE, vin);
        SHSearchFrag fragment = new SHSearchFrag();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        super.initData(savedInstanceState);
        if (getArguments() != null) {
            mType = getArguments().getInt(Constant.KEY_TYPE, 1);
            String vin = getArguments().getString(Constant.KEY_TITLE);
            if (!TextUtils.isEmpty(vin)) {
                search_vin_number.setText(vin);
            }
        }
        mPayPop = new PayPop(getActivity(), (view, content) -> {
//            if (mType == 1) {
//                searchMtnce(search_vin_number.getText().toString(), path);
//            } else if (mType == 2) {
//                searchInsurance(search_vin_number.getText().toString(), path);
//            }
            goAliPay();

        });
//        if (mType == 1 || mType == 2) {
//            searchAllLl.setVisibility(View.VISIBLE);
//        }
        tv_tip.setText("1 、查询服务：车无界拥有查询VIN信息版块，维保查询 11 元起／条，碰撞查询 19 元起／条。\n\n2 、查询分类：维保记录分为普通品牌，特殊品牌,人工品牌询，由于上游费用不同，可能造成价格有所不同。");
        Spannable span = new SpannableString(tv_tip.getText());
        span.setSpan(new AbsoluteSizeSpan(48), 28, 30, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        span.setSpan(new ForegroundColorSpan(Color.RED), 28, 30, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        tv_tip.setText(span);
        Spannable span2 = new SpannableString(tv_tip.getText());
        span2.setSpan(new AbsoluteSizeSpan(48), 41, 43, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        span2.setSpan(new ForegroundColorSpan(Color.RED), 41, 43, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        tv_tip.setText(span2);
    }

    public void goAliPay() {
        String url;
        if (mType == 1) {
            url = UrlKit.APP_MTNCE_ORDER_PAY;
        } else {
            url = UrlKit.APP_INSURANCE_ORDER_PAY;
        }
        String json = "{\"id\":" + accidentBean.getId() + ",\"payMethod\": \"Alipay\"}";
        NetApi.postJson(url, json, new JsonCallback() {

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
                        Util.toast("支付成功，请等待查询结果");
                        getActivity().finish();
//                        showSuccessDialog(orderId);
                    }

                    @Override
                    public void failed(int code, String msg) {
                        Util.toast(msg);
                    }

                    @Override
                    public void cancel() {
                        Util.toast("支付取消");
                    }
                });
            }
        });
    }

    @Override
    public int getLayoutId() {
        return R.layout.frag_second_hand_search;
    }

    @OnClick({R.id.search_scan, R.id.search_img, R.id.search_regular_txt, R.id.submit})
    public void onViewClick(View view) {
        switch (view.getId()) {
            case R.id.search_scan:
                Intent intent = new Intent(context, CameraActivity.class);
                intent.putExtra(CameraActivity.KEY_OUTPUT_FILE_PATH, FileUtil.getSaveFile(context).getAbsolutePath());
                intent.putExtra(CameraActivity.KEY_CONTENT_TYPE, CameraActivity.CONTENT_TYPE_GENERAL);
                startActivityForResult(intent, REQUEST_CODE_VINCODE);
                break;
            case R.id.search_img:
                Utils.getPhoto(getActivity());
                break;
            case R.id.search_regular_txt:
                Intent intent2 = new Intent();
                intent2.putExtra(Constant.KEY_TITLE, "查询服务协议");
                intent2.putExtra(Constant.KEY_URL, UrlKit.H5_SEARCH_SERVICE_REGULAR);
                ActivityManage.push(WebViewAct.class, intent2);
                break;
            case R.id.submit:
                String vinNumber = search_vin_number.getText().toString();
                if (TextUtils.isEmpty(vinNumber)) {
                    Util.toast("请输入车架号");
                    return;
                }
                if (TextUtils.isEmpty(shPath)) {
                    Util.toast("请上传行驶证");
                    return;
                }
                if (!search_regular.isChecked()) {
                    Util.toast("请同意服务条款");
                    return;
                }
                if (mType == 1) {
                    checkVin(search_vin_number.getText().toString(), "Mtnce");
                } else if (mType == 2) {
                    checkVin(search_vin_number.getText().toString(), "Insurance");
                }
                break;
        }
    }

    private void checkVin(String vin, String type) {
        NetApi.get(String.format(UrlKit.PRICE_CAR_CONDITION, type, vin), null, new JsonCallback() {

            @Override
            public void onSuccess(String response, int id) {
                Map<String, Object> map = new Gson().fromJson(response, Map.class);
                if (map != null) {
                    boolean valid = (boolean) map.get("valid");
                    if (valid) {
                        search(search_vin_number.getText().toString(), shPath);
                    } else {
                        Util.toast("无效的vin");
                    }
                }
            }
        });
    }

    //1 维保 2 碰撞
    private void search(String vin, String drivingLicense) {
        MtnceBean bean = new MtnceBean();
        bean.setVin(vin);
        bean.setDrivingLicense(drivingLicense);
        String url = "";
        if (mType == 1) {
            url = UrlKit.APP_MTNCE_ORDER;
        } else if (mType == 2) {
            url = UrlKit.APP_INSURANCE_ORDER;
        }
        NetApi.postJson(url, new Gson().toJson(bean), new JsonCallback() {

            @Override
            public void onSuccess(String response, int id) {
                accidentBean = new Gson().fromJson(response, AccidentBean.class);
                mPayPop.setAmount(accidentBean.getPrice());
                mPayPop.showAtLocation(search_vin_number, Gravity.CENTER, 0, 0);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && requestCode == PictureConfig.CHOOSE_REQUEST) {
            String path = PictureSelector.obtainMultipleResult(data).get(0).getCompressPath();
            OssServiceUtil.getInstance().asyncPutImage(OtherLogic.getImgUrl(), path, (objectResult, currentPath) -> {
                shPath = currentPath;
                Glide.with(ActivityManage.peek()).load(UrlKit.getImgUrl(shPath)).into(search_img);
            });
        } else if (requestCode == REQUEST_CODE_VINCODE && resultCode == Activity.RESULT_OK) {
            // 识别成功回调，VIN码
            RecognizeService.recognizeVincode(context, FileUtil.getSaveFile(context).getAbsolutePath(),
                    result -> {
                        if (!TextUtils.isEmpty(result)) {
                            ScanVinBean vinBean = new Gson().fromJson(result, ScanVinBean.class);
                            if (vinBean != null && vinBean.getWords_result() != null && vinBean.getWords_result().size() > 0
                                    && !TextUtils.isEmpty(vinBean.getWords_result().get(0).getWords())) {
                                search_vin_number.setText(vinBean.getWords_result().get(0).getWords());
                            }
                        }
                    });
        }
    }

}
