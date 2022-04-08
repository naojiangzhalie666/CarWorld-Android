package com.liansheng.carworld.activity.home;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.liansheng.carworld.R;
import com.liansheng.carworld.activity.BaseActivity;
import com.liansheng.carworld.fragment.SHHistoryFrag;
import com.liansheng.carworld.fragment.SHSearchFrag;
import com.liansheng.carworld.kit.Constant;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import cn.droidlover.xdroid.views.appbar.ToolBar;
import cn.droidlover.xdroidbase.base.XFragmentAdapter;

public class SecondhandSearchAct extends BaseActivity {

    @BindView(R.id.toolbar)
    ToolBar toolBar;
    @BindView(R.id.tab_layout)
    TabLayout tableLayout;
    @BindView(R.id.view_page)
    ViewPager viewpage;
    XFragmentAdapter adapter;
    private int mType;
    private SHSearchFrag frag;

    @Override
    public void initData(Bundle savedInstanceState) {
        initViewPager();
    }

    @Override
    public int getLayoutId() {
        return R.layout.act_second_hand_search;
    }

    private void initViewPager() {
        mType = getIntent().getIntExtra(Constant.KEY_TYPE, 1);
        List<Fragment> fragmentList = new ArrayList<>();
        String[] titles = {"", "历史查询"};
        if (mType == 1) {
            toolBar.setTitle("维保查询");
                titles[0]="维保查询";
        } else if (mType == 2) {
            toolBar.setTitle("碰撞查询");
            titles[0]="碰撞查询";
        } else if (mType == 3) {
            toolBar.setTitle("综合查询");
            titles[0]="维保+碰撞";
        }
        frag = SHSearchFrag.newInstance(mType,getIntent().getStringExtra(Constant.KEY_TITLE));
        fragmentList.add(frag);
        fragmentList.add(SHHistoryFrag.newInstance(mType));
        if (adapter == null) {
            adapter = new XFragmentAdapter(getSupportFragmentManager(), fragmentList, titles);
        }
        viewpage.setAdapter(adapter);
        viewpage.setOffscreenPageLimit(4);
        tableLayout.setupWithViewPager(viewpage);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(frag!=null){
            frag.onActivityResult(requestCode,resultCode,data);
        }
    }
}
