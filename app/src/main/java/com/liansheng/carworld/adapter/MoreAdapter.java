package com.liansheng.carworld.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.liansheng.carworld.R;
import com.xuexiang.xui.adapter.listview.BaseListAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;


public class MoreAdapter extends BaseListAdapter<String, MoreAdapter.ViewHolder> {

    public MoreAdapter(Context context, String[] data) {
        super(context, data);
    }

    @Override
    protected ViewHolder newViewHolder(View convertView) {
        return new ViewHolder(convertView);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.adapter_more;
    }

    @Override
    protected void convert(ViewHolder holder, String item, int position) {
        holder.mText.setText(item);
        if (mSelectPosition != -1) {
            if (mSelectPosition == position) {
                holder.mText.setSelected(true);
            } else {
                holder.mText.setSelected(false);
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
