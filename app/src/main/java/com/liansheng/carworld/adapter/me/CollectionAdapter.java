package com.liansheng.carworld.adapter.me;

import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.liansheng.carworld.R;
import com.liansheng.carworld.bean.Directory;
import com.liansheng.carworld.utils.DateParserHelper;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

import cn.droidlover.xdroid.tools.utils.ActivityManage;

public class CollectionAdapter extends BaseQuickAdapter<Directory.ItemsBean, BaseViewHolder> {

    public CollectionAdapter(@Nullable List<Directory.ItemsBean> data) {
        super(R.layout.item_collection, data);
    }

    @Override
    protected void convert(@NotNull BaseViewHolder baseViewHolder, Directory.ItemsBean item) {
        baseViewHolder.setText(R.id.tv_name,item.getCompany());
        baseViewHolder.setText(R.id.tv_date,DateParserHelper.getYearAndMonthAndDay(item.getCreation().toString()) + "更新");
        baseViewHolder.setText(R.id.tv_phone,"4s座机号:" + item.getPhone());
        baseViewHolder.setText(R.id.tv_mobile,"二网手机:" + item.getMobile() + " " + item.getName());
        baseViewHolder.setText(R.id.tv_address,"地址:" + item.getAddress());
        ImageView ivHeader = baseViewHolder.getView(R.id.iv_header);
        if (item.getPhoto() != null && !item.getPhoto().equals("")) {
            Glide.with(ActivityManage.peek()).load(item.getPhoto()).into(ivHeader);
        }
    }

}
