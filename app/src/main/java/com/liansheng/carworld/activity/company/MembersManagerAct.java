package com.liansheng.carworld.activity.company;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.DividerItemDecoration;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.liansheng.carworld.R;
import com.liansheng.carworld.activity.BaseActivity;
import com.liansheng.carworld.activity.logic.OtherLogic;
import com.liansheng.carworld.adapter.company.MembersManagerAdapter;
import com.liansheng.carworld.bean.MemberBean;
import com.liansheng.carworld.bean.other.PageBean;
import com.liansheng.carworld.kit.Constant;
import com.liansheng.carworld.net.JsonCallback;
import com.liansheng.carworld.net.NetApi;
import com.liansheng.carworld.net.UrlKit;
import com.liansheng.carworld.utils.ViewUtils;
import com.liansheng.carworld.view.PopAddDialog;
import com.liansheng.carworld.view.pop.AddPop;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.OnClick;
import cn.droidlover.xdroid.tools.utils.ActivityManage;
import cn.droidlover.xdroid.tools.utils.Util;
import cn.droidlover.xdroid.views.appbar.TitleBar;
import cn.droidlover.xdroid.views.appbar.ToolBar;
import cn.droidlover.xdroid.views.recyclerView.DividerLine;
import cn.droidlover.xrecyclerview.XRecyclerContentLayout;
import cn.droidlover.xrecyclerview.XRecyclerView;
import okhttp3.Call;

public class MembersManagerAct extends BaseActivity {

    @BindView(R.id.toolbar)
    ToolBar mToolbar;
    @BindView(R.id.contentLayout)
    XRecyclerContentLayout contentLayout;
    private MembersManagerAdapter mAdapter;
    private String userType;
    private MemberBean managerBean;


    @Override
    public void initData(Bundle savedInstanceState) {
        initAdapter();
        getCompanyAuth();
    }

    @Override
    public int getLayoutId() {
        return R.layout.act_members_manager;
    }

    @Override
    protected void onResume() {
        super.onResume();
        getCompanyManager();
    }

    public void initAdapter() {
        mAdapter = new MembersManagerAdapter();
        contentLayout.getRecyclerView().verticalLayoutManager(context);
        contentLayout.getRecyclerView().setAdapter(mAdapter);
        contentLayout.getRecyclerView().addItemDecoration(new DividerLine(DividerLine.HORIZONTAL));
        contentLayout.getSwipeRefreshLayout().setColorSchemeResources(R.color.order, R.color.order2, R.color.end);
        contentLayout.emptyView(ViewUtils.getEmptyView(this, R.mipmap.bg_bill_none));
        contentLayout.getRecyclerView().setOnRefreshAndLoadMoreListener(new XRecyclerView.OnRefreshAndLoadMoreListener() {
            @Override
            public void onRefresh() {
                getCompanyManager();
            }

            @Override
            public void onLoadMore(int page) {
                getData(page);
            }
        });
        mAdapter.setOnItemClickListener((adapter, view, position) -> {
            MemberBean item = (MemberBean) adapter.getItem(position);
            Intent intent = new Intent();
            intent.putExtra(Constant.KEY_BEAN, item);
            intent.putExtra(Constant.KEY_TYPE, userType);
            ActivityManage.push(MembersDetailsAct.class, intent);
        });
        contentLayout.loadingView(View.inflate(this, R.layout.view_loading, null));
        contentLayout.getRecyclerView().useDefLoadMoreView();
        contentLayout.getRecyclerView().addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        contentLayout.getRecyclerView().setBackgroundResource(R.color.white);
    }

    @OnClick(R.id.member_new)
    public void onViewClick() {
        Intent intent = new Intent();
        ActivityManage.push(ApplyMembersAct.class, intent);
    }

    private void getData(int page) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("pageIndex", page);
        map.put("pageSize", Constant.DEFULT_RESULT);
        map.put("companyId", getIntent().getLongExtra(Constant.KEY_ID, 0));
        map.put("passed", true);
        NetApi.get(UrlKit.USER_MEMBER, map, new JsonCallback() {

            @Override
            public void onSuccess(String response, int id) {
                Gson gson = new Gson();
                PageBean<MemberBean> data = gson.fromJson(response, new TypeToken<PageBean<MemberBean>>() {
                }.getType());
                if (page == 0) {
                    data.getItems().add(0, managerBean);
                }
                OtherLogic.updateAdapter(page, mAdapter, data, contentLayout);
            }
        });
    }

    private void invitation(String mobile) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("mobile", mobile);
        NetApi.post(UrlKit.USER_INVITATION, map, new JsonCallback() {

            @Override
            public void onSuccess(String response, int id) {
                Util.toast("已发起邀请");
            }
        });
    }

    private void getCompanyAuth() {
        NetApi.get(String.format(UrlKit.USER_COMPANY_AUTH, getIntent().getLongExtra(Constant.KEY_ID, 0)), null, new JsonCallback() {

            @Override
            public void onSuccess(String response, int id) {
                userType = response.replace("\"", "");
                if ("admin".equals(userType) || "manager".equals(userType)) {
                    findViewById(R.id.member_new).setVisibility(View.VISIBLE);
                }
            }
        });
    }

    private void getCompanyManager() {
        NetApi.get(String.format(UrlKit.USER_COMPANY_ADMIN, getIntent().getLongExtra(Constant.KEY_ID, 0)), null, new JsonCallback() {

            @Override
            public void onSuccess(String response, int id) {
                getData(0);
                managerBean = new Gson().fromJson(response, MemberBean.class);
                managerBean.setType("admin");
            }
        });
    }


}
