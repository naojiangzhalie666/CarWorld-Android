package com.liansheng.carworld.activity.circle;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.kongzue.dialog.interfaces.OnDialogButtonClickListener;
import com.kongzue.dialog.util.BaseDialog;
import com.kongzue.dialog.v3.MessageDialog;
import com.liansheng.carworld.R;
import com.liansheng.carworld.activity.BaseActivity;
import com.liansheng.carworld.activity.home.DriverEditAct;
import com.liansheng.carworld.activity.logic.OtherLogic;
import com.liansheng.carworld.adapter.home.CircleAdapter;
import com.liansheng.carworld.adapter.home.DriverManagerAdapter;
import com.liansheng.carworld.bean.UserInfo;
import com.liansheng.carworld.bean.event.RefreshEvent;
import com.liansheng.carworld.bean.home.CircleBean;
import com.liansheng.carworld.bean.home.DriverSelectBean;
import com.liansheng.carworld.bean.other.PageBean;
import com.liansheng.carworld.kit.Constant;
import com.liansheng.carworld.net.JsonCallback;
import com.liansheng.carworld.net.NetApi;
import com.liansheng.carworld.net.UrlKit;
import com.liansheng.carworld.utils.ViewUtils;
import com.liansheng.carworld.view.PopAddDialog;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;

import butterknife.BindView;
import cn.droidlover.xdroid.hold.info.SharedInfo;
import cn.droidlover.xdroid.tools.utils.ActivityManage;
import cn.droidlover.xdroid.views.appbar.TitleBar;
import cn.droidlover.xdroid.views.appbar.ToolBar;
import cn.droidlover.xdroid.views.recyclerView.DividerLine;
import cn.droidlover.xrecyclerview.XRecyclerContentLayout;
import cn.droidlover.xrecyclerview.XRecyclerView;
import cn.jpush.android.api.JPushInterface;

public class HistoryReleaseAct extends BaseActivity {

    @BindView(R.id.toolbar)
    ToolBar mToolbar;
    @BindView(R.id.recycler_view)
    XRecyclerContentLayout xRecyclerContentLayout;
    XRecyclerView recyclerView;

    private CircleAdapter adapter;
    private boolean flag;


    @Override
    public void initData(Bundle savedInstanceState) {
        String title = getIntent().getStringExtra(Constant.KEY_TITLE);
        mToolbar.setTitle(title);
        flag = "历史发布".equals(title);
        if (flag) {
            adapter = new CircleAdapter(2);
            adapter.setOnItemClickListener((adapter1, view, position) -> {
                CircleBean bean = (CircleBean) adapter1.getItem(position);
                Intent intent = new Intent();
                intent.putExtra(Constant.KEY_BEAN, bean);
                setResult(Activity.RESULT_OK, intent);
                finish();
            });
        } else {
            adapter = new CircleAdapter(4);
            adapter.addChildClickViewIds(R.id.item_circle_delete);
            adapter.setOnItemChildClickListener((adapter1, view, position) -> {
                MessageDialog.build((AppCompatActivity) context)
                        .setMessage("是否删除该发布？")
                        .setCancelButton("取消", new OnDialogButtonClickListener() {
                            @Override
                            public boolean onClick(BaseDialog baseDialog, View v) {
                                return false;
                            }
                        })
                        .setOkButton("确定", new OnDialogButtonClickListener() {
                            @Override
                            public boolean onClick(BaseDialog baseDialog, View v) {
                                CircleBean item = (CircleBean) adapter1.getItem(position);
                                NetApi.delete(UrlKit.USER_CAR_MOMENT + "/" + item.getId(), null, new JsonCallback() {
                                    @Override
                                    public void onSuccess(String data, int id) {
                                        adapter.remove(position);
                                        EventBus.getDefault().post(new RefreshEvent());
                                    }
                                });
                                return false;
                            }
                        })
                        .show();
            });
        }
        recyclerView = xRecyclerContentLayout.getRecyclerView();
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        xRecyclerContentLayout.emptyView(ViewUtils.getEmptyView(this, R.mipmap.bg_bill_none));
        xRecyclerContentLayout.loadingView(View.inflate(this, R.layout.view_loading, null));
        xRecyclerContentLayout.getRecyclerView().useDefLoadMoreView();
        recyclerView.setAdapter(adapter);
        recyclerView.setOnRefreshAndLoadMoreListener(new XRecyclerView.OnRefreshAndLoadMoreListener() {
            @Override
            public void onRefresh() {
                getData(0);
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
        getData(0);
    }

    public void getData(int page) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("pageIndex", page);
        map.put("pageSize", 10);
        if (flag) {
            map.put("type", "Post");
        }
        map.put("userId", SharedInfo.getInstance().getEntity(UserInfo.class).getId());
        NetApi.get(UrlKit.USER_CAR_MOMENT, map, new JsonCallback() {

            @Override
            public void onSuccess(String response, int id) {
                PageBean<CircleBean> message = new Gson().fromJson(response, new TypeToken<PageBean<CircleBean>>() {
                }.getType());
                OtherLogic.updateAdapter(page, 10, adapter, message, xRecyclerContentLayout);
            }
        });
    }
}
