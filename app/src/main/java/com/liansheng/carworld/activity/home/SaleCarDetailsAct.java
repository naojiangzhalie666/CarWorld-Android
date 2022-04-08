package com.liansheng.carworld.activity.home;

import android.content.Intent;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.view.Gravity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.kongzue.dialog.interfaces.OnDialogButtonClickListener;
import com.kongzue.dialog.util.BaseDialog;
import com.kongzue.dialog.v3.MessageDialog;
import com.liansheng.carworld.R;
import com.liansheng.carworld.activity.BaseActivity;
import com.liansheng.carworld.activity.WebViewAct;
import com.liansheng.carworld.activity.logic.OtherLogic;
import com.liansheng.carworld.activity.logic.UserLogic;
import com.liansheng.carworld.activity.me.InfoAuthAct;
import com.liansheng.carworld.adapter.CarDetailsAdapter;
import com.liansheng.carworld.adapter.RecyclerViewBannerAdapter;
import com.liansheng.carworld.bean.Message;
import com.liansheng.carworld.bean.UserInfo;
import com.liansheng.carworld.bean.company.SearchReportBean;
import com.liansheng.carworld.bean.event.EventShare;
import com.liansheng.carworld.bean.home.CarDetailsBean;
import com.liansheng.carworld.bean.home.SaleCardBean;
import com.liansheng.carworld.bean.other.PageBean;
import com.liansheng.carworld.kit.Constant;
import com.liansheng.carworld.net.JsonCallback;
import com.liansheng.carworld.net.NetApi;
import com.liansheng.carworld.net.UrlKit;
import com.liansheng.carworld.utils.DateParserHelper;
import com.liansheng.carworld.utils.GlideEngine;
import com.liansheng.carworld.view.ShareDialog;
import com.liansheng.carworld.view.pop.PriceSharePop;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.entity.LocalMedia;
import com.xuexiang.xui.widget.banner.recycler.BannerLayout;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.droidlover.xdroid.hold.info.SharedInfo;
import cn.droidlover.xdroid.tools.utils.ActivityManage;
import cn.droidlover.xdroid.tools.utils.ContextHolder;
import cn.droidlover.xdroid.tools.utils.StringFormat;
import cn.droidlover.xdroid.tools.utils.Util;
import cn.droidlover.xdroid.views.appbar.TitleBar;
import cn.droidlover.xdroid.views.appbar.ToolBar;

public class SaleCarDetailsAct extends BaseActivity implements BannerLayout.OnBannerItemClickListener {

    //    @BindView(R.id.topbar)
//    QMUITopBar topbar;
    @BindView(R.id.toolbar)
    ToolBar mToolbar;
    @BindView(R.id.bl_horizontal)
    BannerLayout blHorizontal;
    @BindView(R.id.tv_jg)
    TextView tvJg;
    @BindView(R.id.tv_xcjg)
    TextView tvXcjg;
    @BindView(R.id.tv_gls)
    TextView tvGls;
    @BindView(R.id.tv_sprq)
    TextView tvSprq;
    @BindView(R.id.tv_njdq)
    TextView tvNjdq;
    @BindView(R.id.tv_qxdq)
    TextView tvQxdq;
    @BindView(R.id.tv_pl)
    TextView tvPl;
    @BindView(R.id.tv_car_type)
    TextView tvCarType;
    @BindView(R.id.tv_cx)
    TextView tvCx;
    @BindView(R.id.tv_pzd)
    TextView tvPzd;
    @BindView(R.id.tv_ys)
    TextView tvYs;
    @BindView(R.id.tv_gh)
    TextView tvGh;
    //    @BindView(R.id.tv_scrq)
//    TextView tvScrq;
    @BindView(R.id.tv_kcd)
    TextView tvKcd;
    @BindView(R.id.tv_sjsj)
    TextView tvSjsj;
    @BindView(R.id.tv_bsx)
    TextView tvBsx;
    @BindView(R.id.tv_xxck)
    TextView tvXxck;
    @BindView(R.id.tv_company)
    TextView tv_company;
    @BindView(R.id.tv_name)
    TextView tv_name;
    @BindView(R.id.list_command)
    RecyclerView listCommand;
    @BindView(R.id.list_car_recycle)
    RecyclerView listCarRecycle;
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
    @BindView(R.id.tv_wbxx)
    TextView tv_wbxx;
    @BindView(R.id.tv_pzxx)
    TextView tv_pzxx;
    @BindView(R.id.tv_collection)
    TextView tv_collection;
    @BindView(R.id.iv_kf)
    TextView tvKf;
    @BindView(R.id.car_type)
    TextView carType;

    private RecyclerViewBannerAdapter mBannerAdapter;
    private List<String> urls;
    CarDetailsBean carDetailsBean;
    private OptionsPickerView<String> pvOptions;
    private List<String> datas = Arrays.asList("车源已售", "成交下架", "非他一手", "车型有误", "车况不符", "价格不实", "其他");
    private PriceSharePop sharePop;
    private boolean isOnly;
    private SaleCardBean bean;
    private MyAdapter myAdapter;

    @Override
    public void initData(Bundle savedInstanceState) {
        EventBus.getDefault().register(this);
        bean = getIntent().getParcelableExtra(Constant.KEY_BEAN);
        listCommand.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        getCommand();
        pvOptions = new OptionsPickerBuilder(context, new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                //返回的分别是三个级别的选中位置
                subError(datas.get(options1));
            }
        })
                .setDividerColor(ContextCompat.getColor(context, R.color.line_grey))
                .setTextColorCenter(ContextCompat.getColor(context, R.color.main_normal_color))
                .setContentTextSize(16)
                .build();
        pvOptions.setPicker(datas);
        sharePop = new PriceSharePop(context);
        LinearLayoutManager manager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        listCarRecycle.setLayoutManager(manager);
        myAdapter = new MyAdapter(null);
        listCarRecycle.setAdapter(myAdapter);
        Spannable span = new SpannableString(tvKf.getText());
        span.setSpan(new AbsoluteSizeSpan(24), 5, 16, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        tvKf.setText(span);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getDetails();
    }


    private void subError(String content) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("id", bean.getId());
        map.put("content", content);
        NetApi.put(UrlKit.CAR_RESOURCE_CORRECT, map, new JsonCallback() {
            @Override
            public void onSuccess(String response, int id) {
                if (response.contains("code") && response.contains("message")) {
                    return;
                }
                Util.toast("提交成功");
            }
        });
    }

    private void collection() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("id", bean.getId());
        NetApi.post(UrlKit.CAR_RESOURCE_COLLECTION, map, new JsonCallback() {
            @Override
            public void onSuccess(String response, int id) {
                if (response.contains("code") && response.contains("message")) {
                    return;
                }
                Util.toast("收藏成功");
                getDetails();
            }
        });
    }

    private void cancelCollection() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("id", bean.getId());
        NetApi.delete(UrlKit.CAR_RESOURCE_COLLECTION, map, new JsonCallback() {

            @Override
            public void onSuccess(String response, int id) {
                Util.toast("取消成功");
                getDetails();
            }
        });
    }

    private void getDetails() {
        NetApi.get(UrlKit.CAR_RESOURCE_DETAILS + bean.getId(), null, new JsonCallback() {

            @Override
            public void onSuccess(String response, int id) {
                carDetailsBean = new Gson().fromJson(response, CarDetailsBean.class);
                if (carDetailsBean.isShowMtnce()) {
                    tv_wbxx.setVisibility(View.VISIBLE);
                }
                if (carDetailsBean.isShowInsurance()) {
                    tv_pzxx.setVisibility(View.VISIBLE);
                }
                Drawable upDra;
                if (carDetailsBean.isCollected()) {
                    upDra = ContextHolder.getContext().getDrawable(R.mipmap.ic_collection_show);
                } else {
                    upDra = ContextHolder.getContext().getDrawable(R.mipmap.icon_collection);
                }
                if (TextUtils.isEmpty(carDetailsBean.getNegotiatedPrice())) {
                    carType.setText("可议价");
                } else {
                    carType.setText(carDetailsBean.getNegotiatedPrice());
                }
                if (TextUtils.isEmpty(carDetailsBean.getGearbox())) {
                    tvBsx.setText("未填写");
                } else {
                    tvBsx.setText(carDetailsBean.getGearbox());
                }
                upDra.setBounds(0, 0, upDra.getIntrinsicWidth(), upDra.getIntrinsicHeight());
                tv_collection.setCompoundDrawables(null, upDra, null, null);
                if(carDetailsBean.getCompany()!=null){
                    tv_company.setText(carDetailsBean.getCompany().getName());
                }
                if(carDetailsBean.getIdentityCard()!=null){
                    tv_name.setText("发布者：" + carDetailsBean.getIdentityCard().getOwner());
                }
                tvSjsj.setText(DateParserHelper.getYearMonthAndDay(carDetailsBean.getUpdateDate()));
                if (carDetailsBean.isInWater() && carDetailsBean.isInFire() && carDetailsBean.isAccident() && carDetailsBean.isCompleted()) {
                    tvImportant.setText("重要信息：无");
                } else {
                    ckWater.setChecked(!carDetailsBean.isInWater());
                    ckFire.setChecked(!carDetailsBean.isInFire());
                    ckAccident.setChecked(!carDetailsBean.isAccident());
                    ckComplete.setChecked(!carDetailsBean.isCompleted());
                }
                if (getIntent().getBooleanExtra(Constant.KEY_TYPE, false) && !carDetailsBean.isReadOnly() && !mToolbar.hasAction()) {
                    mToolbar.addAction(new TitleBar.ImageAction(R.mipmap.ic_car_edit) {
                        @Override
                        public void performAction(View view) {
                            Intent intent = new Intent();
                            intent.putExtra(Constant.KEY_BEAN, carDetailsBean);
                            ActivityManage.push(SaleCarActivity.class, intent);
                        }
                    });
                }
                urls = carDetailsBean.getImages();
                mBannerAdapter = new RecyclerViewBannerAdapter(urls, carDetailsBean.getNumber());
                blHorizontal.setAdapter(mBannerAdapter);
                mBannerAdapter.setOnBannerItemClickListener(SaleCarDetailsAct.this::onItemClick);
                if (carDetailsBean.getBrand_serie().contains(carDetailsBean.getBrand_fct())) {
                    mToolbar.setTitle(carDetailsBean.getBrand_serie());
                } else {
                    mToolbar.setTitle(carDetailsBean.getBrand_fct() + " " + carDetailsBean.getBrand_serie());
                }
//                if(TextUtils.isEmpty(carDetailsBean.))
//                tvBsx
                tvCx.setText(carDetailsBean.getModel());
                if ("新能源".equals(carDetailsBean.getModel())) {
                    tvCarType.setText("续航");
                    tvPl.setText(carDetailsBean.getEndurance() + "公里");
                } else {
                    tvCarType.setText("排放/排量");
                    tvPl.setText(carDetailsBean.getStandard() + "/" + carDetailsBean.getVolume());
                }
                tvYs.setText(carDetailsBean.getColor());
                tvGls.setText(StringFormat.doubleFormatForW2(carDetailsBean.getMileage()) + "公里");
                if (0 == carDetailsBean.getTransfer_times()) {
                    tvGh.setText("未填写");
                } else {
                    tvGh.setText(String.valueOf(carDetailsBean.getTransfer_times()));
                }
                tvJg.setText(StringFormat.doubleFormatForW2(carDetailsBean.getSell_price()));
                tvXcjg.setText("新车价：" + StringFormat.doubleFormatForW2(carDetailsBean.getGuidance_price()));
                tvXcjg.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
                tvSprq.setText(DateParserHelper.getYearMonthAndDay(carDetailsBean.getRegistration_date()));
//        tvScrq.setText(DateParserHelper.getYearMonthAndDay(bean.getProduction_date()));
                if (TextUtils.isEmpty(carDetailsBean.getYearlyInspectionExpiration()) || "0001-01-01T00:00:00".equals(carDetailsBean.getYearlyInspectionExpiration())) {
                    tvNjdq.setText("未填写");
                } else {
                    tvNjdq.setText(DateParserHelper.getYearMonthAndDay(carDetailsBean.getYearlyInspectionExpiration()));
                }
                if (TextUtils.isEmpty(carDetailsBean.getCompulsoryInsuranceExpiration()) || "0001-01-01T00:00:00".equals(carDetailsBean.getCompulsoryInsuranceExpiration())) {
                    tvQxdq.setText("未填写");
                } else {
                    tvQxdq.setText(DateParserHelper.getYearMonthAndDay(carDetailsBean.getCompulsoryInsuranceExpiration()));
                }
                tvKcd.setText(carDetailsBean.getLocation());
                tvXxck.setText(carDetailsBean.getNote());
                if (TextUtils.isEmpty(carDetailsBean.getRegistration_city())) {
                    tvPzd.setText("未填写");
                } else {
                    tvPzd.setText(carDetailsBean.getRegistration_city());
                }
                myAdapter.setNewData(carDetailsBean.getImages());
            }


        });
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_car_details;
    }

    private void getCommand() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("pageIndex", 0);
        map.put("pageSize", 6);
        map.put("carResourceId", bean.getId());
        NetApi.get(UrlKit.CAR_RESOURCE_RECOMMEND, map, new JsonCallback() {

            @Override
            public void onSuccess(String response, int id) {
                PageBean<SaleCardBean> data = new Gson().fromJson(response, new TypeToken<PageBean<SaleCardBean>>() {
                }.getType());
                CarDetailsAdapter detailsAdapter = new CarDetailsAdapter(data.getItems());
                detailsAdapter.setOnItemClickListener((adapter, view, position) -> {
                    SaleCardBean item = (SaleCardBean) adapter.getItem(position);
                    Intent intent = new Intent();
                    intent.putExtra(Constant.KEY_BEAN, item);
                    ActivityManage.push(SaleCarDetailsAct.class, intent);

                });
                listCommand.setAdapter(detailsAdapter);
            }
        });
    }

    @Override
    public void onItemClick(int position) {
        List<LocalMedia> images = new ArrayList<LocalMedia>();
        for (int i = 0; i < urls.size(); i++) {
            LocalMedia localMedia = new LocalMedia(UrlKit.getImgUrl(urls.get(i)), 0, 0, "");
            images.add(localMedia);
        }
        PictureSelector.create(this)
                .themeStyle(R.style.picture_default_style)
                .isNotPreviewDownload(true)
                .imageEngine(GlideEngine.createGlideEngine())
                .openExternalPreview(position, images);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onShare(EventShare share) {
        if (isOnly) {
            NetApi.postJson(UrlKit.CAR_RESOURCE_SHARED, "{\"carResourceId\": " + bean.getId() + " }", new JsonCallback() {

                @Override
                public void onSuccess(String response, int id) {
                    getPhone();
                }
            });
        }
    }

    @OnClick({R.id.iv_share, R.id.iv_kf, R.id.ll_company, R.id.tv_db, R.id.tv_show_error, R.id.tv_collection,
            R.id.tv_wbxx, R.id.tv_pzxx,R.id.car_details})
    public void onClick(View view) {
        if (!UserLogic.isCarShop()) {
            MessageDialog.build(this)
                    .setMessage("您还未进行汽贸店或4S店认证，请先认证")
                    .setOkButton("去认证", new OnDialogButtonClickListener() {
                        @Override
                        public boolean onClick(BaseDialog baseDialog, View v) {
                            ActivityManage.push(InfoAuthAct.class);
                            return false;
                        }
                    })
                    .setOkButtonDrawable(R.drawable.shape_button_red_bg)
                    .show();
            return;
        }
        if (carDetailsBean == null) return;
        switch (view.getId()) {
            case R.id.tv_wbxx:
                reportDetails(1, carDetailsBean.getVin());
                break;
            case R.id.tv_pzxx:
                reportDetails(2, carDetailsBean.getVin());
                break;
            case R.id.tv_show_error:
                pvOptions.show();
                break;
            case R.id.tv_collection:
                if (carDetailsBean.isCollected()) {
                    cancelCollection();
                } else {
                    collection();
                }
                break;
            case R.id.iv_share:
                isOnly = false;
                sharePop.setPrice(bean);
                if (!sharePop.isShowing())
                    sharePop.showAtLocation(view, Gravity.CENTER, 0, 0);
                break;
            case R.id.tv_db:
                Intent intent2 = new Intent();
                intent2.putExtra(Constant.KEY_ID, carDetailsBean.getId());
                ActivityManage.push(SteamAct.class, intent2);
                break;
            case R.id.iv_kf:
                isOnly = true;
//                getShare(false);
                OtherLogic.toCall(this, carDetailsBean.getUser().getMobile());
                break;
            case R.id.ll_company:
                if(carDetailsBean.getCompany()!=null){
                    Intent intent = new Intent();
                    intent.putExtra(Constant.KEY_BEAN, carDetailsBean.getCompany());
                    intent.putExtra(Constant.KEY_ID,carDetailsBean.getUserId());
                    ActivityManage.push(CompanyInfoAct.class, intent);
                }
                break;
            case R.id.car_details:
                Intent details = new Intent(context, WebViewAct.class);
                details.putExtra(Constant.KEY_TITLE, "参数配置");
                details.putExtra(Constant.KEY_URL, String.format(UrlKit.H5_CAR_DETAILS,carDetailsBean.getBrand_fct(),carDetailsBean.getBrand_serie()));
                startActivity(details);
                break;
        }
    }

    private void reportDetails(int type, String vin) {
        String url;
        if (type == 1) {
            url = "api/app/mtnce_order/report/" + vin;
        } else {
            url = "api/app/insurance_order/report/" + vin;
        }
        NetApi.get(url, null, new JsonCallback() {

            @Override
            public void onSuccess(String response, int id) {
                SearchReportBean reportBean = new Gson().fromJson(response, SearchReportBean.class);
                if (reportBean != null && reportBean.getReport() != null && !TextUtils.isEmpty(reportBean.getReport().getReport_url())) {
                    Intent intent1 = new Intent();
                    intent1.putExtra(Constant.KEY_TITLE, "查询记录");
                    intent1.putExtra(Constant.KEY_URL, reportBean.getReport().getReport_url());
                    ActivityManage.push(WebViewAct.class, intent1);
                } else {
                    Util.toast("暂无记录");
                }
            }
        });
    }

    private void getShare(boolean flag) {
        NetApi.get(UrlKit.CAR_RESOURCE_SHARED, null, new JsonCallback() {

            @Override
            public void onSuccess(String response, int id) {
                if (response.contains("message") && response.contains("code")) {
                    return;
                }
                if (TextUtils.isEmpty(response) || flag) {
                    String title;
                    if (bean.getBrand_serie().contains(bean.getBrand_fct())) {
                        title = bean.getBrand_serie();
                    } else {
                        title = bean.getBrand_fct() + " " + bean.getBrand_serie();
                    }
                    String desc = "批发价：" + carDetailsBean.getSell_price() + "\n表显里程：" + carDetailsBean.getMileage();
                    new ShareDialog(context, String.format(UrlKit.H5_SHARE_CAR, carDetailsBean.getId(), SharedInfo.getInstance().getEntity(UserInfo.class).getId()),
                            title, desc, "转发至300人微信群或朋友圈可获得一天二手车版块查看权限").show();
//                    OtherLogic.share(String.format(UrlKit.H5_SHARE_CAR, carDetailsBean.getId(), SharedInfo.getInstance().getEntity(UserInfo.class).getId()),
//                            "转发至300人微信群或朋友圈可获得一天二手车版块查看权限", title, desc);
                } else {
                    getPhone();
                }
            }
        });
    }

    private void getPhone() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("carResourceId", bean.getId());
        NetApi.get(UrlKit.CAR_RESOURCE_MOBILE, map, new JsonCallback() {

            @Override
            public void onSuccess(String response, int id) {
                if (response.contains("message") && response.contains("code")) {
                    Message message = new Gson().fromJson(response, Message.class);
                    if (message.getCode() == 403) {
                        getShare(true);
                    }
                } else {
                    OtherLogic.toCall(context, response);
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }


    private class MyAdapter extends BaseQuickAdapter<String, BaseViewHolder> {

        public MyAdapter(List<String> list) {
            super(R.layout.item_car_img, list);
        }

        @Override
        protected void convert(@NotNull BaseViewHolder baseViewHolder, String s) {
            Glide.with(ActivityManage.peek()).load(UrlKit.getImgUrl(s)).into((ImageView) baseViewHolder.getView(R.id.item_img));
        }
    }

}
