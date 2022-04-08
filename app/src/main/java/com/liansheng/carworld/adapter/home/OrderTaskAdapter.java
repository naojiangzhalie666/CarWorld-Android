package com.liansheng.carworld.adapter.home;

import android.text.TextUtils;

import androidx.core.content.ContextCompat;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.liansheng.carworld.R;
import com.liansheng.carworld.activity.logic.OtherLogic;
import com.liansheng.carworld.bean.TransporterBean;
import com.liansheng.carworld.bean.UserInfo;
import com.liansheng.carworld.utils.DateParserHelper;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import cn.droidlover.xdroid.hold.info.SharedInfo;
import cn.droidlover.xdroid.tools.utils.ContextHolder;

public class OrderTaskAdapter extends BaseQuickAdapter<TransporterBean, BaseViewHolder> {

    public OrderTaskAdapter(List<TransporterBean> list) {
        super(R.layout.item_order_task_list, list);
    }

    @Override
    protected void convert(@NotNull BaseViewHolder holder, TransporterBean item) {
        if (!TextUtils.isEmpty(item.getOrigin().getCity())) {
            holder.setText(R.id.tv_name, item.getOrigin().getCity() + item.getOrigin().getCounty());
        } else {
            holder.setText(R.id.tv_name, item.getOrigin().getProvince() + item.getOrigin().getCounty());
        }
        if (!TextUtils.isEmpty(item.getDestination().getCity())) {
            holder.setText(R.id.tv_name2, item.getDestination().getCity() + item.getDestination().getCounty());
        } else {
            holder.setText(R.id.tv_name2, item.getDestination().getProvince() + item.getDestination().getCounty());
        }
        holder.setText(R.id.tv_distance, item.getDistance() / 1000 + "公里");
        UserInfo entity = SharedInfo.getInstance().getEntity(UserInfo.class);
        if (item.getIncreasePrice() > 0) {
            int total;
            if(entity!=null&&item.getUserId().equals(entity.getId())){
                total=item.getPrice() + item.getIncreasePrice();
            }else {
                total=item.getPlatformPrice() + item.getIncreasePrice();
            }
            holder.setText(R.id.tv_price, "￥" + total);
        } else {
            if(entity!=null&&item.getUserId().equals(entity.getId())){
                holder.setText(R.id.tv_price, "￥" + item.getPrice());
            }else {
                holder.setText(R.id.tv_price, "￥" + item.getPlatformPrice());
            }
        }
        holder.setText(R.id.tv_type, item.getCarModel().getModel());
        if (item.getStartDate() != null) {
            if ("afternoon".equals(item.getStartDate().getTimeSlot())) {
                holder.setText(R.id.tv_time, DateParserHelper.getYearAndMonthAndDay(item.getStartDate().getDate()) + " 下午 出发");
            } else {
                holder.setText(R.id.tv_time, DateParserHelper.getYearAndMonthAndDay(item.getStartDate().getDate()) + " 上午 出发");
            }
        }
//        if (item.getNote() == null) {
//            holder.setText(R.id.tv_remark,"备注：无");
//        } else {
//            holder.setText(R.id.tv_remark,"备注："+item.getNote());
//        }
        holder.setText(R.id.tv_service, item.getCustomService().getName());
        if (TextUtils.isEmpty(item.getNote())) {
            holder.setText(R.id.tv_remark, "无");
        } else {
            holder.setText(R.id.tv_remark, item.getNote());
        }

        holder.setText(R.id.tv_add_price, item.getIncreasePrice() + "（加价金额）");
        holder.setText(R.id.tv_bc_type, OtherLogic.carSelect(item.isBigCar(), item.getTruckRequire()));
    }
}
