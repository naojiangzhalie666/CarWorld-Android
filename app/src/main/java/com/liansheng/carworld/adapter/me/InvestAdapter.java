package com.liansheng.carworld.adapter.me;

import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.liansheng.carworld.R;
import com.liansheng.carworld.bean.me.InvestBean;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class InvestAdapter extends BaseQuickAdapter<InvestBean, BaseViewHolder> {

    public InvestAdapter(List<InvestBean> datas){
        super(R.layout.item_invest,datas);
    }

    @Override
    protected void convert(@NotNull BaseViewHolder holder, InvestBean item) {
        holder.setText(R.id.item_invest_price,"￥"+item.getPrice());
        holder.setText(R.id.item_invest_time,"兑换"+item.getMonths()+"个月");
        LinearLayout check = holder.getView(R.id.item_invest_check);
        check.setSelected(item.isSelected());
    }
}
