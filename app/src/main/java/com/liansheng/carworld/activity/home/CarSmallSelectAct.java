package com.liansheng.carworld.activity.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.liansheng.carworld.R;
import com.liansheng.carworld.activity.BaseActivity;
import com.liansheng.carworld.activity.OrderActivity;
import com.liansheng.carworld.bean.me.AddressBean;
import com.liansheng.carworld.kit.Constant;

import butterknife.OnClick;
import cn.droidlover.xdroid.tools.utils.ActivityManage;

public class CarSmallSelectAct extends BaseActivity {

    @Override
    public void initData(Bundle savedInstanceState) {

    }

    @Override
    public int getLayoutId() {
        return R.layout.act_car_small_select;
    }


    @OnClick({R.id.car_small_1, R.id.car_small_2, R.id.car_small_3, R.id.car_small_4, R.id.car_small_5, R.id.car_small_6})
    public void onViewClick(View view) {
        Intent intent = new Intent();
        intent.putExtra(Constant.KEY_START, (AddressBean) getIntent().getParcelableExtra(Constant.KEY_START));
        intent.putExtra(Constant.KEY_END, (AddressBean) getIntent().getParcelableExtra(Constant.KEY_END));
        switch (view.getId()) {
            case R.id.car_small_1:
                intent.putExtra(Constant.KEY_TYPE, 0);
                break;
            case R.id.car_small_2:
                intent.putExtra(Constant.KEY_TYPE, 1);
                break;
            case R.id.car_small_3:
                intent.putExtra(Constant.KEY_TYPE, 2);
                break;
            case R.id.car_small_4:
                intent.putExtra(Constant.KEY_TYPE, 3);
                break;
            case R.id.car_small_5:
                intent.putExtra(Constant.KEY_TYPE, 4);
                break;
            case R.id.car_small_6:
                intent.putExtra(Constant.KEY_TYPE, 5);
                break;
        }
        ActivityManage.push(OrderActivity.class, intent);
        finish();
    }
}
