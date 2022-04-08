package com.liansheng.carworld.activity.home;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import com.google.android.material.tabs.TabLayout;
import com.liansheng.carworld.R;
import com.liansheng.carworld.activity.BaseActivity;
import com.liansheng.carworld.fragment.FindCarFrag;
import com.liansheng.carworld.fragment.SaleCarFrag;
import com.liansheng.carworld.view.CWViewPager;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import cn.droidlover.xdroidbase.base.XFragmentAdapter;

public class CarPushAct extends BaseActivity {

    @BindView(R.id.tab_layout)
    TabLayout tabLayoutSortFilter;
    @BindView(R.id.viewpage)
    CWViewPager viewpage;
    List<Fragment> fragmentList = new ArrayList<>();
    String[] titleBar = {"出售", "求购"};

    @Override
    public void initData(Bundle savedInstanceState) {
        initViewPager();
    }

    @Override
    public int getLayoutId() {
        return R.layout.act_car_push;
    }

    private void initViewPager() {
        fragmentList.add(SaleCarFrag.newInstance("1"));
        fragmentList.add(FindCarFrag.newInstance("1"));
        viewpage.setAdapter(new XFragmentAdapter(getSupportFragmentManager(), fragmentList, titleBar));
        viewpage.setOffscreenPageLimit(fragmentList.size());
        tabLayoutSortFilter.setupWithViewPager(viewpage);
    }
}
