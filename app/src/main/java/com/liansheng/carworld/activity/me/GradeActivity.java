package com.liansheng.carworld.activity.me;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.kongzue.dialog.v3.CustomDialog;
import com.liansheng.carworld.R;
import com.liansheng.carworld.activity.BaseActivity;
import com.liansheng.carworld.bean.UserInfo;
import com.liansheng.carworld.kit.Constant;
import com.liansheng.carworld.net.JsonCallback;
import com.liansheng.carworld.net.NetApi;
import com.liansheng.carworld.wxapi.WxUtils;

import butterknife.BindView;
import butterknife.OnClick;
import cn.droidlover.xdroid.hold.info.SharedInfo;
import cn.droidlover.xdroid.tools.utils.ActivityManage;
import cn.droidlover.xdroid.tools.utils.Util;
import cn.droidlover.xdroid.views.appbar.TitleBar;
import cn.droidlover.xdroid.views.appbar.ToolBar;
import okhttp3.Call;

public class GradeActivity extends BaseActivity {

    @BindView(R.id.toolbar)
    ToolBar toolBar;
    @BindView(R.id.tv_grade)
    TextView tvGrade;
    @BindView(R.id.btn_dh)
    Button btnDh;
    @BindView(R.id.tv_yqfx)
    TextView tvYqfx;
    @BindView(R.id.tv_cz)
    TextView tvCz;
    public int yf;

    @Override
    public void initData(Bundle savedInstanceState) {
        toolBar.addAction(new TitleBar.TextAction("历史记录") {
            @Override
            public void performAction(View view) {
                Intent intent = new Intent();
                intent.putExtra(Constant.KEY_TYPE, 1);
                intent.putExtra(Constant.KEY_TITLE, "历史记录");
                ActivityManage.push(BillAct.class, intent);
            }
        });
        tvGrade.setText(getLoginResult().getPoints() + "");
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_grade;
    }//布局文件

    public void getVip(int months) {
        NetApi.putVip(getLoginResult().getToken(), months, new JsonCallback() {

            @Override
            public void onSuccess(String response, int id) {
//                showSnackToast(main, "兑换成功");
                Util.toast("兑换成功");
                finish();
            }
        });
    }

    public void showDhDialog() {
        CustomDialog.build(this, R.layout.dialog_dh, new CustomDialog.OnBindView() {
            @Override
            public void onBind(final CustomDialog dialog, View v) {
                ImageView des = v.findViewById(R.id.iv_des);
                ImageView add = v.findViewById(R.id.iv_add);
                TextView price = v.findViewById(R.id.tv_price);
                Button btnOk = v.findViewById(R.id.btn_ok);
                ImageView close = v.findViewById(R.id.iv_close);
                yf = 1;
                des.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (yf == 1) {
                            showToast("不能再减啦");
                        } else if (yf > 1) {
                            yf = yf - 1;
                            price.setText(yf + "个月");
                        }
                    }
                });
                add.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (yf == 6) {
                            showToast("不能再加啦");
                            return;
                        }
                        yf = yf + 1;
                        price.setText(yf + "个月");
                    }
                });
                btnOk.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        getVip(yf);
                        dialog.doDismiss();
                    }
                });
                close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.doDismiss();
                    }
                });
            }
        })
                .setFullScreen(true)
                .show();
    }

    @OnClick({R.id.btn_dh, R.id.tv_yqfx, R.id.tv_cz})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_dh:
                showDhDialog();
                break;
            case R.id.tv_yqfx:
                WxUtils.getInstance().wxWeb(SharedInfo.getInstance().getEntity(UserInfo.class).getInvitationLink(), "车无界", "邀请您注册车无界");
                break;
            case R.id.tv_cz:
                toActivity(InvestActivity.class, null);
                break;
        }
    }

}
