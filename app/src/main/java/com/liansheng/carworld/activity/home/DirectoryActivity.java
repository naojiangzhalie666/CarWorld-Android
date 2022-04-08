package com.liansheng.carworld.activity.home;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.kongzue.dialog.v3.CustomDialog;
import com.kunminx.linkage.LinkageRecyclerView;
import com.kunminx.linkage.adapter.viewholder.LinkagePrimaryViewHolder;
import com.kunminx.linkage.adapter.viewholder.LinkageSecondaryViewHolder;
import com.kunminx.linkage.bean.BaseGroupedItem;
import com.liansheng.carworld.R;
import com.liansheng.carworld.activity.AddDirectoryActivity;
import com.liansheng.carworld.activity.BaseActivity;
import com.liansheng.carworld.adapter.DirectoryAdapter;
import com.liansheng.carworld.adapter.DropDownAdapter;
import com.liansheng.carworld.adapter.other.AddressAdapter;
import com.liansheng.carworld.bean.Directory;
import com.liansheng.carworld.bean.home.BannerBean;
import com.liansheng.carworld.bean.other.AddressItemBean;
import com.liansheng.carworld.bean.other.PageBean;
import com.liansheng.carworld.kit.Constant;
import com.liansheng.carworld.net.JsonCallback;
import com.liansheng.carworld.net.NetApi;
import com.liansheng.carworld.net.UrlKit;
import com.liansheng.carworld.utils.CityUtil;
import com.liansheng.carworld.utils.CustomGroupedItem;
import com.liansheng.carworld.utils.CustomLinkagePrimaryAdapterConfig;
import com.liansheng.carworld.utils.CustomLinkageSecondaryAdapterConfig;
import com.liansheng.carworld.utils.ViewUtils;
import com.xuexiang.xui.widget.banner.widget.banner.BannerItem;
import com.xuexiang.xui.widget.banner.widget.banner.SimpleImageBanner;
import com.xuexiang.xui.widget.spinner.DropDownMenu;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.droidlover.xdroid.base.SimpleRecAdapter;
import cn.droidlover.xdroid.hold.info.SharedInfo;
import cn.droidlover.xdroid.views.recyclerView.DividerLine;
import cn.droidlover.xrecyclerview.XRecyclerContentLayout;
import cn.droidlover.xrecyclerview.XRecyclerView;
import okhttp3.Call;

//4S名录
public class DirectoryActivity extends BaseActivity {

    @BindView(R.id.contentLayout)
    XRecyclerContentLayout contentLayout;
    //    @BindView(R.id.topbar)
//    QMUITopBar topbar;
    @BindView(R.id.main)
    LinearLayout main;
    @BindView(R.id.ddm_content)
    DropDownMenu mDropDownMenu;
    @BindView(R.id.ivBanner)
    SimpleImageBanner ivBanner;
    @BindView(R.id.btn_back)
    ImageView btnBack;
    @BindView(R.id.btn_function)
    TextView btnFunction;
    List<View> mPopupViews = new ArrayList<>();

    private DirectoryAdapter mAdapter;
    private String brand = "";
    private String sort = "date";
    private String province = "";
    private String city = "";
    private String[] pxs = {"最新排序", "综合排序"};
    private DropDownAdapter mPxAdapter;
    AddressAdapter provinceAdapter;
    AddressAdapter cityAdapter;
    String type;

    @Override
    public void initData(Bundle savedInstanceState) {
        brand = getIntent().getStringExtra("brand");
        city = SharedInfo.getInstance().getValue(Constant.KEY_CITY, "").toString().replace("市","");
        type = getIntent().getStringExtra(Constant.KEY_TYPE);
        if (!"new".equals(type)) {
            String[] headers1 = {"综合排序", "全国"};
            String[] headers2 = {"综合排序", city};
            if (TextUtils.isEmpty(city)) {
                initBar(headers1);
            } else {
                initBar(headers2);
            }
            getBanner();
        } else {
            String[] headers1 = {"最新排序", "全国"};
            String[] headers2 = {"最新排序", city};
            if (TextUtils.isEmpty(city)) {
                initBar(headers1);
            } else {
                initBar(headers2);
            }
        }
        initAdapter();
        initView();
    }

    private void getBanner() {
        NetApi.get(UrlKit.APP_RESOURCES, null, new JsonCallback() {

            @Override
            public void onSuccess(String response, int id) {
                Gson gson = new Gson();
                BannerBean bannerBean = gson.fromJson(response, BannerBean.class);
                ivBanner.setSource(getBannerList(bannerBean.getDirectory_banners())).setOnItemClickListener((view, t, position) -> {
                }).setIsOnePageLoop(false).startScroll();
            }
        });
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_directory;
    }

    private void initView() {
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        if (!"new".equals(type)) {
            btnFunction.setText("新增4S店");
            btnFunction.setVisibility(View.VISIBLE);
            btnFunction.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    toActivity(AddDirectoryActivity.class, null);
                }
            });
            ivBanner.setVisibility(View.VISIBLE);
            sort = "synthetic";
        }
        if ((boolean) SharedInfo.getInstance().getValue(UrlKit.FIRST_ML, true)) {
            mLDialog();
        }
        getData(0);
        getProvince();
    }

    private void initBar(String[] headers) {
        final ListView pxView = new ListView(this);
        mPxAdapter = new DropDownAdapter(this, pxs);
        pxView.setDividerHeight(0);
        pxView.setAdapter(mPxAdapter);
        pxView.setOnItemClickListener((parent, view, position, id) -> {
            String px = pxs[position];
            mPxAdapter.setSelectPosition(position);
            mDropDownMenu.setTabMenuText(px);
            mDropDownMenu.closeMenu();
            if (position == 1) {
                sort = "synthetic";
            } else {
                sort = "date";
            }
            mAdapter.clearData();
            getData(0);
        });
        mPxAdapter.setSelectPosition(0);

        final View addressView = getLayoutInflater().inflate(R.layout.pop_address, null);
        RecyclerView provinceList = addressView.findViewById(R.id.pop_province_list);
        RecyclerView cityList = addressView.findViewById(R.id.pop_city_list);
        provinceList.addItemDecoration(new DividerLine(DividerLine.HORIZONTAL));
        cityList.addItemDecoration(new DividerLine(DividerLine.HORIZONTAL));
        provinceAdapter = new AddressAdapter(null);
        provinceAdapter.setOnItemClickListener((adapter, view, position) -> {
            AddressItemBean item = (AddressItemBean) adapter.getItem(position);
            List<AddressItemBean> data = adapter.getData();
            for (int i = 0; i < data.size(); i++) {
                AddressItemBean bean = data.get(i);
                if (bean.equals(item)) {
                    item.setSelected(true);
                } else {
                    bean.setSelected(false);
                }
            }
            adapter.replaceData(data);
            getCity(item.getName());
            province = item.getName();
        });
        cityAdapter = new AddressAdapter(null);
        cityAdapter.setOnItemClickListener((adapter, view, position) -> {
            AddressItemBean item = (AddressItemBean) adapter.getItem(position);
            city = item.getName();
            mDropDownMenu.setTabMenuText(city);
            mDropDownMenu.closeMenu();
            getData(0);
        });
        provinceList.setAdapter(provinceAdapter);
        cityList.setAdapter(cityAdapter);
        provinceList.setLayoutManager(new LinearLayoutManager(this));
        cityList.setLayoutManager(new LinearLayoutManager(this));
        mPopupViews.add(pxView);
        mPopupViews.add(addressView);
        TextView contentView = new TextView(this);
        contentView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0));
        contentView.setGravity(Gravity.CENTER);
        contentView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
        mDropDownMenu.setDropDownMenu(headers, mPopupViews, contentView);
    }

    private void getData(int page) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("province", province);
        if("全国".equals(city)){
            map.put("city", "");
        }else {
            map.put("city", city);
        }
        map.put("brand", TextUtils.isEmpty(brand) ? "" : brand);
        map.put("sort", sort);
        map.put("pageIndex", page);
        map.put("pageSize", DEFULT_RESULT);
        map.put("self", !TextUtils.isEmpty(SharedInfo.getInstance().getValue(Constant.KEY_TOKEN, "").toString()));
        NetApi.get(UrlKit.DIRECTORY, map, new JsonCallback() {

            @Override
            public void onSuccess(String response, int id) {
                Gson gson = new Gson();
                Directory data = gson.fromJson(response, Directory.class);
                if(page==0){
                    mAdapter.clearData();
                }
                if (data.getItems().size() > 0) {
                    mAdapter.addData(data.getItems());
                } else {
                    if (page == 0) {
                        contentLayout.showEmpty();
                        return;
                    }
                }
                if (data.getItems().size() < DEFULT_RESULT) {
                    MAX_PAGE = page;
                } else {
                    MAX_PAGE++;
                }
                contentLayout.getRecyclerView().setPage(page, MAX_PAGE);
            }
        });
    }

    private void getProvince() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("brand", TextUtils.isEmpty(brand) ? "" : brand);
        NetApi.get(UrlKit.AREA_PROVINCE, map, new JsonCallback() {

            @Override
            public void onSuccess(String response, int id) {
                PageBean<AddressItemBean> provinces = new Gson().fromJson(response, new TypeToken<PageBean<AddressItemBean>>() {
                }.getType());
                provinceAdapter.setNewData(provinces.getItems());
            }
        });
    }


    private void getCity(String province) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("brand", TextUtils.isEmpty(brand) ? "" : brand);
        map.put("province", province);
        NetApi.get(UrlKit.AREA_CITY, map, new JsonCallback() {

            @Override
            public void onSuccess(String response, int id) {
                PageBean<AddressItemBean> provinces = new Gson().fromJson(response, new TypeToken<PageBean<AddressItemBean>>() {
                }.getType());
                cityAdapter.setNewData(provinces.getItems());
            }
        });
    }


    public void initAdapter() {
        mAdapter = new DirectoryAdapter(context, this, main);
        contentLayout.getRecyclerView().verticalLayoutManager(context);
        contentLayout.getRecyclerView()
                .setAdapter(mAdapter);
        contentLayout.getSwipeRefreshLayout().setColorSchemeResources(R.color.order, R.color.order2, R.color.end);
        contentLayout.emptyView(ViewUtils.getEmptyView(this, R.mipmap.bg_ml));
        contentLayout.getRecyclerView()
                .setOnRefreshAndLoadMoreListener(new XRecyclerView.OnRefreshAndLoadMoreListener() {
                    @Override
                    public void onRefresh() {
                        mAdapter.clearData();
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


    public void mLDialog() {
        CustomDialog.build(this, R.layout.dialog_ml, new CustomDialog.OnBindView() {
            @Override
            public void onBind(final CustomDialog dialog, View v) {

                AppCompatCheckBox checkBox = v.findViewById(R.id.cb);
                ImageView close = v.findViewById(R.id.iv_close);
                checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (isChecked) {
                            SharedInfo.getInstance().saveValue(UrlKit.FIRST_ML, false);
                        }
                    }
                });
                close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.doDismiss();
                    }
                });
            }
        })
                .setFullScreen(true)
                .show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ivBanner.recycle();
    }

    public List<BannerItem> getBannerList(List<String> bannerBeans) {
        ArrayList<BannerItem> list = new ArrayList<>();
        for (int i = 0; i < bannerBeans.size(); i++) {
            BannerItem item = new BannerItem();
            item.imgUrl = bannerBeans.get(i);
            list.add(item);
        }
        return list;
    }
}
