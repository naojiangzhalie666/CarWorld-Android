package com.liansheng.carworld.adapter.home;

import android.text.TextUtils;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.liansheng.carworld.R;
import com.liansheng.carworld.bean.company.InsuranceBean;

import org.jetbrains.annotations.NotNull;

public class InsuranceAdapter extends BaseQuickAdapter<InsuranceBean.SchemasBean.ItemsBean, BaseViewHolder> {

    public InsuranceAdapter() {
        super(R.layout.item_insurance);
    }

    @Override
    protected void convert(@NotNull BaseViewHolder baseViewHolder, InsuranceBean.SchemasBean.ItemsBean itemsBean) {
        baseViewHolder.setText(R.id.item_title, itemsBean.getTitle());
        baseViewHolder.setText(R.id.item_fee, itemsBean.getSubTitle());
        if (TextUtils.isEmpty(itemsBean.getSale())) {
            baseViewHolder.getView(R.id.item_sale).setVisibility(View.GONE);
        } else {
            baseViewHolder.setText(R.id.item_sale, itemsBean.getSale());
            baseViewHolder.getView(R.id.item_sale).setVisibility(View.VISIBLE);
        }
    }
}
