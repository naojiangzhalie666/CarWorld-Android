package com.liansheng.carworld.adapter;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.liansheng.carworld.App;
import com.liansheng.carworld.activity.BaseActivity;
import com.liansheng.carworld.activity.home.DirectoryActivity;
import com.liansheng.carworld.bean.Brand;
import com.liansheng.carworld.R;
import com.liansheng.carworld.net.UrlKit;
import com.liansheng.carworld.utils.DateParserHelper;

import java.util.List;

import cn.droidlover.xdroid.tools.utils.ActivityManage;

public class SortAdaper extends BaseQuickAdapter<Brand.ItemsBean, BaseViewHolder> {
    List<Brand.ItemsBean> mData;

    public SortAdaper(List<Brand.ItemsBean> data, int type) {
        super(R.layout.itemview_sort, data);
        mData = data;
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
        viewHolder.setVisible(R.id.tv_count,true);
        viewHolder.setText(R.id.tv_count, "已有信息数量：" + item.getTotal());
        viewHolder.setText(R.id.tv_update,"最后更新时间："+ DateParserHelper.getYearAndMonthAndDay2(item.getUpdation()));
        viewHolder.setVisible(R.id.tv_update,true);
        RequestOptions myOptions = new RequestOptions().fitCenter().error(R.mipmap.header);
        ImageView iv = viewHolder.getView(R.id.iv_img);
        Glide.with(App.getContext()).load(UrlKit.getImgUrl(item.getIcon())).apply(myOptions).into(iv);

    }


}
