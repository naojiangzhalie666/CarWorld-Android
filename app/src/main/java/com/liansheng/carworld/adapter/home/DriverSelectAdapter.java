package com.liansheng.carworld.adapter.home;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.liansheng.carworld.R;
import com.liansheng.carworld.activity.logic.OtherLogic;
import com.liansheng.carworld.bean.home.DriverSelectBean;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class DriverSelectAdapter extends BaseQuickAdapter<DriverSelectBean, BaseViewHolder> {

    public DriverSelectAdapter(List<DriverSelectBean> data) {
        super(R.layout.item_driver_select, data);
    }

    @Override
    protected void convert(@NotNull BaseViewHolder holder, DriverSelectBean item) {
        if (item.getCar() != null) {
            holder.setText(R.id.item_driver_name, item.getUsername() + "\n(" + OtherLogic.carSelect(item.getCar().isBigCar(), item.getCar().getTruckRequire()) + ")");
        } else {
            holder.setText(R.id.item_driver_name, item.getUsername());
        }
        if (item.getOrders() == null || item.getOrders().size() == 0) {
            holder.setGone(R.id.item_driver_send1, true);
            holder.setGone(R.id.item_driver_send2, false);
            holder.setText(R.id.item_driver_send2, "空闲");
            holder.setGone(R.id.item_driver_btn1,false);
            holder.setGone(R.id.item_driver_btn2,true);
            holder.setGone(R.id.item_driver_btn3,true);
        } else {
            DriverSelectBean.OrdersBean bean = item.getOrders().get(0);
            if (item.getOrders().size() == 1) {
                holder.setGone(R.id.item_driver_send1, false);
                holder.setGone(R.id.item_driver_send2, true);
                holder.setText(R.id.item_driver_send1, "(" + bean.getOrigin().getCity() + "-" + bean.getDestination().getCity() + ") 运输中");
                holder.setGone(R.id.item_driver_btn1,false);
                holder.setGone(R.id.item_driver_btn2,true);
                holder.setGone(R.id.item_driver_btn3,true);
            } else if (item.getOrders().size() == 2) {
                DriverSelectBean.OrdersBean bean2 = item.getOrders().get(0);
                holder.setGone(R.id.item_driver_send1, false);
                holder.setGone(R.id.item_driver_send2, false);
                holder.setText(R.id.item_driver_send1, "(" + bean.getOrigin().getCity() + "-" + bean.getDestination().getCity() + ") 运输中");
                holder.setText(R.id.item_driver_send2, "(" + bean2.getOrigin().getCity() + "-" + bean2.getDestination().getCity() + ") 待运输");
                holder.setGone(R.id.item_driver_btn1,true);
                holder.setGone(R.id.item_driver_btn2,false);
                holder.setGone(R.id.item_driver_btn3,false);
            }
        }
    }
}
