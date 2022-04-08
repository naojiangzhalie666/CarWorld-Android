//package com.liansheng.carworld.adapter.home;
//
//import com.chad.library.adapter.base.BaseQuickAdapter;
//import com.chad.library.adapter.base.viewholder.BaseViewHolder;
//import com.liansheng.carworld.R;
//import com.liansheng.carworld.bean.Directory;
//import com.liansheng.carworld.utils.DateParserHelper;
//
//import org.jetbrains.annotations.NotNull;
//
//import java.util.List;
//
//public class HomeItemAdapter extends BaseQuickAdapter<Directory.ItemsBean, BaseViewHolder> {
//
//    public HomeItemAdapter(List<Directory.ItemsBean> beans) {
//        super(R.layout.item_new, beans);
//    }
//
//    @Override
//    protected void convert(@NotNull BaseViewHolder holder, Directory.ItemsBean item) {
//        holder.setText(R.id.tv_title, item.getBrand() + " " + item.getCompany());
//        holder.setText(R.id.tv_mobile,"二网手机:" + item.getPhone() +item.getName());
//        holder.setText(R.id.tv_phone,"座机号码:" + item.getMobile());
//        holder.setText(R.id.tv_date, DateParserHelper.getYearAndMonthAndDay(item.getCreation()) + "更新");
//    }
//}
