package com.liansheng.carworld.activity.sub;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;
import com.kongzue.dialog.interfaces.OnDialogButtonClickListener;
import com.kongzue.dialog.util.BaseDialog;
import com.kongzue.dialog.v3.MessageDialog;
import com.liansheng.carworld.R;
import com.liansheng.carworld.activity.BaseActivity;
import com.liansheng.carworld.activity.ChangePswActivity;
import com.liansheng.carworld.bean.child.ChildInfoBean;
import com.liansheng.carworld.kit.Constant;
import com.liansheng.carworld.net.JsonCallback;
import com.liansheng.carworld.net.NetApi;
import com.liansheng.carworld.net.UrlKit;

import butterknife.BindView;
import butterknife.OnClick;
import cn.droidlover.xdroid.hold.info.SharedInfo;
import cn.droidlover.xdroid.tools.utils.ActivityManage;
import cn.droidlover.xdroid.views.LeftRightLayout;
import okhttp3.Call;

public class SubMeAct extends BaseActivity {
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_zhpf)
    TextView tvZhpf;
    @BindView(R.id.tv_evaluation)
    TextView tvEvaluation;
    @BindView(R.id.llyt_company)
    LeftRightLayout llytCompany;

    @Override
    public void initData(Bundle savedInstanceState) {

    }

    @Override
    public int getLayoutId() {
        return R.layout.act_sub_me;
    }

    @Override
    public void onResume() {
        super.onResume();
        getUserInfo();
    }

    private void getUserInfo() {
        NetApi.get(UrlKit.CHILD_ACCOUNT_INFO, null, new JsonCallback() {

            @Override
            public void onSuccess(String response, int id) {
                Gson gson = new Gson();
                ChildInfoBean userInfo = gson.fromJson(response, ChildInfoBean.class);
                SharedInfo.getInstance().saveEntity(userInfo);
                llytCompany.setRightText(userInfo.getCompany().getName());
                tvEvaluation.setText(userInfo.getScore());
                tvName.setText(userInfo.getUsername());
            }
        });
    }

    @OnClick({R.id.llyt_order, R.id.llyt_pwd, R.id.tv_zhpf, R.id.tv_evaluation, R.id.btn_exit})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.llyt_order:
                ActivityManage.push(SubOrderListAct.class);
                break;
            case R.id.llyt_pwd:
                Intent intent = new Intent();
                intent.putExtra(Constant.KEY_TYPE, true);
                intent.putExtra("sign", true);
                ActivityManage.push(ChangePswActivity.class, intent);
                break;
            case R.id.tv_zhpf:
            case R.id.tv_evaluation:
                showPfDialog();
                break;
            case R.id.btn_exit:
                MessageDialog.build(this)
                        .setTitle("温馨提示")
                        .setMessage("确定要重新登录吗？")
                        .setCancelButton("取消", new OnDialogButtonClickListener() {
                            @Override
                            public boolean onClick(BaseDialog baseDialog, View v) {
                                return false;
                            }
                        })
                        .setOkButton("确定", new OnDialogButtonClickListener() {
                            @Override
                            public boolean onClick(BaseDialog baseDialog, View v) {
                                SharedPreferences sp = getSharedPreferences(UrlKit.LOGIN_STATE, Context.MODE_PRIVATE);
                                sp.edit().clear().commit();
                                SharedInfo.getInstance().saveValue(UrlKit.FIRST_RESULT, true);
                                SharedInfo.getInstance().remove(Constant.CHILD_TYPE);
                                SharedInfo.getInstance().remove(Constant.KEY_TOKEN);
                                SharedInfo.getInstance().remove(ChildInfoBean.class);
                                finish();
                                return false;
                            }
                        })
                        .show();
                break;
        }
    }

    public void showPfDialog() {
        MessageDialog.build(this)
                .setTitle("综合评分说明")
                .setMessage("初始评分为100分，上传商业险评分+20分，货运险评分+50分，营业执照评分+50分。好评加10分，差评减10分。\n望各位司机注重评分，评分低于100分，平台不予以接单。")
                .setOkButton("知道了", new OnDialogButtonClickListener() {
                    @Override
                    public boolean onClick(BaseDialog baseDialog, View v) {
                        return false;
                    }
                })
                .show();
    }
}
