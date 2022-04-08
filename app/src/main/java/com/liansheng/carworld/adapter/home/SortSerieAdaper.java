package com.liansheng.carworld.adapter.home;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.liansheng.carworld.R;
import com.liansheng.carworld.activity.BaseActivity;
import com.liansheng.carworld.activity.home.FindCarActivity;
import com.liansheng.carworld.activity.home.SaleCarActivity;
import com.liansheng.carworld.bean.BrandSerie;
import com.liansheng.carworld.kit.Constant;

import java.util.List;

import cn.droidlover.xdroid.hold.info.SharedInfo;
import cn.droidlover.xdroid.tools.utils.ActivityManage;

public class SortSerieAdaper extends BaseQuickAdapter<BrandSerie, BaseViewHolder> {

    public SortSerieAdaper() {
        super(R.layout.itemview_sort);
    }

    @Override
    protected void convert(BaseViewHolder viewHolder, BrandSerie item) {
        viewHolder.setText(R.id.tv_key, item.getYear()+"款");
        //第一个字母显示
        if (viewHolder.getLayoutPosition() == 0) {
            (viewHolder.getView(R.id.tv_key)).setVisibility(View.VISIBLE);
        } else {
            //然后判断当前姓名的首字母和上一个首字母是否相同,如果相同字母导航条就影藏,否则就显示
            if (item.getYear().equals(getData().get(viewHolder.getLayoutPosition() - 1).getYear())) {
                (viewHolder.getView(R.id.tv_key)).setVisibility(View.GONE);
            } else {
                (viewHolder.getView(R.id.tv_key)).setVisibility(View.VISIBLE);
            }
        }
        viewHolder.setText(R.id.tv_brand, item.getName());
        ImageView iv = viewHolder.getView(R.id.iv_img);
        iv.setVisibility(View.GONE);
        viewHolder.getView(R.id.tv_count).setVisibility(View.VISIBLE);
        viewHolder.setText(R.id.tv_count, "新车指导价\n" + item.getPrice());

    }


}
