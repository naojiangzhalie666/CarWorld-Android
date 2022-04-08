package com.liansheng.carworld.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.liansheng.carworld.R;
import com.liansheng.carworld.kit.Constant;
import com.liansheng.carworld.net.JsonCallback;
import com.liansheng.carworld.net.NetApi;
import com.liansheng.carworld.net.UrlKit;
import com.qmuiteam.qmui.widget.QMUITopBar;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.droidlover.xdroid.tools.utils.Util;
import cn.droidlover.xdroid.views.textView.NoDoubleClickButton;
import okhttp3.Call;

public class ChangePswActivity extends BaseActivity {

    @BindView(R.id.btn_back)
    ImageView btnBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.et_psw)
    EditText etPsw;
    @BindView(R.id.et_new_psw)
    EditText etNewPsw;
    @BindView(R.id.et_new_psw2)
    EditText etNewPsw2;
    @BindView(R.id.btn_go)
    NoDoubleClickButton btnGo;

    public String psw;
    public String newpsw;
    public String newpsw2;
    @BindView(R.id.main)
    LinearLayout main;

    private boolean hasPwd;
    private boolean isSign;

    @Override
    public void initData(Bundle savedInstanceState) {
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        hasPwd = getIntent().getBooleanExtra(Constant.KEY_TYPE, false);
        isSign = getIntent().getBooleanExtra("sign", false);
        if (hasPwd) {
            findViewById(R.id.pwd_has_y).setVisibility(View.VISIBLE);
        }
        tvTitle.setText(hasPwd ? "修改密码" : "设置密码");
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_change_psw;
    }//布局文件

    @OnClick(R.id.btn_go)
    public void onClick() {
        if (hasPwd) {
            if (etPsw.getText().toString().length() > 0) {
                psw = etPsw.getText().toString();
            } else {
                showToast("请输入原密码");
                return;
            }
        }
        if (etNewPsw.getText().toString().length() > 0) {
            newpsw = etNewPsw.getText().toString();
        } else {
            showToast("请输入新密码");
            return;
        }
        if (etNewPsw2.getText().toString().length() > 0) {
            newpsw2 = etNewPsw2.getText().toString();
        } else {
            showToast("请输入确认密码");
            return;
        }
        if (newpsw.equals(newpsw2)) {
            change();
        } else {
            showToast("新密码和确认密码不一致");
        }
    }

    public void change() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("password", newpsw);
        if (hasPwd) {
            map.put("origin", psw);
        }

        if(isSign){
            NetApi.put(UrlKit.CHILD_ACCOUNT_PASSWORD, map, new JsonCallback() {

                @Override
                public void onSuccess(String response, int id) {
                    if (hasPwd) {
                        Util.toast("修改密码成功");
                    } else {
                        Util.toast("设置密码成功");
                    }
                    finish();
                }
            });
        }else {
            NetApi.put(UrlKit.CHANGE_PSW, map, new JsonCallback() {

                @Override
                public void onSuccess(String response, int id) {
                    if (hasPwd) {
                        Util.toast("修改密码成功");
                    } else {
                        Util.toast("设置密码成功");
                    }
                    finish();
                }
            });
        }
    }
}
