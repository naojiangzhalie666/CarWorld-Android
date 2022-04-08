package com.liansheng.carworld.activity;

import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.liansheng.carworld.R;
import com.liansheng.carworld.adapter.InviteAdapter;
import com.liansheng.carworld.bean.UserInfo;
import com.liansheng.carworld.bean.me.InviteBean;
import com.liansheng.carworld.bean.other.PageBean;
import com.liansheng.carworld.net.JsonCallback;
import com.liansheng.carworld.net.NetApi;
import com.liansheng.carworld.net.UrlKit;
import com.liansheng.carworld.utils.ViewUtils;
import com.liansheng.carworld.wxapi.WxUtils;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.OnClick;
import cn.droidlover.xdroid.hold.info.SharedInfo;
import cn.droidlover.xdroid.views.recyclerView.DividerLine;
import cn.droidlover.xrecyclerview.XRecyclerContentLayout;
import cn.droidlover.xrecyclerview.XRecyclerView;
import okhttp3.Call;

public class InviteListActivity extends BaseActivity {

    @BindView(R.id.contentLayout)
    XRecyclerContentLayout contentLayout;
    @BindView(R.id.btn_back)
    ImageView btnBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.btn_invite)
    Button btnInvite;
    private InviteAdapter mAdapter;

    @Override
    public void initData(Bundle savedInstanceState) {
        initAdapter();
        initView();
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_invite_list;
    }

    private void initView() {
        tvTitle.setText("邀请好友");
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        getData(0);
    }

    private void getData(int page) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("type", "Follow");
        NetApi.get(UrlKit.USER_INVITE_LIST, map, new JsonCallback() {

            @Override
            public void onSuccess(String response, int id) {
                Gson gson = new Gson();
                PageBean<InviteBean> data = gson.fromJson(response, new TypeToken<PageBean<InviteBean>>() {
                }.getType());
                if (page == 0) {
                    mAdapter.getData().clear();
                }
                if (data.getItems().size() > 0) {
//                            getAdapter().clearData();
                    mAdapter.addData(data.getItems());
                } else {
                    if (page == 0) {
                        contentLayout.showEmpty();
                        return;
                    }
                }
                if (data.getItems().size() < DEFULT_RESULT) {
                    MAX_PAGE = page;
                } else {
                    MAX_PAGE++;
                }
                contentLayout.getRecyclerView().setPage(page, MAX_PAGE);
            }
        });
    }

    public void initAdapter() {
        mAdapter = new InviteAdapter(null);
        setLayoutManager(contentLayout.getRecyclerView());
        contentLayout.getRecyclerView().setAdapter(mAdapter);
        contentLayout.getRecyclerView().addItemDecoration(new DividerLine(DividerLine.HORIZONTAL));
        contentLayout.getSwipeRefreshLayout().setColorSchemeResources(R.color.order, R.color.order2, R.color.end);
        contentLayout.emptyView(ViewUtils.getEmptyView(this, R.mipmap.bg_bill_none));
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

        contentLayout.loadingView(View.inflate(this, R.layout.view_loading, null));
        contentLayout.getRecyclerView().useDefLoadMoreView();
    }


    public void setLayoutManager(XRecyclerView recyclerView) {
        recyclerView.verticalLayoutManager(context);
    }

    @OnClick(R.id.btn_invite)
    public void onClick() {
        WxUtils.getInstance().wxWeb(SharedInfo.getInstance().getEntity(UserInfo.class).getInvitationLink(), "车无界", "邀请您注册车无界");
    }
}
