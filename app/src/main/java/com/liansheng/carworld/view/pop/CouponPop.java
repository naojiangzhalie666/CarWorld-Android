package com.liansheng.carworld.view.pop;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.liansheng.carworld.R;
import com.liansheng.carworld.bean.UpdateBean;
import com.liansheng.carworld.bean.me.CouponBean;
import com.liansheng.carworld.utils.DateParserHelper;

import me.wangyuwei.loadingview.LoadingView;

public class CouponPop extends PopupWindow {

    public CouponPop(Context context, String amount) {
        View view = View.inflate(context, R.layout.pop_coupon, null);
        setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
        setContentView(view);
        setBackgroundDrawable(new ColorDrawable(0xAA000000));
        setAnimationStyle(R.style.bottomMenuAnim);
        TextView textView=view.findViewById(R.id.pop_amount);
//        TextView date=view.findViewById(R.id.pop_date);
        textView.setText(amount);
//        date.setText("有效期至"+DateParserHelper.getYearAndMonthAndDay(couponBean.getExpireDate()));
        view.findViewById(R.id.update_close).setOnClickListener(v -> {
            dismiss();
        });
        view.findViewById(R.id.update_btn).setOnClickListener(v -> {
            dismiss();
        });
    }
}
