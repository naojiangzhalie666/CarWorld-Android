package com.liansheng.carworld.adapter;

import android.os.Build;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextDirectionHeuristics;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.liansheng.carworld.R;
import com.liansheng.carworld.activity.logic.OtherLogic;
import com.liansheng.carworld.bean.home.SaleCardBean;
import com.liansheng.carworld.net.UrlKit;
import com.liansheng.carworld.utils.DateParserHelper;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

import cn.droidlover.xdroid.tools.utils.ActivityManage;
import cn.droidlover.xdroid.tools.utils.DensityUtil;
import cn.droidlover.xdroid.tools.utils.StringFormat;

public class CarAdapter extends BaseQuickAdapter<SaleCardBean, BaseViewHolder> {

    public CarAdapter() {
        super(R.layout.item_car);
    }

    @Override
    protected void convert(@NotNull BaseViewHolder holder, SaleCardBean bean) {
        if (bean.getBrand_serie().contains(bean.getBrand_fct())) {
            holder.setText(R.id.tv_car, bean.getBrand_serie());
        } else {
            holder.setText(R.id.tv_car, bean.getBrand_fct() + " " + bean.getBrand_serie());
        }
        if (bean.getRegistration_date() != null && bean.getRegistration_date().length() > 4) {
            holder.setText(R.id.tv_nf, bean.getRegistration_date().substring(0, 4) + " | ");
        } else {
            holder.setText(R.id.tv_nf, bean.getRegistration_date() + " | ");
        }
        if (TextUtils.isEmpty(bean.getStandard())) {
            holder.setText(R.id.tv_gl, StringFormat.doubleFormatForW2(bean.getMileage()) + "公里");
        } else {
            holder.setText(R.id.tv_gl, StringFormat.doubleFormatForW2(bean.getMileage()) + "公里 | " + bean.getStandard());
        }
        holder.setText(R.id.tv_szd, bean.getLocation());
        holder.setText(R.id.tv_jg, StringFormat.doubleFormatForW2(bean.getSell_price()));
        if (TextUtils.isEmpty(bean.getUpdateDate())) {
            holder.setText(R.id.tv_pass, "");
        } else {
            holder.setText(R.id.tv_pass, DateParserHelper.getYearAndMonthAndDay2(bean.getUpdateDate()) + "发布");
        }
        if (bean.isNewCar()) {
            holder.setGone(R.id.tv_new_car, false);
        } else {
            holder.setGone(R.id.tv_new_car, true);
        }
        if (TextUtils.isEmpty(bean.getNegotiatedPrice())) {
            holder.setText(R.id.tv_car_type, "可议价");
        } else {
            holder.setText(R.id.tv_car_type, bean.getNegotiatedPrice());
        }
        ImageView img1 = holder.getView(R.id.iv_img);
        holder.setText(R.id.tv_browse, String.valueOf(bean.getVisits()));
        if (bean.isAccident()) {
            holder.setGone(R.id.tv_type1, false);
        } else {
            holder.setGone(R.id.tv_type1, true);
        }
        if (bean.isCompleted()) {
            holder.setGone(R.id.tv_type2, false);
        } else {
            holder.setGone(R.id.tv_type2, true);
        }
        if (bean.getImages() != null && bean.getImages().size() > 0) {
            OtherLogic.updateAdapterImg(img1, bean.getImages().get(0));
//            Glide.with(ActivityManage.peek()).load(UrlKit.getImgUrl(bean.getImages().get(0))).into(img1);
        } else {
            Glide.with(ActivityManage.peek()).load(R.mipmap.bg_login).into(img1);
        }

    }

}
