package com.liansheng.carworld.activity.company;

import android.graphics.EmbossMaskFilter;
import android.os.Bundle;
import android.view.View;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.liansheng.carworld.R;
import com.liansheng.carworld.activity.BaseActivity;
import com.liansheng.carworld.activity.logic.OtherLogic;
import com.liansheng.carworld.adapter.company.ApplyMembersAdapter;
import com.liansheng.carworld.adapter.me.BillAdapter;
import com.liansheng.carworld.bean.MemberBean;
import com.liansheng.carworld.bean.UserInfo;
import com.liansheng.carworld.bean.company.ReqApplyMembers;
import com.liansheng.carworld.bean.me.CompanyBean;
import com.liansheng.carworld.bean.me.ScoreBean;
import com.liansheng.carworld.bean.other.PageBean;
import com.liansheng.carworld.kit.Constant;
import com.liansheng.carworld.net.JsonCallback;
import com.liansheng.carworld.net.NetApi;
import com.liansheng.carworld.net.UrlKit;
import com.liansheng.carworld.utils.ViewUtils;

import java.util.HashMap;

import butterknife.BindView;
import cn.droidlover.xdroid.hold.info.SharedInfo;
import cn.droidlover.xdroid.tools.utils.Util;
import cn.droidlover.xdroid.views.appbar.ToolBar;
import cn.droidlover.xdroid.views.recyclerView.DividerLine;
import cn.droidlover.xrecyclerview.XRecyclerContentLayout;
import cn.droidlover.xrecyclerview.XRecyclerView;
import okhttp3.Call;

public class ApplyMembersAct extends BaseActivity {

    @BindView(R.id.toolbar)
    ToolBar toolBar;
    @BindView(R.id.recycler_view)
    XRecyclerContentLayout contentLayout;
    private ApplyMembersAdapter billAdapter;

    @Override
    public void initData(Bundle savedInstanceState) {
        toolBar.setTitle("申请列表");
        initAdapter();
        getBill(0);
    }

    private void initAdapter() {
        billAdapter = new ApplyMembersAdapter(null);
        billAdapter.addChildClickViewIds(R.id.confirm, R.id.cancel);
        billAdapter.setOnItemChildClickListener((adapter, view, position) -> {
            MemberBean item = (MemberBean) adapter.getItem(position);
            switch (view.getId()) {
                case R.id.confirm:
                    accept(item.getUserId());
                    break;
                case R.id.cancel:
                    reject(item.getId());
                    break;
            }
        });
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
    }

    @Override
    public int getLayoutId() {
        return R.layout.common_list;
    }

    private void accept(String id) {
        ReqApplyMembers applyMembers = new ReqApplyMembers();
        applyMembers.setMemberId(id);
        applyMembers.setType("ordinary");
        NetApi.put(UrlKit.USER_MEMBER_STATUS, new Gson().toJson(applyMembers), new JsonCallback() {

            @Override
            public void onSuccess(String response, int id) {
                Util.toast("操作成功");
                getBill(0);
            }
        });
    }

    private void reject(String id) {
        NetApi.delete(UrlKit.USER_MEMBER_DELETE + id, null, new JsonCallback() {

            @Override
            public void onSuccess(String response, int id) {
                Util.toast("操作成功");
                getBill(0);
            }
        });
    }

    private void getBill(int page) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("pageIndex", page);
        map.put("pageSize", Constant.DEFULT_RESULT);
        map.put("passed", false);
        UserInfo userInfo = SharedInfo.getInstance().getEntity(UserInfo.class);
        for (int i = 0; i < userInfo.getCompanys().size(); i++) {
            CompanyBean companyBean = userInfo.getCompanys().get(i);
            if (Constant.ID_STATUS_PASSED.equals(companyBean.getStatus())) {
                map.put("companyId", companyBean.getId());
                break;
            }
        }
        NetApi.get(UrlKit.USER_MEMBER, map, new JsonCallback() {

            @Override
            public void onSuccess(String response, int id) {
                Gson gson = new Gson();
                PageBean<MemberBean> pageBean = gson.fromJson(response, new TypeToken<PageBean<MemberBean>>() {
                }.getType());
                OtherLogic.updateAdapter(page, billAdapter, pageBean, contentLayout);
            }
        });
    }
}
