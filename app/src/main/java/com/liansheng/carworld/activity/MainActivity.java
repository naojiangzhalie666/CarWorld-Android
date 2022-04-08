package com.liansheng.carworld.activity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.gson.Gson;
import com.jpeng.jptabbar.JPTabBar;
import com.jpeng.jptabbar.OnTabSelectListener;
import com.jpeng.jptabbar.anno.NorIcons;
import com.jpeng.jptabbar.anno.SeleIcons;
import com.jpeng.jptabbar.anno.Titles;
import com.liansheng.carworld.R;
import com.liansheng.carworld.activity.logic.UserLogic;
import com.liansheng.carworld.bean.MessageCount;
import com.liansheng.carworld.bean.other.OssBean;
import com.liansheng.carworld.fragment.CircleFrag;
import com.liansheng.carworld.fragment.CompanyFrag;
import com.liansheng.carworld.fragment.EmptyFrag;
import com.liansheng.carworld.fragment.HomesFragment;
import com.liansheng.carworld.fragment.MeFragment;
import com.liansheng.carworld.net.JsonCallback;
import com.liansheng.carworld.net.NetApi;
import com.liansheng.carworld.net.UrlKit;
import com.liansheng.carworld.oss.OssServiceUtil;
import com.liansheng.carworld.utils.Utils;
import com.liansheng.carworld.view.CWViewPager;
import com.liansheng.carworld.view.pop.HomeAddPop;
import com.pgyersdk.update.PgyUpdateManager;
import com.pgyersdk.update.javabean.AppBean;
import com.xuexiang.xui.utils.StatusBarUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.droidlover.xdroid.tools.utils.ActivityManage;
import cn.droidlover.xdroid.tools.utils.Util;
import me.wangyuwei.loadingview.LoadingView;

public class MainActivity extends BaseActivity {

    @BindView(R.id.vp)
    CWViewPager vpMain;
    @BindView(R.id.tabbar)
    JPTabBar tabbar;
    @BindView(R.id.tv_progress)
    TextView tvProgress;
    @BindView(R.id.loading_view)
    LoadingView loadingView;
    @BindView(R.id.llyt_update)
    LinearLayout llytUpdate;
    //    @BindView(R.id.tabbar)
//    BottomNavigationViewEx bnve;
    private List<Fragment> fragments;
    private VpAdapter adapter;
    private AppBean appBean;
    MeFragment me;
    CompanyFrag companyFrag;
    @Titles
    private static final String[] mTitles = {"二手车", "车友圈", " ", "我的公司", "我的"};
    @SeleIcons
    private static final int[] mSeleIcons = {R.mipmap.icon_home_s, R.mipmap.icon_message_s,
            R.color.transparent,
            R.mipmap.icon_company_s, R.mipmap.icon_me_s};
    @NorIcons
    private static final int[] mNormalIcons = {R.mipmap.icon_home, R.mipmap.icon_message,
            R.color.transparent,
            R.mipmap.icon_company, R.mipmap.icon_me};

    private HomeAddPop addPop;

    @Override
    public void initData(Bundle savedInstanceState) {
        initView();
        getParams();
    }

    private void getParams() {
        NetApi.get(UrlKit.OSS_CONFIG, null, new JsonCallback() {

            @Override
            public void onSuccess(String response, int id) {
                OssBean ossBean = new Gson().fromJson(response, OssBean.class);
                if (!TextUtils.isEmpty(ossBean.getAccessKeyId())) {
                    OssServiceUtil.getInstance().init(ossBean);
                }
            }
        });
    }


    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

    private void initView() {
        fragments = new ArrayList<>();
        me = new MeFragment();
        fragments.add(HomesFragment.newInstance());
        fragments.add(CircleFrag.newInstance());
        companyFrag = CompanyFrag.newInstance();
        fragments.add(EmptyFrag.newInstance());
        fragments.add(companyFrag);
        fragments.add(me);
        vpMain.setScrollEnable(false);
        adapter = new VpAdapter(getSupportFragmentManager(), fragments);
        vpMain.setAdapter(adapter);
        vpMain.setOffscreenPageLimit(fragments.size());
        tabbar.setContainer(vpMain);
        tabbar.setTabListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int index) {
                if (index > 0) {
                    if (!UserLogic.isLogin()) {
                        Util.toast("请先登录");
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                tabbar.setSelectTab(0);
                                vpMain.setCurrentItem(0);
                            }
                        }, 300);
                        me.checkLogin();
                    }
                }
                if(index==4){
                    StatusBarUtils.setStatusBarDarkMode(context);
                }else {
                    StatusBarUtils.setStatusBarLightMode(context);
                }

            }

            @Override
            public boolean onInterruptSelect(int index) {
                return false;
            }
        });
        addPop = new HomeAddPop(this);
    }

    @OnClick(R.id.home_show)
    public void onClick(View view) {
        if (UserLogic.isLogin()) {
            if (!addPop.isShowing()) {
                addPop.showAtLocation(view, Gravity.BOTTOM, 0, 0);
            }
        } else {
            me.checkLogin();
        }
    }

    public void setTab(int index) {
        tabbar.setSelectTab(index);
    }

    public void getUserInfo() {
        if (companyFrag != null) {
            companyFrag.getUserInfo();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
//        if (UserLogic.isLogin()) {
//            getMessageCount();
//        }
    }

    public void getMessageCount() {
        NetApi.get(UrlKit.MESSAGECOUNT, null, new JsonCallback() {

            @Override
            public void onSuccess(String response, int id) {
                MessageCount msg = new Gson().fromJson(response, MessageCount.class);
                if (msg.getCounts() > 0) {
                    tabbar.showBadge(1, msg.getCounts() + "", true);
                }
            }
        });
    }

    public void clearBadge() {
        tabbar.hideBadge(1);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        switch (requestCode) {
            case 991:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //这里已经获取到了存储的权限，想干嘛干嘛了可以
                    PgyUpdateManager.downLoadApk(appBean.getDownloadURL());

                } else {
                    //这里是拒绝给APP存储权限，给个提示什么的说明一下都可以。
                    showToast("请手动打开存储权限,才能更新App");
                }
                break;
            default:
                break;
        }
    }

    public ViewPager getVp() {
        return vpMain;
    }

    private static class VpAdapter extends FragmentPagerAdapter {
        private List<Fragment> data;

        public VpAdapter(FragmentManager fm, List<Fragment> data) {
            super(fm);
            this.data = data;
        }

        @Override
        public int getCount() {
            return data.size();
        }

        @Override
        public Fragment getItem(int position) {
            return data.get(position);
        }
    }

    private long firstTime = 0;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            if (addPop != null && addPop.isShowing()) {
                addPop.dismiss();
                return true;
            }
            if (System.currentTimeMillis() - firstTime > 2000) {
                Utils.showToast("再按一次退出程序");
                firstTime = System.currentTimeMillis();
            } else {
                finish();
                ActivityManage.finishAll();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (me != null) {
            me.onActivityResult(requestCode, resultCode, data);
        }
        if (companyFrag != null) {
            companyFrag.onActivityResult(requestCode, resultCode, data);
        }
    }


}
