package com.liansheng.carworld.activity.home;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.kongzue.dialog.interfaces.OnDialogButtonClickListener;
import com.kongzue.dialog.util.BaseDialog;
import com.kongzue.dialog.v3.MessageDialog;
import com.liansheng.carworld.R;
import com.liansheng.carworld.activity.BaseActivity;
import com.liansheng.carworld.activity.logic.OtherLogic;
import com.liansheng.carworld.bean.BrandSerie;
import com.liansheng.carworld.bean.CityBean;
import com.liansheng.carworld.bean.UserInfo;
import com.liansheng.carworld.bean.home.CarDetailsBean;
import com.liansheng.carworld.bean.home.ImageUploadBean;
import com.liansheng.carworld.bean.home.ReqSaleCarBean;
import com.liansheng.carworld.kit.Constant;
import com.liansheng.carworld.net.UrlKit;
import com.liansheng.carworld.oss.OssServiceUtil;
import com.liansheng.carworld.utils.DateParserHelper;
import com.liansheng.carworld.utils.GetJsonDataUtil;
import com.liansheng.carworld.utils.MoneyValueFilter;
import com.liansheng.carworld.utils.Utils;
import com.liansheng.carworld.view.GlideRoundTransform;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.entity.LocalMedia;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.droidlover.xdroid.hold.info.SharedInfo;
import cn.droidlover.xdroid.tools.utils.ActivityManage;
import cn.droidlover.xdroid.tools.utils.NetworkUtil;
import cn.droidlover.xdroid.tools.utils.Util;
import cn.droidlover.xdroid.views.appbar.ToolBar;

public class SaleCarActivity extends BaseActivity {

    private static final int REQUEST_CODE_VINCODE = 134;
    final int CAR_PICK = 1001;

    @BindView(R.id.toolbar)
    ToolBar toolBar;
    @BindView(R.id.tv_xcj)
    TextView tvXcj;
    @BindView(R.id.tv_sprq)
    TextView tvSprq;
    @BindView(R.id.ll_pf)
    RelativeLayout llPf;
    @BindView(R.id.ll_pl)
    RelativeLayout llPl;
    @BindView(R.id.ll_xh)
    LinearLayout llXh;
    @BindView(R.id.tv_cx)
    TextView tvCx;
    @BindView(R.id.tv_pl)
    TextView tvPl;
    @BindView(R.id.tv_pf)
    TextView tvPf;
    @BindView(R.id.et_xh)
    EditText etXh;
    @BindView(R.id.tv_wg)
    TextView tvWg;
    @BindView(R.id.tv_ns)
    TextView tvNs;
    @BindView(R.id.tv_szd)
    TextView tv_szd;
    @BindView(R.id.et_gls)
    EditText etGls;
//    @BindView(R.id.et_lsj)
//    EditText etLsj;
    @BindView(R.id.et_csjg)
    EditText etCsjg;
    @BindView(R.id.ck_yj)
    CheckBox ckYj;
    @BindView(R.id.ck_dj)
    CheckBox ckDj;
    @BindView(R.id.ck_water)
    CheckBox ckWater;
    @BindView(R.id.ck_fire)
    CheckBox ckFire;
    @BindView(R.id.ck_accident)
    CheckBox ckAccident;
    @BindView(R.id.ck_complete)
    CheckBox ckComplete;
    @BindView(R.id.et_ck)
    EditText etCk;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.tv_bsx)
    TextView tvBsx;

    private String price;
    private OptionsPickerView pvOptions;
    private MyAdapter myAdapter;
    private int index = 0;
    private List<ImageUploadBean> uploadBeans = new ArrayList<>();
    private boolean saleType;//false 上架 true 入库
    CarDetailsBean detailsBean;
    private BrandSerie mBrandSerie;
    private String brand;
    private String brand_fct;
    private String serie;

    @Override
    public void initData(Bundle savedInstanceState) {
        initView();
        toolBar.setLeftListener(v -> {
            if (detailsBean != null) {
                finish();
                return;
            }
            MessageDialog.build(this)
                    .setTitle("温馨提示")
                    .setMessage("车辆信息未发布，是否退出？")
                    .setCancelButton("取消", new OnDialogButtonClickListener() {
                        @Override
                        public boolean onClick(BaseDialog baseDialog, View v) {
                            return false;
                        }
                    })
                    .setOkButton("确定", new OnDialogButtonClickListener() {
                        @Override
                        public boolean onClick(BaseDialog baseDialog, View v) {
                            finish();
                            return false;
                        }
                    })
                    .show();
        });
    }


    @Override
    public int getLayoutId() {
        return R.layout.activity_sale_car;
    }

    private void initView() {
        saleType = getIntent().getBooleanExtra(Constant.KEY_TYPE, false);
        uploadBeans.add(new ImageUploadBean());
        myAdapter = new MyAdapter(uploadBeans);
        Object obj = getIntent().getParcelableExtra(Constant.KEY_BEAN);
        if (obj instanceof BrandSerie) {
            mBrandSerie = (BrandSerie) obj;
        } else {
            detailsBean = (CarDetailsBean) obj;
        }
        if (detailsBean != null) {
            brand = detailsBean.getBrand();
            brand_fct = detailsBean.getBrand_fct();
            serie = detailsBean.getBrand_serie();
            price = String.valueOf(detailsBean.getGuidance_price());
            tvSprq.setText(DateParserHelper.getYearAndMonthAndDay2(detailsBean.getRegistration_date()));
//            if (detailsBean.getRetail_price() > 0) {
//                etLsj.setText(String.valueOf(detailsBean.getRetail_price()));
//            }
//            carBean.setModel(cx);
            if ("新能源".equals(detailsBean.getModel())) {
                llXh.setVisibility(View.VISIBLE);
                llPl.setVisibility(View.GONE);
                llPf.setVisibility(View.GONE);
                etXh.setText(String.valueOf(detailsBean.getEndurance()));
                tvCx.setText("新能源");
            } else {
                tvCx.setText("燃油/混动");
                llXh.setVisibility(View.GONE);
                llPl.setVisibility(View.VISIBLE);
                llPf.setVisibility(View.VISIBLE);
                tvPl.setText(detailsBean.getVolume());
                tvPf.setText(detailsBean.getStandard());
            }
            String[] split = detailsBean.getColor().split("/");
            if (split.length == 2) {
                tvWg.setText(split[0]);
                tvNs.setText(split[1]);
            }
            tv_szd.setText(detailsBean.getLocation());
            etGls.setText(String.valueOf(Double.valueOf(detailsBean.getMileage()) / 10000));
            etCk.setText(detailsBean.getNote());
            for (int i = 0; i < detailsBean.getImages().size(); i++) {
                changeImage(index, detailsBean.getImages().get(i));
                index++;
            }
            ckWater.setChecked(!detailsBean.isInWater());
            ckFire.setChecked(!detailsBean.isInFire());
            ckAccident.setChecked(!detailsBean.isAccident());
            ckComplete.setChecked(!detailsBean.isCompleted());
            etCsjg.setText(String.valueOf(Double.valueOf(detailsBean.getSell_price()) / 10000));
        } else {
            brand = mBrandSerie.getBrand();
            brand_fct = mBrandSerie.getFct();
            serie = mBrandSerie.getName();
            price = mBrandSerie.getPrice();
            if (mBrandSerie.getLiter() > 0) {
                tvCx.setText("燃油/混动");
                llXh.setVisibility(View.GONE);
                llPl.setVisibility(View.VISIBLE);
                llPf.setVisibility(View.VISIBLE);
                if ("0".equals(mBrandSerie.getLiter_type())) {
                    tvPl.setText(mBrandSerie.getLiter() + "L");
                } else {
                    tvPl.setText(mBrandSerie.getLiter() + "T");
                }
                tvPf.setText(mBrandSerie.getDischarge_standard());
            } else {
                tvCx.setText("新能源");
                llXh.setVisibility(View.VISIBLE);
                llPl.setVisibility(View.GONE);
                llPf.setVisibility(View.GONE);
            }
            if ("1".equals(mBrandSerie.getGear_type())) {
                tvBsx.setText("手动");
            } else {
                tvBsx.setText("自动");
            }
        }
        if (serie.contains(brand_fct)) {
            toolBar.setTitle(serie);
        } else {
            toolBar.setTitle(brand_fct + " " + serie);
        }
        tvXcj.setText(price + "元");
        new GetJsonDataUtil(new GetJsonDataUtil.SelectListener() {
            @Override
            public void success(ArrayList<CityBean> cityBeans, ArrayList<ArrayList<String>> city) {
                pvOptions = new OptionsPickerBuilder(context, new OnOptionsSelectListener() {
                    @Override
                    public void onOptionsSelect(int options1, int options2, int options3, View v) {
                        //返回的分别是三个级别的选中位置
                        String city = cityBeans.get(options1).getCity_list().get(options2);
                        tv_szd.setText(city);
                    }
                })
                        .setDividerColor(ContextCompat.getColor(context, R.color.line_grey))
                        .setTextColorCenter(ContextCompat.getColor(context, R.color.main_normal_color))
                        .setContentTextSize(16)
                        .build();
                pvOptions.setPicker(cityBeans, city);//二级选择器（市区）
                pvOptions.setSelectOptions(10, 5);
            }
        });
        myAdapter.addChildClickViewIds(R.id.item_img_close, R.id.item_img_add);
        myAdapter.setOnItemChildClickListener((adapter, view, position) -> {
            ImageUploadBean item = (ImageUploadBean) adapter.getItem(position);
            switch (view.getId()) {
                case R.id.item_img_add:
                    index = position;
                    if (TextUtils.isEmpty(item.getUrl())) {
                        Utils.getPhoto(context, PictureConfig.MULTIPLE, 12 - myAdapter.getData().size() + 1);
                    }
                    break;
                case R.id.item_img_close:
                    adapter.remove(position);
                    if (adapter.getData().size() == 11) {
                        ImageUploadBean uploadBean = (ImageUploadBean) adapter.getData().get(adapter.getData().size() - 1);
                        if (!TextUtils.isEmpty(uploadBean.getUrl())) {
                            adapter.addData(new ImageUploadBean());
                        }
                    }
                    break;
            }
        });
        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        recyclerView.setAdapter(myAdapter);
        etGls.setFilters(new InputFilter[]{new MoneyValueFilter()});
        etCsjg.setFilters(new InputFilter[]{new MoneyValueFilter()});
//        etLsj.setFilters(new InputFilter[]{new MoneyValueFilter()});
        ckYj.setOnCheckedChangeListener((buttonView, isChecked) -> {
            ckDj.setChecked(!isChecked);
        });
        ckDj.setOnCheckedChangeListener((buttonView, isChecked) -> {
            ckYj.setChecked(!isChecked);
        });
    }


    private void changeImage(int position, String url) {
        List<ImageUploadBean> data = myAdapter.getData();
        for (int i = 0; i < data.size(); i++) {
            if (position == i) {
                ImageUploadBean uploadBean = data.get(i);
                uploadBean.setUrl(url);
                if (data.size() != 12)
                    data.add(new ImageUploadBean());
            }
        }
        myAdapter.replaceData(data);
    }

    private void setDate(TextView textView, int year, int monthOfYear, int dayOfMonth) {
        String month = String.valueOf(monthOfYear + 1);
        if (monthOfYear < 10) {
            month = "0" + (monthOfYear + 1);
        }
        String day = String.valueOf(dayOfMonth);
        if (dayOfMonth < 10) {
            day = "0" + dayOfMonth;
        }
        textView.setText(String.format("%d-%s-%s", year, month, day));
    }

    @OnClick({R.id.tv_sprq,
            R.id.tv_szd, R.id.btn_go})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_sprq:
                new TimePickerBuilder(this, new OnTimeSelectListener() {
                    @Override
                    public void onTimeSelect(Date date, View v) {
                        tvSprq.setText(new SimpleDateFormat("yyyy-MM-dd").format(date));
                    }
                })
                        .setType(new boolean[]{true, true, true, false, false, false})
                        .setItemVisibleCount(6)
                        .setRangDate(null, Calendar.getInstance())
                        .setLabel("年", "月", "日", "", "", "")
                        .build().show();
//                DatePickerDialog pickerDialog = new DatePickerDialog(context
//                        , R.style.MyDatePickerDialogTheme
//                        , (View, year, monthOfYear, dayOfMonth) -> {
//                    setDate(tvSprq, year, monthOfYear, dayOfMonth);
//                }
//                        , Calendar.getInstance().get(Calendar.YEAR)
//                        , Calendar.getInstance().get(Calendar.MONTH)
//                        , Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
//                DatePicker datePicker = pickerDialog.getDatePicker();
//                datePicker.setMaxDate(System.currentTimeMillis());
//                pickerDialog.show();
                break;
            case R.id.tv_szd:
                if (pvOptions != null) {
                    pvOptions.show();
                }
                break;
            case R.id.btn_go:
                List<ImageUploadBean> data = myAdapter.getData();
                List<String> imgs = new ArrayList<>();
                for (int i = 0; i < data.size(); i++) {
                    if (!TextUtils.isEmpty(data.get(i).getUrl())) {
                        imgs.add(data.get(i).getUrl());
                    }
                }
                String sprq = tvSprq.getText().toString();
                String pf = tvPf.getText().toString();
                String pl = tvPl.getText().toString();
                String cx = tvCx.getText().toString();
                String city = tv_szd.getText().toString();
                String wg = tvWg.getText().toString();
                String ns = tvNs.getText().toString();
//                String lsj = etLsj.getText().toString();
                String csjg = etCsjg.getText().toString();
                String ck = etCk.getText().toString();
                String gls = etGls.getText().toString();
                String xh = etXh.getText().toString();
                String bsx = tvBsx.getText().toString();
                if (TextUtils.isEmpty(sprq)) {
                    Util.toast("请选择上牌日期");
                    return;
                }
                if (TextUtils.isEmpty(gls)) {
                    Util.toast("请输入公里数");
                    return;
                }
                if (Double.valueOf(gls) < 0.1) {
                    Util.toast("公里数不小于0.1万公里");
                    return;
                }
                if ("新能源".equals(cx) && TextUtils.isEmpty(xh)) {
                    Util.toast("请输入续航");
                    return;
                }
                if (TextUtils.isEmpty(city)) {
                    Util.toast("请选择所在地");
                    return;
                }
                if (TextUtils.isEmpty(wg)) {
                    Util.toast("请填写外观");
                    return;
                }
                if (TextUtils.isEmpty(ns)) {
                    Util.toast("请填写内饰");
                    return;
                }
//                if (TextUtils.isEmpty(lsj)) {
//                    Util.toast("请填写零售价");
//                    return;
//                }
                if (TextUtils.isEmpty(csjg)) {
                    Util.toast("请填写批发价");
                    return;
                }
                if (TextUtils.isEmpty(ck)) {
                    Util.toast("请填写车况详情");
                    return;
                }
                if (imgs.size() == 0) {
                    Util.toast("请上传车辆照片");
                    return;
                }
                ReqSaleCarBean carBean = new ReqSaleCarBean();
                carBean.setBrand(brand);
                carBean.setBrand_fct(brand_fct);
                carBean.setBrand_serie(serie);
                carBean.setRegistration_date(sprq);
                carBean.setModel(cx);
                if ("新能源".equals(cx)) {
                    carBean.setEndurance(Integer.parseInt(xh));
                } else {
                    carBean.setVolume(pl);
                    carBean.setStandard(pf);
                }
                if (ckYj.isChecked()) {
                    carBean.setNegotiatedPrice("可议价");
                } else {
                    carBean.setNegotiatedPrice("底价");
                }
                carBean.setLocation(city);
                carBean.setColor(wg + "/" + ns);
                carBean.setMileage((int) (Double.valueOf(gls) * 10000));
                carBean.setGuidance_price(Integer.valueOf(price));
//                carBean.setRetail_price((int) (Double.valueOf(lsj) * 10000));
                carBean.setSell_price((int) (Double.valueOf(csjg) * 10000));
                carBean.setNote(ck);
                carBean.setImages(imgs);
                carBean.setInWater(!ckWater.isChecked());
                carBean.setInFire(!ckFire.isChecked());
                carBean.setGearbox(bsx);
                carBean.setAccident(!ckAccident.isChecked());
                carBean.setCompleted(!ckComplete.isChecked());
                carBean.setOff(saleType);
                UserInfo userInfo = SharedInfo.getInstance().getEntity(UserInfo.class);
                if (userInfo.getCompanys().size() > 0) {
                    carBean.setUserCompanyId(String.valueOf(userInfo.getCompanys().get(0).getId()));
                }
                if (carBean.getSell_price() > carBean.getGuidance_price()) {
                    MessageDialog.build(this)
                            .setTitle("温馨提示")
//                            .setMessage("您输入的批发价：" + carBean.getSell_price() + "元\n" + "超过指导价：" + carBean.getGuidance_price() + "元\n是否继续？")
                            .setMessage("您的批发价输入超出正常范围值")
                            .setCancelButton("取消", new OnDialogButtonClickListener() {
                                @Override
                                public boolean onClick(BaseDialog baseDialog, View v) {
                                    return false;
                                }
                            })
                            .setOkButton("继续", new OnDialogButtonClickListener() {
                                @Override
                                public boolean onClick(BaseDialog baseDialog, View v) {
                                    next(carBean);
                                    return false;
                                }
                            })
                            .show();
                } else {
                    next(carBean);
                }
                break;
        }
    }

    private void next(ReqSaleCarBean carBean) {
        Intent intent = new Intent();
        if (detailsBean != null) {
            intent.putExtra("details", detailsBean);
        }
        intent.putExtra(Constant.KEY_BEAN, carBean);
        ActivityManage.push(SaleCarOtherActivity.class, intent, 1005);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && (requestCode == PictureConfig.CHOOSE_REQUEST || requestCode == CAR_PICK)) {
            List<LocalMedia> selectList = PictureSelector.obtainMultipleResult(data);
            if (selectList != null && selectList.size() > 0) {
                List<String> strings = new ArrayList<>();
                for (int i = 0; i < selectList.size(); i++) {
                    strings.add(selectList.get(i).getCompressPath());
                }
                upload(strings);
            }
        } else if (resultCode == RESULT_OK && requestCode == 1005) {
            finish();
        }
    }

    public void upload(List<String> urls) {
        NetworkUtil.showCutscenes("正在上传...", "剩余" + urls.size() + "张");
        OssServiceUtil.getInstance().asyncPutImage(OtherLogic.getImgUrl(), urls.get(0), (objectResult, currentPath) -> {
            changeImage(index, currentPath);
            index++;
            urls.remove(0);
            if (urls.size() > 0) {
                upload(urls);
            } else {
                NetworkUtil.dismissCutscenes();
            }
        });
    }

    private class MyAdapter extends BaseQuickAdapter<ImageUploadBean, BaseViewHolder> {

        public MyAdapter(List<ImageUploadBean> imgs) {
            super(R.layout.item_img, imgs);
        }

        @Override
        protected void convert(@NotNull BaseViewHolder holder, ImageUploadBean item) {
            ImageView view = holder.getView(R.id.item_img);
            if (TextUtils.isEmpty(item.getUrl())) {
                holder.setGone(R.id.item_img_close, true);
                view.setVisibility(View.GONE);
            } else {
                holder.setGone(R.id.item_img_close, false);
                view.setVisibility(View.VISIBLE);
                Glide.with(ActivityManage.peek()).load(UrlKit.getImgUrl(item.getUrl())).transform(new GlideRoundTransform()).into(view);
            }

        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            if (detailsBean != null) {
                finish();
                return true;
            }
            MessageDialog.build(this)
                    .setTitle("温馨提示")
                    .setMessage("车辆信息未发布，是否退出？")
                    .setCancelButton("取消", new OnDialogButtonClickListener() {
                        @Override
                        public boolean onClick(BaseDialog baseDialog, View v) {
                            return false;
                        }
                    })
                    .setOkButton("确定", new OnDialogButtonClickListener() {
                        @Override
                        public boolean onClick(BaseDialog baseDialog, View v) {
                            finish();
                            return false;
                        }
                    })
                    .show();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
