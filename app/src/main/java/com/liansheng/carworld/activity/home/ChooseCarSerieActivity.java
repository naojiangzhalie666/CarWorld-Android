package com.liansheng.carworld.activity.home;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.liansheng.carworld.R;
import com.liansheng.carworld.activity.BaseActivity;
import com.liansheng.carworld.adapter.home.SortSerieAdaper;
import com.liansheng.carworld.bean.Brand;
import com.liansheng.carworld.bean.BrandSerie;
import com.liansheng.carworld.bean.CheckBean;
import com.liansheng.carworld.bean.event.EventClose;
import com.liansheng.carworld.kit.Constant;
import com.liansheng.carworld.net.JsonCallback;
import com.liansheng.carworld.net.NetApi;
import com.liansheng.carworld.bean.other.PageBean;
import com.liansheng.carworld.net.UrlKit;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import cn.droidlover.xdroid.hold.info.SharedInfo;
import cn.droidlover.xdroid.tools.utils.ActivityManage;
import okhttp3.Call;

public class ChooseCarSerieActivity extends BaseActivity {

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.type_1)
    TextView type1;
    @BindView(R.id.type_2)
    TextView type2;
    @BindView(R.id.pl_list)
    RecyclerView pl_list;
    @BindView(R.id.btn_back)
    ImageView btnBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    SortSerieAdaper mSortAdaper;
    PLAdapter plAdapter;
    List<BrandSerie> selectList = new ArrayList<>();
    private boolean typeCheck1;
    private boolean typeCheck2;
    private String sortType = "";

    @Override
    public void initData(Bundle savedInstanceState) {
        EventBus.getDefault().register(this);
        initView();
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_choose_serie;
    }

    private void initView() {
        tvTitle.setText("选择款式");
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mSortAdaper = new SortSerieAdaper();
        mSortAdaper.setOnItemClickListener((adapter, view, position) -> {
            BrandSerie item = (BrandSerie) adapter.getItem(position);
            Intent intent = new Intent();
            intent.putExtra(Constant.KEY_BEAN, item);
            String value = SharedInfo.getInstance().getValue("car_type", "sale").toString();
            if ("sale".equals(value)) {
                intent.putExtra(Constant.KEY_TYPE, false);
                ActivityManage.push(SaleCarActivity.class, intent);
            } else if ("inter".equals(value)) {
                intent.putExtra(Constant.KEY_TYPE, true);
                ActivityManage.push(SaleCarActivity.class, intent);
            } else {
                ActivityManage.push(FindCarActivity.class, intent);
            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(context)); //设置LayoutManager为LinearLayoutManager
        recyclerView.setAdapter(mSortAdaper);
        recyclerView.setNestedScrollingEnabled(false);//解决滑动不流畅
        plAdapter = new PLAdapter(null);
        pl_list.setLayoutManager(new GridLayoutManager(context, 3));
        plAdapter.setOnItemClickListener((adapter, view, position) -> {
            CheckBean item = (CheckBean) adapter.getItem(position);
            if (item.isCheck()) {
                item.setCheck(!item.isCheck());
                adapter.notifyItemChanged(position);
            } else {
                for (int i = 0; i < adapter.getData().size(); i++) {
                    CheckBean bean = (CheckBean) adapter.getData().get(i);
                    bean.setCheck(false);
                }
                item.setCheck(!item.isCheck());
                adapter.notifyDataSetChanged();
            }
            reSort();
        });
        pl_list.setAdapter(plAdapter);
        getData();
    }

    @OnClick({R.id.type_1, R.id.type_2})
    public void onViewClick(View view) {
        setInit();
        switch (view.getId()) {
            case R.id.type_1:
                if (typeCheck1) {
                    sortType = "";
                    typeCheck1 = false;
                    reSort();
                } else {
                    sortType = "1";
                    reSort();
                    typeCheck1 = true;
                    typeCheck2 = false;
                    type1.setTextColor(ContextCompat.getColor(context, R.color.order));
                    type1.setBackground(ContextCompat.getDrawable(context, R.drawable.shape_button_red_10));
                }
                break;
            case R.id.type_2:
                if (typeCheck2) {
                    sortType = "";
                    reSort();
                    typeCheck2 = false;
                } else {
                    sortType = "2";
                    reSort();
                    typeCheck2 = true;
                    typeCheck1 = false;
                    type2.setTextColor(ContextCompat.getColor(context, R.color.order));
                    type2.setBackground(ContextCompat.getDrawable(context, R.drawable.shape_button_red_10));
                }
                break;
        }
    }

    private void reSort() {
        selectList.clear();
        PageBean<BrandSerie> json = new Gson().fromJson(SharedInfo.getInstance().getValue("temp", "").toString(), new TypeToken<PageBean<BrandSerie>>() {
        }.getType());
        List<CheckBean> datas = plAdapter.getData();
        String content = "";
        for (int i = 0; i < datas.size(); i++) {
            if (datas.get(i).isCheck()) {
                content = datas.get(i).getContent();
            }
        }
        if (TextUtils.isEmpty(content) && TextUtils.isEmpty(sortType)) {
            mSortAdaper.setNewData(json.getItems());
            return;
        }
        for (int i = 0; i < json.getItems().size(); i++) {
            BrandSerie bean = json.getItems().get(i);
            if (TextUtils.isEmpty(content)) {
                if (TextUtils.isEmpty(sortType) || sortType.equals(bean.getGear_type())) {
                    selectList.add(bean);
                }
            } else {
                if (String.valueOf(bean.getLiter()).equals(content)) {
                    if (TextUtils.isEmpty(sortType) || sortType.equals(bean.getGear_type())) {
                        selectList.add(bean);
                    }
                }
            }
        }
        mSortAdaper.replaceData(selectList);
    }

    private void setInit() {
        type1.setTextColor(ContextCompat.getColor(context, R.color.main_normal_color));
        type1.setBackground(ContextCompat.getDrawable(context, R.drawable.shape_button_gray));
        type2.setTextColor(ContextCompat.getColor(context, R.color.main_normal_color));
        type2.setBackground(ContextCompat.getDrawable(context, R.drawable.shape_button_gray));
    }

    private void getData() {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("fctId", getIntent().getStringExtra("brand"));
        NetApi.get(UrlKit.CAR_RESOURCE_SERIE, hashMap, new JsonCallback() {

            @Override
            public void onSuccess(String response, int id) {
                PageBean<BrandSerie> brand = new Gson().fromJson(response, new TypeToken<PageBean<BrandSerie>>() {
                }.getType());
                SharedInfo.getInstance().saveValue("temp", response);
                List<BrandSerie> mList = brand.getItems();
                mSortAdaper.replaceData(mList);
                List<String> plList = new ArrayList<>();
                for (int i = 0; i < mList.size(); i++) {
                    if (plList.contains(String.valueOf(mList.get(i).getLiter()))) {
                        continue;
                    }
                    plList.add(String.valueOf(mList.get(i).getLiter()));
                }
                Collections.sort(plList);
                List<CheckBean> beans = new ArrayList<>();
                for (int i = 0; i < plList.size(); i++) {
                    beans.add(new CheckBean(plList.get(i), false));
                }
                plAdapter.replaceData(beans);
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

    private class PLAdapter extends BaseQuickAdapter<CheckBean, BaseViewHolder> {

        public PLAdapter(List<CheckBean> beans) {
            super(R.layout.item_pl_txt, beans);
        }

        @Override
        protected void convert(@NonNull BaseViewHolder baseViewHolder, CheckBean bean) {
            TextView view = baseViewHolder.getView(R.id.item_txt);
            view.setText(bean.getContent());
            if (bean.isCheck()) {
                view.setBackground((ContextCompat.getDrawable(context, R.drawable.shape_button_red_10)));
                view.setTextColor(ContextCompat.getColor(context, R.color.order));
            } else {
                view.setBackground((ContextCompat.getDrawable(context, R.drawable.shape_button_gray)));
                view.setTextColor(ContextCompat.getColor(context, R.color.main_normal_color));
            }
        }
    }
}
