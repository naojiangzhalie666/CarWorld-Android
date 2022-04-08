package com.liansheng.carworld.activity.me;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.liansheng.carworld.R;
import com.liansheng.carworld.activity.BaseActivity;
import com.liansheng.carworld.bean.UserInfo;
import com.liansheng.carworld.bean.company.ApplyCompanyBean;
import com.liansheng.carworld.bean.me.CompanyBean;
import com.liansheng.carworld.bean.me.LicensesBean;
import com.liansheng.carworld.kit.Constant;
import com.liansheng.carworld.net.JsonCallback;
import com.liansheng.carworld.net.NetApi;
import com.liansheng.carworld.net.UrlKit;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.droidlover.xdroid.hold.info.SharedInfo;
import cn.droidlover.xdroid.tools.utils.ActivityManage;
import cn.droidlover.xdroid.tools.utils.Util;
import okhttp3.Call;

public class InfoAuthAct extends BaseActivity {

    @BindView(R.id.info_company_state)
    ImageView companyState;
    @BindView(R.id.info_driver_state)
    ImageView driverState;

    UserInfo userInfo;
    private boolean userPass;
    ApplyCompanyBean companyBean;

    @Override
    public void initData(Bundle savedInstanceState) {

    }

    @Override
    public int getLayoutId() {
        return R.layout.act_info_auth;
    }

    @Override
    protected void onResume() {
        super.onResume();
        getUserInfo();
    }

    @OnClick({R.id.info_company, R.id.info_person, R.id.info_driver, R.id.info_company_state, R.id.info_driver_state})
    public void onViewClicker(View view) {
        if (userInfo == null) {
            return;
        }
        switch (view.getId()) {
            case R.id.info_person:
                ActivityManage.push(IdAuthAct.class);
                break;
            case R.id.info_company:
                if (!userPass) {
                    Util.toast("请先实名认证");
                    return;
                }
                ActivityManage.push(CompanyAuthInfoAct.class);
                break;
            case R.id.info_driver_state:
            case R.id.info_driver:
                if (!userPass) {
                    Util.toast("请先实名认证");
                    return;
                }
                ActivityManage.push(DriverAuthAct.class);
                break;
            case R.id.info_company_state:
                if (companyBean != null && !companyBean.isPassed()) {
                    Intent intent = new Intent();
                    intent.putExtra(Constant.KEY_ID, String.valueOf(companyBean.getCompanyId()));
                    ActivityManage.push(CompanyApplyAct.class, intent);
                }
                break;
        }
    }


    public void getUserInfo() {
        NetApi.get(UrlKit.USER_INFORM, null, new JsonCallback() {

            @Override
            public void onSuccess(String response, int id) {
                Gson gson = new Gson();
                userInfo = gson.fromJson(response, UserInfo.class);
                if (userInfo == null) {
                    Util.toast("用户信息获取失败");
                    return;
                }
                userInfo.setToken(SharedInfo.getInstance().getValue(Constant.KEY_TOKEN, "").toString());
                setLoginResult(userInfo, false);
                SharedInfo.getInstance().saveEntity(userInfo);
                List<LicensesBean> licenses = userInfo.getLicenses();
                findViewById(R.id.info_person).setVisibility(View.VISIBLE);
                findViewById(R.id.info_person_state).setVisibility(View.GONE);
                findViewById(R.id.info_driver).setVisibility(View.VISIBLE);
                driverState.setVisibility(View.GONE);
                driverState.setEnabled(false);
                if (licenses != null && licenses.size() > 0) {
                    for (int i = 0; i < licenses.size(); i++) {
                        LicensesBean bean = licenses.get(i);
                        if (Constant.AUTH_IDENTITY_CARD.equals(bean.getType()) && Constant.ID_STATUS_PASSED.equals(bean.getStatus())) {
                            findViewById(R.id.info_person).setVisibility(View.INVISIBLE);
                            findViewById(R.id.info_person_state).setVisibility(View.VISIBLE);
                            userPass = true;
                        } else if ((Constant.AUTH_DRIVERS_LICENSE.equals(bean.getType()) || Constant.AUTH_DRIVING_LICENSE.equals(bean.getType())
                                || Constant.AUTH_COMPULSORY_INSURANCE.equals(bean.getType()))) {
                            if (Constant.ID_STATUS_PASSED.equals(bean.getStatus())) {
                                findViewById(R.id.info_driver).setVisibility(View.INVISIBLE);
                                driverState.setVisibility(View.VISIBLE);
                                driverState.setImageResource(R.mipmap.ic_status_pass);
                            } else if (Constant.ID_STATUS_VERIFYING.equals(bean.getStatus())) {
                                findViewById(R.id.info_driver).setVisibility(View.INVISIBLE);
                                driverState.setVisibility(View.VISIBLE);
                                driverState.setImageResource(R.mipmap.ic_status_verfying);
                            } else if (Constant.ID_STATUS_REJECT.equals(bean.getStatus())) {
                                findViewById(R.id.info_driver).setVisibility(View.INVISIBLE);
                                driverState.setVisibility(View.VISIBLE);
                                driverState.setImageResource(R.mipmap.ic_status_reject);
                                driverState.setEnabled(true);
                                break;
                            }
                        }
                    }
                }
                findViewById(R.id.info_company).setVisibility(View.VISIBLE);
                companyState.setVisibility(View.GONE);
                List<CompanyBean> companys = userInfo.getCompanys();
                if (companys != null && companys.size() > 0) {
                    CompanyBean bean = companys.get(0);
                    if (Constant.ID_STATUS_PASSED.equals(bean.getStatus())) {
                        findViewById(R.id.info_company).setVisibility(View.INVISIBLE);
                        companyState.setVisibility(View.VISIBLE);
                        companyState.setImageResource(R.mipmap.ic_status_pass);
                        return;
                    } else if (Constant.ID_STATUS_VERIFYING.equals(bean.getStatus())) {
                        companyState.setVisibility(View.VISIBLE);
                        findViewById(R.id.info_company).setVisibility(View.INVISIBLE);
                        companyState.setImageResource(R.mipmap.ic_status_verfying);
                    }
                }
                getApplyCompany();
            }
        });
    }

    private void getApplyCompany() {
        NetApi.get(UrlKit.USER_APPLYING_COMPANY, null, new JsonCallback() {

            @Override
            public void onSuccess(String response, int id) {
                List<ApplyCompanyBean> pageBean = new Gson().fromJson(response, new TypeToken<List<ApplyCompanyBean>>() {
                }.getType());
                companyBean = null;
                if (pageBean != null && pageBean.size() > 0) {
                    companyBean = pageBean.get(0);
                    if (!companyBean.isPassed()) {
                        findViewById(R.id.info_company).setVisibility(View.INVISIBLE);
                        companyState.setVisibility(View.VISIBLE);
                        companyState.setImageResource(R.mipmap.ic_status_verfying);
                    }
                }
            }
        });
    }

}
