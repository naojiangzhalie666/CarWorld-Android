package com.liansheng.carworld.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.liansheng.carworld.R;
import com.liansheng.carworld.activity.WebViewAct;
import com.liansheng.carworld.activity.logic.OtherLogic;
import com.liansheng.carworld.adapter.company.SearchReportAdapter;
import com.liansheng.carworld.bean.company.SearchReportBean;
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

public class SHHistoryFrag extends BaseFragment {

    @BindView(R.id.contentLayout)
    XRecyclerContentLayout contentLayout;
    private SearchReportAdapter reportAdapter;


    public static SHHistoryFrag newInstance(int type) {
        Bundle args = new Bundle();
        args.putInt(Constant.KEY_TYPE, type);
        SHHistoryFrag fragment = new SHHistoryFrag();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        super.initData(savedInstanceState);
        initAdapter();
        getData(0);
    }

    @Override
    public int getLayoutId() {
        return R.layout.frag_list;
    }


    private void initAdapter() {
        reportAdapter = new SearchReportAdapter(null);
        contentLayout.getRecyclerView().verticalLayoutManager(context);
        contentLayout.getRecyclerView().setAdapter(reportAdapter);
        contentLayout.getRecyclerView().addItemDecoration(new DividerLine(DividerLine.HORIZONTAL));
        reportAdapter.setOnItemClickListener((adapter, view, position) -> {
            SearchReportBean item = (SearchReportBean) adapter.getItem(position);
            if(item.getReport()!=null&&!TextUtils.isEmpty(item.getReport().getReport_url())){
                Intent intent1 = new Intent();
                intent1.putExtra(Constant.KEY_TITLE, "查询记录");
                intent1.putExtra(Constant.KEY_URL, item.getReport().getReport_url());
                ActivityManage.push(WebViewAct.class, intent1);
            }
        });
        contentLayout.getSwipeRefreshLayout().setColorSchemeResources(R.color.order, R.color.order2, R.color.end);
        contentLayout.emptyView(ViewUtils.getEmptyView(getBaseActivity(), R.mipmap.bg_bill_none));
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

        contentLayout.loadingView(View.inflate(getBaseActivity(), R.layout.view_loading, null));
        contentLayout.getRecyclerView().useDefLoadMoreView();
//        contentLayout.getRecyclerView().addItemDecoration(new DividerItemDecoration(getBaseActivity(), DividerItemDecoration.VERTICAL));
    }

    private void getData(int page) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("pageIndex", page);
        map.put("pageSize", Constant.DEFULT_RESULT);
        String url;
        if (getArguments().getInt(Constant.KEY_TYPE, 1) == 1) {
            url = UrlKit.APP_MTNCE_ORDER;
        } else {
            url = UrlKit.APP_INSURANCE_ORDER;
        }
        map.put("paid", true);
        NetApi.get(url, map, new JsonCallback() {

            @Override
            public void onSuccess(String response, int id) {
                PageBean<SearchReportBean> home = new Gson().fromJson(response, new TypeToken<PageBean<SearchReportBean>>() {
                }.getType());
                OtherLogic.updateAdapter(page,reportAdapter,home,contentLayout);
            }
        });
    }

}
