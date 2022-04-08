package com.liansheng.carworld.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.kongzue.dialog.v3.MessageDialog;
import com.liansheng.carworld.R;
import com.liansheng.carworld.activity.MainActivity;
import com.liansheng.carworld.activity.company.CompanyManagerAct;
import com.liansheng.carworld.activity.company.MembersManagerAct;
import com.liansheng.carworld.activity.home.DriverManagerAct;
import com.liansheng.carworld.activity.logic.OtherLogic;
import com.liansheng.carworld.activity.logic.UserLogic;
import com.liansheng.carworld.activity.me.InfoAuthAct;
import com.liansheng.carworld.bean.MemberBean;
import com.liansheng.carworld.bean.UserInfo;
import com.liansheng.carworld.bean.company.SumBean;
import com.liansheng.carworld.bean.event.UIEvent;
import com.liansheng.carworld.bean.me.CompanyBean;
import com.liansheng.carworld.bean.other.PageBean;
import com.liansheng.carworld.kit.Constant;
import com.liansheng.carworld.net.JsonCallback;
import com.liansheng.carworld.net.NetApi;
import com.liansheng.carworld.net.UrlKit;
import com.liansheng.carworld.oss.OssServiceUtil;
import com.liansheng.carworld.utils.GlideEngine;
import com.liansheng.carworld.utils.Utils;
import com.liansheng.carworld.view.CWViewPager;
import com.liansheng.carworld.view.LineGraphicView;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.entity.LocalMedia;
import com.qmuiteam.qmui.widget.QMUITopBar;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import cn.droidlover.xdroid.hold.info.SharedInfo;
import cn.droidlover.xdroid.tools.log.Logger;
import cn.droidlover.xdroid.tools.utils.ActivityManage;
import cn.droidlover.xdroid.tools.utils.ContextHolder;
import cn.droidlover.xdroid.tools.utils.NetworkUtil;
import cn.droidlover.xdroid.tools.utils.Util;
import cn.droidlover.xdroidbase.base.XFragmentAdapter;
import okhttp3.Call;

public class CompanyFrag extends BaseFragment {


    @BindView(R.id.topbar)
    QMUITopBar toolBar;
    @BindView(R.id.company_none_ll)
    RelativeLayout noneLl;
    @BindView(R.id.company_tv1)
    TextView companyT1;
    @BindView(R.id.company_name)
    TextView company_name;
    @BindView(R.id.company_address)
    TextView company_address;
    @BindView(R.id.company_type)
    TextView company_type;
    @BindView(R.id.company_zy)
    TextView company_zy;
    @BindView(R.id.company_imgs)
    RecyclerView companyImgs;
    @BindView(R.id.company_type_0)
    RelativeLayout companyType0;
    @BindView(R.id.company_type_1)
    LinearLayout companyType1;
    @BindView(R.id.company_type_2)
    RelativeLayout companyType2;
    @BindView(R.id.tab_layout)
    TabLayout tabLayoutSortFilter;
    @BindView(R.id.viewpage)
    CWViewPager viewpage;
    @BindView(R.id.line_view)
    LineGraphicView lineView;
    @BindView(R.id.company_member_count)
    TextView memberCount;
    private List<Fragment> fragmentList = new ArrayList<>();
    String[] titles = {"所有订单", "待发车", "发车中", "已送达", "待评价"};
    private int type = 1;// 1 汽贸 2 物流
    @BindView(R.id.refresh)
    SwipeRefreshLayout refreshLayout;
    CompanyBean currentCompany;
    private MyAdapter myAdapter;

    public static CompanyFrag newInstance() {
        Bundle args = new Bundle();
        CompanyFrag fragment = new CompanyFrag();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        EventBus.getDefault().register(this);
        toolBar.setTitle("我的公司");
        initTrans();
//        toolBar.addAction(new TitleBar.ImageAction(R.mipmap.icon_settings) {
//            @Override
//            public void performAction(View view) {
//                if (currentCompany != null) {
//                    Intent intent = new Intent();
//                    intent.putExtra(Constant.KEY_BEAN, currentCompany);
//                    ActivityManage.push(CompanySettingAct.class, intent, 1003);
//                }
//            }
//        });

        myAdapter = new MyAdapter();
        myAdapter.setOnItemClickListener((adapter, view, position) -> {
            List<LocalMedia> images = new ArrayList<>();
            for (int i = 0; i < adapter.getData().size(); i++) {
                LocalMedia localMedia = new LocalMedia(UrlKit.getImgUrl(adapter.getData().get(i).toString()), 0, 0, "");
                images.add(localMedia);
            }
            PictureSelector.create(this)
                    .themeStyle(R.style.picture_default_style)
                    .isNotPreviewDownload(true)
                    .imageEngine(GlideEngine.createGlideEngine())
                    .openExternalPreview(position, images);
        });
        myAdapter.setOnItemLongClickListener((adapter, view, position) -> {
            MessageDialog.build((AppCompatActivity) getActivity())
                    .setTitle("温馨提示")
                    .setMessage("是否删除图片？")
                    .setCancelButton("取消", (baseDialog, v) -> false)
                    .setOkButton("确定", (baseDialog, v) -> {
                        myAdapter.remove(position);
                        updateCompany(false);
                        return false;
                    })
                    .show();
            return false;
        });
        companyImgs.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        companyImgs.setAdapter(myAdapter);
        refreshLayout.setColorSchemeColors(ContextCompat.getColor(getActivity(), R.color.colorPrimary));
        refreshLayout.setOnRefreshListener(this::getUserInfo);
        if (UserLogic.isLogin()) {
            UserInfo userInfo = SharedInfo.getInstance().getEntity(UserInfo.class);
            if (userInfo != null) {
                for (int i = 0; i < userInfo.getCompanys().size(); i++) {
                    CompanyBean companyBean = userInfo.getCompanys().get(i);
                    if (Constant.ID_STATUS_PASSED.equals(companyBean.getStatus())) {
                        setCompany(companyBean);
                        return;
                    }
                }
            }
            currentCompany = null;
            companyType0.setVisibility(View.GONE);
            companyType1.setVisibility(View.GONE);
            companyType2.setVisibility(View.GONE);
            noneLl.setVisibility(View.VISIBLE);
        }
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
                SharedInfo.getInstance().saveEntity(userInfo);
                ((MainActivity) getActivity()).setTab(3);
                for (int i = 0; i < userInfo.getCompanys().size(); i++) {
                    CompanyBean companyBean = userInfo.getCompanys().get(i);
                    if (Constant.ID_STATUS_PASSED.equals(companyBean.getStatus())) {
                        setCompany(companyBean);
                        return;
                    }
                }
                currentCompany = null;
                companyType0.setVisibility(View.GONE);
                companyType1.setVisibility(View.GONE);
                companyType2.setVisibility(View.GONE);
                noneLl.setVisibility(View.VISIBLE);
            }
        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onUpdateCompany(UIEvent uiEvent) {
        if (UIEvent.UPDATE_HOME == uiEvent.getType()) {
            getUserInfo();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (UserLogic.isLogin() && getUserVisibleHint()) {
            getUserInfo();
        }
    }

    private void getSummary() {
        if (!UserLogic.isLogin()) {
            return;
        }
        NetApi.get(UrlKit.CAR_RESOURCE_SUMMARY, null, new JsonCallback() {

            @Override
            public void onSuccess(String response, int id) {
                refreshLayout.setRefreshing(false);
                List<SumBean> sumBeans = new Gson().fromJson(response, new TypeToken<List<SumBean>>() {
                }.getType());
                if (sumBeans != null && sumBeans.size() > 0) {
                    ArrayList<String> yList = new ArrayList<>();
                    ArrayList<String> xRawDatas = new ArrayList<>();
                    yList.add("0");
                    xRawDatas.add("");
                    for (int i = 0; i < sumBeans.size(); i++) {
                        yList.add(sumBeans.get(i).getCount());
                        xRawDatas.add(sumBeans.get(i).getTitle());
                    }
                    int max = Integer.valueOf(Collections.max(yList));
                    int maxValue = 0;
                    if (max < 25) {
                        maxValue = 25;
                    } else if (max < 50) {
                        maxValue = 50;
                    } else if (max < 100) {
                        maxValue = 100;
                    } else if (max < 200) {
                        maxValue = 200;
                    } else if (max < 300) {
                        maxValue = 300;
                    }
                    lineView.setData(yList, xRawDatas, maxValue, maxValue / 5);
                }
            }
        });
    }

    private void getData() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("pageIndex", 0);
        map.put("pageSize", Constant.DEFULT_RESULT);
        map.put("passed", false);
        if (currentCompany != null) {
            map.put("companyId", currentCompany.getId());
        }
        NetApi.get(UrlKit.USER_MEMBER, map, new JsonCallback() {

            @Override
            public void onSuccess(String response, int id) {
                Gson gson = new Gson();
                PageBean<MemberBean> pageBean = gson.fromJson(response, new TypeToken<PageBean<MemberBean>>() {
                }.getType());
                if (pageBean != null) {
                    if (pageBean.getTotal() > 0) {
                        memberCount.setText(String.valueOf(pageBean.getTotal()));
                        memberCount.setVisibility(View.VISIBLE);
                    } else {
                        memberCount.setVisibility(View.GONE);
                    }
                }
            }
        });
    }

    private void setCompany(CompanyBean companyBean) {
        this.currentCompany = companyBean;
        getData();
        noneLl.setVisibility(View.GONE);
        companyT1.setVisibility(View.VISIBLE);
        companyType0.setVisibility(View.VISIBLE);
        company_name.setText(companyBean.getName());
        if(companyBean.getAddress()!=null){
            company_address.setText(companyBean.getAddress().getAddress());
        }
        List<String> strings;
        if (companyBean.getFrontDoorPhotos() == null) {
            strings = new ArrayList<>();
        } else {
            strings = companyBean.getFrontDoorPhotos();
        }
        if (Constant.STORE_TYPE_2.equals(companyBean.getType())) {
            company_type.setText("4S店");
            if (!TextUtils.isEmpty(companyBean.getMainBrand()))
                company_zy.setText(companyBean.getMainBrand());
            companyType1.setVisibility(View.VISIBLE);
            companyType2.setVisibility(View.GONE);
            refreshLayout.setEnabled(true);
            companyT1.setVisibility(View.GONE);
            getSummary();
            if (!TextUtils.isEmpty(currentCompany.getFrontDoorPhoto())) {
                strings.add(0, currentCompany.getFrontDoorPhoto());
            }
            myAdapter.replaceData(strings);
        } else if (Constant.STORE_TYPE_1.equals(companyBean.getType())) {
            company_zy.setVisibility(View.INVISIBLE);
            if (companyBean.isSecondHandPriority()) {
                company_type.setText("汽贸店-主营二手车");
            } else {
                company_type.setText("汽贸店-主营新车");
            }
            companyType1.setVisibility(View.VISIBLE);
            companyType2.setVisibility(View.GONE);
            refreshLayout.setEnabled(true);
            getSummary();
            if (!TextUtils.isEmpty(currentCompany.getFrontDoorPhoto())) {
                strings.add(0, currentCompany.getFrontDoorPhoto());
            }
            myAdapter.replaceData(strings);
        } else if (Constant.STORE_TYPE_3.equals(companyBean.getType())) {
            refreshLayout.setEnabled(false);
            company_type.setText("物流公司");
            company_zy.setVisibility(View.INVISIBLE);
            companyType2.setVisibility(View.VISIBLE);
            companyType1.setVisibility(View.GONE);
        }
    }

    private void initTrans() {
        fragmentList.add(OrderChildListFrag.newInstance(""));
        fragmentList.add(OrderChildListFrag.newInstance("processed"));
        fragmentList.add(OrderChildListFrag.newInstance("origin,transportation"));
        fragmentList.add(OrderChildListFrag.newInstance("destination"));
        viewpage.setAdapter(new XFragmentAdapter(getChildFragmentManager(), fragmentList, titles));
        viewpage.setOffscreenPageLimit(fragmentList.size());
        tabLayoutSortFilter.setupWithViewPager(viewpage);
        viewpage.setCurrentItem(0);
    }

    @Override
    public int getLayoutId() {
        return R.layout.frag_company;
    }

    @OnClick({R.id.auth, R.id.tv_manager, R.id.company_car_manager, R.id.company_member_manager, R.id.company_img_add})
    public void onViewClicker(View view) {
        switch (view.getId()) {
            case R.id.tv_manager://司机人员管理
                ActivityManage.push(DriverManagerAct.class);
                break;
            case R.id.company_member_manager://汽贸人员管理
                Intent intent = new Intent();
                intent.putExtra(Constant.KEY_ID, currentCompany.getId());
                ActivityManage.push(MembersManagerAct.class, intent);
                break;
            case R.id.company_car_manager://车源管理
                ActivityManage.push(CompanyManagerAct.class);
                break;
            case R.id.auth:
                ActivityManage.push(InfoAuthAct.class);
                break;
            case R.id.company_img_add:
                Utils.getPhoto(context, PictureConfig.MULTIPLE, 8 - myAdapter.getData().size());
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && requestCode == 1003) {
//            CompanyBean bean = data.getParcelableExtra(Constant.KEY_BEAN);
//            if (bean != null) {
//                setCompany(bean);
//            }
            getUserInfo();
        } else if (resultCode == Activity.RESULT_OK && (requestCode == PictureConfig.CHOOSE_REQUEST)) {
            List<LocalMedia> selectList = PictureSelector.obtainMultipleResult(data);
            if (selectList != null && selectList.size() > 0) {
                List<String> strings = new ArrayList<>();
                for (int i = 0; i < selectList.size(); i++) {
                    strings.add(selectList.get(i).getCompressPath());
                }
                upload(strings);
            }
        }
    }

    public void upload(List<String> urls) {
        NetworkUtil.showCutscenes("正在上传...", "剩余" + urls.size() + "张");
        OssServiceUtil.getInstance().asyncPutImage(OtherLogic.getImgUrl(), urls.get(0), (objectResult, currentPath) -> {
            myAdapter.addData(currentPath);
            Logger.e("currentPath=", currentPath);
            urls.remove(0);
            if (urls.size() > 0) {
                upload(urls);
            } else {
                updateCompany(true);
                NetworkUtil.dismissCutscenes();
            }
        });
    }

    private void updateCompany(boolean flag) {
        if (currentCompany != null) {
            NetApi.put("api/app/user/company/" + currentCompany.getId() + "/frontDoorPhoto", new Gson().toJson(myAdapter.getData()), new JsonCallback() {

                @Override
                public void onSuccess(String response, int id) {
                    if (flag) {
                        Util.toast("上传成功");
                    } else {
                        Util.toast("删除成功");
                    }
                    NetApi.get(UrlKit.USER_INFORM, null, new JsonCallback() {

                        @Override
                        public void onSuccess(String response, int id) {
                            UserInfo userInfo = new Gson().fromJson(response, UserInfo.class);
                            if (userInfo != null) {
                                SharedInfo.getInstance().saveEntity(userInfo);
                            }
                        }
                    });
                }
            });
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }


    private class MyAdapter extends BaseQuickAdapter<String, BaseViewHolder> {

        public MyAdapter() {
            super(R.layout.item_company_img);
        }

        @Override
        protected void convert(@NonNull BaseViewHolder baseViewHolder, String s) {
            Glide.with(ContextHolder.getContext()).load(UrlKit.getImgUrl(s)).into((ImageView) baseViewHolder.getView(R.id.item_img));
        }
    }
}
