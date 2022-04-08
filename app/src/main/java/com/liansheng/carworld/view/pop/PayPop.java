package com.liansheng.carworld.view.pop;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.liansheng.carworld.R;

import cn.droidlover.xdroid.tools.utils.Util;

public class PayPop extends PopupWindow {

    private int type;

    TextView pop_pay_amount;
    CheckBox payCk;

    public PayPop(Context context, PopClick popClick) {
//        this.type = type;
        View view = View.inflate(context, R.layout.pop_pay, null);
        payCk = view.findViewById(R.id.pop_pay_ck);
        setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
        setContentView(view);
        setBackgroundDrawable(new ColorDrawable(0xAA000000));
        setAnimationStyle(R.style.bottomMenuAnim);
        view.findViewById(R.id.pop_pay).setOnClickListener(v -> {
            if (payCk.isChecked()) {
                popClick.chick(v, "");
            } else {
                Util.toast("请选择支付方式");
            }
        });
        pop_pay_amount = view.findViewById(R.id.pop_pay_amount);
        setContentView(view);
        view.findViewById(R.id.item_rl).setOnClickListener(v -> {
            dismiss();
        });
        view.findViewById(R.id.item_ll).setOnClickListener(v -> {});
    }


    public void setAmount(String amount) {
        pop_pay_amount.setText("￥" + amount);
    }

}
