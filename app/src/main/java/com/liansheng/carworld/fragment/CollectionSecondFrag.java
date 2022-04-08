package com.liansheng.carworld.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.liansheng.carworld.R;
import com.liansheng.carworld.activity.home.SaleCarDetailsAct;
import com.liansheng.carworld.activity.me.CollectionActivity;
import com.liansheng.carworld.adapter.me.CollectionCarAdapter;
import com.liansheng.carworld.bean.home.SaleCardBean;
import com.liansheng.carworld.bean.other.PageBean;
import com.liansheng.carworld.kit.Constant;
import com.liansheng.carworld.net.JsonCallback;
import com.liansheng.carworld.net.NetApi;
import com.liansheng.carworld.net.UrlKit;
import com.liansheng.carworld.utils.ViewUtils;

import java.util.HashMap;

import butterknife.BindView;
import cn.droidlover.xdroid.tools.utils.ActivityManage;
import cn.droidlover.xdroid.tools.utils.Util;
import cn.droidlover.xdroid.views.recyclerView.DividerLine;
import cn.droidlover.xrecyclerview.XRecyclerContentLayout;
import cn.droidlover.xrecyclerview.XRecyclerView;
import okhttp3.Call;

public class CollectionSecondFrag extends BaseFragment {

    @BindView(R.id.recycler_view)
    XRecyclerContentLayout contentLayout;
    private CollectionCarAdapter mAdapter;

    public static CollectionSecondFrag newInstance() {
        Bundle args = new Bundle();
        CollectionSecondFrag fragment = new CollectionSecondFrag();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        initView();
        initAdapter();
        getData(0);
    }

    @Override
    public int getLayoutId() {
        return R.layout.content_main;
    }

    private void initView() {
        mAdapter = new CollectionCarAdapter(null);
        mAdapter.addChildClickViewIds(R.id.collection_cancel, R.id.collection_share);
        mAdapter.setOnItemChildClickListener((adapter, view, position) -> {
            SaleCardBean item = (SaleCardBean) adapter.getItem(position);
            switch (view.getId()) {
                case R.id.collection_cancel:
                    delCollection(item.getId());
                    break;
                case R.id.collection_share:
//                    String desc = "二网手机：" + item.getMobile() + " " + item.getName() + "\n" + "4s座机号：" + item.getPhone();
//                    new WxUtils(context).wxWeb(String.format(UrlKit.H5_SHARE_4S, item.getId(), SharedInfo.getInstance().getEntity(UserInfo.class).getId()), item.getCompany(), desc);
                    break;
            }
        });
        mAdapter.setOnItemClickListener((adapter, view, position) -> {
            SaleCardBean item = (SaleCardBean) adapter.getItem(position);
            Intent intent = new Intent();
            intent.putExtra(Constant.KEY_BEAN, item);
            ActivityManage.push(SaleCarDetailsAct.class, intent);
        });
    }

    public void initAdapter() {
        contentLayout.getRecyclerView().verticalLayoutManager(context);
        contentLayout.getRecyclerView().setAdapter(mAdapter);
        contentLayout.getRecyclerView().addItemDecoration(new DividerLine(DividerLine.HORIZONTAL));
        contentLayout.getSwipeRefreshLayout().setColorSchemeResources(R.color.order, R.color.order2, R.color.end);
        contentLayout.emptyView(ViewUtils.getEmptyView(getActivity(), R.mipmap.bg_sc));
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

        contentLayout.loadingView(View.inflate(getActivity(), R.layout.view_loading, null));
        contentLayout.getRecyclerView().useDefLoadMoreView();
    }

    private void delCollection(String id) {
        NetApi.delCollection(id, new JsonCallback() {

            @Override
            public void onSuccess(String response, int id) {
                refresh();
                Util.toast("已取消收藏");
                ((CollectionActivity)getActivity()).getUserInfo();
            }
        });
    }

    public void getData(int page) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("pageIndex", page);
        map.put("pageSize", Constant.DEFULT_RESULT);
        NetApi.get(UrlKit.CAR_RESOURCE_COLLECTION, map, new JsonCallback() {

            @Override
            public void onSuccess(String response, int id) {
                Gson gson = new Gson();
                PageBean<SaleCardBean> data = gson.fromJson(response, new TypeToken<PageBean<SaleCardBean>>() {
                }.getType());
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

    public void refresh() {
        getData(0);
    }
}
