package com.liansheng.carworld.activity.home;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.kongzue.dialog.interfaces.OnDialogButtonClickListener;
import com.kongzue.dialog.util.BaseDialog;
import com.kongzue.dialog.v3.MessageDialog;
import com.liansheng.carworld.R;
import com.liansheng.carworld.activity.BaseActivity;
import com.liansheng.carworld.activity.logic.OtherLogic;
import com.liansheng.carworld.bean.home.FindCardBean;
import com.liansheng.carworld.kit.Constant;
import com.liansheng.carworld.net.JsonCallback;
import com.liansheng.carworld.net.NetApi;
import com.liansheng.carworld.net.UrlKit;
import com.liansheng.carworld.utils.DateParserHelper;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.droidlover.xdroid.tools.utils.ActivityManage;
import cn.droidlover.xdroid.tools.utils.StringFormat;
import cn.droidlover.xdroid.views.appbar.ToolBar;
import okhttp3.Call;
import pub.devrel.easypermissions.EasyPermissions;

@Deprecated
public class FindCarDetailsAct extends BaseActivity {

    @BindView(R.id.tv_company)
    TextView tvCompany;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_ys)
    TextView tvYs;
    @BindView(R.id.tv_spd)
    TextView tvSpd;
    @BindView(R.id.tv_gls)
    TextView tvGls;
    @BindView(R.id.tv_cl)
    TextView tvCl;
    @BindView(R.id.tv_qyyq)
    TextView tvQyyq;
    @BindView(R.id.tv_tcjl)
    TextView tvTcjl;
    @BindView(R.id.tv_important)
    TextView tvImportant;
    @BindView(R.id.ck_water)
    CheckBox ckWater;
    @BindView(R.id.ck_fire)
    CheckBox ckFire;
    @BindView(R.id.ck_accident)
    CheckBox ckAccident;
    @BindView(R.id.ck_complete)
    CheckBox ckComplete;
    @BindView(R.id.tv_price)
    TextView tvPrice;
    @BindView(R.id.et_ck)
    TextView etCk;
    @BindView(R.id.toolbar)
    ToolBar toolbar;
    private FindCardBean findCardBean;

    @Override
    public void initData(Bundle savedInstanceState) {
        getCarDetails(getIntent().getStringExtra(Constant.KEY_ID));
    }

    private void getCarDetails(String id) {
        NetApi.get(UrlKit.CAR_RESOURCE_BRAND_ORDER_DETAILS + id, null, new JsonCallback() {

            @Override
            public void onSuccess(String response, int id) {
                findCardBean = new Gson().fromJson(response, FindCardBean.class);
                if (findCardBean.getCompany() != null) {
                    tvCompany.setText(findCardBean.getCompany().getName());
                }
                tvName.setText("发布者：" + findCardBean.getIdentityCard().getOwner());
                toolbar.setTitle("寻 " + findCardBean.getBrand_fct());
                if (findCardBean.isInWater() && findCardBean.isInFire() && findCardBean.isAccident()) {
                    tvImportant.setText("重点要求：无");
                } else {
                    ckWater.setChecked(!findCardBean.isInWater());
                    ckFire.setChecked(!findCardBean.isInFire());
                    ckAccident.setChecked(!findCardBean.isAccident());
                    ckComplete.setChecked(!findCardBean.isCompleted());
                    ckWater.setVisibility(!findCardBean.isInWater() ? View.VISIBLE : View.GONE);
                    ckFire.setVisibility(!findCardBean.isInFire() ? View.VISIBLE : View.GONE);
                    ckAccident.setVisibility(!findCardBean.isAccident() ? View.VISIBLE : View.GONE);
                    ckComplete.setVisibility(!findCardBean.isCompleted() ? View.VISIBLE : View.GONE);
                }
                tvYs.setText(findCardBean.getAppearance() + "/" + findCardBean.getInterior());
                if (TextUtils.isEmpty(findCardBean.getLicenseLocation())) {
                    tvSpd.setText("不限");
                } else {
                    tvSpd.setText(findCardBean.getLicenseLocation());
                }
                if (TextUtils.isEmpty(findCardBean.getArea())) {
                    tvQyyq.setText("不限");
                } else {
                    tvQyyq.setText(findCardBean.getArea());
                }
                if (TextUtils.isEmpty(findCardBean.getMileage())) {
                    tvGls.setText("不限");
                } else {
                    tvGls.setText(findCardBean.getMileage());
                }
                tvTcjl.setText(findCardBean.getPickCarDistance());
                if (TextUtils.isEmpty(findCardBean.getYear())) {
                    tvCl.setText("不限");
                } else {
                    tvCl.setText(findCardBean.getYear());
                }
//        holder.setText(R.id.tv_date, DateParserHelper.getYearAndMonthAndDay2(findCardBean.getCreation()));
                tvPrice.setText(StringFormat.doubleFormatForW2(findCardBean.getMinPrice()) + "-" + StringFormat.doubleFormatForW2(findCardBean.getMaxPrice()));
                if (!TextUtils.isEmpty(findCardBean.getNote())) {
                    etCk.setText(findCardBean.getNote());
                }
            }
        });
    }

    @Override
    public int getLayoutId() {
        return R.layout.act_find_car_details;
    }

    @OnClick({R.id.rl_company, R.id.btn_share, R.id.btn_link})
    public void onClick(View view) {
        if (findCardBean == null) return;
        switch (view.getId()) {
            case R.id.rl_company:
                if (findCardBean.getCompany() == null) {
                    return;
                }
                Intent intent = new Intent();
                intent.putExtra(Constant.KEY_BEAN, findCardBean.getCompany());
                intent.putExtra(Constant.KEY_POSITION, 1);
                ActivityManage.push(CompanyInfoAct.class, intent);
                break;
            case R.id.btn_share:
                break;
            case R.id.btn_link:
                OtherLogic.callPhone(FindCarDetailsAct.this, "联系电话",
                        "买家电话：" + findCardBean.getUser().getMobile() + "\n联系客服 " + Constant.SERVICE_PHONE, findCardBean.getUser().getMobile());
                break;
        }
    }
}
