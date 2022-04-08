package com.liansheng.carworld.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Point;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.BounceInterpolator;
import android.view.animation.Interpolator;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.amap.api.location.AMapLocation;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.Projection;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.MyLocationStyle;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;
import com.liansheng.carworld.R;
import com.liansheng.carworld.utils.map.GDMapUtils;
import com.zaaach.citypicker.model.City;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import pub.devrel.easypermissions.EasyPermissions;

public class ChooseLocationActivity extends BaseActivity implements PoiSearch.OnPoiSearchListener {

    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.search_input)
    EditText searchEt;
    @BindView(R.id.ll)
    RecyclerView recyclerView;
    @BindView(R.id.map)
    MapView mMapView;
    @BindView(R.id.confirm)
    TextView confirm;
    @BindView(R.id.iv_clear)
    ImageView ivClear;
    private MyLocationStyle myLocationStyle;
    private PoiSearch poiSearch;
    private PoiSearch.Query query;
    boolean isPoiSearched = false; //是否进行poi搜索
    ArrayList<PoiItem> poiItemArrayList;
    MyAdpter adapter;
    MyHandler myHandler;
    private double mCurrentLat;
    private double mCurrentLng;
    Map<String, String> currentInfo = new HashMap<>();
    ImageView currentSelectItem = null;
//    private Location local;
//    private CameraPosition cameraBean;
//    private MyAdpter gaoDeMapListAdapter;
//    private List<PoiItem> multitemPoiItemList = new ArrayList<>();
//    private GeocodeSearch geocoderSearch;
    private String type;
    public AMap aMap;
    public Marker marker;
    public PoiItem poiItem;

    @Override
    public void initData(Bundle savedInstanceState) {
        Intent intent = getIntent();
        type = intent.getStringExtra("type");
        if (EasyPermissions.hasPermissions(this, PERMS_LOACTION_STATE)) {
            location();
        } else {
            EasyPermissions.requestPermissions(this, "我们需要您的位置信息，请点击确定打开位置权限.",
                    PER_LOACTION, PERMS_LOACTION_STATE);
        }
        findAllView();
        setAllViewOnclickLinster();
        mMapView.onCreate(savedInstanceState);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_choose_location;
    }

    public void location() {
        new GDMapUtils().location(aMapLocation -> {
            mCurrentLat = aMapLocation.getLatitude();
            mCurrentLng = aMapLocation.getLongitude();
            setMapCenter(aMapLocation);
            drawLatLng(new LatLng(aMapLocation.getLatitude(), aMapLocation.getLongitude()));
//                    showToast(mCurrentLng + "");
            searchPoi("", currentInfo.get("cityCode"), true);
        });
        if (aMap == null) {
            aMap = mMapView.getMap();
        }
        myLocationStyle = new MyLocationStyle();//初始化定位蓝点样式类myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE);//连续定位、且将视角移动到地图中心点，定位点依照设备方向旋转，并且会跟随设备移动。（1秒1次定位）如果不设置myLocationType，默认也会执行此种模式。
        myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_SHOW);
        myLocationStyle.interval(2000); //设置连续定位模式下的定位间隔，只在连续定位模式下生效，单次定位模式下不会生效。单位为毫秒。
        myLocationStyle.strokeColor(Color.argb(0, 0, 0, 0));// 设置圆形的边框颜色
        myLocationStyle.radiusFillColor(Color.argb(0, 0, 0, 0));// 设置圆形的填充颜色
        myLocationStyle.myLocationIcon(BitmapDescriptorFactory.fromResource(R.mipmap.icon_black));
        aMap.setMyLocationStyle(myLocationStyle); //设置定位蓝点的style
        aMap.getUiSettings().setMyLocationButtonEnabled(false); // 设置默认定位按钮是否显示，非必须设置
        aMap.getUiSettings().setZoomControlsEnabled(false);
        aMap.setMyLocationEnabled(true);//设置为true表示启动显示定位蓝点，false表示隐藏定位蓝点并不进行定位，默认是false

    }

    /**
     * 获取view对象，初始化一些对象
     */
    void findAllView() {
        poiItemArrayList = new ArrayList<>();
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new MyAdpter(poiItemArrayList, aMap);
        recyclerView.setAdapter(adapter);
        myHandler = new MyHandler();
    }

    void setAllViewOnclickLinster() {
        searchEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length()>0) {
                    ivClear.setVisibility(View.VISIBLE);
                }
                searchPoi(s + "", currentInfo.get("cityCode"), false);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        searchEt.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_UP) {
                    String key = searchEt.getText().toString();
                    if (!key.trim().isEmpty()) {
                        if (currentSelectItem != null) {
                            currentSelectItem.setVisibility(View.INVISIBLE);
                        }
                        searchPoi(key, currentInfo.get("cityCode"), false);
                    }
                    return true;
                }
                return false;
            }
        });

        ivClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchEt.setText("");
                ivClear.setVisibility(View.GONE);
            }
        });
        //返回处理事件
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
//                intent.putExtra("start", poiItem);
                setResult(Activity.RESULT_OK, intent);
                finish();
            }
        });
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                if (type.equals("start")) {
                    intent.putExtra("start", poiItem);
                    setResult(Activity.RESULT_OK, intent);
                } else {
                    intent.putExtra("end", poiItem);
                    setResult(Activity.RESULT_OK, intent);
                }
                finish();
            }
        });
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
    }

    class MyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0x001:
                    //加载listview中数据
//                    showToast("handle");
                    adapter.notifyDataSetChanged();
                    break;
            }
        }
    }

    public class MyAdpter extends RecyclerView.Adapter<MyAdpter.ViewHolder> {

        private List<PoiItem> MpoiItemArrayList;
        private int expandPosition = -1;
        public AMap aMap;

        class ViewHolder extends RecyclerView.ViewHolder {
            TextView v1;
            TextView v2;
            ImageView iv;

            public ViewHolder(View view) {
                super(view);
                v1 = (TextView) view.findViewById(R.id.name);
                v2 = (TextView) view.findViewById(R.id.sub);
                iv = (ImageView) view.findViewById(R.id.yes);
            }
        }

        public MyAdpter(List<PoiItem> poiItemArrayList, AMap map) {
            MpoiItemArrayList = poiItemArrayList;
            aMap = map;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.location_item, parent, false);
            ViewHolder holder = new ViewHolder(view);
            return holder;
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int i) {
            PoiItem item = MpoiItemArrayList.get(i);
            holder.v1.setText(item.getTitle());
            holder.v2.setText(item.getSnippet());
            holder.iv.setVisibility(i == expandPosition ? View.VISIBLE : View.GONE);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    if (expandPosition != i) {
//                        notifyItemChanged(expandPosition);
//                        expandPosition = i;
//                    } else {
//                        expandPosition = -1;
//                    }
//                    notifyItemChanged(i);
                    poiItem = item;
                    searchEt.setText(item.getTitle());
                    aMap.animateCamera(CameraUpdateFactory.newCameraPosition(
                            new CameraPosition(new LatLng(poiItem.getLatLonPoint().getLatitude(), poiItem.getLatLonPoint().getLongitude())
                                    , 15, 0, 0)), 300, null); //设置地图中心点
                    drawLatLng(new LatLng(poiItem.getLatLonPoint().getLatitude(), poiItem.getLatLonPoint().getLongitude()));
                }
            });
        }

        @Override
        public int getItemCount() {
            return MpoiItemArrayList.size();
        }
    }

    /**
     * 搜索poi
     *
     * @param key      关键字
     * @param cityCode 城市代码，或者城市名称
     * @param nearby   是否搜索周边
     */
    void searchPoi(String key, String cityCode, boolean nearby) {
        isPoiSearched = true;
        query = new PoiSearch.Query(key, "", TextUtils.isEmpty(cityCode)?"":cityCode);
        //keyWord表示搜索字符串，
        //第二个参数表示POI搜索类型，二者选填其一，
        //POI搜索类型共分为以下20种：汽车服务|汽车销售|
        //汽车维修|摩托车服务|餐饮服务|购物服务|生活服务|体育休闲服务|医疗保健服务|
        //住宿服务|风景名胜|商务住宅|政府机构及社会团体|科教文化服务|交通设施服务|
        //金融保险服务|公司企业|道路附属设施|地名地址信息|公共设施
        //cityCode表示POI搜索区域，可以是城市编码也可以是城市名称，也可以传空字符串，空字符串代表全国在全国范围内进行搜索
        query.setPageSize(20);// 设置每页最多返回多少条poiitem
//        query.setPageNum(pageNum);//设置查询页码
        poiSearch = new PoiSearch(this, query);
        poiSearch.setOnPoiSearchListener(this);
        if (nearby)
            poiSearch.setBound(new PoiSearch.SearchBound(new LatLonPoint(mCurrentLat,
                    mCurrentLng), 1000));//设置周边搜索的中心点以及半径
        poiSearch.searchPOIAsyn();
    }

    @Override
    public void onPoiSearched(PoiResult poiResult, int i) {
        int index = 0;
        if (poiResult != null) {
            ArrayList<PoiItem> result = poiResult.getPois();
            if (result.size() > 0) {
                poiItemArrayList.clear();
                poiItemArrayList.addAll(result);
                adapter.notifyDataSetChanged();
                myHandler.sendEmptyMessage(0x001);
            }
        }
    }

    @Override
    public void onPoiItemSearched(PoiItem poiItem, int i) {

    }

    private void drawLatLng(LatLng latLng) {
        if (marker != null) {
            marker.remove();
        }
        MarkerOptions options = new MarkerOptions()
                //自定义图标 不添加为蓝色点点
                .icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory.decodeResource(getResources(), R.mipmap.icon_location)))
                //在地图上标记位置的经纬度值。必填参数
                .position(latLng)
                //点标记的标题
                //点标记的内容
                .snippet("DefaultMarker")
                //点标记是否可拖拽
                .draggable(false)
                //点标记是否可见
                .visible(true)
                //点标记的透明度
                .alpha(1.0f)
                //设置marker平贴地图效果
                .setFlat(true);
        marker = aMap.addMarker(options);
        jumpPoint(marker);

    }

    public void jumpPoint(final Marker marker) {
        final Handler handler = new Handler();
        final long start = SystemClock.uptimeMillis();
        Projection proj = aMap.getProjection();
        final LatLng markerLatlng = marker.getPosition();
        Point markerPoint = proj.toScreenLocation(markerLatlng);
        markerPoint.offset(0, -100);
        final LatLng startLatLng = proj.fromScreenLocation(markerPoint);
        final long duration = 1500;
        final Interpolator interpolator = new BounceInterpolator();
        handler.post(new Runnable() {
            @Override
            public void run() {
                long elapsed = SystemClock.uptimeMillis() - start;
                float t = interpolator.getInterpolation((float) elapsed
                        / duration);
                double lng = t * markerLatlng.longitude + (1 - t)
                        * startLatLng.longitude;
                double lat = t * markerLatlng.latitude + (1 - t)
                        * startLatLng.latitude;
                marker.setPosition(new LatLng(lat, lng));
                if (t < 1.0) {
                    handler.postDelayed(this, 16);
                }
            }
        });
    }

    public void setMapCenter(AMapLocation amapLocation) {
        aMap.animateCamera(CameraUpdateFactory.newCameraPosition(
                new CameraPosition(new LatLng(amapLocation.getLatitude(), amapLocation.getLongitude())
                        , 15, 0, 0)), 300, null); //设置地图中心点
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
//    private void query(String keyWord, double lat, double lng) {
////        DialogUtil.waitDialog(mContext, "正在搜索周边地址...");
//        //keyWord表示搜索字符串，
//        //第二个参数表示POI搜索类型，二者选填其一，选用POI搜索类型时建议填写类型代码，码表可以参考下方（而非文字）
//        //cityCode表示POI搜索区域，可以是城市编码也可以是城市名称，也可以传空字符串，空字符串代表全国在全国范围内进行搜索
//        PoiSearch.Query query = new PoiSearch.Query("上海金茂大厦", "", "");
//        // 设置每页最多返回多少条poiitem
//        query.setPageSize(50);
//        //设置查询页码
//        query.setPageNum(0);
//        PoiSearch poiSearch = new PoiSearch(ChooseLocationActivity2.this, query);
//        //设置周边搜索的中心点以及半径
//        poiSearch.setBound(new PoiSearch.SearchBound(new LatLonPoint(lat, lng), 800));
//        poiSearch.setOnPoiSearchListener(ChooseLocationActivity2.this);
//        poiSearch.searchPOIAsyn();
//    }
}
