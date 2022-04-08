package com.liansheng.carworld.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.liansheng.carworld.R;
import com.liansheng.carworld.activity.me.BillAct;
import com.liansheng.carworld.kit.Constant;
import com.liansheng.carworld.net.JsonCallback;
import com.liansheng.carworld.net.NetApi;
import com.qmuiteam.qmui.widget.QMUITopBar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.droidlover.xdroid.tools.utils.ActivityManage;
import cn.droidlover.xdroid.tools.utils.Util;
import cn.droidlover.xdroid.views.appbar.TitleBar;
import cn.droidlover.xdroid.views.appbar.ToolBar;
import okhttp3.Call;

public class WalletActivity extends BaseActivity {

    @BindView(R.id.toolbar)
    ToolBar toolBar;
    @BindView(R.id.tv_money)
    TextView tvMoney;
    @BindView(R.id.et_num)
    EditText etNum;
    @BindView(R.id.confirm)
    TextView confirm;
    @BindView(R.id.et_account)
    EditText etAccount;

    private void initView() {
        tvMoney.setText(getLoginResult().getBalance());
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        initView();
        toolBar.addAction(new TitleBar.TextAction("账单") {
            @Override
            public void performAction(View view) {
                Intent intent=new Intent();
                intent.putExtra(Constant.KEY_TYPE,2);
                intent.putExtra(Constant.KEY_TITLE,"账单");
                ActivityManage.push(BillAct.class,intent);
            }
        });
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_wallet;
    }//布局文件

    @OnClick(R.id.confirm)
    public void onClick() {
        if (etAccount.getText().equals("")) {
            showToast("请输入支付宝账号");
            return;
        }
        if (etNum.getText().toString().equals("")) {
            showToast("请输入金额");
            return;
        }
        if (Integer.parseInt(etNum.getText().toString()) < 10) {
            showToast("提现金额必须大于10元");
            return;
        }
        NetApi.postCashOut(getLoginResult().getToken(), etNum.getText().toString(), etAccount.getText().toString(), new JsonCallback() {

            @Override
            public void onSuccess(String response, int id) {
//                    XToastUtils.success("提交成功");
                Util.toast("提现申请成功");
                finish();
            }
        });
    }
}
