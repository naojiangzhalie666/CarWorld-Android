package com.liansheng.carworld.oss;

import android.graphics.Bitmap;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.alibaba.sdk.android.oss.ClientConfiguration;
import com.alibaba.sdk.android.oss.ClientException;
import com.alibaba.sdk.android.oss.OSS;
import com.alibaba.sdk.android.oss.OSSClient;
import com.alibaba.sdk.android.oss.ServiceException;
import com.alibaba.sdk.android.oss.callback.OSSCompletedCallback;
import com.alibaba.sdk.android.oss.callback.OSSProgressCallback;
import com.alibaba.sdk.android.oss.common.auth.OSSCredentialProvider;
import com.alibaba.sdk.android.oss.internal.OSSAsyncTask;
import com.alibaba.sdk.android.oss.model.ListObjectsRequest;
import com.alibaba.sdk.android.oss.model.ListObjectsResult;
import com.alibaba.sdk.android.oss.model.ListPartsRequest;
import com.alibaba.sdk.android.oss.model.ListPartsResult;
import com.alibaba.sdk.android.oss.model.PutObjectRequest;
import com.alibaba.sdk.android.oss.model.PutObjectResult;
import com.liansheng.carworld.App;
import com.liansheng.carworld.bean.other.OssBean;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import cn.droidlover.xdroid.tools.log.Logger;
import cn.droidlover.xdroid.tools.utils.ActivityManage;
import cn.droidlover.xdroid.tools.utils.NetworkUtil;
import cn.droidlover.xdroid.tools.utils.Util;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

public class OssServiceUtil {
    private static OSS oss;
    private static OSSCredentialProvider credentialProvider;
    private static ClientConfiguration conf;
    private String bucket;
    private String path = "";

    private OssServiceUtil() {
    }

    private static volatile OssServiceUtil ossUtils;

    public static OssServiceUtil getInstance() {
        if (ossUtils == null) {
            synchronized (OssServiceUtil.class) {
                if (ossUtils == null) {
                    ossUtils = new OssServiceUtil();
                }
            }
        }
        return ossUtils;
    }

    //?????????????????????
    public void init(OssBean bean) {
        bucket = bean.getBucketName();
        credentialProvider = new STSGetter(bean.getAccessKeyId(), bean.getAccessKeySecret(), bean.getSecurityToken(), bean.getExpiration());
        conf = new ClientConfiguration();
        conf.setConnectionTimeout(15 * 1000); // ?????????????????????15???
        conf.setSocketTimeout(15 * 1000); // socket???????????????15???
        conf.setMaxConcurrentRequest(5); // ??????????????????????????????5???
        conf.setMaxErrorRetry(2); // ????????????????????????????????????2???
        oss = new OSSClient(App.getContext(), bean.getEndpoint(), credentialProvider, conf);
    }


    //?????????????????????
    public void init() {
        bucket = "chewujie";
        credentialProvider = new STSGetter();
        conf = new ClientConfiguration();
        conf.setConnectionTimeout(15 * 1000); // ?????????????????????15???
        conf.setSocketTimeout(15 * 1000); // socket???????????????15???
        conf.setMaxConcurrentRequest(5); // ??????????????????????????????5???
        conf.setMaxErrorRetry(2); // ????????????????????????????????????2???
        oss = new OSSClient(App.getContext(), "oss-cn-beijing.aliyuncs.com", credentialProvider, conf);
    }

    /**
     * ??????????????????????????????
     * <p>
     * //     * @param ???????????????????????????????????????
     * //     * @param ??????????????????
     * //     * @param ?????????
     * //     * @param ???????????????img
     */
    public void asyncPutImage(String object, final String localFile, picResultCallback callback) {
        if (object.equals("")) {
            return;
        }
        File file = new File(localFile);
        if (!file.exists()) {
            return;
        }
        // ??????????????????
        PutObjectRequest put = new PutObjectRequest(bucket, object, localFile);
//        put.setProgressCallback(new OSSProgressCallback<PutObjectRequest>() {
//            @Override
//            public void onProgress(PutObjectRequest request, long currentSize, long totalSize) {
//                int progress = (int) (100 * currentSize / totalSize);
//                if (mProgress != null) {
//                    mProgress.setProgress(progress);
//                }
//            }
//        });
        if (oss == null) {
            init();
            return;
        }
        oss.asyncPutObject(put, new OSSCompletedCallback<PutObjectRequest, PutObjectResult>() {
            @Override
            public void onSuccess(PutObjectRequest request, final PutObjectResult result) {
                if (callback != null) {
                    ActivityManage.peek().runOnUiThread(() -> {
                        callback.getPicData(result, object.replace("wwwroot", ""));
                    });
                }
            }

            @Override
            public void onFailure(PutObjectRequest request, ClientException clientExcepion, ServiceException serviceException) {
                String info = "";
                // ????????????
                if (clientExcepion != null) {
                    // ??????????????????????????????
                    clientExcepion.printStackTrace();
                    info = clientExcepion.toString();
                }
                if (serviceException != null) {
                    // ????????????
                    Logger.e("ErrorCode", serviceException.getErrorCode());
                    Logger.e("RequestId", serviceException.getRequestId());
                    Logger.e("HostId", serviceException.getHostId());
                    Logger.e("RawMessage", serviceException.getRawMessage());
                    info = serviceException.toString();
                }
                Logger.e(info);
                NetworkUtil.dismissCutscenes();
                ActivityManage.peek().runOnUiThread(() -> {
                    Util.toast("??????????????????????????????");
                });
            }
        });
    }

    /**
     * ?????????bitmap???????????????
     *
     * @param object
     * @param localFile
     */
    public void asyncPutImage(String object, final Bitmap localFile, picResultCallback callback) {
        if (object.equals("")) {
            Logger.w("AsyncPutImage", "ObjectNull");
            return;
        }
        if (localFile == null) {
            Logger.w("AsyncPutImage", "bitmapNull");
            return;
        }

        // ??????????????????
        PutObjectRequest put = new PutObjectRequest(bucket, object, getBitmapByte(localFile));
        OSSAsyncTask task = oss.asyncPutObject(put, new OSSCompletedCallback<PutObjectRequest, PutObjectResult>() {
            @Override
            public void onSuccess(PutObjectRequest request, final PutObjectResult result) {
                Observable.just(result).observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<PutObjectResult>() {
                    @Override
                    public void call(PutObjectResult putObjectResult) {
                        if (callback != null) {
                            callback.getPicData(result, "");
                        }
                    }
                });
            }

            @Override
            public void onFailure(PutObjectRequest request, ClientException clientExcepion, ServiceException serviceException) {
                String info = "";
                // ????????????
                if (clientExcepion != null) {
                    // ??????????????????????????????
                    clientExcepion.printStackTrace();
                    info = clientExcepion.toString();
                }
                if (serviceException != null) {
                    // ????????????
                    Logger.e("ErrorCode", serviceException.getErrorCode());
                    Logger.e("RequestId", serviceException.getRequestId());
                    Logger.e("HostId", serviceException.getHostId());
                    Logger.e("RawMessage", serviceException.getRawMessage());
                    info = serviceException.toString();
                }
            }
        });
    }

    public byte[] getBitmapByte(Bitmap bitmap) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
        try {
            out.flush();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return out.toByteArray();
    }

    public interface picResultCallback {
        void getPicData(PutObjectResult objectResult, String currentPath);
    }
}