package com.liansheng.carworld.activity.home;

import android.os.Bundle;
import android.os.Handler;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.jpeng.jptabbar.JPTabBar;
import com.jpeng.jptabbar.OnTabSelectListener;
import com.jpeng.jptabbar.anno.NorIcons;
import com.jpeng.jptabbar.anno.SeleIcons;
import com.jpeng.jptabbar.anno.Titles;
import com.liansheng.carworld.R;
import com.liansheng.carworld.activity.BaseActivity;
import com.liansheng.carworld.activity.logic.UserLogic;
import com.liansheng.carworld.fragment.CompanyFrag;
import com.liansheng.carworld.fragment.EmptyFrag;
import com.liansheng.carworld.fragment.HomesFragment;
import com.liansheng.carworld.fragment.IMFrag;
import com.liansheng.carworld.fragment.IMMsgFrag;
import com.liansheng.carworld.fragment.MeFragment;
import com.liansheng.carworld.fragment.MessageFragment;
import com.liansheng.carworld.view.CWViewPager;
import com.liansheng.carworld.view.pop.HomeAddPop;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import cn.droidlover.xdroid.tools.utils.Util;

public class IMAct extends BaseActivity {

    @BindView(R.id.vp)
    CWViewPager vpMain;
    @BindView(R.id.tabbar)
    JPTabBar tabbar;

    @Titles
    private static final String[] mTitles = {"聊天室", "消息", "我"};
    @SeleIcons
    private static final int[] mSeleIcons = {R.mipmap.icon_home_s, R.mipmap.icon_message_s, R.mipmap.icon_me_s};
    @NorIcons
    private static final int[] mNormalIcons = {R.mipmap.icon_home, R.mipmap.icon_message, R.mipmap.icon_me};
    private VpAdapter adapter;

    @Override
    public void initData(Bundle savedInstanceState) {
        initView();
    }

    private void initView() {
        ArrayList<Fragment> fragments = new ArrayList<>();
        fragments.add(new IMFrag());
        fragments.add(new IMMsgFrag());
        fragments.add(new IMFrag());
        vpMain.setScrollEnable(false);
        adapter = new VpAdapter(getSupportFragmentManager(), fragments);
        vpMain.setAdapter(adapter);
        vpMain.setOffscreenPageLimit(fragments.size());
        tabbar.setContainer(vpMain);
        tabbar.setTabListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int index) {
//                if (index > 0) {
//                    if (UserLogic.isLogin()) {
//                        if (index == 1 && tabbar.isBadgeShow(1)) {
//                            Fragment fragment = adapter.getItem(1);
//                            if (fragment instanceof MessageFragment) {
//                                ((MessageFragment) fragment).getMsg(0);
//                            }
//                        }
//                    } else {
//                        Util.toast("请先登录");
//                        new Handler().postDelayed(new Runnable() {
//                            @Override
//                            public void run() {
//                                tabbar.setSelectTab(0);
//                                vpMain.setCurrentItem(0);
//                            }
//                        }, 300);
//                    }
//                }

            }

            @Override
            public boolean onInterruptSelect(int index) {
                return false;
            }
        });
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

    @Override
    public int getLayoutId() {
        return R.layout.act_im;
    }
}
