package com.liansheng.carworld.adapter;

import android.content.Intent;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.liansheng.carworld.R;
import com.liansheng.carworld.activity.OrderDetailsActivity;
import com.liansheng.carworld.bean.UserMessage;
import com.liansheng.carworld.utils.DateParserHelper;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

import cn.droidlover.xdroid.tools.utils.ActivityManage;


public class MessageAdapter extends BaseQuickAdapter<UserMessage, BaseViewHolder> {


    public MessageAdapter(@Nullable List<UserMessage> data) {
        super(R.layout.item_message, data);
    }

    @Override
    protected void convert(@NotNull BaseViewHolder holder, UserMessage item) {
        holder.setVisible(R.id.iv_right, true);
        if ("User".equals(item.getType())) {
            holder.setImageResource(R.id.iv_type, R.mipmap.icon_msg_account);
        } else if ("TransportOrder".equals(item.getType())) {
            for (int i = 0; i < item.getValues().size(); i++) {
                if ("orderNO".equals(item.getValues().get(i).getKey())) {
                    holder.setText(R.id.tv_title, "订单号：" + item.getValues().get(i).getValue());
                }
            }
            holder.setImageResource(R.id.iv_type, R.mipmap.icon_msg_order);
        } else if ("Invitation".equals(item.getType()) || "JoinCompany".equals(item.getType()) || "CallPhone".equals(item.getType())) {
            holder.setText(R.id.tv_title, "公司消息");
            holder.setImageResource(R.id.iv_type, R.mipmap.icon_msg_company);
        } else if ("PlatformGuarantee".equals(item.getType())) {
            holder.setImageResource(R.id.iv_type, R.mipmap.icon_msg_steam);
            holder.setText(R.id.tv_title, "二手交易合同");
        } else {
            holder.setText(R.id.tv_title, "系统消息");
            holder.setVisible(R.id.iv_right, false);
            holder.setImageResource(R.id.iv_type, R.mipmap.icon_msg_ding);
        }
        holder.setText(R.id.tv_content, item.getTitle());
        holder.setText(R.id.tv_date, DateParserHelper.getYearAndMonthAndDayAndTime(item.getCreation()));
    }
}
