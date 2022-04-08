
package com.liansheng.carworld.adapter;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.liansheng.carworld.R;
import com.liansheng.carworld.net.UrlKit;
import com.xuexiang.xui.adapter.recyclerview.BaseRecyclerAdapter;
import com.xuexiang.xui.adapter.recyclerview.RecyclerViewHolder;
import com.xuexiang.xui.widget.banner.recycler.BannerLayout;
import com.xuexiang.xui.widget.imageview.ImageLoader;
import com.xuexiang.xui.widget.imageview.strategy.DiskCacheStrategyEnum;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import cn.droidlover.xdroid.tools.utils.ContextHolder;

public class RecyclerViewBannerAdapter extends BaseRecyclerAdapter<String> {

    /**
     * 默认加载图片
     */
    private ColorDrawable mColorDrawable;
    private String number;

    /**
     * 是否允许进行缓存
     */
    private boolean mEnableCache = true;

    private BannerLayout.OnBannerItemClickListener mOnBannerItemClickListener;


    public RecyclerViewBannerAdapter() {
        this(new ArrayList<>());
    }

    public RecyclerViewBannerAdapter(List<String> list) {
        super(list);
        mColorDrawable = new ColorDrawable(Color.parseColor("#555555"));
    }

    public RecyclerViewBannerAdapter(List<String> list, String number) {
        super(list);
        this.number = number;
        mColorDrawable = new ColorDrawable(Color.parseColor("#555555"));
    }

    public RecyclerViewBannerAdapter(String[] list) {
        this(Arrays.asList(list));
    }

    /**
     * 适配的布局
     *
     * @param viewType
     * @return
     */
    @Override
    public int getItemLayoutId(int viewType) {
        return R.layout.adapter_recycler_view_banner_image_item;
    }

    /**
     * 绑定数据
     *
     * @param holder
     * @param position
     * @param imgUrl
     */
    @Override
    public void bindData(@NonNull RecyclerViewHolder holder, final int position, String imgUrl) {
        ImageView imageView = holder.findViewById(R.id.iv_item);
        TextView tvNum = holder.findViewById(R.id.tv_number);
        if (TextUtils.isEmpty(number)) {
            tvNum.setVisibility(View.GONE);
        } else {
            tvNum.setVisibility(View.VISIBLE);
            tvNum.setText("车源编号："+number);
        }
        if (!TextUtils.isEmpty(imgUrl)) {
            Glide.with(ContextHolder.getContext()).load(UrlKit.getImgUrl(imgUrl)).into(imageView);
//            ImageLoader.get().loadImage(imageView, UrlKit.getImgUrl(imgUrl), mColorDrawable,
//                    mEnableCache ? DiskCacheStrategyEnum.RESOURCE : DiskCacheStrategyEnum.NONE);
        } else {
            imageView.setImageDrawable(mColorDrawable);
        }

        imageView.setOnClickListener(v -> {
            if (mOnBannerItemClickListener != null) {
                mOnBannerItemClickListener.onItemClick(position);
            }
        });
    }

    /**
     * 设置是否允许缓存
     *
     * @param enableCache
     * @return
     */
    public RecyclerViewBannerAdapter enableCache(boolean enableCache) {
        mEnableCache = enableCache;
        return this;
    }

    /**
     * 获取是否允许缓存
     *
     * @return
     */
    public boolean getEnableCache() {
        return mEnableCache;
    }

    public ColorDrawable getColorDrawable() {
        return mColorDrawable;
    }

    public RecyclerViewBannerAdapter setColorDrawable(ColorDrawable colorDrawable) {
        mColorDrawable = colorDrawable;
        return this;
    }

    public RecyclerViewBannerAdapter setOnBannerItemClickListener(BannerLayout.OnBannerItemClickListener onBannerItemClickListener) {
        mOnBannerItemClickListener = onBannerItemClickListener;
        return this;
    }
}
