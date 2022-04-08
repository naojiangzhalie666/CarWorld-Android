package com.liansheng.carworld.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.kongzue.dialog.v3.CustomDialog;
import com.liansheng.carworld.R;
import com.liansheng.carworld.activity.OrderDetailsActivity;
import com.liansheng.carworld.activity.logic.OtherLogic;
import com.liansheng.carworld.activity.logic.UserLogic;
import com.liansheng.carworld.adapter.home.ChildOrderAdapter;
import com.liansheng.carworld.bean.TransporterBean;
import com.liansheng.carworld.bean.other.PageBean;
import com.liansheng.carworld.kit.Constant;
import com.liansheng.carworld.net.JsonCallback;
import com.liansheng.carworld.net.NetApi;
import com.liansheng.carworld.net.UrlKit;
import com.liansheng.carworld.utils.ViewUtils;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import cn.droidlover.xdroid.tools.utils.ActivityManage;
import cn.droidlover.xdroid.tools.utils.Util;
import cn.droidlover.xrecyclerview.XRecyclerContentLayout;
import cn.droidlover.xrecyclerview.XRecyclerView;
import okhttp3.Call;


public class OrderChildListFrag extends BaseFragment {
    @BindView(R.id.contentLayout)
    XRecyclerContentLayout contentLayout;
    private ChildOrderAdapter orderAdapter;
    private String status = "";
    Unbinder unbinder;

    public static OrderChildListFrag newInstance(String type) {
        Bundle args = new Bundle();
        args.putString(Constant.KEY_TYPE, type);
        OrderChildListFrag fragment = new OrderChildListFrag();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        unbinder = ButterKnife.bind(this, rootView);
        initAdapter();
        status = getArguments().getString(Constant.KEY_TYPE);
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && UserLogic.isLogin()) {
            getOrder(status, 0);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (getUserVisibleHint() && UserLogic.isLogin()) {
            getOrder(status, 0);
        }
    }

    private void initAdapter() {
        orderAdapter = new ChildOrderAdapter(null);
        contentLayout.getRecyclerView().verticalLayoutManager(context);
        contentLayout.getRecyclerView().setAdapter(orderAdapter);
        orderAdapter.setOnItemClickListener((adapter, view, position) -> {
            TransporterBean item = (TransporterBean) adapter.getItem(position);
            Intent bundle = new Intent();
            bundle.putExtra("id", item.getId());
            ActivityManage.push(OrderDetailsActivity.class, bundle);
        });
        contentLayout.getSwipeRefreshLayout().setColorSchemeResources(R.color.order, R.color.order2, R.color.end);
        contentLayout.emptyView(ViewUtils.getEmptyView(getBaseActivity(), R.mipmap.bg_home_order));
        contentLayout.getRecyclerView()
                .setOnRefreshAndLoadMoreListener(new XRecyclerView.OnRefreshAndLoadMoreListener() {
                    @Override
                    public void onRefresh() {
                        getOrder(status, 0);
                    }

                    @Override
                    public void onLoadMore(int page) {
                        getOrder(status, page);
                    }
                });

        contentLayout.loadingView(View.inflate(getBaseActivity(), R.layout.view_loading, null));
        contentLayout.getRecyclerView().useDefLoadMoreView();
//        contentLayout.getRecyclerView().addItemDecoration(new DividerItemDecoration(getBaseActivity(), DividerItemDecoration.VERTICAL));
    }

    private void getOrder(String status, int page) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("pageIndex", page);
        map.put("pageSize", Constant.DEFULT_RESULT);
        map.put("privately", true);
        map.put("status", status);
        map.put("showAccount", true);
        NetApi.get(UrlKit.TRANSPORT_ORDER, map, new JsonCallback() {
            @Override
            public void onSuccess(String response, int id) {
                PageBean<TransporterBean> home = new Gson().fromJson(response, new TypeToken<PageBean<TransporterBean>>() {
                }.getType());
                OtherLogic.updateAdapter(page, orderAdapter, home, contentLayout);
                if (home.getItems().size() > 0) {
//                            getAdapter().clearData();
                    orderAdapter.addData(home.getItems());
                    for (int i = 0; i < home.getItems().size(); i++) {
                        TransporterBean bean = home.getItems().get(i);
                        if (bean.getStatus().equals("completion") && TextUtils.isEmpty(bean.getAssessType())) {
                            showAssessdialog(bean.getId());
                            break;
                        }
                    }
                }
            }
        });
    }

    String assess = "";

    public void showAssessdialog(String id) {
        CustomDialog.build(getBaseActivity(), R.layout.dialog_assess, new CustomDialog.OnBindView() {
            @Override
            public void onBind(final CustomDialog dialog, View v) {
                Button btnOk = v.findViewById(R.id.btn_ok);
                ImageView ivClose = v.findViewById(R.id.iv_close);
                ImageView ivGood = v.findViewById(R.id.iv_good);
                ImageView ivBad = v.findViewById(R.id.iv_bad);
                ivGood.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        assess = "good";
                        ivBad.setSelected(false);
                        ivGood.setSelected(true);
                    }
                });
                ivBad.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        assess = "bad";
                        ivBad.setSelected(true);
                        ivGood.setSelected(false);
                    }
                });
                btnOk.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        getAccess(assess, id);
                        dialog.doDismiss();
                    }
                });
                ivClose.setOnClickListener(new View.OnClickListener() {
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

    public void getAccess(String type, String id) {
        NetApi.postAssess(getBaseActivity().getLoginResult().getToken(), id, type, new JsonCallback() {
            @Override
            public void onSuccess(String response, int id) {
//                    XToastUtils.success("感谢评价！");
                Util.toast("感谢评价");
            }
        });
    }

    @Override
    public void initData(Bundle savedInstanceState) {

    }

    @Override
    public int getLayoutId() {
        return R.layout.frag_list;
    }

}
