package com.liansheng.carworld.activity.circle;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.liansheng.carworld.R;
import com.liansheng.carworld.activity.BaseActivity;
import com.liansheng.carworld.activity.home.SaleCarActivity;
import com.liansheng.carworld.activity.logic.OtherLogic;
import com.liansheng.carworld.bean.CityBean;
import com.liansheng.carworld.bean.event.RefreshEvent;
import com.liansheng.carworld.bean.home.CircleBean;
import com.liansheng.carworld.bean.home.ImageUploadBean;
import com.liansheng.carworld.kit.Constant;
import com.liansheng.carworld.net.JsonCallback;
import com.liansheng.carworld.net.NetApi;
import com.liansheng.carworld.net.UrlKit;
import com.liansheng.carworld.oss.OssServiceUtil;
import com.liansheng.carworld.utils.GetJsonDataUtil;
import com.liansheng.carworld.utils.MoneyValueFilter;
import com.liansheng.carworld.utils.Utils;
import com.liansheng.carworld.view.GlideRoundTransform;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.entity.LocalMedia;

import org.greenrobot.eventbus.EventBus;
import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import cn.droidlover.xdroid.tools.utils.ActivityManage;
import cn.droidlover.xdroid.tools.utils.NetworkUtil;
import cn.droidlover.xdroid.tools.utils.Util;

public class ReleaseAct extends BaseActivity {

    final int CAR_PICK = 1001;

    @BindView(R.id.input_content)
    EditText inputContent;
    @BindView(R.id.input_content_size)
    TextView inputContentSize;
    @BindView(R.id.et_sale_price)
    EditText et_sale_price;
    @BindView(R.id.tv_city)
    TextView tv_city;
    @BindView(R.id.ck_yj)
    CheckBox ckYj;
    @BindView(R.id.ck_dj)
    CheckBox ckDj;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    private List<ImageUploadBean> uploadBeans = new ArrayList<>();
    private OptionsPickerView pvOptions;
    private MyAdapter myAdapter;
    private int index = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        super.onCreate(savedInstanceState);
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        uploadBeans.add(new ImageUploadBean());
        myAdapter = new MyAdapter(uploadBeans);
        new GetJsonDataUtil(new GetJsonDataUtil.SelectListener() {
            @Override
            public void success(ArrayList<CityBean> cityBeans, ArrayList<ArrayList<String>> city) {
                pvOptions = new OptionsPickerBuilder(context, new OnOptionsSelectListener() {
                    @Override
                    public void onOptionsSelect(int options1, int options2, int options3, View v) {
                        //返回的分别是三个级别的选中位置
                        String city = cityBeans.get(options1).getCity_list().get(options2);
                        tv_city.setText(city);
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
        inputContent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (!TextUtils.isEmpty(editable)) {
                    inputContentSize.setText(editable.length() + "/100");
                }
            }
        });
        ckYj.setOnCheckedChangeListener((buttonView, isChecked) -> {
            ckDj.setChecked(!isChecked);
        });
        ckDj.setOnCheckedChangeListener((buttonView, isChecked) -> {
            ckYj.setChecked(!isChecked);
        });
        et_sale_price.setFilters(new InputFilter[]{new MoneyValueFilter()});
        initImgAdapter();
    }

    private void initImgAdapter() {
        myAdapter.addChildClickViewIds(R.id.item_img_close, R.id.item_img_add, R.id.item_img);
        myAdapter.setOnItemChildClickListener((adapter, view, position) -> {
            ImageUploadBean item = (ImageUploadBean) adapter.getItem(position);
            switch (view.getId()) {
                case R.id.item_img_add:
                    index = position;
                    if (TextUtils.isEmpty(item.getUrl())) {
                        Utils.getPhoto(context, PictureConfig.MULTIPLE, 9 - myAdapter.getData().size() + 1);
                    }
                    break;
                case R.id.item_img_close:
                    adapter.remove(position);
                    if (adapter.getData().size() == 8) {
                        ImageUploadBean uploadBean = (ImageUploadBean) adapter.getData().get(adapter.getData().size() - 1);
                        if (!TextUtils.isEmpty(uploadBean.getUrl())) {
                            adapter.addData(new ImageUploadBean());
                        }
                    }
                    break;
                case R.id.item_img:
                    Util.toast("冲冲冲");
                    break;
            }
        });
        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        recyclerView.setAdapter(myAdapter);
    }

    @Override
    public int getLayoutId() {
        return R.layout.act_release;
    }


    @OnClick({R.id.tv_city, R.id.tv_history, R.id.btn_go})
    public void onViewClicker(View view) {
        switch (view.getId()) {
            case R.id.tv_city:
                if (pvOptions != null) {
                    pvOptions.show();
                }
                break;
            case R.id.tv_history:
                Intent intent = new Intent();
                intent.putExtra(Constant.KEY_TITLE, "历史发布");
                ActivityManage.push(HistoryReleaseAct.class, intent, 1001);
                break;
            case R.id.btn_go:
                String content = inputContent.getText().toString();
                String city = tv_city.getText().toString();
                String salePrice = et_sale_price.getText().toString();
                if (TextUtils.isEmpty(content)) {
                    Util.toast("请输入批发信息");
                    return;
                }
                if (TextUtils.isEmpty(city)) {
                    Util.toast("请选择城市");
                    return;
                }
                if (TextUtils.isEmpty(salePrice)) {
                    Util.toast("请输入批发价");
                    return;
                }
                List<ImageUploadBean> data = myAdapter.getData();
                int size = 0;
                List<String> imgs = new ArrayList<>();
                for (int i = 0; i < data.size(); i++) {
                    if (!TextUtils.isEmpty(data.get(i).getUrl())) {
                        imgs.add(data.get(i).getUrl());
                        size++;
                    }
                }
                if (size == 0) {
                    Util.toast("请上传车辆照片");
                    return;
                }
                Map<String, Object> map = new HashMap<>();
                map.put("type", "Post");
                map.put("text", content);
                map.put("city", city);
                if (imgs.size() > 0)
                    map.put("images", imgs);
                map.put("price", Double.valueOf(salePrice) * 10000);
                JSONObject json = new JSONObject(map);
                NetApi.postJson(UrlKit.USER_CAR_MOMENT, json.toString(), new JsonCallback() {
                    @Override
                    public void onSuccess(String data, int id) {
                        Util.toast("发布成功");
                        EventBus.getDefault().post(new RefreshEvent());
                        finish();
                    }
                });
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1001 && resultCode == Activity.RESULT_OK) {
            CircleBean detailsBean = data.getParcelableExtra(Constant.KEY_BEAN);
            if (detailsBean.getImages() != null) {
                index = 0;
                uploadBeans.clear();
                uploadBeans.add(new ImageUploadBean());
                myAdapter.replaceData(uploadBeans);
                if (detailsBean.getImages() != null && detailsBean.getImages().size() > 0) {
                    for (int i = 0; i < detailsBean.getImages().size(); i++) {
                        changeImage(index, detailsBean.getImages().get(i));
                        index++;
                    }
                    new Handler().postDelayed(() -> myAdapter.notifyDataSetChanged(), 700);
                }
            }
            ckYj.setChecked(detailsBean.getPriceType().equals("Negotiate"));
            inputContent.setText(detailsBean.getText());
        } else if (resultCode == RESULT_OK && (requestCode == PictureConfig.CHOOSE_REQUEST || requestCode == CAR_PICK)) {
            List<LocalMedia> selectList = PictureSelector.obtainMultipleResult(data);
            if (selectList != null && selectList.size() > 0) {
                List<String> strings = new ArrayList<>();
                for (int i = 0; i < selectList.size(); i++) {
                    strings.add(selectList.get(i).getCompressPath());
                }
                upload(strings);
            }
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

    private void changeImage(int position, String url) {
        uploadBeans = myAdapter.getData();
        for (int i = 0; i < uploadBeans.size(); i++) {
            if (position == i) {
                ImageUploadBean uploadBean = uploadBeans.get(i);
                uploadBean.setUrl(url);
                if (uploadBeans.size() != 9)
                    uploadBeans.add(new ImageUploadBean());
            }
        }
        myAdapter.replaceData(uploadBeans);
    }

    private class MyAdapter extends BaseQuickAdapter<ImageUploadBean, BaseViewHolder> {

        public MyAdapter(List<ImageUploadBean> imgs) {
            super(R.layout.item_img, imgs);
        }

        @Override
        protected void convert(@NotNull BaseViewHolder holder, ImageUploadBean item) {
            holder.setImageResource(R.id.item_img_add, R.mipmap.ic_circle_upload);
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
}
