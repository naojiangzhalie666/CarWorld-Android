package com.liansheng.carworld.adapter.company;

import android.os.Build;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextDirectionHeuristics;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.liansheng.carworld.R;
import com.liansheng.carworld.bean.company.SteamGuaranteeBean;
import com.liansheng.carworld.bean.home.FindCardBean;
import com.liansheng.carworld.bean.home.SaleCardBean;
import com.liansheng.carworld.net.UrlKit;
import com.liansheng.carworld.utils.DateParserHelper;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

import cn.droidlover.xdroid.tools.utils.ActivityManage;
import cn.droidlover.xdroid.tools.utils.DensityUtil;
import cn.droidlover.xdroid.tools.utils.StringFormat;

public class GuaranteeAdapter extends BaseQuickAdapter<SteamGuaranteeBean, BaseViewHolder> {

    public GuaranteeAdapter(@Nullable List<SteamGuaranteeBean> data) {
        super(R.layout.item_guarantee_car, data);
    }

    @Override
    protected void convert(@NotNull BaseViewHolder holder, SteamGuaranteeBean item) {
        SaleCardBean bean = item.getCarResource();
        holder.setText(R.id.tv_car, bean.getBrand_fct() + " " + bean.getBrand_serie());
        if (bean.getRegistration_date() != null && bean.getRegistration_date().length() > 4) {
            holder.setText(R.id.tv_nf, bean.getRegistration_date().substring(0, 4) + "年/");
        } else {
            holder.setText(R.id.tv_nf, bean.getRegistration_date() + "年/");
        }
        holder.setText(R.id.tv_gl, StringFormat.doubleFormatForW2(bean.getMileage()) + "公里");
        holder.setText(R.id.tv_szd, bean.getLocation());
        holder.setText(R.id.tv_jg, StringFormat.doubleFormatForW2(bean.getSell_price()));
        if (TextUtils.isEmpty(bean.getUpdateDate())) {
            holder.setText(R.id.tv_pass, "");
        } else {
            holder.setText(R.id.tv_pass, DateParserHelper.getYearAndMonthAndDay2(bean.getUpdateDate()) + "发布");
        }
        ImageView img1 = holder.getView(R.id.iv_img);
        if (bean.getImages() != null && bean.getImages().size() > 0) {
            Glide.with(ActivityManage.peek()).load(UrlKit.getImgUrl(bean.getImages().get(0))).into(img1);
        } else {
            Glide.with(ActivityManage.peek()).load(R.mipmap.bg_login).into(img1);
        }

    }

}
