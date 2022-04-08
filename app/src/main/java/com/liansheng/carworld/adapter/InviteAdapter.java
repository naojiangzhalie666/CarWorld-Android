package com.liansheng.carworld.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.liansheng.carworld.R;
import com.liansheng.carworld.bean.me.InviteBean;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class InviteAdapter extends BaseQuickAdapter<InviteBean, BaseViewHolder> {

    public InviteAdapter(@Nullable List<InviteBean> data) {
        super(R.layout.item_invite, data);
    }

    @Override
    protected void convert(@NotNull BaseViewHolder holder, InviteBean itemsBean) {
        holder.setText(R.id.item_name, itemsBean.getIdentityCard());
        holder.setText(R.id.item_mobile, itemsBean.getMobile());
        holder.setText(R.id.item_company, itemsBean.getCompany());
        holder.setText(R.id.item_money, itemsBean.getAmount());
    }
}
