package com.liansheng.carworld.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.liansheng.carworld.R;
import com.xuexiang.xui.adapter.listview.BaseListAdapter;
import com.xuexiang.xui.utils.ResUtils;

import butterknife.BindView;
import butterknife.ButterKnife;


public class DropDownAdapter extends BaseListAdapter<String, DropDownAdapter.ViewHolder> {

    public DropDownAdapter(Context context) {
        super(context);
    }

    public DropDownAdapter(Context context, String[] data) {
        super(context, data);
    }

    @Override
    protected ViewHolder newViewHolder(View convertView) {
        return new ViewHolder(convertView);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.adapter_drop_down_list_item;
    }

    @Override
    protected void convert(ViewHolder holder, String item, int position) {
        holder.mText.setText(item);
        if (mSelectPosition != -1) {
            if (mSelectPosition == position) {
                holder.mText.setSelected(true);
                holder.mText.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, ResUtils.getVectorDrawable(holder.mText.getContext(), R.mipmap.icon_choose), null);
            } else {
                holder.mText.setSelected(false);
                holder.mText.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, null, null);
            }
        }
    }

    static class ViewHolder {
        @BindView(R.id.text)
        TextView mText;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
