package com.liansheng.carworld.adapter.home;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.liansheng.carworld.R;
import com.liansheng.carworld.activity.logic.OtherLogic;
import com.liansheng.carworld.bean.TransporterBean;
import com.liansheng.carworld.utils.DateParserHelper;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ChildOrderAdapter extends BaseQuickAdapter<TransporterBean, BaseViewHolder> {


    public ChildOrderAdapter(@Nullable List<TransporterBean> data) {
        super(R.layout.item_order_child_list, data);
    }

    @Override
    protected void convert(@NotNull BaseViewHolder holder, TransporterBean item) {
        holder.setText(R.id.tv_order_no, "订单号：" + item.getOrderNO());
        if (item.getOrigin().getCity().equals("")) {
            holder.setText(R.id.tv_name, item.getOrigin().getProvince() + item.getOrigin().getCounty());
        } else {
            holder.setText(R.id.tv_name, item.getOrigin().getCity() + item.getOrigin().getCounty());
        }
        if (item.getDestination().getCity().equals("")) {
            holder.setText(R.id.tv_name2, item.getDestination().getProvince() + item.getDestination().getCounty());
        } else {
            holder.setText(R.id.tv_name2, item.getDestination().getCity() + item.getDestination().getCounty());
        }
        holder.setText(R.id.tv_distance, item.getDistance() / 1000 + "公里");
        holder.setText(R.id.tv_price, "￥  " + item.getPrice() + "元");
        if (item.getCarModel().isFault()) {
            holder.setText(R.id.tv_type, item.getCarModel().getModel() + "（故障车）");
        } else {
            holder.setText(R.id.tv_type, OtherLogic.carSelect(item.getCar().isBigCar(), item.getCar().getTruckRequire()));
        }
        holder.setText(R.id.tv_driver, item.getNickname());
        if (item.getStatus().equals("creation")) {
//            if (!item.isPaid()) {
//                holder.setText(R.id.tv_status, "未支付");
//            } else {
//                holder.setText(R.id.tv_status, "已支付");
//            }
            holder.setText(R.id.tv_status, "已创建");
        } else if (item.getStatus().equals("processed")) {
            holder.setText(R.id.tv_status, "已接单");
        } else if (item.getStatus().equals("origin")) {
            holder.setText(R.id.tv_status, "到达出发地");
        } else if (item.getStatus().equals("transportation")) {
            holder.setText(R.id.tv_status, "运输中");
        } else if (item.getStatus().equals("destination")) {
            holder.setText(R.id.tv_status, "到达目的地");
        } else if (item.getStatus().equals("completion")) {
            holder.setText(R.id.tv_status, "已完成");
        }
        holder.setText(R.id.tv_time, "订单时间:" + DateParserHelper.getYearAndMonthAndDayAndTimeAndMin(item.getCreation()));
    }
}
