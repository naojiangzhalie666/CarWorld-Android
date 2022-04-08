package com.liansheng.carworld.activity.me;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Gravity;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.kongzue.dialog.interfaces.OnDialogButtonClickListener;
import com.kongzue.dialog.util.BaseDialog;
import com.kongzue.dialog.v3.MessageDialog;
import com.liansheng.carworld.R;
import com.liansheng.carworld.activity.BaseActivity;
import com.liansheng.carworld.activity.ChangePswActivity;
import com.liansheng.carworld.activity.MainActivity;
import com.liansheng.carworld.activity.WebViewAct;
import com.liansheng.carworld.activity.logic.UserLogic;
import com.liansheng.carworld.bean.UpdateBean;
import com.liansheng.carworld.bean.UserInfo;
import com.liansheng.carworld.kit.Constant;
import com.liansheng.carworld.net.JsonCallback;
import com.liansheng.carworld.net.NetApi;
import com.liansheng.carworld.net.UrlKit;
import com.liansheng.carworld.view.pop.UpdatePop;
import com.liansheng.carworld.wxapi.WxUtils;
import com.pgyersdk.update.DownloadFileListener;
import com.pgyersdk.update.PgyUpdateManager;
import com.pgyersdk.update.UpdateManagerListener;
import com.pgyersdk.update.javabean.AppBean;
import com.tencent.mm.opensdk.modelbiz.WXOpenCustomerServiceChat;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.FileCallBack;

import java.io.File;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.droidlover.xdroid.hold.info.SharedInfo;
import cn.droidlover.xdroid.tools.utils.ActivityManage;
import cn.droidlover.xdroid.tools.utils.AppUtil;
import cn.droidlover.xdroid.tools.utils.Util;
import cn.droidlover.xdroid.views.LeftRightLayout;
import cn.droidlover.xdroid.views.SwitchButton;
import cn.jpush.android.api.JPushInterface;
import okhttp3.Call;
import pub.devrel.easypermissions.EasyPermissions;

public class SettingsAct extends BaseActivity implements EasyPermissions.PermissionCallbacks {

    @BindView(R.id.push_switch)
    SwitchButton pushBtn;
    @BindView(R.id.llyt_psw)
    LeftRightLayout llytPsw;
    @BindView(R.id.llyt_version)
    LeftRightLayout llytVersion;
    private UpdatePop updatePop;

    @Override
    public void initData(Bundle savedInstanceState) {
        llytVersion.setRightText("V" + AppUtil.getAppVersionName(this));
        updatePop = new UpdatePop(this, (view, content) -> {
            if ("cancel".equals(content)) {
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
        pushBtn.setOpened(!JPushInterface.isPushStopped(context));
        pushBtn.setOnStateChangedListener(new SwitchButton.OnStateChangedListener() {
            @Override
            public void toggleToOn(SwitchButton view) {
                Util.toast("消息通知开启");
                JPushInterface.resumePush(context);
                view.setOpened(true);
            }

            @Override
            public void toggleToOff(SwitchButton view) {
                Util.toast("消息通知关闭");
                JPushInterface.stopPush(context);
                view.setOpened(false);
            }
        });
    }

    @Override
    public int getLayoutId() {
        return R.layout.act_settings;
    }

    @Override
    protected void onResume() {
        super.onResume();
        getUserInfo();
    }

    private void getUserInfo() {
        NetApi.get(UrlKit.USER_INFORM, null, new JsonCallback() {

            @Override
            public void onSuccess(String response, int id) {
                Gson gson = new Gson();
                UserInfo userInfo = gson.fromJson(response, UserInfo.class);
                setLoginResult(userInfo, false);
                SharedInfo.getInstance().saveEntity(userInfo);
                if (userInfo.isHasPassword()) {
                    llytPsw.setLeftText("修改密码");
                } else {
                    llytPsw.setLeftText("设置密码");
                }
            }
        });
    }

    @OnClick({R.id.llyt_psw, R.id.llyt_call, R.id.llyt_advice, R.id.llyt_version, R.id.btn_delete, R.id.btn_exit, R.id.tv_regular1, R.id.tv_regular2})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.llyt_psw:
                if (!UserLogic.isLogin()) return;
                Intent intent = new Intent();
                intent.putExtra(Constant.KEY_TYPE, getLoginResult().isHasPassword());
                ActivityManage.push(ChangePswActivity.class, intent);
                break;
            case R.id.llyt_call:
                if (!WxUtils.getInstance().linkWXService()) {
                    callPhone(this, "联系客服电话：" + Constant.SERVICE_PHONE, Constant.SERVICE_PHONE);
                }
                break;
            case R.id.llyt_advice:
                ActivityManage.push(AdviceAct.class);
                break;
            case R.id.llyt_version:
                EasyPermissions.requestPermissions(this, "我们需要本地存储权限",
                        3001, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION
                );
                break;
            case R.id.btn_delete:
                ActivityManage.push(DeleteAccountAct.class);
                break;
            case R.id.btn_exit:
                MessageDialog.build(this)
                        .setTitle("温馨提示")
                        .setMessage("确定要重新登录吗？")
                        .setCancelButton("取消", new OnDialogButtonClickListener() {
                            @Override
                            public boolean onClick(BaseDialog baseDialog, View v) {
                                return false;
                            }
                        })
                        .setOkButton("确定", new OnDialogButtonClickListener() {
                            @Override
                            public boolean onClick(BaseDialog baseDialog, View v) {
                                SharedPreferences sp = getSharedPreferences(UrlKit.LOGIN_STATE, Context.MODE_PRIVATE);
                                sp.edit().clear().commit();
                                SharedInfo.getInstance().saveValue(UrlKit.FIRST_RESULT, true);
                                SharedInfo.getInstance().remove(Constant.KEY_TOKEN);
                                SharedInfo.getInstance().remove(UserInfo.class);
                                JPushInterface.deleteAlias(getApplicationContext(), Constant.JPUSH_MESSAGE);
//                                fastLogin();
                                setResult(Activity.RESULT_OK);
                                finish();
                                return false;
                            }
                        })
                        .show();
                break;
            case R.id.tv_regular1:
                Intent intent2 = new Intent();
                intent2.putExtra(Constant.KEY_TITLE, "隐私协议");
                intent2.putExtra(Constant.KEY_URL, UrlKit.H5_SECRET_REGULAR);
                ActivityManage.push(WebViewAct.class, intent2);
                break;
            case R.id.tv_regular2:
                Intent intent1 = new Intent();
                intent1.putExtra(Constant.KEY_TITLE, "用户服务协议");
                intent1.putExtra(Constant.KEY_URL, UrlKit.H5_REGISTER_REGULAR);
                ActivityManage.push(WebViewAct.class, intent1);
                break;
        }
    }

    private void initUpdate() {
        NetApi.get(UrlKit.APP_VERSION, null, new JsonCallback() {

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
                            updatePop.showAtLocation(llytVersion, Gravity.CENTER, 0, 0);
                            return;
                        }
                    }
                }
                Util.toast("当前已是最新版本！");
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
    }
}
