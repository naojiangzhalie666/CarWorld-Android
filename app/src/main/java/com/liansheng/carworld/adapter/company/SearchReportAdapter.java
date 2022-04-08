package com.liansheng.carworld.adapter.company;

import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.liansheng.carworld.R;
import com.liansheng.carworld.bean.company.SearchReportBean;
import com.liansheng.carworld.utils.DateParserHelper;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

import cn.droidlover.xdroid.tools.utils.ActivityManage;

public class SearchReportAdapter extends BaseQuickAdapter<SearchReportBean, BaseViewHolder> {

    public SearchReportAdapter(@Nullable List<SearchReportBean> data) {
        super(R.layout.item_search, data);
    }

    @Override
    protected void convert(@NotNull BaseViewHolder baseViewHolder, SearchReportBean bean) {
//        Glide.with(ActivityManage.peek()).load(info.getBrand_img()).into((ImageView) baseViewHolder.getView(R.id.item_img));
        baseViewHolder.setText(R.id.item_title, "车架号：" + bean.getVin());
        baseViewHolder.setText(R.id.item_date, "创建时间：" + DateParserHelper.getYearAndMonthAndDayAndTimeAndMin(bean.getCreation()));
        if (bean.getStatus().equals("watting")) {
            baseViewHolder.setText(R.id.item_state, "正在查询");
        } else if (bean.getStatus().equals("completed")) {
            baseViewHolder.setText(R.id.item_state, "查询完成");
            if (bean.isRecord()) {
                baseViewHolder.getView(R.id.item_has_record).setVisibility(View.VISIBLE);
            } else {
                baseViewHolder.getView(R.id.item_has_record).setVisibility(View.INVISIBLE);
//                baseViewHolder.setText(R.id.item_state, "查询完成，无数据");
            }
        } else {
            baseViewHolder.setText(R.id.item_state, "");
        }
        baseViewHolder.setText(R.id.item_result, bean.getError());

    }
}
