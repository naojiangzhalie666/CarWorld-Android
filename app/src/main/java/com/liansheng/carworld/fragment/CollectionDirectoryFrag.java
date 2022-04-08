package com.liansheng.carworld.fragment;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.google.gson.Gson;
import com.kunminx.linkage.LinkageRecyclerView;
import com.kunminx.linkage.adapter.viewholder.LinkagePrimaryViewHolder;
import com.kunminx.linkage.adapter.viewholder.LinkageSecondaryViewHolder;
import com.kunminx.linkage.bean.BaseGroupedItem;
import com.liansheng.carworld.R;
import com.liansheng.carworld.activity.me.CollectionActivity;
import com.liansheng.carworld.adapter.me.CollectionAdapter;
import com.liansheng.carworld.adapter.SortCarAdaper;
import com.liansheng.carworld.bean.Brand;
import com.liansheng.carworld.bean.Directory;
import com.liansheng.carworld.bean.UserInfo;
import com.liansheng.carworld.kit.Constant;
import com.liansheng.carworld.net.JsonCallback;
import com.liansheng.carworld.net.NetApi;
import com.liansheng.carworld.net.UrlKit;
import com.liansheng.carworld.utils.CityUtil;
import com.liansheng.carworld.utils.CustomGroupedItem;
import com.liansheng.carworld.utils.CustomLinkagePrimaryAdapterConfig;
import com.liansheng.carworld.utils.CustomLinkageSecondaryAdapterConfig;
import com.liansheng.carworld.utils.SortComparator;
import com.liansheng.carworld.utils.Utils;
import com.liansheng.carworld.utils.ViewUtils;
import com.liansheng.carworld.wxapi.WxUtils;
import com.lzj.sidebar.SideBarLayout;
import com.xuexiang.xui.widget.spinner.DropDownMenu;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import cn.droidlover.xdroid.hold.info.SharedInfo;
import cn.droidlover.xdroid.tools.utils.Util;
import cn.droidlover.xrecyclerview.XRecyclerContentLayout;
import cn.droidlover.xrecyclerview.XRecyclerView;
import okhttp3.Call;

public class CollectionDirectoryFrag extends BaseFragment {

    @BindView(R.id.contentLayout)
    XRecyclerContentLayout contentLayout;
    @BindView(R.id.ddm_content)
    DropDownMenu mDropDownMenu;
    @BindView(R.id.main)
    LinearLayout main;
    private CollectionAdapter mAdapter;
    private String city = "";
    private String brand = "";
    List<View> mPopupViews = new ArrayList<>();
    String[] headers = {"品牌", "城市"};
    private SortCarAdaper mSortAdaper;
    private List<Brand.ItemsBean> mList;

    public static CollectionDirectoryFrag newInstance() {
        Bundle args = new Bundle();
        CollectionDirectoryFrag fragment = new CollectionDirectoryFrag();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        initView();
        initAdapter();
        getCarSort();
        getData(0);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_collection_4s;
    }

    private void getCarSort() {
        NetApi.get(UrlKit.COLLECTED_BRAND, null, new JsonCallback() {

            @Override
            public void onSuccess(String response, int id) {
                Gson gson = new Gson();
                Brand brand = gson.fromJson(response, Brand.class);
                mList = brand.getItems();
                //进行排序
                Collections.sort(mList, new SortComparator());
                mSortAdaper = new SortCarAdaper(R.layout.itemview_sort, mList, context);
                initHeader();
            }
        });
    }

    private void initView() {
        mAdapter = new CollectionAdapter(null);
        mAdapter.addChildClickViewIds(R.id.collection_cancel, R.id.collection_share, R.id.ll_phone);
        mAdapter.setOnItemChildClickListener((adapter, view, position) -> {
            Directory.ItemsBean item = (Directory.ItemsBean) adapter.getItem(position);
            switch (view.getId()) {
                case R.id.collection_cancel:
                    delCollection(item.getId());
                    break;
                case R.id.collection_share:
                    String desc = "二网手机：" + item.getMobile() + " " + item.getName() + "\n" + "4s座机号：" + item.getPhone();
                    WxUtils.getInstance().wxWeb(String.format(UrlKit.H5_SHARE_4S, item.getId(), SharedInfo.getInstance().getEntity(UserInfo.class).getId()), item.getCompany(), desc);
                    break;
                case R.id.ll_phone:
                    Utils.callPhone((AppCompatActivity) getActivity(), "请问是否要联系", item);
                    break;
            }
        });
    }

    private void delCollection(String id) {
        NetApi.delCollection(SharedInfo.getInstance().getValue(Constant.KEY_TOKEN, "").toString(), id, new JsonCallback() {

            @Override
            public void onSuccess(String response, int id) {
                refresh();
                Util.toast("已取消收藏");
                ((CollectionActivity) getActivity()).getUserInfo();
            }
        });
    }


    private void initHeader() {
        final View brandView = getLayoutInflater().inflate(R.layout.layout_car, null);
        RecyclerView recyclerView = brandView.findViewById(R.id.recyclerView);
        SideBarLayout sidebarView = brandView.findViewById(R.id.sidebar);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(mSortAdaper);
        recyclerView.setNestedScrollingEnabled(false);//解决滑动不流畅
        mSortAdaper.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull BaseQuickAdapter adapter, @NonNull View view, int position) {
                brand = mList.get(position).getName();
                mDropDownMenu.setTabMenuText(brand);
                mDropDownMenu.closeMenu();
                getData(0);
            }
        });
        sidebarView.setSideBarLayout(new SideBarLayout.OnSideBarLayoutListener() {
            @Override
            public void onSideBarScrollUpdateItem(String word) {
                //循环判断点击的拼音导航栏和集合中姓名的首字母,如果相同recyclerView就跳转指定位置
                for (int i = 0; i < mList.size(); i++) {
                    if (mList.get(i).getLetter().toUpperCase().equals(word)) {
                        recyclerView.smoothScrollToPosition(i);
                        break;
                    }
                }
            }
        });
        final View cityView = getLayoutInflater().inflate(R.layout.layout_city, null);
        LinkageRecyclerView linkage = cityView.findViewById(R.id.linkage);
        linkage.init(CityUtil.getGroupItems(), new CustomLinkagePrimaryAdapterConfig(new CustomLinkagePrimaryAdapterConfig.OnPrimaryItemClickListener() {
            @Override
            public void onPrimaryItemClick(LinkagePrimaryViewHolder holder, View view, String title) {
//                city="";
//                if (title.equals(province)) {
//                    mDropDownMenu.setTabMenuText(title);
//                    mDropDownMenu.closeMenu();
//                    mAdapter.clearData();
//                    getData(0);
//                } else {
//                    province = title;
//                }
            }
        }), new CustomLinkageSecondaryAdapterConfig(new CustomLinkageSecondaryAdapterConfig.OnSecondaryItemClickListener() {
            @Override
            public void onSecondaryItemClick(LinkageSecondaryViewHolder holder, ViewGroup view, BaseGroupedItem<CustomGroupedItem.ItemInfo> item) {
                city = item.info.getTitle();
                mDropDownMenu.setTabMenuText(item.info.getTitle());
                mDropDownMenu.closeMenu();
                getData(0);
            }
        }));
        linkage.setScrollSmoothly(true);
        mPopupViews.add(brandView);
        mPopupViews.add(cityView);
        TextView contentView = new TextView(getActivity());
        contentView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0));
        contentView.setGravity(Gravity.CENTER);
        contentView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
        mDropDownMenu.setDropDownMenu(headers, mPopupViews, contentView);
    }

    public void getData(int page) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("pageIndex", page);
        map.put("pageSize", Constant.DEFULT_RESULT);
        if (!TextUtils.isEmpty(city)) {
            map.put("city", city);
        }
        if (!TextUtils.isEmpty(brand)) {
            map.put("brand", brand);
        }
        NetApi.get(UrlKit.COLLECTION, map, new JsonCallback() {

            @Override
            public void onSuccess(String response, int id) {
                Gson gson = new Gson();
                Directory data = gson.fromJson(response, Directory.class);
                if (page == 0) {
                    mAdapter.getData().clear();
                }
                if (data.getItems().size() > 0) {
                    mAdapter.addData(data.getItems());
                } else {
                    if (page == 0) {
                        contentLayout.showEmpty();
                        return;
                    }
                }
                if (data.getItems().size() < Constant.DEFULT_RESULT) {
                    MAX_PAGE = page;
                } else {
                    MAX_PAGE++;
                }
                contentLayout.getRecyclerView().setPage(page, MAX_PAGE);
            }
        });
    }

    public void initAdapter() {
        contentLayout.getRecyclerView().verticalLayoutManager(context);
        contentLayout.getRecyclerView()
                .setAdapter(mAdapter);
        contentLayout.getSwipeRefreshLayout().setColorSchemeResources(R.color.order, R.color.order2, R.color.end);
        contentLayout.emptyView(ViewUtils.getEmptyView(getActivity(), R.mipmap.bg_sc));
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

        contentLayout.loadingView(View.inflate(getActivity(), R.layout.view_loading, null));
        contentLayout.getRecyclerView().useDefLoadMoreView();
    }

    public void refresh() {
        getData(0);
    }
}
