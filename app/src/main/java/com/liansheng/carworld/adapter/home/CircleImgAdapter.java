package com.liansheng.carworld.adapter.home;

import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.liansheng.carworld.R;
import com.liansheng.carworld.net.UrlKit;

import java.util.List;

import cn.droidlover.xdroid.tools.utils.ContextHolder;

public class CircleImgAdapter extends BaseQuickAdapter<String, BaseViewHolder> {

    private int count;
    private String url;
    private int type;

    public CircleImgAdapter(int type, List<String> images, int count) {
        super(R.layout.item_circle_img, images);
        this.type = type;
        if (images.size() > 2) {
            url = images.get(2);
        }
        this.count = count;
    }

    @Override
    public int getItemCount() {
        return super.getItemCount();
    }

    @Override
    protected void convert(@NonNull BaseViewHolder baseViewHolder, String s) {
        Glide.with(ContextHolder.getContext()).load(UrlKit.getImgUrl(s))
                .into((ImageView) baseViewHolder.getView(R.id.item_img));
        if (type == 1) {
            if (s.equals(url) && count - 3 != 0) {
                baseViewHolder.setText(R.id.item_txt, "+" + (count - 3));
                baseViewHolder.setGone(R.id.item_txt, false);
            } else {
                baseViewHolder.setGone(R.id.item_txt, true);
            }
        }
    }
}
