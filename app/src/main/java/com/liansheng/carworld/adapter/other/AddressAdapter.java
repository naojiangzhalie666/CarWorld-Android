package com.liansheng.carworld.adapter.other;

import androidx.core.content.ContextCompat;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.liansheng.carworld.R;
import com.liansheng.carworld.bean.other.AddressItemBean;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

import cn.droidlover.xdroid.tools.utils.ActivityManage;

public class AddressAdapter extends BaseQuickAdapter<AddressItemBean, BaseViewHolder> {

    public AddressAdapter(@Nullable List<AddressItemBean> data) {
        super(R.layout.item_address, data);
    }

    @Override
    protected void convert(@NotNull BaseViewHolder holder, AddressItemBean item) {
        holder.setText(R.id.item_content, item.getName());
        if (item.isSelected()) {
            holder.setBackgroundColor(R.id.item_ll,
                    ContextCompat.getColor(ActivityManage.peek(), R.color.line_grey));
        } else {
            holder.setBackgroundColor(R.id.item_ll,
                    ContextCompat.getColor(ActivityManage.peek(), R.color.white));
        }

        holder.setText(R.id.item_number, item.getTotal() + "家");
    }
}
