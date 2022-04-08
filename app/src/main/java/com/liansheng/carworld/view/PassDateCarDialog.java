package com.liansheng.carworld.view;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.TextUtils;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.liansheng.carworld.R;
import com.liansheng.carworld.activity.WebViewAct;
import com.liansheng.carworld.activity.home.SaleCarDetailsAct;
import com.liansheng.carworld.activity.logic.OtherLogic;
import com.liansheng.carworld.bean.home.SaleCardBean;
import com.liansheng.carworld.kit.Constant;
import com.liansheng.carworld.net.UrlKit;
import com.liansheng.carworld.utils.DateParserHelper;
import com.liansheng.carworld.utils.MoneyValueFilter;
import com.liansheng.carworld.view.pop.PopClick;
import com.liansheng.carworld.view.pop.PopWithIdClick;

import java.util.List;

import cn.droidlover.xdroid.hold.info.SharedInfo;
import cn.droidlover.xdroid.tools.utils.ActivityManage;
import cn.droidlover.xdroid.tools.utils.StringFormat;
import cn.droidlover.xdroid.views.recyclerView.DividerLine;

public class PassDateCarDialog extends Dialog {

    PopWithIdClick popClick;

    RecyclerView list;
    private MyAdapter myAdapter;

    public PassDateCarDialog(Activity context, PopWithIdClick popClick) {
        super(context, R.style.MyDialog);
        myAdapter = new MyAdapter();
        myAdapter.addChildClickViewIds(R.id.sale, R.id.refresh);
        myAdapter.setOnItemClickListener((adapter, view, position) -> {
            SaleCardBean item = (SaleCardBean) adapter.getItem(position);
            Intent intent = new Intent();
            intent.putExtra(Constant.KEY_BEAN, item);
            intent.putExtra(Constant.KEY_TYPE, true);
            ActivityManage.push(SaleCarDetailsAct.class, intent);
        });
        this.popClick = popClick;
    }

    public PassDateCarDialog(Context context, int themeId) {
        super(context, themeId);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_passdate_car);
        //如果对话框宽度异常，可以通过下方代码根据设备的宽度来设置弹窗宽度
        WindowManager windowManager = getWindow().getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        WindowManager.LayoutParams params = getWindow().getAttributes();
        Point point = new Point();
        display.getSize(point);
        params.width = (int) (point.x * 0.8);
        setCancelable(false);
        getWindow().setAttributes(params);
        list = findViewById(R.id.dialog_list);
        list.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        list.setAdapter(myAdapter);
        findViewById(R.id.cancel).setOnClickListener(v -> {
            dismiss();
        });
    }

    public void updateData(List<SaleCardBean> items) {
        myAdapter.setOnItemChildClickListener((adapter, view, position) -> {
            SaleCardBean item = (SaleCardBean) adapter.getItem(position);
            if (view.getId() == R.id.sale) {
                popClick.click(view, item.getId(), "sale");
            } else if (view.getId() == R.id.refresh) {
//                EditText inputMoney = (EditText) adapter.getViewByPosition(position, R.id.input_money);
//                popClick.click(view, item.getId(), inputMoney.getText().toString().trim());
                popClick.click(view, item.getId(), "");
            }
        });
        myAdapter.replaceData(items);
    }

    public void updateList(String id) {
        for (int i = 0; i < myAdapter.getData().size(); i++) {
            SaleCardBean cardBean = myAdapter.getData().get(i);
            if (cardBean.getId().equals(id)) {
                myAdapter.remove(cardBean);
            }
        }
        myAdapter.notifyDataSetChanged();
        if (myAdapter.getData().size() == 0) {
            dismiss();
        }
    }


    private class MyAdapter extends BaseQuickAdapter<SaleCardBean, BaseViewHolder> {

        public MyAdapter() {
            super(R.layout.item_dialog_refresh);
        }

        @Override
        protected void convert(@NonNull BaseViewHolder holder, SaleCardBean bean) {
//            EditText inputMoney = holder.getView(R.id.input_money);
//            inputMoney.setFilters(new InputFilter[]{new MoneyValueFilter()});
//            inputMoney.setHint(StringFormat.doubleFormatForW2(bean.getSell_price()).replace("万", ""));
            if (bean.getBrand_serie().contains(bean.getBrand_fct())) {
                holder.setText(R.id.tv_car, bean.getBrand_serie());
            } else {
                holder.setText(R.id.tv_car, bean.getBrand_fct() + " " + bean.getBrand_serie());
            }
            if (TextUtils.isEmpty(bean.getUpdateDate())) {
                holder.setText(R.id.tv_pass, "");
            } else {
                holder.setText(R.id.tv_pass, DateParserHelper.getYearAndMonthAndDay2(bean.getUpdateDate()) + "发布");
            }
            holder.setText(R.id.tv_jg, StringFormat.doubleFormatForW2(bean.getSell_price()));
            if (bean.getRegistration_date() != null && bean.getRegistration_date().length() > 4) {
                holder.setText(R.id.tv_nf, bean.getRegistration_date().substring(0, 4) + " | " + StringFormat.doubleFormatForW2(bean.getMileage()) + "公里 | " + bean.getLocation());
            } else {
                holder.setText(R.id.tv_nf, bean.getRegistration_date() + " | " + StringFormat.doubleFormatForW2(bean.getMileage()) + "公里 | " + bean.getLocation());
            }
            ImageView img1 = holder.getView(R.id.iv_img);
            if (bean.getImages() != null && bean.getImages().size() > 0) {
                Glide.with(ActivityManage.peek()).load(UrlKit.getImgUrl(bean.getImages().get(0))).into(img1);
            } else {
                Glide.with(ActivityManage.peek()).load(R.mipmap.bg_login).into(img1);
            }
        }
    }

}