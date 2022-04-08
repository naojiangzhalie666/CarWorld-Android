package com.liansheng.carworld.adapter.company;

import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.gif.GifDrawable;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.liansheng.carworld.R;
import com.liansheng.carworld.bean.home.FindCardBean;
import com.liansheng.carworld.utils.DateParserHelper;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import cn.droidlover.xdroid.tools.utils.ActivityManage;
import cn.droidlover.xdroid.tools.utils.StringFormat;

public class CompanyFindCarAdapter extends BaseQuickAdapter<FindCardBean, BaseViewHolder> {

    private int type;

    public CompanyFindCarAdapter(@NonNull List<FindCardBean> list, int type) {
        super(R.layout.item_company_find_car, list);
        this.type = type;
    }

    @Override
    protected void convert(@NotNull BaseViewHolder holder, FindCardBean findCardBean) {
        holder.setText(R.id.tv_name, "寻 " + findCardBean.getBrand_fct());
        holder.setText(R.id.tv_ys, findCardBean.getAppearance() + "/" + findCardBean.getInterior());
        if (TextUtils.isEmpty(findCardBean.getArea())) {
            holder.setText(R.id.tv_dd, "不限");
        } else {
            holder.setText(R.id.tv_dd, findCardBean.getArea());
        }

        if (TextUtils.isEmpty(findCardBean.getMileage())) {
            holder.setText(R.id.tv_gl, "不限");
        } else {
            holder.setText(R.id.tv_gl, findCardBean.getMileage());
        }
        holder.setText(R.id.tv_jl, findCardBean.getPickCarDistance());
        if (TextUtils.isEmpty(findCardBean.getYear())) {
            holder.setText(R.id.tv_cl, "不限");
        } else {
            holder.setText(R.id.tv_cl, findCardBean.getYear());
        }
        if (type == 1) {
            holder.getView(R.id.find_edit_ll).setVisibility(View.VISIBLE);
        } else {
            holder.getView(R.id.find_edit_ll).setVisibility(View.GONE);
        }
        ImageView view = holder.getView(R.id.iv_header);
        Glide.with(ActivityManage.peek()).load(findCardBean.getLogo()).into(view);
        holder.setText(R.id.tv_date, DateParserHelper.getYearAndMonthAndDay2(findCardBean.getCreation()));
        holder.setText(R.id.tv_jg, StringFormat.doubleFormatForW2(findCardBean.getMinPrice()) + "-" + StringFormat.doubleFormatForW2(findCardBean.getMaxPrice()));
    }

}
