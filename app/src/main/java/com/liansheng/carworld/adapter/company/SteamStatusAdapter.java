package com.liansheng.carworld.adapter.company;

import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.liansheng.carworld.R;
import com.liansheng.carworld.bean.company.SteamGuaranteeBean;
import com.liansheng.carworld.utils.DateParserHelper;
import com.umeng.commonsdk.statistics.common.DataHelper;

import org.jetbrains.annotations.NotNull;

import java.text.DateFormat;
import java.util.ArrayList;

public class SteamStatusAdapter extends BaseQuickAdapter<SteamGuaranteeBean.StatusInfosBean, BaseViewHolder> {

    public SteamStatusAdapter(ArrayList<SteamGuaranteeBean.StatusInfosBean> list) {
        super(R.layout.item_steam_status, list);
    }

    @Override
    protected void convert(@NotNull BaseViewHolder baseViewHolder, SteamGuaranteeBean.StatusInfosBean bean) {
        if (bean.isShowUp()) {
            baseViewHolder.getView(R.id.steam_line_1).setVisibility(View.VISIBLE);
        } else {
            baseViewHolder.getView(R.id.steam_line_1).setVisibility(View.INVISIBLE);
        }
        if (bean.isShowDown()) {
            baseViewHolder.getView(R.id.steam_line_2).setVisibility(View.VISIBLE);
        } else {
            baseViewHolder.getView(R.id.steam_line_2).setVisibility(View.INVISIBLE);
        }
        baseViewHolder.setText(R.id.item_steam_title,bean.getTitle());
        baseViewHolder.setText(R.id.item_steam_date, DateParserHelper.getYearAndMonthAndDayAndTime(bean.getDate()));
    }
}
