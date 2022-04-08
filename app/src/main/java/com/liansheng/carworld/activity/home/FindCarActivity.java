package com.liansheng.carworld.activity.home;

import android.os.Bundle;
import android.text.InputFilter;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.google.gson.Gson;
import com.liansheng.carworld.R;
import com.liansheng.carworld.activity.BaseActivity;
import com.liansheng.carworld.bean.BrandSerie;
import com.liansheng.carworld.bean.CityBean;
import com.liansheng.carworld.bean.UserInfo;
import com.liansheng.carworld.bean.event.EventClose;
import com.liansheng.carworld.bean.home.ReqFindCardBean;
import com.liansheng.carworld.bean.me.CompanyBean;
import com.liansheng.carworld.kit.Constant;
import com.liansheng.carworld.net.JsonCallback;
import com.liansheng.carworld.net.NetApi;
import com.liansheng.carworld.net.UrlKit;
import com.liansheng.carworld.utils.GetJsonDataUtil;
import com.liansheng.carworld.utils.MoneyValueFilter;
import com.qmuiteam.qmui.skin.QMUISkinManager;
import com.qmuiteam.qmui.util.QMUIDisplayHelper;
import com.qmuiteam.qmui.widget.popup.QMUIPopup;
import com.qmuiteam.qmui.widget.popup.QMUIPopups;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.droidlover.xdroid.hold.info.SharedInfo;
import cn.droidlover.xdroid.tools.utils.Util;
import okhttp3.Call;
@Deprecated
public class FindCarActivity extends BaseActivity {

    @BindView(R.id.tv_cx)
    TextView tvCx;
    @BindView(R.id.tv_ys)
    EditText tvYs;
    @BindView(R.id.tv_ns)
    EditText tvNs;
    @BindView(R.id.tv_spd)
    TextView tvSpd;
    @BindView(R.id.tv_tcjl)
    TextView tvTcjl;
    @BindView(R.id.tv_qyyq)
    TextView tvQyyq;
    @BindView(R.id.tv_ccrq)
    TextView tvCcrq;
    @BindView(R.id.tv_cl)
    TextView tvCl;
    @BindView(R.id.tv_gls)
    TextView tvGls;
    @BindView(R.id.tv_qwje_min)
    EditText tvQwjeMin;
    @BindView(R.id.tv_qwje_max)
    EditText tvQwjeMax;
    @BindView(R.id.ck_water)
    CheckBox ckWater;
    @BindView(R.id.ck_fire)
    CheckBox ckFire;
    @BindView(R.id.ck_accident)
    CheckBox ckAccident;
    @BindView(R.id.ck_complete)
    CheckBox ckComplete;
    @BindView(R.id.tv_gs)
    TextView tvGs;
    @BindView(R.id.et_ck)
    EditText etCk;
    @BindView(R.id.iv_ys)
    ImageView ivYs;
    @BindView(R.id.iv_ns)
    ImageView ivNs;
    @BindView(R.id.btn_go)
    Button btnGo;
    //    private String price;
    private String years;
    private String mileage;
    private OptionsPickerView pvOptions;
    private OptionsPickerView gsOptions;
    private OptionsPickerView qyOptions;
    private OptionsPickerView glOptions;
    private OptionsPickerView rqOptions;
    private OptionsPickerView clsOptions;
    private OptionsPickerView glsOptions;
    private String[] jls = {"不限", "100公里内", "300公里内", "500公里内", "500公里以上"};
    private String[] rqs = {"不限", "1天", "3天", "7天"};
    private String[] gls = {"不限", "1万以内", "1-3万", "3-5万", "5-8万", "8-10万", "10万以上"};
    private String[] cls = {"不限", "1年以内", "1-3年", "3-5年", "5-8年", "8-10年", "10年以上"};
    private UserInfo userInfo;
    private List<CompanyBean> companys;
    private QMUIPopup mYsPopup;
    private QMUIPopup mNsPopup;
    private BrandSerie brandSerie;

    @Override
    public void initData(Bundle savedInstanceState) {
        initView();
    }

    private void initView() {
        brandSerie = getIntent().getParcelableExtra(Constant.KEY_BEAN);
        tvCx.setText(brandSerie.getName());
        companys = new ArrayList<>();
        userInfo = SharedInfo.getInstance().getEntity(UserInfo.class);
        for (int i = 0; i < userInfo.getCompanys().size(); i++) {
            CompanyBean bean = userInfo.getCompanys().get(i);
            if (Constant.ID_STATUS_PASSED.equals(bean.getStatus()) && !Constant.STORE_TYPE_3.equals(bean.getType())) {
                companys.add(bean);
            }
        }
        if (companys.size() > 0) {
            tvGs.setText(companys.get(0).getName());
        }
        gsOptions = new OptionsPickerBuilder(context, (options1, options2, options3, v) -> {
            tvGs.setText(companys.get(options1).getName());
        }).setDividerColor(ContextCompat.getColor(context, R.color.line_grey))
                .setTextColorCenter(ContextCompat.getColor(context, R.color.main_normal_color))
                .setContentTextSize(16)
                .build();
        gsOptions.setPicker(companys);
        new GetJsonDataUtil(new GetJsonDataUtil.SelectListener() {
            @Override
            public void success(ArrayList<CityBean> cityBeans, ArrayList<ArrayList<String>> city) {
                CityBean cityBean = new CityBean();
                cityBean.setProvince("不限");
                List<String> list = new ArrayList<>();
                list.add("不限");
                cityBean.setCity_list(list);
                cityBeans.add(0, cityBean);
                ArrayList<String> citys = new ArrayList<>();
                citys.add("不限");
                city.add(0, citys);
                pvOptions = new OptionsPickerBuilder(context, new OnOptionsSelectListener() {
                    @Override
                    public void onOptionsSelect(int options1, int options2, int options3, View v) {
                        //返回的分别是三个级别的选中位置
                        String city = cityBeans.get(options1).getCity_list().get(options2);
                        tvSpd.setText(city);
                    }
                })
                        .setDividerColor(ContextCompat.getColor(context, R.color.line_grey))
                        .setTextColorCenter(ContextCompat.getColor(context, R.color.main_normal_color)) //设置选中项文字颜色
                        .setContentTextSize(16)
                        .build();
                pvOptions.setPicker(cityBeans, city);//二级选择器（市区）
                pvOptions.setSelectOptions(11, 5);
                qyOptions = new OptionsPickerBuilder(context, new OnOptionsSelectListener() {
                    @Override
                    public void onOptionsSelect(int options1, int options2, int options3, View v) {
                        //返回的分别是三个级别的选中位置
                        String city = cityBeans.get(options1).getCity_list().get(options2);
                        tvQyyq.setText(city);
                    }
                })
                        .setDividerColor(ContextCompat.getColor(context, R.color.line_grey))
                        .setTextColorCenter(ContextCompat.getColor(context, R.color.main_normal_color)) //设置选中项文字颜色
                        .setContentTextSize(16)
                        .build();
                qyOptions.setPicker(cityBeans, city);
                qyOptions.setSelectOptions(11, 5);
            }
        });
        glOptions = new OptionsPickerBuilder(context, new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                tvTcjl.setText(jls[options1]);
            }
        })
                .setDividerColor(ContextCompat.getColor(context, R.color.line_grey))
                .setTextColorCenter(ContextCompat.getColor(context, R.color.main_normal_color)) //设置选中项文字颜色
                .setContentTextSize(16)
                .build();
        glOptions.setPicker(Arrays.asList(jls));
        rqOptions = new OptionsPickerBuilder(context, new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                tvCcrq.setText(rqs[options1]);
            }
        })
                .setDividerColor(ContextCompat.getColor(context, R.color.line_grey))
                .setTextColorCenter(ContextCompat.getColor(context, R.color.main_normal_color)) //设置选中项文字颜色
                .setContentTextSize(16)
                .build();
        rqOptions.setPicker(Arrays.asList(rqs));
        clsOptions = new OptionsPickerBuilder(context, new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                tvCl.setText(cls[options1]);
                years = cls[options1];
            }
        })
                .setDividerColor(ContextCompat.getColor(context, R.color.line_grey))
                .setTextColorCenter(ContextCompat.getColor(context, R.color.main_normal_color)) //设置选中项文字颜色
                .setContentTextSize(16)
                .build();
        clsOptions.setPicker(Arrays.asList(cls));
        glsOptions = new OptionsPickerBuilder(context, new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                tvGls.setText(gls[options1]);
                mileage = gls[options1];
            }
        })
                .setDividerColor(ContextCompat.getColor(context, R.color.line_grey))
                .setTextColorCenter(ContextCompat.getColor(context, R.color.main_normal_color)) //设置选中项文字颜色
                .setContentTextSize(16)
                .build();
        glsOptions.setPicker(Arrays.asList(gls));
        tvQwjeMax.setFilters(new InputFilter[]{new MoneyValueFilter()});
        tvQwjeMin.setFilters(new InputFilter[]{new MoneyValueFilter()});
        initCarStyle();
    }

    private void initCarStyle() {
        List<String> data = Arrays.asList("不限", "自定义");
        ArrayAdapter adapter = new ArrayAdapter(this, R.layout.simple_list_item, data);
        ArrayAdapter adapter2 = new ArrayAdapter(this, R.layout.simple_list_item, data);
        AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 0) {
                    tvYs.setEnabled(false);
                    tvYs.setText("不限");
                } else if (i == 1) {
                    tvYs.setText("");
                    tvYs.setEnabled(true);
                    tvYs.requestFocus();
                }
                mYsPopup.dismiss();
            }
        };
        AdapterView.OnItemClickListener onItemClickListener2 = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 0) {
                    tvNs.setEnabled(false);
                    tvNs.setText("不限");
                } else if (i == 1) {
                    tvNs.setText("");
                    tvNs.setEnabled(true);
                    tvNs.requestFocus();
                }
                mNsPopup.dismiss();
            }
        };
        mYsPopup = QMUIPopups.listPopup(this,
                QMUIDisplayHelper.dp2px(this, 100),
                QMUIDisplayHelper.dp2px(this, 200),
                adapter, onItemClickListener)
                .animStyle(QMUIPopup.ANIM_GROW_FROM_CENTER)
                .preferredDirection(QMUIPopup.DIRECTION_BOTTOM)
                .shadow(true)
                .offsetYIfTop(QMUIDisplayHelper.dp2px(this, 1))
                .skinManager(QMUISkinManager.defaultInstance(this))
                .show(tvYs);
        mNsPopup = QMUIPopups.listPopup(this,
                QMUIDisplayHelper.dp2px(this, 100),
                QMUIDisplayHelper.dp2px(this, 200),
                adapter2, onItemClickListener2)
                .animStyle(QMUIPopup.ANIM_GROW_FROM_CENTER)
                .preferredDirection(QMUIPopup.DIRECTION_BOTTOM)
                .shadow(true)
                .offsetYIfTop(QMUIDisplayHelper.dp2px(this, 1))
                .skinManager(QMUISkinManager.defaultInstance(this))
                .show(tvNs);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_find_car;
    }

    @OnClick({R.id.tv_spd, R.id.tv_tcjl, R.id.tv_qyyq, R.id.tv_ccrq, R.id.tv_cl, R.id.tv_gls, R.id.tv_gs, R.id.iv_ys, R.id.iv_ns, R.id.btn_go})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_spd:
                if (pvOptions != null && !pvOptions.isShowing()) {
                    pvOptions.show();
                }
                break;
            case R.id.tv_tcjl:
                if (!glOptions.isShowing()) {
                    glOptions.show();
                }
                break;
            case R.id.tv_qyyq:
                if (qyOptions != null && !qyOptions.isShowing()) {
                    qyOptions.show();
                }
                break;
            case R.id.tv_ccrq:
                if (!rqOptions.isShowing()) {
                    rqOptions.show();
                }
                break;
            case R.id.tv_gs:
                gsOptions.show();
                break;
            case R.id.tv_cl:
                if (!clsOptions.isShowing()) {
                    clsOptions.show();
                }
                break;
            case R.id.tv_gls:
                if (!glsOptions.isShowing()) {
                    glsOptions.show();
                }
                break;
            case R.id.iv_ys:
                mYsPopup.show(view);
                break;
            case R.id.iv_ns:
                mNsPopup.show(view);
                break;
            case R.id.btn_go:
                String ys = tvYs.getText().toString();
                String ns = tvNs.getText().toString();
                String spd = tvSpd.getText().toString();
                String tcjl = tvTcjl.getText().toString();
                String qyyq = tvQyyq.getText().toString();
                String ccrq = tvCcrq.getText().toString();
                String qwjeMax = tvQwjeMax.getText().toString();
                String qwjeMin = tvQwjeMin.getText().toString();
                String ck = etCk.getText().toString();
                String gs = tvGs.getText().toString();
                if (TextUtils.isEmpty(ys)) {
                    Util.toast("请输入外观颜色");
                    return;
                }
                if (TextUtils.isEmpty(ns)) {
                    Util.toast("请输入内饰");
                    return;
                }
                if (TextUtils.isEmpty(spd)) {
                    Util.toast("请选择上牌地");
                    return;
                }
                if (TextUtils.isEmpty(tcjl)) {
                    Util.toast("请选择提车距离");
                    return;
                }
                if (TextUtils.isEmpty(qyyq)) {
                    Util.toast("请选择区域要求");
                    return;
                }
                if (TextUtils.isEmpty(ccrq)) {
                    Util.toast("请选择有效期");
                    return;
                }
                if (TextUtils.isEmpty(years)) {
                    Util.toast("请选择车龄");
                    return;
                }
                if (TextUtils.isEmpty(mileage)) {
                    Util.toast("请选择公里数");
                    return;
                }
                if (TextUtils.isEmpty(qwjeMin)) {
                    Util.toast("请输入最小期望价格");
                    return;
                }
                if (TextUtils.isEmpty(qwjeMax)) {
                    Util.toast("请输入最大期望价格");
                    return;
                }
                if (TextUtils.isEmpty(gs)) {
                    Util.toast("请选择已认证的公司");
                    return;
                }
                ReqFindCardBean cardBean = new ReqFindCardBean();
                cardBean.setBrand(brandSerie.getBrand());
                cardBean.setBrand_fct(brandSerie.getFct());
                cardBean.setBrand_serie(brandSerie.getName());
                cardBean.setAppearance(ys);
                cardBean.setInterior(ns);
                cardBean.setLicenseLocation(spd);
                cardBean.setPickCarDistance(tcjl);
                cardBean.setInWater(!ckWater.isChecked());
                cardBean.setInFire(!ckFire.isChecked());
                cardBean.setAccident(!ckAccident.isChecked());
                cardBean.setCompleted(!ckComplete.isChecked());
                if ("1天".equals(ccrq)) {
                    cardBean.setExpirationDay("OneDay");
                } else if ("3天".equals(ccrq)) {
                    cardBean.setExpirationDay("ThreeDay");
                } else if ("7天".equals(ccrq)) {
                    cardBean.setExpirationDay("SevenDay");
                } else {
                    cardBean.setExpirationDay(null);
                }
                if ("不限".equals(qyyq)) {
                    cardBean.setArea("");
                } else {
                    cardBean.setArea(qyyq);
                }
                if ("不限".equals(years)) {
                    cardBean.setYear("");
                } else {
                    cardBean.setYear(years);
                }
                if ("不限".equals(mileage)) {
                    cardBean.setMileage(null);
                } else {
                    cardBean.setMileage(mileage);
                }
                cardBean.setMinPrice((int) (Double.valueOf(qwjeMin) * 10000));
                cardBean.setMaxPrice((int) (Double.valueOf(qwjeMax) * 10000));
                if (!TextUtils.isEmpty(ck)) {
                    cardBean.setNote(ck);
                }
                for (int i = 0; i < companys.size(); i++) {
                    if (gs.equals(companys.get(i).getName())) {
                        cardBean.setUserCompanyId(String.valueOf(companys.get(i).getId()));
                    }
                }
                NetApi.postJson(UrlKit.CAR_RESOURCE_BRAND_ORDER, new Gson().toJson(cardBean), new JsonCallback() {

                    @Override
                    public void onSuccess(String response, int id) {
                        EventBus.getDefault().post(new EventClose());
                        Util.toast("发布成功");
                        finish();
                    }
                });
                break;
        }
    }
}
