package com.liansheng.carworld.activity.me;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DividerItemDecoration;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.kongzue.dialog.v3.MessageDialog;
import com.liansheng.carworld.R;
import com.liansheng.carworld.activity.BaseActivity;
import com.liansheng.carworld.activity.company.CompanyFindCarAct;
import com.liansheng.carworld.activity.company.CompanyFindCarHistoryAct;
import com.liansheng.carworld.activity.home.CarSelectAct;
import com.liansheng.carworld.activity.home.FindCarDetailsAct;
import com.liansheng.carworld.activity.logic.OtherLogic;
import com.liansheng.carworld.adapter.company.CompanyFindCarAdapter;
import com.liansheng.carworld.adapter.me.CouponAdapter;
import com.liansheng.carworld.bean.UserInfo;
import com.liansheng.carworld.bean.home.FindCardBean;
import com.liansheng.carworld.bean.me.CouponBean;
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
import cn.droidlover.xdroid.views.appbar.ToolBar;
import cn.droidlover.xrecyclerview.XRecyclerContentLayout;
import cn.droidlover.xrecyclerview.XRecyclerView;

public class CouponAct extends BaseActivity {

    @BindView(R.id.toolbar)
    ToolBar mToolbar;
    @BindView(R.id.recycler_view)
    XRecyclerContentLayout contentLayout;
    private CouponAdapter mAdapter;

    @Override
    public void initData(Bundle savedInstanceState) {
        mToolbar.setTitle("卡券");
        mToolbar.setRightTextAction(0, R.color.order);
        initAdapter();
    }

    private void initAdapter() {
        mAdapter = new CouponAdapter();
        mAdapter.addChildClickViewIds(R.id.item_use);
        mAdapter.setOnItemChildClickListener((adapter, view, position) -> {
            CouponBean item = (CouponBean) adapter.getItem(position);
            if (getIntent().getIntExtra(Constant.KEY_TYPE, 0) == 2) {
                Intent intent = new Intent();
                intent.putExtra(Constant.KEY_BEAN, item);
                setResult(Activity.RESULT_OK, intent);
            } else {
                ActivityManage.push(CarSelectAct.class);
            }
            finish();
        });
        contentLayout.getRecyclerView().verticalLayoutManager(context);
        contentLayout.getRecyclerView().setAdapter(mAdapter);
        contentLayout.getSwipeRefreshLayout().setColorSchemeResources(R.color.order, R.color.order2, R.color.end);
        contentLayout.emptyView(ViewUtils.getEmptyView(this, R.mipmap.bg_cy));
        contentLayout.getRecyclerView().setOnRefreshAndLoadMoreListener(new XRecyclerView.OnRefreshAndLoadMoreListener() {
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
        contentLayout.getRecyclerView().addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        contentLayout.getRecyclerView().setBackgroundResource(R.color.white);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getData(0);
    }

    @Override
    public int getLayoutId() {
        return R.layout.common_list;
    }

    private void getData(int page) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("pageIndex", page);
        map.put("pageSize", Constant.DEFULT_RESULT);
        map.put("expired", false);
        map.put("used", false);
        NetApi.get(UrlKit.USER_COUPON, map, new JsonCallback() {

            @Override
            public void onSuccess(String response, int id) {
                PageBean<CouponBean> data = new Gson().fromJson(response, new TypeToken<PageBean<CouponBean>>() {
                }.getType());
                OtherLogic.updateAdapter(page, mAdapter, data, contentLayout);
            }
        });
    }
}
