package com.liansheng.carworld.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.liansheng.carworld.R;
import com.liansheng.carworld.activity.OrderDetailsActivity;
import com.liansheng.carworld.activity.OrderSearchActivity;
import com.liansheng.carworld.activity.logic.OtherLogic;
import com.liansheng.carworld.activity.user.LoginYzmActivity;
import com.liansheng.carworld.adapter.OrderAdapter;
import com.liansheng.carworld.bean.TransporterBean;
import com.liansheng.carworld.bean.other.PageBean;
import com.liansheng.carworld.net.JsonCallback;
import com.liansheng.carworld.net.NetApi;
import com.liansheng.carworld.utils.ViewUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import cn.droidlover.xdroid.tools.utils.ActivityManage;
import cn.droidlover.xrecyclerview.XRecyclerContentLayout;
import cn.droidlover.xrecyclerview.XRecyclerView;
import okhttp3.Call;


public class OrderSearchFragment extends BaseFragment {
    @BindView(R.id.contentLayout)
    XRecyclerContentLayout contentLayout;
    private OrderAdapter orderAdapter;
    private String status = "";
    Unbinder unbinder;
    private View rootView;
    private String[] statusList = new String[]{"origin", "destination"};

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        unbinder = ButterKnife.bind(this, rootView);
        getOrderStatus();
        initAdapter();
        initView();
        return super.onCreateView(inflater, container, savedInstanceState);
    }


    private void getOrderStatus() {
        status = statusList[OrderSearchActivity.pageCount];
        OrderSearchActivity.pageCount++;
    }

    private void initView() {
        getOrder(status, 0);
    }

    private void getOrder(String status, int page) {
        if (getBaseActivity().getLoginResult() != null) {
            NetApi.getSerachList(getBaseActivity().getLoginResult().getToken(), page + "", getBaseActivity().DEFULT_RESULT + "", status, OrderSearchActivity.city, new JsonCallback() {

                @Override
                public void onSuccess(String response, int id) {
                    PageBean<TransporterBean> home = new Gson().fromJson(response, new TypeToken<PageBean<TransporterBean>>() {
                    }.getType());
                    OtherLogic.updateAdapter(page, orderAdapter, home, contentLayout);
                }
            });
        } else {
            getBaseActivity().showToast("账号未登录，请先登录！");
            getBaseActivity().toActivity(LoginYzmActivity.class, null);
        }
    }

    private void initAdapter() {
        orderAdapter = new OrderAdapter(null);
        contentLayout.getRecyclerView().verticalLayoutManager(context);
        contentLayout.getRecyclerView().setAdapter(orderAdapter);
        orderAdapter.setOnItemClickListener((adapter, view, position) -> {
            TransporterBean item = (TransporterBean) adapter.getItem(position);
            Intent bundle = new Intent();
            bundle.putExtra("id", item.getId());
            ActivityManage.push(OrderDetailsActivity.class, bundle);
        });
        contentLayout.emptyView(ViewUtils.getEmptyView(getBaseActivity(), R.mipmap.bg_home_order));
        contentLayout.getSwipeRefreshLayout().setColorSchemeResources(R.color.order, R.color.order2, R.color.end);
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
//        contentLayout.getRecyclerView().addItemDecoration(new DividerItemDecoration(getBaseActivity(), DividerItemDecoration.VERTICAL));
    }

    @Override
    public void initData(Bundle savedInstanceState) {

    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_search_list;
    }

    public static OrderSearchFragment newInstance() {
        return new OrderSearchFragment();
    }
}
