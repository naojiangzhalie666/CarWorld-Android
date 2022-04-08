package com.liansheng.carworld.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.liansheng.carworld.App;
import com.liansheng.carworld.R;
import com.liansheng.carworld.bean.UpdateBean;
import com.liansheng.carworld.kit.Constant;
import com.liansheng.carworld.net.JsonCallback;
import com.liansheng.carworld.net.NetApi;
import com.liansheng.carworld.net.UrlKit;
import com.liansheng.carworld.view.FirstRegularDialog;
import com.liansheng.carworld.view.pop.UpdatePop;
import com.pgyersdk.crash.PgyCrashManager;
import com.pgyersdk.feedback.PgyerFeedbackManager;
import com.pgyersdk.update.DownloadFileListener;
import com.pgyersdk.update.PgyUpdateManager;
import com.pgyersdk.update.UpdateManagerListener;
import com.pgyersdk.update.javabean.AppBean;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.FileCallBack;

import java.io.File;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.droidlover.xdroid.hold.info.SharedInfo;
import cn.droidlover.xdroid.tools.utils.AppUtil;
import cn.droidlover.xdroid.tools.utils.Util;
import cn.jpush.android.api.JPushInterface;
import okhttp3.Call;
import pub.devrel.easypermissions.EasyPermissions;

public class SplashActivity extends AppCompatActivity implements EasyPermissions.PermissionCallbacks {

    @BindView(R.id.img)
    ImageView img;

    private UpdatePop updatePop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);
        Glide.with(this).load(R.drawable.qdy).into(img);
        initView();
        if (!(Boolean) SharedInfo.getInstance().getValue(Constant.REGULAR_STATE, false)) {
            new FirstRegularDialog(this,(view, content) -> {
                PgyCrashManager.register(); //推荐使用
                new PgyerFeedbackManager.PgyerFeedbackBuilder()
                        .setDisplayType(PgyerFeedbackManager.TYPE.ACTIVITY_TYPE)
                        .setBarBackgroundColor("#808080")
                        .setBarImmersive(true)
                        .builder()
                        .register();
                JPushInterface.setDebugMode(UrlKit.IS_DEBUG);
                JPushInterface.init(App.getContext());
                new Handler().postDelayed(() -> EasyPermissions.requestPermissions(this, "我们需要本地存储权限",
                        3001, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                ), 2100);
            }).show();
        }else {
        new Handler().postDelayed(() -> EasyPermissions.requestPermissions(this, "我们需要本地存储权限",
                3001, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION
        ), 2100);
        }
    }

    private void initView() {
        updatePop = new UpdatePop(this, (view, content) -> {
            if ("cancel".equals(content)) {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                finish();
            } else {
                if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    EasyPermissions.requestPermissions(this, "更新APP需要本地存储权限，多次拒绝后，需在APP设置中开启",
                            3002, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE);
                } else {
                    updatePop.update_btn.setEnabled(false);
                    updatePop.update_ll.setVisibility(View.VISIBLE);
                    updatePop.update_btn.setVisibility(View.GONE);
                    updatePop.loadingView.start();
                    download(content);
                }
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {
        if (requestCode == 3001) {
            initUpdate();
        }

    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {
        if (requestCode == 3001) {
            initUpdate();
        }
    }

    private void initUpdate() {
        NetApi.get(UrlKit.APP_VERSION, null, new JsonCallback() {

            @Override
            public void onError(Call call, Exception e, int id) {
                super.onError(call, e, id);
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                finish();
            }

            @Override
            public void onSuccess(String response, int id) {
                List<UpdateBean> list = new Gson().fromJson(response, new TypeToken<List<UpdateBean>>() {
                }.getType());
                if (list != null && list.size() > 0) {
                    String versionName = AppUtil.getAppVersionName(getApplicationContext()).replace(".", "");
                    for (int i = 0; i < list.size(); i++) {
                        String version = list.get(i).getVersion().replace(".", "");
                        if (Integer.parseInt(version) > Integer.parseInt(versionName)) {
                            updatePop.upData(list.get(i), versionName);
                            updatePop.showAtLocation(img, Gravity.CENTER, 0, 0);
                            return;
                        }
                    }
                }
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                finish();
            }
        });
    }

    private int values = 0;

    private void download(String url) {
//        File fileDir = new File(getExternalFilesDir(null).getPath() + "apk");
//        if (!fileDir.exists()) {
//            fileDir.mkdir();
//        }
//        //创建文件
//        File fileName = new File(fileDir + "/" + "CarWorld.apk");
//        if (!fileName.exists()) {
//            try {
//                fileName.createNewFile();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
        OkHttpUtils.get().url(url).build().execute(new FileCallBack(Environment.getExternalStorageDirectory().getAbsolutePath(), "CarWorld.apk") {

            @Override
            public void inProgress(float progress, long total, int id) {
                int newProgress = (int) (progress * 100);
                if (newProgress > values) {
                    values = newProgress;
                    updatePop.update_progress.setText(newProgress + "%");
                }
            }

            @Override
            public void onError(Call call, Exception e, int id) {
                pgyDownload();
            }

            @Override
            public void onResponse(File response, int id) {
                updatePop.loadingView.stop();
                updatePop.dismiss();
                Util.toast("下载成功，准备安装...");
                Intent intent = new Intent("android.intent.action.VIEW");
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                if (Build.VERSION.SDK_INT >= 24) {
                    Uri uri = FileProvider.getUriForFile(getApplicationContext(), getPackageName() + ".fileProvider", response);
                    intent.addCategory("android.intent.category.DEFAULT");
                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    intent.setDataAndType(uri, "application/vnd.android.package-archive");
                } else {
                    intent.setDataAndType(Uri.fromFile(response), "application/vnd.android.package-archive");
                }
                startActivity(intent);
                finish();
            }
        });
    }


    private void pgyDownload() {
        new PgyUpdateManager.Builder()
                .setForced(true)                //设置是否强制提示更新,非自定义回调更新接口此方法有用
                .setUserCanRetry(false)         //失败后是否提示重新下载，非自定义下载 apk 回调此方法有用
                .setUpdateManagerListener(new UpdateManagerListener() {
                    @Override
                    public void onNoUpdateAvailable() {
                        //没有更新是回调此方法
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        finish();
                        android.util.Log.d("pgyer", "there is no new version");
                    }

                    @Override
                    public void onUpdateAvailable(AppBean bean) {
                        //有更新回调此方法
//                        updatePop.llytUpdate.setVisibility(View.VISIBLE);
//                        updatePop.loadingView.start();
                        PgyUpdateManager.downLoadApk(bean.getDownloadURL());
                    }

                    @Override
                    public void checkUpdateFailed(Exception e) {
                        //更新检测失败回调
                        //更新拒绝（应用被下架，过期，不在安装有效期，下载次数用尽）以及无网络情况会调用此接口
                        android.util.Log.e("pgyer", "check update failed ", e);
                    }
                })
                //注意 ：
                //下载方法调用 PgyUpdateManager.downLoadApk(appBean.getDownloadURL()); 此回调才有效
                //此方法是方便用户自己实现下载进度和状态的 UI 提供的回调
                //想要使用蒲公英的默认下载进度的UI则不设置此方法
                .setDownloadFileListener(new DownloadFileListener() {
                    @Override
                    public void downloadFailed() {
                        //下载失败
                        android.util.Log.e("pgyer", "download apk failed");
                    }

                    @Override
                    public void downloadSuccessful(File file) {
                        android.util.Log.e("pgyer", "download apk success");
                        // 使用蒲公英提供的安装方法提示用户 安装apk
//                        llytUpdate.setVisibility(View.GONE);
                        updatePop.loadingView.stop();
                        PgyUpdateManager.installApk(file);
                        updatePop.dismiss();
                        finish();
//                        showToast("下载成功，准备安装...");
                    }

                    @Override
                    public void onProgressUpdate(Integer... integers) {
                        if (integers[0] > values) {
                            values = integers[0];
                            updatePop.update_progress.setText(integers[0] + "%");
                        }
                        Log.e("pgyer", "update download apk progress current " + integers[0]);
                    }
                }).register();
    }
}