package com.liansheng.carworld.activity.home;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
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
import com.liansheng.carworld.activity.BaseActivity;
import com.liansheng.carworld.activity.OrderDetailsActivity;
import com.liansheng.carworld.bean.Distance;
import com.liansheng.carworld.bean.TransporterBean;
import com.liansheng.carworld.bean.UserInfo;
import com.liansheng.carworld.kit.Constant;
import com.liansheng.carworld.net.JsonCallback;
import com.liansheng.carworld.net.NetApi;
import com.liansheng.carworld.net.UrlKit;
import com.liansheng.carworld.utils.XToastUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cn.droidlover.xdroid.hold.info.SharedInfo;
import cn.droidlover.xdroid.tools.utils.ActivityManage;
import cn.droidlover.xdroid.tools.utils.Util;
import okhttp3.Call;

public class OrderPopAct extends BaseActivity {
    TextView pop_order_start;
    TextView pop_order_end;
    TextView pop_order_distance;
    MapView mMapView;
    TextView pop_order_loc;
    TextView pop_order_cancel;
    TextView pop_order_btn1;
    TextView pop_order_btn2;
    private AMap aMap;
    private TransporterBean item;
    private int current = 1;
    private LatLonPoint startPoint;
    private LatLonPoint currentPoint;
    private RouteSearch mRouteSearch;

    @Override
    public void initData(Bundle savedInstanceState) {
        pop_order_start = findViewById(R.id.pop_order_start);
        pop_order_end = findViewById(R.id.pop_order_end);
        pop_order_distance = findViewById(R.id.pop_order_distance);
        mMapView = findViewById(R.id.pop_order_map);
        pop_order_loc = findViewById(R.id.pop_order_loc);
        pop_order_cancel = findViewById(R.id.pop_order_cancel);
        pop_order_btn1 = findViewById(R.id.pop_order_btn1);
        pop_order_btn2 = findViewById(R.id.pop_order_btn2);
        mMapView.onCreate(savedInstanceState);
        item = getIntent().getParcelableExtra(Constant.KEY_BEAN);
        pop_order_cancel.setOnClickListener(v -> {
            finish();
        });
        pop_order_btn1.setOnClickListener(v -> {
            jd(item.getId());
        });
        pop_order_btn2.setOnClickListener(v -> {
            UserInfo userInfo = SharedInfo.getInstance().getEntity(UserInfo.class);
            if (userInfo != null) {
                for (int i = 0; i < userInfo.getTypes().size(); i++) {
                    if (Constant.USER_TYPE_4.equals(userInfo.getTypes().get(i))) {
                        Intent intent = new Intent();
                        intent.putExtra(Constant.KEY_ID, item.getId());
                        ActivityManage.push(DriverSelectAct.class, intent);
                        finish();
                        return;
                    }
                }
                jd(item.getId());
            }
        });
        initMap();
        upData(item, getIntent().getStringExtra("lng"), getIntent().getStringExtra("lat"));
    }

    @Override
    public int getLayoutId() {
        return R.layout.pop_order;
    }

    private void initMap() {
        if (aMap == null) {
            aMap = mMapView.getMap();
        }
        mRouteSearch = new RouteSearch(context);
        mRouteSearch.setRouteSearchListener(new OnRouteSearchListener());
    }

    public void upData(TransporterBean item, String lng, String lat) {
        if (!TextUtils.isEmpty(lng) && !TextUtils.isEmpty(lat)) {
            getDistance(lng, lat, item.getOrigin().getLongitude(), item.getOrigin().getLatitude());
        }
        String start = item.getOrigin().getCity() + item.getOrigin().getCounty() + item.getOrigin().getStreet();
        if (TextUtils.isEmpty(item.getOrigin().getCity())) {
            start = item.getOrigin().getProvince() + item.getOrigin().getCounty() + item.getOrigin().getStreet();
        }
        String end = item.getDestination().getCity() + item.getDestination().getCounty() + item.getDestination().getStreet();
        if (TextUtils.isEmpty(item.getOrigin().getCity())) {
            end = item.getDestination().getProvince() + item.getDestination().getCounty() + item.getDestination().getStreet();
        }
        pop_order_start.setText(start);
        pop_order_end.setText(end);
        pop_order_distance.setText(item.getDistance() / 1000 + "公里");

        UserInfo userInfo = SharedInfo.getInstance().getEntity(UserInfo.class);
        if (userInfo != null && userInfo.getTypes().size() > 0) {
            if (userInfo.getTypes().contains(Constant.USER_TYPE_4) && userInfo.getTypes().contains(Constant.USER_TYPE_2)) {
                pop_order_btn1.setVisibility(View.VISIBLE);
                pop_order_btn2.setVisibility(View.VISIBLE);
            } else if (userInfo.getTypes().contains(Constant.USER_TYPE_4)) {
                pop_order_btn1.setVisibility(View.INVISIBLE);
                pop_order_btn2.setVisibility(View.VISIBLE);
            }
        }
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
        initSearch(item, lng, lat);
    }

    private void initSearch(TransporterBean item, String lng, String lat) {
        startPoint = new LatLonPoint(Double.parseDouble(item.getOrigin().getLatitude()), Double.parseDouble(item.getOrigin().getLongitude()));
        if (TextUtils.isEmpty(lng) || TextUtils.isEmpty(lat)) {
            current = 4;
        } else {
            currentPoint = new LatLonPoint(Double.parseDouble(lat), Double.parseDouble(lng));
        }
        searchRouteResult(startPoint, currentPoint);
    }

    public void searchRouteResult(LatLonPoint startPoint, LatLonPoint endPoint) {
        RouteSearch.FromAndTo fromAndTo = new RouteSearch.FromAndTo(startPoint, endPoint);
        //设置搜索参数 1.fromAndTo 路径的起点终点  2.路径规划的策略(这里是驾车模式,具体看高德API) 3.途
        //经点，可选 4.避让区域，可选， 5.避让道路 ,可选
        RouteSearch.DriveRouteQuery query = new RouteSearch.DriveRouteQuery(fromAndTo, RouteSearch.DRIVING_SINGLE_DEFAULT, null, null, "");
        //开始异步查询
        mRouteSearch.calculateDriveRouteAsyn(query);
    }

    private void jd(String orderId) {
        NetApi.postDriver(SharedInfo.getInstance().getValue(Constant.KEY_TOKEN, "").toString(), orderId, new JsonCallback() {

            @Override
            public void onSuccess(String response, int id) {
                Util.toast("接单成功");
                Intent bundle = new Intent();
                bundle.putExtra("id", orderId);
                ActivityManage.push(OrderDetailsActivity.class, bundle);
                finish();
            }
        });
    }

    public void getDistance(String mCurrentLng, String mCurrentLat, String l, String la) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("origin.longitude", mCurrentLng);
        map.put("origin.latitude", mCurrentLat);
        map.put("destination.longitude", l);
        map.put("destination.latitude", la);
        map.put("checkCarPay", "");
        map.put("bigCar", "");
        map.put("truckRequire", "");
        NetApi.get(UrlKit.GET_DISTANCE, map, new JsonCallback() {

            @Override
            public void onSuccess(String response, int id) {
                Gson gson = new Gson();
                Distance d = gson.fromJson(response, Distance.class);
                String distance = d.getDistance() / 1000 + "";
                pop_order_loc.setVisibility(View.VISIBLE);
                pop_order_loc.setText("出发地距您当前位置 " + distance + " 公里");
            }
        });
    }

    private class OnRouteSearchListener implements RouteSearch.OnRouteSearchListener {

        @Override
        public void onBusRouteSearched(BusRouteResult busRouteResult, int i) {

        }

        @Override
        public void onDriveRouteSearched(DriveRouteResult driveRouteResult, int rCode) {
            if (rCode == 1000) {//获取规划路线成功,获取到的是了,路线坐标点的集合
                List<DrivePath> paths = driveRouteResult.getPaths();
                //创建存储坐标点的集合
                List<LatLng> latLngs = new ArrayList<>();
                //遍历获取规划的所有路线坐标点
                for (DrivePath mDrivePath : paths) {
                    for (DriveStep mDriveStep : mDrivePath.getSteps()) {
                        for (LatLonPoint mLatLonPoint : mDriveStep.getPolyline()) {
                            latLngs.add(new LatLng(mLatLonPoint.getLatitude(), mLatLonPoint.getLongitude()));
                        }
                    }
                }
//                aMap.clear();
                //绘制起始位置和目的地marker
                if (current == 1) {
                    //显示完整包含所有marker地图路线
                    LatLngBounds.Builder builder = new LatLngBounds.Builder();
                    for (int i = 0; i < latLngs.size(); i++) {
                        builder.include(latLngs.get(i));
                    }
                    //显示全部marker,第二个参数是四周留空宽度
                    aMap.moveCamera(CameraUpdateFactory.newLatLngBounds(builder.build(), 20));
                    current++;
                    searchRouteResult(startPoint, currentPoint);
                } else if (current == 2) {
                    aMap.addMarker(new MarkerOptions()
                            .icon(BitmapDescriptorFactory.fromResource(R.mipmap.icon_tran_start))
                            .position(new LatLng(startPoint.getLatitude(), startPoint.getLongitude())));
                    aMap.addMarker(new MarkerOptions()
                            .icon(BitmapDescriptorFactory.fromResource(R.mipmap.icon_tran_current))
                            .position(new LatLng(currentPoint.getLatitude(), currentPoint.getLongitude())));
                    //绘制规划路径路线
                    aMap.addPolyline(new PolylineOptions()
                            //路线坐标点的集合
                            .addAll(latLngs)
                            //线的宽度
                            .width(10)
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
        //在activity执行onDestroy时执行mMapView.onDestroy()，销毁地图
        mMapView.onDestroy();
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
