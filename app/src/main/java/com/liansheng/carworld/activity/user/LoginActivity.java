package com.liansheng.carworld.activity.user;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.liansheng.carworld.R;
import com.liansheng.carworld.activity.BaseActivity;
import com.liansheng.carworld.activity.ForgetPswActivity;
import com.liansheng.carworld.activity.MainActivity;
import com.liansheng.carworld.activity.logic.UserLogic;
import com.liansheng.carworld.activity.sub.SubMainAct;
import com.liansheng.carworld.bean.UserInfo;
import com.liansheng.carworld.bean.child.ChildInfoBean;
import com.liansheng.carworld.kit.Constant;
import com.liansheng.carworld.net.JsonCallback;
import com.liansheng.carworld.net.NetApi;
import com.liansheng.carworld.net.UrlKit;
import com.liansheng.carworld.utils.StringUtil;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import cn.droidlover.xdroid.hold.info.SharedInfo;
import cn.droidlover.xdroid.tools.utils.ActivityManage;
import cn.droidlover.xdroid.tools.utils.Util;
import cn.jpush.android.api.JPushInterface;
import okhttp3.Call;


public class LoginActivity extends BaseActivity {

    @BindView(R.id.et_account)
    EditText etAccount;
    @BindView(R.id.et_password)
    EditText etPassword;
    @BindView(R.id.btn_back)
    ImageView btnBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_forget)
    TextView tvForget;
    @BindView(R.id.btn_go)
    Button btnGo;
    int type;

    @Override
    public void initData(Bundle savedInstanceState) {
        tvTitle.setText("登录");
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        type = getIntent().getIntExtra(Constant.KEY_TYPE, 1);
        if (type == 1) {
            tvForget.setVisibility(View.GONE);
        }
        tvForget.setOnClickListener(view -> ActivityManage.push(ForgetPswActivity.class));
        btnGo.setOnClickListener(view -> {
            String mobile = etAccount.getText().toString().trim();
            String password = etPassword.getText().toString().trim();
            if (StringUtil.isTrimBlank(mobile)) {
                showToast("请输入手机号或用户名");
                return;
            }
            if (StringUtil.isTrimBlank(password)) {
                showToast("请输入密码");
                return;
            }
            HashMap<String, Object> map = new HashMap<>();
            map.put("mobile", mobile);
            map.put("password", password);
            if (type == 1) {
                map.put("type", "childAccount");
                NetApi.post(UrlKit.LOGIN, map, new JsonCallback() {

                    @Override
                    public void onSuccess(String response, int id) {
                        Map<String, String> hashMap = new Gson().fromJson(response, Map.class);
                        SharedInfo.getInstance().saveValue(Constant.KEY_TOKEN, hashMap.get("token"));
                        NetApi.get(UrlKit.CHILD_ACCOUNT_INFO, null, new JsonCallback() {

                            @Override
                            public void onSuccess(String response, int id) {
                                ChildInfoBean userInfo = new Gson().fromJson(response, ChildInfoBean.class);
                                SharedInfo.getInstance().saveValue(Constant.CHILD_TYPE,true);
                                SharedInfo.getInstance().saveEntity(userInfo);
                                jumpToActivityAndClearTask(SubMainAct.class);
                                Util.toast("登录成功");
                            }
                        });
                    }
                });
            } else {
                map.put("type", "password");
                UserLogic.login(map, (token, data) -> {
                    UserInfo userInfo = new Gson().fromJson(data, UserInfo.class);
                    userInfo.setToken(token);
                    SharedInfo.getInstance().saveEntity(userInfo);
                    setLoginResult(userInfo, false);
                    JPushInterface.setAlias(ActivityManage.peek(), Constant.JPUSH_MESSAGE, userInfo.getMobile());
                    jumpToActivityAndClearTask(MainActivity.class);
                    Util.toast("登录成功");
                });
            }

        });
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_login2;
    }


}
