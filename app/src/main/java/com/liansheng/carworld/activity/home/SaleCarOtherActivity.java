package com.liansheng.carworld.activity.home;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.baidu.ocr.ui.camera.CameraActivity;
import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.google.gson.Gson;
import com.kongzue.dialog.interfaces.OnDialogButtonClickListener;
import com.kongzue.dialog.util.BaseDialog;
import com.kongzue.dialog.v3.MessageDialog;
import com.liansheng.carworld.R;
import com.liansheng.carworld.activity.BaseActivity;
import com.liansheng.carworld.activity.logic.OtherLogic;
import com.liansheng.carworld.bean.CityBean;
import com.liansheng.carworld.bean.ScanVinBean;
import com.liansheng.carworld.bean.UserInfo;
import com.liansheng.carworld.bean.event.EventClose;
import com.liansheng.carworld.bean.home.CarDetailsBean;
import com.liansheng.carworld.bean.home.ImageUploadBean;
import com.liansheng.carworld.bean.home.ReqSaleCarBean;
import com.liansheng.carworld.bean.me.CompanyBean;
import com.liansheng.carworld.kit.Constant;
import com.liansheng.carworld.net.JsonCallback;
import com.liansheng.carworld.net.NetApi;
import com.liansheng.carworld.net.UrlKit;
import com.liansheng.carworld.oss.OssServiceUtil;
import com.liansheng.carworld.utils.DateParserHelper;
import com.liansheng.carworld.utils.GetJsonDataUtil;
import com.liansheng.carworld.utils.MoneyValueFilter;
import com.liansheng.carworld.utils.Utils;
import com.liansheng.carworld.view.GlideRoundTransform;
import com.liansheng.carworld.view.MyDialog;
import com.liansheng.carworld.view.pop.ViewShowPop;
import com.liansheng.carworld.wxapi.RecognizeService;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.entity.LocalMedia;

import org.greenrobot.eventbus.EventBus;
import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.droidlover.xdroid.hold.info.SharedInfo;
import cn.droidlover.xdroid.tools.utils.ActivityManage;
import cn.droidlover.xdroid.tools.utils.FileUtil;
import cn.droidlover.xdroid.tools.utils.NetworkUtil;
import cn.droidlover.xdroid.tools.utils.StringFormat;
import cn.droidlover.xdroid.tools.utils.Util;
import cn.droidlover.xdroid.views.appbar.ToolBar;
import okhttp3.Call;

public class SaleCarOtherActivity extends BaseActivity {

    private static final int REQUEST_CODE_VINCODE = 134;
    final int CAR_PICK = 1001;

    @BindView(R.id.toolbar)
    ToolBar toolBar;
    @BindView(R.id.tv_vin)
    TextView tvVin;
    @BindView(R.id.tv_njdq)
    TextView tvNjdq;
    @BindView(R.id.tv_qxdq)
    TextView tvQxdq;
    @BindView(R.id.tv_kcd)
    TextView tvKcd;
    @BindView(R.id.et_nbj)
    EditText etNbj;
    @BindView(R.id.et_lsj)
    EditText etLsj;
    @BindView(R.id.et_gh)
    EditText etGh;
    @BindView(R.id.ck_wb)
    Switch ckWb;
    @BindView(R.id.ck_pz)
    Switch ckPz;
    @BindView(R.id.iv_car_auth)
    ImageView iv_car_auth;
    @BindView(R.id.btn_go)
    Button btnGo;

    private OptionsPickerView pvOptions;
    private List<ImageUploadBean> uploadBeans = new ArrayList<>();
    private String carUrl = "";
    private ViewShowPop viewShowPop;
    private CarDetailsBean detailsBean;

    @Override
    public void initData(Bundle savedInstanceState) {
        detailsBean = getIntent().getParcelableExtra("details");
        if (detailsBean != null) {
            ckWb.setChecked(detailsBean.isShowMtnce());
            ckPz.setChecked(detailsBean.isShowInsurance());
            tvNjdq.setText(DateParserHelper.getYearAndMonthAndDay2(detailsBean.getYearlyInspectionExpiration()));
            tvQxdq.setText(DateParserHelper.getYearAndMonthAndDay2(detailsBean.getCompulsoryInsuranceExpiration()));
            tvVin.setText(detailsBean.getVin());
            if (detailsBean.getRetail_price() > 0) {
                etLsj.setText(String.valueOf(Double.valueOf(detailsBean.getRetail_price()) / 10000));
            }
            if (detailsBean.getInside_price() > 0) {
                etNbj.setText(String.valueOf(Double.valueOf(detailsBean.getInside_price()) / 10000));
            }
//            etLsj.setText(detailsBean.getSell_price());
            tvKcd.setText(detailsBean.getRegistration_city());
            if (detailsBean.getTransfer_times() > 0) {
                etGh.setText(String.valueOf(detailsBean.getTransfer_times()));
            }
            btnGo.setText("更新");
        }
        initView();
        viewShowPop = new ViewShowPop(this);
        etLsj.setFilters(new InputFilter[]{new MoneyValueFilter()});
        etNbj.setFilters(new InputFilter[]{new MoneyValueFilter()});
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_sale_car_other;
    }

    private void initView() {
        new GetJsonDataUtil(new GetJsonDataUtil.SelectListener() {
            @Override
            public void success(ArrayList<CityBean> cityBeans, ArrayList<ArrayList<String>> city) {
                pvOptions = new OptionsPickerBuilder(context, new OnOptionsSelectListener() {
                    @Override
                    public void onOptionsSelect(int options1, int options2, int options3, View v) {
                        //返回的分别是三个级别的选中位置
                        String city = cityBeans.get(options1).getCity_list().get(options2);
                        tvKcd.setText(city);
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
        uploadBeans.add(new ImageUploadBean());
    }

    @OnClick({R.id.iv_scan, R.id.tv_njdq, R.id.tv_qxdq,
            R.id.tv_kcd, R.id.iv_car_auth, R.id.btn_go, R.id.tv_tips})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_scan:
                Intent intent = new Intent(context, CameraActivity.class);
                intent.putExtra(CameraActivity.KEY_OUTPUT_FILE_PATH, FileUtil.getSaveFile(context).getAbsolutePath());
                intent.putExtra(CameraActivity.KEY_CONTENT_TYPE, CameraActivity.CONTENT_TYPE_GENERAL);
                startActivityForResult(intent, REQUEST_CODE_VINCODE);
                break;
            case R.id.tv_tips:
                if (viewShowPop.isShowing()) {
                    viewShowPop.dismiss();
                } else {
                    viewShowPop.showAtLocation(view, Gravity.CENTER, 0, 0);
                }
                break;
            case R.id.tv_njdq:
                new TimePickerBuilder(this, (date, v) -> tvNjdq.setText(new SimpleDateFormat("yyyy-MM-dd").format(date)))
                        .setType(new boolean[]{true, true, true, false, false, false})
                        .setItemVisibleCount(6)
                        .setLabel("年", "月", "日", "", "", "")
                        .build().show();
                break;
            case R.id.tv_qxdq:
                new TimePickerBuilder(this, (date, v) -> tvQxdq.setText(new SimpleDateFormat("yyyy-MM-dd").format(date)))
                        .setType(new boolean[]{true, true, true, false, false, false})
                        .setItemVisibleCount(6)
                        .setLabel("年", "月", "日", "", "", "")
                        .build().show();
                break;
            case R.id.tv_kcd:
                if (pvOptions != null) {
                    pvOptions.show();
                }
                break;
            case R.id.iv_car_auth:
                Utils.getPhoto(this, CAR_PICK);
                break;
            case R.id.btn_go:
                String njdq = tvNjdq.getText().toString();
                String qxdq = tvQxdq.getText().toString();
                String vin = tvVin.getText().toString();
                String spCity = tvKcd.getText().toString();
                String gh = etGh.getText().toString();
                String nbj = etNbj.getText().toString();
                String lsj = etLsj.getText().toString();
                ReqSaleCarBean carBean = getIntent().getParcelableExtra(Constant.KEY_BEAN);
                carBean.setRegistration_city(spCity);
                if (TextUtils.isEmpty(njdq)) {
                    carBean.setYearlyInspectionExpiration(null);
                } else {
                    carBean.setYearlyInspectionExpiration(njdq);
                }
                if (TextUtils.isEmpty(qxdq)) {
                    carBean.setCompulsoryInsuranceExpiration(null);
                } else {
                    carBean.setCompulsoryInsuranceExpiration(qxdq);
                }
                if (!TextUtils.isEmpty(gh)) {
                    carBean.setTransfer_times(Integer.valueOf(gh));
                }
                if (!TextUtils.isEmpty(lsj)) {
                    carBean.setRetail_price((int) (Double.valueOf(lsj) * 10000));
                }
                if (!TextUtils.isEmpty(nbj)) {
                    carBean.setInside_price((int) (Double.valueOf(nbj) * 10000));
                }
                carBean.setShowMtnce(ckWb.isChecked());
                carBean.setShowInsurance(ckPz.isChecked());
                carBean.setVin(vin);
                carBean.setCar_verify_image(carUrl);
                if (detailsBean != null) {
                    carBean.setId(detailsBean.getId());
                    NetApi.put(UrlKit.CAR_RESOURCE, new Gson().toJson(carBean), new JsonCallback() {

                        @Override
                        public void onSuccess(String response, int id) {
                            Util.toast("更新成功");
                            setResult(RESULT_OK);
                            finish();
                        }
                    });
                } else {
                    NetApi.postJson(UrlKit.CAR_RESOURCE, new Gson().toJson(carBean), new JsonCallback() {

                        @Override
                        public void onSuccess(String response, int id) {
                            if (carBean.isOff()) {
                                Util.toast("入库成功");
                            } else {
                                Util.toast("发布成功");
                            }
                            EventBus.getDefault().post(new EventClose());
                            setResult(RESULT_OK);
                            finish();
                        }
                    });
                }

                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && (requestCode == PictureConfig.CHOOSE_REQUEST || requestCode == CAR_PICK)) {
            String path = PictureSelector.obtainMultipleResult(data).get(0).getCompressPath();
            OssServiceUtil.getInstance().asyncPutImage(OtherLogic.getImgUrl(), path, (objectResult, currentPath) -> {
                carUrl = currentPath;
                Glide.with(ActivityManage.peek()).load(UrlKit.getImgUrl(carUrl)).into(iv_car_auth);
            });
        } else if (requestCode == REQUEST_CODE_VINCODE && resultCode == Activity.RESULT_OK) {
            // 识别成功回调，VIN码
            RecognizeService.recognizeVincode(context, FileUtil.getSaveFile(context).getAbsolutePath(),
                    result -> {
                        if (!TextUtils.isEmpty(result)) {
                            ScanVinBean vinBean = new Gson().fromJson(result, ScanVinBean.class);
                            if (vinBean != null && vinBean.getWords_result() != null && vinBean.getWords_result().size() > 0
                                    && !TextUtils.isEmpty(vinBean.getWords_result().get(0).getWords())) {
//                                search_vin_number.setText(vinBean.getWords_result().get(0).getWords());
                                tvVin.setText(vinBean.getWords_result().get(0).getWords());
                            }
                        }
                    });
        }
    }

}
