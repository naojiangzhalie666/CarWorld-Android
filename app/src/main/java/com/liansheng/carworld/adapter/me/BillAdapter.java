package com.liansheng.carworld.adapter.me;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.liansheng.carworld.R;
import com.liansheng.carworld.bean.me.ScoreBean;
import com.liansheng.carworld.utils.DateParserHelper;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class BillAdapter extends BaseQuickAdapter<ScoreBean, BaseViewHolder> {
    private int type;

    public BillAdapter(@Nullable List<ScoreBean> data, int type) {
        super(R.layout.item_bill, data);
        this.type = type;
    }

    @Override
    protected void convert(@NotNull BaseViewHolder holder, ScoreBean scoreBean) {
        if (type == 1) {
            holder.setImageResource(R.id.item_bill_type, R.mipmap.ic_bill_score);
        } else {
            holder.setImageResource(R.id.item_bill_type, R.mipmap.ic_bill_wallte);
        }
        holder.setText(R.id.item_bill_title, scoreBean.getItem());
        holder.setText(R.id.item_bill_score, scoreBean.getAmount());
        holder.setText(R.id.item_bill_date, DateParserHelper.getYearAndMonthAndDayAndTimeAndMin(scoreBean.getCreation()));
    }
}
