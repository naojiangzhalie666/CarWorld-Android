package com.liansheng.carworld.activity.me;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.liansheng.carworld.R;
import com.liansheng.carworld.activity.BaseActivity;
import com.liansheng.carworld.activity.home.CompanyInfoAct;
import com.liansheng.carworld.bean.UserInfo;
import com.liansheng.carworld.bean.me.CompanyBean;
import com.liansheng.carworld.kit.Constant;


import butterknife.BindView;
import butterknife.OnClick;
import cn.droidlover.xdroid.hold.info.SharedInfo;
import cn.droidlover.xdroid.tools.utils.ActivityManage;

@Deprecated
public class CompanyAct extends BaseActivity {

    @BindView(R.id.tv_qm)
    TextView tv_qm;
    @BindView(R.id.ll_qm)
    RelativeLayout ll_qm;
    @BindView(R.id.tv_company_qm)
    TextView tv_company_qm;
    @BindView(R.id.tv_company_qm_type)
    TextView tv_company_qm_type;
    @BindView(R.id.tv_wl)
    TextView tv_wl;
    @BindView(R.id.ll_wl)
    RelativeLayout ll_wl;
    @BindView(R.id.tv_company_wl)
    TextView tv_company_wl;
    @BindView(R.id.tv_4s)
    TextView tv_4s;
    @BindView(R.id.ll_4s)
    RelativeLayout ll_4s;
    @BindView(R.id.tv_company_4s)
    TextView tv_company_4s;
    @BindView(R.id.tv_company_4s_type)
    TextView tv_company_4s_type;
    private CompanyBean qmBean;
    private CompanyBean fourSBean;
    private CompanyBean wlBean;


    @Override
    public void initData(Bundle savedInstanceState) {
        UserInfo userInfo = SharedInfo.getInstance().getEntity(UserInfo.class);
        if (userInfo.getCompanys() != null && userInfo.getCompanys().size() > 0) {
            for (int i = 0; i < userInfo.getCompanys().size(); i++) {
                CompanyBean bean = userInfo.getCompanys().get(i);
                if (Constant.ID_STATUS_PASSED.equals(bean.getStatus())) {
                    if (Constant.STORE_TYPE_1.equals(bean.getType())) {
                        qmBean = bean;
                        tv_company_qm.setText(bean.getName());
                        if (bean.isSecondHandPriority()) {
                            tv_company_qm_type.setText("主营二手车，顺带新车");
                        } else {
                            tv_company_qm_type.setText("主营新车，顺带二手车");
                        }
                        tv_qm.setVisibility(View.VISIBLE);
                        ll_qm.setVisibility(View.VISIBLE);
                    } else if (Constant.STORE_TYPE_3.equals(bean.getType())) {
                        wlBean = bean;
                        tv_company_wl.setText(bean.getName());
                        tv_wl.setVisibility(View.VISIBLE);
                        ll_wl.setVisibility(View.VISIBLE);
                    } else if (Constant.STORE_TYPE_2.equals(bean.getType())) {
                        fourSBean = bean;
                        tv_4s.setVisibility(View.VISIBLE);
                        ll_4s.setVisibility(View.VISIBLE);
                        tv_company_4s.setText(bean.getName());
                        tv_company_4s_type.setText("主营：" + bean.getMainBrand());
                    }
                }
            }
        }
    }

    @Override
    public int getLayoutId() {
        return R.layout.act_company_me;
    }

    @OnClick({R.id.ll_qm, R.id.ll_wl, R.id.ll_4s})
    public void onClick(View view) {
        Intent intent = new Intent();
        switch (view.getId()) {
            case R.id.ll_wl:
                intent.putExtra(Constant.KEY_BEAN, wlBean);
                ActivityManage.push(CompanyInfoAct.class, intent);
                break;
            case R.id.ll_qm:
                intent.putExtra(Constant.KEY_BEAN, qmBean);
                ActivityManage.push(CompanyInfoAct.class, intent);
                break;
            case R.id.ll_4s:
                intent.putExtra(Constant.KEY_BEAN, fourSBean);
                ActivityManage.push(CompanyInfoAct.class, intent);
                break;
        }
    }
}
