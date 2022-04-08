package com.liansheng.carworld.activity.me;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.liansheng.carworld.R;
import com.liansheng.carworld.activity.BaseActivity;
import com.liansheng.carworld.activity.home.AuthDispatchAct;
import com.liansheng.carworld.bean.me.CompanyBean;
import com.liansheng.carworld.bean.me.LicensesBean;
import com.liansheng.carworld.bean.other.PageBean;
import com.liansheng.carworld.kit.Constant;
import com.liansheng.carworld.net.JsonCallback;
import com.liansheng.carworld.net.NetApi;
import com.liansheng.carworld.net.UrlKit;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.droidlover.xdroid.tools.utils.ActivityManage;
import cn.droidlover.xdroid.tools.utils.Util;
import okhttp3.Call;

public class SignAuthAct extends BaseActivity {

    @BindView(R.id.sign_auth_ck1)
    Switch signAuthCk1;
    @BindView(R.id.sign_auth_ck2)
    Switch signAuthCk2;
    @BindView(R.id.sign_auth_ck3)
    Switch signAuthCk3;
    @BindView(R.id.sign_auth_ck4)
    Switch signAuthCk4;
    @BindView(R.id.sign_auth_ck5)
    Switch signAuthCk5;
    private ArrayList<CompanyBean> companyBeans;
    private ArrayList<LicensesBean> licensesBeans;

    @Override
    public void initData(Bundle savedInstanceState) {
        initView();
    }

    @Override
    public int getLayoutId() {
        return R.layout.act_sign_auth;
    }


    @Override
    protected void onResume() {
        super.onResume();
        getCompanyInfo();
        getIDInfo();
    }

    private void getIDInfo() {
        NetApi.get(UrlKit.USER_LICENSE, null, new JsonCallback() {

            @Override
            public void onSuccess(String response, int id) {
                PageBean<LicensesBean> pageBean = new Gson().fromJson(response, new TypeToken<PageBean<LicensesBean>>() {
                }.getType());
                if (pageBean.getItems() != null && pageBean.getItems().size() > 0) {
                    licensesBeans = (ArrayList<LicensesBean>) pageBean.getItems();
                    int j = 0;
                    for (int i = 0; i < pageBean.getItems().size(); i++) {
                        LicensesBean bean = pageBean.getItems().get(i);
                        if (Constant.AUTH_IDENTITY_CARD.equals(bean.getType()) || Constant.AUTH_COMPULSORY_INSURANCE.equals(bean.getType()) || Constant.AUTH_DRIVING_LICENSE.equals(bean.getType())) {
                            j++;
                        }
                    }
                    if (j == 3) {
                        findViewById(R.id.sign_auth_tv3).setVisibility(View.VISIBLE);
                    } else {
                        findViewById(R.id.sign_auth_tv3).setVisibility(View.GONE);
                    }
                } else {
                    findViewById(R.id.sign_auth_tv3).setVisibility(View.GONE);
                }
            }
        });
    }

    private void getCompanyInfo() {
        NetApi.get(UrlKit.USER_COMPANY, null, new JsonCallback() {

            @Override
            public void onSuccess(String response, int id) {
                signAuthCk1.setVisibility(View.VISIBLE);
                signAuthCk2.setVisibility(View.VISIBLE);
                findViewById(R.id.sign_auth_tv).setVisibility(View.GONE);
                findViewById(R.id.sign_auth_tv4).setVisibility(View.GONE);
                PageBean<CompanyBean> pageBean = new Gson().fromJson(response, new TypeToken<PageBean<CompanyBean>>() {
                }.getType());
                if (pageBean.getItems() != null && pageBean.getItems().size() > 0) {
                    companyBeans = (ArrayList<CompanyBean>) pageBean.getItems();
                    for (int i = 0; i < pageBean.getItems().size(); i++) {
                        CompanyBean bean = pageBean.getItems().get(i);
                        if (Constant.STORE_TYPE_1.equals(bean.getType())) {
                            if (bean.isSecondHandPriority()) {
                                signAuthCk1.setVisibility(View.GONE);
                                findViewById(R.id.sign_auth_tv).setVisibility(View.VISIBLE);
                                findViewById(R.id.sign_auth_txt1).setVisibility(View.GONE);
                            } else {
                                signAuthCk2.setVisibility(View.GONE);
                                findViewById(R.id.sign_auth_txt2).setVisibility(View.GONE);
                                findViewById(R.id.sign_auth_tv).setVisibility(View.VISIBLE);
                            }
                        } else if (Constant.STORE_TYPE_2.equals(bean.getType())) {
                            findViewById(R.id.sign_auth_tv4).setVisibility(View.VISIBLE);
                        }
                    }
                } else {
                    signAuthCk1.setVisibility(View.VISIBLE);
                    signAuthCk2.setVisibility(View.VISIBLE);
                    findViewById(R.id.sign_auth_tv).setVisibility(View.GONE);
                    findViewById(R.id.sign_auth_tv4).setVisibility(View.GONE);
                }
            }
        });
    }

    private void initView() {
        signAuthCk1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                setUnCheck();
                if (isChecked) {
                    signAuthCk1.setChecked(true);
                }
            }
        });
        signAuthCk2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                setUnCheck();
                if (isChecked) {
                    signAuthCk2.setChecked(true);
                }
            }
        });
        signAuthCk3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                setUnCheck();
                if (isChecked) {
                    signAuthCk3.setChecked(true);
                }
            }
        });
        signAuthCk4.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                setUnCheck();
                if (isChecked) {
                    signAuthCk4.setChecked(true);
                }
            }
        });
        signAuthCk5.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                setUnCheck();
                if (isChecked) {
                    signAuthCk5.setChecked(true);
                }
            }
        });
    }

    private void setUnCheck() {
        signAuthCk1.setChecked(false);
        signAuthCk2.setChecked(false);
        signAuthCk3.setChecked(false);
        signAuthCk4.setChecked(false);
        signAuthCk5.setChecked(false);
    }

    @OnClick(R.id.submit)
    public void onClick() {
        Intent intent = new Intent();
        if (signAuthCk1.isChecked()) {
            intent.putExtra(Constant.KEY_TYPE, "1");
            ActivityManage.push(AuthQMAct.class, intent);
        } else if (signAuthCk2.isChecked()) {
            intent.putExtra(Constant.KEY_TYPE, "2");
            ActivityManage.push(AuthQMAct.class, intent);
        } else if (signAuthCk3.isChecked()) {
            ActivityManage.push(AuthActivity.class,intent);
        } else if (signAuthCk4.isChecked()) {
            ActivityManage.push(Auth4SAct.class,intent);
        } else if (signAuthCk5.isChecked()) {
            ActivityManage.push(AuthDispatchAct.class);
        } else {
            Util.toast("请选择要认证的类别");
        }
    }
}
