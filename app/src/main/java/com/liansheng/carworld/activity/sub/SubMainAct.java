package com.liansheng.carworld.activity.sub;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.kongzue.dialog.interfaces.OnDialogButtonClickListener;
import com.kongzue.dialog.util.BaseDialog;
import com.kongzue.dialog.v3.MessageDialog;
import com.liansheng.carworld.R;
import com.liansheng.carworld.activity.BaseActivity;
import com.liansheng.carworld.activity.logic.OtherLogic;
import com.liansheng.carworld.adapter.home.OrderTaskAdapter;
import com.liansheng.carworld.bean.Distance;
import com.liansheng.carworld.bean.TransporterBean;
import com.liansheng.carworld.bean.child.ChildInfoBean;
import com.liansheng.carworld.bean.other.PageBean;
import com.liansheng.carworld.kit.Constant;
import com.liansheng.carworld.net.JsonCallback;
import com.liansheng.carworld.net.NetApi;
import com.liansheng.carworld.net.UrlKit;
import com.liansheng.carworld.utils.PinYinStringHelper;
import com.liansheng.carworld.utils.Utils;
import com.liansheng.carworld.utils.ViewUtils;
import com.liansheng.carworld.utils.map.GDMapUtils;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.OnClick;
import cn.droidlover.xdroid.hold.info.SharedInfo;
import cn.droidlover.xdroid.tools.utils.ActivityManage;
import cn.droidlover.xdroid.tools.utils.Util;
import cn.droidlover.xdroid.views.appbar.ToolBar;
import cn.droidlover.xrecyclerview.XRecyclerContentLayout;
import cn.droidlover.xrecyclerview.XRecyclerView;
import okhttp3.Call;
import pub.devrel.easypermissions.EasyPermissions;

public class SubMainAct extends BaseActivity {

    @BindView(R.id.toolbar)
    ToolBar toolBar;
    @BindView(R.id.et_search)
    EditText etSearch;
    @BindView(R.id.wl_dfc)
    TextView wl_dfc;
    @BindView(R.id.wl_fcz)
    TextView wl_fcz;
    @BindView(R.id.wl_ywc)
    TextView wl_ywc;
    @BindView(R.id.contentLayout)
    XRecyclerContentLayout contentLayout;

    private OrderTaskAdapter homeAdapter;
    public String mCurrentLat = "";
    public String mCurrentLng = "";
    private String keywords = "";

    @Override
    public void initData(Bundle savedInstanceState) {
        initview();
        toolBar.setTitle(UrlKit.IS_DEBUG ? "顺路版物流测试版" : "顺路版物流");
        toolBar.setLeftListener(v -> {
            ActivityManage.push(SubMeAct.class);
        });
    }

    @Override
    public int getLayoutId() {
        return R.layout.act_sub_main;
    }

    public void initview() {
        initDriverAdapter();
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
    }

    @Override
    protected void onResume() {
        super.onResume();
        getUserInfo();
        getPermission();
    }

    public void getPermission() {
        if (EasyPermissions.hasPermissions(this, PERMS_LOACTION_STATE)) {
            new GDMapUtils().location(aMapLocation -> {
                mCurrentLat = aMapLocation.getLatitude() + "";
                mCurrentLng = aMapLocation.getLongitude() + "";
                SharedInfo.getInstance().saveValue(Constant.KEY_CITY, aMapLocation.getCity());
                HashMap<String, Object> map = new HashMap<>();
                map.put("latitude", mCurrentLat);
                map.put("longitude", mCurrentLng);
                NetApi.put(UrlKit.CHILD_ACCOUNT_POSITION, map, new JsonCallback() {

                    @Override
                    public void onSuccess(String response, int id) {

                    }
                });
            });
        } else {
            EasyPermissions.requestPermissions(this, "我们需要您的位置信息，请点击确定打开位置权限.",
                    PER_LOACTION, PERMS_LOACTION_STATE);
        }
    }

    private void getUserInfo() {
        NetApi.get(UrlKit.CHILD_ACCOUNT_INFO, null, new JsonCallback() {

            @Override
            public void onSuccess(String response, int id) {
                ChildInfoBean infoBean = new Gson().fromJson(response, ChildInfoBean.class);
                wl_dfc.setText(infoBean.getPendingOrderCounts() + "\n待发车");
                wl_fcz.setText(infoBean.getProcessingOrderCounts() + "\n发车中");
                wl_ywc.setText(infoBean.getCompletedOrderCounts() + "\n已送达");
                getData(0);
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
        ChildInfoBean infoBean = SharedInfo.getInstance().getEntity(ChildInfoBean.class);
        hashMap.put("bigCar", infoBean.getCar().isBigCar());
        hashMap.put("truckRequire", "");
        NetApi.get(UrlKit.TRANSPORT_ORDER, hashMap, new JsonCallback() {

            @Override
            public void onSuccess(String response, int id) {
//                checkResponseMsgForCode(response.getCode());
                PageBean<TransporterBean> home = new Gson().fromJson(response, new TypeToken<PageBean<TransporterBean>>(){}.getType());
                OtherLogic.updateAdapter(page,homeAdapter,home,contentLayout);
            }
        });
    }

    private void initDriverAdapter() {
        homeAdapter = new OrderTaskAdapter(null);
        homeAdapter.addChildClickViewIds(R.id.iv_jd);
        homeAdapter.setOnItemChildClickListener((adapter, view, position) -> {
            TransporterBean item = (TransporterBean) adapter.getItem(position);
            if (TextUtils.isEmpty(mCurrentLat) || TextUtils.isEmpty(mCurrentLng)) {
                dialog(item, "确定要接单吗？");
            } else {
                getDistance(item.getOrigin().getLongitude(), item.getOrigin().getLatitude(), item);
            }
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
                        homeAdapter.getData().clear();
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

    public void getDistance(String l, String la, TransporterBean item) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("origin.longitude", mCurrentLng);
        map.put("origin.latitude", mCurrentLat);
        map.put("destination.longitude", l);
        map.put("destination.latitude", la);
        map.put("checkCarPay", "");
        map.put("bigCar", "");
        map.put("truckRequire", "");
        NetApi.get(UrlKit.CHILD_ACCOUNT_TRANSPORT_ORDER_PRICE, map, new JsonCallback() {

            @Override
            public void onSuccess(String response, int id) {
                if (response.contains("code") && response.contains("message")) {
//                    mContext.checkResponseMsg(response);
                    dialog(item, "确定要接单吗？");
                } else {
                    Gson gson = new Gson();
                    Distance d = gson.fromJson(response, Distance.class);
                    String distance = d.getDistance() / 1000 + "";
                    dialog(item, "出发地距离您" + distance + "公里，确定要接单吗？");
                }
            }
        });
    }

    public void dialog(TransporterBean item, String title) {
        MessageDialog.build(this)
                .setTitle(title)
                .setMessage(item.getOrigin().getProvince() + item.getOrigin().getCity() + item.getOrigin().getCounty() + "-" + item.getDestination().getProvince() + item.getDestination().getCity() + item.getDestination().getCounty())
                .setCancelButton("取消", new OnDialogButtonClickListener() {
                    @Override
                    public boolean onClick(BaseDialog baseDialog, View v) {
                        return false;
                    }
                })
                .setOkButton("确定", new OnDialogButtonClickListener() {
                    @Override
                    public boolean onClick(BaseDialog baseDialog, View v) {
                        jd(item.getId());
                        return false;
                    }
                })
                .setOkButtonDrawable(R.drawable.shape_button_red_bg)
                .setOtherButton("客服", new OnDialogButtonClickListener() {
                    @Override
                    public boolean onClick(BaseDialog baseDialog, View v) {
                        callPhone(SubMainAct.this, "客服电话 4008678280", item.getCustomService().getPhoneNumber());
                        return false;
                    }
                })
                .show();
    }

    public void jd(String orderId) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("orderId", orderId);
        NetApi.put(UrlKit.CHILD_ACCOUNT_TRANSPORTORDER, map, new JsonCallback() {

            @Override
            public void onSuccess(String response, int id) {
                Util.toast("接单成功");
                Bundle bundle = new Bundle();
                bundle.putString("id", orderId);
                toActivity(SubOrderDetailsAct.class, bundle);
            }
        });
    }

    @OnClick({R.id.wl_dfc, R.id.wl_fcz, R.id.wl_ywc})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.wl_dfc:
                Bundle bundle = new Bundle();
                bundle.putInt("jump", 1);
                toActivity(SubOrderListAct.class, bundle);
                break;
            case R.id.wl_fcz:
                Bundle bundle1 = new Bundle();
                bundle1.putInt("jump", 2);
                toActivity(SubOrderListAct.class, bundle1);
                break;
            case R.id.wl_ywc:
                Bundle bundle2 = new Bundle();
                bundle2.putInt("jump", 3);
                toActivity(SubOrderListAct.class, bundle2);
                break;
        }
    }

    private long firstTime = 0;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            if (System.currentTimeMillis() - firstTime > 2000) {
                Utils.showToast("再按一次退出程序");
                firstTime = System.currentTimeMillis();
            } else {
                finish();
                ActivityManage.finishAll();
                System.exit(0);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
