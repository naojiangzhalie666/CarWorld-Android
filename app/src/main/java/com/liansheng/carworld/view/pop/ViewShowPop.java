package com.liansheng.carworld.view.pop;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.core.content.FileProvider;

import com.liansheng.carworld.R;
import com.liansheng.carworld.bean.UpdateBean;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.FileCallBack;

import java.io.File;

import cn.droidlover.xdroid.tools.utils.ActivityManage;
import cn.droidlover.xdroid.tools.utils.Util;
import me.wangyuwei.loadingview.LoadingView;
import okhttp3.Call;

public class ViewShowPop extends PopupWindow {

    ImageView update_view;

    public ViewShowPop(Context context) {
        View view = View.inflate(context, R.layout.pop_view_show, null);
        setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
        setContentView(view);
        setBackgroundDrawable(new ColorDrawable(0x00000000));
        setAnimationStyle(R.style.bottomMenuAnim);
        update_view = view.findViewById(R.id.pop_view);
        update_view.setOnClickListener(v -> {
            dismiss();
        });
        view.findViewById(R.id.pop_item).setOnClickListener(v -> dismiss());
    }

}
