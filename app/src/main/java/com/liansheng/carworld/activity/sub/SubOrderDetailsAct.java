package com.liansheng.carworld.activity.sub;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.LatLng;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.kongzue.dialog.interfaces.OnDialogButtonClickListener;
import com.kongzue.dialog.util.BaseDialog;
import com.kongzue.dialog.v3.CustomDialog;
import com.kongzue.dialog.v3.MessageDialog;
import com.liansheng.carworld.R;
import com.liansheng.carworld.activity.BaseActivity;
import com.liansheng.carworld.activity.OrderDetailsActivity;
import com.liansheng.carworld.activity.TransportDetailsActivity;
import com.liansheng.carworld.activity.logic.OtherLogic;
import com.liansheng.carworld.bean.OrderDetails;
import com.liansheng.carworld.bean.child.ChildInfoBean;
import com.liansheng.carworld.kit.Constant;
import com.liansheng.carworld.net.JsonCallback;
import com.liansheng.carworld.net.NetApi;
import com.liansheng.carworld.net.UrlKit;
import com.liansheng.carworld.utils.DateParserHelper;
import com.liansheng.carworld.utils.XToastUtils;
import com.liansheng.carworld.view.MyDialog;
import com.liansheng.carworld.view.StepsView;
import com.xgr.alipay.alipay.AliPay;
import com.xgr.alipay.alipay.AlipayInfoImpli;
import com.xgr.easypay.EasyPay;
import com.xgr.easypay.callback.IPayCallback;
import com.xuexiang.xui.widget.button.switchbutton.SwitchButton;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.droidlover.xdroid.hold.info.SharedInfo;
import cn.droidlover.xdroid.tools.utils.ActivityManage;
import okhttp3.Call;

public class SubOrderDetailsAct extends BaseActivity {

    //    @BindView(R.id.topbar)
//    QMUITopBar topbar;
    @BindView(R.id.tv_start)
    TextView tvStart;
    @BindView(R.id.tv_start_city)
    TextView tvStartCity;
    @BindView(R.id.llyt_start)
    LinearLayout llytStart;
    @BindView(R.id.tv_end)
    TextView tvEnd;
    @BindView(R.id.tv_end_city)
    TextView tvEndCity;
    @BindView(R.id.llyt_end)
    LinearLayout llytEnd;
    @BindView(R.id.tv_prices)
    TextView tvPrice;
    @BindView(R.id.tv_distance)
    TextView tvDistance;
    @BindView(R.id.tv_jdsj)
    TextView tvJdsj;
    @BindView(R.id.tv_zcsj)
    TextView tvZcsj;
    @BindView(R.id.tv_yjdd)
    TextView tvYjdd;
    @BindView(R.id.tv_cx)
    TextView tvCx;
    @BindView(R.id.tv_bc)
    TextView tvBc;
    @BindView(R.id.tv_gzc)
    TextView tvGzc;
    @BindView(R.id.sw_ycdk)
    SwitchButton swYcdk;
    @BindView(R.id.steps)
    StepsView steps;
    @BindView(R.id.iv_step)
    ImageView ivStep;
    @BindView(R.id.tv_fcr_name)
    TextView tvFcrName;
    @BindView(R.id.tv_fcr_phone)
    TextView tvFcrPhone;
    @BindView(R.id.tv_jcr_name)
    TextView tvJcrName;
    @BindView(R.id.tv_bz)
    TextView tvBz;
    @BindView(R.id.tv_jcr_phone)
    TextView tvJcrPhone;
    @BindView(R.id.btn_pay)
    TextView btn_pay;
    @BindView(R.id.iv_tran)
    ImageView ivTran;
    @BindView(R.id.main)
    LinearLayout main;
    @BindView(R.id.iv_dd)
    ImageView ivDd;
    @BindView(R.id.tv_header)
    TextView tvHeader;
    @BindView(R.id.btn_cancel)
    Button btnCancel;
    @BindView(R.id.tv_orderid)
    TextView tvOrderid;
    @BindView(R.id.btn_cfd)
    Button btnCfd;
    @BindView(R.id.btn_ysz)
    Button btnYsz;
    @BindView(R.id.btn_mdd)
    Button btnMdd;
    @BindView(R.id.btn_back)
    ImageView btnBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.btn_function)
    TextView btnFunction;
    @BindView(R.id.btn_right)
    TextView btnRight;
    @BindView(R.id.map)
    MapView mMapView;
    public AMap aMap;
    private int currentPosition = 0;
    private String id = "";
    private OrderDetails orderDetails;

    //标准模板
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        Intent intent = getIntent();
        id = intent.getStringExtra("id");
        tvTitle.setText("订单详情");
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btnFunction.setVisibility(View.GONE);
        initView();
        aMap.setOnMapClickListener(new AMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                if (!orderDetails.getStatus().equals("creation")) {
                    Intent bundle = new Intent();
                    bundle.putExtra(Constant.KEY_ID, orderDetails.getId());
                    bundle.putExtra(Constant.KEY_TYPE,true);
                    ActivityManage.push(TransportDetailsActivity.class, bundle);
                }
            }
        });
    }

    private void initView() {
        aMap.getUiSettings().setZoomControlsEnabled(false);
        aMap.getUiSettings().setZoomGesturesEnabled(false);
        aMap.getUiSettings().setAllGesturesEnabled(false);
        NetApi.get(UrlKit.CHILD_ACCOUNT_TRANSPORT_ORDER_ID + id, null, new JsonCallback() {

            @Override
            public void onSuccess(String response, int id) {
                Gson gson = new Gson();
                orderDetails = gson.fromJson(response, OrderDetails.class);
                if (orderDetails.getCustomService() != null) {
                    btnRight.setVisibility(View.VISIBLE);
                    btnRight.setOnClickListener(v -> {
                        callPhone(SubOrderDetailsAct.this, "如有疑问请联系专属客服\n客服电话：" + orderDetails.getCustomService().getPhoneNumber(), orderDetails.getCustomService().getPhoneNumber());
                    });
                }
                setData(orderDetails);
                if (orderDetails.getDriver() != null && orderDetails.getDriver().getPosition() != null) {
                    LatLng latLng = new LatLng(Double.valueOf(orderDetails.getDriver().getPosition().getLatitude()), Double.valueOf(orderDetails.getDriver().getPosition().getLongitude()));//构造一个位置
                    aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 11));
                }
            }
        });
    }

    public void setData(OrderDetails response) {
        tvStart.setText(response.getOrigin().getName());
        tvStartCity.setText(response.getOrigin().getProvince() + response.getOrigin().getCity() + response.getOrigin().getCounty());
        tvEndCity.setText(response.getDestination().getProvince() + response.getDestination().getCity() + response.getDestination().getCounty());
        tvEnd.setText(response.getDestination().getName());
        tvDistance.setText(response.getDistance() / 1000 + "千米");
        if (response.getIncreasePrice() > 0) {
            int total = response.getPrice() + response.getIncreasePrice();
            tvPrice.setText("已加价:" + response.getIncreasePrice() + "元\n" + total + "元");
        } else {
            tvPrice.setText(response.getPrice() + "元");
        }
        tvOrderid.setText("订单号: " + response.getOrderNO());
        tvJdsj.setText("创建时间: \n" + DateParserHelper.getYearAndMonthAndDayAndTime(response.getCreation()));
        String time;
        if (response.getStartDate().getTimeSlot().equals("afternoon")) {
            time = " 下午";
        } else {
            time = " 上午";
        }
        tvZcsj.setText("装车时间: \n" + DateParserHelper.getYearAndMonthAndDayAndTime(response.getStartDate().getDate()) + time);
        if (response.getTimeOfArrival() != null) {
            tvYjdd.setText("预计到店时间: " + response.getTimeOfArrival());
            tvYjdd.setVisibility(View.VISIBLE);
        }
        if (response.isBigCar()) {
            tvBc.setText("板车要求: 大板车");
        } else {
            tvBc.setText("板车要求: 小板车—" + response.getTruckRequireString());
        }
        tvCx.setText("车型: " + response.getCarModel().getModel());
        if (response.getCarModel().isFault()) {
            tvGzc.setVisibility(View.VISIBLE);
        }
        swYcdk.setChecked(response.isCheckCarPay());
        String userId = SharedInfo.getInstance().getEntity(ChildInfoBean.class).getId();
        if (response.getStatus().equals("creation")) {
            currentPosition = R.drawable.order_details_1;
            findViewById(R.id.iv_tran_ll).setVisibility(View.GONE);
            btnCancel.setVisibility(View.VISIBLE);
//            if (response.isPaid()) {
                ivDd.setVisibility(View.VISIBLE);
                Glide.with(this).load(R.drawable.order_dd).into(ivDd);
                tvHeader.setVisibility(View.GONE);
//            }
        } else if (response.getStatus().equals("processed")) {
            currentPosition = R.drawable.order_details_2;
            if  (userId.equals(orderDetails.getDriverId())) {
                btnCfd.setVisibility(View.VISIBLE);
                btnYsz.setVisibility(View.GONE);
                btnMdd.setVisibility(View.GONE);
            }
        } else if (response.getStatus().equals("origin")) {
            currentPosition = R.drawable.order_details_3;
            if  (userId.equals(orderDetails.getDriverId())) {
                btnYsz.setVisibility(View.VISIBLE);
                btnCfd.setVisibility(View.GONE);
                btnMdd.setVisibility(View.GONE);
            }
        } else if (response.getStatus().equals("transportation")) {
            currentPosition = R.drawable.order_details_4;
            if (userId.equals(orderDetails.getDriverId())) {
                btnMdd.setVisibility(View.VISIBLE);
                btnYsz.setVisibility(View.GONE);
                btnCfd.setVisibility(View.GONE);
            }
        } else if (response.getStatus().equals("destination")) {
            currentPosition = R.drawable.order_details_5;
            btnMdd.setVisibility(View.GONE);
            btnYsz.setVisibility(View.GONE);
            btnCfd.setVisibility(View.GONE);
        } else if (response.getStatus().equals("completion")) {
            currentPosition = R.drawable.order_details_6;
        }
        Glide.with(SubOrderDetailsAct.this).load(currentPosition).into(ivStep);
        tvFcrName.setText("联系人：" + response.getOriginContacts().getName());
        tvFcrPhone.setText("手机：" + response.getOriginContacts().getMobile());
        tvJcrName.setText("联系人：" + response.getDestinationContacts().getName());
        tvJcrPhone.setText("手机：" + response.getDestinationContacts().getMobile());
        tvBz.setText(response.getNote());
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        mMapView.onCreate(savedInstanceState);
        if (aMap == null) {
            aMap = mMapView.getMap();
        }
    }

    public void showDialog(String content) {
        MessageDialog.build(SubOrderDetailsAct.this)
                .setTitle("温馨提示")
                .setMessage(content)
                .setCancelButton("点错了", new OnDialogButtonClickListener() {
                    @Override
                    public boolean onClick(BaseDialog baseDialog, View v) {
                        return false;
                    }
                })
                .setOkButton("确定", new OnDialogButtonClickListener() {
                    @Override
                    public boolean onClick(BaseDialog baseDialog, View v) {
                        goNextStatus();
                        return false;
                    }
                })
                .show();
    }

    public void goNextStatus() {
        HashMap<String, Object> map=new HashMap<>();
        map.put("orderId",id);
        NetApi.put(UrlKit.CHILD_ACCOUNT_NEXT_STATUS, map, new JsonCallback() {

            @Override
            public void onSuccess(String response, int id) {
                showSnackToast(main, "操作成功");
                initView();
            }
        });
    }


    @Override
    public int getLayoutId() {
        return R.layout.activity_detials;
    }//布局文件

    @OnClick({R.id.iv_fcr_phone, R.id.iv_jcr_phone, R.id.llyt_start, R.id.llyt_end, R.id.llyt_transport, R.id.btn_cfd, R.id.btn_ysz, R.id.btn_mdd})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_fcr_phone:
                callPhone(this, "请问是否要联系发车人电话？", orderDetails.getOriginContacts().getMobile());
                break;
            case R.id.iv_jcr_phone:
                callPhone(this, "请问是否要联系接车人电话？", orderDetails.getDestinationContacts().getMobile());
                break;
            case R.id.llyt_start:
                OtherLogic.showNav(context,orderDetails.getOrigin().getLongitude(), orderDetails.getOrigin().getLatitude(), orderDetails.getOrigin().getName());
                break;
            case R.id.llyt_end:
                OtherLogic.showNav(context,orderDetails.getDestination().getLongitude(), orderDetails.getDestination().getLatitude(), orderDetails.getDestination().getName());
                break;
            case R.id.llyt_transport:
                if (!orderDetails.getStatus().equals("creation")) {
                    Intent bundle = new Intent();
                    bundle.putExtra(Constant.KEY_ID, orderDetails.getId());
                    bundle.putExtra(Constant.KEY_TYPE,true);
                    ActivityManage.push(TransportDetailsActivity.class, bundle);
                }
                break;
            case R.id.btn_cfd:
                showDialog("确定已到达出发地吗？");
                break;
            case R.id.btn_ysz:
                showDialog("确定开始运输车辆吗？");
                break;
            case R.id.btn_mdd:
                showDialog("确定已到达目的地吗？");
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //在activity执行onDestroy时执行mMapView.onDestroy()，销毁地图
        mMapView.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView.onResume ()，重新绘制加载地图
        mMapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView.onPause ()，暂停地图的绘制
        mMapView.onPause();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //在activity执行onSaveInstanceState时执行mMapView.onSaveInstanceState (outState)，保存地图当前的状态
        mMapView.onSaveInstanceState(outState);
    }
}
