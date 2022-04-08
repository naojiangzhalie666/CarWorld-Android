package com.liansheng.carworld.activity.me;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.liansheng.carworld.R;
import com.liansheng.carworld.activity.BaseActivity;
import com.liansheng.carworld.adapter.me.InvestAdapter;
import com.liansheng.carworld.bean.me.InvestBean;
import com.liansheng.carworld.net.JsonCallback;
import com.liansheng.carworld.net.NetApi;
import com.liansheng.carworld.bean.other.PageBean;
import com.liansheng.carworld.net.UrlKit;
import com.xgr.alipay.alipay.AliPay;
import com.xgr.alipay.alipay.AlipayInfoImpli;
import com.xgr.easypay.EasyPay;
import com.xgr.easypay.callback.IPayCallback;

import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.droidlover.xdroid.tools.utils.Util;
import okhttp3.Call;

public class InvestActivity extends BaseActivity {

    @BindView(R.id.btn_back)
    ImageView btnBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.main)
    LinearLayout main;
    @BindView(R.id.invest_list)
    RecyclerView investList;
    @BindView(R.id.img_ico)
    ImageView imgIco;
    @BindView(R.id.iv_alipay)
    ImageView ivAlipay;
    @BindView(R.id.llyt_alipay)
    LinearLayout llytAlipay;
    private InvestAdapter mAdapter;

    @Override
    public void initData(Bundle savedInstanceState) {
        tvTitle.setText("充值");
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        initView();
        getList();
    }

    private void initView() {
        mAdapter = new InvestAdapter(null);
        mAdapter.setOnItemClickListener((adapter, view, position) -> {
            List<InvestBean> data = adapter.getData();
            for (int i = 0; i < data.size(); i++) {
                if (position == i) {
                    data.get(i).setSelected(true);
                } else {
                    data.get(i).setSelected(false);
                }
            }
            adapter.notifyDataSetChanged();

        });
        investList.setLayoutManager(new GridLayoutManager(this, 3));
        investList.setAdapter(mAdapter);
    }

    private void getList() {
        NetApi.get(UrlKit.RECHARGE_VIP_PRICE, null, new JsonCallback() {

            @Override
            public void onSuccess(String response, int id) {
                PageBean<InvestBean> pageBean = new Gson().fromJson(response, new TypeToken<PageBean<InvestBean>>() {
                }.getType());
                List<InvestBean> items = pageBean.getItems();
                if (items != null && items.size() > 0) {
                    items.get(0).setSelected(true);
                }
                mAdapter.replaceData(items);
            }
        });
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_invest;
    }


    @OnClick(R.id.btn_go)
    public void onClick(View view) {
        List<InvestBean> beans = mAdapter.getData();
        for (int i = 0; i < beans.size(); i++) {
            if (beans.get(i).isSelected()) {
                HashMap<String, Object> hasMap = new HashMap<>();
                hasMap.put("vipPriceId", beans.get(i).getId());
                NetApi.post(UrlKit.RECHARGE_VIP_ORDER, hasMap, new JsonCallback() {

                    @Override
                    public void onSuccess(String response, int id) {
                        HashMap<String, String> map = new Gson().fromJson(response, HashMap.class);
                        String orderId = map.get("vipOrderId");
                        if (!TextUtils.isEmpty(orderId)) {
                            goAliPay(orderId);
                        }
                    }
                });
                break;
            }
        }
    }

    public void goAliPay(String orderId) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("vipOrderId", orderId);
        NetApi.get(UrlKit.RECHARGE_VIP_ORDER_PAY, hashMap, new JsonCallback() {

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

}
