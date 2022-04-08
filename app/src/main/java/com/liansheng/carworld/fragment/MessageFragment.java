package com.liansheng.carworld.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.liansheng.carworld.R;
import com.liansheng.carworld.activity.OrderDetailsActivity;
import com.liansheng.carworld.activity.company.ApplyMembersAct;
import com.liansheng.carworld.activity.home.SteamDetailsAct;
import com.liansheng.carworld.activity.logic.OtherLogic;
import com.liansheng.carworld.activity.logic.UserLogic;
import com.liansheng.carworld.activity.user.LoginYzmActivity;
import com.liansheng.carworld.activity.MainActivity;
import com.liansheng.carworld.adapter.MessageAdapter;
import com.liansheng.carworld.bean.UserMessage;
import com.liansheng.carworld.bean.other.PageBean;
import com.liansheng.carworld.kit.Constant;
import com.liansheng.carworld.net.JsonCallback;
import com.liansheng.carworld.net.NetApi;
import com.liansheng.carworld.net.UrlKit;
import com.liansheng.carworld.utils.ViewUtils;
import com.liansheng.carworld.view.pop.AddPop;
import com.qmuiteam.qmui.widget.QMUITopBar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import cn.droidlover.xdroid.base.SimpleRecAdapter;
import cn.droidlover.xdroid.hold.info.SharedInfo;
import cn.droidlover.xdroid.tools.utils.ActivityManage;
import cn.droidlover.xdroid.tools.utils.Util;
import cn.droidlover.xdroid.views.appbar.ToolBar;
import cn.droidlover.xrecyclerview.XRecyclerContentLayout;
import cn.droidlover.xrecyclerview.XRecyclerView;
import okhttp3.Call;


public class MessageFragment extends BaseFragment {
    @BindView(R.id.topbar)
    QMUITopBar topbar;
    @BindView(R.id.recycler_view)
    XRecyclerContentLayout contentLayout;
    private MessageAdapter messageAdapter;
    Unbinder unbinder;
    AddPop mDialog;
    private String companyId = "";

    @Override
    public void initData(Bundle savedInstanceState) {
    }

    @Override
    public int getLayoutId() {
        return R.layout.frag_message;
    }

    public static MessageFragment newInstance() {
        Bundle args = new Bundle();
        MessageFragment fragment = new MessageFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        unbinder = ButterKnife.bind(this, rootView);
        topbar.setTitle("消息");
        initAdapter();
        mDialog = new AddPop(getActivity(), 2, (view, content) -> {
            if (view.getId() == R.id.cancel) {
                invitation(false);
            } else if (view.getId() == R.id.submit) {
                invitation(true);
            }
        });
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser & UserLogic.isLogin()) {
            getMsg(0);
        }
    }

    private void initAdapter() {
        messageAdapter = new MessageAdapter(null);
        contentLayout.getRecyclerView().verticalLayoutManager(context);
        contentLayout.getRecyclerView().setAdapter(messageAdapter);
        contentLayout.getSwipeRefreshLayout().setColorSchemeResources(R.color.order, R.color.order2, R.color.end);
        contentLayout.getSwipeRefreshLayout().setColorSchemeResources(R.color.order, R.color.order2, R.color.end);
        contentLayout.emptyView(ViewUtils.getEmptyView(getBaseActivity(), R.mipmap.bg_message_list));
        contentLayout.getRecyclerView()
                .setOnRefreshAndLoadMoreListener(new XRecyclerView.OnRefreshAndLoadMoreListener() {
                    @Override
                    public void onRefresh() {
                        getMsg(0);
                    }

                    @Override
                    public void onLoadMore(int page) {
                        getMsg(page);
                    }
                });

        contentLayout.loadingView(View.inflate(getBaseActivity(), R.layout.view_loading, null));
        contentLayout.getRecyclerView().useDefLoadMoreView();
        messageAdapter.setOnItemClickListener((adapter, view, position) -> {
            UserMessage item = (UserMessage) adapter.getItem(position);
            List<UserMessage.ValuesBean> values = item.getValues();
            if (values == null || values.size() == 0) return;
            if ("TransportOrder".equals(item.getType())) {
                Intent bundle = new Intent();
                for (int i = 0; i < values.size(); i++) {
                    if ("orderId".equals(values.get(i).getKey())) {
                        bundle.putExtra("id", values.get(i).getValue());
                        break;
                    }
                }
                ActivityManage.push(OrderDetailsActivity.class, bundle);
            } else if ("Invitation".equals(item.getType())) {
                for (int i = 0; i < values.size(); i++) {
                    if ("company".equals(values.get(i).getKey())) {
                        mDialog.setTitle(values.get(i).getValue());
                        mDialog.setContent("邀请您加入本公司");
                        mDialog.showAtLocation(topbar, Gravity.CENTER, 0, 0);
                    } else if ("id".equals(values.get(i).getKey())) {
                        companyId = values.get(i).getValue();
                    }
                }
            } else if ("PlatformGuarantee".equals(item.getType())) {
                Intent bundle = new Intent();
                for (int i = 0; i < values.size(); i++) {
                    if ("platformGuaranteeId".equals(values.get(i).getKey())) {
                        bundle.putExtra(Constant.KEY_ID, values.get(i).getValue());
                        break;
                    }
                }
                ActivityManage.push(SteamDetailsAct.class, bundle);
            } else if ("CallPhone".equals(item.getType())) {
                for (int i = 0; i < values.size(); i++) {
                    if ("mobile".equals(values.get(i).getKey())) {
                        String phone = values.get(i).getValue();
                        OtherLogic.callPhone((AppCompatActivity) getActivity(), "联系电话：" + phone, phone);
                        break;
                    }
                }
            } else if ("JoinCompany".equals(item.getType())) {
                ActivityManage.push(ApplyMembersAct.class);
            }
        });
    }

    private void invitation(boolean flag) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("id", companyId);
        map.put("yes", flag);
        NetApi.put(UrlKit.USER_INVITATION, map, new JsonCallback() {

            @Override
            public void onSuccess(String response, int id) {
                if (flag) {
                    Util.toast("加入成功");
                    ((MainActivity) getActivity()).getUserInfo();
                } else {
                    Util.toast("已拒绝");
                }
            }
        });

    }

    public void getMsg(int page) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("pageIndex", page);
        map.put("pageSize", Constant.DEFULT_RESULT);
        NetApi.get(UrlKit.MESSAGE, map, new JsonCallback() {

            @Override
            public void onSuccess(String response, int id) {
//                MainActivity mainActivity = (MainActivity) getActivity();
//                mainActivity.clearBadge();
                PageBean<UserMessage> message = new Gson().fromJson(response, new TypeToken<PageBean<UserMessage>>() {
                }.getType());
                OtherLogic.updateAdapter(page, messageAdapter, message, contentLayout);
            }
        });
    }

}