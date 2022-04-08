package com.liansheng.carworld.adapter.home;

import android.text.Html;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.liansheng.carworld.R;
import com.liansheng.carworld.bean.company.SteamGuaranteeBean;
import com.liansheng.carworld.bean.home.CarLoanBean;
import com.liansheng.carworld.bean.home.SaleCardBean;
import com.liansheng.carworld.net.UrlKit;
import com.liansheng.carworld.utils.DateParserHelper;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

import cn.droidlover.xdroid.tools.utils.ActivityManage;
import cn.droidlover.xdroid.tools.utils.ContextHolder;
import cn.droidlover.xdroid.tools.utils.StringFormat;

public class CarLoanAdapter extends BaseQuickAdapter<CarLoanBean, BaseViewHolder> {

    public CarLoanAdapter(@Nullable List<CarLoanBean> data) {
        super(R.layout.item_car_loan, data);
    }

    @Override
    protected void convert(@NotNull BaseViewHolder holder, CarLoanBean item) {
        holder.setText(R.id.item_city, item.getLocation().getCity());
        Glide.with(ContextHolder.getContext()).load(UrlKit.getImgUrl(item.getLogo())).into((ImageView) holder.getView(R.id.item_img));
        holder.setText(R.id.item_content, Html.fromHtml(item.getDescription()));
    }

}
