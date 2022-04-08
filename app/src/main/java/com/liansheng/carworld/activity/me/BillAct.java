package com.liansheng.carworld.activity.me;

import android.os.Bundle;
import android.view.View;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.liansheng.carworld.R;
import com.liansheng.carworld.activity.BaseActivity;
import com.liansheng.carworld.adapter.me.BillAdapter;
import com.liansheng.carworld.bean.me.ScoreBean;
import com.liansheng.carworld.bean.other.PageBean;
import com.liansheng.carworld.kit.Constant;
import com.liansheng.carworld.net.JsonCallback;
import com.liansheng.carworld.net.NetApi;
import com.liansheng.carworld.net.UrlKit;
import com.liansheng.carworld.utils.ViewUtils;

import java.util.HashMap;

import butterknife.BindView;
import cn.droidlover.xdroid.views.appbar.ToolBar;
import cn.droidlover.xdroid.views.recyclerView.DividerLine;
import cn.droidlover.xrecyclerview.XRecyclerContentLayout;
import cn.droidlover.xrecyclerview.XRecyclerView;
import okhttp3.Call;

public class BillAct extends BaseActivity {

    @BindView(R.id.toolbar)
    ToolBar toolBar;
    @BindView(R.id.recycler_view)
    XRecyclerContentLayout contentLayout;
    private BillAdapter billAdapter;
    private int type;//1 积分 2 金额

    @Override
    public void initData(Bundle savedInstanceState) {
        toolBar.setTitle(getIntent().getStringExtra(Constant.KEY_TITLE));
        type = getIntent().getIntExtra(Constant.KEY_TYPE, 1);
        initAdapter();
        getBill(0);
    }

    private void initAdapter() {
        billAdapter = new BillAdapter(null, type);
        contentLayout.getRecyclerView().verticalLayoutManager(context);
        contentLayout.getRecyclerView().setAdapter(billAdapter);
        contentLayout.getRecyclerView().addItemDecoration(new DividerLine(DividerLine.HORIZONTAL));
        contentLayout.getSwipeRefreshLayout().setColorSchemeResources(R.color.order, R.color.order2, R.color.end);
        contentLayout.emptyView(ViewUtils.getEmptyView(this, R.mipmap.bg_bill_none));
        contentLayout.getRecyclerView()
                .setOnRefreshAndLoadMoreListener(new XRecyclerView.OnRefreshAndLoadMoreListener() {
                    @Override
                    public void onRefresh() {
                        getBill(0);
                    }

                    @Override
                    public void onLoadMore(int page) {
                        getBill(page);
                    }
                });

        contentLayout.loadingView(View.inflate(this, R.layout.view_loading, null));
        contentLayout.getRecyclerView().useDefLoadMoreView();
//        contentLayout.getRecyclerView().addItemDecoration(new DividerItemDecoration(getBaseActivity(), DividerItemDecoration.VERTICAL));
    }

    @Override
    public int getLayoutId() {
        return R.layout.common_list;
    }

    private void getBill(int page) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("pageIndex", page);
        map.put("pageSize", Constant.DEFULT_RESULT);
        String url;
        if(type==1){
            url=UrlKit.USER_POINT;
        }else {
            url=UrlKit.USER_BALANCE;
        }
        NetApi.get(url, map, new JsonCallback() {

            @Override
            public void onSuccess(String response, int id) {
                Gson gson = new Gson();
                PageBean<ScoreBean> pageBean = gson.fromJson(response, new TypeToken<PageBean<ScoreBean>>(){}.getType());
                if (page == 0) {
                    billAdapter.getData().clear();
                }
                if (pageBean.getItems().size() > 0) {
                    billAdapter.addData(pageBean.getItems());
                } else {
                    if (page == 0) {
                        contentLayout.showEmpty();
                        return;
                    }
                }
                if (pageBean.getItems().size() < Constant.DEFULT_RESULT) {
                    MAX_PAGE = page;
                } else {
                    MAX_PAGE++;
                }
                contentLayout.getRecyclerView().setPage(page, MAX_PAGE);
            }
        });
    }
}
