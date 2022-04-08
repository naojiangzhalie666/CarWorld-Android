package com.liansheng.carworld.adapter.company;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.liansheng.carworld.R;
import com.liansheng.carworld.bean.MemberBean;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ApplyMembersAdapter extends BaseQuickAdapter<MemberBean, BaseViewHolder> {

    public ApplyMembersAdapter(@Nullable List<MemberBean> data) {
        super(R.layout.item_apply_members, data);
    }

    @Override
    protected void convert(@NotNull BaseViewHolder baseViewHolder, MemberBean bean) {
        baseViewHolder.setText(R.id.item_name, bean.getName());
        baseViewHolder.setText(R.id.item_phone, bean.getMobile());
    }
}
