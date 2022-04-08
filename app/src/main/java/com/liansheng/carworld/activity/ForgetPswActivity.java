package com.liansheng.carworld.activity;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.ybq.android.spinkit.SpinKitView;
import com.liansheng.carworld.R;
import com.liansheng.carworld.net.JsonCallback;
import com.liansheng.carworld.net.NetApi;
import com.liansheng.carworld.utils.StringUtil;
import com.qmuiteam.qmui.widget.QMUITopBar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;


public class ForgetPswActivity extends BaseActivity {

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
    @BindView(R.id.btn_go)
    Button btnGo;
    @BindView(R.id.progress)
    SpinKitView progress;
    public ProgressBar progressBar;
    public String token;
    final MyCountDownTimer myCountDownTimer = new MyCountDownTimer(60000, 1000);
    @BindView(R.id.et_new_psw)
    EditText etNewPsw;
    @BindView(R.id.main)
    LinearLayout main;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_psw);
        ButterKnife.bind(this);
        tvTitle.setText("忘记密码");
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        initView();
    }

    private void initView() {
        progressBar = (ProgressBar) findViewById(R.id.progress);
        btnGo.setOnClickListener(view -> {
            progressBar.setVisibility(View.VISIBLE);
            String mobile = etAccount.getText().toString().trim();
            String code = etPassword.getText().toString().trim();
            String password = etNewPsw.getText().toString().trim();
            if (StringUtil.isTrimBlank(mobile)) {
                showToast("请输入手机号");
                return;
            }
            if (StringUtil.isTrimBlank(code)) {
                showToast("请输入验证码");
                return;
            }
            if (StringUtil.isTrimBlank(password)) {
                showToast("请输入新密码");
                return;
            }
            NetApi.putForgetPsw(mobile, code, password, new JsonCallback() {

                @Override
                public void onSuccess(String response, int id) {
//                        showToast("重置密码成功");
                        showSnackToast(main, "重置密码成功");
                        progressBar.setVisibility(View.GONE);
                        finish();
                }
            });
        });

    }

    private void getSms(String mobile) {
        NetApi.getSms(mobile, new JsonCallback() {

            @Override
            public void onSuccess(String response, int id) {
                showToast("验证码已发送");
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

    }

    @Override
    public int getLayoutId() {
        return 0;
    }

    @OnClick({R.id.btn_yzm, R.id.btn_go})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_yzm:
                if (StringUtil.isTrimBlank(etAccount.getText().toString())) {
                    showToast("请输入手机号");
                    return;
                } else {
                    myCountDownTimer.start();
                    getSms(etAccount.getText().toString());
                }
                break;
        }
    }
}
