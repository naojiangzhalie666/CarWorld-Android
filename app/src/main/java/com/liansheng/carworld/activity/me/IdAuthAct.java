package com.liansheng.carworld.activity.me;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.Nullable;

import com.baidu.idl.face.platform.FaceConfig;
import com.baidu.idl.face.platform.FaceEnvironment;
import com.baidu.idl.face.platform.FaceSDKManager;
import com.baidu.idl.face.platform.LivenessTypeEnum;
import com.baidu.idl.face.platform.listener.IInitCallback;
import com.baidu.idl.face.platform.ui.utils.IntentUtils;
import com.baidu.ocr.sdk.OCR;
import com.baidu.ocr.sdk.OnResultListener;
import com.baidu.ocr.sdk.exception.OCRError;
import com.baidu.ocr.sdk.model.IDCardParams;
import com.baidu.ocr.sdk.model.IDCardResult;
import com.baidu.ocr.ui.camera.CameraActivity;
import com.baidu.ocr.ui.camera.CameraNativeHelper;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.liansheng.carworld.App;
import com.liansheng.carworld.R;
import com.liansheng.carworld.activity.BaseActivity;
import com.liansheng.carworld.activity.logic.OtherLogic;
import com.liansheng.carworld.bean.company.ReqPersonalBean;
import com.liansheng.carworld.bean.me.ReqAuthBean;
import com.liansheng.carworld.bean.me.ReqBean;
import com.liansheng.carworld.face.FaceDetectExpActivity;
import com.liansheng.carworld.face.FaceLivenessExpActivity;
import com.liansheng.carworld.face.manager.QualityConfigManager;
import com.liansheng.carworld.face.model.Const;
import com.liansheng.carworld.face.model.QualityConfig;
import com.liansheng.carworld.face.utils.SharedPreferencesUtil;
import com.liansheng.carworld.kit.Constant;
import com.liansheng.carworld.net.JsonCallback;
import com.liansheng.carworld.net.NetApi;
import com.liansheng.carworld.net.UrlKit;
import com.liansheng.carworld.oss.OssServiceUtil;
import com.luck.picture.lib.PictureSelector;

import java.io.File;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;
import cn.droidlover.xdroid.tools.utils.FileUtil;
import cn.droidlover.xdroid.tools.utils.NetworkUtil;
import cn.droidlover.xdroid.tools.utils.Util;
import okhttp3.Call;

public class IdAuthAct extends BaseActivity {

    private boolean mIsInitSuccess;

    @BindView(R.id.id_auth_z)
    ImageView idAuthZ;
    @BindView(R.id.id_auth_f)
    ImageView idAuthF;

    private static final int REQUEST_CODE_CAMERA = 102;
    private String sfzZm = "";
    private String sfzFm = "";
    private String idName;
    private String idCard;

    @Override
    public void initData(Bundle savedInstanceState) {
        initView();
        initLicense();
        addActionLive();
    }

    private void addActionLive() {
        // 根据需求添加活体动作
        App.livenessList.clear();
        App.livenessList.add(LivenessTypeEnum.Eye);
        App.livenessList.add(LivenessTypeEnum.Mouth);
        App.livenessList.add(LivenessTypeEnum.HeadRight);
//        ExampleApplication.livenessList.add(LivenessTypeEnum.HeadUp);
//        ExampleApplication.livenessList.add(LivenessTypeEnum.HeadLeft);
//        ExampleApplication.livenessList.add(LivenessTypeEnum.HeadDown);
    }

    private void initView() {
        //  初始化本地质量控制模型,释放代码在onDestory中
        //  调用身份证扫描必须加上 intent.putExtra(CameraActivity.KEY_NATIVE_MANUAL, true); 关闭自动初始化和释放本地模型
        CameraNativeHelper.init(this, OCR.getInstance(this).getLicense(),
                new CameraNativeHelper.CameraNativeInitCallback() {
                    @Override
                    public void onError(int errorCode, Throwable e) {
//                        String msg;
//                        switch (errorCode) {
//                            case CameraView.NATIVE_SOLOAD_FAIL:
//                                msg = "加载so失败，请确保apk中存在ui部分的so";
//                                break;
//                            case CameraView.NATIVE_AUTH_FAIL:
//                                msg = "授权本地质量控制token获取失败";
//                                break;
//                            case CameraView.NATIVE_INIT_FAIL:
//                                msg = "本地质量控制";
//                                break;
//                            default:
//                                msg = String.valueOf(errorCode);
//                        }
//                        runOnUiThread(() -> {
//                            Util.toast(msg);
//                        });
                    }
                });
    }

    public void check() {
        if (sfzZm.equals("")) {
            showToast("请选择身份证正面照片");
            return;
        }
        if (sfzFm.equals("")) {
            showToast("请选择身份证反面照片");
            return;
        }
        ArrayList<ReqAuthBean> authBeans = new ArrayList<>();
        authBeans.add(new ReqAuthBean(Constant.AUTH_IDENTITY_CARD, sfzZm, sfzFm, idName, idCard));
        authInfo(authBeans);
    }

    private void authInfo(ArrayList<ReqAuthBean> authBeans) {
        ReqBean reqBean = new ReqBean();
        reqBean.setItems(authBeans);
        NetApi.put(UrlKit.USER_LICENSES, new Gson().toJson(reqBean), new JsonCallback() {

            @Override
            public void onSuccess(String response, int id) {
//                Util.toast();
            }
        });
    }

    @Override
    public int getLayoutId() {
        return R.layout.act_id_auth;
    }

    @OnClick({R.id.id_auth_z, R.id.id_auth_f, R.id.submit})
    public void onViewClick(View view) {
        Intent intent = new Intent(this, CameraActivity.class);
        intent.putExtra(CameraActivity.KEY_OUTPUT_FILE_PATH, FileUtil.getSaveFile(getApplication()).getAbsolutePath());
        intent.putExtra(CameraActivity.KEY_NATIVE_ENABLE, true);
        // KEY_NATIVE_MANUAL设置了之后CameraActivity中不再自动初始化和释放模型
        // 请手动使用CameraNativeHelper初始化和释放模型
        // 推荐这样做，可以避免一些activity切换导致的不必要的异常
        intent.putExtra(CameraActivity.KEY_NATIVE_MANUAL, true);
        switch (view.getId()) {
            case R.id.id_auth_z:
                intent.putExtra(CameraActivity.KEY_CONTENT_TYPE, CameraActivity.CONTENT_TYPE_ID_CARD_FRONT);
                startActivityForResult(intent, REQUEST_CODE_CAMERA);
                break;
            case R.id.id_auth_f:
                intent.putExtra(CameraActivity.KEY_CONTENT_TYPE, CameraActivity.CONTENT_TYPE_ID_CARD_BACK);
                startActivityForResult(intent, REQUEST_CODE_CAMERA);
                break;
            case R.id.submit:
//                ActivityManage.push(HomeActivity.class);
                if (sfzZm.equals("")) {
                    showToast("请选择身份证正面照片");
                    return;
                }
                if (sfzFm.equals("")) {
                    showToast("请选择身份证反面照片");
                    return;
                }
                if (!mIsInitSuccess) {
                    Util.toast("人脸识别初始化中...");
                    return;
                }
                startCollect();
                break;
        }
    }

    public void startCollect() {
        if (App.isActionLive) {
            Intent intent = new Intent(context, FaceLivenessExpActivity.class);
            startActivityForResult(intent, 103);
        } else {
            Intent intent = new Intent(context, FaceDetectExpActivity.class);
            startActivityForResult(intent, 103);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_CAMERA && resultCode == Activity.RESULT_OK) {
            if (data != null) {
                String contentType = data.getStringExtra(CameraActivity.KEY_CONTENT_TYPE);
                String filePath = FileUtil.getSaveFile(getApplicationContext()).getAbsolutePath();
                if (!TextUtils.isEmpty(contentType)) {
                    if (CameraActivity.CONTENT_TYPE_ID_CARD_FRONT.equals(contentType)) {
                        recIDCard(IDCardParams.ID_CARD_SIDE_FRONT, filePath);
                    } else if (CameraActivity.CONTENT_TYPE_ID_CARD_BACK.equals(contentType)) {
//                        recIDCard(IDCardParams.ID_CARD_SIDE_BACK, filePath);
                        OssServiceUtil.getInstance().asyncPutImage(OtherLogic.getImgUrl(), filePath, (objectResult, currentPath) -> {
                            sfzFm = currentPath;
                            Glide.with(context).load(UrlKit.getImgUrl(currentPath)).into(idAuthF);
                        });
                    }
                }
            }
        } else if (resultCode == RESULT_OK && requestCode == 103) {
            check(IntentUtils.getInstance().getBitmap());
        }
    }

    private void recIDCard(String idCardSide, String filePath) {
        IDCardParams param = new IDCardParams();
        param.setImageFile(new File(filePath));
        // 设置身份证正反面
        param.setIdCardSide(idCardSide);
        // 设置方向检测
        param.setDetectDirection(true);
        // 设置图像参数压缩质量0-100, 越大图像质量越好但是请求时间越长。 不设置则默认值为20
        param.setImageQuality(100);
        NetworkUtil.showCutscenes("", "正在上传...");
        OCR.getInstance(this).recognizeIDCard(param, new OnResultListener<IDCardResult>() {
            @Override
            public void onResult(IDCardResult result) {
                NetworkUtil.dismissCutscenes();
                if (result != null) {
                    if (IDCardParams.ID_CARD_SIDE_FRONT.equals(idCardSide)) {
                        idCard = result.getIdNumber().getWords();
                        idName = result.getName().getWords();
                        OssServiceUtil.getInstance().asyncPutImage(OtherLogic.getImgUrl(), filePath, (objectResult, currentPath) -> {
                            sfzZm = currentPath;
                            Glide.with(context).load(UrlKit.getImgUrl(currentPath)).into(idAuthZ);
                        });
                    }
                }
            }

            @Override
            public void onError(OCRError error) {
                NetworkUtil.dismissCutscenes();
            }
        });
    }

    private void initLicense() {
        boolean success = setFaceConfig();
        if (!success) {
            showToast("初始化失败 = json配置文件解析出错");
            return;
        }
        // 为了android和ios 区分授权，appId=appname_face_android ,其中appname为申请sdk时的应用名
        // 应用上下文
        // 申请License取得的APPID
        // assets目录下License文件名
        FaceSDKManager.getInstance().initialize(context, "CarWorld-face-android",
                "idl-license.faceexample-face-android-1", new IInitCallback() {
                    @Override
                    public void initSuccess() {
//                        runOnUiThread(() -> {
//                            showToast("初始化成功");
                        mIsInitSuccess = true;
//                        });
                    }

                    @Override
                    public void initFailure(final int errCode, final String errMsg) {
//                        runOnUiThread(() -> {
//                            showToast("初始化失败 = " + errCode + ", " + errMsg);
                        mIsInitSuccess = false;
//                        });
                    }
                });
    }

    /**
     * 参数配置方法
     */
    private boolean setFaceConfig() {
        FaceConfig config = FaceSDKManager.getInstance().getFaceConfig();
        // SDK初始化已经设置完默认参数（推荐参数），也可以根据实际需求进行数值调整
        // 质量等级（0：正常、1：宽松、2：严格、3：自定义）
        // 获取保存的质量等级
        SharedPreferencesUtil util = new SharedPreferencesUtil(context);
        int qualityLevel = (int) util.getSharedPreference(Const.KEY_QUALITY_LEVEL_SAVE, -1);
        if (qualityLevel == -1) {
            qualityLevel = App.qualityLevel;
        }
        // 根据质量等级获取相应的质量值（注：第二个参数要与质量等级的set方法参数一致）
        QualityConfigManager manager = QualityConfigManager.getInstance();
        manager.readQualityFile(context, qualityLevel);
        QualityConfig qualityConfig = manager.getConfig();
        if (qualityConfig == null) {
            return false;
        }
        // 设置模糊度阈值
        config.setBlurnessValue(qualityConfig.getBlur());
        // 设置最小光照阈值（范围0-255）
        config.setBrightnessValue(qualityConfig.getMinIllum());
        // 设置最大光照阈值（范围0-255）
        config.setBrightnessMaxValue(qualityConfig.getMaxIllum());
        // 设置左眼遮挡阈值
        config.setOcclusionLeftEyeValue(qualityConfig.getLeftEyeOcclusion());
        // 设置右眼遮挡阈值
        config.setOcclusionRightEyeValue(qualityConfig.getRightEyeOcclusion());
        // 设置鼻子遮挡阈值
        config.setOcclusionNoseValue(qualityConfig.getNoseOcclusion());
        // 设置嘴巴遮挡阈值
        config.setOcclusionMouthValue(qualityConfig.getMouseOcclusion());
        // 设置左脸颊遮挡阈值
        config.setOcclusionLeftContourValue(qualityConfig.getLeftContourOcclusion());
        // 设置右脸颊遮挡阈值
        config.setOcclusionRightContourValue(qualityConfig.getRightContourOcclusion());
        // 设置下巴遮挡阈值
        config.setOcclusionChinValue(qualityConfig.getChinOcclusion());
        // 设置人脸姿态角阈值
        config.setHeadPitchValue(qualityConfig.getPitch());
        config.setHeadYawValue(qualityConfig.getYaw());
        config.setHeadRollValue(qualityConfig.getRoll());
        // 设置可检测的最小人脸阈值
        config.setMinFaceSize(FaceEnvironment.VALUE_MIN_FACE_SIZE);
        // 设置可检测到人脸的阈值
        config.setNotFaceValue(FaceEnvironment.VALUE_NOT_FACE_THRESHOLD);
        // 设置闭眼阈值
        config.setEyeClosedValue(FaceEnvironment.VALUE_CLOSE_EYES);
        // 设置图片缓存数量
        config.setCacheImageNum(FaceEnvironment.VALUE_CACHE_IMAGE_NUM);
        // 设置活体动作，通过设置list，LivenessTypeEunm.Eye, LivenessTypeEunm.Mouth,
        // LivenessTypeEunm.HeadUp, LivenessTypeEunm.HeadDown, LivenessTypeEunm.HeadLeft,
        // LivenessTypeEunm.HeadRight
        config.setLivenessTypeList(App.livenessList);
        // 设置动作活体是否随机
        config.setLivenessRandom(App.isLivenessRandom);
        // 设置开启提示音
        config.setSound(App.isOpenSound);
        // 原图缩放系数
        config.setScale(FaceEnvironment.VALUE_SCALE);
        // 抠图宽高的设定，为了保证好的抠图效果，建议高宽比是4：3
        config.setCropHeight(FaceEnvironment.VALUE_CROP_HEIGHT);
        config.setCropWidth(FaceEnvironment.VALUE_CROP_WIDTH);
        // 抠图人脸框与背景比例
        config.setEnlargeRatio(FaceEnvironment.VALUE_CROP_ENLARGERATIO);
        // 加密类型，0：Base64加密，上传时image_sec传false；1：百度加密文件加密，上传时image_sec传true
        config.setSecType(FaceEnvironment.VALUE_SEC_TYPE);
        // 检测超时设置
        config.setTimeDetectModule(FaceEnvironment.TIME_DETECT_MODULE);
        // 检测框远近比率
        config.setFaceFarRatio(FaceEnvironment.VALUE_FAR_RATIO);
        config.setFaceClosedRatio(FaceEnvironment.VALUE_CLOSED_RATIO);
        FaceSDKManager.getInstance().setFaceConfig(config);
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 释放
        FaceSDKManager.getInstance().release();
    }

    public void check(String img) {
        ReqPersonalBean personalBean = new ReqPersonalBean();
        personalBean.setId_card_positive(sfzZm);
        personalBean.setId_card_back(sfzFm);
        personalBean.setImage_type("BASE64");
        personalBean.setName(idName);
        personalBean.setId_card_number(idCard);
        personalBean.setApp("android");
        personalBean.setImage(img);
        NetApi.postJson("api/app/user/FaceDetect", new Gson().toJson(personalBean), new JsonCallback() {

            @Override
            public void onSuccess(String response, int id) {
                Util.toast("身份认证完成");
                finish();
            }
        });
    }

}
