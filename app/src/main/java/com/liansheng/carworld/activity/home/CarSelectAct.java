package com.liansheng.carworld.activity.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.liansheng.carworld.R;
import com.liansheng.carworld.activity.BaseActivity;
import com.liansheng.carworld.activity.WebViewBtnAct;
import com.liansheng.carworld.adapter.home.InsuranceAdapter;
import com.liansheng.carworld.bean.company.InsuranceBean;
import com.liansheng.carworld.kit.Constant;
import com.liansheng.carworld.net.JsonCallback;
import com.liansheng.carworld.net.NetApi;
import com.liansheng.carworld.net.UrlKit;

import butterknife.BindView;
import butterknife.OnClick;
import cn.droidlover.xdroid.tools.utils.ActivityManage;
import cn.droidlover.xdroid.views.recyclerView.DividerLine;
import okhttp3.Call;

public class CarSelectAct extends BaseActivity {


    @BindView(R.id.insurance_list)
    RecyclerView insuranceList;

    private InsuranceAdapter insuranceAdapter;

    @Override
    public void initData(Bundle savedInstanceState) {
        insuranceList.setLayoutManager(new LinearLayoutManager(this));
        insuranceAdapter = new InsuranceAdapter();
        insuranceList.addItemDecoration(new DividerLine(DividerLine.HORIZONTAL));
        insuranceAdapter.setOnItemClickListener((adapter, view, position) -> {
            InsuranceBean.SchemasBean.ItemsBean item = (InsuranceBean.SchemasBean.ItemsBean) adapter.getItem(position);
            Intent intent = new Intent();
            intent.putExtra(Constant.KEY_TITLE, "");
            intent.putExtra(Constant.KEY_URL, UrlKit.getUrl(item.getUrl()));
            ActivityManage.push(WebViewBtnAct.class, intent);
        });
        insuranceList.setAdapter(insuranceAdapter);
        getData();
    }

    @Override
    public int getLayoutId() {
        return R.layout.act_car_select;
    }


    @OnClick({R.id.car_small, R.id.car_big, R.id.btn_1, R.id.btn_2, R.id.btn_3})
    public void onViewClick(View view) {
        Intent intent = new Intent();
//        intent.putExtra(Constant.KEY_START, (AddressBean) getIntent().getParcelableExtra(Constant.KEY_START));
//        intent.putExtra(Constant.KEY_END, (AddressBean) getIntent().getParcelableExtra(Constant.KEY_END));
        switch (view.getId()) {
//            case R.id.car_small:
//                ActivityManage.push(CarSmallSelectAct.class, intent);
//                break;
            case R.id.car_small:
                ActivityManage.push(TransporterActivity.class, intent);
                break;
            case R.id.car_big:
                intent.putExtra(Constant.KEY_TYPE, -1);
                ActivityManage.push(TransporterActivity.class, intent);
                break;
            case R.id.btn_1:
                intent.putExtra(Constant.KEY_TITLE, "救援车货运险方案①");
                intent.putExtra(Constant.KEY_URL, UrlKit.H5_INSURANCE_CAR_SMALL_1);
                intent.putExtra(Constant.KEY_TYPE, 1);
                ActivityManage.push(WebViewBtnAct.class, intent);
                break;
            case R.id.btn_2:
                intent.putExtra(Constant.KEY_TITLE, "救援车货运险方案②");
                intent.putExtra(Constant.KEY_URL, UrlKit.H5_INSURANCE_CAR_SMALL_2);
                intent.putExtra(Constant.KEY_TYPE, 2);
                ActivityManage.push(WebViewBtnAct.class, intent);
                break;
            case R.id.btn_3:
                intent.putExtra(Constant.KEY_TITLE, "轿运车货运险方案");
                intent.putExtra(Constant.KEY_URL, UrlKit.H5_INSURANCE_CAR_BIG);
                intent.putExtra(Constant.KEY_TYPE, 3);
                ActivityManage.push(WebViewBtnAct.class, intent);
                break;
        }
    }


    private void getData() {
        NetApi.get(UrlKit.APP_RESOURCES_INSURANCE, null, new JsonCallback() {

            @Override
            public void onSuccess(String response, int id) {
                InsuranceBean bean = new Gson().fromJson(response, InsuranceBean.class);
                InsuranceBean.SchemasBean schemasBean = bean.getSchemas().get(0);
                insuranceAdapter.setNewData(schemasBean.getItems());
            }
        });
    }

}
