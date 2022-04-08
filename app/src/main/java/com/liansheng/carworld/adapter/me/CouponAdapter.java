package com.liansheng.carworld.adapter.me;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.liansheng.carworld.R;
import com.liansheng.carworld.bean.me.CouponBean;
import com.liansheng.carworld.bean.me.ScoreBean;
import com.liansheng.carworld.utils.DateParserHelper;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class CouponAdapter extends BaseQuickAdapter<CouponBean, BaseViewHolder> {

    public CouponAdapter() {
        super(R.layout.item_coupon);
    }

    @Override
    protected void convert(@NotNull BaseViewHolder holder, CouponBean bean) {
        holder.setText(R.id.item_amount,bean.getDenomination()+"元优惠券");
        holder.setText(R.id.item_date,DateParserHelper.getYearAndMonthAndDay(bean.getExpireDate())+"过期");
    }
}
