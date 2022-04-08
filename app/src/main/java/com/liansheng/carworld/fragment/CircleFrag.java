package com.liansheng.carworld.fragment;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.gif.GifDrawable;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.kongzue.dialog.interfaces.OnDialogButtonClickListener;
import com.kongzue.dialog.util.BaseDialog;
import com.kongzue.dialog.v3.MessageDialog;
import com.liansheng.carworld.R;
import com.liansheng.carworld.activity.circle.MessageAct;
import com.liansheng.carworld.activity.logic.OtherLogic;
import com.liansheng.carworld.activity.logic.UserLogic;
import com.liansheng.carworld.adapter.home.CircleAdapter;
import com.liansheng.carworld.bean.event.RefreshEvent;
import com.liansheng.carworld.bean.home.CircleBean;
import com.liansheng.carworld.bean.other.PageBean;
import com.liansheng.carworld.net.JsonCallback;
import com.liansheng.carworld.net.NetApi;
import com.liansheng.carworld.net.UrlKit;
import com.liansheng.carworld.utils.GetJsonDataUtil;
import com.liansheng.carworld.utils.ViewUtils;
import com.liansheng.carworld.view.pop.ReleasePop;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.droidlover.xdroid.tools.utils.ActivityManage;
import cn.droidlover.xdroid.tools.utils.Util;
import cn.droidlover.xdroid.views.appbar.TitleBar;
import cn.droidlover.xdroid.views.appbar.ToolBar;
import cn.droidlover.xdroid.views.recyclerView.DividerLine;
import cn.droidlover.xrecyclerview.XRecyclerContentLayout;
import cn.droidlover.xrecyclerview.XRecyclerView;


public class CircleFrag extends BaseFragment {

    @BindView(R.id.recycler_view)
    XRecyclerContentLayout contentLayout;
    @BindView(R.id.tv_city)
    TextView tvCity;
    @BindView(R.id.toolbar)
    ToolBar toolBar;
    @BindView(R.id.btn_mc)
    ImageView btn_mc;
    @BindView(R.id.et_search)
    EditText etSearch;
    private CircleAdapter messageAdapter;
    ReleasePop releasePop;
    private OptionsPickerView pvOptions;
    private String mCity = "";

    @Override
    public void initData(Bundle savedInstanceState) {
        toolBar.setLeftListener(null);
        toolBar.addAction(new TitleBar.ImageAction(R.mipmap.ic_circle_msg) {
            @Override
            public void performAction(View view) {
                ActivityManage.push(MessageAct.class);
            }
        });
    }

    @Override
    public int getLayoutId() {
        return R.layout.frag_circle;
    }

    public static CircleFrag newInstance() {
        Bundle args = new Bundle();
        CircleFrag fragment = new CircleFrag();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        ButterKnife.bind(this, rootView);
        EventBus.getDefault().register(this);
        initView();
        initAdapter();
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    private void initView() {
        new GetJsonDataUtil((cityBeans, citys) -> {
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
        });
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser & UserLogic.isLogin()) {
            getData(0);
        }
    }

    @Subscribe
    public void onDataEvent(RefreshEvent event) {
        getData(0);
    }

    @OnClick({R.id.tv_search_btn, R.id.tv_city})
    public void onViewClicker(View view) {
        switch (view.getId()) {
            case R.id.tv_city:
                if (pvOptions != null && !pvOptions.isShowing()) {
                    pvOptions.show();
                }
                break;
            case R.id.tv_search_btn:
                getData(0);
                break;
        }
    }

    private void initAdapter() {
        releasePop = new ReleasePop(context);
        Glide.with(ActivityManage.peek()).load(R.drawable.ic_cricle_send).into(new SimpleTarget<Drawable>() {
            @Override
            public void onResourceReady(Drawable drawable, Transition<? super Drawable> transition) {
                if (drawable instanceof GifDrawable) {
                    GifDrawable gifDrawable = (GifDrawable) drawable;
//                    gifDrawable.setLoopCount(1);
                    btn_mc.setImageDrawable(drawable);
                    gifDrawable.start();
                }
            }
        });
        btn_mc.setOnClickListener(view -> {
            if (!UserLogic.isRealAuth()) {
                return;
            }
            releasePop.showAtLocation(btn_mc, Gravity.CENTER, 0, 0);
        });
        messageAdapter = new CircleAdapter(1);
        contentLayout.getRecyclerView().verticalLayoutManager(context);
        contentLayout.getRecyclerView().addItemDecoration(new DividerLine(DividerLine.HORIZONTAL));
        contentLayout.getRecyclerView().setAdapter(messageAdapter);
        contentLayout.getSwipeRefreshLayout().setColorSchemeResources(R.color.order, R.color.order2, R.color.end);
        contentLayout.getSwipeRefreshLayout().setColorSchemeResources(R.color.order, R.color.order2, R.color.end);
        contentLayout.emptyView(ViewUtils.getEmptyView(getBaseActivity(), R.mipmap.bg_bill_none));
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

        messageAdapter.addChildClickViewIds(R.id.item_circle_delete);
        messageAdapter.setOnItemChildClickListener((adapter1, view, position) -> {
            MessageDialog.build((AppCompatActivity) context)
                    .setMessage("是否删除该发布？")
                    .setCancelButton("取消", new OnDialogButtonClickListener() {
                        @Override
                        public boolean onClick(BaseDialog baseDialog, View v) {
                            return false;
                        }
                    })
                    .setOkButton("确定", new OnDialogButtonClickListener() {
                        @Override
                        public boolean onClick(BaseDialog baseDialog, View v) {
                            CircleBean item = (CircleBean) adapter1.getItem(position);
                            NetApi.delete(UrlKit.USER_CAR_MOMENT + "/" + item.getId(), null, new JsonCallback() {
                                @Override
                                public void onSuccess(String data, int id) {
                                    messageAdapter.remove(position);
                                }
                            });
                            return false;
                        }
                    })
                    .show();
        });
        contentLayout.loadingView(View.inflate(getBaseActivity(), R.layout.view_loading, null));
        contentLayout.getRecyclerView().useDefLoadMoreView();
    }

    public void getData(int page) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("pageIndex", page);
        map.put("pageSize", 10);
        String content = etSearch.getText().toString();
        if (!TextUtils.isEmpty(content)) {
            map.put("keyword", content);
        } else {
            map.put("city", mCity);
        }
        map.put("common", true);
        NetApi.get(UrlKit.USER_CAR_MOMENT, map, new JsonCallback() {

            @Override
            public void onSuccess(String response, int id) {
                PageBean<CircleBean> message = new Gson().fromJson(response, new TypeToken<PageBean<CircleBean>>() {
                }.getType());
                OtherLogic.updateAdapter(page, 10, messageAdapter, message, contentLayout);
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}