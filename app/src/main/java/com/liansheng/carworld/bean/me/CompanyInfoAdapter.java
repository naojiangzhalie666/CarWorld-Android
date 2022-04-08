package com.liansheng.carworld.bean.me;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.liansheng.carworld.R;
import com.liansheng.carworld.bean.MemberBean;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class CompanyInfoAdapter extends BaseQuickAdapter<MemberBean, BaseViewHolder> {

    public CompanyInfoAdapter(@Nullable List<MemberBean> data) {
        super(R.layout.item_company_info, data);
    }

    @Override
    protected void convert(@NotNull BaseViewHolder baseViewHolder, MemberBean bean) {
        baseViewHolder.setText(R.id.item_name, bean.getName());
        baseViewHolder.setText(R.id.item_call, bean.getMobile());
    }
}
