package com.liansheng.carworld.activity.me;

import android.os.Bundle;

import com.liansheng.carworld.R;
import com.liansheng.carworld.activity.BaseActivity;
import com.liansheng.carworld.kit.Constant;

import butterknife.OnClick;

public class DeleteAccountAct extends BaseActivity {
    @Override
    public void initData(Bundle savedInstanceState) {

    }

    @Override
    public int getLayoutId() {
        return R.layout.act_delete_account;
    }

    @OnClick(R.id.btn_link)
    public void onViewClick(){
        callPhone(this,"联系客服电话："+ Constant.SERVICE_PHONE, Constant.SERVICE_PHONE);
    }
}
