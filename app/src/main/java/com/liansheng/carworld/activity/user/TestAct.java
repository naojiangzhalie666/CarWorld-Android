package com.liansheng.carworld.activity.user;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;

import com.liansheng.carworld.R;
import com.liansheng.carworld.activity.BaseActivity;

public class TestAct extends BaseActivity {

    public static void start(Activity activity){
        Intent intent=new Intent(activity,TestAct.class);
        activity.startActivity(intent);
    }

    @Override
    public int getLayoutId() {
        return R.layout.act_test;
    }

    @Override
    public void initData(Bundle savedInstanceState) {

    }

}
