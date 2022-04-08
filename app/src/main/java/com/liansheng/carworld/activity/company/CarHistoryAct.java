package com.liansheng.carworld.activity.company;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.kongzue.dialog.v3.MessageDialog;
import com.liansheng.carworld.R;
import com.liansheng.carworld.activity.BaseActivity;
import com.liansheng.carworld.activity.home.SaleCarDetailsAct;
import com.liansheng.carworld.activity.logic.OtherLogic;
import com.liansheng.carworld.adapter.company.CarHistoryAdapter;
import com.liansheng.carworld.bean.UserInfo;
import com.liansheng.carworld.bean.home.SaleCardBean;
import com.liansheng.carworld.bean.other.PageBean;
import com.liansheng.carworld.kit.Constant;
import com.liansheng.carworld.net.JsonCallback;
import com.liansheng.carworld.net.NetApi;
import com.liansheng.carworld.net.UrlKit;
import com.liansheng.carworld.utils.ViewUtils;

import java.util.HashMap;

import butterknife.BindView;
import cn.droidlover.xdroid.hold.info.SharedInfo;
import cn.droidlover.xdroid.tools.utils.ActivityManage;
import cn.droidlover.xdroid.tools.utils.Util;
import cn.droidlover.xdroid.views.appbar.ToolBar;
import cn.droidlover.xdroid.views.recyclerView.DividerLine;
import cn.droidlover.xrecyclerview.XRecyclerContentLayout;
import cn.droidlover.xrecyclerview.XRecyclerView;
import okhttp3.Call;

public class CarHistoryAct extends BaseActivity {

    @BindView(R.id.et_search)
    EditText etSearch;
    @BindView(R.id.toolbar)
    ToolBar toolBar;
    @BindView(R.id.recycler_view)
    XRecyclerContentLayout contentLayout;
    private CarHistoryAdapter mAdapter;
    private String searchContent;


    @Override
    public void initData(Bundle savedInstanceState) {
        toolBar.setTitle("已售记录");
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (TextUtils.isEmpty(s)) {
                    searchContent = "";
                } else {
                    searchContent = s.toString();
                }
                getData(0);
            }
        });
        initAdapter();
        getData(0);
    }

    @Override
    public int getLayoutId() {
        return R.layout.act_car_histroy;
    }


    private void initAdapter() {
        mAdapter = new CarHistoryAdapter(null);
        mAdapter.addChildClickViewIds(R.id.tv_delete, R.id.tv_recover);
        mAdapter.setOnItemChildClickListener((adapter, view, position) -> {
            SaleCardBean item = (SaleCardBean) adapter.getItem(position);
            if (view.getId() == R.id.tv_delete) {
                MessageDialog.build(CarHistoryAct.this)
                        .setTitle("温馨提示")
                        .setMessage("您是否要删除？（删除后不可恢复!）")
                        .setCancelButton("取消", (baseDialog, v) -> false)
                        .setOkButton("确定", (baseDialog, v) -> {
                            deleteCar(item.getId());
                            return false;
                        })
                        .show();
            } else if (view.getId() == R.id.tv_recover) {
                MessageDialog.build(CarHistoryAct.this)
                        .setTitle("温馨提示")
                        .setMessage("是否进行恢复？")
                        .setCancelButton("取消", (baseDialog, v) -> false)
                        .setOkButton("确定", (baseDialog, v) -> {
                            recover(item.getId());
                            return false;
                        })
                        .show();
            }

        });
        contentLayout.getRecyclerView().verticalLayoutManager(context);
        contentLayout.getRecyclerView().setAdapter(mAdapter);
        contentLayout.getRecyclerView().addItemDecoration(new DividerLine(DividerLine.HORIZONTAL));
        mAdapter.setOnItemClickListener((adapter, view, position) -> {
            SaleCardBean item = (SaleCardBean) adapter.getItem(position);
            Intent intent = new Intent();
            intent.putExtra(Constant.KEY_BEAN, item);
            ActivityManage.push(SaleCarDetailsAct.class, intent);
        });
        contentLayout.getSwipeRefreshLayout().setColorSchemeResources(R.color.order, R.color.order2, R.color.end);
        contentLayout.emptyView(ViewUtils.getEmptyView(this, R.mipmap.bg_bill_none));
        contentLayout.getRecyclerView()
                .setOnRefreshAndLoadMoreListener(new XRecyclerView.OnRefreshAndLoadMoreListener() {
                    @Override
                    public void onRefresh() {
                        getData(0);
                    }

                    @Override
                    public void onLoadMore(int page) {
                        getData(page);
                    }
                });

        contentLayout.loadingView(View.inflate(this, R.layout.view_loading, null));
        contentLayout.getRecyclerView().useDefLoadMoreView();
//        contentLayout.getRecyclerView().addItemDecoration(new DividerItemDecoration(getBaseActivity(), DividerItemDecoration.VERTICAL));
    }

    private void recover(String id) {
        NetApi.put(UrlKit.CAR_RESOURCE_SOLD + id + "?sold=false&off=true", "", new JsonCallback() {

            @Override
            public void onSuccess(String response, int id) {
                Util.toast("操作成功");
                getData(0);
            }
        });
    }

    private void deleteCar(String id) {
        NetApi.delete(UrlKit.CAR_RESOURCE_DETAILS + id, null, new JsonCallback() {

            @Override
            public void onSuccess(String response, int id) {
                Util.toast("操作成功");
                getData(0);
            }
        });
    }

    private void getData(int page) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("pageIndex", page);
        map.put("pageSize", Constant.DEFULT_RESULT);
        if (!TextUtils.isEmpty(searchContent)) {
            map.put("keywords", searchContent);
        }
        map.put("sold", true);
        NetApi.get(UrlKit.CAR_RESOURCE, map, new JsonCallback() {

            @Override
            public void onSuccess(String response, int id) {
                Gson gson = new Gson();
                PageBean<SaleCardBean> data = gson.fromJson(response, new TypeToken<PageBean<SaleCardBean>>() {
                }.getType());
                OtherLogic.updateAdapter(page, mAdapter, data, contentLayout);
            }
        });
    }
}
