package com.liansheng.carworld.activity.home;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.liansheng.carworld.R;
import com.liansheng.carworld.activity.BaseActivity;
import com.liansheng.carworld.adapter.home.HotAdapter;
import com.liansheng.carworld.adapter.home.SortAllAdaper;
import com.liansheng.carworld.bean.Brand;
import com.liansheng.carworld.bean.event.EventClose;
import com.liansheng.carworld.bean.other.PageBean;
import com.liansheng.carworld.net.JsonCallback;
import com.liansheng.carworld.net.NetApi;
import com.liansheng.carworld.net.UrlKit;
import com.liansheng.carworld.utils.SortComparator;
import com.lzj.sidebar.SideBarLayout;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import cn.droidlover.xdroid.tools.utils.ActivityManage;
import cn.droidlover.xdroid.views.recyclerView.DividerLine;
import okhttp3.Call;

public class ChooseCarAllActivity extends BaseActivity implements TextWatcher {

    @BindView(R.id.btn_back)
    ImageView tvBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.search_input)
    EditText edtSearch;
    @BindView(R.id.hot_list)
    RecyclerView hotList;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.sidebar)
    SideBarLayout sidebarView;
    SortAllAdaper mSortAdaper;
    List<Brand.ItemsBean> mList;
    private int mScrollState = -1;

    @Override
    public void initData(Bundle savedInstanceState) {
        EventBus.getDefault().register(this);
        initView();
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_choose_car;
    }

    private void initView() {
        tvTitle.setText("选择品牌");
        tvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        edtSearch.addTextChangedListener(this);
        mScrollState = -1;
        getBrand();
        getHotBrand();
    }

    private void getBrand() {
        NetApi.get(UrlKit.CAR_RESOURCE_BRAND, null, new JsonCallback() {

            @Override
            public void onSuccess(String response, int id) {
                Gson gson = new Gson();
                Brand brand = gson.fromJson(response, Brand.class);
                mList = brand.getItems();
                //进行排序
                Collections.sort(mList, new SortComparator());
                mSortAdaper = new SortAllAdaper(R.layout.itemview_sort, mList, true);
                recyclerView.setLayoutManager(new LinearLayoutManager(context)); //设置LayoutManager为LinearLayoutManager
                recyclerView.setAdapter(mSortAdaper);
                recyclerView.setNestedScrollingEnabled(false);//解决滑动不流畅
                connectData();
            }
        });
    }

    private void getHotBrand() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("hot", true);
        map.put("pageIndex", 0);
        map.put("pageSize", 8);
        NetApi.get(UrlKit.CAR_RESOURCE_BRAND, map, new JsonCallback() {

            @Override
            public void onSuccess(String response, int id) {
                Gson gson = new Gson();
                Brand brand = gson.fromJson(response, Brand.class);
                hotList.setLayoutManager(new GridLayoutManager(context, 4));
                HotAdapter hotAdapter = new HotAdapter(brand.getItems());
                hotList.addItemDecoration(new DividerLine(DividerLine.VERTICAL));
                hotList.addItemDecoration(new DividerLine(DividerLine.HORIZONTAL));
                hotAdapter.setOnItemClickListener((adapter, view, position) -> {
                    Brand.ItemsBean item = (Brand.ItemsBean) adapter.getItem(position);
                    Intent intent = new Intent();
                    intent.putExtra("brand", item.getId());
                    ActivityManage.push(ChooseCarFctActivity.class, intent);
                });
                hotList.setAdapter(hotAdapter);
            }
        });
    }

    private void connectData() {
        //侧边栏滑动 --> item
        sidebarView.setSideBarLayout(new SideBarLayout.OnSideBarLayoutListener() {
            @Override
            public void onSideBarScrollUpdateItem(String word) {
                //循环判断点击的拼音导航栏和集合中姓名的首字母,如果相同recyclerView就跳转指定位置
                for (int i = 0; i < mList.size(); i++) {
                    if (mList.get(i).getLetter().equals(word)) {
                        recyclerView.smoothScrollToPosition(i);
                        break;
                    }
                }
            }
        });
        //item滑动 --> 侧边栏
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
                    //第一个可见的位置
                    RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
                    //判断是当前layoutManager是否为LinearLayoutManager
                    // 只有LinearLayoutManager才有查找第一个和最后一个可见view位置的方法
                    int firstItemPosition = 0;
                    if (layoutManager instanceof LinearLayoutManager) {
                        LinearLayoutManager linearManager = (LinearLayoutManager) layoutManager;
                        //获取第一个可见view的位置
                        firstItemPosition = linearManager.findFirstVisibleItemPosition();
                    }
                    sidebarView.OnItemScrollUpdateText(mList.get(firstItemPosition).getLetter().toUpperCase());
                    if (mScrollState == RecyclerView.SCROLL_STATE_IDLE) {
                        mScrollState = -1;
                    }
                }
            }
        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void close(EventClose eventClose) {
        finish();
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
    }

    @Override
    public void afterTextChanged(Editable s) {
        if (mList == null || mList.size() <= 0) {
            return;
        }
        String keyWord = s.toString();
        Log.i("test", "------------key=" + keyWord);
        if (!keyWord.equals("")) {
            if (matcherSearch(keyWord, mList).size() > 0) {
                sidebarView.OnItemScrollUpdateText(matcherSearch(keyWord, mList).get(0).getLetter());
            }
            mSortAdaper.setNewData(matcherSearch(keyWord, mList));
            mSortAdaper.notifyDataSetChanged();
        } else {
            sidebarView.OnItemScrollUpdateText(mList.get(0).getLetter());
            mSortAdaper.setNewData(mList);
            mSortAdaper.notifyDataSetChanged();
        }
    }

    /**
     * 匹配输入数据
     *
     * @param keyword
     * @param list
     * @return
     */
    public List<Brand.ItemsBean> matcherSearch(String keyword, List<Brand.ItemsBean> list) {
        List<Brand.ItemsBean> results = new ArrayList<>();
        String patten = Pattern.quote(keyword);
        Pattern pattern = Pattern.compile(patten, Pattern.CASE_INSENSITIVE);
        for (int i = 0; i < list.size(); i++) {
            //根据首字母
            Matcher matcherWord = pattern.matcher((list.get(i)).getLetter());
            //根据拼音
//            Matcher matcherPin = pattern.matcher((list.get(i)).getPinyin());
            //根据简拼
            Matcher matcherJianPin = pattern.matcher((list.get(i)).getLetter());
            //根据名字
            Matcher matcherName = pattern.matcher((list.get(i)).getName());
            if (matcherWord.find() || matcherName.find() || matcherJianPin.find()) {
                results.add(list.get(i));
            }
        }
        return results;
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
