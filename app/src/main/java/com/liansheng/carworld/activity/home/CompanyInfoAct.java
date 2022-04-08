package com.liansheng.carworld.activity.home;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
import com.liansheng.carworld.R;
import com.liansheng.carworld.activity.BaseActivity;
import com.liansheng.carworld.activity.logic.OtherLogic;
import com.liansheng.carworld.bean.UserInfo;
import com.liansheng.carworld.bean.me.CompanyBean;
import com.liansheng.carworld.fragment.FindCarFrag;
import com.liansheng.carworld.fragment.OrderChildListFrag;
import com.liansheng.carworld.fragment.SaleCarFrag;
import com.liansheng.carworld.kit.Constant;
import com.liansheng.carworld.net.JsonCallback;
import com.liansheng.carworld.net.NetApi;
import com.liansheng.carworld.net.UrlKit;
import com.liansheng.carworld.view.CWViewPager;
import com.liansheng.carworld.view.ShareDialog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import cn.droidlover.xdroid.hold.info.SharedInfo;
import cn.droidlover.xdroid.tools.log.Logger;
import cn.droidlover.xdroid.tools.utils.ActivityManage;
import cn.droidlover.xdroid.tools.utils.BitmapUtil;
import cn.droidlover.xdroid.tools.utils.StringFormat;
import cn.droidlover.xdroidbase.base.XFragmentAdapter;

public class CompanyInfoAct extends BaseActivity {

    @BindView(R.id.company_name)
    TextView company_name;
    @BindView(R.id.company_address)
    TextView company_address;
    @BindView(R.id.company_type)
    TextView company_type;
    @BindView(R.id.company_zy)
    TextView company_zy;
    @BindView(R.id.tv_zk)
    TextView tvZk;
    @BindView(R.id.tv_sj)
    TextView tvSj;
    @BindView(R.id.tv_num)
    TextView tvNum;
    @BindView(R.id.tv_company_type)
    TextView tv_company_type;
    @BindView(R.id.tab_layout)
    TabLayout tabLayoutSortFilter;
    @BindView(R.id.viewpage)
    CWViewPager viewpage;
    private CompanyBean companyBean;
    List<Fragment> fragmentList = new ArrayList<>();
    String[] titles = {"所有订单", "待发车", "发车中", "已送达", "待评价"};
    String[] titleBar = {"批发中车源", "车友圈"};

    @Override
    public void initData(Bundle savedInstanceState) {
        companyBean = getIntent().getParcelableExtra(Constant.KEY_BEAN);
        if (companyBean == null) {
            initViewPager(1);
            findViewById(R.id.company_ll).setVisibility(View.GONE);
            findViewById(R.id.btn_share).setVisibility(View.GONE);
            return;
        }
        company_name.setText(companyBean.getName());
        if (companyBean.getAddress() != null) {
            company_address.setText(companyBean.getAddress().getAddress());
        }
        if (Constant.STORE_TYPE_2.equals(companyBean.getType())) {
            company_type.setText("4S店");
            if (!TextUtils.isEmpty(companyBean.getMainBrand()))
                company_zy.setText(companyBean.getMainBrand());
            initViewPager(2);
        } else if (Constant.STORE_TYPE_1.equals(companyBean.getType())) {
            company_zy.setVisibility(View.INVISIBLE);
            if (companyBean.isSecondHandPriority()) {
                company_type.setText("汽贸店-主营二手车");
            } else {
                company_type.setText("汽贸店-主营新车");
            }
            initViewPager(1);
        } else if (Constant.STORE_TYPE_3.equals(companyBean.getType())) {
            company_type.setText("物流公司");
            findViewById(R.id.tv_manager).setVisibility(View.VISIBLE);
            company_zy.setVisibility(View.INVISIBLE);
            tv_company_type.setText("调度订单");
            initViewPager(3);
        }
        getCompanyInfo(String.valueOf(companyBean.getId()));
    }

    @Override
    public int getLayoutId() {
        return R.layout.act_company_info;
    }


    private void getCompanyInfo(String id) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("companyId", id);
        NetApi.get(UrlKit.CAR_RESOURCE_COUNT, map, new JsonCallback() {

            @Override
            public void onSuccess(String response, int id) {
                Map<String, Integer> map = new Gson().fromJson(response, Map.class);
                if (map != null) {
                    tvZk.setText(StringFormat.subZeroAndDot(map.get("all")));
                    tvSj.setText(StringFormat.subZeroAndDot(map.get("on")));
                    tvNum.setText(StringFormat.subZeroAndDot(map.get("memberCounts")));
//                    tvDownCount.setText("自销( " + StringFormat.subZeroAndDot(map.get("off")) + " )");
                }
            }
        });
    }


    @OnClick({R.id.btn_back, R.id.tv_manager, R.id.company_address, R.id.btn_share})
    public void onViewClick(View view) {
        switch (view.getId()) {
            case R.id.btn_back:
                finish();
                break;
            case R.id.btn_share:
                showShare(BitmapUtil.readBitmap(context, R.mipmap.ic_store_default));
                break;
            case R.id.tv_manager:
                ActivityManage.push(DriverManagerAct.class);
                break;
            case R.id.company_address:
                if (companyBean.getAddress() != null)
                    OtherLogic.showNav(context, companyBean.getAddress().getLongitude(), companyBean.getAddress().getLatitude(), companyBean.getAddress().getName());
                break;
        }
    }

    private void showShare(Bitmap bmp) {
        UserInfo userInfo = SharedInfo.getInstance().getEntity(UserInfo.class);
        new ShareDialog(context, String.format(UrlKit.H5_SHARE_COMPANY, companyBean.getId(), userInfo.getId())
                , companyBean.getName() + "的车辆商铺", "\n内有本公司大量精品车源",
                "分享微信将显示所有车辆的<font color=\"#ff3357\">零售价</font>",
                bmp).show();
    }

    private void initViewPager(int type) {
        if (type == 3) {
            fragmentList.add(OrderChildListFrag.newInstance(""));
            fragmentList.add(OrderChildListFrag.newInstance("processed"));
            fragmentList.add(OrderChildListFrag.newInstance("origin,transportation"));
            fragmentList.add(OrderChildListFrag.newInstance("destination"));
            viewpage.setAdapter(new XFragmentAdapter(getSupportFragmentManager(), fragmentList, titles));
        } else {
            fragmentList.add(SaleCarFrag.newInstance(getIntent().getStringExtra(Constant.KEY_ID)));
            fragmentList.add(FindCarFrag.newInstance(getIntent().getStringExtra(Constant.KEY_ID)));
            viewpage.setAdapter(new XFragmentAdapter(getSupportFragmentManager(), fragmentList, titleBar));
        }
        viewpage.setOffscreenPageLimit(fragmentList.size());
        tabLayoutSortFilter.setupWithViewPager(viewpage);
        viewpage.setCurrentItem(getIntent().getIntExtra(Constant.KEY_POSITION, 0));
    }

    public void setTabTitle(String title, int index) {
        tabLayoutSortFilter.getTabAt(index).setText(title);
    }
}
