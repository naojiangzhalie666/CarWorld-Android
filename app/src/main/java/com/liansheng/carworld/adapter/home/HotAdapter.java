package com.liansheng.carworld.adapter.home;

import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.liansheng.carworld.R;
import com.liansheng.carworld.bean.Brand;

import java.util.List;

import cn.droidlover.xdroid.tools.utils.ContextHolder;

public class HotAdapter extends BaseQuickAdapter<Brand.ItemsBean, BaseViewHolder> {

    public HotAdapter(List<Brand.ItemsBean> items) {
        super(R.layout.item_hot, items);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder baseViewHolder, Brand.ItemsBean brand) {
        baseViewHolder.setText(R.id.item_hot_name, brand.getName());
        Glide.with(ContextHolder.getContext()).load(brand.getLogo()).into((ImageView) baseViewHolder.getView(R.id.item_hot_img));
    }
}
