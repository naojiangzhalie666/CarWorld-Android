package com.liansheng.carworld.activity.me;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.liansheng.carworld.R;
import com.liansheng.carworld.activity.BaseActivity;
import com.liansheng.carworld.net.JsonCallback;
import com.liansheng.carworld.net.NetApi;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.OnClick;
import cn.droidlover.xdroid.tools.utils.Util;
import okhttp3.Call;

public class AdviceAct extends BaseActivity {

    @BindView(R.id.content)
    EditText content;


    @Override
    public void initData(Bundle savedInstanceState) {

    }

    @Override
    public int getLayoutId() {
        return R.layout.act_advice;
    }

    @OnClick(R.id.btn_go)
    public void onClick() {
        String content = this.content.getText().toString();
        if (TextUtils.isEmpty(content) || content.length() < 7) {
            Util.toast("您输入的建议长度不够6个字");
            return;
        }
        HashMap<String,Object> map=new HashMap<>();
        map.put("content",content);
        NetApi.post("/api/app/user/idea", map, new JsonCallback() {

            @Override
            public void onSuccess(String response, int id) {
                Util.toast("提交成功");
                finish();
            }
        });
    }
}
