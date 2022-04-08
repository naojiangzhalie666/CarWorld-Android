package com.liansheng.carworld.activity.company;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.DrawableUtils;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;
import com.google.android.material.tabs.TabLayout;
import com.liansheng.carworld.R;
import com.liansheng.carworld.activity.BaseActivity;
import com.liansheng.carworld.bean.UserInfo;
import com.liansheng.carworld.bean.me.CompanyBean;
import com.liansheng.carworld.fragment.NewCarFrag;
import com.liansheng.carworld.fragment.SecondCardFrag;
import com.liansheng.carworld.net.UrlKit;
import com.liansheng.carworld.view.CWViewPager;
import com.liansheng.carworld.view.ShareDialog;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import cn.droidlover.xdroid.hold.info.SharedInfo;
import cn.droidlover.xdroid.tools.utils.ActivityManage;
import cn.droidlover.xdroid.tools.utils.BitmapUtil;
import cn.droidlover.xdroid.views.appbar.TitleBar;
import cn.droidlover.xdroid.views.appbar.ToolBar;
import cn.droidlover.xdroidbase.base.XFragmentAdapter;

public class CompanyManagerAct extends BaseActivity {


    @BindView(R.id.btn_back)
    ImageView btnBack;
    @BindView(R.id.tab_layout)
    TabLayout tabLayoutSortFilter;
    @BindView(R.id.viewpage)
    CWViewPager viewpage;
    List<Fragment> fragmentList = new ArrayList<>();
    String[] titles = {"二手车", "新车"};
    XFragmentAdapter adapter;


    @Override
    public void initData(Bundle savedInstanceState) {
        btnBack.setOnClickListener(v -> finish());
        findViewById(R.id.btn_share).setOnClickListener(v -> {
//            UserInfo userInfo = SharedInfo.getInstance().getEntity(UserInfo.class);
//            CompanyBean companyBean = userInfo.getCompanys().get(0);
//            if (TextUtils.isEmpty(companyBean.getFrontDoorPhoto()) && (companyBean.getFrontDoorPhotos() == null || companyBean.getFrontDoorPhotos().size() == 0)) {
                showShare(BitmapUtil.readBitmap(context, R.mipmap.ic_store_default));
//            } else {
//                if (!TextUtils.isEmpty(companyBean.getFrontDoorPhoto())) {
//                    Glide.with(context).asBitmap().load(UrlKit.getImgUrl(companyBean.getFrontDoorPhoto())).into(new SimpleTarget<Bitmap>() {
//                        @Override
//                        public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
//                            showShare(resource);
//                        }
//                    });
//                } else if (companyBean.getFrontDoorPhotos() != null && companyBean.getFrontDoorPhotos().size() > 0) {
//                    Glide.with(context).asBitmap().load(UrlKit.getImgUrl(companyBean.getFrontDoorPhotos().get(0))).into(new SimpleTarget<Bitmap>() {
//                        @Override
//                        public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
//                            showShare(resource);
//                        }
//                    });
//                }
//            }
        });
        initViewPager();
    }

    private void showShare(Bitmap bmp) {
        UserInfo userInfo = SharedInfo.getInstance().getEntity(UserInfo.class);
        CompanyBean companyBean = userInfo.getCompanys().get(0);
        new ShareDialog(context, String.format(UrlKit.H5_SHARE_COMPANY, companyBean.getId(), userInfo.getId())
                , companyBean.getName() + "的车辆商铺", "\n内有本公司大量精品车源",
                "分享微信将显示所有车辆的<font color=\"#ff3357\">零售价</font>",
                bmp).show();
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_company_manager;
    }

    private void initViewPager() {
        fragmentList.add(SecondCardFrag.newInstance());
        fragmentList.add(NewCarFrag.newInstance());
        adapter = new XFragmentAdapter(getSupportFragmentManager(), fragmentList, titles);
        viewpage.setAdapter(adapter);
        viewpage.setOffscreenPageLimit(fragmentList.size());
        tabLayoutSortFilter.setupWithViewPager(viewpage);
        viewpage.setScrollEnable(false);
    }

}
