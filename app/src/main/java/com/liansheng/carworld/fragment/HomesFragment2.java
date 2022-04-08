package com.liansheng.carworld.fragment;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.kongzue.dialog.interfaces.OnDialogButtonClickListener;
import com.kongzue.dialog.util.BaseDialog;
import com.kongzue.dialog.v3.MessageDialog;
import com.liansheng.carworld.R;
import com.liansheng.carworld.activity.MainActivity;
import com.liansheng.carworld.activity.home.CarLoanAct;
import com.liansheng.carworld.activity.home.CarSelectAct;
import com.liansheng.carworld.activity.home.ChooseCarActivity;
import com.liansheng.carworld.activity.home.SaleCarDetailsAct;
import com.liansheng.carworld.activity.home.SaleCarListAct;
import com.liansheng.carworld.activity.logic.UserLogic;
import com.liansheng.carworld.activity.me.GradeActivity;
import com.liansheng.carworld.activity.user.LoginYzmActivity;
import com.liansheng.carworld.adapter.CarDetailsAdapter;
import com.liansheng.carworld.adapter.home.HomeAdapter;
import com.liansheng.carworld.bean.Directory;
import com.liansheng.carworld.bean.UserInfo;
import com.liansheng.carworld.bean.event.UIEvent;
import com.liansheng.carworld.bean.home.HomeBannerBean;
import com.liansheng.carworld.bean.home.SaleCardBean;
import com.liansheng.carworld.bean.me.CouponBean;
import com.liansheng.carworld.bean.other.PageBean;
import com.liansheng.carworld.kit.Constant;
import com.liansheng.carworld.net.JsonCallback;
import com.liansheng.carworld.net.NetApi;
import com.liansheng.carworld.net.UrlKit;
import com.liansheng.carworld.utils.DateParserHelper;
import com.liansheng.carworld.utils.Utils;
import com.liansheng.carworld.utils.map.GDMapUtils;
import com.liansheng.carworld.view.pop.CouponPop;
import com.xuexiang.xui.widget.banner.widget.banner.BannerItem;
import com.xuexiang.xui.widget.banner.widget.banner.SimpleImageBanner;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import cn.droidlover.xdroid.hold.info.SharedInfo;
import cn.droidlover.xdroid.tools.utils.ActivityManage;
import cn.droidlover.xdroid.tools.utils.Util;
import pub.devrel.easypermissions.EasyPermissions;


public class HomesFragment2 extends BaseFragment {
    Unbinder unbinder;
    @BindView(R.id.list_command)
    RecyclerView listCommand;
    //    @BindView(R.id.contentLayout)
//    XRecyclerContentLayout contentLayout;
    @BindView(R.id.home_list)
    RecyclerView homeList;
    @BindView(R.id.main)
    LinearLayout main;
    @BindView(R.id.ivBanner)
    SimpleImageBanner ivBanner;
    @BindView(R.id.iv_car)
    ImageView ivCar;
    @BindView(R.id.iv_new)
    ImageView ivNew;
    @BindView(R.id.swipe_target)
    SwipeRefreshLayout refreshLayout;

    public static HomesFragment2 newInstance() {
        Bundle args = new Bundle();
        HomesFragment2 fragment = new HomesFragment2();
        fragment.setArguments(args);
        return fragment;
    }

    private HomeAdapter homeAdapter;
    List<Directory.ItemsBean> itemsBeanList = new ArrayList<>();
    private List<Map<String, Object>> dataList;
    public final static int PER_LOACTION = 0X003;
    public final static String[] PERMS_LOACTION_STATE = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};
    public String mCurrentLat = "";
    public String mCurrentLng = "";
    private OptionsPickerView gsOptions;
    private List<String> cars = Arrays.asList("大板车", "蓝牌斜板", "蓝牌落地", "黄牌落地", "5吨板", "厢车");
    private CarDetailsAdapter detailsAdapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        unbinder = ButterKnife.bind(this, rootView);
        initview();
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_home;
    }

    public void initview() {
        EventBus.getDefault().register(this);
//        topbar.setTitle(UrlKit.IS_DEBUG ? "车无界测试版" : "车无界");
        Glide.with(this).load(R.drawable.icon_new).into(ivNew);
        Glide.with(this).load(R.drawable.icon_second_car).into(ivCar);
        initAdapter();
        getBanner();
    }

    private void getBanner() {
        NetApi.get(UrlKit.APP_RESOURCES_INDEX_BANNER, null, new JsonCallback() {
            @Override
            public void onSuccess(String response, int id) {
                Gson gson = new Gson();
                List<HomeBannerBean> bannerBean = gson.fromJson(response, new TypeToken<List<HomeBannerBean>>() {
                }.getType());
                if (bannerBean != null) {
                    ivBanner.setSource(getBannerList(bannerBean)).setOnItemClickListener((view, t, position) -> {
                        if (!UserLogic.isLogin()) {
                            ActivityManage.push(LoginYzmActivity.class);
                            return;
                        }
                        if ("4s".equals(t.getTitle())) {
                            UserInfo userInfo = SharedInfo.getInstance().getEntity(UserInfo.class);
                            if (!UserLogic.isRealAuth()) {
                                return;
                            }
                            if (userInfo.getVip() != null && userInfo.getVip().isExpired()) {
                                MessageDialog.build(getBaseActivity())
                                        .setMessage("您的会员已到期，请兑换")
                                        .setOkButton("去兑换", new OnDialogButtonClickListener() {
                                            @Override
                                            public boolean onClick(BaseDialog baseDialog, View v) {
                                                UserLogic.checkLogin(GradeActivity.class);
                                                return false;
                                            }
                                        })
                                        .setOkButtonDrawable(R.drawable.shape_button_red_bg)
                                        .show();
                                return;
                            }
                            if (userInfo.getVip() != null) {
                                if (TextUtils.isEmpty(userInfo.getVip().getExpiration())) {
                                    Intent intent = new Intent();
                                    intent.putExtra(Constant.KEY_TYPE, 1);
                                    ActivityManage.push(ChooseCarActivity.class, intent);
                                } else {
                                    int days = 0;
                                    try {
                                        days = DateParserHelper.daysBetween(userInfo.getVip().getExpiration());
                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }
                                    if (days > 0) {
                                        Intent intent = new Intent();
                                        intent.putExtra(Constant.KEY_TYPE, 1);
                                        ActivityManage.push(ChooseCarActivity.class, intent);
                                    } else {
                                        getBaseActivity().showSnackToast(main, "您的VIP已到期，请续费");
                                    }
                                }
                            } else {
                                Intent intent = new Intent();
                                intent.putExtra(Constant.KEY_TYPE, 1);
                                ActivityManage.push(ChooseCarActivity.class, intent);
                            }
                        } else if ("wl".equals(t.getTitle())) {
                            ActivityManage.push(CarSelectAct.class);
                        } else if ("esc".equals(t.getTitle())) {
                            ActivityManage.push(SaleCarListAct.class);
                        } else if ("400".equals(t.getTitle())) {
                            ((MainActivity) getActivity()).callPhone((AppCompatActivity) ActivityManage.peek(), "联系客服电话：" + Constant.SERVICE_PHONE, Constant.SERVICE_PHONE);
                        }
                    }).setIsOnePageLoop(false).startScroll();
                }
            }
        });
        gsOptions = new OptionsPickerBuilder(context, (i, options2, options3, v) -> {
            boolean isBigCar = false;
            String smallCarType = "";
            if (i == 0) {
                isBigCar = true;
            } else if (i == 1) {
                smallCarType = "BlueOblique";
            } else if (i == 2) {
                smallCarType = "BlueGround";
            } else if (i == 3) {
                smallCarType = "YellowGround";
            } else if (i == 4) {
                smallCarType = "Five";
            } else if (i == 5) {
                smallCarType = "Box";
            }
            HashMap<String, Object> map = new HashMap<>();
            map.put("bigCar", isBigCar);
            map.put("truckRequire", smallCarType);
            NetApi.post(UrlKit.USER_CAR, map, new JsonCallback() {

                @Override
                public void onSuccess(String response, int id) {
                    Util.toast("提交成功");
                    getUserInfo(SharedInfo.getInstance().getValue(Constant.KEY_TOKEN, "").toString());
                }
            });
        }).setDividerColor(ContextCompat.getColor(context, R.color.line_grey))
                .setTextColorCenter(ContextCompat.getColor(context, R.color.main_normal_color))
                .setContentTextSize(16)
                .build();
        gsOptions.setPicker(cars);
        HashMap<String, Object> map = new HashMap<>();
        map.put("read", false);
        map.put("expired", false);
        map.put("used", false);
        NetApi.get(UrlKit.USER_COUPON, map, new JsonCallback() {
            @Override
            public void onSuccess(String data, int id) {
                PageBean<CouponBean> bean = new Gson().fromJson(data, new TypeToken<PageBean<CouponBean>>() {
                }.getType());
                if (bean != null && bean.getItems() != null && bean.getItems().size() > 0) {
                    int total = 0;
                    for (int i = 0; i < bean.getItems().size(); i++) {
                        CouponBean couponBean = bean.getItems().get(i);
                        total = total + couponBean.getDenomination();
                        NetApi.put(UrlKit.USER_COUPON_READ + couponBean.getId(), "", null);
                    }
                    new CouponPop(context, String.valueOf(total)).showAtLocation(main, Gravity.CENTER, 0, 0);

                }
            }
        });
    }

    private void getUserInfo(String token) {
        NetApi.get(UrlKit.USER_INFORM, null, new JsonCallback() {

            @Override
            public void onSuccess(String response, int id) {
                UserInfo userInfo = new Gson().fromJson(response, UserInfo.class);
                userInfo.setToken(token);
                getBaseActivity().setLoginResult(userInfo, false);
                SharedInfo.getInstance().saveEntity(userInfo);
            }
        });
    }

    public void getPermission() {
        if (EasyPermissions.hasPermissions(getBaseActivity(), PERMS_LOACTION_STATE)) {
            new GDMapUtils().location(aMapLocation -> {
                mCurrentLat = aMapLocation.getLatitude() + "";
                mCurrentLng = aMapLocation.getLongitude() + "";
                SharedInfo.getInstance().saveValue(Constant.KEY_CITY, aMapLocation.getCity());
                getPosition();
            });
        } else {
            EasyPermissions.requestPermissions(getBaseActivity(), "我们需要您的位置信息，请点击确定打开位置权限.",
                    PER_LOACTION, PERMS_LOACTION_STATE);
        }
    }

    private void getHomeData() {
        if (!UserLogic.isLogin()) {
            refreshLayout.setRefreshing(false);
            return;
        }
        HashMap<String, Object> map = new HashMap<>();
        map.put("pageIndex", 0);
        map.put("pageSize", 10);
        map.put("sort", "quality");
        NetApi.get(UrlKit.DIRECTORY, map, new JsonCallback() {

            @Override
            public void onSuccess(String response, int id) {
                refreshLayout.setRefreshing(false);
                Gson gson = new Gson();
                Directory directory = gson.fromJson(response, Directory.class);
                if (directory != null && directory.getItems() != null && directory.getItems().size() > 0) {
                    itemsBeanList = directory.getItems();
                    homeAdapter.setNewData(itemsBeanList);
                }
//                else {
//                    contentLayout.showEmpty();
//                }
            }
        });
    }

    private void initAdapter() {
        refreshLayout.setColorSchemeColors(ContextCompat.getColor(getActivity(), R.color.colorPrimary));
        refreshLayout.setOnRefreshListener(() -> {
            getHomeData();
            getCarData();
        });
        homeAdapter = new HomeAdapter();
        homeAdapter.setOnItemClickListener((adapter, view, position) -> {
            Directory.ItemsBean item = (Directory.ItemsBean) adapter.getItem(position);
            Utils.callPhone(getBaseActivity(), "请问是否要联系", item);
        });
        LinearLayoutManager manager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        homeList.setLayoutManager(manager);
        homeList.setAdapter(homeAdapter);
        detailsAdapter = new CarDetailsAdapter(null);
        detailsAdapter.setOnItemClickListener((adapter, view, position) -> {
            if (!UserLogic.isLogin()) {
                ActivityManage.push(LoginYzmActivity.class);
                return;
            }
            SaleCardBean item = (SaleCardBean) adapter.getItem(position);
            Intent intent = new Intent();
            intent.putExtra(Constant.KEY_BEAN, item);
            ActivityManage.push(SaleCarDetailsAct.class, intent);

        });
        listCommand.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        RecyclerView.RecycledViewPool pool = listCommand.getRecycledViewPool();
        pool.setMaxRecycledViews(0, 10);
        listCommand.setRecycledViewPool(pool);
        listCommand.setAdapter(detailsAdapter);
    }

    @OnClick({R.id.tv_ml, R.id.tv_car, R.id.home_item_1, R.id.home_item_2, R.id.home_item_3, R.id.home_item_4, R.id.home_item_5, R.id.home_item_6, R.id.home_item_7})
    public void onClick(View view) {
        UserInfo userInfo = SharedInfo.getInstance().getEntity(UserInfo.class);
        if (userInfo == null) {
            ActivityManage.push(LoginYzmActivity.class);
            return;
        }
        if (view.getId() == R.id.tv_ml || view.getId() == R.id.home_item_1) {
            if (!UserLogic.isRealAuth()) {
                return;
            }
            if (userInfo.getVip() != null && userInfo.getVip().isExpired()) {
                MessageDialog.build(getBaseActivity())
                        .setMessage("您的会员已到期，请兑换")
                        .setOkButton("去兑换", new OnDialogButtonClickListener() {
                            @Override
                            public boolean onClick(BaseDialog baseDialog, View v) {
                                UserLogic.checkLogin(GradeActivity.class);
                                return false;
                            }
                        })
                        .setOkButtonDrawable(R.drawable.shape_button_red_bg)
                        .show();
                return;
            }
        }
        switch (view.getId()) {
            case R.id.tv_ml:
            case R.id.home_item_1:
                if (UserLogic.isLogin()) {
                    if (userInfo.getVip() != null) {
                        if (!TextUtils.isEmpty(userInfo.getVip().getExpiration())) {
                            int days = 0;
                            try {
                                days = DateParserHelper.daysBetween(userInfo.getVip().getExpiration());
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            if (days > 0) {
                                Intent intent = new Intent();
                                intent.putExtra(Constant.KEY_TYPE, 1);
                                ActivityManage.push(ChooseCarActivity.class, intent);
                            } else {
                                getBaseActivity().showSnackToast(main, "您的VIP已到期，请续费");
                            }
                        }
                    } else {
                        Intent intent = new Intent();
                        intent.putExtra(Constant.KEY_TYPE, 1);
                        ActivityManage.push(ChooseCarActivity.class, intent);
                    }
                }
                break;
            case R.id.home_item_2:
//                if (userInfo.getLicenses().size() > 0) {
//                    for (int i = 0; i < userInfo.getLicenses().size(); i++) {
//                        LicensesBean bean = userInfo.getLicenses().get(i);
//                        if (Constant.ID_STATUS_PASSED.equals(bean.getStatus()) && Constant.AUTH_COMPULSORY_INSURANCE.equals(bean.getType()) && userInfo.getUserCar() == null) {
//                            Util.toast("您未选择板车类型，请选择板车类型");
//                            gsOptions.show();
//                            return;
//                        }
//                    }
//                }
                ActivityManage.push(CarSelectAct.class);
//                ActivityManage.push(TransporterActivity.class);
                break;
            case R.id.home_item_3:
            case R.id.tv_car:
                ActivityManage.push(SaleCarListAct.class);
                break;
            case R.id.home_item_5:
                if (!UserLogic.isRealAuth()) {
                    return;
                }
                ActivityManage.push(CarLoanAct.class);
                break;
            case R.id.home_item_4:
            case R.id.home_item_6:
            case R.id.home_item_7:
//                ActivityManage.push(IMAct.class);
                Utils.showToast("敬请期待");
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (UserLogic.isLogin()) {
            getPermission();
        }
        if (getUserVisibleHint()) {
            getHomeData();
            getCarData();
        }
    }

    private void getCarData() {
//        if(!UserLogic.isLogin()){
//            refreshLayout.setRefreshing(false);
//            return;
//        }
        HashMap<String, Object> map = new HashMap<>();
        map.put("pageIndex", 0);
        map.put("pageSize", 6);
        map.put("common", true);
        map.put("off", false);
        NetApi.get(UrlKit.CAR_RESOURCE, map, new JsonCallback() {
            @Override
            public void onSuccess(String response, int id) {
                refreshLayout.setRefreshing(false);
                Gson gson = new Gson();
                PageBean<SaleCardBean> data = gson.fromJson(response, new TypeToken<PageBean<SaleCardBean>>() {
                }.getType());
                if (data != null && data.getItems().size() > 0) {
//                    List<SaleCardBean> beans = detailsAdapter.getData();
//                    if (beans.size() > 0 && beans.get(0).getId().equals(data.getItems().get(0).getId())) {
//
//                    } else {
                    detailsAdapter.replaceData(data.getItems());
//                    }
                }
            }
        });
    }

    @Subscribe
    public void updateHome(UIEvent uiEvent) {
        if (UIEvent.UPDATE_HOME == uiEvent.getType()) {
            getHomeData();
        }
    }

    public void getPosition() {
        if (!mCurrentLat.equals("")) {
            NetApi.putPostion(getBaseActivity().getLoginResult().getToken(), mCurrentLat, mCurrentLng, new JsonCallback() {

                @Override
                public void onSuccess(String response, int id) {
//                        getBaseActivity().showToast("update "+mCurrentLat+mCurrentLng);
                }
            });
        }
    }

    public List<BannerItem> getBannerList(List<HomeBannerBean> bannerBeans) {
        ArrayList<BannerItem> list = new ArrayList<>();
        for (int i = 0; i < bannerBeans.size(); i++) {
            BannerItem item = new BannerItem();
            item.imgUrl = UrlKit.getImgUrl(bannerBeans.get(i).getSrc());
            item.title = bannerBeans.get(i).getParam();
            list.add(item);
        }
        return list;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ivBanner.recycle();
        EventBus.getDefault().unregister(this);
    }
}