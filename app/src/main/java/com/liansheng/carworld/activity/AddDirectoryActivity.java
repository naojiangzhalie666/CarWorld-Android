package com.liansheng.carworld.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.bumptech.glide.Glide;
import com.liansheng.carworld.R;
import com.liansheng.carworld.activity.home.ChooseCarActivity;
import com.liansheng.carworld.activity.logic.OtherLogic;
import com.liansheng.carworld.bean.CityBean;
import com.liansheng.carworld.kit.Constant;
import com.liansheng.carworld.net.JsonCallback;
import com.liansheng.carworld.net.NetApi;
import com.liansheng.carworld.net.UrlKit;
import com.liansheng.carworld.oss.OssServiceUtil;
import com.liansheng.carworld.utils.GetJsonDataUtil;
import com.liansheng.carworld.utils.Utils;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.qmuiteam.qmui.skin.QMUISkinManager;
import com.qmuiteam.qmui.util.QMUIDisplayHelper;
import com.qmuiteam.qmui.widget.popup.QMUIPopup;
import com.qmuiteam.qmui.widget.popup.QMUIPopups;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.droidlover.xdroid.tools.utils.ActivityManage;
import cn.droidlover.xdroid.tools.utils.Util;
import okhttp3.Call;

public class AddDirectoryActivity extends BaseActivity {

    //    @BindView(R.id.topbar)
//    QMUITopBar topbar;
    @BindView(R.id.et_pp)
    TextView etPp;
    @BindView(R.id.et_gsm)
    EditText etGsm;
    //    @BindView(R.id.et_sf)
//    NiceSpinner etSf;
    @BindView(R.id.et_dz)
    EditText etDz;
    @BindView(R.id.et_ew)
    EditText etEw;
    @BindView(R.id.et_sj)
    EditText etSj;
    @BindView(R.id.et_zj)
    EditText etZj;
    @BindView(R.id.iv_add)
    ImageView ivAdd;
    @BindView(R.id.btn_go)
    Button btnGo;
    @BindView(R.id.main)
    LinearLayout main;
    @BindView(R.id.btn_back)
    ImageView btnBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_city)
    TextView tvCity;
    List<View> mPopupViews = new ArrayList<>();
    private String province;
    private String city;
    private String photo = "";
    private OptionsPickerView pvOptions;

    @Override
    public void initData(Bundle savedInstanceState) {
        tvTitle.setText("??????4S???");
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        GetJsonDataUtil dataUtil = new GetJsonDataUtil(new GetJsonDataUtil.SelectListener() {
            @Override
            public void success(ArrayList<CityBean> cityBeans, ArrayList<ArrayList<String>> citys) {
                pvOptions = new OptionsPickerBuilder(context, new OnOptionsSelectListener() {
                    @Override
                    public void onOptionsSelect(int options1, int options2, int options3, View v) {
                        //?????????????????????????????????????????????
                        province = cityBeans.get(options1).getProvince();
                        city = cityBeans.get(options1).getCity_list().get(options2);
                        tvCity.setText(city);
                    }
                })
                        .setDividerColor(ContextCompat.getColor(context, R.color.line_grey))
                        .setTextColorCenter(ContextCompat.getColor(context, R.color.main_normal_color)) //???????????????????????????
                        .setContentTextSize(16)
                        .build();
                pvOptions.setPicker(cityBeans, citys);//???????????????????????????
                pvOptions.setSelectOptions(10, 5);
            }
        });
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_add_directory;
    }//????????????

    @OnClick({R.id.et_pp, R.id.iv_add, R.id.tv_city, R.id.btn_go})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.et_pp:
                Intent intent = new Intent();
                intent.putExtra(Constant.KEY_TYPE, 2);
                ActivityManage.push(ChooseCarActivity.class, intent, 1001);
                break;
            case R.id.iv_add:
                Utils.getPhoto(context);
                break;
            case R.id.tv_city:
                if (!pvOptions.isShowing()) {
                    pvOptions.show();
                }
                break;
            case R.id.btn_go:
                AddDirectory();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case PictureConfig.CHOOSE_REQUEST:
                    String path = PictureSelector.obtainMultipleResult(data).get(0).getCompressPath();
                    OssServiceUtil.getInstance().asyncPutImage(OtherLogic.getImgUrl(), path, (objectResult, currentPath) -> {
                        photo = currentPath;
                        Glide.with(context).load(UrlKit.getImgUrl(currentPath)).into(ivAdd);
                    });
                    break;
                case 1001:
                    etPp.setText(data.getStringExtra("value"));
                    break;
            }
        }
    }

    public void AddDirectory() {
        String pp = etPp.getText().toString();
        String gsm = etGsm.getText().toString();
        String dz = etDz.getText().toString();
        String ew = etEw.getText().toString();
        String sj = etSj.getText().toString();
        String zj = etZj.getText().toString();
        if (pp.equals("")) {
            showToast("???????????????");
            return;
        }
        if (gsm.equals("")) {
            showToast("??????????????????");
            return;
        }
        if (dz.equals("")) {
            showToast("?????????????????????");
            return;
        }
        if (TextUtils.isEmpty(city)) {
            Util.toast("?????????????????????");
            return;
        }
        if (ew.equals("")) {
            showToast("?????????????????????");
            return;
        }
        if (sj.equals("")) {
            showToast("??????????????????");
            return;
        }
        if (zj.equals("")) {
            showToast("?????????4S?????????");
            return;
        }
        if (photo.equals("")) {
            showToast("???????????????");
            return;
        }
        HashMap<String, Object> map = new HashMap<>();
        map.put("brand", pp);
        map.put("company", gsm);
        map.put("province", province);
        map.put("city", city);
        map.put("address", dz);
        map.put("name", ew);
        map.put("mobile", sj);
        map.put("phone", zj);
        map.put("photo", photo);
        NetApi.post(UrlKit.FORM, map, new JsonCallback() {

            @Override
            public void onSuccess(String response, int id) {
                Util.toast("????????????");
                finish();
            }
        });
    }
}
