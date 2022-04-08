package com.liansheng.carworld.activity.circle;

import android.os.Bundle;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.liansheng.carworld.R;
import com.liansheng.carworld.activity.BaseActivity;
import com.liansheng.carworld.adapter.home.CircleAdapter;
import com.liansheng.carworld.bean.home.CircleBean;
import com.liansheng.carworld.kit.Constant;

import java.util.ArrayList;

import butterknife.BindView;
import cn.droidlover.xrecyclerview.XRecyclerContentLayout;

public class CircleDetailsAct extends BaseActivity {

    @BindView(R.id.refresh)
    SwipeRefreshLayout refreshLayout;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    private CircleAdapter mAdapter;

    @Override
    public void initData(Bundle savedInstanceState) {
        CircleBean item = getIntent().getParcelableExtra(Constant.KEY_BEAN);
        refreshLayout.setEnabled(false);
        if(item!=null){
            ArrayList<CircleBean> list=new ArrayList<>();
            list.add(item);
            mAdapter = new CircleAdapter(3);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            recyclerView.setAdapter(mAdapter);
            mAdapter.replaceData(list);
        }
    }

    @Override
    public int getLayoutId() {
        return R.layout.common_list_no_more;
    }
}
