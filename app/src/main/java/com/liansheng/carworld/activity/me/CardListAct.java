package com.liansheng.carworld.activity.me;


import android.content.Intent;
import android.os.Bundle;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.liansheng.carworld.R;
import com.liansheng.carworld.activity.BaseActivity;
import com.liansheng.carworld.activity.home.CardAct;
import com.liansheng.carworld.adapter.me.CardListAadpter;
import com.liansheng.carworld.bean.me.LicensesBean;
import com.liansheng.carworld.kit.Constant;
import com.liansheng.carworld.net.JsonCallback;
import com.liansheng.carworld.net.NetApi;
import com.liansheng.carworld.bean.other.PageBean;
import com.liansheng.carworld.net.UrlKit;

import butterknife.BindView;
import cn.droidlover.xdroid.tools.utils.ActivityManage;
import cn.droidlover.xdroid.views.appbar.ToolBar;
import cn.droidlover.xrecyclerview.XRecyclerContentLayout;
import okhttp3.Call;

public class CardListAct extends BaseActivity {

    @BindView(R.id.toolbar)
    ToolBar mToolbar;
    @BindView(R.id.recycler_view)
    XRecyclerContentLayout xRecyclerContentLayout;
    RecyclerView recyclerView;

    private CardListAadpter cardListAadpter;


    @Override
    public void initData(Bundle savedInstanceState) {
        mToolbar.setTitle("证件");
        recyclerView = xRecyclerContentLayout.getRecyclerView();
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        cardListAadpter = new CardListAadpter(null);
        recyclerView.setAdapter(cardListAadpter);
//        recyclerView.setOnRefreshAndLoadMoreListener(new XRecyclerView.OnRefreshAndLoadMoreListener() {
//            @Override
//            public void onRefresh() {
//
//            }
//
//            @Override
//            public void onLoadMore(int page) {
//
//            }
//        });
        cardListAadpter.setOnItemClickListener((adapter, view, position) -> {
            LicensesBean item = (LicensesBean) adapter.getItem(position);
            Intent intent = new Intent();
            intent.putExtra(Constant.KEY_BEAN, item);
            ActivityManage.push(CardAct.class, intent);
        });
    }

    @Override
    public int getLayoutId() {
        return R.layout.common_list;
    }

    @Override
    protected void onResume() {
        super.onResume();
        getAuth();
    }

    public void getAuth() {
        NetApi.get(UrlKit.USER_LICENSE, null, new JsonCallback() {

            @Override
            public void onSuccess(String response, int id) {
                PageBean<LicensesBean> pageBean = new Gson().fromJson(response, new TypeToken<PageBean<LicensesBean>>() {
                }.getType());
                cardListAadpter.replaceData(pageBean.getItems());
            }
        });
    }
}
