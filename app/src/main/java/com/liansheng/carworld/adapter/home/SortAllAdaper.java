package com.liansheng.carworld.adapter.home;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.liansheng.carworld.R;
import com.liansheng.carworld.activity.BaseActivity;
import com.liansheng.carworld.activity.home.ChooseCarFctActivity;
import com.liansheng.carworld.bean.Brand;

import java.util.List;

import cn.droidlover.xdroid.tools.utils.ActivityManage;
import cn.droidlover.xdroid.tools.utils.ContextHolder;

public class SortAllAdaper extends BaseQuickAdapter<Brand.ItemsBean, BaseViewHolder> {
    List<Brand.ItemsBean> mData;
    private boolean flag;

    public SortAllAdaper(int layoutResId, List<Brand.ItemsBean> data, boolean flag) {
        super(layoutResId, data);
        mData = data;
        this.flag = flag;
    }

    @Override
    protected void convert(BaseViewHolder viewHolder, Brand.ItemsBean item) {
        //第一个字母显示
        if (viewHolder.getLayoutPosition() == 0) {
            (viewHolder.getView(R.id.tv_key)).setVisibility(View.VISIBLE);
        } else {
            //然后判断当前姓名的首字母和上一个首字母是否相同,如果相同字母导航条就影藏,否则就显示
            if (mData.get(viewHolder.getLayoutPosition()).getLetter().equals(mData.get(viewHolder.getLayoutPosition() - 1).getLetter())) {
                (viewHolder.getView(R.id.tv_key)).setVisibility(View.GONE);
            } else {
                (viewHolder.getView(R.id.tv_key)).setVisibility(View.VISIBLE);
            }
        }
        viewHolder.setText(R.id.tv_key, item.getLetter().toUpperCase());
        viewHolder.setText(R.id.tv_brand, item.getName());
        ImageView iv = viewHolder.getView(R.id.iv_img);
        Glide.with(ContextHolder.getContext()).load(item.getLogo()).into(iv);
        if (flag) {
            viewHolder.getView(R.id.llyt_brand).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent();
                    intent.putExtra("brand", item.getId());
                    ActivityManage.push(ChooseCarFctActivity.class, intent);
                }
            });
        }

    }


}
