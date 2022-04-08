package com.liansheng.carworld.activity.home;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.kongzue.dialog.interfaces.OnDialogButtonClickListener;
import com.kongzue.dialog.util.BaseDialog;
import com.kongzue.dialog.v3.MessageDialog;
import com.kunminx.linkage.adapter.viewholder.LinkagePrimaryViewHolder;
import com.kunminx.linkage.adapter.viewholder.LinkageSecondaryViewHolder;
import com.kunminx.linkage.bean.BaseGroupedItem;
import com.liansheng.carworld.R;
import com.liansheng.carworld.activity.BaseActivity;
import com.liansheng.carworld.activity.company.CompanyManagerAct;
import com.liansheng.carworld.activity.logic.OtherLogic;
import com.liansheng.carworld.activity.logic.UserLogic;
import com.liansheng.carworld.activity.me.InfoAuthAct;
import com.liansheng.carworld.activity.me.SignAuthAct;
import com.liansheng.carworld.adapter.CarAdapter;
import com.liansheng.carworld.adapter.DropDownAdapter;
import com.liansheng.carworld.adapter.MoreAdapter;
import com.liansheng.carworld.adapter.home.SortAllAdaper;
import com.liansheng.carworld.bean.Brand;
import com.liansheng.carworld.bean.CityBean;
import com.liansheng.carworld.bean.UserInfo;
import com.liansheng.carworld.bean.home.SaleCardBean;
import com.liansheng.carworld.bean.me.CompanyBean;
import com.liansheng.carworld.kit.Constant;
import com.liansheng.carworld.net.JsonCallback;
import com.liansheng.carworld.net.NetApi;
import com.liansheng.carworld.bean.other.PageBean;
import com.liansheng.carworld.net.UrlKit;
import com.liansheng.carworld.utils.CustomGroupedItem;
import com.liansheng.carworld.utils.CustomLinkagePrimaryAdapterConfig;
import com.liansheng.carworld.utils.CustomLinkageSecondaryAdapterConfig;
import com.liansheng.carworld.utils.GetJsonDataUtil;
import com.liansheng.carworld.utils.SortComparator;
import com.liansheng.carworld.utils.ViewUtils;
import com.liansheng.carworld.view.PassDateCarDialog;
import com.lzj.sidebar.SideBarLayout;
import com.xuexiang.xui.widget.spinner.DropDownMenu;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.droidlover.xdroid.hold.info.SharedInfo;
import cn.droidlover.xdroid.tools.utils.ActivityManage;
import cn.droidlover.xdroid.tools.utils.Util;
import cn.droidlover.xdroid.views.recyclerView.DividerLine;
import cn.droidlover.xdroid.views.textView.NoDoubleClickButton;
import cn.droidlover.xrecyclerview.XRecyclerContentLayout;
import cn.droidlover.xrecyclerview.XRecyclerView;
import okhttp3.Call;

public class SaleCarListAct extends BaseActivity implements CustomLinkageSecondaryAdapterConfig.OnSecondaryItemClickListener, CustomLinkagePrimaryAdapterConfig.OnPrimaryItemClickListener {

    @BindView(R.id.contentLayout)
    XRecyclerContentLayout contentLayout;
    @BindView(R.id.btn_back)
    ImageView btnBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.btn_function)
    TextView tvFunction;
    @BindView(R.id.tv_city)
    TextView tvCity;
    @BindView(R.id.et_search)
    EditText etSearch;
    @BindView(R.id.home_item_1)
    TextView home1;
    @BindView(R.id.home_item_2)
    TextView home2;
    @BindView(R.id.home_item_3)
    TextView home3;
    @BindView(R.id.home_item_4)
    TextView home4;
    @BindView(R.id.ddm_content)
    DropDownMenu mDropDownMenu;
    @BindView(R.id.main)
    RelativeLayout main;
    private CarAdapter mAdapter;
    private SortAllAdaper mSortAdaper;
    private DropDownAdapter mPxAdapter;
    private DropDownAdapter mJgAdapter;
    private MoreAdapter mMoreAdapter;
    private List<Brand.ItemsBean> mList;
    private int mScrollState = -1;
    private List<View> mPopupViews = new ArrayList<>();
    private String[] headers = {"最新上架", "价格", "品牌", "车龄"};
    private String[] pxs = {"最新上架", "价格最低", "里程最少", "车龄最短"};
    private String[] pxs_k = {"PostDate", "LessSellPrice", "LessMileage", "LessYear"};
    private String[] jgs = {"不限", "0-5万", "5-10万", "10-15万", "15-20万", "20万以上"};
    private String[] jgs_k = {"", "ZeroToFive", "FiveToTen", "TenToFifteen", "FifteenToTwenty", "OverTwenty"};
    private String[] cls = {"不限", "1年以内", "1-3年", "3-5年", "5-8年", "8-10年", "10年以上"};
    private String[] cls_k = {"", "WithinOne", "OneToThree", "ThreeToFive", "FiveToEight", "EightToTen", "OverTen"};
//    private String[] jbs = {"不限", "轿车", "越野车", "MPV", "面包车", "皮卡车", "跑车"};

    private String sort = "";
    private String price = "";
    private String car_brand = "";
    private String mCity = "";
    private String car_years = "";
    //    private String jb = "";
    private OptionsPickerView pvOptions;
    private boolean newCar = false;
    private String negotiatedPrice = "";

    private boolean flag = false;//是否关键字搜索

    private PassDateCarDialog carDialog;

    @Override
    public void initData(Bundle savedInstanceState) {
        tvTitle.setText("二手车商交易");
        tvFunction.setText("我的车库");
        if (UserLogic.isCarShop()) {
            tvFunction.setVisibility(View.VISIBLE);
        } else {
            tvFunction.setVisibility(View.GONE);
        }
        tvFunction.setOnClickListener(v -> {
//            ActivityManage.push(CarPushAct.class);
            ActivityManage.push(CompanyManagerAct.class);
        });
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        initAdapter();
        initView();
//        mCity = SharedInfo.getInstance().getValue(Constant.KEY_CITY, "").toString();
        tvCity.setText("全国");
        new GetJsonDataUtil(new GetJsonDataUtil.SelectListener() {
            @Override
            public void success(ArrayList<CityBean> cityBeans, ArrayList<ArrayList<String>> citys) {
                pvOptions = new OptionsPickerBuilder(context, new OnOptionsSelectListener() {
                    @Override
                    public void onOptionsSelect(int options1, int options2, int options3, View v) {
                        //返回的分别是三个级别的选中位置
                        mCity = cityBeans.get(options1).getCity_list().get(options2);
                        tvCity.setText(mCity);
                        getData(0);
                    }
                })
                        .setDividerColor(ContextCompat.getColor(context, R.color.line_grey))
                        .setTextColorCenter(ContextCompat.getColor(context, R.color.main_normal_color)) //设置选中项文字颜色
                        .setContentTextSize(16)
                        .build();
                pvOptions.setPicker(cityBeans, citys);//二级选择器（市区）
                pvOptions.setSelectOptions(10, 5);
            }
        });
        if (UserLogic.isCarShop())
            getPassData();
        getData(0);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_car;
    }

    @OnClick({R.id.btn_mc})
    public void onViewClick(View view) {
        switch (view.getId()) {
            case R.id.btn_mc:
                if (UserLogic.isCarShop()) {
                    SharedInfo.getInstance().saveValue("car_type", "sale");
                    if (view.getId() == R.id.btn_mc) {
                        ActivityManage.push(ChooseCarAllActivity.class);
                    } else {
                        ActivityManage.push(FindCarListActivity.class);
                    }
                } else {
                    MessageDialog.build(SaleCarListAct.this)
                            .setMessage("您还未进行汽贸店或4S店认证，请先认证")
                            .setOkButton("去认证", new OnDialogButtonClickListener() {
                                @Override
                                public boolean onClick(BaseDialog baseDialog, View v) {
                                    ActivityManage.push(InfoAuthAct.class);
                                    return false;
                                }
                            })
                            .setOkButtonDrawable(R.drawable.shape_button_red_bg)
                            .show();
                }
                break;
        }
    }

    private void initView() {
        mScrollState = -1;
        NetApi.get(UrlKit.CAR_RESOURCE_BRAND, null, new JsonCallback() {

            @Override
            public void onSuccess(String response, int id) {
                Gson gson = new Gson();
                Brand brand = gson.fromJson(response, Brand.class);
                mList = brand.getItems();
                //进行排序
                Collections.sort(mList, new SortComparator());
                mSortAdaper = new SortAllAdaper(R.layout.itemview_sort, mList, false);
                connectData();
            }
        });
    }

    private void getPassData() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("pageIndex", 0);
        map.put("pageSize", 10);
        map.put("off", false);
        map.put("expired", true);
        NetApi.get(UrlKit.CAR_RESOURCE, map, new JsonCallback() {

            @Override
            public void onSuccess(String response, int id) {
                PageBean<SaleCardBean> data = new Gson().fromJson(response, new TypeToken<PageBean<SaleCardBean>>() {
                }.getType());
                if (data.getItems() != null && data.getItems().size() > 0) {
                    carDialog.updateData(data.getItems());
                    carDialog.show();
                }
            }
        });
    }

    @OnClick({R.id.tv_city, R.id.tv_search_btn, R.id.home_item_1, R.id.home_item_2, R.id.home_item_3, R.id.home_item_4})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_city:
                if (!pvOptions.isShowing()) {
                    pvOptions.show();
                }
                break;
            case R.id.tv_search_btn:
                flag = true;
                getData(0);
                break;
            case R.id.home_item_1:
                initBg();
                home1.setBackground(ContextCompat.getDrawable(context, R.drawable.shape_button_red_10));
                home1.setTextColor(ContextCompat.getColor(context, R.color.order));
                newCar = false;
                negotiatedPrice = "";
                getData(0);
                break;
            case R.id.home_item_2:
                initBg();
                home2.setBackground(ContextCompat.getDrawable(context, R.drawable.shape_button_red_10));
                home2.setTextColor(ContextCompat.getColor(context, R.color.order));
                newCar = false;
                negotiatedPrice = "可议价";
                getData(0);
                break;
            case R.id.home_item_3:
                initBg();
                home3.setBackground(ContextCompat.getDrawable(context, R.drawable.shape_button_red_10));
                home3.setTextColor(ContextCompat.getColor(context, R.color.order));
                newCar = false;
                negotiatedPrice = "底价";
                getData(0);
                break;
            case R.id.home_item_4:
                initBg();
                home4.setBackground(ContextCompat.getDrawable(context, R.drawable.shape_button_red_10));
                home4.setTextColor(ContextCompat.getColor(context, R.color.order));
                newCar = true;
                negotiatedPrice = "";
                getData(0);
                break;
        }
    }

    private void initBg() {
        Drawable drawable = ContextCompat.getDrawable(context, R.drawable.shape_white_rect_10);
        home1.setBackground(drawable);
        home2.setBackground(drawable);
        home3.setBackground(drawable);
        home4.setBackground(drawable);
        int color = ContextCompat.getColor(context, R.color.main_normal_color);
        home1.setTextColor(color);
        home2.setTextColor(color);
        home3.setTextColor(color);
        home4.setTextColor(color);
    }

    private void connectData() {
        final ListView pxView = new ListView(this);
        mPxAdapter = new DropDownAdapter(this, pxs);
        pxView.setDividerHeight(0);
        pxView.setAdapter(mPxAdapter);

        final ListView jgView = new ListView(this);
        mJgAdapter = new DropDownAdapter(this, jgs);
        jgView.setDividerHeight(0);
        jgView.setAdapter(mJgAdapter);

        final View moreView = getLayoutInflater().inflate(R.layout.layout_more, null);
        GridView clView = moreView.findViewById(R.id.grid_cl);
        mMoreAdapter = new MoreAdapter(this, cls);
        clView.setAdapter(mMoreAdapter);
        clView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                car_years = cls_k[position];
                mMoreAdapter.setSelectPosition(position);
                mDropDownMenu.setTabMenuText(cls[position]);
                mDropDownMenu.closeMenu();
                flag = false;
                getData(0);
            }
        });
        final View brandView = getLayoutInflater().inflate(R.layout.layout_car, null);
        RecyclerView recyclerView = brandView.findViewById(R.id.recyclerView);
        SideBarLayout sidebarView = brandView.findViewById(R.id.sidebar);
        recyclerView.setLayoutManager(new LinearLayoutManager(SaleCarListAct.this));
        recyclerView.setAdapter(mSortAdaper);
        recyclerView.setNestedScrollingEnabled(false);//解决滑动不流畅
        mSortAdaper.setOnItemClickListener((adapter, view, position) -> {
            car_brand = mList.get(position).getName();
            mDropDownMenu.setTabMenuText(car_brand);
            mDropDownMenu.closeMenu();
            flag = false;
            getData(0);
        });
        pxView.setOnItemClickListener((parent, view, position, id) -> {
            sort = pxs_k[position];
            mPxAdapter.setSelectPosition(position);
            mDropDownMenu.setTabMenuText(pxs[position]);
            mDropDownMenu.closeMenu();
            flag = false;
            getData(0);
        });
        jgView.setOnItemClickListener((parent, view, position, id) -> {
            price = jgs_k[position];
            mJgAdapter.setSelectPosition(position);
            mDropDownMenu.setTabMenuText(jgs[position]);
            mDropDownMenu.closeMenu();
            flag = false;
            getData(0);
        });
        sidebarView.setSideBarLayout(new SideBarLayout.OnSideBarLayoutListener() {
            @Override
            public void onSideBarScrollUpdateItem(String word) {
                for (int i = 0; i < mList.size(); i++) {
                    if (mList.get(i).getLetter().toUpperCase().equals(word)) {
                        recyclerView.smoothScrollToPosition(i);
                        break;
                    }
                }
            }
        });
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int scrollState) {
                super.onScrollStateChanged(recyclerView, scrollState);
                mScrollState = scrollState;
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (mScrollState != -1) {
                    RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
                    int firstItemPosition = 0;
                    if (layoutManager instanceof LinearLayoutManager) {
                        LinearLayoutManager linearManager = (LinearLayoutManager) layoutManager;
                        firstItemPosition = linearManager.findFirstVisibleItemPosition();
                    }
                    sidebarView.OnItemScrollUpdateText(mList.get(firstItemPosition).getLetter().toUpperCase());
                    if (mScrollState == RecyclerView.SCROLL_STATE_IDLE) {
                        mScrollState = -1;
                    }
                }
            }
        });
        mPopupViews.add(pxView);
        mPopupViews.add(jgView);
        mPopupViews.add(brandView);
        mPopupViews.add(moreView);
        TextView contentView = new TextView(this);
        contentView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0));
        contentView.setGravity(Gravity.CENTER);
        contentView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
        mDropDownMenu.setDropDownMenu(headers, mPopupViews, contentView);
    }


    private void getData(int page) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("pageIndex", page);
        map.put("pageSize", Constant.DEFULT_RESULT);
        map.put("common", true);
        map.put("off", false);
        if (!TextUtils.isEmpty(mCity)) {
            map.put("location", mCity.replace("市", ""));
        }
        if (flag) {
            String content = etSearch.getText().toString();
            if (TextUtils.isEmpty(content)) {
                Util.toast("请输入搜索关键字");
                return;
            }
            map.put("keywords", content);
        } else {
            if (!TextUtils.isEmpty(car_brand)) {
                map.put("brand", car_brand);
            }
            if (!TextUtils.isEmpty(price)) {
                map.put("price", price);
            }
            if (!TextUtils.isEmpty(car_years)) {
                map.put("year", car_years);
            }
            if (!TextUtils.isEmpty(sort)) {
                map.put("sort", sort);
            }
            if (!TextUtils.isEmpty(negotiatedPrice)) {
                map.put("negotiatedPrice", negotiatedPrice);
            }
            if (newCar) {
                map.put("newCar", true);
            }
        }
        NetApi.get(UrlKit.CAR_RESOURCE, map, new JsonCallback() {

            @Override
            public void onSuccess(String response, int id) {
                    PageBean<SaleCardBean> data = new Gson().fromJson(response, new TypeToken<PageBean<SaleCardBean>>() {
                    }.getType());
                    OtherLogic.updateAdapter(page, mAdapter, data, contentLayout);
            }
        });
    }

    public void initAdapter() {
        mAdapter = new CarAdapter();
        XRecyclerView recyclerView = contentLayout.getRecyclerView();
        recyclerView.addItemDecoration(new DividerLine(LinearLayoutManager.HORIZONTAL));
        setLayoutManager(recyclerView);
        recyclerView.setAdapter(mAdapter);
        contentLayout.getSwipeRefreshLayout().setColorSchemeResources(R.color.order, R.color.order2, R.color.end);
        contentLayout.emptyView(ViewUtils.getEmptyView(this, R.mipmap.bg_cy));
        recyclerView.setOnRefreshAndLoadMoreListener(new XRecyclerView.OnRefreshAndLoadMoreListener() {
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
        recyclerView.useDefLoadMoreView();
        mAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull BaseQuickAdapter adapter, @NonNull View view, int position) {
                SaleCardBean item = (SaleCardBean) adapter.getItem(position);
                Intent intent = new Intent();
                intent.putExtra(Constant.KEY_BEAN, item);
                ActivityManage.push(SaleCarDetailsAct.class, intent);
            }
        });
        carDialog = new PassDateCarDialog(context, (view, carId, content) -> {
            if ("sale".equals(content)) {
                NetApi.put(UrlKit.CAR_RESOURCE_SOLD + carId, "", new JsonCallback() {

                    @Override
                    public void onSuccess(String response, int id) {
                        Util.toast("操作成功");
                        getData(0);
                        carDialog.updateList(carId);
                    }
                });
            } else {
                NetApi.put(UrlKit.CAR_RESOURCE_UPDATE_DATE + carId, "", new JsonCallback() {

                    @Override
                    public void onSuccess(String response, int id) {
                        Util.toast("刷新成功");
                        getData(0);
                        carDialog.updateList(carId);
                    }
                });
            }
        });
    }

    public void setLayoutManager(XRecyclerView recyclerView) {
        recyclerView.verticalLayoutManager(context);
    }

    @Override
    public void onSecondaryItemClick(LinkageSecondaryViewHolder holder, ViewGroup view, BaseGroupedItem<CustomGroupedItem.ItemInfo> item) {
        mCity = item.info.getTitle();
        mDropDownMenu.setTabMenuText(item.info.getTitle());
        mDropDownMenu.closeMenu();
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    @Override
    public void onPrimaryItemClick(LinkagePrimaryViewHolder holder, View view, String title) {

    }
}
