package com.liansheng.carworld.activity.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import com.google.gson.Gson;
import com.liansheng.carworld.R;
import com.liansheng.carworld.activity.BaseActivity;
import com.liansheng.carworld.activity.WebViewAct;
import com.liansheng.carworld.bean.ConfigBean;
import com.liansheng.carworld.bean.UserInfo;
import com.liansheng.carworld.kit.Constant;
import com.liansheng.carworld.net.JsonCallback;
import com.liansheng.carworld.net.NetApi;
import com.liansheng.carworld.net.UrlKit;

import butterknife.BindView;
import butterknife.OnClick;
import cn.droidlover.xdroid.hold.info.SharedInfo;
import cn.droidlover.xdroid.tools.utils.ActivityManage;
import cn.droidlover.xdroid.tools.utils.Util;
import okhttp3.Call;

public class SteamAct extends BaseActivity {

    @BindView(R.id.steam_ck)
    CheckBox steamCk;
    @BindView(R.id.steam_tv3)
    TextView steam_tv3;

    @Override
    public void initData(Bundle savedInstanceState) {
        getConfig();
    }

    @Override
    public int getLayoutId() {
        return R.layout.act_steam;
    }

    @OnClick({R.id.steam_regular, R.id.steam_sale, R.id.steam_buy})
    public void onViewClick(View view) {
        Intent intent = new Intent();
        switch (view.getId()) {
            case R.id.steam_regular:
                intent.putExtra(Constant.KEY_TITLE, "车无界平台担保协议");
                intent.putExtra(Constant.KEY_URL, UrlKit.H5_DEPOSIT_PROTECTION_REGULAR);
                ActivityManage.push(WebViewAct.class, intent);
                break;
            case R.id.steam_buy:
                if (!steamCk.isChecked()) {
                    Util.toast("请先同意担保协议");
                    return;
                }
                intent.putExtra(Constant.KEY_ID, getIntent().getStringExtra(Constant.KEY_ID));
                intent.putExtra(Constant.KEY_TYPE, "buy");
                ActivityManage.push(SteamDetailsCreateAct.class, intent);
                break;
            case R.id.steam_sale:
                if (!steamCk.isChecked()) {
                    Util.toast("请先同意担保协议");
                    return;
                }
                intent.putExtra(Constant.KEY_ID, getIntent().getStringExtra(Constant.KEY_ID));
                intent.putExtra(Constant.KEY_TYPE, "sale");
                ActivityManage.push(SteamDetailsCreateAct.class, intent);
                break;
        }
    }

    void getConfig() {
        NetApi.get(UrlKit.APP_RESOURCE_CONFIG, null, new JsonCallback() {

            @Override
            public void onSuccess(String response, int id) {
                ConfigBean configBean = new Gson().fromJson(response, ConfigBean.class);
                steam_tv3.setText(configBean.getPlatformGuarantee_amount());
            }
        });
    }
}
