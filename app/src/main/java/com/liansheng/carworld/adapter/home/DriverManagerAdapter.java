package com.liansheng.carworld.adapter.home;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.liansheng.carworld.R;
import com.liansheng.carworld.activity.logic.OtherLogic;
import com.liansheng.carworld.bean.home.DriverSelectBean;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class DriverManagerAdapter extends BaseQuickAdapter<DriverSelectBean, BaseViewHolder> {

    public DriverManagerAdapter(@Nullable List<DriverSelectBean> data) {
        super(R.layout.item_driver_manager, data);
    }

    @Override
    protected void convert(@NotNull BaseViewHolder holder, DriverSelectBean item) {
        holder.setText(R.id.item_driver_nikename,item.getNickname());
        holder.setText(R.id.item_driver_name, item.getUsername());
        holder.setText(R.id.item_driver_phone, item.getMobile());
        holder.setText(R.id.item_driver_type, OtherLogic.carSelect(item.getCar().isBigCar(), item.getCar().getTruckRequire()));
    }
}
