package com.liansheng.carworld.activity.home;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.amap.api.maps.MapView;
import com.amap.api.services.core.PoiItem;
import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.liansheng.carworld.R;
import com.liansheng.carworld.activity.BaseActivity;
import com.liansheng.carworld.activity.OrderActivity;
import com.liansheng.carworld.activity.OrderListActivity;
import com.liansheng.carworld.activity.logic.OtherLogic;
import com.liansheng.carworld.activity.logic.UserLogic;
import com.liansheng.carworld.adapter.home.OrderTaskAdapter;
import com.liansheng.carworld.bean.ConfigBean;
import com.liansheng.carworld.bean.TransporterBean;
import com.liansheng.carworld.bean.UserInfo;
import com.liansheng.carworld.bean.me.AddressBean;
import com.liansheng.carworld.bean.me.CompanyBean;
import com.liansheng.carworld.bean.me.LicensesBean;
import com.liansheng.carworld.bean.other.PageBean;
import com.liansheng.carworld.kit.Constant;
import com.liansheng.carworld.net.JsonCallback;
import com.liansheng.carworld.net.NetApi;
import com.liansheng.carworld.net.UrlKit;
import com.liansheng.carworld.utils.PinYinStringHelper;
import com.liansheng.carworld.utils.ViewUtils;
import com.liansheng.carworld.utils.map.GDMapUtils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.droidlover.xdroid.hold.info.SharedInfo;
import cn.droidlover.xdroid.tools.utils.ActivityManage;
import cn.droidlover.xdroid.tools.utils.Util;
import cn.droidlover.xrecyclerview.XRecyclerContentLayout;
import cn.droidlover.xrecyclerview.XRecyclerView;
import okhttp3.Call;


public class TransporterActivity extends BaseActivity {

    @BindView(R.id.btn_back)
    ImageView btnBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.et_search)
    EditText etSearch;
    @BindView(R.id.llyt_start)
    LinearLayout llytStart;
    @BindView(R.id.wl_dfc)
    TextView wl_dfc;
    @BindView(R.id.wl_fcz)
    TextView wl_fcz;
    @BindView(R.id.wl_ywc)
    TextView wl_ywc;
    @BindView(R.id.llyt_end)
    LinearLayout llytEnd;
    @BindView(R.id.contentLayout)
    XRecyclerContentLayout contentLayout;
    @BindView(R.id.tv_start)
    TextView tvStart;
    @BindView(R.id.tv_end)
    TextView tvEnd;
    @BindView(R.id.tv_start_city)
    TextView tvStartCity;
    @BindView(R.id.tv_end_city)
    TextView tvEndCity;
    @BindView(R.id.main)
    LinearLayout main;
    @BindView(R.id.btn_function)
    TextView btnFunction;
    @BindView(R.id.tv_tips)
    TextView tv_tips;

    private OrderTaskAdapter homeAdapter;
    public AddressBean poiItemStart = null;
    public AddressBean poiItemEnd = null;
    public final static int PER_LOACTION = 0X003;
    public final static String[] PERMS_LOACTION_STATE = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};
    public String mCurrentLat = "";
    public String mCurrentLng = "";
    private String keywords = "";
    private int carType;
    private UserInfo userInfo;
    //    private OrderPop orderPop;
    private MapView mMapView;
    private OptionsPickerView gsOptions;
    private List<String> cars = Arrays.asList("大板车", "蓝牌斜板", "蓝牌落地", "黄牌落地", "5吨板", "厢车");

    @Override
    public void initData(Bundle savedInstanceState) {
        tvTitle.setText("顺路板物流");
//        orderPop = new OrderPop(this);
//        mMapView = orderPop.getPop_order_map();
//        mMapView.onCreate(savedInstanceState);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        carType = getIntent().getIntExtra(Constant.KEY_TYPE, 0);
        initDriverAdapter();
        UserInfo userInfo = SharedInfo.getInstance().getEntity(UserInfo.class);
        if (userInfo != null && userInfo.getTypes().size() > 0) {
            for (int i = 0; i < userInfo.getTypes().size(); i++) {
                if (Constant.USER_TYPE_2.equals(userInfo.getTypes().get(i))) {
                    findViewById(R.id.tv_tips).setVisibility(View.VISIBLE);
                    getConfig();
                    break;
                }
            }
        }
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!TextUtils.isEmpty(s.toString()) && PinYinStringHelper.isContainChinese(s.toString())) {
                    keywords = s.toString();
                    getData(0);
                }
            }
        });
        new GDMapUtils().location(aMapLocation -> {
            mCurrentLat = aMapLocation.getLatitude() + "";
            mCurrentLng = aMapLocation.getLongitude() + "";
        });

        initAddCar();
    }

    private void initAddCar() {
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
                    getUserInfo();
                }
            });
        }).setDividerColor(ContextCompat.getColor(context, R.color.line_grey))
                .setTextColorCenter(ContextCompat.getColor(context, R.color.main_normal_color))
                .setContentTextSize(16)
                .build();
        gsOptions.setPicker(cars);
    }

    void getConfig() {
        NetApi.get(UrlKit.APP_RESOURCE_CONFIG, null, new JsonCallback() {

            @Override
            public void onSuccess(String response, int id) {
                ConfigBean configBean = new Gson().fromJson(response, ConfigBean.class);
                tv_tips.setText(configBean.getTransport_notice());
            }
        });
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_transporter;
    }


    @Override
    protected void onResume() {
        super.onResume();
//        mMapView.onResume();
        getData(0);
        getUserInfo();
    }

    private void getUserInfo() {
        NetApi.get(UrlKit.USER_INFORM, null, new JsonCallback() {

            @Override
            public void onSuccess(String response, int id) {
                userInfo = new Gson().fromJson(response, UserInfo.class);
                setLoginResult(userInfo, false);
                SharedInfo.getInstance().saveEntity(userInfo);
                wl_dfc.setText(userInfo.getPendingOrderCounts() + "\n待发车");
                wl_fcz.setText(userInfo.getProcessingOrderCounts() + "\n发车中");
                wl_ywc.setText(userInfo.getCompletedOrderCounts() + "\n已送达");
//                btnFunction.setText("调度申请");
//                btnFunction.setVisibility(View.VISIBLE);
//                btnFunction.setOnClickListener(v -> ActivityManage.push(ApplyDispatchAct.class));
                if (userInfo.getTypes().size() > 0) {
                    for (int i = 0; i < userInfo.getTypes().size(); i++) {
                        if (Constant.USER_TYPE_4.equals(userInfo.getTypes().get(i))) {
                            btnFunction.setText("公司管理");
                            btnFunction.setVisibility(View.VISIBLE);
                            btnFunction.setOnClickListener(v -> {
                                for (int j = 0; j < userInfo.getCompanys().size(); j++) {
                                    CompanyBean bean = userInfo.getCompanys().get(j);
                                    if (Constant.STORE_TYPE_3.equals(bean.getType())) {
                                        Intent intent = new Intent();
                                        intent.putExtra(Constant.KEY_BEAN, userInfo.getId());
                                        ActivityManage.push(CompanyInfoAct.class, intent);
                                        break;
                                    }
                                }
                            });
                            break;
                        }
                    }
                }
            }
        });
    }

    private void getData(int page) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("pageIndex", page);
        hashMap.put("pageSize", Constant.DEFULT_RESULT);
        hashMap.put("keywords", keywords);
        hashMap.put("privately", "false");
        hashMap.put("status", "creation");
//        hashMap.put("paid", "true");
        if (-1 == carType) {
            hashMap.put("bigCar", true);
        } else {
            hashMap.put("bigCar", false);
        }
        hashMap.put("truckRequire", "");
        NetApi.get(UrlKit.TRANSPORT_ORDER, hashMap, new JsonCallback() {

            @Override
            public void onSuccess(String response, int id) {
                PageBean<TransporterBean> home = new Gson().fromJson(response, new TypeToken<PageBean<TransporterBean>>() {
                }.getType());
                OtherLogic.updateAdapter(page, homeAdapter, home, contentLayout);
            }
        });
    }

    private void initDriverAdapter() {
//        UserInfo userInfo = SharedInfo.getInstance().getEntity(UserInfo.class);
//        boolean isAccept = false;
//        for (int i = 0; i < userInfo.getCompanys().size(); i++) {
//            CompanyBean bean = userInfo.getCompanys().get(i);
//            if (Constant.STORE_TYPE_3.equals(bean.getType()) && Constant.ID_STATUS_PASSED.equals(bean.getStatus())) {
//                isAccept = true;
//            }
//        }
        homeAdapter = new OrderTaskAdapter(null);
        homeAdapter.addChildClickViewIds(R.id.iv_jd);
        homeAdapter.setOnItemChildClickListener((adapter, view, position) -> {
            if (checkRule()) {
                return;
            }
            TransporterBean item = (TransporterBean) adapter.getItem(position);
            if (TextUtils.isEmpty(mCurrentLat) || TextUtils.isEmpty(mCurrentLng)) {
                new GDMapUtils().location(aMapLocation -> {
                    mCurrentLat = aMapLocation.getLatitude() + "";
                    mCurrentLng = aMapLocation.getLongitude() + "";
                    Intent intent = new Intent();
                    intent.putExtra(Constant.KEY_BEAN, item);
                    intent.putExtra("lng", mCurrentLng);
                    intent.putExtra("lat", mCurrentLat);
                    ActivityManage.push(OrderPopAct.class, intent);
                });
            } else {
                Intent intent = new Intent();
                intent.putExtra(Constant.KEY_BEAN, item);
                intent.putExtra("lng", mCurrentLng);
                intent.putExtra("lat", mCurrentLat);
                ActivityManage.push(OrderPopAct.class, intent);
            }
//            orderPop.upData(item, mCurrentLng, mCurrentLat);
//            orderPop.showAtLocation(main, Gravity.CENTER, 0, 0);
//            if (TextUtils.isEmpty(mCurrentLat) || TextUtils.isEmpty(mCurrentLng)) {
//                dialog(item, "确定要接单吗？");
//            } else {
//                getDistance(item.getOrigin().getLongitude(), item.getOrigin().getLatitude(), item);
//            }
        });
        contentLayout.getRecyclerView().verticalLayoutManager(context);
        contentLayout.getRecyclerView()
                .setAdapter(homeAdapter);
        View emptyView = ViewUtils.getEmptyView(this, R.mipmap.bg_home_order);
        contentLayout.emptyView(emptyView);
        emptyView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getData(0);
            }
        });
        contentLayout.getSwipeRefreshLayout().setColorSchemeResources(R.color.order, R.color.order2, R.color.end);
        contentLayout.getRecyclerView()
                .setOnRefreshAndLoadMoreListener(new XRecyclerView.OnRefreshAndLoadMoreListener() {
                    @Override
                    public void onRefresh() {
                        getData(0);
                    }

                    @Override
                    public void onLoadMore(int page) {
                        getData(page);
                    }
                });

        contentLayout.loadingView(View.inflate(this, R.layout.view_loading, null));
        contentLayout.getRecyclerView().useDefLoadMoreView();
    }


    private boolean checkRule() {
        if (userInfo.getLicenses().size() > 0) {
            for (int i = 0; i < userInfo.getLicenses().size(); i++) {
                LicensesBean bean = userInfo.getLicenses().get(i);
                if (Constant.ID_STATUS_PASSED.equals(bean.getStatus()) && Constant.AUTH_COMPULSORY_INSURANCE.equals(bean.getType()) && userInfo.getUserCar() == null) {
                    Util.toast("您未选择板车类型，请选择板车类型");
                    gsOptions.show();
                    return true;
                }
            }
        }
        if (!UserLogic.isRealAuth()) {
            return true;
        }
        return false;
    }

    @OnClick({R.id.llyt_start, R.id.llyt_end, R.id.btn_go, R.id.wl_dfc, R.id.wl_fcz, R.id.wl_ywc})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.llyt_start:
//                toActivity(ChooseLocationActivity2.class,null);
                Intent startIntent = new Intent();
                startIntent.putExtra(Constant.KEY_TYPE, "start");
                startIntent.putExtra(Constant.KEY_TITLE, "出发地");
                ActivityManage.push(ChooseAreaActivity.class, startIntent, 1000);
                break;
            case R.id.llyt_end:
                Intent intent2 = new Intent();
                intent2.putExtra(Constant.KEY_TYPE, "end");
                intent2.putExtra(Constant.KEY_TITLE, "目的地");
//                startActivityForResult(intent2, 2000);
                ActivityManage.push(ChooseAreaActivity.class, intent2, 2000);
                break;
            case R.id.btn_go:
                if (checkRule()) {
                    return;
                }
                Intent intent = new Intent();
                intent.putExtra(Constant.KEY_START, poiItemStart);
                intent.putExtra(Constant.KEY_END, poiItemEnd);
                if (-1 == carType) {
                    intent.putExtra(Constant.KEY_TYPE, getIntent().getIntExtra(Constant.KEY_TYPE, 0));
                    ActivityManage.push(OrderActivity.class, intent);
                } else {
                    ActivityManage.push(CarSmallSelectAct.class, intent);
                }
                break;
            case R.id.wl_dfc:
                Bundle bundle = new Bundle();
                bundle.putInt("jump", 1);
                toActivity(OrderListActivity.class, bundle);
                break;
            case R.id.wl_fcz:
                Bundle bundle1 = new Bundle();
                bundle1.putInt("jump", 2);
                toActivity(OrderListActivity.class, bundle1);
                break;
            case R.id.wl_ywc:
                Bundle bundle2 = new Bundle();
                bundle2.putInt("jump", 3);
                toActivity(OrderListActivity.class, bundle2);
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            if (requestCode == 1000) {
                if (data.getParcelableExtra("start") != null) {
                    PoiItem result = (PoiItem) data.getParcelableExtra("start");
                    poiItemStart = new AddressBean(result.getTitle(), String.valueOf(result.getLatLonPoint().getLongitude()), String.valueOf(result.getLatLonPoint().getLatitude()), result.getProvinceName(), result.getCityName(), result.getAdName(), result.getSnippet());
                    tvStart.setText(result.getTitle());
                    tvStartCity.setText(result.getCityName() + result.getAdName());
                }
            } else if (requestCode == 2000) {
                if (data.getParcelableExtra("end") != null) {
                    PoiItem result = (PoiItem) data.getParcelableExtra("end");
                    poiItemEnd = new AddressBean(result.getTitle(), String.valueOf(result.getLatLonPoint().getLongitude()), String.valueOf(result.getLatLonPoint().getLatitude()), result.getProvinceName(), result.getCityName(), result.getAdName(), result.getSnippet());
                    tvEnd.setText(result.getTitle());
                    tvEndCity.setText(result.getCityName() + result.getAdName());
                }
            }
        }
    }

}
