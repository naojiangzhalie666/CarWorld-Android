package com.liansheng.carworld.activity.home;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.liansheng.carworld.R;
import com.liansheng.carworld.activity.BaseActivity;
import com.liansheng.carworld.adapter.SortAdaper;
import com.liansheng.carworld.bean.Brand;
import com.liansheng.carworld.kit.Constant;
import com.liansheng.carworld.net.JsonCallback;
import com.liansheng.carworld.net.NetApi;
import com.liansheng.carworld.net.UrlKit;
import com.liansheng.carworld.utils.SortComparator;
import com.lzj.sidebar.SideBarLayout;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.OnClick;
import cn.droidlover.xdroid.tools.utils.ActivityManage;
import cn.droidlover.xdroid.tools.utils.Util;
import okhttp3.Call;

/**
 * 4s名录
 */
public class ChooseCarActivity extends BaseActivity implements TextWatcher {

    //    @BindView(R.id.topbar)
//    QMUITopBar topbar;
    @BindView(R.id.back)
    ImageView tvBack;
    @BindView(R.id.search_input)
    EditText edtSearch;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.sidebar)
    SideBarLayout sidebarView;
    @BindView(R.id.et_zdy)
    EditText etZdy;
    SortAdaper mSortAdaper;
    List<Brand.ItemsBean> mList;
    private int mScrollState = -1;
    @BindView(R.id.tv_title)
    TextView tv_title;
    private int type = 1;//1 下一页 2 返回

    @Override
    public void initData(Bundle savedInstanceState) {
        initView();
        findViewById(R.id.btn_back).setOnClickListener(v -> finish());
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_choose_car;
    }

    private void initView() {
        type = getIntent().getIntExtra(Constant.KEY_TYPE, 1);
        if (type == 2) {
            findViewById(R.id.ll_zdy).setVisibility(View.VISIBLE);
            findViewById(R.id.hot_txt).setVisibility(View.GONE);
            tv_title.setText("选择品牌");
        } else {
            tv_title.setText("4S名录");
        }
        tvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
//        ImmersionBar.with(this).transparentStatusBar().fitsSystemWindows(false).statusBarDarkFont(false).init();
        edtSearch.addTextChangedListener(this);
        mScrollState = -1;
        NetApi.get(UrlKit.BRAND, null, new JsonCallback() {

            @Override
            public void onSuccess(String response, int id) {
                Gson gson = new Gson();
                Brand brand = gson.fromJson(response, Brand.class);
                mList = brand.getItems();
                //进行排序
                Collections.sort(mList, new SortComparator());
                mSortAdaper = new SortAdaper(mList, type);
                mSortAdaper.setOnItemClickListener((adapter, view, position) -> {
                    Brand.ItemsBean item = (Brand.ItemsBean) adapter.getItem(position);
                    if (type == 1) {
                        Intent intent = new Intent();
                        intent.putExtra("brand", item.getName());
                        ActivityManage.push(DirectoryActivity.class, intent);
                    } else {
                        Intent intent = new Intent();
                        intent.putExtra("value", item.getName());
                        setResult(Activity.RESULT_OK, intent);
                        finish();
                    }
                });
                recyclerView.setLayoutManager(new LinearLayoutManager(context)); //设置LayoutManager为LinearLayoutManager
                recyclerView.setAdapter(mSortAdaper);
                recyclerView.setNestedScrollingEnabled(false);//解决滑动不流畅
                connectData();
            }
        });
    }

    @OnClick(R.id.confirm)
    public void onViewClick() {
        String content = etZdy.getText().toString().trim();
        if (TextUtils.isEmpty(content)) {
            Util.toast("请输入品牌");
            return;
        }
        Intent intent = new Intent();
        intent.putExtra("value", content);
        setResult(Activity.RESULT_OK, intent);
        finish();
    }

    private void connectData() {
        //侧边栏滑动 --> item
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
                sidebarView.OnItemScrollUpdateText(matcherSearch(keyWord, mList).get(0).getWord());
            }
            mSortAdaper.setNewData(matcherSearch(keyWord, mList));
            mSortAdaper.notifyDataSetChanged();
        } else {
            sidebarView.OnItemScrollUpdateText(mList.get(0).getWord());
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
            Matcher matcherWord = pattern.matcher((list.get(i)).getWord());
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
}
