package com.liansheng.carworld.activity.sub;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.google.android.material.tabs.TabLayout;
import com.liansheng.carworld.R;
import com.liansheng.carworld.activity.BaseActivity;
import com.liansheng.carworld.view.CWViewPager;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import cn.droidlover.xdroidbase.base.XFragmentAdapter;

public class SubOrderListAct extends BaseActivity {


    @BindView(R.id.btn_back)
    ImageView btnBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tab_layout)
    TabLayout tabLayoutSortFilter;
    @BindView(R.id.viewpage)
    CWViewPager viewpage;
    List<Fragment> fragmentList = new ArrayList<>();
    String[] titles = {"所有订单", "待发车", "发车中", "已送达", "待评价"};
    XFragmentAdapter adapter;
    public String p;
    public String c;
    public String code;
    public int jump = 0;
    @BindView(R.id.btn_function)
    ImageView btnFunction;


    @Override
    public void initData(Bundle savedInstanceState) {
        jump = getIntent().getIntExtra("jump", 0);
        tvTitle.setText("订单列表");
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
//        location();
        initViewPager();
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_order_list;
    }


    private void initViewPager() {
        fragmentList.clear();
        fragmentList.add(SubOrderListFrag.newInstance(""));
        fragmentList.add(SubOrderListFrag.newInstance("processed"));
        fragmentList.add(SubOrderListFrag.newInstance("origin,transportation"));
        fragmentList.add(SubOrderListFrag.newInstance("destination"));
        if (adapter == null) {
            adapter = new XFragmentAdapter(getSupportFragmentManager(), fragmentList, titles);
        }
        viewpage.setAdapter(adapter);
        viewpage.setOffscreenPageLimit(fragmentList.size());
        tabLayoutSortFilter.setupWithViewPager(viewpage);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                viewpage.setCurrentItem(jump);
            }
        },300);
    }

}
