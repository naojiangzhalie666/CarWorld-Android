package com.liansheng.carworld.activity.me;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.liansheng.carworld.R;
import com.liansheng.carworld.activity.BaseActivity;
import com.liansheng.carworld.bean.company.SearchCompanyBean;
import com.liansheng.carworld.bean.me.AddressBean;
import com.liansheng.carworld.bean.other.PageBean;
import com.liansheng.carworld.kit.Constant;
import com.liansheng.carworld.net.JsonCallback;
import com.liansheng.carworld.net.NetApi;
import com.liansheng.carworld.net.UrlKit;
import com.liansheng.carworld.utils.StringUtil;
import com.qmuiteam.qmui.skin.QMUISkinManager;
import com.qmuiteam.qmui.util.QMUIDisplayHelper;
import com.qmuiteam.qmui.widget.popup.QMUIPopup;
import com.qmuiteam.qmui.widget.popup.QMUIPopups;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import cn.droidlover.xdroid.tools.utils.ActivityManage;
import cn.droidlover.xdroid.tools.utils.Util;
import okhttp3.Call;

public class CompanyAuthInfoAct extends BaseActivity {

    @BindView(R.id.et_company_name)
    EditText et_company_name;
    @BindView(R.id.tv_company_name)
    TextView tv_company_name;
    @BindView(R.id.company_list)
    RecyclerView companyList;
    private SearchCompanyBean companyBean;
    private TxtAdapter txtAdapter;
    private String selectContent;

    @Override
    public void initData(Bundle savedInstanceState) {
        et_company_name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (TextUtils.isEmpty(s)) {
                    return;
                }
                if (StringUtil.isChinese(s.toString()) && s.length() > 1) {
                    if (s.toString().equals(selectContent)) {
                        return;
                    }
                    company(s.toString());
                }
            }
        });
        companyList.setLayoutManager(new LinearLayoutManager(this));
        txtAdapter = new TxtAdapter();
        companyList.setAdapter(txtAdapter);
    }

    @Override
    public int getLayoutId() {
        return R.layout.act_company_auth_info;
    }

    @OnClick({R.id.apply, R.id.upload})
    public void onViewClick(View v) {
        switch (v.getId()) {
            case R.id.apply:
                String name = et_company_name.getText().toString();
                if (TextUtils.isEmpty(name)) {
                    Util.toast("请输入公司名");
                    return;
                }
                if (companyBean == null) {
                    return;
                }
                applyCompany();
                break;
            case R.id.upload:
                ActivityManage.push(CompanyAuthAct.class, null, 1001);
                break;
        }
    }

    private void applyCompany() {
        NetApi.post(String.format(UrlKit.USER_COMPANY_MEMBER, companyBean.getId()), null, new JsonCallback() {

            @Override
            public void onSuccess(String response, int id) {
                Util.toast("申请成功");
                Intent intent = new Intent();
                intent.putExtra(Constant.KEY_ID, companyBean.getId());
                ActivityManage.push(CompanyApplyAct.class, intent);
            }
        });
    }

    private void company(String name) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("pageIndex", 0);
        map.put("pageSize", 5);
        map.put("company", name);
        NetApi.get(UrlKit.APP_RESOURCE_COMPANY, map, new JsonCallback() {

            @Override
            public void onSuccess(String response, int id) {
                PageBean<SearchCompanyBean> beans = new Gson().fromJson(response, new TypeToken<PageBean<SearchCompanyBean>>() {
                }.getType());
                if (beans != null) {
                    show(beans.getItems());
                }
            }
        });
    }

    public void show(List<SearchCompanyBean> list) {
        List<String> data = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            data.add(list.get(i).getCompany());
        }
        txtAdapter.replaceData(data);
        companyList.setVisibility(View.VISIBLE);
        txtAdapter.setOnItemClickListener((adapter, view, position) -> {
            companyBean = list.get(position);
            selectContent = companyBean.getCompany();
            et_company_name.setText(selectContent);
            et_company_name.setSelection(selectContent.length());
            tv_company_name.setText("企业管理员：" + companyBean.getIdentityCard().getOwner());
            companyList.setVisibility(View.GONE);

        });
    }

    private class TxtAdapter extends BaseQuickAdapter<String, BaseViewHolder> {
        public TxtAdapter() {
            super(R.layout.item_txt);
        }

        @Override
        protected void convert(@NotNull BaseViewHolder baseViewHolder, String s) {
            baseViewHolder.setText(R.id.txt, s);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            finish();
        }
    }
}
