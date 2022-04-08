package com.liansheng.carworld.activity.home;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.liansheng.carworld.R;
import com.liansheng.carworld.activity.BaseActivity;
import com.liansheng.carworld.adapter.home.DriverManagerAdapter;
import com.liansheng.carworld.bean.home.DriverSelectBean;
import com.liansheng.carworld.bean.other.PageBean;
import com.liansheng.carworld.net.JsonCallback;
import com.liansheng.carworld.net.NetApi;
import com.liansheng.carworld.net.UrlKit;
import com.liansheng.carworld.utils.ViewUtils;
import com.liansheng.carworld.view.PopAddDialog;
import com.liansheng.carworld.view.pop.AddPop;

import butterknife.BindView;
import cn.droidlover.xdroid.tools.utils.ActivityManage;
import cn.droidlover.xdroid.views.appbar.TitleBar;
import cn.droidlover.xdroid.views.appbar.ToolBar;
import cn.droidlover.xdroid.views.recyclerView.DividerLine;
import cn.droidlover.xrecyclerview.XRecyclerContentLayout;
import cn.droidlover.xrecyclerview.XRecyclerView;
import okhttp3.Call;

public class DriverManagerAct extends BaseActivity {

    @BindView(R.id.toolbar)
    ToolBar mToolbar;
    @BindView(R.id.recycler_view)
    XRecyclerContentLayout xRecyclerContentLayout;
    XRecyclerView recyclerView;

    private DriverManagerAdapter adapter;


    @Override
    public void initData(Bundle savedInstanceState) {
        mToolbar.setTitle("人员管理");
        mToolbar.addAction(new TitleBar.ImageAction(R.mipmap.icon_add_ml) {
            @Override
            public void performAction(View view) {
                PopAddDialog.show(getSupportFragmentManager())
                        .setTitle("新增员工")
                        .setCallback((view1, content) -> {
                            if (!TextUtils.isEmpty(content)) {
//                                invitation(content);
                            }
                        });
            }
        });
        recyclerView = xRecyclerContentLayout.getRecyclerView();
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        xRecyclerContentLayout.emptyView(ViewUtils.getEmptyView(this, R.mipmap.bg_bill_none));
        xRecyclerContentLayout.loadingView(View.inflate(this, R.layout.view_loading, null));
        xRecyclerContentLayout.getRecyclerView().useDefLoadMoreView();
        adapter = new DriverManagerAdapter(null);
        adapter.addChildClickViewIds(R.id.item_driver_phone, R.id.tv_delete, R.id.tv_edit);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemChildClickListener((adapter1, view, position) -> {
            switch (view.getId()) {
                case R.id.item_driver_phone:
                    DriverSelectBean bean = (DriverSelectBean) adapter1.getItem(position);
                    callPhone(this, "联系电话：" + bean.getMobile(), bean.getMobile());
                    break;
                case R.id.tv_delete:
                    break;
                case R.id.tv_edit:
                    ActivityManage.push(DriverEditAct.class);
                    break;
            }
        });
        recyclerView.setOnRefreshAndLoadMoreListener(new XRecyclerView.OnRefreshAndLoadMoreListener() {
            @Override
            public void onRefresh() {
                getData();
            }

            @Override
            public void onLoadMore(int page) {

            }
        });
        recyclerView.addItemDecoration(new DividerLine(DividerLine.HORIZONTAL));
    }

    @Override
    public int getLayoutId() {
        return R.layout.common_list;
    }

    @Override
    protected void onResume() {
        super.onResume();
        getData();
    }

    public void getData() {
        NetApi.get(UrlKit.CHILD_ACCOUNT, null, new JsonCallback() {

            @Override
            public void onSuccess(String response, int id) {
                PageBean<DriverSelectBean> pageBean = new Gson().fromJson(response, new TypeToken<PageBean<DriverSelectBean>>() {
                }.getType());
                adapter.replaceData(pageBean.getItems());
                if (pageBean.getItems().size() == 0) {
                    xRecyclerContentLayout.showEmpty();
                }
            }
        });
    }
}
