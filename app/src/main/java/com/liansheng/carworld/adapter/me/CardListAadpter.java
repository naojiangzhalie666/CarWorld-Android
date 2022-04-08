package com.liansheng.carworld.adapter.me;

import androidx.core.content.ContextCompat;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.liansheng.carworld.R;
import com.liansheng.carworld.bean.me.LicensesBean;
import com.liansheng.carworld.kit.Constant;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import cn.droidlover.xdroid.tools.utils.ContextHolder;
import cn.droidlover.xdroid.views.LeftRightLayout;

public class CardListAadpter extends BaseQuickAdapter<LicensesBean, BaseViewHolder> {

    public CardListAadpter(List<LicensesBean> data){
        super(R.layout.item_card_list,data);
    }

    @Override
    protected void convert(@NotNull BaseViewHolder baseViewHolder, LicensesBean item) {
        LeftRightLayout leftRightLayout= baseViewHolder.getView(R.id.item_card);

        if (Constant.ID_STATUS_PASSED.equals(item.getStatus())) {
            leftRightLayout.setRightText("已认证");
        } else if (Constant.ID_STATUS_VERIFYING.equals(item.getStatus())) {
            leftRightLayout.setRightText("认证中");
        } else {
            leftRightLayout.setRightText("已驳回");
        }
        if(Constant.AUTH_IDENTITY_CARD.equals(item.getType())){
            leftRightLayout.setLeftText("身份证");
            leftRightLayout.setIconImg(ContextCompat.getDrawable(ContextHolder.getContext(),R.mipmap.ic_card_sfz));
        }else if(Constant.AUTH_DRIVERS_LICENSE.equals(item.getType())){
            leftRightLayout.setLeftText("驾驶证");
            leftRightLayout.setIconImg(ContextCompat.getDrawable(ContextHolder.getContext(),R.mipmap.ic_card_jsz));
        }else if(Constant.AUTH_DRIVING_LICENSE.equals(item.getType())){
            leftRightLayout.setLeftText("行驶证");
            leftRightLayout.setIconImg(ContextCompat.getDrawable(ContextHolder.getContext(),R.mipmap.ic_card_xsz));
        }else if(Constant.AUTH_COMPULSORY_INSURANCE.equals(item.getType())){
            leftRightLayout.setLeftText("交强险");
            leftRightLayout.setIconImg(ContextCompat.getDrawable(ContextHolder.getContext(),R.mipmap.ic_card_jqx));
        }else if(Constant.AUTH_CARGO_INSURANCE.equals(item.getType())){
            leftRightLayout.setIconImg(ContextCompat.getDrawable(ContextHolder.getContext(),R.mipmap.ic_card_hyx));
            leftRightLayout.setLeftText("商业险");
        }else if(Constant.AUTH_COMMERCIAL_INSURANCE.equals(item.getType())){
            leftRightLayout.setLeftText("货运险");
            leftRightLayout.setIconImg(ContextCompat.getDrawable(ContextHolder.getContext(),R.mipmap.ic_card_syx));
        }
    }
}
