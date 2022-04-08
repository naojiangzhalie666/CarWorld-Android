package com.liansheng.carworld.activity.home;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.kongzue.dialog.interfaces.OnDialogButtonClickListener;
import com.kongzue.dialog.util.BaseDialog;
import com.kongzue.dialog.v3.MessageDialog;
import com.liansheng.carworld.R;
import com.liansheng.carworld.activity.BaseActivity;
import com.liansheng.carworld.adapter.home.DriverSelectAdapter;
import com.liansheng.carworld.bean.child.ChildInfoBean;
import com.liansheng.carworld.bean.home.DriverSelectBean;
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

public class DriverSelectAct extends BaseActivity {

    @BindView(R.id.toolbar)
    ToolBar mToolbar;
    @BindView(R.id.recycler_view)
    XRecyclerContentLayout xRecyclerContentLayout;
    XRecyclerView recyclerView;

    private DriverSelectAdapter selectAdapter;

    private String id;


    @Override
    public void initData(Bundle savedInstanceState) {
        mToolbar.setTitle("请选择司机");
        id = getIntent().getStringExtra(Constant.KEY_ID);
        recyclerView = xRecyclerContentLayout.getRecyclerView();
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        selectAdapter = new DriverSelectAdapter(null);
        recyclerView.setAdapter(selectAdapter);
        recyclerView.addItemDecoration(new DividerLine(DividerLine.HORIZONTAL));
        recyclerView.setOnRefreshAndLoadMoreListener(new XRecyclerView.OnRefreshAndLoadMoreListener() {
            @Override
            public void onRefresh() {
                getData();
            }

            @Override
            public void onLoadMore(int page) {

            }
        });
        xRecyclerContentLayout.getSwipeRefreshLayout().setColorSchemeResources(R.color.order, R.color.order2, R.color.end);
        xRecyclerContentLayout.emptyView(ViewUtils.getEmptyView(this, R.mipmap.bg_bill_none));
        xRecyclerContentLayout.loadingView(View.inflate(this, R.layout.view_loading, null));
        xRecyclerContentLayout.getRecyclerView().useDefLoadMoreView();
        selectAdapter.addChildClickViewIds(R.id.item_driver_btn1, R.id.item_driver_btn3);
        selectAdapter.setOnItemChildClickListener((adapter, view, position) -> {
            DriverSelectBean item = (DriverSelectBean) adapter.getItem(position);
            if (view.getId() == R.id.item_driver_btn1) {
                MessageDialog.build(this)
                        .setTitle("温馨提示")
                        .setMessage("是否分配订单给 "+item.getUsername())
                        .setCancelButton("取消", new OnDialogButtonClickListener() {
                            @Override
                            public boolean onClick(BaseDialog baseDialog, View v) {
                                return false;
                            }
                        })
                        .setOkButton("确定", new OnDialogButtonClickListener() {
                            @Override
                            public boolean onClick(BaseDialog baseDialog, View v) {
                                orderDriver(id, item.getId());
                                return false;
                            }
                        })
                        .show();
            } else if (view.getId() == R.id.item_driver_btn3) {
                MessageDialog.build(this)
                        .setTitle("温馨提示")
                        .setMessage("是否取消 "+item.getUsername()+" 分配的订单")
                        .setCancelButton("取消", new OnDialogButtonClickListener() {
                            @Override
                            public boolean onClick(BaseDialog baseDialog, View v) {
                                return false;
                            }
                        })
                        .setOkButton("确定", new OnDialogButtonClickListener() {
                            @Override
                            public boolean onClick(BaseDialog baseDialog, View v) {
                                cancelOrder(item.getOrders().get(1).getId(), item.getId());
                                return false;
                            }
                        })
                        .show();
            }
        });
        getData();
    }

    @Override
    public int getLayoutId() {
        return R.layout.common_list;
    }

    public void cancelOrder(String id, String accountId) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("transportOrderId", id);
        map.put("accountId", accountId);
        NetApi.delete(UrlKit.USER_ORDER_DIRVER, map, new JsonCallback() {

            @Override
            public void onSuccess(String response, int id) {
                Util.toast("取消成功");
                finish();
            }
        });
    }

    public void orderDriver(String id, String accountId) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("transportOrderId", id);
        map.put("accountId", accountId);
        NetApi.post(UrlKit.USER_ORDER_DIRVER, map, new JsonCallback() {

            @Override
            public void onSuccess(String response, int id) {
                Util.toast("分配完成");
                finish();
            }
        });
    }

    public void getData() {
        NetApi.get(UrlKit.CHILD_ACCOUNT, null, new JsonCallback() {

            @Override
            public void onSuccess(String response, int id) {
                selectAdapter.getData().clear();
                PageBean<DriverSelectBean> pageBean = new Gson().fromJson(response, new TypeToken<PageBean<DriverSelectBean>>() {
                }.getType());
                selectAdapter.replaceData(pageBean.getItems());
                if (pageBean.getItems().size() == 0) {
                    xRecyclerContentLayout.showEmpty();
                }
            }
        });
    }
}
