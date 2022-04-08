package com.liansheng.carworld.activity.me;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.bumptech.glide.Glide;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
import com.liansheng.carworld.R;
import com.liansheng.carworld.activity.BaseActivity;
import com.liansheng.carworld.bean.UserInfo;
import com.liansheng.carworld.bean.event.UIEvent;
import com.liansheng.carworld.bean.me.LicensesBean;
import com.liansheng.carworld.fragment.CollectionDirectoryFrag;
import com.liansheng.carworld.fragment.CollectionSecondFrag;
import com.liansheng.carworld.kit.Constant;
import com.liansheng.carworld.net.JsonCallback;
import com.liansheng.carworld.net.NetApi;
import com.liansheng.carworld.net.UrlKit;
import com.liansheng.carworld.utils.DateParserHelper;
import com.liansheng.carworld.view.CWViewPager;

import androidx.fragment.app.Fragment;

import org.greenrobot.eventbus.EventBus;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import cn.droidlover.xdroid.hold.info.SharedInfo;
import cn.droidlover.xdroid.tools.utils.Util;
import cn.droidlover.xdroidbase.base.XFragmentAdapter;
import cn.jpush.android.api.JPushInterface;
import okhttp3.Call;

public class CollectionActivity extends BaseActivity {

    @BindView(R.id.tab_layout)
    TabLayout tabLayoutSortFilter;
    @BindView(R.id.viewpage)
    CWViewPager viewpage;
    String[] titles = {"4S名录", "二手车"};
    XFragmentAdapter adapter;

    @Override
    public void initData(Bundle savedInstanceState) {
        initViewPager();
        getUserInfo();
    }

    @Override
    public int getLayoutId() {
        return R.layout.act_collection;
    }


    private void initViewPager() {
        List<Fragment> fragmentList = new ArrayList<>();
        fragmentList.add(CollectionDirectoryFrag.newInstance());
        fragmentList.add(CollectionSecondFrag.newInstance());
        if (adapter == null) {
            adapter = new XFragmentAdapter(getSupportFragmentManager(), fragmentList, titles);
        }
        viewpage.setAdapter(adapter);
        viewpage.setOffscreenPageLimit(fragmentList.size());
        tabLayoutSortFilter.setupWithViewPager(viewpage);
    }

    public void getUserInfo() {
        NetApi.get(UrlKit.USER_INFORM, null, new JsonCallback() {

            @Override
            public void onSuccess(String response, int id) {
                Gson gson = new Gson();
                UserInfo userInfo = gson.fromJson(response, UserInfo.class);
                if (userInfo == null) {
                    Util.toast("用户信息获取失败");
                    return;
                }
                tabLayoutSortFilter.getTabAt(0).setText("4S名录（" + userInfo.getDirectoryCollectionCounts() + "）");
                tabLayoutSortFilter.getTabAt(1).setText("二手车（" + userInfo.getCarResourceCollectionCounts() + "）");
            }
        });
    }

}
