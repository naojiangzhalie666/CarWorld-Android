package com.liansheng.carworld.activity.me;

import android.os.Bundle;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.liansheng.carworld.R;
import com.liansheng.carworld.activity.BaseActivity;
import com.liansheng.carworld.bean.MemberBean;
import com.liansheng.carworld.bean.company.SearchCompanyBean;
import com.liansheng.carworld.bean.me.CompanyInfoAdapter;
import com.liansheng.carworld.bean.other.PageBean;
import com.liansheng.carworld.kit.Constant;
import com.liansheng.carworld.net.JsonCallback;
import com.liansheng.carworld.net.NetApi;
import com.liansheng.carworld.net.UrlKit;
import com.liansheng.carworld.utils.StringUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.droidlover.xdroid.tools.utils.ActivityManage;
import cn.droidlover.xdroid.tools.utils.Util;
import cn.droidlover.xdroid.views.recyclerView.DividerLine;
import okhttp3.Call;

public class CompanyApplyAct extends BaseActivity {


    CompanyInfoAdapter infoAdapter;
    @BindView(R.id.company_list)
    RecyclerView company_list;

    private String id;

    @Override
    public void initData(Bundle savedInstanceState) {
        company_list.setLayoutManager(new LinearLayoutManager(context));
        company_list.addItemDecoration(new DividerLine(DividerLine.HORIZONTAL));
        infoAdapter = new CompanyInfoAdapter(null);
        infoAdapter.setOnItemClickListener((adapter, view, position) -> {
            MemberBean item = (MemberBean) adapter.getItem(position);
            callPhone(CompanyApplyAct.this, "联系电话：" + item.getMobile(), item.getMobile());
        });
        company_list.setAdapter(infoAdapter);
        id = getIntent().getStringExtra(Constant.KEY_ID);
        getData();
    }


    @OnClick(R.id.cancel)
    public void onViewClick(View view) {
        exitCompany();
    }

    private void exitCompany() {
        NetApi.delete(String.format(UrlKit.USER_COMPANY_MEMBER, id), null, new JsonCallback() {

            @Override
            public void onSuccess(String response, int id) {
                Util.toast("撤销成功");
                finish();
            }
        });
    }

    @Override
    public int getLayoutId() {
        return R.layout.act_company_apply;
    }


    private void getData() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("pageIndex", 0);
        map.put("pageSize", Constant.DEFULT_RESULT);
        map.put("companyId", id);
        map.put("type", "manager");
        map.put("passed", true);
        NetApi.get(UrlKit.USER_MEMBER, map, new JsonCallback() {

            @Override
            public void onSuccess(String response, int id) {
                Gson gson = new Gson();
                PageBean<MemberBean> data = gson.fromJson(response, new TypeToken<PageBean<MemberBean>>() {
                }.getType());
                infoAdapter.replaceData(data.getItems());
            }
        });
    }
}
