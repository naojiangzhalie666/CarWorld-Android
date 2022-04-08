package com.liansheng.carworld.view.pop;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;

import com.liansheng.carworld.R;
import com.liansheng.carworld.activity.logic.OtherLogic;
import com.liansheng.carworld.bean.UserInfo;
import com.liansheng.carworld.bean.home.SaleCardBean;
import com.liansheng.carworld.net.UrlKit;
import com.liansheng.carworld.utils.MoneyValueFilter;
import com.liansheng.carworld.view.ShareDialog;
import com.liansheng.carworld.wxapi.WxUtils;

import cn.droidlover.xdroid.hold.info.SharedInfo;
import cn.droidlover.xdroid.tools.utils.ActivityManage;
import cn.droidlover.xdroid.tools.utils.ContextHolder;
import cn.droidlover.xdroid.tools.utils.StringFormat;
import cn.droidlover.xdroid.tools.utils.Util;

public class PriceSharePop extends PopupWindow {

    TextView pop_share_price1;
    TextView pop_share_price2;
    SaleCardBean item;
    EditText input_money;

    public PriceSharePop(Context context) {
        View view = View.inflate(context, R.layout.pop_price_share, null);
        setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
        setContentView(view);
        setFocusable(true);
        setBackgroundDrawable(new ColorDrawable(0xAA000000));
        setAnimationStyle(R.style.bottomMenuAnim);
        setContentView(view);
        input_money = view.findViewById(R.id.pop_input_money);
        pop_share_price1 = view.findViewById(R.id.pop_share_price1);
        pop_share_price2 = view.findViewById(R.id.pop_share_price2);
        pop_share_price1.setOnClickListener(v -> {
            String title;
            if (item.getBrand_serie().contains(item.getBrand_fct())) {
                title = item.getBrand_serie();
            } else {
                title = item.getBrand_fct() + " " + item.getBrand_serie();
            }
            String desc = "批发价：" + item.getSell_price() + "\n表显里程：" + item.getMileage();
//            OtherLogic.share(String.format(UrlKit.H5_SHARE_CAR, item.getId(), SharedInfo.getInstance().getEntity(UserInfo.class).getId()),
//                    "", title, desc);
            dismiss();
            new ShareDialog(context, String.format(UrlKit.H5_SHARE_CAR, item.getId(), SharedInfo.getInstance().getEntity(UserInfo.class).getId())
                    , title, desc,"").show();
        });
        view.findViewById(R.id.iv_close).setOnClickListener(v -> {
            dismiss();
        });
        pop_share_price2.setOnClickListener(v -> {
            String content = input_money.getText().toString();
            if (TextUtils.isEmpty(content)) {
                Util.toast("请输入金额");
                return;
            }
            int newPrice = (int) (Double.parseDouble(content) * 10000);
//            if (item.getSell_price() > newPrice) {
//                Util.toast("零售价不得低于批发价");
//                return;
//            }
            String title;
            if (item.getBrand_serie().contains(item.getBrand_fct())) {
                title = item.getBrand_serie();
            } else {
                title = item.getBrand_fct() + " " + item.getBrand_serie();
            }
            String desc = "零售价：" + newPrice + "\n表显里程：" + item.getMileage();
            input_money.setText("");
            dismiss();
            new ShareDialog(context, String.format(UrlKit.H5_SHARE_CAR_2, item.getId(), SharedInfo.getInstance().getEntity(UserInfo.class).getId(), newPrice)
                    , title, desc).show();
        });
        input_money.setFilters(new InputFilter[]{new MoneyValueFilter()});
    }

    public void setPrice(SaleCardBean saleCardBean) {
        this.item = saleCardBean;
        pop_share_price1.setText(StringFormat.doubleFormatForW2(item.getSell_price()) + "\n批发价分享");
    }

}
