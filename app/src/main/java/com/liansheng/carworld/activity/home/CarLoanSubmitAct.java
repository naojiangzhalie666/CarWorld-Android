package com.liansheng.carworld.activity.home;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.liansheng.carworld.R;
import com.liansheng.carworld.activity.BaseActivity;
import com.liansheng.carworld.activity.WebViewAct;
import com.liansheng.carworld.activity.user.LoginYzmActivity;
import com.liansheng.carworld.bean.UserInfo;
import com.liansheng.carworld.bean.home.ReqLoan;
import com.liansheng.carworld.bean.me.LicensesBean;
import com.liansheng.carworld.kit.Constant;
import com.liansheng.carworld.net.JsonCallback;
import com.liansheng.carworld.net.NetApi;
import com.liansheng.carworld.net.UrlKit;
import com.liansheng.carworld.utils.StringUtil;

import butterknife.BindView;
import butterknife.OnClick;
import cn.droidlover.xdroid.hold.info.SharedInfo;
import cn.droidlover.xdroid.tools.utils.ActivityManage;
import cn.droidlover.xdroid.tools.utils.Util;
import cn.droidlover.xdroid.views.textView.NoDoubleClickButton;
import okhttp3.Call;

public class CarLoanSubmitAct extends BaseActivity {

    @BindView(R.id.header)
    ImageView header;
    @BindView(R.id.loan_company_name)
    TextView companyName;
    @BindView(R.id.loan_username)
    TextView username;
    @BindView(R.id.loan_name)
    EditText name;
    @BindView(R.id.loan_id_card)
    EditText idCard;
    @BindView(R.id.loan_phone)
    EditText phone;
    @BindView(R.id.loan_code)
    EditText loan_code;
    @BindView(R.id.loan_getcode)
    NoDoubleClickButton code;
    private String id;
    final MyCountDownTimer myCountDownTimer = new MyCountDownTimer(30000, 1000);

    @Override
    public void initData(Bundle savedInstanceState) {
        id=getIntent().getStringExtra(Constant.KEY_ID);
        UserInfo userInfo = SharedInfo.getInstance().getEntity(UserInfo.class);
        for (int i = 0; i < userInfo.getLicenses().size(); i++) {
            LicensesBean licensesBean = userInfo.getLicenses().get(i);
            if (Constant.AUTH_IDENTITY_CARD.equals(licensesBean.getType()) && "passed".equals(licensesBean.getStatus())) {
                username.setText(licensesBean.getOwner());
            }
        }
        if (userInfo.getCompanys() != null && userInfo.getCompanys().size() > 0) {
            companyName.setText(userInfo.getCompanys().get(0).getName());
        }else {
            companyName.setText("??????????????????");
        }
        Glide.with(context).load(UrlKit.getImgUrl(userInfo.getHeadphoto())).into(header);
    }

    @Override
    public int getLayoutId() {
        return R.layout.act_car_loan_submit;
    }


    @OnClick({R.id.back, R.id.submit, R.id.loan_getcode, R.id.loan_regular})
    public void onViewClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.loan_getcode:
                String phone = this.phone.getText().toString();
                if (StringUtil.isTrimBlank(phone)) {
                    showToast("??????????????????");
                    return;
                }
                if (!isMobilPhone(phone)) {
                    showToast("??????????????????????????????");
                    return;
                }
                getCode(phone);
                break;
            case R.id.loan_regular:
                Intent intent = new Intent(context, WebViewAct.class);
                intent.putExtra(Constant.KEY_TITLE, "???????????????????????????");
                intent.putExtra(Constant.KEY_URL, UrlKit.H5_PARTNER);
                startActivity(intent);
                break;
            case R.id.submit:
                String name = this.name.getText().toString();
                String idCard = this.idCard.getText().toString();
                String phone2 = this.phone.getText().toString();
                String code = loan_code.getText().toString();
                if (StringUtil.isTrimBlank(name)) {
                    showToast("????????????????????????");
                    return;
                }
                if (StringUtil.isTrimBlank(idCard)) {
                    showToast("?????????????????????????????????");
                    return;
                }
                if(idCard.length()!=15&&idCard.length()!=18){
                    showToast("????????????????????????");
                    return;
                }
                if (StringUtil.isTrimBlank(phone2)) {
                    showToast("??????????????????");
                    return;
                }
                if (!isMobilPhone(phone2)) {
                    showToast("??????????????????????????????");
                    return;
                }
                if (StringUtil.isTrimBlank(code)) {
                    showToast("??????????????????");
                    return;
                }
                ReqLoan reqLoan=new ReqLoan();
                reqLoan.setPartnerId(id);
                reqLoan.setName(name);
                reqLoan.setIdnumber(idCard);
                reqLoan.setMobile(phone2);
                reqLoan.setCode(code);
                NetApi.postJson(UrlKit.CAR_LOAN_ORDER, new Gson().toJson(reqLoan), new JsonCallback() {

                    @Override
                    public void onSuccess(String response, int id) {
                        Util.toast("????????????");
                        ActivityManage.push(CarLoanResultAct.class);
                        finish();
                    }
                });
                break;
        }
    }


    private void getCode(String mobile) {
        NetApi.getSms(mobile, new JsonCallback() {

            @Override
            public void onSuccess(String response, int id) {
                myCountDownTimer.start();
                showToast("??????????????????");
            }
        });
    }


    private class MyCountDownTimer extends CountDownTimer {

        public MyCountDownTimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        //????????????
        @Override
        public void onTick(long l) {
            //?????????????????????????????????
            code.setClickable(false);
            code.setText(l / 1000 + "???");
        }

        //?????????????????????
        @Override
        public void onFinish() {
            //?????????Button????????????
            code.setText("????????????");
            //???????????????
            code.setClickable(true);
        }

    }
}
