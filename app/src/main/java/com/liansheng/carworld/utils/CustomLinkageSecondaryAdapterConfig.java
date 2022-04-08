
package com.liansheng.carworld.utils;


import android.content.Context;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kunminx.linkage.adapter.viewholder.LinkageSecondaryFooterViewHolder;
import com.kunminx.linkage.adapter.viewholder.LinkageSecondaryHeaderViewHolder;
import com.kunminx.linkage.adapter.viewholder.LinkageSecondaryViewHolder;
import com.kunminx.linkage.bean.BaseGroupedItem;
import com.kunminx.linkage.contract.ILinkageSecondaryAdapterConfig;
import com.liansheng.carworld.R;


public class CustomLinkageSecondaryAdapterConfig implements ILinkageSecondaryAdapterConfig<CustomGroupedItem.ItemInfo> {

    private OnSecondaryItemClickListener mItemClickListener;
    private static final int SPAN_COUNT = 2;

    public CustomLinkageSecondaryAdapterConfig(OnSecondaryItemClickListener itemClickListener) {
        mItemClickListener = itemClickListener;
    }

    public CustomLinkageSecondaryAdapterConfig setOnItemClickListner(OnSecondaryItemClickListener itemClickListener) {
        mItemClickListener = itemClickListener;
        return this;
    }
    @Override
    public void setContext(Context context) {
    }

    @Override
    public int getGridLayoutId() {
        return R.layout.default_adapter_linkage_secondary_grid;
    }

    @Override
    public int getLinearLayoutId() {
        return R.layout.layout_secondary_city;
    }

    @Override
    public int getHeaderLayoutId() {
        return R.layout.layout_secondary_header;
    }

    @Override
    public int getFooterLayoutId() {
        return R.layout.adapter_linkage_empty_footer;
    }

    @Override
    public int getHeaderTextViewId() {
        return R.id.secondary_header;
    }

    @Override
    public int getSpanCountOfGridMode() {
        return SPAN_COUNT;
    }

    @Override
    public void onBindViewHolder(final LinkageSecondaryViewHolder holder,
                                 final BaseGroupedItem<CustomGroupedItem.ItemInfo> item) {
        ((TextView) holder.getView(R.id.city)).setText(item.info.getTitle());
//        ((TextView) holder.getView(R.id.level_2_content)).setText(item.info.getContent());
        ViewGroup viewGroup = holder.getView(R.id.item);
        viewGroup.setOnClickListener(v -> {
            if (mItemClickListener != null) {
                mItemClickListener.onSecondaryItemClick(holder, viewGroup, item);
            }
        });
    }

    @Override
    public void onBindHeaderViewHolder(LinkageSecondaryHeaderViewHolder holder,
                                       BaseGroupedItem<CustomGroupedItem.ItemInfo> item) {
        ((TextView) holder.getView(R.id.secondary_header)).setText(item.header);
    }

    @Override
    public void onBindFooterViewHolder(LinkageSecondaryFooterViewHolder holder,
                                       BaseGroupedItem<CustomGroupedItem.ItemInfo> item) {
    }

    public interface OnSecondaryItemClickListener {
        void onSecondaryItemClick(LinkageSecondaryViewHolder holder, ViewGroup view, BaseGroupedItem<CustomGroupedItem.ItemInfo> item);
    }
}
