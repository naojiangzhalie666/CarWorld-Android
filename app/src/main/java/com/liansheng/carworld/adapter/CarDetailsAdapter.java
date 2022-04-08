package com.liansheng.carworld.adapter;

import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.liansheng.carworld.R;
import com.liansheng.carworld.activity.logic.OtherLogic;
import com.liansheng.carworld.bean.home.SaleCardBean;
import com.liansheng.carworld.net.UrlKit;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

import cn.droidlover.xdroid.tools.utils.ActivityManage;
import cn.droidlover.xdroid.tools.utils.DensityUtil;
import cn.droidlover.xdroid.tools.utils.StringFormat;

public class CarDetailsAdapter extends BaseQuickAdapter<SaleCardBean, BaseViewHolder> {

    public CarDetailsAdapter(@Nullable List<SaleCardBean> data) {
        super(R.layout.item_card_details, data);
    }

    @Override
    protected void convert(@NotNull BaseViewHolder holder, SaleCardBean bean) {
        if(bean.getBrand_serie().contains(bean.getBrand_fct())){
            holder.setText(R.id.tv_car, bean.getBrand_serie());
        }else {
            holder.setText(R.id.tv_car, bean.getBrand_fct() + " " + bean.getBrand_serie());
        }
        if (bean.getRegistration_date() != null && bean.getRegistration_date().length() > 4) {
            holder.setText(R.id.tv_nf, bean.getRegistration_date().substring(0, 4) + " | " + StringFormat.doubleFormatForW2(bean.getMileage()) + "公里 | " + bean.getLocation());
        } else {
            holder.setText(R.id.tv_nf, bean.getRegistration_date() + " | " + StringFormat.doubleFormatForW2(bean.getMileage()) + "公里 | " + bean.getLocation());
        }
        holder.setText(R.id.tv_jg, StringFormat.doubleFormatForW2(bean.getSell_price()));
        ImageView img1 = holder.getView(R.id.iv_img);
        if (bean.getImages() != null && bean.getImages().size() > 0) {
            OtherLogic.updateAdapterImg(img1,bean.getImages().get(0));
//            Glide.with(ActivityManage.peek()).load(UrlKit.getImgUrl(bean.getImages().get(0))).into(img1);
        } else {
            Glide.with(ActivityManage.peek()).load(R.mipmap.bg_login).into(img1);
        }
    }
}
