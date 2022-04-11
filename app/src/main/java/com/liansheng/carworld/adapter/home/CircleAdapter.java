package com.liansheng.carworld.adapter.home;

import android.content.Intent;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.liansheng.carworld.R;
import com.liansheng.carworld.activity.circle.CircleDetailsAct;
import com.liansheng.carworld.activity.home.CompanyInfoAct;
import com.liansheng.carworld.activity.logic.OtherLogic;
import com.liansheng.carworld.activity.logic.UserLogic;
import com.liansheng.carworld.bean.UserInfo;
import com.liansheng.carworld.bean.home.CircleBean;
import com.liansheng.carworld.bean.home.CommentsDTO;
import com.liansheng.carworld.kit.Constant;
import com.liansheng.carworld.net.JsonCallback;
import com.liansheng.carworld.net.NetApi;
import com.liansheng.carworld.net.UrlKit;
import com.liansheng.carworld.utils.DateParserHelper;
import com.liansheng.carworld.utils.GlideEngine;
import com.liansheng.carworld.utils.Utils;
import com.liansheng.carworld.view.pop.CircleAskPop;
import com.liansheng.carworld.view.pop.PopClick;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.entity.LocalMedia;

import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.droidlover.xdroid.hold.info.SharedInfo;
import cn.droidlover.xdroid.tools.utils.ActivityManage;
import cn.droidlover.xdroid.tools.utils.ContextHolder;
import cn.droidlover.xdroid.tools.utils.StringFormat;
import cn.droidlover.xdroid.tools.utils.Util;

public class CircleAdapter extends BaseQuickAdapter<CircleBean, BaseViewHolder> {

    //修改
    private int type = 1;//1 咨询 2 选择 3查阅 4删除

    private double day = 1000 * 60 * 60 * 24;
    private double hours = 1000 * 60 * 60;
    private CircleBean circleBean;
    private CircleAskPop askPop;

    public CircleAdapter(int type) {
        super(R.layout.item_circle);
        this.type = type;
        askPop = new CircleAskPop(ContextHolder.getContext(), (view, content) -> {
            if (!TextUtils.isEmpty(content) && circleBean != null) {
//                circleBean.getId()
                Map<String, Object> map = new HashMap<>();
                map.put("carMomentId", circleBean.getId());
                map.put("relationId", circleBean.getUserId());
                map.put("text", content);
                NetApi.postJson(UrlKit.USER_CAR_MOMENT_COMMENT, new JSONObject(map).toString(), new JsonCallback() {
                    @Override
                    public void onSuccess(String data, int id) {
                        CommentsDTO dto = new CommentsDTO();
                        dto.setOwner(UserLogic.getRealName());
                        dto.setOwnerId(SharedInfo.getInstance().getEntity(UserInfo.class).getId());
                        dto.setRelation(circleBean.getUser().getName());
                        dto.setRelationId(circleBean.getUserId());
                        dto.setText(content);
                        circleBean.getComments().add(dto);
                        notifyItemChanged(getItemPosition(circleBean), circleBean);
                    }
                });
            }
        });
    }

    @Override
    protected void convert(@NonNull BaseViewHolder holder, CircleBean item) {
        holder.setGone(R.id.item_circle_company_name, true);
        holder.setGone(R.id.item_circle_company,true);
        holder.setText(R.id.item_circle_name, "");
        Glide.with(ContextHolder.getContext()).load(R.mipmap.logo)
                .into((ImageView) holder.getView(R.id.item_circle_header));
        if (item.getUser() != null) {
            holder.setText(R.id.item_circle_name, item.getUser().getName());
            if(item.getUser().getPhoto() != null) {
                Glide.with(ContextHolder.getContext()).load(UrlKit.getImgUrl(item.getUser().getPhoto()))
                        .into((ImageView) holder.getView(R.id.item_circle_header));
            }
            if (!TextUtils.isEmpty(item.getUser().getCompany())) {
                holder.setText(R.id.item_circle_company_name, item.getUser().getCompany());
                holder.setGone(R.id.item_circle_company_name, false);
                holder.setGone(R.id.item_circle_company,false);
            }
        }
        holder.setText(R.id.item_circle_content, item.getText());
        if ("Seek".equals(item.getType())) {
            holder.setText(R.id.item_circle_under, "求购");
            holder.setGone(R.id.item_circle_sale, true);
            holder.setBackgroundResource(R.id.item_circle_under, R.drawable.shape_button_circle_buy);
        } else {
            holder.setBackgroundResource(R.id.item_circle_under, R.drawable.shape_button_circle_sale);
            if ("Minimum".equals(item.getPriceType())) {
                holder.setText(R.id.item_circle_under, "底价");
            } else {
                holder.setText(R.id.item_circle_under, "可议价");
            }
            holder.setText(R.id.item_circle_sale, "售价：" + StringFormat.doubleFormatForW(item.getPrice()));
            holder.setBackgroundResource(R.id.item_circle_sale, R.drawable.shape_button_circle_price);
            holder.setGone(R.id.item_circle_sale, false);
        }
        double current = new Date().getTime() - dateToStamp(item.getCreation());
        String dateContent;
        if (current > day) {
            dateContent = DateParserHelper.getnumberDateFormat(item.getCreation());
        } else {
            double currentHours = Math.floor(current / hours);
            if (currentHours < 1) {
                dateContent = "1小时内";
            } else {
                dateContent = StringFormat.subZeroAndDot(currentHours) + "小时前";
            }
        }
        if (item.getCity() == null) {
            holder.setText(R.id.item_circle_city_date, dateContent);
        } else {
            holder.setText(R.id.item_circle_city_date, item.getCity() + "·" + dateContent);
        }
        RecyclerView imgs = holder.getView(R.id.item_imgs);
        if (item.getImages() == null || item.getImages().size() == 0) {
            imgs.setVisibility(View.GONE);
        } else {
            CircleImgAdapter imgAdapter;
            imgs.setVisibility(View.VISIBLE);
            imgs.setLayoutManager(new GridLayoutManager(ContextHolder.getContext(), 3));
            if (item.getImages().size() > 3) {
                ArrayList<String> list = new ArrayList<>();
                for (int i = 0; i < (type == 3 ? item.getImages().size() : 3); i++) {
                    list.add(item.getImages().get(i));
                }
                imgAdapter = new CircleImgAdapter(type, list, item.getImages().size());
                imgs.setAdapter(imgAdapter);
            } else {
                imgAdapter = new CircleImgAdapter(type, item.getImages(), item.getImages().size());
                imgs.setAdapter(imgAdapter);
            }
            if (type != 2) {
                imgAdapter.setOnItemClickListener((adapter, view, position) -> {
                    if (type == 3) {
                        List<LocalMedia> images = new ArrayList<LocalMedia>();
                        for (int i = 0; i < item.getImages().size(); i++) {
                            LocalMedia localMedia = new LocalMedia(UrlKit.getImgUrl(item.getImages().get(i)), 0, 0, "");
                            images.add(localMedia);
                        }
                        PictureSelector.create(ActivityManage.peek())
                                .themeStyle(R.style.picture_default_style)
                                .isNotPreviewDownload(true)
                                .imageEngine(GlideEngine.createGlideEngine())
                                .openExternalPreview(position, images);
                    } else {
                        Intent intent = new Intent();
                        intent.putExtra(Constant.KEY_BEAN, item);
                        ActivityManage.push(CircleDetailsAct.class, intent);
                    }
                });
            }
        }

        if (type == 4) {
            holder.setVisible(R.id.item_circle_ask, false);
            holder.setVisible(R.id.item_circle_phone, false);
            holder.setGone(R.id.item_circle_delete, false);
        } else {
            holder.setVisible(R.id.item_circle_ask, true);
            holder.setVisible(R.id.item_circle_phone, true);
            UserInfo userInfo = SharedInfo.getInstance().getEntity(UserInfo.class);
            if(type==1){
                holder.setGone(R.id.item_circle_delete, userInfo == null || !userInfo.getId().equals(item.getUserId()));
            }else {
                holder.setGone(R.id.item_circle_delete, true);
            }
            holder.getView(R.id.item_circle_ask).setOnClickListener(view -> {
                circleBean = item;
                if (!askPop.isShowing()) {
                    if(item.getUser()!=null){
                        askPop.setUser(item.getUser().getName());
                    }
                    askPop.showAtLocation(view, Gravity.BOTTOM, 0, 0);
                }
            });
            holder.getView(R.id.item_circle_phone).setOnClickListener(view -> {
                OtherLogic.toCall(ActivityManage.peek(), item.getUser().getMobile());
            });
        }
        if (type == 1 || type == 3 || type == 4) {
            holder.setGone(R.id.item_comments, false);
            holder.setGone(R.id.item_circle_else, false);

            RecyclerView comments = holder.getView(R.id.item_comments);
            if (item.getComments() == null || item.getComments().size() == 0) {
                comments.setVisibility(View.GONE);
            } else {
                comments.setVisibility(View.VISIBLE);
                comments.setLayoutManager(new LinearLayoutManager(ContextHolder.getContext()));
                comments.setAdapter(new CircleCommentAdapter(this, item, item.getComments()));
            }
            holder.getView(R.id.item_circle_personal).setOnClickListener(view -> {
                Intent intent = new Intent();
                intent.putExtra(Constant.KEY_ID, item.getUserId());
                ActivityManage.push(CompanyInfoAct.class, intent);
            });
        } else if (type == 2) {
            holder.setGone(R.id.item_comments, true);
            holder.setGone(R.id.item_circle_else, true);
        }

    }

    private long dateToStamp(String s) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        Date date = null;
        try {
            date = simpleDateFormat.parse(s);
        } catch (Exception e) {
            date = new Date();
            e.printStackTrace();
        }
        return date.getTime();
    }
}
