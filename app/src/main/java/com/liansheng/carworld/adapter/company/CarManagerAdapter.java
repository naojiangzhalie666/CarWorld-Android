package com.liansheng.carworld.adapter.company;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextDirectionHeuristics;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.liansheng.carworld.R;
import com.liansheng.carworld.activity.logic.OtherLogic;
import com.liansheng.carworld.bean.home.SaleCardBean;
import com.liansheng.carworld.net.UrlKit;
import com.liansheng.carworld.utils.DateParserHelper;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

import cn.droidlover.xdroid.tools.utils.ActivityManage;
import cn.droidlover.xdroid.tools.utils.ContextHolder;
import cn.droidlover.xdroid.tools.utils.DensityUtil;
import cn.droidlover.xdroid.tools.utils.StringFormat;

public class CarManagerAdapter extends BaseQuickAdapter<SaleCardBean, BaseViewHolder> {

    private boolean checkMode;

    private int listType;

    public CarManagerAdapter() {
        super(R.layout.item_car_manager);
    }

    @Override
    protected void convert(@NotNull BaseViewHolder holder, SaleCardBean bean) {
        CheckBox ckSelect = holder.getView(R.id.ck_select);
        TextView carUp = holder.getView(R.id.item_car_up);
        TextView carSale = holder.getView(R.id.item_car_down);
        TextView carShare = holder.getView(R.id.item_car_share);
        TextView carRefresh = holder.getView(R.id.item_car_refresh);
        TextView carRecover = holder.getView(R.id.item_car_recover);
        TextView carDelete = holder.getView(R.id.item_car_delete);
        if (bean.isOff()) {
            holder.setGone(R.id.item_car_refresh, true);
        } else {
            holder.setGone(R.id.item_car_refresh, false);
        }
        Context context = ContextHolder.getContext();
        if (checkMode) {
            ckSelect.setVisibility(View.VISIBLE);
            if(listType==3){
                holder.setGone(R.id.ll_type_1,true);
                holder.setGone(R.id.ll_type_2,false);
                Drawable recoverDra = context.getDrawable(R.mipmap.ic_company_recover_grey);
                recoverDra.setBounds(0, 0, recoverDra.getIntrinsicWidth(), recoverDra.getIntrinsicHeight());
                carRecover.setCompoundDrawables(null, recoverDra, null, null);
                Drawable deleteDra = context.getDrawable(R.mipmap.ic_company_delete_grey);
                deleteDra.setBounds(0, 0, deleteDra.getIntrinsicWidth(), deleteDra.getIntrinsicHeight());
                carDelete.setCompoundDrawables(null, deleteDra, null, null);
            }else {
                holder.setGone(R.id.ll_type_1,false);
                holder.setGone(R.id.ll_type_2,true);
                Drawable upDra;
                if (bean.isOff()) {
                    carUp.setText("转批发");
                    upDra = context.getDrawable(R.mipmap.ic_company_up_grey);
                } else {
                    carUp.setText("转自销");
                    upDra = context.getDrawable(R.mipmap.ic_company_down_grey);
                }
                upDra.setBounds(0, 0, upDra.getIntrinsicWidth(), upDra.getIntrinsicHeight());
                carUp.setCompoundDrawables(null, upDra, null, null);
                Drawable saleDra = context.getDrawable(R.mipmap.ic_company_sale_grey);
                saleDra.setBounds(0, 0, saleDra.getIntrinsicWidth(), saleDra.getIntrinsicHeight());
                carSale.setCompoundDrawables(null, saleDra, null, null);
                Drawable shareDra = context.getDrawable(R.mipmap.ic_company_share_grey);
                shareDra.setBounds(0, 0, shareDra.getIntrinsicWidth(), shareDra.getIntrinsicHeight());
                carShare.setCompoundDrawables(null, shareDra, null, null);
                Drawable refreshDra = context.getDrawable(R.mipmap.ic_company_refresh_grey);
                refreshDra.setBounds(0, 0, refreshDra.getIntrinsicWidth(), refreshDra.getIntrinsicHeight());
                carRefresh.setCompoundDrawables(null, refreshDra, null, null);
            }
        } else {
            ckSelect.setVisibility(View.INVISIBLE);
            if(listType==3){
                holder.setGone(R.id.ll_type_1,true);
                holder.setGone(R.id.ll_type_2,false);
                Drawable recoverDra = context.getDrawable(R.mipmap.ic_company_recover);
                recoverDra.setBounds(0, 0, recoverDra.getIntrinsicWidth(), recoverDra.getIntrinsicHeight());
                carRecover.setCompoundDrawables(null, recoverDra, null, null);
                Drawable deleteDra = context.getDrawable(R.mipmap.ic_company_delete);
                deleteDra.setBounds(0, 0, deleteDra.getIntrinsicWidth(), deleteDra.getIntrinsicHeight());
                carDelete.setCompoundDrawables(null, deleteDra, null, null);
            }else {
                holder.setGone(R.id.ll_type_1,false);
                holder.setGone(R.id.ll_type_2,true);
                Drawable upDra;
                if (bean.isOff()) {
                    carUp.setText("转批发");
                    upDra = context.getDrawable(R.mipmap.ic_company_up);
                } else {
                    carUp.setText("转自销");
                    upDra = context.getDrawable(R.mipmap.ic_company_down);
                }
                upDra.setBounds(0, 0, upDra.getIntrinsicWidth(), upDra.getIntrinsicHeight());
                carUp.setCompoundDrawables(null, upDra, null, null);
                Drawable saleDra = context.getDrawable(R.mipmap.ic_company_sale);
                saleDra.setBounds(0, 0, saleDra.getIntrinsicWidth(), saleDra.getIntrinsicHeight());
                carSale.setCompoundDrawables(null, saleDra, null, null);
                Drawable shareDra = context.getDrawable(R.mipmap.ic_company_share);
                shareDra.setBounds(0, 0, shareDra.getIntrinsicWidth(), shareDra.getIntrinsicHeight());
                carShare.setCompoundDrawables(null, shareDra, null, null);
                Drawable refreshDra = context.getDrawable(R.mipmap.ic_company_refresh);
                refreshDra.setBounds(0, 0, refreshDra.getIntrinsicWidth(), refreshDra.getIntrinsicHeight());
                carRefresh.setCompoundDrawables(null, refreshDra, null, null);
            }
        }
        if (bean.isTodayUpdated()) {
            Drawable refreshDra = context.getDrawable(R.mipmap.ic_company_refresh_grey);
            refreshDra.setBounds(0, 0, refreshDra.getIntrinsicWidth(), refreshDra.getIntrinsicHeight());
            carRefresh.setCompoundDrawables(null, refreshDra, null, null);
        }
        ckSelect.setChecked(bean.isChecked());
        ckSelect.setOnCheckedChangeListener((buttonView, isChecked) -> {
            bean.setChecked(isChecked);
        });
        holder.setText(R.id.tv_car, bean.getBrand_fct() + " " + bean.getBrand_serie());
        if (bean.getRetail_price() > 0) {
            holder.setVisible(R.id.ll_ls, true);
            holder.setText(R.id.tv_ls, StringFormat.doubleFormatForW2(bean.getRetail_price()));
        } else {
            holder.setVisible(R.id.ll_ls, false);
        }
        if (bean.getInside_price() > 0) {
            holder.setVisible(R.id.ll_nb, true);
            holder.setText(R.id.tv_nb, StringFormat.doubleFormatForW2(bean.getInside_price()));
        } else {
            holder.setVisible(R.id.ll_nb, false);
        }
        holder.setText(R.id.tv_jg, StringFormat.doubleFormatForW2(bean.getSell_price()));
//        holder.setText(R.id.tv_pass, DateParserHelper.getYearAndMonthAndDay2(bean.getPassedDate()) + "发布");

        ImageView img1 = holder.getView(R.id.iv_img);
        if (bean.getImages() != null && bean.getImages().size() > 0) {
//            Glide.with(ActivityManage.peek()).load(UrlKit.getImgUrl(bean.getImages().get(0))).into(img1);
            OtherLogic.updateAdapterImg(img1, bean.getImages().get(0));
        } else {
            Glide.with(ActivityManage.peek()).load(R.mipmap.bg_login).into(img1);
        }

    }

    public void setCheckMode(boolean checkMode) {
        this.checkMode = checkMode;
        notifyDataSetChanged();
    }


    public void setListType(int listType) {
        this.listType = listType;
        notifyDataSetChanged();
    }
}
