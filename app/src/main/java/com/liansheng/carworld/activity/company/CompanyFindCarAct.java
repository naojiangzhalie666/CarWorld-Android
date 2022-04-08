package com.liansheng.carworld.activity.company;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.kongzue.dialog.v3.MessageDialog;
import com.liansheng.carworld.R;
import com.liansheng.carworld.activity.BaseActivity;
import com.liansheng.carworld.activity.home.FindCarActivity;
import com.liansheng.carworld.activity.home.FindCarDetailsAct;
import com.liansheng.carworld.activity.home.SaleCarDetailsAct;
import com.liansheng.carworld.activity.logic.OtherLogic;
import com.liansheng.carworld.adapter.company.CarManagerAdapter;
import com.liansheng.carworld.adapter.company.CompanyFindCarAdapter;
import com.liansheng.carworld.bean.UserInfo;
import com.liansheng.carworld.bean.home.FindCardBean;
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
import cn.droidlover.xdroid.views.appbar.TitleBar;
import cn.droidlover.xdroid.views.appbar.ToolBar;
import cn.droidlover.xrecyclerview.XRecyclerContentLayout;
import cn.droidlover.xrecyclerview.XRecyclerView;
import okhttp3.Call;

public class CompanyFindCarAct extends BaseActivity {

    @BindView(R.id.toolbar)
    ToolBar mToolbar;
    @BindView(R.id.recycler_view)
    XRecyclerContentLayout contentLayout;
    private CompanyFindCarAdapter mAdapter;

    @Override
    public void initData(Bundle savedInstanceState) {
        mToolbar.setTitle("我的求购");
        mToolbar.addAction(new TitleBar.TextAction("历史记录") {
            @Override
            public void performAction(View view) {
                ActivityManage.push(CompanyFindCarHistoryAct.class);
            }
        });
        mToolbar.setRightTextAction(0, R.color.order);
        initAdapter();
    }

    private void initAdapter() {
        mAdapter = new CompanyFindCarAdapter(null, 1);
        mAdapter.addChildClickViewIds(R.id.find_edit, R.id.find_down);
        mAdapter.setOnItemChildClickListener((adapter, view, position) -> {
            FindCardBean item = (FindCardBean) adapter.getItem(position);
            switch (view.getId()) {
//                case R.id.find_edit:
//                    Intent intent = new Intent();
//                    intent.putExtra(Constant.KEY_BEAN, item);
//                    ActivityManage.push(FindCarActivity.class, intent);
//                    break;
                case R.id.find_down:
                    MessageDialog.build(CompanyFindCarAct.this)
                            .setTitle("温馨提示")
                            .setMessage("您是否确定下架？")
                            .setCancelButton("取消", (baseDialog, v) -> false)
                            .setOkButton("确定", (baseDialog, v) -> {
                                carOff(item.getId());
                                return false;
                            })
                            .show();
                    break;
            }
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
        mAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull BaseQuickAdapter adapter, @NonNull View view, int position) {
                FindCardBean item = (FindCardBean) adapter.getItem(position);
                Intent intent = new Intent();
                intent.putExtra(Constant.KEY_ID, item.getId());
                ActivityManage.push(FindCarDetailsAct.class, intent);
            }
        });
        contentLayout.loadingView(View.inflate(this, R.layout.view_loading, null));
        contentLayout.getRecyclerView().useDefLoadMoreView();
        contentLayout.getRecyclerView().addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        contentLayout.getRecyclerView().setBackgroundResource(R.color.white);
        getData(0);
    }


    @Override
    public int getLayoutId() {
        return R.layout.common_list;
    }


    private void carOff(String id) {
        NetApi.put(UrlKit.CAR_RESOURCE_ORDER_OFF + id, "", new JsonCallback() {

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
        map.put("userId", SharedInfo.getInstance().getEntity(UserInfo.class).getId());
        NetApi.get(UrlKit.CAR_RESOURCE_BRAND_ORDER, map, new JsonCallback() {

            @Override
            public void onSuccess(String response, int id) {
                PageBean<FindCardBean> data = new Gson().fromJson(response, new TypeToken<PageBean<FindCardBean>>() {
                }.getType());
                OtherLogic.updateAdapter(page, mAdapter, data, contentLayout);
            }
        });
    }
}
