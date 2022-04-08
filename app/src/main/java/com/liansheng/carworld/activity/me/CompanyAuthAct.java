package com.liansheng.carworld.activity.me;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.amap.api.services.core.PoiItem;
import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.liansheng.carworld.R;
import com.liansheng.carworld.activity.BaseActivity;
import com.liansheng.carworld.activity.home.ChooseAreaActivity;
import com.liansheng.carworld.activity.logic.OtherLogic;
import com.liansheng.carworld.bean.company.SearchCompanyBean;
import com.liansheng.carworld.bean.company.SearchCompanyBean2;
import com.liansheng.carworld.bean.me.AddressBean;
import com.liansheng.carworld.bean.me.CompanyBean;
import com.liansheng.carworld.bean.me.ReqBean;
import com.liansheng.carworld.bean.other.PageBean;
import com.liansheng.carworld.kit.Constant;
import com.liansheng.carworld.net.JsonCallback;
import com.liansheng.carworld.net.NetApi;
import com.liansheng.carworld.net.UrlKit;
import com.liansheng.carworld.oss.OssServiceUtil;
import com.liansheng.carworld.utils.StringUtil;
import com.liansheng.carworld.utils.Utils;
import com.liansheng.carworld.view.SuccessDialog;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.entity.LocalMedia;
import com.qmuiteam.qmui.skin.QMUISkinManager;
import com.qmuiteam.qmui.util.QMUIDisplayHelper;
import com.qmuiteam.qmui.widget.popup.QMUIPopup;
import com.qmuiteam.qmui.widget.popup.QMUIPopups;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.droidlover.xdroid.tools.utils.ActivityManage;
import cn.droidlover.xdroid.tools.utils.Util;
import cn.droidlover.xdroid.views.textView.NoDoubleClickTextView;
import okhttp3.Call;

public class CompanyAuthAct extends BaseActivity {

    @BindView(R.id.et_name)
    EditText etName;
    @BindView(R.id.et_company_pp)
    EditText etBrand;
    @BindView(R.id.tv_address)
    TextView tvAddress;
    @BindView(R.id.submit)
    NoDoubleClickTextView submit;
    @BindView(R.id.tv_company_type)
    TextView tv_company_type;
    @BindView(R.id.iv_yyzz)
    ImageView ivYyzz;
    @BindView(R.id.iv_yyzz_zt)
    ImageView ivYyzzZt;
    @BindView(R.id.iv_mt)
    ImageView ivMt;
    @BindView(R.id.iv_mt_zt)
    ImageView ivMtZt;
    @BindView(R.id.tv_yyzz_bh)
    TextView tvYyzzBh;
    @BindView(R.id.company_list)
    RecyclerView companyList;
    private TxtAdapter txtAdapter;
    public int type = 1;//图片位置
    public String yyzz = "";
    public String mt = "";
    private PoiItem poiItemStart;
    private boolean isAuth;//是否存在
    private List<CompanyBean> companyBeans = new ArrayList<>();
    private String[] cars = {"汽贸店-主营二手车", "汽贸店-主营新车", "4S店", "物流公司"};
    private OptionsPickerView carTypes;
    private String carType;
    private SuccessDialog dialog;
    private boolean sendHand;
    //    private QMUIPopup mNormalPopup;
    private String selectContent;


    @Override
    public void initData(Bundle savedInstanceState) {
        dialog = new SuccessDialog(context);
        dialog.setMessage("您已提交认证，请稍等\n客服会尽快审核")
                .setOnDismissListener(dialog1 -> {
                    setResult(RESULT_OK);
                    finish();
                });
        carTypes = new OptionsPickerBuilder(context, (options1, options2, options3, v) -> {
            if (options1 == 0 || options1 == 1) {
                sendHand = (options2 == 0);
                carType = Constant.STORE_TYPE_1;
                findViewById(R.id.llyt_type).setVisibility(View.GONE);
                ivYyzz.setVisibility(View.VISIBLE);
                ivMt.setVisibility(View.VISIBLE);
                findViewById(R.id.ll_tp).setVisibility(View.VISIBLE);
            } else if (options1 == 2) {
                carType = Constant.STORE_TYPE_2;
                findViewById(R.id.llyt_type).setVisibility(View.VISIBLE);
                ivYyzz.setVisibility(View.GONE);
                ivMt.setVisibility(View.GONE);
                findViewById(R.id.ll_tp).setVisibility(View.GONE);
            } else if (options1 == 3) {
                carType = Constant.STORE_TYPE_3;
                findViewById(R.id.llyt_type).setVisibility(View.GONE);
                ivMt.setVisibility(View.GONE);
                ivYyzz.setVisibility(View.VISIBLE);
                findViewById(R.id.ll_tp).setVisibility(View.VISIBLE);
            }
            tv_company_type.setText(cars[options1]);
        })
                .setDividerColor(ContextCompat.getColor(context, R.color.line_grey))
                .setTextColorCenter(ContextCompat.getColor(context, R.color.main_normal_color))
                .setContentTextSize(16)
                .build();
        carTypes.setPicker(Arrays.asList(cars));
        etName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (TextUtils.isEmpty(s)) {
                    return;
                }
                if (StringUtil.isChinese(s.toString()) && s.length() > 1) {
                    if (s.toString().equals(selectContent)) {
                        return;
                    }
                    company(s.toString());
                }
            }
        });
        companyList.setLayoutManager(new LinearLayoutManager(this));
        txtAdapter = new TxtAdapter();
        companyList.setAdapter(txtAdapter);
        txtAdapter.setOnItemClickListener((adapter, view, position) -> {
            selectContent = adapter.getItem(position).toString();
            etName.setText(adapter.getItem(position).toString());
            companyList.setVisibility(View.GONE);
        });
    }

    private void company(String name) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("com", name);
        map.put("page", 1);
        map.put("query", "all");
        NetApi.get(UrlKit.APP_RESOURCE_ENTERPRISE, map, new JsonCallback() {

            @Override
            public void onSuccess(String response, int id) {
                SearchCompanyBean2 beans = new Gson().fromJson(response, new TypeToken<SearchCompanyBean2>() {
                }.getType());
                if (beans != null && beans.getItems().size() > 0) {
                    show(beans.getItems());
                }
            }
        });
    }

    @Override
    public int getLayoutId() {
        return R.layout.act_company_auth;
    }

    @OnClick({R.id.tv_address, R.id.tv_company_type, R.id.iv_yyzz, R.id.iv_mt, R.id.submit})
    public void onViewClick(View view) {
        switch (view.getId()) {
            case R.id.tv_address:
                Intent intent = new Intent();
                intent.putExtra("type", "start");
                ActivityManage.push(ChooseAreaActivity.class, intent, 1000);
                break;
            case R.id.tv_company_type:
                Util.hideKeyBoard(view);
                carTypes.show();
                break;
            case R.id.iv_yyzz:
                type = 3;//图片位置
                Utils.getPhoto(this);
                break;
            case R.id.iv_mt:
                type = 4;//图片位置
                Utils.getPhoto(this);
                break;
            case R.id.submit:
                String name = etName.getText().toString();
                if (TextUtils.isEmpty(name)) {
                    Utils.showToast(etName.getHint().toString());
                    return;
                }
                if (name.length() < 7) {
                    Util.toast("企业名称不正确");
                    return;
                }
                if (poiItemStart == null) {
                    Util.toast("请选择公司地址");
                    return;
                }
                if (Constant.STORE_TYPE_1.equals(carType)) {
                    if (TextUtils.isEmpty(yyzz)) {
                        Util.toast("请选择营业执照正面");
                        return;
                    }
                    if (TextUtils.isEmpty(mt)) {
                        Util.toast("请选择门头照片");
                        return;
                    }
                } else if (Constant.STORE_TYPE_2.equals(carType)) {
                    String brand = etBrand.getText().toString();
                    if (TextUtils.isEmpty(brand)) {
                        Util.toast("请输入主营品牌");
                        return;
                    }
                } else if (Constant.STORE_TYPE_3.equals(carType)) {
                    if (TextUtils.isEmpty(yyzz)) {
                        Util.toast("请选择营业执照正面");
                        return;
                    }
                }
                auth();
                break;
        }
    }

    private long getCompanyId() {
        if (companyBeans != null && companyBeans.size() > 0) {
            for (int i = 0; i < companyBeans.size(); i++) {
                CompanyBean bean = companyBeans.get(i);
                return bean.getId();
            }
        }
        return 0;
    }

    private String getCompanyBean() {
        ReqBean reqBean = new ReqBean();
        List<CompanyBean> list = new ArrayList<>();
        CompanyBean companyBean = new CompanyBean();
        AddressBean addressBean = new AddressBean();
        addressBean.setLongitude(String.valueOf(poiItemStart.getLatLonPoint().getLongitude()));
        addressBean.setLatitude(String.valueOf(poiItemStart.getLatLonPoint().getLatitude()));
        addressBean.setProvince(poiItemStart.getProvinceName());
        addressBean.setCity(poiItemStart.getCityName());
        addressBean.setCounty(poiItemStart.getAdName());
        addressBean.setStreet(poiItemStart.getSnippet());
        addressBean.setName(poiItemStart.getTitle());
        companyBean.setAddress(addressBean);
        companyBean.setName(etName.getText().toString());
        companyBean.setType(carType);
        if (Constant.STORE_TYPE_1.equals(carType)) {
            companyBean.setSecondHandPriority(sendHand);
        }
        if (getCompanyId() != 0) {
            companyBean.setId(getCompanyId());
        }
        companyBean.setBusinessLicense(yyzz);
        companyBean.setFrontDoorPhoto(mt);
        companyBean.setMainBrand(etBrand.getText().toString());
        list.add(companyBean);
        reqBean.setItems(list);
        return new Gson().toJson(reqBean);
    }

    private void auth() {
        NetApi.put(UrlKit.USER_COMPANYS, getCompanyBean(), new JsonCallback() {

            @Override
            public void onSuccess(String response, int id) {
                if (!dialog.isShowing()) {
                    dialog.show();
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && requestCode == 1000) {
            if (data.getParcelableExtra("start") != null) {
                poiItemStart = data.getParcelableExtra("start");
                tvAddress.setText(poiItemStart.getProvinceName() + poiItemStart.getCityName() + poiItemStart.getAdName() + poiItemStart.getTitle());
            }
        } else if (resultCode == RESULT_OK && requestCode == PictureConfig.CHOOSE_REQUEST) {
            String path = PictureSelector.obtainMultipleResult(data).get(0).getCompressPath();
            OssServiceUtil.getInstance().asyncPutImage(OtherLogic.getImgUrl(), path, (objectResult, currentPath) -> {
                if (type == 3) {
                    yyzz = currentPath;
                    Glide.with(context).load(UrlKit.getImgUrl(currentPath)).into(ivYyzz);
                } else if (type == 4) {
                    mt = currentPath;
                    Glide.with(context).load(UrlKit.getImgUrl(currentPath)).into(ivMt);
                }
            });
        }
    }

    public void show(List<SearchCompanyBean2.ItemsBean> list) {
        List<String> data = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            data.add(list.get(i).getCompanyName());
        }
        txtAdapter.replaceData(data);
        companyList.setVisibility(View.VISIBLE);
    }

    private class TxtAdapter extends BaseQuickAdapter<String, BaseViewHolder> {
        public TxtAdapter() {
            super(R.layout.item_txt);
        }

        @Override
        protected void convert(@NotNull BaseViewHolder baseViewHolder, String s) {
            baseViewHolder.setText(R.id.txt, s);
        }
    }
}
