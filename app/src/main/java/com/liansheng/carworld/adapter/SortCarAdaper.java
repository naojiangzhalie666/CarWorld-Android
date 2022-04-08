package com.liansheng.carworld.adapter;

import android.app.Activity;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.liansheng.carworld.R;
import com.liansheng.carworld.bean.Brand;
import com.liansheng.carworld.net.UrlKit;

import java.util.List;

public class SortCarAdaper extends BaseQuickAdapter<Brand.ItemsBean, BaseViewHolder> {
    List<Brand.ItemsBean> mData;
    private Activity mContext;
    public SortCarAdaper(int layoutResId, List<Brand.ItemsBean> data, Activity activity) {
        super(layoutResId, data);
        mContext = activity;
        mData=data;
    }

    @Override
    protected void convert(BaseViewHolder viewHolder, Brand.ItemsBean item) {

        //第一个字母显示
        if (viewHolder.getLayoutPosition() == 0) {
            (viewHolder.getView(R.id.tv_key)).setVisibility(View.VISIBLE);
        } else {
            //然后判断当前姓名的首字母和上一个首字母是否相同,如果相同字母导航条就影藏,否则就显示
            if(mData.get(viewHolder.getLayoutPosition()).getLetter().equals(mData.get(viewHolder.getLayoutPosition()-1).getLetter())){
                (viewHolder.getView(R.id.tv_key)).setVisibility(View.GONE);
            }else {
                (viewHolder.getView(R.id.tv_key)).setVisibility(View.VISIBLE);
            }
        }
        viewHolder.setText(R.id.tv_key, item.getLetter().toUpperCase());
        viewHolder.setText(R.id.tv_brand, item.getName());
        RequestOptions myOptions = new RequestOptions().fitCenter().error(R.mipmap.header);
        ImageView iv = viewHolder.getView(R.id.iv_img);
        Glide.with(mContext).load(UrlKit.getImgUrl(item.getIcon())).apply(myOptions).into(iv);

    }


}
