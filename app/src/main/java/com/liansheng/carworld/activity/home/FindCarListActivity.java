package com.liansheng.carworld.activity.home;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
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
import androidx.recyclerview.widget.DividerItemDecoration;
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
import com.liansheng.carworld.activity.logic.OtherLogic;
import com.liansheng.carworld.adapter.FindCarAdapter;
import com.liansheng.carworld.adapter.DropDownAdapter;
import com.liansheng.carworld.adapter.MoreAdapter;
import com.liansheng.carworld.adapter.home.SortAllAdaper;
import com.liansheng.carworld.bean.Brand;
import com.liansheng.carworld.bean.CityBean;
import com.liansheng.carworld.bean.home.FindCardBean;
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
import cn.droidlover.xrecyclerview.XRecyclerContentLayout;
import cn.droidlover.xrecyclerview.XRecyclerView;
import okhttp3.Call;
import pub.devrel.easypermissions.EasyPermissions;

@Deprecated
public class FindCarListActivity extends BaseActivity implements CustomLinkageSecondaryAdapterConfig.OnSecondaryItemClickListener, CustomLinkagePrimaryAdapterConfig.OnPrimaryItemClickListener {

    @BindView(R.id.contentLayout)
    XRecyclerContentLayout contentLayout;
    @BindView(R.id.btn_back)
    ImageView btnBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_city)
    TextView tvCity;
    @BindView(R.id.et_search)
    EditText etSearch;
    @BindView(R.id.ddm_content)
    DropDownMenu mDropDownMenu;
    @BindView(R.id.btn_function)
    TextView btnFunction;
    @BindView(R.id.main)
    RelativeLayout main;
    private FindCarAdapter mAdapter;
    private SortAllAdaper mSortAdaper;
    private DropDownAdapter mPxAdapter;
    private DropDownAdapter mJgAdapter;
    private MoreAdapter mMoreAdapter;
    private MoreAdapter mMoreAdapter2;
    private List<Brand.ItemsBean> mList;
    private int mScrollState = -1;
    private List<View> mPopupViews = new ArrayList<>();

    private String[] headers = {"品牌", "公里数", "价格", "车龄"};
    private String[] jgs = {"不限", "0-5万", "5-10万", "10-15万", "15-20万", "20万以上"};
    private String[] jgs_k = {"", "ZeroToFive", "FiveToTen", "TenToFifteen", "FifteenToTwenty", "OverTwenty"};
    private String[] gls = {"不限", "1万以内", "1-3万", "3-5万", "5-8万", "8-10万", "10万以上"};
    //    private String[] gls_k = {"", "WithinOne", "OneToThree", "ThreeToFive", "FiveToEight", "EightToTen", "OverTen"};
    private String[] cls = {"不限", "1年以内", "1-3年", "3-5年", "5-8年", "8-10年", "10年以上"};
//    private String[] cls_k = {"", "WithinOne", "OneToThree", "ThreeToFive", "FiveToEight", "EightToTen", "OverTen"};

    private String mileage = "";
    private String price = "";
    private String car_brand = "";
    private String city = "";
    private String car_years = "";
    private OptionsPickerView pvOptions;
    private boolean flag = false;

    @Override
    public void initData(Bundle savedInstanceState) {
        initAdapter();
        initView();
        String locationCity = SharedInfo.getInstance().getValue(Constant.KEY_CITY, "全国").toString();
        tvCity.setText(locationCity);
        GetJsonDataUtil dataUtil = new GetJsonDataUtil(new GetJsonDataUtil.SelectListener() {
            @Override
            public void success(ArrayList<CityBean> cityBeans, ArrayList<ArrayList<String>> citys) {
                pvOptions = new OptionsPickerBuilder(context, new OnOptionsSelectListener() {
                    @Override
                    public void onOptionsSelect(int options1, int options2, int options3, View v) {
                        //返回的分别是三个级别的选中位置
                        city = cityBeans.get(options1).getCity_list().get(options2);
                        tvCity.setText(city);
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
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_find;
    }

    private void initView() {
        tvTitle.setText("求购");
        btnFunction.setText("发布求购");
        btnFunction.setVisibility(View.VISIBLE);
        btnFunction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedInfo.getInstance().saveValue("car_type", "search");
                ActivityManage.push(ChooseCarAllActivity.class);
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
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

    @Override
    protected void onResume() {
        super.onResume();
        getData(0);
    }

    @OnClick({R.id.tv_city, R.id.tv_search_btn})
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
        }
    }

    private void connectData() {
        final ListView glView = new ListView(this);
        mPxAdapter = new DropDownAdapter(this, gls);
        glView.setDividerHeight(0);
        glView.setAdapter(mPxAdapter);

        final ListView jgView = new ListView(this);
        mJgAdapter = new DropDownAdapter(this, jgs);
        jgView.setDividerHeight(0);
        jgView.setAdapter(mJgAdapter);

        final View moreView = getLayoutInflater().inflate(R.layout.layout_more, null);
        GridView clView = moreView.findViewById(R.id.grid_cl);
        mMoreAdapter = new MoreAdapter(this, cls);
        clView.setAdapter(mMoreAdapter);
//        GridView jbView = moreView.findViewById(R.id.grid_jb);
//        mMoreAdapter2 = new MoreAdapter(this, jbs);
//        jbView.setAdapter(mMoreAdapter2);
        clView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                car_years = position == 0 ? "" : cls[position];
                mMoreAdapter.setSelectPosition(position);
                mDropDownMenu.setTabMenuText(cls[position]);
                mDropDownMenu.closeMenu();
                flag = false;
                getData(0);
            }
        });
//        jbView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                jb = jbs[position];
//                mMoreAdapter2.setSelectPosition(position);
//            }
//        });
//        moreView.findViewById(R.id.btn_ok).setOnClickListener(v -> {
//            mDropDownMenu.closeMenu();
//        });

        final View brandView = getLayoutInflater().inflate(R.layout.layout_car, null);
        RecyclerView recyclerView = brandView.findViewById(R.id.recyclerView);
        SideBarLayout sidebarView = brandView.findViewById(R.id.sidebar);
        recyclerView.setLayoutManager(new LinearLayoutManager(FindCarListActivity.this));
        recyclerView.setAdapter(mSortAdaper);
        recyclerView.setNestedScrollingEnabled(false);//解决滑动不流畅
        mSortAdaper.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull BaseQuickAdapter adapter, @NonNull View view, int position) {
                car_brand = mList.get(position).getName();
                mDropDownMenu.setTabMenuText(car_brand);
                mDropDownMenu.closeMenu();
                flag = false;
                getData(0);
            }
        });
        glView.setOnItemClickListener((parent, view, position, id) -> {
            mileage = position == 0 ? "" : gls[position];
            mPxAdapter.setSelectPosition(position);
            mDropDownMenu.setTabMenuText(gls[position]);
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
        mPopupViews.add(brandView);
        mPopupViews.add(glView);
        mPopupViews.add(jgView);
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
        if (!TextUtils.isEmpty(city)) {
            map.put("area", city);
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
            if (!TextUtils.isEmpty(mileage)) {
                map.put("mileage", mileage);
            }
        }
        NetApi.get(UrlKit.CAR_RESOURCE_BRAND_ORDER, map, new JsonCallback() {

            @Override
            public void onSuccess(String response, int id) {
                PageBean<FindCardBean> data = new Gson().fromJson(response, new TypeToken<PageBean<FindCardBean>>() {
                }.getType());
                OtherLogic.updateAdapter(page,mAdapter,data,contentLayout);
            }
        });
    }

    public void initAdapter() {
        mAdapter = new FindCarAdapter(null);
        setLayoutManager(contentLayout.getRecyclerView());
        contentLayout.getRecyclerView()
                .setAdapter(mAdapter);
        contentLayout.getSwipeRefreshLayout().setColorSchemeResources(R.color.order, R.color.order2, R.color.end);
        contentLayout.emptyView(ViewUtils.getEmptyView(this, R.mipmap.bg_cy));
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
        mAdapter.addChildClickViewIds(R.id.iv_bj);
        mAdapter.setOnItemChildClickListener((adapter, view, position) -> {
            FindCardBean item = (FindCardBean) adapter.getItem(position);
            OtherLogic.callPhone(FindCarListActivity.this, "联系电话",
                    "买家电话：" + item.getMobile() + "\n联系客服 " + Constant.SERVICE_PHONE, item.getMobile());
        });
        mAdapter.setOnItemClickListener((adapter, view, position) -> {
            FindCardBean item = (FindCardBean) adapter.getItem(position);
            Intent intent = new Intent();
            intent.putExtra(Constant.KEY_ID, item.getId());
            ActivityManage.push(FindCarDetailsAct.class, intent);
        });
        contentLayout.loadingView(View.inflate(this, R.layout.view_loading, null));
        contentLayout.getRecyclerView().addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        contentLayout.getRecyclerView().useDefLoadMoreView();
    }

    public void setLayoutManager(XRecyclerView recyclerView) {
        recyclerView.verticalLayoutManager(context);
    }


    @Override
    public void onSecondaryItemClick(LinkageSecondaryViewHolder holder, ViewGroup view, BaseGroupedItem<CustomGroupedItem.ItemInfo> item) {
        city = item.info.getTitle();
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
