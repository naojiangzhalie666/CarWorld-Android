package com.liansheng.carworld.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
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

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;

@Deprecated
public class AuthChooseActivity extends BaseActivity {

    public int type = 1;//图片位置
    public String syx = "";
    public String hyx = "";
    public String yyzz = "";
    public int SCAN_ID = 1000;
    //    @BindView(R.id.topbar)
//    QMUITopBar topbar;
//    @BindView(R.id.tv_syx)
//    TextView tvSyx;
    @BindView(R.id.iv_syx)
    ImageView ivSyx;
    //    @BindView(R.id.tv_hyx)
//    TextView tvHyx;
    @BindView(R.id.iv_hyx)
    ImageView ivHyx;
    //    @BindView(R.id.tv_yyzz)
//    TextView tvYyzz;
    @BindView(R.id.iv_yyzz)
    ImageView ivYyzz;
    @BindView(R.id.main)
    LinearLayout main;
    @BindView(R.id.btn_back)
    ImageView btnBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        tvTitle.setText("补充信息");
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
//        Intent intent = getIntent();
//        if(intent.getStringExtra("from")!=null){
//            from = intent.getStringExtra("from");
//        }
        initView();
    }

    private void initView() {

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
                        syx = path;
                        Glide.with(context).load(UrlKit.getImgUrl(path)).apply(myOptions).into(ivSyx);
                    } else if (type == 2) {
                        hyx = path;
                        Glide.with(context).load(UrlKit.getImgUrl(path)).apply(myOptions).into(ivHyx);
                    } else if (type == 3) {
                        yyzz = path;
                        Glide.with(context).load(UrlKit.getImgUrl(path)).apply(myOptions).into(ivYyzz);
                    }
                }
            });
        } else {
            showToast("登录信息出错");
        }
    }


//    public void uploadSyx() {
//        if (getLoginResult() != null) {
//            NetApi.putSyx(getLoginResult().getToken(), syx, new JsonCallback<String>() {
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
////                        XToastUtils.success("提交成功");
//                        showSnackToast(main, "提交成功");
//                }
//            });
//        } else {
//            showToast("登录信息出错");
//        }
//    }

//    public void uploadYyzz() {
//        if (getLoginResult() != null) {
//            NetApi.putYyzz(getLoginResult().getToken(), yyzz, new JsonCallback<String>() {
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
////                        XToastUtils.success("提交成功");
//                        showSnackToast(main, "提交成功");
//                }
//            });
//        } else {
//            showToast("登录信息出错");
//        }
//    }

//    public void uploadHyx() {
//        if (getLoginResult() != null) {
//            NetApi.putHyx(getLoginResult().getToken(), hyx, new JsonCallback<String>() {
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
////                        XToastUtils.success("提交成功");
//                        showSnackToast(main, "提交成功");
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
        return R.layout.activity_auth_choose;
    }//布局文件

    @OnClick({R.id.iv_syx, R.id.iv_hyx, R.id.iv_yyzz})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_syx:
                type = 1;
                getPhoto();
                break;
            case R.id.iv_hyx:
                type = 2;
                getPhoto();
                break;
            case R.id.iv_yyzz:
                type = 3;
                getPhoto();
                break;
        }
    }
}
