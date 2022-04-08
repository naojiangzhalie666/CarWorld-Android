package com.liansheng.carworld.adapter.home;

import android.text.TextUtils;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.liansheng.carworld.R;
import com.liansheng.carworld.bean.Directory;
import com.liansheng.carworld.utils.DateParserHelper;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class HomeAdapter extends BaseQuickAdapter<Directory.ItemsBean, BaseViewHolder> {

    public HomeAdapter(){
        super(R.layout.item_new);
    }

    @Override
    protected void convert(@NotNull BaseViewHolder holder, Directory.ItemsBean item) {
        holder.setText(R.id.tv_title,item.getBrand() + " " + item.getCompany());
        if(TextUtils.isEmpty(item.getName())){
            holder.setText(R.id.tv_mobile,"二网手机：" + item.getMobile());
        }else {
            holder.setText(R.id.tv_mobile,"二网手机：" + item.getMobile() +" （"+ item.getName()+"）");
        }
        holder.setText(R.id.tv_date,DateParserHelper.getYearAndMonthAndDay(item.getCreation()) + "更新");
    }
}
