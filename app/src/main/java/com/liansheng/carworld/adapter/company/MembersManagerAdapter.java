package com.liansheng.carworld.adapter.company;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.liansheng.carworld.R;
import com.liansheng.carworld.bean.MemberBean;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class MembersManagerAdapter extends BaseQuickAdapter<MemberBean, BaseViewHolder> {

    public MembersManagerAdapter() {
        super(R.layout.item_members_manager);
    }

    @Override
    protected void convert(@NotNull BaseViewHolder baseViewHolder, MemberBean item) {
        baseViewHolder.setText(R.id.item_name, item.getName());
        baseViewHolder.setText(R.id.item_phone, item.getMobile());
        if (item.getType().equals("ordinary")) {
            baseViewHolder.setText(R.id.item_type, "员工");
        } else if (item.getType().equals("manager")) {
            baseViewHolder.setText(R.id.item_type, "管理员");
        } else {
            baseViewHolder.setText(R.id.item_type, "创建者");
        }

    }
}
