package com.liansheng.carworld.activity.sub;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.liansheng.carworld.R;
import com.liansheng.carworld.activity.logic.OtherLogic;
import com.liansheng.carworld.adapter.OrderAdapter;
import com.liansheng.carworld.bean.TransporterBean;
import com.liansheng.carworld.bean.other.PageBean;
import com.liansheng.carworld.fragment.BaseFragment;
import com.liansheng.carworld.kit.Constant;
import com.liansheng.carworld.net.JsonCallback;
import com.liansheng.carworld.net.NetApi;
import com.liansheng.carworld.net.UrlKit;
import com.liansheng.carworld.utils.ViewUtils;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import cn.droidlover.xdroid.tools.utils.ActivityManage;
import cn.droidlover.xrecyclerview.XRecyclerContentLayout;
import cn.droidlover.xrecyclerview.XRecyclerView;
import okhttp3.Call;


public class SubOrderListFrag extends BaseFragment {
    @BindView(R.id.contentLayout)
    XRecyclerContentLayout contentLayout;
    private OrderAdapter orderAdapter;
    private String status = "";
    Unbinder unbinder;

    public static SubOrderListFrag newInstance(String type) {
        Bundle args = new Bundle();
        args.putString(Constant.KEY_TYPE, type);
        SubOrderListFrag fragment = new SubOrderListFrag();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        unbinder = ButterKnife.bind(this, rootView);
        getOrderStatus();
        initAdapter();
        return super.onCreateView(inflater, container, savedInstanceState);
    }


    private void getOrderStatus() {
        status = getArguments().getString(Constant.KEY_TYPE);//statusList[OrderListActivity.pageCount-1];
//        OrderListActivity.pageCount++;
//        if (status.equals("destination")) {
//            OrderListActivity orderListActivity = (OrderListActivity) getActivity();
//            orderListActivity.jump();
//        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            getOrder(status, 0);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (getUserVisibleHint()) {
            getOrder(status, 0);
        }
    }

    private void initAdapter() {
        orderAdapter=new OrderAdapter(null);
        contentLayout.getRecyclerView().verticalLayoutManager(context);
        contentLayout.getRecyclerView().setAdapter(orderAdapter);
        orderAdapter.setOnItemClickListener((adapter, view, position) -> {
            TransporterBean item = (TransporterBean) adapter.getItem(position);
            Intent bundle = new Intent();
            bundle.putExtra("id", item.getId());
            ActivityManage.push(SubOrderDetailsAct.class,bundle);
        });
        contentLayout.getSwipeRefreshLayout().setColorSchemeResources(R.color.order, R.color.order2, R.color.end);
        contentLayout.emptyView(ViewUtils.getEmptyView(getBaseActivity(), R.mipmap.bg_home_order));
        contentLayout.getRecyclerView()
                .setOnRefreshAndLoadMoreListener(new XRecyclerView.OnRefreshAndLoadMoreListener() {
                    @Override
                    public void onRefresh() {
                        getOrder(status, 0);
                    }

                    @Override
                    public void onLoadMore(int page) {
                        getOrder(status, page);
                    }
                });

        contentLayout.loadingView(View.inflate(getBaseActivity(), R.layout.view_loading, null));
        contentLayout.getRecyclerView().useDefLoadMoreView();
    }

    private void getOrder(String status, int page) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("pageIndex", page);
        map.put("pageSize", Constant.DEFULT_RESULT);
        map.put("status", status);
        NetApi.get(UrlKit.CHILD_ACCOUNT_TRANSPORT_ORDER, map, new JsonCallback() {

            @Override
            public void onSuccess(String response, int id) {
                PageBean<TransporterBean> home = new Gson().fromJson(response, new TypeToken<PageBean<TransporterBean>>() {
                }.getType());
                OtherLogic.updateAdapter(page, orderAdapter, home, contentLayout);
            }
        });
    }

    @Override
    public void initData(Bundle savedInstanceState) {

    }

    @Override
    public int getLayoutId() {
        return R.layout.frag_list;
    }

}
