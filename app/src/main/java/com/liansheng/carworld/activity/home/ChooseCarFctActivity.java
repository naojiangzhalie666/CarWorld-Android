package com.liansheng.carworld.activity.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.liansheng.carworld.R;
import com.liansheng.carworld.activity.BaseActivity;
import com.liansheng.carworld.adapter.home.SortFctAdaper;
import com.liansheng.carworld.bean.BrandFct;
import com.liansheng.carworld.bean.event.EventClose;
import com.liansheng.carworld.net.JsonCallback;
import com.liansheng.carworld.net.NetApi;
import com.liansheng.carworld.bean.other.PageBean;
import com.liansheng.carworld.net.UrlKit;
import com.liansheng.carworld.utils.SortFctComparator;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import cn.droidlover.xdroid.tools.utils.ActivityManage;
import okhttp3.Call;

public class ChooseCarFctActivity extends BaseActivity {

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.btn_back)
    ImageView btnBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    SortFctAdaper mSortAdaper;
    List<BrandFct> mList;
    private String brand;

    @Override
    public void initData(Bundle savedInstanceState) {
        EventBus.getDefault().register(this);
        brand = getIntent().getStringExtra("brand");
        initView();
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_choose_fct;
    }


    private void initView() {
        tvTitle.setText("选择车型");
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mSortAdaper = new SortFctAdaper();
        mSortAdaper.setOnItemClickListener((adapter, view, position) -> {
            BrandFct item = (BrandFct) adapter.getItem(position);
            Intent bundle = new Intent();
            bundle.putExtra("brand", item.getId());
            ActivityManage.push(ChooseCarSerieActivity.class, bundle);
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(this)); //设置LayoutManager为LinearLayoutManager
        recyclerView.setAdapter(mSortAdaper);
        recyclerView.setNestedScrollingEnabled(false);//解决滑动不流畅
        getData();
    }

    private void getData() {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("brandId", brand);
        NetApi.get(UrlKit.CAR_RESOURCE_FCT, hashMap, new JsonCallback() {

            @Override
            public void onSuccess(String response, int id) {
                Gson gson = new Gson();
                PageBean<BrandFct> brand = gson.fromJson(response, new TypeToken<PageBean<BrandFct>>() {
                }.getType());
                mList = brand.getItems();
                if (mList.get(0).getFctname().contains("进口")) {
                    Collections.reverse(mList);
                }
                outList(brand.getItems());
                mSortAdaper.setNewData(newList);
            }
        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void close(EventClose eventClose) {
        finish();
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    List<BrandFct> newList = new ArrayList<>();
    List<BrandFct> jkList = new ArrayList<>();

    private List<BrandFct> reSort(List<BrandFct> mList) {
//        int index = -1;
        for (int i = 0; i < mList.size(); i++) {
//            index++;
            BrandFct brandFct = mList.get(i);
            if (brandFct.getFctname().contains("进口")) {
                jkList.add(brandFct);
                mList.remove(brandFct);
//                index = index - 1;
            }
        }
        if (jkList.size() > 0) {
            jkList.get(0).setShow(true);
        }
        return mList;
    }

    private void outList(List<BrandFct> mList) {
        if (mList == null || mList.size() == 0) {
            return;
        }
//        int index = -1;
        String sortStr = mList.get(0).getFctname();
        List<BrandFct> tempList = new ArrayList<>();
        for (int i = 0; i < mList.size(); i++) {
//            index++;
            BrandFct brandFct = mList.get(i);
            if (sortStr.equals(brandFct.getFctname())) {
                tempList.add(brandFct);
                mList.remove(brandFct);
//                index = index - 1;
            }
        }
        if (tempList.size() > 0) {
            tempList.get(0).setShow(true);
        }
        newList.addAll(tempList);
        outList(mList);
    }

}
