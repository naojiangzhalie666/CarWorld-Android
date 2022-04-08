package com.liansheng.carworld.activity.company;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.core.content.ContextCompat;

import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.kongzue.dialog.interfaces.OnDialogButtonClickListener;
import com.kongzue.dialog.util.BaseDialog;
import com.kongzue.dialog.v3.MessageDialog;
import com.liansheng.carworld.R;
import com.liansheng.carworld.activity.BaseActivity;
import com.liansheng.carworld.activity.home.AuthDispatchAct;
import com.liansheng.carworld.activity.me.Auth4SAct;
import com.liansheng.carworld.activity.me.AuthQMAct;
import com.liansheng.carworld.bean.UserInfo;
import com.liansheng.carworld.bean.me.CompanyBean;
import com.liansheng.carworld.kit.Constant;
import com.liansheng.carworld.net.JsonCallback;
import com.liansheng.carworld.net.NetApi;
import com.liansheng.carworld.net.UrlKit;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.droidlover.xdroid.hold.info.SharedInfo;
import cn.droidlover.xdroid.tools.utils.ActivityManage;
import cn.droidlover.xdroid.tools.utils.Util;
import cn.droidlover.xdroid.views.LeftRightLayout;
import cn.droidlover.xdroid.views.textView.NoDoubleClickButton;
import okhttp3.Call;

public class CompanySettingAct extends BaseActivity {

    @BindView(R.id.setting_change)
    LeftRightLayout settingChange;
    @BindView(R.id.btn_exit)
    NoDoubleClickButton btnExit;

    private List<CompanyBean> companys;
    private OptionsPickerView gsOptions;
    private CompanyBean companyBean;

    @Override
    public void initData(Bundle savedInstanceState) {
        companys = new ArrayList<>();
        companyBean = getIntent().getParcelableExtra(Constant.KEY_BEAN);
        settingChange.setRightText(companyBean.getName());
        UserInfo userInfo = SharedInfo.getInstance().getEntity(UserInfo.class);
        for (int i = 0; i < userInfo.getCompanys().size(); i++) {
            CompanyBean bean = userInfo.getCompanys().get(i);
            if (Constant.ID_STATUS_PASSED.equals(bean.getStatus())) {
                companys.add(bean);
            }
        }
        gsOptions = new OptionsPickerBuilder(context, (options1, options2, options3, v) -> {
            settingChange.setRightText(companys.get(options1).getName());
            Intent intent = new Intent();
            intent.putExtra(Constant.KEY_BEAN, companys.get(options1));
            setResult(RESULT_OK, intent);
            finish();
        }).setDividerColor(ContextCompat.getColor(context, R.color.line_grey))
                .setTextColorCenter(ContextCompat.getColor(context, R.color.main_normal_color))
                .setContentTextSize(16)
                .build();
        gsOptions.setPicker(companys);
        getCompanyAuth();
    }

    @Override
    public int getLayoutId() {
        return R.layout.act_company_setting;
    }

    @OnClick({R.id.setting_update, R.id.setting_change, R.id.btn_exit})
    public void onViewClicker(View view) {
        switch (view.getId()) {
            case R.id.setting_update:
                if (companyBean != null) {
                    if (Constant.STORE_TYPE_1.equals(companyBean.getType())) {
                        ActivityManage.push(AuthQMAct.class);
                    } else if (Constant.STORE_TYPE_2.equals(companyBean.getType())) {
                        ActivityManage.push(Auth4SAct.class);
                    } else if (Constant.STORE_TYPE_3.equals(companyBean.getType())) {
                        ActivityManage.push(AuthDispatchAct.class);
                    }
                }
                break;
            case R.id.setting_change:
                gsOptions.show();
                break;
            case R.id.btn_exit:
                MessageDialog.build(this)
                        .setTitle("温馨提示")
                        .setMessage("是否退出公司？")
                        .setCancelButton("取消", new OnDialogButtonClickListener() {
                            @Override
                            public boolean onClick(BaseDialog baseDialog, View v) {
                                return false;
                            }
                        })
                        .setOkButton("确定", new OnDialogButtonClickListener() {
                            @Override
                            public boolean onClick(BaseDialog baseDialog, View v) {
                                exitCompany();
                                return false;
                            }
                        })
                        .show();
                break;

        }
    }

    private void exitCompany() {
        NetApi.delete(String.format(UrlKit.USER_COMPANY_MEMBER, companyBean.getId()), null, new JsonCallback() {

            @Override
            public void onSuccess(String response, int id) {
                Util.toast("退出成功");
                setResult(RESULT_OK);
                finish();
            }
        });
    }

    private void getCompanyAuth() {
        NetApi.get(String.format(UrlKit.USER_COMPANY_AUTH, companyBean.getId()), null, new JsonCallback() {

            @Override
            public void onSuccess(String response, int id) {
                String userType = response.replace("\"", "");
                if (!"admin".equals(userType)) {
                    btnExit.setVisibility(View.VISIBLE);
                }
            }
        });
    }

}
