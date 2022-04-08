package com.liansheng.carworld.activity;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.liansheng.carworld.R;
import com.liansheng.carworld.fragment.OrderListFragment;
import com.liansheng.carworld.utils.map.GDMapUtils;
import com.liansheng.carworld.view.CWViewPager;
import com.zaaach.citypicker.CityPicker;
import com.zaaach.citypicker.adapter.OnPickListener;
import com.zaaach.citypicker.model.City;
import com.zaaach.citypicker.model.HotCity;
import com.zaaach.citypicker.model.LocateState;
import com.zaaach.citypicker.model.LocatedCity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.droidlover.xdroidbase.base.XFragmentAdapter;
import pub.devrel.easypermissions.EasyPermissions;

public class OrderListActivity extends BaseActivity {


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
        btnFunction.setVisibility(View.GONE);
        btnFunction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (EasyPermissions.hasPermissions(OrderListActivity.this, PERMS_LOACTION_STATE)) {
                    new GDMapUtils().location(aMapLocation -> {
                        c = aMapLocation.getCity();
                        p = aMapLocation.getProvince();
                        code = aMapLocation.getAdCode();
                    });
                    search();
                } else {
                    EasyPermissions.requestPermissions(OrderListActivity.this, "我们需要您的位置信息，请点击确定打开位置权限.",
                            PER_LOACTION, PERMS_LOACTION_STATE);
                }
            }
        });
//        location();
        initViewPager();
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_order_list;
    }

    private void search() {
        List<HotCity> hotCities = new ArrayList<>();
        hotCities.add(new HotCity("北京", "北京", "101010100")); //code为城市代码
        hotCities.add(new HotCity("上海", "上海", "101020100"));
        hotCities.add(new HotCity("广州", "广东", "101280101"));
        hotCities.add(new HotCity("深圳", "广东", "101280601"));
        hotCities.add(new HotCity("杭州", "浙江", "101210101"));
        CityPicker.from(this) //activity或者fragment
                .enableAnimation(true)    //启用动画效果，默认无
                .setHotCities(hotCities)    //指定热门城市
//                .setLocatedCity(null)
                .setOnPickListener(new OnPickListener() {
                    @Override
                    public void onPick(int position, City data) {
                        String city = data.getName();
                        if (data.getName() != null) {
                            if (!data.getName().endsWith("市")) {
                                city = data.getName() + "市";
                            }
                            Bundle bundle = new Bundle();
                            bundle.putString("city", city);
                            toActivity(OrderSearchActivity.class, bundle);
                        } else {
                            showToast("请重新选择城市");
                        }
                    }

                    @Override
                    public void onLocate() {
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                //定位完成之后更新数据
                                CityPicker.from(OrderListActivity.this).locateComplete(new LocatedCity(c, p, code), LocateState.SUCCESS);
                            }
                        }, 5000);
                    }

                    @Override
                    public void onCancel() {
                    }
                })
                .show();
    }

    private void initViewPager() {
        fragmentList.clear();
        fragmentList.add(OrderListFragment.newInstance("", false));
        fragmentList.add(OrderListFragment.newInstance("processed", false));
        fragmentList.add(OrderListFragment.newInstance("origin,transportation", false));
        fragmentList.add(OrderListFragment.newInstance("destination", false));
        fragmentList.add(OrderListFragment.newInstance("", true));
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
