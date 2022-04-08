package com.liansheng.carworld.activity.home;

import android.os.Bundle;
import android.view.View;

import com.liansheng.carworld.R;
import com.liansheng.carworld.activity.BaseActivity;

import butterknife.OnClick;
import cn.droidlover.xdroid.tools.utils.ActivityManage;

public class ApplyDispatchAct extends BaseActivity {

    @Override
    public void initData(Bundle savedInstanceState) {

    }

    @Override
    public int getLayoutId() {
        return R.layout.act_apply_dispatch;
    }


    @OnClick(R.id.dispatch_btn)
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.dispatch_btn:
                ActivityManage.push(AuthDispatchAct.class);
                break;
        }
    }
}
