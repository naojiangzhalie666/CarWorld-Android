package com.liansheng.carworld.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.kongzue.dialog.v3.CustomDialog;
import com.liansheng.carworld.R;
import com.liansheng.carworld.activity.home.DirectoryActivity;
import com.liansheng.carworld.bean.Directory;
import com.liansheng.carworld.bean.UserInfo;
import com.liansheng.carworld.net.JsonCallback;
import com.liansheng.carworld.net.NetApi;
import com.liansheng.carworld.net.UrlKit;
import com.liansheng.carworld.utils.DateParserHelper;
import com.liansheng.carworld.utils.Utils;
import com.liansheng.carworld.utils.XToastUtils;
import com.liansheng.carworld.wxapi.WxUtils;
import com.qmuiteam.qmui.widget.QMUIRadiusImageView;
import com.xuexiang.xui.widget.button.shinebutton.ShineButton;

import org.angmarch.views.NiceSpinner;
import org.angmarch.views.OnSpinnerItemSelectedListener;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import butterknife.BindView;
import cn.droidlover.xdroid.base.SimpleRecAdapter;
import cn.droidlover.xdroid.hold.info.SharedInfo;
import cn.droidlover.xdroid.kit.KnifeKit;
import okhttp3.Call;

public class DirectoryAdapter extends SimpleRecAdapter<Directory.ItemsBean, DirectoryAdapter.ViewHolder> {


    private DirectoryActivity directoryActivity;
    private View main;

    public DirectoryAdapter(Context context, DirectoryActivity dir, View view) {
        super(context);
        directoryActivity = dir;
        main = view;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_directory, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        Directory.ItemsBean item = data.get(position);
        holder.tvName.setText(item.getCompany());
        holder.tvDate.setText(DateParserHelper.getYearAndMonthAndDay(item.getCreation().toString()) + "更新");
        holder.tvPhone.setText("4s座机号:" + item.getPhone());
        holder.tvMobile.setText("二网手机:" + item.getMobile() + " " + item.getName());
        holder.tvAddress.setText("地址:" + item.getAddress());
        if (item.getVoteCount() >= 0 && item.isVoted()) {
            holder.tvDzNum.setText(String.valueOf(item.getVoteCount()));
        } else {
            holder.tvDzNum.setText("点赞");
        }
        holder.btnGood.setChecked(item.isVoted());
        holder.btnCollection.setChecked(item.isCollected());
        if (item.getPhoto() != null && !item.getPhoto().equals("")) {
            Glide.with(directoryActivity).load(item.getPhoto()).into(holder.ivHeader);
        }
        holder.btnGood.setOnCheckStateChangeListener(new ShineButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(ShineButton shineButton, boolean isChecked) {
                if (isChecked) {
                    postDz(item.getId(), holder.btnGood, position);
                } else {
                    delDz(item.getId(), holder.btnGood, position);
                }
            }
        });
        holder.btnCollection.setOnCheckStateChangeListener(new ShineButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(ShineButton shineButton, boolean isChecked) {
                if (isChecked) {
                    postCollection(item.getId(), holder.btnCollection);

                } else {
                    delCollection(item.getId(), holder.btnCollection);
                }
            }
        });
        holder.llytJc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                suggestDialog(item);
            }
        });
        holder.llytShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                UrlKit.API_BASE_URL + "directory/info/" + item.getId()+"/"+SharedInfo.getInstance().getEntity(UserInfo.class).getId()
                WxUtils.getInstance().wxWeb(String.format(UrlKit.H5_SHARE_4S,item.getId(),SharedInfo.getInstance().getEntity(UserInfo.class).getId()), item.getCompany(),
                        "二网手机：" + item.getMobile() + " " + item.getName() + "\n" + "4s座机号：" + item.getPhone());
            }
        });
//        holder.tvMobile.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (!TextUtils.isEmpty(item.getMobile())) {
//                    directoryActivity.callPhone(directoryActivity, "请问是否要联系4S？", item.getMobile());
//                }
//
//            }
//        });
//        holder.tvPhone.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                directoryActivity.callPhone(directoryActivity, "请问是否要联系4S座机？", item.getPhone());
//            }
//        });
        holder.llPhone.setOnClickListener(v -> Utils.callPhone(directoryActivity, "请问是否要联系", item));

    }

    public void postDz(String id, ShineButton btn, int position) {
        NetApi.postVote(directoryActivity.getLoginResult().getToken(), id, new JsonCallback() {

            @Override
            public void onSuccess(String response, int id) {
                btn.setChecked(true);
                Directory.ItemsBean bean = data.get(position);
                bean.setVoteCount(bean.getVoteCount() + 1);
                bean.setVoted(true);
                notifyItemChanged(position);
                directoryActivity.showSnackToast(main, "已点赞");
            }
        });
    }

    public void delDz(String id, ShineButton btn, int position) {
        NetApi.delVote(directoryActivity.getLoginResult().getToken(), id, new JsonCallback() {

            @Override
            public void onSuccess(String response, int id) {
                btn.setChecked(false);
                Directory.ItemsBean bean = data.get(position);
                bean.setVoteCount(bean.getVoteCount() - 1);
                bean.setVoted(false);
                notifyItemChanged(position);
                directoryActivity.showSnackToast(main, "已取消点赞");
            }
        });
    }

    public void postCollection(String id, ShineButton btn) {
        NetApi.postCollection(directoryActivity.getLoginResult().getToken(), id, new JsonCallback() {

            @Override
            public void onSuccess(String response, int id) {
//                    XToastUtils.success("取消成功");
                btn.setChecked(true);
                directoryActivity.showSnackToast(main, "已收藏");
            }
        });
    }

    public void delCollection(String id, ShineButton btn) {
        NetApi.delCollection(directoryActivity.getLoginResult().getToken(), id, new JsonCallback() {

            @Override
            public void onSuccess(String response, int id) {
                btn.setChecked(false);
                directoryActivity.showSnackToast(main, "已取消收藏");
            }
        });
    }

    String type;

    public void suggestDialog(Directory.ItemsBean item) {
        List<String> dataset = new LinkedList<>(Arrays.asList("品牌错误", "手机错误", "座机错误"));
        type = dataset.get(0);
        CustomDialog.build(directoryActivity, R.layout.dialog_suggest, new CustomDialog.OnBindView() {
            @Override
            public void onBind(final CustomDialog dialog, View v) {
                NiceSpinner spinner = v.findViewById(R.id.sp_suggest);
                EditText et = v.findViewById(R.id.et_suggest);
                TextView price = v.findViewById(R.id.tv_price);
                Button btnOk = v.findViewById(R.id.btn_ok);
                ImageView close = v.findViewById(R.id.iv_close);
                spinner.attachDataSource(dataset);
                spinner.setOnSpinnerItemSelectedListener(new OnSpinnerItemSelectedListener() {
                    @Override
                    public void onItemSelected(NiceSpinner parent, View view, int position, long id) {
                        type = dataset.get(position);

                    }
                });
                btnOk.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        postSuggest(item.getId(), type + ":" + et.getText());
                        dialog.doDismiss();
                    }
                });
                close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.doDismiss();
                    }
                });
            }
        })
                .setFullScreen(true)
                .show();
    }

    public void postSuggest(String id, String description) {
        NetApi.postSuggest(directoryActivity.getLoginResult().getToken(), id, description, new JsonCallback() {

            @Override
            public void onSuccess(String response, int id) {
                directoryActivity.showSnackToast(main, "提交成功");
            }
        });
    }

    @Override
    public ViewHolder newViewHolder(View itemView) {
        return new ViewHolder(itemView);
    }

    @Override
    public int getLayoutId() {
        return R.layout.item_order_task_list;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_name)
        TextView tvName;
        @BindView(R.id.tv_date)
        TextView tvDate;
        @BindView(R.id.iv_header)
        QMUIRadiusImageView ivHeader;
        @BindView(R.id.tv_phone)
        TextView tvPhone;
        @BindView(R.id.tv_mobile)
        TextView tvMobile;
        @BindView(R.id.tv_address)
        TextView tvAddress;
        @BindView(R.id.btn_good)
        ShineButton btnGood;
        @BindView(R.id.btn_collection)
        ShineButton btnCollection;
        @BindView(R.id.llyt_dz)
        LinearLayout llytDz;
        @BindView(R.id.llyt_jc)
        LinearLayout llytJc;
        @BindView(R.id.llyt_collection)
        LinearLayout llytCollection;
        @BindView(R.id.llyt_share)
        LinearLayout llytShare;
        @BindView(R.id.tv_dz_num)
        TextView tvDzNum;
        @BindView(R.id.ll_phone)
        RelativeLayout llPhone;

        public ViewHolder(View itemView) {
            super(itemView);
            KnifeKit.bind(this, itemView);
        }
    }
}
