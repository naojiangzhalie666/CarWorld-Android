package com.liansheng.carworld.view.pop;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.renderscript.Allocation;
import androidx.renderscript.RenderScript;
import androidx.renderscript.ScriptIntrinsicBlur;

import com.kongzue.dialog.interfaces.OnDialogButtonClickListener;
import com.kongzue.dialog.util.BaseDialog;
import com.kongzue.dialog.v3.MessageDialog;
import com.liansheng.carworld.R;
import com.liansheng.carworld.activity.AddDirectoryActivity;
import com.liansheng.carworld.activity.MainActivity;
import com.liansheng.carworld.activity.home.CarLoanAct;
import com.liansheng.carworld.activity.home.CarSelectAct;
import com.liansheng.carworld.activity.home.ChooseCarActivity;
import com.liansheng.carworld.activity.home.ChooseCarAllActivity;
import com.liansheng.carworld.activity.home.FindCarListActivity;
import com.liansheng.carworld.activity.home.SaleCarListAct;
import com.liansheng.carworld.activity.home.SecondhandSearchAct;
import com.liansheng.carworld.activity.home.TransporterActivity;
import com.liansheng.carworld.activity.logic.UserLogic;
import com.liansheng.carworld.activity.me.InfoAuthAct;
import com.liansheng.carworld.activity.me.SignAuthAct;
import com.liansheng.carworld.bean.UpdateBean;
import com.liansheng.carworld.bean.UserInfo;
import com.liansheng.carworld.bean.me.CompanyBean;
import com.liansheng.carworld.kit.Constant;
import com.liansheng.carworld.utils.DateParserHelper;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.FileCallBack;

import java.io.File;
import java.text.ParseException;

import cn.droidlover.xdroid.hold.info.SharedInfo;
import cn.droidlover.xdroid.tools.utils.ActivityManage;
import cn.droidlover.xdroid.tools.utils.Util;
import me.wangyuwei.loadingview.LoadingView;
import okhttp3.Call;

public class HomeAddPop extends PopupWindow implements View.OnClickListener {
    ImageView update_close;
    private UpdateBean updateBean;
    private Activity context;


    public HomeAddPop(Context context) {
        this.context = (Activity) context;
        View view = View.inflate(context, R.layout.pop_home_add, null);
        setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        setContentView(view);
        setBackgroundDrawable(new ColorDrawable(0xAA000000));
        setAnimationStyle(R.style.bottomMenuAnim);
        view.findViewById(R.id.home_slb).setOnClickListener(this);
        view.findViewById(R.id.home_4s).setOnClickListener(this);
        view.findViewById(R.id.home_cdml).setOnClickListener(this);
        view.findViewById(R.id.home_xb).setOnClickListener(this);
        view.findViewById(R.id.home_db).setOnClickListener(this);
        view.findViewById(R.id.home_esc).setOnClickListener(this);
        view.findViewById(R.id.home_qg).setOnClickListener(this);
        view.findViewById(R.id.home_ml).setOnClickListener(this);
        view.findViewById(R.id.home_wb).setOnClickListener(this);
        view.findViewById(R.id.home_pz).setOnClickListener(this);
        view.findViewById(R.id.home_zh).setOnClickListener(this);
        view.findViewById(R.id.home_xx).setOnClickListener(this);

        update_close = view.findViewById(R.id.close);
        update_close.setOnClickListener(v ->
                dismiss()
        );
    }

    @Override
    public void onClick(View v) {
        UserInfo userInfo = SharedInfo.getInstance().getEntity(UserInfo.class);
        Intent intent = new Intent();
        switch (v.getId()) {
            case R.id.home_slb:
                ActivityManage.push(CarSelectAct.class);
                break;
            case R.id.home_4s:
                if (UserLogic.isLogin()) {
                    if (userInfo.getVip() != null) {
                        if (!TextUtils.isEmpty(userInfo.getVip().getExpiration())) {
                            int days = 0;
                            try {
                                days = DateParserHelper.daysBetween(userInfo.getVip().getExpiration());
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            if (days > 0) {
                                intent.putExtra(Constant.KEY_TYPE, 1);
                                ActivityManage.push(ChooseCarActivity.class, intent);
                            } else {
                                Util.toast("您的VIP已到期，请续费");
                            }
                        }else {
                            intent.putExtra(Constant.KEY_TYPE, 1);
                            ActivityManage.push(ChooseCarActivity.class, intent);
                        }
                    } else {
                        intent.putExtra(Constant.KEY_TYPE, 1);
                        ActivityManage.push(ChooseCarActivity.class, intent);
                    }
                }
                break;
            case R.id.home_cdml:
                if (!UserLogic.isRealAuth()) {
                    return;
                }
                ActivityManage.push(CarLoanAct.class);
                break;
            case R.id.home_xb:
                ActivityManage.push(TransporterActivity.class, intent);
                break;
            case R.id.home_db:
                intent.putExtra(Constant.KEY_TYPE, -1);
                ActivityManage.push(TransporterActivity.class, intent);
                break;
            case R.id.home_esc:
            case R.id.home_qg:
                if (userInfo.getCompanys() != null) {
                    boolean auth = false;
                    for (int i = 0; i < userInfo.getCompanys().size(); i++) {
                        CompanyBean bean = userInfo.getCompanys().get(i);
                        if ((Constant.STORE_TYPE_2.equals(bean.getType()) || Constant.STORE_TYPE_1.equals(bean.getType())) && Constant.ID_STATUS_PASSED.equals(bean.getStatus())) {
                            auth = true;
                            break;
                        }
                    }
                    if (auth) {
                        if (v.getId() == R.id.home_esc) {
                            SharedInfo.getInstance().saveValue("car_type", "sale");
                        } else {
                            SharedInfo.getInstance().saveValue("car_type", "search");
                        }
                        ActivityManage.push(ChooseCarAllActivity.class);

                    } else {
                        MessageDialog.build((AppCompatActivity) context)
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
                    }
                }
                break;
            case R.id.home_ml:
                ActivityManage.push(AddDirectoryActivity.class);
                break;
            case R.id.home_wb:
                Intent intent1 = new Intent();
                intent1.putExtra(Constant.KEY_TYPE, 1);
                ActivityManage.push(SecondhandSearchAct.class, intent1);
                break;
            case R.id.home_pz:
                Intent intent2 = new Intent();
                intent2.putExtra(Constant.KEY_TYPE, 2);
                ActivityManage.push(SecondhandSearchAct.class, intent2);
                break;
            case R.id.home_zh:
            case R.id.home_xx:
                Util.toast("暂无开放，敬请期待");
                break;
        }
    }


}
