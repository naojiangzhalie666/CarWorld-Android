package com.liansheng.carworld.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.liansheng.carworld.R;
import com.liansheng.carworld.fragment.OrderSearchFragment;
import com.liansheng.carworld.view.CWViewPager;
import com.qmuiteam.qmui.widget.QMUITopBar;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.droidlover.xdroidbase.base.XFragmentAdapter;

public class OrderSearchActivity extends BaseActivity {


    @BindView(R.id.btn_back)
    ImageView btnBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tab_layout)
    TabLayout tabLayoutSortFilter;
    @BindView(R.id.viewpage)
    CWViewPager viewpage;
    public static int pageCount = 0;
    List<Fragment> fragmentList = new ArrayList<>();
    String[] titles = {"出发地", "目的地"};
    XFragmentAdapter adapter;
    public static String city = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_list);
        ButterKnife.bind(this);
        Intent i = getIntent();
        city = i.getStringExtra("city");
//        showToast(city);
        tvTitle.setText("搜索订单");
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        initViewPager();
        pageCount = 0;//每次进来初始化页数
    }

    private void initViewPager() {
        fragmentList.clear();
        fragmentList.add(OrderSearchFragment.newInstance());
        fragmentList.add(OrderSearchFragment.newInstance());
        if (adapter == null) {
            adapter = new XFragmentAdapter(getSupportFragmentManager(), fragmentList, titles);
        }
        viewpage.setAdapter(adapter);
        viewpage.setOffscreenPageLimit(1);
        tabLayoutSortFilter.setupWithViewPager(viewpage);
    }

    public ViewPager getVp() {
        return viewpage;
    }

    @Override
    public void initData(Bundle savedInstanceState) {

    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_order_list;
    }//布局文件

}
