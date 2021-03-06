
package com.liansheng.carworld.utils;


import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.kunminx.linkage.adapter.viewholder.LinkagePrimaryViewHolder;
import com.kunminx.linkage.contract.ILinkagePrimaryAdapterConfig;
import com.liansheng.carworld.R;

public class CustomLinkagePrimaryAdapterConfig implements ILinkagePrimaryAdapterConfig {

    private static final int MARQUEE_REPEAT_LOOP_MODE = -1;
    private static final int MARQUEE_REPEAT_NONE_MODE = 0;
    private Context mContext;
    private OnPrimaryItemClickListener mItemClickListener;

    public CustomLinkagePrimaryAdapterConfig(OnPrimaryItemClickListener itemClickListener) {
        mItemClickListener = itemClickListener;
    }

    public CustomLinkagePrimaryAdapterConfig setOnItemClickListner(OnPrimaryItemClickListener itemClickListener) {
        mItemClickListener = itemClickListener;
        return this;
    }
    @Override
    public void setContext(Context context) {
        mContext = context;
    }

    @Override
    public int getLayoutId() {
        return R.layout.default_adapter_linkage_primary;
    }

    @Override
    public int getGroupTitleViewId() {
        return R.id.tv_group;
    }

    @Override
    public int getRootViewId() {
        return R.id.layout_group;
    }

    @Override
    public void onBindViewHolder(LinkagePrimaryViewHolder holder, boolean selected, String title) {
        TextView tvTitle = ((TextView) holder.mGroupTitle);
        tvTitle.setText(title);

        tvTitle.setBackgroundColor(mContext.getResources().getColor(selected ? R.color.order : R.color.colorWhite));
        tvTitle.setTextColor(ContextCompat.getColor(mContext, selected ? R.color.colorWhite : R.color.order));
        tvTitle.setEllipsize(selected ? TextUtils.TruncateAt.MARQUEE : TextUtils.TruncateAt.END);
        tvTitle.setFocusable(selected);
        tvTitle.setFocusableInTouchMode(selected);
        tvTitle.setMarqueeRepeatLimit(selected ? MARQUEE_REPEAT_LOOP_MODE : MARQUEE_REPEAT_NONE_MODE);
    }

    @Override
    public void onItemClick(LinkagePrimaryViewHolder holder, View view, String title) {
        if (mItemClickListener != null) {
            mItemClickListener.onPrimaryItemClick(holder, view, title);
        }
    }

    public interface OnPrimaryItemClickListener {
        void onPrimaryItemClick(LinkagePrimaryViewHolder holder, View view, String title);
    }
}
