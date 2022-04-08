package com.liansheng.carworld.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.liansheng.carworld.R;
import com.liansheng.carworld.net.JsonCallback;
import com.liansheng.carworld.net.NetApi;
import com.liansheng.carworld.net.UrlKit;
import com.liansheng.carworld.utils.GlideEngine;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.qmuiteam.qmui.widget.QMUITopBar;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;

@Deprecated
public class AuthUpdateActivity extends BaseActivity {

    @BindView(R.id.btn_back)
    ImageView btnBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.iv_sfz_zm)
    ImageView ivSfzZm;
    @BindView(R.id.iv_sfz_fm)
    ImageView ivSfzFm;
    @BindView(R.id.iv_xsz_zm)
    ImageView ivXszZm;
    @BindView(R.id.iv_jqx)
    ImageView ivJqx;
    @BindView(R.id.btn_go)
    Button btnGo;
    @BindView(R.id.et_id)
    EditText etId;
    @BindView(R.id.et_name)
    EditText etName;

    public int type = 1;//图片位置
    public String sfzZm = "";
    public String sfzFm = "";
    public String xszZm = "";
    public String jqx = "";
    public String idCard = "";
    public String idName = "";
    public int SCAN_ID = 1001;
    @BindView(R.id.main)
    LinearLayout main;
    @BindView(R.id.view_name)
    View viewName;
    @BindView(R.id.iv_scan)
    ImageView ivScan;
    @BindView(R.id.llyt_name)
    LinearLayout llytName;
    @BindView(R.id.view_name2)
    View viewName2;
    @BindView(R.id.llyt_id)
    LinearLayout llytId;
    @BindView(R.id.view_name3)
    View viewName3;
    @BindView(R.id.xsz)
    TextView tv_xsz;
    @BindView(R.id.jqx)
    TextView tv_jqx;
    @BindView(R.id.sfz)
    TextView tv_sfz;
    @BindView(R.id.llyt_sfz)
    LinearLayout llytSfz;

    private String updateType = "1";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        tvTitle.setText("认证资料更新");
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        Intent intent = getIntent();
        if (intent.getStringExtra("type") != null) {
            updateType = intent.getStringExtra("type");
        }
        initView();
    }

    private void initView() {
        if (updateType.equals("1")) {
            tv_jqx.setVisibility(View.GONE);
            tv_sfz.setVisibility(View.GONE);
            ivJqx.setVisibility(View.GONE);
            llytSfz.setVisibility(View.GONE);
            llytId.setVisibility(View.GONE);
            llytName.setVisibility(View.GONE);
            viewName.setVisibility(View.GONE);
            viewName2.setVisibility(View.GONE);
            viewName3.setVisibility(View.GONE);
        } else if (updateType.equals("2")) {
            tv_xsz.setVisibility(View.GONE);
            ivXszZm.setVisibility(View.GONE);
            llytSfz.setVisibility(View.GONE);
            tv_sfz.setVisibility(View.GONE);
            llytId.setVisibility(View.GONE);
            llytName.setVisibility(View.GONE);
            llytName.setVisibility(View.GONE);
            viewName.setVisibility(View.GONE);
            viewName2.setVisibility(View.GONE);
            viewName3.setVisibility(View.GONE);
        } else if (updateType.equals("3")) {
            tv_jqx.setVisibility(View.GONE);
            tv_xsz.setVisibility(View.GONE);
            ivXszZm.setVisibility(View.GONE);
            ivJqx.setVisibility(View.GONE);
        }

    }

    public void getPhoto() {
        PictureSelector.create(this)
                .openGallery(PictureMimeType.ofImage())
                .imageEngine(GlideEngine.createGlideEngine())
                .selectionMode(PictureConfig.SINGLE)
                .isSingleDirectReturn(true)
                .isWeChatStyle(true)
                .isCompress(true)
                .minimumCompressSize(256)
                .compressQuality(90)
                .forResult(PictureConfig.CHOOSE_REQUEST);
    }

    public void getCamera() {
        PictureSelector.create(this)
                .openCamera(PictureMimeType.ofImage())
                .imageEngine(GlideEngine.createGlideEngine())
                .selectionMode(PictureConfig.SINGLE)
                .isSingleDirectReturn(true)
                .isWeChatStyle(true)
                .isCompress(true)
                .minimumCompressSize(256)
                .compressQuality(90)
                .forResult(PictureConfig.CHOOSE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case PictureConfig.CHOOSE_REQUEST:
                    List<LocalMedia> selectList = PictureSelector.obtainMultipleResult(data);
                    upload(selectList.get(0).getCompressPath());
                    break;
            }
        } else if (requestCode == SCAN_ID) {
            etId.setText(data.getStringExtra("id").toString());
            etName.setText(data.getStringExtra("name").toString());
        }
    }

    public void upload(String headphoto) {
        if (getLoginResult() != null) {
            NetApi.postResources(getLoginResult().getToken(), headphoto, new JsonCallback() {

                @Override
                public void onSuccess(String response, int id) {
                    RequestOptions myOptions = new RequestOptions().fitCenter().error(R.mipmap.header);
                    String path = response.toString().substring(2, response.toString().length() - 2);
                    if (type == 1) {
                        sfzZm = path;
                        Glide.with(context).load(UrlKit.getImgUrl(path)).apply(myOptions).into(ivSfzZm);
                    } else if (type == 2) {
                        sfzFm = path;
                        Glide.with(context).load(UrlKit.getImgUrl(path)).apply(myOptions).into(ivSfzFm);
                    } else if (type == 3) {
                        xszZm = path;
                        Glide.with(context).load(UrlKit.getImgUrl(path)).apply(myOptions).into(ivXszZm);
                    } else if (type == 5) {
                        jqx = path;
                        Glide.with(context).load(UrlKit.getImgUrl(path)).apply(myOptions).into(ivJqx);
                    }
                }
            });
        } else {
            showToast("登录信息出错");
        }
    }

    public void check() {
        if (updateType.equals("1")) {
            if (xszZm.equals("")) {
                showToast("请选择驾驶证照片");
                return;
            }
//            jsz();
        } else if (updateType.equals("2")) {
            if (jqx.equals("")) {
                showToast("请选择交强险照片");
                return;
            }
//            jqx();
        } else if (updateType.equals("3")) {
            if (sfzZm.equals("")) {
                showToast("请选择身份证正面照片");
                return;
            }
            if (sfzFm.equals("")) {
                showToast("请选择身份证反面照片");
                return;
            }
            idCard = etId.getText().toString();
            if (idCard.equals("")) {
                showToast("请填写身份证号码");
                return;
            }
            idName = etName.getText().toString();
            if (idName.equals("")) {
                showToast("请填写身份证姓名");
                return;
            }
//            sfz();
        }

    }

//    public void jsz() {
//        if (getLoginResult() != null) {
//            NetApi.putJsz(getLoginResult().getToken(), xszZm, new JsonCallback<String>() {
//                @Override
//                public void onFail(Call call, Exception e, int id) {
//                    showToast("提交失败");
//                }
//
//                @Override
//                public void onResponse(String response, int id) {
//                    if (response.contains("code") && response.contains("message")) {
//                        return;
//                    }
////                        showToast("认证资料提交成功");
//                        showSnackToast(main, "更新成功");
//                        finish();
//                }
//            });
//        } else {
//            showToast("登录信息出错");
//        }
//    }

//    public void jqx() {
//        if (getLoginResult() != null) {
//            NetApi.putJqx(getLoginResult().getToken(), jqx, new JsonCallback<String>() {
//                @Override
//                public void onFail(Call call, Exception e, int id) {
//                    showToast("提交失败");
//                }
//
//                @Override
//                public void onResponse(String response, int id) {
//                    if (response.contains("code") && response.contains("message")) {
//                        return;
//                    }
////                        showToast("认证资料提交成功");
//                        showSnackToast(main, "更新成功");
//                        finish();
//                }
//            });
//        } else {
//            showToast("登录信息出错");
//        }
//    }

//    public void sfz() {
//        if (getLoginResult() != null) {
//            NetApi.putSfz(getLoginResult().getToken(), sfzZm, sfzFm, idCard, idName, new JsonCallback<String>() {
//                @Override
//                public void onFail(Call call, Exception e, int id) {
//                    showToast("提交失败");
//                }
//
//                @Override
//                public void onResponse(String response, int id) {
//                    if (response.contains("code") && response.contains("message")) {
//                        return;
//                    }
////                        showToast("认证资料提交成功");
//                        showSnackToast(main, "更新成功");
//                        finish();
//                }
//            });
//        } else {
//            showToast("登录信息出错");
//        }
//    }

    @Override
    public void initData(Bundle savedInstanceState) {

    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_auth_update;
    }//布局文件

    @OnClick({R.id.iv_sfz_zm, R.id.iv_sfz_fm, R.id.iv_xsz_zm, R.id.iv_jqx, R.id.iv_scan, R.id.btn_go})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_sfz_zm:
                type = 1;
                getCamera();
                break;
            case R.id.iv_sfz_fm:
                type = 2;
                getCamera();
                break;
            case R.id.iv_xsz_zm:
                type = 3;
                getPhoto();
                break;
            case R.id.iv_jqx:
                type = 5;
                getPhoto();
                break;
            case R.id.iv_scan:
                Intent intent = new Intent(this, ScanIdActivity.class);
                startActivityForResult(intent, SCAN_ID);
                break;
            case R.id.btn_go:
                check();
                break;
        }
    }

}
