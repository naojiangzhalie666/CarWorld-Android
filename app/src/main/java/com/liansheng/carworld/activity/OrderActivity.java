package com.liansheng.carworld.activity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatCheckBox;

import com.amap.api.services.core.PoiItem;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.kongzue.dialog.interfaces.OnDialogButtonClickListener;
import com.kongzue.dialog.util.BaseDialog;
import com.kongzue.dialog.v3.CustomDialog;
import com.kongzue.dialog.v3.MessageDialog;
import com.liansheng.carworld.R;
import com.liansheng.carworld.activity.home.ChooseAreaActivity;
import com.liansheng.carworld.activity.logic.OtherLogic;
import com.liansheng.carworld.activity.me.CouponAct;
import com.liansheng.carworld.bean.Distance;
import com.liansheng.carworld.bean.OrderId;
import com.liansheng.carworld.bean.me.AddressBean;
import com.liansheng.carworld.bean.me.CouponBean;
import com.liansheng.carworld.bean.other.PageBean;
import com.liansheng.carworld.kit.Constant;
import com.liansheng.carworld.net.JsonCallback;
import com.liansheng.carworld.net.NetApi;
import com.liansheng.carworld.net.UrlKit;
import com.liansheng.carworld.utils.XToastUtils;
import com.liansheng.carworld.view.MyDialog;
import com.qmuiteam.qmui.skin.QMUISkinManager;
import com.qmuiteam.qmui.util.QMUIDisplayHelper;
import com.qmuiteam.qmui.widget.popup.QMUIPopup;
import com.qmuiteam.qmui.widget.popup.QMUIPopups;
import com.xgr.alipay.alipay.AliPay;
import com.xgr.alipay.alipay.AlipayInfoImpli;
import com.xgr.easypay.EasyPay;
import com.xgr.easypay.callback.IPayCallback;
import com.xuexiang.xui.widget.button.switchbutton.SwitchButton;
import com.xuexiang.xui.widget.toast.XToast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.droidlover.xdroid.hold.info.SharedInfo;
import cn.droidlover.xdroid.tools.utils.ActivityManage;
import okhttp3.Call;

public class OrderActivity extends BaseActivity {

    @BindView(R.id.tv_start)
    TextView tvStart;
    @BindView(R.id.llyt_start)
    LinearLayout llytStart;
    @BindView(R.id.tv_end)
    TextView tvEnd;
    @BindView(R.id.llyt_end)
    LinearLayout llytEnd;
    @BindView(R.id.tv_distance)
    TextView tvDistance;
    @BindView(R.id.tv_time)
    TextView tvTime;
    @BindView(R.id.sw_time)
    SwitchButton swTime;
    @BindView(R.id.tv_car_type)
    TextView tvCarType;
    @BindView(R.id.et_car_type)
    EditText etCarType;
    @BindView(R.id.cb_gzc)
    AppCompatCheckBox cbGzc;
    @BindView(R.id.et_bz)
    EditText etBz;
    @BindView(R.id.et_fcr_name)
    EditText etFcrName;
    @BindView(R.id.et_fcr_mobile)
    EditText etFcrMobile;
    @BindView(R.id.et_fcr_wx)
    EditText etFcrWx;
    @BindView(R.id.tv_fcr_wx)
    TextView tvFcrWx;
    @BindView(R.id.et_jcr_name)
    EditText etJcrName;
    @BindView(R.id.et_jcr_mobile)
    EditText etJcrMobile;
    @BindView(R.id.et_jcr_wx)
    EditText etJcrWx;
    @BindView(R.id.tv_jcr_wx)
    TextView tvJcrWx;
    @BindView(R.id.sw_ycdk)
    SwitchButton swYcdk;
    @BindView(R.id.cb_xy)
    AppCompatCheckBox cbXy;
    @BindView(R.id.tv_price)
    TextView tvPrice;
    @BindView(R.id.tv_go)
    TextView tvGo;
    @BindView(R.id.tv_start_city)
    TextView tvStartCity;
    @BindView(R.id.tv_end_city)
    TextView tvEndCity;
    @BindView(R.id.tv_tk)
    TextView tvTk;
    @BindView(R.id.tv_address)
    TextView tvAddress;
    @BindView(R.id.tv_go_2)
    TextView tvGo2;
    @BindView(R.id.tv_yhq)
    TextView tv_yhq;
    @BindView(R.id.tv_coupon)
    TextView tv_coupon;

    public AddressBean poiItemStart = null;
    public AddressBean poiItemEnd = null;
    public String zcsj = "";
    public String zcsj2 = "";
    public String cx = "";
    public boolean cx2;
    public String bz = "";
    public String fcrxm = "";
    public String fcrdh = "";
    public String jcrxm = "";
    public String jcrdh = "";
    public boolean ycdk;
    public String price;
    public boolean isBigCar = false;
    public String isCheckCarPay = "false";
    private QMUIPopup mNormalPopup;
    private String xbcType = "";
    CouponBean couponBean;

    @Override
    public void initData(Bundle savedInstanceState) {
        Intent intent = getIntent();
        if (getLoginResult().getAddress() != null) {
            if (getLoginResult().getAddress().getName() != null && getLoginResult().getAddress().getLatitude() != null && getLoginResult().getAddress().getLongitude() != null) {
                poiItemEnd = new AddressBean(getLoginResult().getAddress().getName(), getLoginResult().getAddress().getLongitude(), getLoginResult().getAddress().getLatitude(),
                        getLoginResult().getAddress().getProvince(), getLoginResult().getAddress().getCity(), getLoginResult().getAddress().getCounty(), getLoginResult().getAddress().getStreet());
            }
        } else {
            showSetAddress();
        }
        if (intent.getParcelableExtra("start") != null) {
            poiItemStart = intent.getParcelableExtra("start");
        }
        if (intent.getParcelableExtra("end") != null) {
            poiItemEnd = intent.getParcelableExtra("end");
        }
        XToast.Config.get().setGravity(Gravity.CENTER);
        int type = getIntent().getIntExtra(Constant.KEY_TYPE, 0);
        if (type == -1) {
            isBigCar = true;
            tvCarType.setText("大板车");
        } else if (type == 0) {
            xbcType = "";
            tvCarType.setText("不限");
        } else if (type == 1) {
            xbcType = "BlueOblique";
            tvCarType.setText("蓝牌斜板");
        } else if (type == 2) {
            xbcType = "BlueGround";
            tvCarType.setText("蓝牌落地板");
        } else if (type == 3) {
            xbcType = "YellowGround";
            tvCarType.setText("黄牌落地板");
        } else if (type == 4) {
            xbcType = "Five";
            tvCarType.setText("5吨板");
        } else if (type == 5) {
            xbcType = "Box";
            tvCarType.setText("厢式车");
        }
        initView();
        getCoupon();
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_order;
    }

    private void initView() {
        if (poiItemStart != null) {
            tvStart.setText(poiItemStart.getName());
            tvStartCity.setText(poiItemStart.getCity() + poiItemStart.getCounty());
        }
        if (poiItemEnd != null) {
            tvEnd.setText(poiItemEnd.getName());
            tvEndCity.setText(poiItemEnd.getCity() + poiItemEnd.getCounty());
        }
        hasDistance();
        tvTk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                toActivity(AboutActivity.class, null);
                Intent intent = new Intent();
                intent.putExtra(Constant.KEY_TITLE, "运输服务协议");
                intent.putExtra(Constant.KEY_URL, UrlKit.H5_TRANSPORT_SERVICE);
                ActivityManage.push(WebViewAct.class, intent);
            }
        });
        if (getLoginResult().getMobile() != null) {
            etFcrMobile.setText(getLoginResult().getMobile());
        }

    }

    public void hasDistance() {
        if (poiItemStart != null && poiItemEnd != null) {
            getDistance();
        }
    }

    private void getCoupon() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("expired", false);
        map.put("used", false);
        NetApi.get(UrlKit.USER_COUPON, map, new JsonCallback() {

            @Override
            public void onSuccess(String response, int id) {
                PageBean<CouponBean> data = new Gson().fromJson(response, new TypeToken<PageBean<CouponBean>>() {
                }.getType());
                if(data!=null&&data.getItems()!=null&&data.getItems().size()>0){
                    tv_yhq.setText("您有优惠券未使用");
                }else {
                    tv_yhq.setText("您暂无优惠券可使用");
                }
            }
        });
    }

    public void getDistance() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("origin.longitude", poiItemStart.getLongitude());
        map.put("origin.latitude", poiItemStart.getLatitude());
        map.put("destination.longitude", poiItemEnd.getLongitude());
        map.put("destination.latitude", poiItemEnd.getLatitude());
        map.put("checkCarPay", isCheckCarPay);
        map.put("bigCar", isBigCar);
        map.put("truckRequire", xbcType);
        NetApi.get(UrlKit.GET_DISTANCE, map, new JsonCallback() {

            @Override
            public void onSuccess(String response, int id) {
                Gson gson = new Gson();
                Distance distance = gson.fromJson(response, Distance.class);
                tvDistance.setText(distance.getDistance() / 1000 + "KM");
                price = distance.getPrice() + "";
                if (couponBean == null) {
                    tv_yhq.setText("");
                    tvPrice.setText("￥" + price + "元");
                    tv_coupon.setVisibility(View.GONE);
                } else {
                    tv_coupon.setVisibility(View.VISIBLE);
                    tv_coupon.setText("（已优惠" + couponBean.getDenomination() + "元）");
                    tv_yhq.setText("立减" + couponBean.getDenomination());
                    tvPrice.setText("￥" + (distance.getPrice() - couponBean.getDenomination()) + "元");
                }
            }
        });
    }

    public void getDate(Integer year, Integer monthOfYear, Integer dayOfMonth) {
        tvTime.setText(String.format("%d-%d-%d", year, (monthOfYear + 1), dayOfMonth));
        zcsj = tvTime.getText().toString();
    }

    public void validate() {
        if (poiItemStart == null) {
            showToast("请选择出发地");
            return;
        }
        if (poiItemEnd == null) {
            showToast("请选择目的地");
            return;
        }
        if (zcsj.equals("")) {
            showToast("请选择装车时间");
            return;
        } else {
            zcsj = tvTime.getText().toString();
        }
        if (swTime.isChecked()) {
            zcsj2 = "afternoon";
//            showToast("下午");
        } else {
            zcsj2 = "morning";
//            showToast("上午");
        }
        if (etCarType.getText().toString().equals("")) {
            showToast("请输入车型");
            return;
        } else {
            cx = etCarType.getText().toString();
        }
        if (cbGzc.isChecked()) {
            cx2 = true;
        } else {
            cx2 = false;
        }
        bz = etBz.getText().toString();
        if (etFcrName.getText().toString().length() < 1) {
            showToast("请输入发车人姓名");
            return;
        } else {
            fcrxm = etFcrName.getText().toString();
        }
        if (etFcrMobile.getText().toString().length() < 1) {
            showToast("请输入发车人电话");
            return;
        } else {
            if (isMobilPhone(etFcrMobile.getText().toString())) {
                fcrdh = etFcrMobile.getText().toString();
            } else {
                showToast("发车人手机号格式不正确");
                return;
            }
        }
//        fcrwx = etFcrWx.getText().toString();
        if (etJcrName.getText().toString().length() < 1) {
            showToast("请输入接车人姓名");
            return;
        } else {
            jcrxm = etJcrName.getText().toString();
        }
        if (etJcrMobile.getText().toString().length() < 1) {
            showToast("请输入接车人电话");
            return;
        } else {
            if (isMobilPhone(etJcrMobile.getText().toString())) {
                jcrdh = etJcrMobile.getText().toString();
            } else {
                showToast("接车人手机号格式不正确");
                return;
            }
        }
//        jcrwx = etJcrWx.getText().toString();
        if (swYcdk.isChecked()) {
            ycdk = true;
        } else {
            ycdk = false;
        }
        if (!cbXy.isChecked()) {
            showToast("请同意运输服务协议");
            return;
        }
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("origin.name", poiItemStart.getName());
        hashMap.put("origin.longitude", poiItemStart.getLongitude() + "");
        hashMap.put("origin.latitude", poiItemStart.getLatitude() + "");
        hashMap.put("destination.name", poiItemEnd.getName());
        hashMap.put("destination.longitude", poiItemEnd.getLongitude() + "");
        hashMap.put("destination.latitude", poiItemEnd.getLatitude() + "");
        hashMap.put("startDate.date", zcsj);
        hashMap.put("startDate.timeSlot", zcsj2);
        hashMap.put("carModel.model", cx);
        hashMap.put("carModel.fault", cx2 + "");
        hashMap.put("note", bz);
        hashMap.put("originContacts.name", fcrxm);
        hashMap.put("originContacts.mobile", fcrdh);
        hashMap.put("destinationContacts.name", jcrxm);
        hashMap.put("destinationContacts.mobile", jcrdh);
        hashMap.put("checkCarPay", ycdk + "");
        hashMap.put("truckRequire", xbcType);
        hashMap.put("bigCar", isBigCar);
        if (couponBean != null) {
            hashMap.put("couponId", couponBean.getId());
        }
        NetApi.post(UrlKit.TRANSPORT_ORDER, hashMap, new JsonCallback() {

            @Override
            public void onSuccess(String response, int id) {
//                Util.toast("下单成功");
                Gson gson = new Gson();
                OrderId order = gson.fromJson(response, OrderId.class);
//                showPaydialog(order.getOrderId());
                MyDialog myDialog = new MyDialog(OrderActivity.this);
                myDialog.setCancelable(false);
                myDialog.setTitle("您已成功下单，请您稍等\n司机会尽快接单\n客服电话：" + Constant.SERVICE_PHONE).setCancel("", new MyDialog.OnCancelListtener() {
                    @Override
                    public void onCancel(MyDialog myDialog) {
                        finish();
                    }
                }).setConfirm("查看订单", new MyDialog.OnConfirmListtener() {
                    @Override
                    public void onConfirm(MyDialog myDialog) {
                        finish();
                        Bundle bundle = new Bundle();
                        bundle.putString("id", order.getOrderId());
                        toActivity(OrderDetailsActivity.class, bundle);
                    }
                }).show();
            }
        });
    }

    @OnClick({R.id.llyt_start, R.id.llyt_end, R.id.tv_time, R.id.tv_fcr_wx, R.id.tv_jcr_wx, R.id.sw_ycdk, R.id.tv_yhq, R.id.tv_go, R.id.tv_go_2, R.id.tv_address})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.llyt_start:
//                toActivity(ChooseLocationActivity2.class,null);
                Intent intent = new Intent();
                intent.putExtra(Constant.KEY_TYPE, "start");
                intent.putExtra(Constant.KEY_TITLE, "出发地");
                ActivityManage.push(ChooseAreaActivity.class, intent, 1000);
                break;
            case R.id.llyt_end:
                Intent intent2 = new Intent();
                intent2.putExtra(Constant.KEY_TYPE, "end");
                intent2.putExtra(Constant.KEY_TITLE, "目的地");
//                startActivityForResult(intent2, 2000);
                ActivityManage.push(ChooseAreaActivity.class, intent2, 2000);
                break;
            case R.id.tv_time:
                DatePickerDialog pickerDialog = new DatePickerDialog(context
                        , R.style.MyDatePickerDialogTheme
                        , (View, year, monthOfYear, dayOfMonth) -> getDate(year, monthOfYear, dayOfMonth)
                        , Calendar.getInstance().get(Calendar.YEAR)
                        , Calendar.getInstance().get(Calendar.MONTH)
                        , Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
                DatePicker datePicker = pickerDialog.getDatePicker();
                datePicker.setMinDate(System.currentTimeMillis());
                long time = 1000L * 60L * 60L * 24L * 30L;
                datePicker.setMaxDate(System.currentTimeMillis() + time);
                pickerDialog.show();
                break;
            case R.id.tv_fcr_wx:
                etFcrWx.setText(etFcrMobile.getText().toString());
                break;
            case R.id.tv_jcr_wx:
                etJcrWx.setText(etJcrMobile.getText().toString());
                break;
            case R.id.sw_ycdk:
                if (swYcdk.isChecked()) {
                    isCheckCarPay = "true";
                } else {
                    isCheckCarPay = "false";
                }
                if (poiItemStart != null && poiItemEnd != null) {
                    getDistance();
                }
//                int total = 0;
//                if (swYcdk.isChecked()) {
//                    total = Integer.parseInt(price) + 100;
//                    tvPrice.setText("￥" + total + "元");
//                    price = total + "";
//                } else {
//                    total = Integer.parseInt(price) - 100;
//                    tvPrice.setText("￥" + total + "元");
//                    price = total + "";
//                }
                break;
            case R.id.tv_yhq:
                Intent intent1 = new Intent();
                intent1.putExtra(Constant.KEY_TYPE, 2);
                ActivityManage.push(CouponAct.class, intent1, 1001);
                break;
            case R.id.tv_go:
                validate();
                break;
            case R.id.tv_go_2:
                validate();
                break;
            case R.id.tv_address:
                getAddress();
                break;
        }
    }

    public void getAddress() {
        NetApi.getRecnent(SharedInfo.getInstance().getValue(Constant.KEY_TOKEN, "").toString(), new JsonCallback() {

            @Override
            public void onSuccess(String response, int id) {
                Gson gson = new Gson();
                List<AddressBean> address = gson.fromJson(response, new TypeToken<List<AddressBean>>() {
                }.getType());
                if (address.size() > 0) {
                    showAddress(address);
                } else {
                    showToast("暂无常用目的地");
                }
            }
        });
    }

    public void showAddress(List<AddressBean> address) {
        List<String> data = new ArrayList<>();
        for (int i = 0; i < address.size(); i++) {
            if (address.get(i).getCity().equals("")) {
                data.add(address.get(i).getProvince() + address.get(i).getCounty() + address.get(i).getStreet());
            } else {
                data.add(address.get(i).getCity() + address.get(i).getCounty() + address.get(i).getStreet());
            }
        }
        ArrayAdapter adapter = new ArrayAdapter(this, R.layout.simple_list_item, data);
        AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                poiItemEnd = new AddressBean(address.get(i).getName(), address.get(i).getLongitude(), address.get(i).getLatitude(), address.get(i).getProvince(), address.get(i).getCity(), address.get(i).getCounty(), address.get(i).getStreet());
                tvEnd.setText(poiItemEnd.getName());
                tvEndCity.setText(poiItemEnd.getCity() + poiItemEnd.getCounty());
                hasDistance();
                if (mNormalPopup != null) {
                    mNormalPopup.dismiss();
                }
            }
        };
        mNormalPopup = QMUIPopups.listPopup(this,
                QMUIDisplayHelper.dp2px(this, 300),
                QMUIDisplayHelper.dp2px(this, 250),
                adapter,
                onItemClickListener)
                .animStyle(QMUIPopup.ANIM_GROW_FROM_CENTER)
                .preferredDirection(QMUIPopup.DIRECTION_BOTTOM)
                .shadow(true)
                .offsetYIfTop(QMUIDisplayHelper.dp2px(this, 10))
                .skinManager(QMUISkinManager.defaultInstance(this))
                .show(tvAddress);
    }

    public void showPaydialog(String orderId) {
        CustomDialog.build(this, R.layout.dialog_pay, new CustomDialog.OnBindView() {
            @Override
            public void onBind(final CustomDialog dialog, View v) {
                Button btnOk = v.findViewById(R.id.btn_ok);
                LinearLayout ll = v.findViewById(R.id.llyt_alipay);
                ll.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showToast("alipay");
                    }
                });
                btnOk.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        goAliPay(orderId);
                        dialog.doDismiss();
                    }
                });
            }
        })
                .setFullScreen(true)
                .show();
    }

    public void goAliPay(String orderId) {
        NetApi.getAliPay(getLoginResult().getToken(), orderId, new JsonCallback() {

            @Override
            public void onSuccess(String response, int id) {
                AliPay aliPay = new AliPay();
                //构造支付宝订单实体。一般都是由服务端直接返回。
                AlipayInfoImpli alipayInfoImpli = new AlipayInfoImpli();
                alipayInfoImpli.setOrderInfo(response);
                //策略场景类调起支付方法开始支付，以及接收回调。
                EasyPay.pay(aliPay, context, alipayInfoImpli, new IPayCallback() {
                    @Override
                    public void success() {
//                            showToast("支付成功");
                        showSuccessDialog(orderId);
                    }

                    @Override
                    public void failed(int code, String msg) {
                        showToast(msg);
                    }

                    @Override
                    public void cancel() {
                        showToast("支付取消");
                        finish();
                        Bundle bundle = new Bundle();
                        bundle.putString("id", orderId);
                        toActivity(OrderDetailsActivity.class, bundle);

                    }
                });

            }
        });
    }

    public void showSuccessDialog(String OrderId) {
        MyDialog myDialog = new MyDialog(OrderActivity.this);
        myDialog.setTitle("您已成功下单，请您稍等\n司机会尽快接单\n客服电话：" + Constant.SERVICE_PHONE).setCancel("", new MyDialog.OnCancelListtener() {
            @Override
            public void onCancel(MyDialog myDialog) {
                finish();
            }
        }).setConfirm("查看订单", new MyDialog.OnConfirmListtener() {
            @Override
            public void onConfirm(MyDialog myDialog) {
                finish();
                Bundle bundle = new Bundle();
                bundle.putString("id", OrderId);
                toActivity(OrderDetailsActivity.class, bundle);
            }
        }).show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == 1000) {
                if (data.getParcelableExtra("start") != null) {
                    PoiItem result = (PoiItem) data.getParcelableExtra("start");
                    poiItemStart = new AddressBean(result.getTitle(), String.valueOf(result.getLatLonPoint().getLongitude()), String.valueOf(result.getLatLonPoint().getLatitude()), result.getProvinceName(), result.getCityName(), result.getAdName(), result.getSnippet());
                    tvStart.setText(result.getTitle());
                    tvStartCity.setText(result.getCityName() + result.getAdName());
                    hasDistance();
                }
            } else if (requestCode == 2000) {
                if (data.getParcelableExtra("end") != null) {
                    PoiItem result = (PoiItem) data.getParcelableExtra("end");
                    poiItemEnd = new AddressBean(result.getTitle(), String.valueOf(result.getLatLonPoint().getLongitude()), String.valueOf(result.getLatLonPoint().getLatitude()), result.getProvinceName(), result.getCityName(), result.getAdName(), result.getSnippet());
                    tvEnd.setText(result.getTitle());
                    tvEndCity.setText(result.getCityName() + result.getAdName());
                    hasDistance();
                }
            } else if (requestCode == 1001) {
                couponBean = data.getParcelableExtra(Constant.KEY_BEAN);
                if (couponBean != null) {
                    tv_yhq.setText("立减" + couponBean.getDenomination());
                    if (!TextUtils.isEmpty(price)) {
                        tvPrice.setText("￥" + (Integer.valueOf(price) - couponBean.getDenomination()) + "元");
                        tv_coupon.setText("（已优惠" + couponBean.getDenomination() + "元）");
                        tv_coupon.setVisibility(View.VISIBLE);
                    } else {
                        tv_coupon.setVisibility(View.GONE);
                    }
                }
            }
        }
    }

    public void showSetAddress() {
        if ((Boolean) SharedInfo.getInstance().getValue(UrlKit.FIRST_RESULT, true)) {
            MessageDialog.build(OrderActivity.this)
                    .setTitle("温馨提示")
                    .setMessage("完善常用地址信息后下单更方便，是否要进行完善？")
                    .setCancelButton("稍后完善", new OnDialogButtonClickListener() {
                        @Override
                        public boolean onClick(BaseDialog baseDialog, View v) {
                            return false;
                        }
                    })
                    .setOkButton("前往完善", new OnDialogButtonClickListener() {
                        @Override
                        public boolean onClick(BaseDialog baseDialog, View v) {
                            ActivityManage.push(InfoActivity.class);
                            return false;
                        }
                    })
                    .show();
            SharedInfo.getInstance().saveValue(UrlKit.FIRST_RESULT, false);
        }
    }
}
