package com.liansheng.carworld.adapter.home;

import android.content.Intent;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.baidu.liantian.ac.U;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.liansheng.carworld.R;
import com.liansheng.carworld.activity.home.CompanyInfoAct;
import com.liansheng.carworld.activity.logic.UserLogic;
import com.liansheng.carworld.bean.UserInfo;
import com.liansheng.carworld.bean.home.CircleBean;
import com.liansheng.carworld.bean.home.CommentsDTO;
import com.liansheng.carworld.kit.Constant;
import com.liansheng.carworld.net.JsonCallback;
import com.liansheng.carworld.net.NetApi;
import com.liansheng.carworld.net.UrlKit;
import com.liansheng.carworld.view.pop.CircleAskPop;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.droidlover.xdroid.hold.info.SharedInfo;
import cn.droidlover.xdroid.tools.log.Logger;
import cn.droidlover.xdroid.tools.utils.ActivityManage;
import cn.droidlover.xdroid.tools.utils.ContextHolder;
import cn.droidlover.xdroid.tools.utils.Util;

public class CircleCommentAdapter extends BaseQuickAdapter<CommentsDTO, BaseViewHolder> {

    private CircleAskPop askPop;
    private CommentsDTO commentsDTO;

    public CircleCommentAdapter(CircleAdapter circleAdapter, CircleBean circleBean, @Nullable List<CommentsDTO> data) {
        super(R.layout.item_circle_comment, data);
        askPop = new CircleAskPop(ContextHolder.getContext(), (view, content) -> {
            if (!TextUtils.isEmpty(content)) {
//                circleBean.getId()
                Map<String, Object> map = new HashMap<>();
                map.put("carMomentId", circleBean.getId());
                map.put("relationId", commentsDTO.getOwnerId());
                map.put("text", content);
                NetApi.postJson(UrlKit.USER_CAR_MOMENT_COMMENT, new JSONObject(map).toString(), new JsonCallback() {
                    @Override
                    public void onSuccess(String data, int id) {
                        CommentsDTO dto = new CommentsDTO();
                        dto.setOwner(UserLogic.getRealName());
                        dto.setOwnerId(SharedInfo.getInstance().getEntity(UserInfo.class).getId());
                        dto.setRelation(commentsDTO.getOwner());
                        dto.setRelationId(commentsDTO.getOwnerId());
                        dto.setText(content);
                        circleBean.getComments().add(dto);
                        circleAdapter.notifyItemChanged(circleAdapter.getItemPosition(circleBean), circleBean);
                    }
                });
            }
        });
    }

    @Override
    protected void convert(@NonNull BaseViewHolder baseViewHolder, CommentsDTO item) {
        TextView tv = baseViewHolder.getView(R.id.item_txt);
        SpannableStringBuilder builder = new SpannableStringBuilder();
        tv.setHighlightColor(ContextCompat.getColor(ContextHolder.getContext(), android.R.color.transparent));
        builder.append(item.getOwner());
        builder.setSpan(new ClickableSpan() {
            @Override
            public void onClick(@NonNull View view) {
                Intent intent = new Intent();
                intent.putExtra(Constant.KEY_ID, item.getOwnerId());
                ActivityManage.push(CompanyInfoAct.class, intent);
            }

            @Override
            public void updateDrawState(@NonNull TextPaint ds) {
                super.updateDrawState(ds);
                ds.setUnderlineText(false);
            }
        }, 0, item.getOwner().length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        builder.setSpan(new ForegroundColorSpan(ContextCompat.getColor(ContextHolder.getContext(), R.color.colorDarkBlue)),
                0, item.getOwner().length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        int length = item.getOwner().length();
        if (item.getRelation() != null) {
            builder.append(" 回复 " + item.getRelation());
            length = length + 4;
            builder.setSpan(new ClickableSpan() {
                @Override
                public void onClick(@NonNull View view) {
                    Intent intent = new Intent();
                    intent.putExtra(Constant.KEY_ID, item.getRelationId());
                    ActivityManage.push(CompanyInfoAct.class, intent);
                }

                @Override
                public void updateDrawState(@NonNull TextPaint ds) {
                    super.updateDrawState(ds);
                    ds.setUnderlineText(false);
                }
            }, length, length + item.getRelation().length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            builder.setSpan(new ForegroundColorSpan(ContextCompat.getColor(ContextHolder.getContext(), R.color.colorDarkBlue)),
                    length, length + item.getRelation().length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            length = length + 1 + item.getRelation().length();
        } else {
            length = length + 1;
        }
        builder.append("：" + item.getText());
        builder.setSpan(new ClickableSpan() {
            @Override
            public void onClick(@NonNull View view) {
                commentsDTO = item;
                if (!askPop.isShowing()) {
                    askPop.setUser(item.getOwner());
                    askPop.showAtLocation(view, Gravity.BOTTOM, 0, 0);
                }
            }

            @Override
            public void updateDrawState(@NonNull TextPaint ds) {
                super.updateDrawState(ds);
                ds.setUnderlineText(false);
            }
        }, length, length + item.getText().length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        builder.setSpan(new ForegroundColorSpan(ContextCompat.getColor(ContextHolder.getContext(), R.color.color_666)),
                length, length + item.getText().length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        tv.setMovementMethod(LinkMovementMethod.getInstance());
        tv.setText(builder);
    }

}
