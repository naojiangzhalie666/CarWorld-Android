package com.liansheng.carworld.activity.user;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.liansheng.carworld.R;
import com.liansheng.carworld.activity.BaseActivity;
import com.liansheng.carworld.activity.MainActivity;
import com.liansheng.carworld.activity.WebViewAct;
import com.liansheng.carworld.activity.logic.UserLogic;
import com.liansheng.carworld.bean.UserInfo;
import com.liansheng.carworld.bean.event.UIEvent;
import com.liansheng.carworld.kit.Constant;
import com.liansheng.carworld.net.JsonCallback;
import com.liansheng.carworld.net.NetApi;
import com.liansheng.carworld.net.UrlKit;
import com.liansheng.carworld.utils.StringUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.OnClick;
import cn.droidlover.xdroid.hold.info.SharedInfo;
import cn.droidlover.xdroid.tools.utils.ActivityManage;
import cn.droidlover.xdroid.tools.utils.Util;
import cn.jpush.android.api.JPushInterface;
import okhttp3.Call;


public class LoginYzmActivity extends BaseActivity {

    @BindView(R.id.et_account)
    EditText etAccount;
    @BindView(R.id.et_password)
    EditText etPassword;
    @BindView(R.id.btn_back)
    ImageView btnBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.btn_yzm)
    Button btnYzm;
    @BindView(R.id.tv_go)
    TextView tvGo;
    public String token;
    final MyCountDownTimer myCountDownTimer = new MyCountDownTimer(30000, 1000);
    @BindView(R.id.main)
    LinearLayout main;

    private String mToken;
    private String mAccessCode;

    private void getSms(String mobile) {
        NetApi.getSms(mobile, new JsonCallback() {

            @Override
            public void onSuccess(String response, int id) {
//                    showToast("验证码已发送");
                myCountDownTimer.start();
                showSnackToast(main, "验证码已发送");
            }
        });
    }

    private class MyCountDownTimer extends CountDownTimer {

        public MyCountDownTimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        //计时过程
        @Override
        public void onTick(long l) {
            //防止计时过程中重复点击
            btnYzm.setClickable(false);
            btnYzm.setText(l / 1000 + "秒");
        }

        //计时完毕的方法
        @Override
        public void onFinish() {
            //重新给Button设置文字
            btnYzm.setText("重新获取");
            //设置可点击
            btnYzm.setClickable(true);
        }

    }

    @Override
    public void initData(Bundle savedInstanceState) {
        EventBus.getDefault().register(this);
        tvTitle.setText("登录");
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onDataEvent(UIEvent uiEvent) {
        if (uiEvent.getType() == UIEvent.UPDATE_LOGIN) {
            HashMap<String, Object> map = new HashMap<>();
            map.put("type", "phoneNumber");
            map.put("token", mToken);
            map.put("accessToken", mAccessCode);
            UserLogic.login(map, (token, data) -> {
                Gson gson = new Gson();
                UserInfo userInfo = gson.fromJson(data, UserInfo.class);
                userInfo.setToken(token);
                setLoginResult(userInfo, false);
                SharedInfo.getInstance().saveEntity(userInfo);
                JPushInterface.setAlias(getApplicationContext(), Constant.JPUSH_MESSAGE, userInfo.getMobile());
                Util.toast("登录成功");
                jumpToActivityAndClearTask(MainActivity.class);
            });
        }
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_login;
    }

    @OnClick({R.id.btn_yzm, R.id.tv_son, R.id.tv_go, R.id.btn_go, R.id.tv_regular1, R.id.tv_regular2})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_yzm:
                if (StringUtil.isTrimBlank(etAccount.getText().toString())) {
                    showToast("请输入手机号");
                    return;
                }
                getSms(etAccount.getText().toString());
                break;
            case R.id.tv_son:
                Intent intent3 = new Intent();
                intent3.putExtra(Constant.KEY_TYPE, 1);
                ActivityManage.push(LoginActivity.class, intent3);
                break;
            case R.id.tv_go:
                Intent intent2 = new Intent();
                intent2.putExtra(Constant.KEY_TYPE, 2);
                ActivityManage.push(LoginActivity.class, intent2);
                break;
            case R.id.btn_go:
//                progressBar.setVisibility(View.VISIBLE);
                String mobile = etAccount.getText().toString().trim();
                String password = etPassword.getText().toString().trim();
                if (StringUtil.isTrimBlank(mobile)) {
                    showToast("请输入手机号");
                    return;
                }
                if (!isMobilPhone(mobile)) {
                    showToast("请输入有效的手机号码");
                    return;
                }
                if (StringUtil.isTrimBlank(password)) {
                    showToast("请输入验证码");
                    return;
                }
                HashMap<String, Object> map = new HashMap<>();
                map.put("mobile", mobile);
                map.put("password", password);
                map.put("type", "sms");
                UserLogic.login(map, (token, data) -> {
                    Gson gson = new Gson();
                    UserInfo userInfo = gson.fromJson(data, UserInfo.class);
                    userInfo.setToken(token);
                    setLoginResult(userInfo, false);
                    SharedInfo.getInstance().saveEntity(userInfo);
                    JPushInterface.setAlias(getApplicationContext(), Constant.JPUSH_MESSAGE, userInfo.getMobile());
                    Util.toast("登录成功");
                    jumpToActivityAndClearTask(MainActivity.class);
                });
                break;
            case R.id.tv_regular1:
                Intent intent = new Intent();
                intent.putExtra(Constant.KEY_TITLE, "隐私协议");
                intent.putExtra(Constant.KEY_URL, UrlKit.H5_SECRET_REGULAR);
                ActivityManage.push(WebViewAct.class, intent);
                break;
            case R.id.tv_regular2:
                Intent intent1 = new Intent();
                intent1.putExtra(Constant.KEY_TITLE, "用户服务协议");
                intent1.putExtra(Constant.KEY_URL, UrlKit.H5_REGISTER_REGULAR);
                ActivityManage.push(WebViewAct.class, intent1);
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
