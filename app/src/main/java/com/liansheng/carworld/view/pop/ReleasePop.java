package com.liansheng.carworld.view.pop;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.liansheng.carworld.R;
import com.liansheng.carworld.activity.circle.BuyAct;
import com.liansheng.carworld.activity.circle.ReleaseAct;

import cn.droidlover.xdroid.tools.utils.ActivityManage;
import cn.droidlover.xdroid.tools.utils.Util;

public class ReleasePop extends PopupWindow {

    public ReleasePop(Context context) {
        View view = View.inflate(context, R.layout.pop_release, null);
        setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
        setContentView(view);
        setBackgroundDrawable(new ColorDrawable(0xAA000000));
//        setAnimationStyle(R.style.bottomMenuAnim);
        setContentView(view);
        view.findViewById(R.id.cancel).setOnClickListener(v -> dismiss());
        view.findViewById(R.id.pop_buy).setOnClickListener(v -> {
            ActivityManage.push(BuyAct.class);
            dismiss();

        });
        view.findViewById(R.id.pop_sale).setOnClickListener(v -> {
            ActivityManage.push(ReleaseAct.class);
            dismiss();
        });

    }

}

