package com.liansheng.carworld.activity.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.liansheng.carworld.R;
import com.liansheng.carworld.activity.BaseActivity;
import com.liansheng.carworld.adapter.home.CarLoanAdapter;
import com.liansheng.carworld.bean.home.CarLoanBean;
import com.liansheng.carworld.bean.other.PageBean;
import com.liansheng.carworld.kit.Constant;
import com.liansheng.carworld.net.JsonCallback;
import com.liansheng.carworld.net.NetApi;
import com.liansheng.carworld.net.UrlKit;

import java.util.HashMap;

import butterknife.BindView;
import cn.droidlover.xdroid.tools.utils.ActivityManage;
import cn.droidlover.xdroid.views.appbar.ToolBar;
import okhttp3.Call;

public class CarLoanAct extends BaseActivity {

    @BindView(R.id.toolbar)
    ToolBar mToolbar;
    @BindView(R.id.refresh)
    SwipeRefreshLayout refreshLayout;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    private CarLoanAdapter mAdapter;

    @Override
    public void initData(Bundle savedInstanceState) {
        mToolbar.setTitle("车贷名录");
        mToolbar.setRightTextAction(0, R.color.order);
        initAdapter();
    }

    private void initAdapter() {
        mAdapter = new CarLoanAdapter(null);
        refreshLayout.setColorSchemeColors(ContextCompat.getColor(context, R.color.order), ContextCompat.getColor(context, R.color.order2));
        refreshLayout.setOnRefreshListener(() -> getData(0));
        mAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull BaseQuickAdapter adapter, @NonNull View view, int position) {
                CarLoanBean item = (CarLoanBean) adapter.getItem(position);
                if (item.getName().equals("平安车抵贷")) {
                    Intent intent = new Intent();
                    intent.putExtra(Constant.KEY_ID, String.valueOf(item.getId()));
                    ActivityManage.push(CarLoanSubmitAct.class, intent);
                }
            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setAdapter(mAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getData(0);
    }

    @Override
    public int getLayoutId() {
        return R.layout.common_list_no_more;
    }

    private void getData(int page) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("pageIndex", page);
        map.put("pageSize", Constant.DEFULT_RESULT);
        NetApi.get(UrlKit.CAR_LOAN_PARTNER, map, new JsonCallback() {

            @Override
            public void onSuccess(String response, int id) {
                PageBean<CarLoanBean> data = new Gson().fromJson(response, new TypeToken<PageBean<CarLoanBean>>() {
                }.getType());
                if (data != null && data.getItems() != null) {
                    if (page == 0) {
                        mAdapter.getData().clear();
                    }
                    if (data.getItems().size() > 0) {
                        mAdapter.addData(data.getItems());
                    }
                }
            }
        });
    }
}
