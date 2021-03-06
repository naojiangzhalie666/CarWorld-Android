package com.liansheng.carworld.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.LatLngBounds;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.MyLocationStyle;
import com.amap.api.maps.model.PolylineOptions;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.route.BusRouteResult;
import com.amap.api.services.route.DrivePath;
import com.amap.api.services.route.DriveRouteResult;
import com.amap.api.services.route.DriveStep;
import com.amap.api.services.route.RideRouteResult;
import com.amap.api.services.route.RouteSearch;
import com.amap.api.services.route.WalkRouteResult;
import com.google.gson.Gson;
import com.liansheng.carworld.R;
import com.liansheng.carworld.activity.logic.OtherLogic;
import com.liansheng.carworld.bean.OrderDetails;
import com.liansheng.carworld.kit.Constant;
import com.liansheng.carworld.net.JsonCallback;
import com.liansheng.carworld.net.NetApi;
import com.liansheng.carworld.net.UrlKit;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;

public class TransportDetailsActivity extends BaseActivity {

    @BindView(R.id.map)
    MapView mMapView;
    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.tv_status)
    TextView tvStatus;
    @BindView(R.id.tv_kf)
    TextView tvKf;
    @BindView(R.id.tv_grade)
    TextView tvGrade;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_mobile)
    TextView tvMobile;
    @BindView(R.id.tv_company)
    TextView tvCompany;
    @BindView(R.id.tv_idcard)
    TextView tvIdcard;

    private OrderDetails orderDetails;
    private RouteSearch mRouteSearch;//???????????????
    private int current = 1;
    private LatLonPoint startPoint;
    private LatLonPoint currentPoint;
    private LatLonPoint endPoint;
    public AMap aMap;

    @Override
    public void initData(Bundle savedInstanceState) {
        mMapView.onCreate(savedInstanceState);
        if (aMap == null) {
            aMap = mMapView.getMap();
        }
        getDetails(getIntent().getStringExtra(Constant.KEY_ID), getIntent().getBooleanExtra(Constant.KEY_TYPE, false));
    }

    private void getDetails(String orderId, boolean isChild) {
        if (isChild) {
            NetApi.get(UrlKit.CHILD_ACCOUNT_TRANSPORT_ORDER_ID + orderId, null, new JsonCallback() {

                @Override
                public void onSuccess(String response, int id) {
                    Gson gson = new Gson();
                    orderDetails = gson.fromJson(response, OrderDetails.class);
                    location();
                }
            });
        } else {
            NetApi.get(UrlKit.ORDER_DETIALS + orderId, null, new JsonCallback() {

                @Override
                public void onSuccess(String response, int id) {
                    Gson gson = new Gson();
                    orderDetails = gson.fromJson(response, OrderDetails.class);
                    location();
                }
            });
        }
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_transport_details;
    }

    public void location() {
//        aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(Double.parseDouble(orderDetails.getDestination().getLatitude()), Double.parseDouble(orderDetails.getDestination().getLongitude())),8));
        startPoint = new LatLonPoint(Double.parseDouble(orderDetails.getOrigin().getLatitude()), Double.parseDouble(orderDetails.getOrigin().getLongitude()));
        if (orderDetails.getDriver() != null && orderDetails.getDriver().getPosition() != null) {
            currentPoint = new LatLonPoint(Double.parseDouble(orderDetails.getDriver().getPosition().getLatitude()), Double.parseDouble(orderDetails.getDriver().getPosition().getLongitude()));
        }
        endPoint = new LatLonPoint(Double.parseDouble(orderDetails.getDestination().getLatitude()), Double.parseDouble(orderDetails.getDestination().getLongitude()));
        initSearch();
        if (orderDetails.getStatus().equals("creation")) {
            tvStatus.setText("?????????");
        } else if (orderDetails.getStatus().equals("processed")) {
            tvStatus.setText("?????????");
        } else if (orderDetails.getStatus().equals("origin")) {
            tvStatus.setText("???????????????");
        } else if (orderDetails.getStatus().equals("transportation")) {
            tvStatus.setText("?????????");
        } else if (orderDetails.getStatus().equals("destination")) {
            tvStatus.setText("???????????????");
        } else if (orderDetails.getStatus().equals("completion")) {
            tvStatus.setText("?????????");
        }
        if (orderDetails.getDriver() != null) {
            tvGrade.setText(orderDetails.getDriver().getScore());
            if (orderDetails.getDriver().getName() != null) {
                tvName.setText("?????????" + orderDetails.getDriver().getName());
            }
            if (orderDetails.getDriver().getMobile() != null) {
                tvMobile.setText("????????????" + orderDetails.getDriver().getMobile());
            }
            if (orderDetails.getDriver().getCompany() != null) {
                tvCompany.setText("?????????" + orderDetails.getDriver().getCompany());
            }
            if (orderDetails.getDriver().getCardNo() != null) {
                tvIdcard.setText("????????????" + orderDetails.getDriver().getCardNo());
            }
        }
    }

    private void initSearch() {
        if (orderDetails.getDriver() == null || orderDetails.getDriver().getPosition() == null || TextUtils.isEmpty(orderDetails.getDriver().getPosition().getLatitude())) {
            current = 4;
        }//????????????????????????
        mRouteSearch = new RouteSearch(this);
        mRouteSearch.setRouteSearchListener(new OnRouteSearchListener());
        searchRouteResult(startPoint, endPoint);
    }

    public void searchRouteResult(LatLonPoint startPoint, LatLonPoint endPoint) {
        MyLocationStyle myLocationStyle = new MyLocationStyle();
        myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_SHOW);
        myLocationStyle.interval(2000);
        myLocationStyle.strokeColor(Color.argb(0, 0, 0, 0));
        myLocationStyle.radiusFillColor(Color.argb(0, 0, 0, 0));
        myLocationStyle.myLocationIcon(BitmapDescriptorFactory.fromResource(R.mipmap.icon_black));
        aMap.setMyLocationStyle(myLocationStyle);
        aMap.getUiSettings().setMyLocationButtonEnabled(false);
        aMap.getUiSettings().setZoomControlsEnabled(false);
        aMap.setMyLocationEnabled(true);
        RouteSearch.FromAndTo fromAndTo = new RouteSearch.FromAndTo(startPoint, endPoint);
        //?????????????????? 1.fromAndTo ?????????????????????  2.?????????????????????(?????????????????????,???????????????API) 3.???
        //??????????????? 4.???????????????????????? 5.???????????? ,??????
        RouteSearch.DriveRouteQuery query = new
                RouteSearch.DriveRouteQuery(fromAndTo, RouteSearch.DRIVING_SINGLE_DEFAULT, null, null, "");
        //??????????????????
        mRouteSearch.calculateDriveRouteAsyn(query);
    }

    @OnClick({R.id.phone_ll, R.id.tv_kf, R.id.tv_mobile, R.id.back})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.phone_ll:
                if (orderDetails != null && orderDetails.getDriver() != null) {
                    OtherLogic.callPhone(this, "?????????????????????" + orderDetails.getDriver().getMobile(), orderDetails.getDriver().getMobile());
                }
                break;
            case R.id.tv_kf:
                OtherLogic.callPhone(this, "?????????????????????" + Constant.SERVICE_PHONE, Constant.SERVICE_PHONE);
                break;
            case R.id.tv_mobile:
                break;
            case R.id.back:
                finish();
                break;
        }
    }

    class OnRouteSearchListener implements RouteSearch.OnRouteSearchListener {

        @Override
        public void onBusRouteSearched(BusRouteResult busRouteResult, int i) {

        }

        @Override
        public void onDriveRouteSearched(DriveRouteResult driveRouteResult, int rCode) {
            if (rCode == 1000) {//????????????????????????,??????????????????,????????????????????????
                List<DrivePath> paths = driveRouteResult.getPaths();
                //??????????????????????????????
                List<LatLng> latLngs = new ArrayList<>();
                //??????????????????????????????????????????
                for (DrivePath mDrivePath : paths) {
                    for (DriveStep mDriveStep : mDrivePath.getSteps()) {
                        for (LatLonPoint mLatLonPoint : mDriveStep.getPolyline()) {
                            latLngs.add(new
                                    LatLng(mLatLonPoint.getLatitude(), mLatLonPoint.getLongitude()));
                        }
                    }
                }
//                aMap.clear();
                //??????????????????????????????marker
                if (current == 1) {
                    //????????????????????????marker????????????
                    LatLngBounds.Builder builder = new LatLngBounds.Builder();
                    for (int i = 0; i < latLngs.size(); i++) {
                        builder.include(latLngs.get(i));
                    }
                    //????????????marker,????????????????????????????????????
                    aMap.moveCamera(CameraUpdateFactory.newLatLngBounds(builder.build(), 330));
                    current++;
                    searchRouteResult(startPoint, currentPoint);
                } else if (current == 2) {
                    aMap.addMarker(new MarkerOptions()
                            .icon(BitmapDescriptorFactory.fromResource(R.mipmap.icon_tran_start))
                            .position(new LatLng(startPoint.getLatitude(), startPoint.getLongitude())));
                    aMap.addMarker(new MarkerOptions()
                            .icon(BitmapDescriptorFactory.fromResource(R.mipmap.icon_tran_current))
                            .position(new LatLng(currentPoint.getLatitude(), currentPoint.getLongitude())));
                    //????????????????????????
                    aMap.addPolyline(new PolylineOptions()
                            //????????????????????????
                            .addAll(latLngs)
                            //????????????
                            .width(20)
                            .color(getResources().getColor(R.color.order)));
                    current++;
                    searchRouteResult(currentPoint, endPoint);
                } else if (current == 3) {
                    aMap.addMarker(new MarkerOptions()
                            .icon(BitmapDescriptorFactory.fromResource(R.mipmap.icon_tran_end))
                            .position(new LatLng(endPoint.getLatitude(), endPoint.getLongitude())));
                    aMap.addPolyline(new PolylineOptions()
                            .addAll(latLngs)
                            .width(20)
                            .color(getResources().getColor(R.color.end)));
                } else if (current == 4) {
                    LatLngBounds.Builder builder = new LatLngBounds.Builder();
                    for (int i = 0; i < latLngs.size(); i++) {
                        builder.include(latLngs.get(i));
                    }
                    aMap.moveCamera(CameraUpdateFactory.newLatLngBounds(builder.build(), 330));
                    aMap.addMarker(new MarkerOptions()
                            .icon(BitmapDescriptorFactory.fromResource(R.mipmap.icon_tran_start))
                            .position(new LatLng(startPoint.getLatitude(), startPoint.getLongitude())));
                    aMap.addMarker(new MarkerOptions()
                            .icon(BitmapDescriptorFactory.fromResource(R.mipmap.icon_tran_end))
                            .position(new LatLng(endPoint.getLatitude(), endPoint.getLongitude())));
                    aMap.addPolyline(new PolylineOptions()
                            .addAll(latLngs)
                            .width(20)
                            .color(getResources().getColor(R.color.order)));
                }
            }
        }

        @Override
        public void onWalkRouteSearched(WalkRouteResult walkRouteResult, int i) {

        }

        @Override
        public void onRideRouteSearched(RideRouteResult rideRouteResult, int i) {

        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mMapView.onSaveInstanceState(outState);
    }

}
