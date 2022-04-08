package com.liansheng.carworld.adapter.home;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.liansheng.carworld.R;
import com.liansheng.carworld.activity.BaseActivity;
import com.liansheng.carworld.activity.home.ChooseCarSerieActivity;
import com.liansheng.carworld.bean.BrandFct;

import java.util.List;

import cn.droidlover.xdroid.tools.utils.ActivityManage;

public class SortFctAdaper extends BaseQuickAdapter<BrandFct, BaseViewHolder> {

    public SortFctAdaper() {
        super(R.layout.itemview_sort);
    }

    @Override
    protected void convert(BaseViewHolder viewHolder, BrandFct item) {
        //第一个字母显示
        if (viewHolder.getLayoutPosition() == 0) {
            (viewHolder.getView(R.id.tv_key)).setVisibility(View.VISIBLE);
        } else {
            //然后判断当前姓名的首字母和上一个首字母是否相同,如果相同字母导航条就影藏,否则就显示
            if(getData().get(viewHolder.getLayoutPosition()).getWord().equals(getData().get(viewHolder.getLayoutPosition()-1).getWord())){
                (viewHolder.getView(R.id.tv_key)).setVisibility(View.GONE);
            }else {
                (viewHolder.getView(R.id.tv_key)).setVisibility(View.VISIBLE);
            }
        }
//        if (item.isShow()) {
//            viewHolder.getView(R.id.tv_key).setVisibility(View.VISIBLE);
//        } else {
//            viewHolder.getView(R.id.tv_key).setVisibility(View.GONE);
//        }
        viewHolder.setText(R.id.tv_key, item.getFctname());
        viewHolder.setText(R.id.tv_brand, item.getSeriesName());
        viewHolder.getView(R.id.iv_img).setVisibility(View.GONE);
    }
}
