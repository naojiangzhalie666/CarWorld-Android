package com.liansheng.carworld.fragment;

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
import com.liansheng.carworld.activity.home.CompanyInfoAct;
import com.liansheng.carworld.activity.home.SaleCarDetailsAct;
import com.liansheng.carworld.activity.logic.OtherLogic;
import com.liansheng.carworld.adapter.CarAdapter;
import com.liansheng.carworld.bean.UserInfo;
import com.liansheng.carworld.bean.home.SaleCardBean;
import com.liansheng.carworld.bean.me.CompanyBean;
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
import cn.droidlover.xrecyclerview.XRecyclerContentLayout;
import cn.droidlover.xrecyclerview.XRecyclerView;
import okhttp3.Call;

public class SaleCarFrag extends BaseFragment {

    @BindView(R.id.contentLayout)
    XRecyclerContentLayout contentLayout;

    private CarAdapter mAdapter;


    public static SaleCarFrag newInstance(String id) {
        Bundle args = new Bundle();
        SaleCarFrag fragment = new SaleCarFrag();
        args.putString(Constant.KEY_ID, id);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void initData(Bundle savedInstanceState) {
        super.initData(savedInstanceState);
        initAdapter();
        getData(0);
    }

    public void initAdapter() {
        mAdapter = new CarAdapter();
        contentLayout.getRecyclerView().verticalLayoutManager(context);
        contentLayout.getRecyclerView().setAdapter(mAdapter);
        contentLayout.getSwipeRefreshLayout().setColorSchemeResources(R.color.order, R.color.order2, R.color.end);
        contentLayout.emptyView(ViewUtils.getEmptyView(getActivity(), R.mipmap.bg_cy));
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
                SaleCardBean item = (SaleCardBean) adapter.getItem(position);
                Intent intent = new Intent();
                intent.putExtra(Constant.KEY_BEAN, item);
                ActivityManage.push(SaleCarDetailsAct.class, intent);
            }
        });
        contentLayout.loadingView(View.inflate(getActivity(), R.layout.view_loading, null));
        contentLayout.getRecyclerView().useDefLoadMoreView();
        contentLayout.getRecyclerView().addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
        contentLayout.getRecyclerView().setBackgroundResource(R.color.white);
    }

    @Override
    public int getLayoutId() {
        return R.layout.frag_list;
    }

    private void getData(int page) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("pageIndex", page);
        map.put("pageSize", Constant.DEFULT_RESULT);
        if (getArguments().getString(Constant.KEY_ID) != null) {
            map.put("userCompanyId", getArguments().getString(Constant.KEY_ID));
        } else {
            UserInfo userInfo = SharedInfo.getInstance().getEntity(UserInfo.class);
            map.put("userId", userInfo.getId());
        }
        map.put("off", false);
        NetApi.get(UrlKit.CAR_RESOURCE, map, new JsonCallback() {

            @Override
            public void onSuccess(String response, int id) {
                PageBean<SaleCardBean> data = new Gson().fromJson(response, new TypeToken<PageBean<SaleCardBean>>() {
                }.getType());
                OtherLogic.updateAdapter(page, mAdapter, data, contentLayout);
                ((CompanyInfoAct) getActivity()).setTabTitle("批发中车源( " + data.getTotal() + " )", 0);
            }
        });
    }
}
