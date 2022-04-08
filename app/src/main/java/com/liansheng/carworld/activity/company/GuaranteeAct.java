package com.liansheng.carworld.activity.company;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DividerItemDecoration;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.liansheng.carworld.R;
import com.liansheng.carworld.activity.BaseActivity;
import com.liansheng.carworld.activity.home.SteamDetailsAct;
import com.liansheng.carworld.adapter.company.GuaranteeAdapter;
import com.liansheng.carworld.bean.company.SteamGuaranteeBean;
import com.liansheng.carworld.bean.other.PageBean;
import com.liansheng.carworld.kit.Constant;
import com.liansheng.carworld.net.JsonCallback;
import com.liansheng.carworld.net.NetApi;
import com.liansheng.carworld.net.UrlKit;
import com.liansheng.carworld.utils.ViewUtils;

import java.util.HashMap;

import butterknife.BindView;
import cn.droidlover.xdroid.tools.utils.ActivityManage;
import cn.droidlover.xdroid.views.appbar.ToolBar;
import cn.droidlover.xrecyclerview.XRecyclerContentLayout;
import cn.droidlover.xrecyclerview.XRecyclerView;
import okhttp3.Call;

public class GuaranteeAct extends BaseActivity {

    @BindView(R.id.toolbar)
    ToolBar mToolbar;
    @BindView(R.id.recycler_view)
    XRecyclerContentLayout contentLayout;
    private GuaranteeAdapter mAdapter;

    @Override
    public void initData(Bundle savedInstanceState) {
        mToolbar.setTitle("我的合同");
        mToolbar.setRightTextAction(0, R.color.order);
        initAdapter();
    }

    private void initAdapter() {
        mAdapter = new GuaranteeAdapter(null);
        mAdapter.addChildClickViewIds(R.id.find_edit, R.id.find_down);
        contentLayout.getRecyclerView().verticalLayoutManager(context);
        contentLayout.getRecyclerView().setAdapter(mAdapter);
        contentLayout.getSwipeRefreshLayout().setColorSchemeResources(R.color.order, R.color.order2, R.color.end);
        contentLayout.emptyView(ViewUtils.getEmptyView(this, R.mipmap.bg_bill_none));
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
                SteamGuaranteeBean item = (SteamGuaranteeBean) adapter.getItem(position);
                Intent intent = new Intent();
                intent.putExtra(Constant.KEY_ID, String.valueOf(item.getId()));
                ActivityManage.push(SteamDetailsAct.class, intent);
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
        NetApi.get(UrlKit.APP_PLATFORM_GUARANTEE, map, new JsonCallback() {

            @Override
            public void onSuccess(String response, int id) {
                Gson gson = new Gson();
                PageBean<SteamGuaranteeBean> data = gson.fromJson(response, new TypeToken<PageBean<SteamGuaranteeBean>>() {
                }.getType());
                if (page == 0) {
                    mAdapter.getData().clear();
                }
                if (data.getItems().size() > 0) {
                    mAdapter.replaceData(data.getItems());
                } else {
                    if (page == 0) {
                        contentLayout.showEmpty();
                        return;
                    }
                }
                if (data.getItems().size() < Constant.DEFULT_RESULT) {
                    MAX_PAGE = page;
                } else {
                    MAX_PAGE++;
                }
                contentLayout.getRecyclerView().setPage(page, MAX_PAGE);
            }
        });
    }
}
