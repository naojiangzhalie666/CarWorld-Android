package com.liansheng.carworld.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.kongzue.dialog.v3.MessageDialog;
import com.liansheng.carworld.R;
import com.liansheng.carworld.activity.company.CarHistoryAct;
import com.liansheng.carworld.activity.company.CompanyFindCarAct;
import com.liansheng.carworld.activity.home.ChooseCarAllActivity;
import com.liansheng.carworld.activity.home.SaleCarDetailsAct;
import com.liansheng.carworld.activity.logic.OtherLogic;
import com.liansheng.carworld.adapter.company.CarManagerAdapter;
import com.liansheng.carworld.bean.ListDeleteBean;
import com.liansheng.carworld.bean.home.SaleCardBean;
import com.liansheng.carworld.bean.other.PageBean;
import com.liansheng.carworld.kit.Constant;
import com.liansheng.carworld.net.JsonCallback;
import com.liansheng.carworld.net.NetApi;
import com.liansheng.carworld.net.UrlKit;
import com.liansheng.carworld.utils.ViewUtils;
import com.liansheng.carworld.view.PassDateCarDialog;
import com.liansheng.carworld.view.pop.PriceSharePop;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import cn.droidlover.xdroid.hold.info.SharedInfo;
import cn.droidlover.xdroid.tools.utils.ActivityManage;
import cn.droidlover.xdroid.tools.utils.StringFormat;
import cn.droidlover.xdroid.tools.utils.Util;
import cn.droidlover.xrecyclerview.XRecyclerContentLayout;
import cn.droidlover.xrecyclerview.XRecyclerView;
import okhttp3.Call;

public class SecondCardFrag extends BaseFragment {

    @BindView(R.id.et_search)
    EditText etSearch;
    @BindView(R.id.car_num)
    TextView carNum;
    @BindView(R.id.company_line_0)
    View line0;
    @BindView(R.id.company_line_1)
    View line1;
    @BindView(R.id.company_line_2)
    View line2;
    @BindView(R.id.company_line_3)
    View line3;
    @BindView(R.id.company_edit_all)
    TextView company_edit_all;
    @BindView(R.id.company_select_all)
    CheckBox companyAll;
    @BindView(R.id.company_up)
    TextView companyUp;
    @BindView(R.id.company_down)
    TextView companyDown;
    @BindView(R.id.company_delete)
    TextView companyDelete;
    @BindView(R.id.company_recover)
    TextView companyRecover;
    @BindView(R.id.recycler_view)
    XRecyclerContentLayout contentLayout;
    @BindView(R.id.manager_line)
    LinearLayout managerLine;
    @BindView(R.id.tv_all_count)
    TextView tvAllCount;
    @BindView(R.id.tv_up_count)
    TextView tvUpCount;
    @BindView(R.id.tv_down_count)
    TextView tvDownCount;
    @BindView(R.id.tv_sold_count)
    TextView tv_sold_count;

    private CarManagerAdapter mAdapter;
    private List<String> ids = new ArrayList<>();

    private String searchContent;

    int mType = 0;//0 全部 1 已上架 2 未上架
    private PriceSharePop sharePop;

    public static SecondCardFrag newInstance() {
        Bundle args = new Bundle();
        SecondCardFrag fragment = new SecondCardFrag();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        super.initData(savedInstanceState);
        sharePop = new PriceSharePop(context);
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (TextUtils.isEmpty(s)) {
                    searchContent = "";
                } else {
                    searchContent = s.toString();
                }
                getData(0);
            }
        });
        initAdapter();
    }


    @Override
    public void onResume() {
        super.onResume();
        getCarCount();
        getData(0);
    }

    private void getCarCount() {
        NetApi.get(UrlKit.CAR_RESOURCE_COUNT, null, new JsonCallback() {

            @Override
            public void onSuccess(String response, int id) {
                Map<String, Integer> map = new Gson().fromJson(response, Map.class);
                if (map != null) {
                    carNum.setText("车源\n" + StringFormat.subZeroAndDot(map.get("all")));
                    tvAllCount.setText("库存( " + StringFormat.subZeroAndDot(map.get("all")) + " )");
                    tvUpCount.setText("批发( " + StringFormat.subZeroAndDot(map.get("on")) + " )");
                    tvDownCount.setText("自销( " + StringFormat.subZeroAndDot(map.get("off")) + " )");
                    tv_sold_count.setText("已售记录( " + StringFormat.subZeroAndDot(map.get("sold")) + " )");
                }
            }
        });
    }

    private void initAdapter() {
        mAdapter = new CarManagerAdapter();
        mAdapter.addChildClickViewIds(R.id.item_car_up, R.id.item_car_down, R.id.item_car_share,
                R.id.item_car_refresh, R.id.item_car_delete, R.id.item_car_recover);
        mAdapter.setOnItemChildClickListener((adapter, view, position) -> {
            SaleCardBean item = (SaleCardBean) adapter.getItem(position);
            switch (view.getId()) {
                case R.id.item_car_up:
                    ids.clear();
                    ids.add(item.getId());
                    if (item.isOff()) {
                        carUp(ids);
                    } else {
                        MessageDialog.build((AppCompatActivity) getActivity())
                                .setTitle("温馨提示")
                                .setMessage("自销仅作内部库存管理，转自销后将不在批发平台显示")
                                .setCancelButton("取消", (baseDialog, v) -> false)
                                .setOkButton("确定", (baseDialog, v) -> {
                                    carDown(ids);
                                    return false;
                                })
                                .show();
                    }
                    break;
                case R.id.item_car_down:
                    carSale(item.getId());
                    break;
                case R.id.item_car_refresh:
                    updateCar(item.getId());
                    break;
                case R.id.item_car_share:
                    sharePop.setPrice(item);
                    sharePop.showAtLocation(view, Gravity.CENTER, 0, 0);
                    break;
                case R.id.item_car_delete:
                    ids.clear();
                    ids.add(item.getId());
                    MessageDialog.build((AppCompatActivity) getActivity())
                            .setTitle("温馨提示")
                            .setMessage("您是否要删除？（删除后不可恢复!）")
                            .setCancelButton("取消", (baseDialog, v) -> false)
                            .setOkButton("确定", (baseDialog, v) -> {
                                deleteCar(ids);
                                return false;
                            })
                            .show();
                    break;
                case R.id.item_car_recover:
                    MessageDialog.build((AppCompatActivity) getActivity())
                            .setTitle("温馨提示")
                            .setMessage("是否进行恢复？")
                            .setCancelButton("取消", (baseDialog, v) -> false)
                            .setOkButton("确定", (baseDialog, v) -> {
                                recover(item.getId());
                                return false;
                            })
                            .show();
                    break;
            }
        });
        contentLayout.getRecyclerView().verticalLayoutManager(context);
        contentLayout.getRecyclerView().setAdapter(mAdapter);
        contentLayout.getSwipeRefreshLayout().setColorSchemeResources(R.color.order, R.color.order2, R.color.end);
        contentLayout.emptyView(ViewUtils.getEmptyView(getActivity(), R.mipmap.bg_cy));
        contentLayout.loadingView(View.inflate(getActivity(), R.layout.view_loading, null));
        contentLayout.getRecyclerView().useDefLoadMoreView();
        contentLayout.getRecyclerView().addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
        contentLayout.getRecyclerView().setBackgroundResource(R.color.white);
        contentLayout.getRecyclerView().setOnRefreshAndLoadMoreListener(new XRecyclerView.OnRefreshAndLoadMoreListener() {
            @Override
            public void onRefresh() {
                getData(0);
            }

            @Override
            public void onLoadMore(int page) {
                getData(page);
            }
        });
        mAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull BaseQuickAdapter adapter, @NonNull View view, int position) {
                SaleCardBean item = (SaleCardBean) adapter.getItem(position);
                if (View.VISIBLE == companyAll.getVisibility()) {
                    item.setChecked(!item.isChecked());
                    adapter.notifyItemChanged(position);
                } else {
                    Intent intent = new Intent();
                    intent.putExtra(Constant.KEY_BEAN, item);
                    intent.putExtra(Constant.KEY_TYPE, true);
                    ActivityManage.push(SaleCarDetailsAct.class, intent);
                }
            }
        });
        companyAll.setOnCheckedChangeListener((buttonView, isChecked) -> {
            for (int i = 0; i < mAdapter.getData().size(); i++) {
                SaleCardBean bean = mAdapter.getData().get(i);
                bean.setChecked(isChecked);
            }
            mAdapter.notifyDataSetChanged();
        });
    }

    @Override
    public int getLayoutId() {
        return R.layout.act_company_manager;
    }

    @OnClick({R.id.company_edit_all, R.id.car_in, R.id.company_find, R.id.company_rl_0, R.id.company_rl_1, R.id.company_rl_2, R.id.company_rl_3,
            R.id.company_delete, R.id.company_recover, R.id.company_down, R.id.company_up, R.id.company_cancel})
    public void onViewClicker(View view) {
        switch (view.getId()) {
            case R.id.company_edit_all:
                setGone();
                managerLine.setVisibility(View.VISIBLE);
                if (mType == 0) {
                    companyUp.setVisibility(View.VISIBLE);
                    companyDown.setVisibility(View.VISIBLE);
                    line0.setVisibility(View.VISIBLE);
                } else if (mType == 1) {
                    companyDown.setVisibility(View.VISIBLE);
                    line1.setVisibility(View.VISIBLE);
                } else if (mType == 2) {
                    companyUp.setVisibility(View.VISIBLE);
                    line2.setVisibility(View.VISIBLE);
                } else if (mType == 3) {
                    line3.setVisibility(View.VISIBLE);
                    companyDelete.setVisibility(View.VISIBLE);
                    companyRecover.setVisibility(View.VISIBLE);
                }
                mAdapter.setCheckMode(true);
                companyAll.setVisibility(View.VISIBLE);
                company_edit_all.setVisibility(View.GONE);
                break;
            case R.id.car_in:
                SharedInfo.getInstance().saveValue("car_type", "inter");
                ActivityManage.push(ChooseCarAllActivity.class);
                break;
            case R.id.company_find:
                ActivityManage.push(CompanyFindCarAct.class);
                break;
            case R.id.company_rl_0:
                setGone();
                line0.setVisibility(View.VISIBLE);
                mType = 0;
                mAdapter.setListType(mType);
                setCheck();
                break;
            case R.id.company_rl_1:
                setGone();
                line1.setVisibility(View.VISIBLE);
                mType = 1;
                setCheck();
                mAdapter.setListType(mType);
                break;
            case R.id.company_rl_2:
                setGone();
                line2.setVisibility(View.VISIBLE);
                mType = 2;
                mAdapter.setListType(mType);
                setCheck();
                break;
            case R.id.company_rl_3:
                setGone();
                line3.setVisibility(View.VISIBLE);
                mType = 3;
                mAdapter.setListType(mType);
                setCheck();
                break;
            case R.id.company_up:
            case R.id.company_down:
            case R.id.company_delete:
            case R.id.company_recover:
                ids.clear();
                List<SaleCardBean> cardBeans = mAdapter.getData();
                for (int i = 0; i < cardBeans.size(); i++) {
                    if (cardBeans.get(i).isChecked()) {
                        ids.add(cardBeans.get(i).getId());
                    }
                }
                if (ids.size() == 0) {
                    Util.toast("请选择车型");
                    return;
                }
                if (view.getId() == R.id.company_down) {
                    MessageDialog.build((AppCompatActivity) getActivity())
                            .setTitle("温馨提示")
                            .setMessage("是否进行批量自销？")
                            .setCancelButton("取消", (baseDialog, v) -> false)
                            .setOkButton("确定", (baseDialog, v) -> {
                                carDown(ids);
                                return false;
                            })
                            .show();
                } else if (view.getId() == R.id.company_up) {
                    MessageDialog.build((AppCompatActivity) getActivity())
                            .setTitle("温馨提示")
                            .setMessage("是否要批量批发？")
                            .setCancelButton("取消", (baseDialog, v) -> false)
                            .setOkButton("确定", (baseDialog, v) -> {
                                carUp(ids);
                                return false;
                            })
                            .show();
                } else if (view.getId() == R.id.company_delete) {
                    MessageDialog.build((AppCompatActivity) getActivity())
                            .setTitle("温馨提示")
                            .setMessage("是否要批量删除？（删除后不可恢复!）")
                            .setCancelButton("取消", (baseDialog, v) -> false)
                            .setOkButton("确定", (baseDialog, v) -> {
                                deleteCar(ids);
                                return false;
                            })
                            .show();
                } else if (view.getId() == R.id.company_recover) {
                    ListDeleteBean deleteBean = new ListDeleteBean();
                    List<ListDeleteBean.Item> items = new ArrayList<>();
                    for (int i = 0; i < ids.size(); i++) {
                        ListDeleteBean.Item item = new ListDeleteBean.Item();
                        item.setId(ids.get(i));
                        item.setSold(false);
                        item.setOff(true);
                        items.add(item);
                    }
                    deleteBean.setItems(items);
                    MessageDialog.build((AppCompatActivity) getActivity())
                            .setTitle("温馨提示")
                            .setMessage("是否要批量恢复？")
                            .setCancelButton("取消", (baseDialog, v) -> false)
                            .setOkButton("确定", (baseDialog, v) -> {
                                recover(deleteBean);
                                return false;
                            })
                            .show();
                }
                break;
            case R.id.company_cancel:
                companyAll.setChecked(false);
                setGone();
//                if (mType == 0) {
//                    line0.setVisibility(View.VISIBLE);
//                } else if (mType == 1) {
//                    line1.setVisibility(View.VISIBLE);
//                } else if (mType == 2) {
//                    line2.setVisibility(View.VISIBLE);
//                }
                mAdapter.setCheckMode(false);
                managerLine.setVisibility(View.GONE);
                companyAll.setVisibility(View.GONE);
                company_edit_all.setVisibility(View.VISIBLE);
                break;
        }
    }

    private void setCheck() {
        searchContent = "";
        companyAll.setChecked(false);
        mAdapter.setCheckMode(false);
        managerLine.setVisibility(View.GONE);
        companyAll.setVisibility(View.GONE);
        company_edit_all.setVisibility(View.VISIBLE);
        getData(0);
    }

    private void setGone() {
        line0.setVisibility(View.GONE);
        line1.setVisibility(View.GONE);
        line2.setVisibility(View.GONE);
        line3.setVisibility(View.GONE);
        managerLine.setVisibility(View.GONE);
        companyUp.setVisibility(View.GONE);
        companyDown.setVisibility(View.GONE);
        companyDelete.setVisibility(View.GONE);
        companyRecover.setVisibility(View.GONE);
    }

    private void getData(int page) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("pageIndex", page);
        map.put("pageSize", Constant.DEFULT_RESULT);
        if (!TextUtils.isEmpty(searchContent)) {
            map.put("keywords", searchContent);
        }
        if (mType == 1) {
            map.put("off", false);
        } else if (mType == 2) {
            map.put("off", true);
        } else if (mType == 3) {
            map.put("sold", true);
        }
        NetApi.get(UrlKit.CAR_RESOURCE, map, new JsonCallback() {

            @Override
            public void onSuccess(String response, int id) {
                    PageBean<SaleCardBean> data = new Gson().fromJson(response, new TypeToken<PageBean<SaleCardBean>>() {
                    }.getType());
                    OtherLogic.updateAdapter(page, mAdapter, data, contentLayout);
            }
        });
    }

    private void recover(String id) {
        NetApi.put(UrlKit.CAR_RESOURCE_SOLD + id + "?sold=false&off=true", "", new JsonCallback() {

            @Override
            public void onSuccess(String response, int id) {
                Util.toast("操作成功");
                getData(0);
                getCarCount();
            }
        });
    }

    private void recover(ListDeleteBean bean) {
        NetApi.put("api/app/car_resource/sold", new Gson().toJson(bean), new JsonCallback() {

            @Override
            public void onSuccess(String response, int id) {
                Util.toast("操作成功");
                getData(0);
                getCarCount();
            }
        });
    }

    private void deleteCar(List<String> ids) {
        NetApi.delete(UrlKit.CAR_RESOURCE, new JsonCallback() {

            @Override
            public void onSuccess(String response, int id) {
                Util.toast("操作成功");
                getData(0);
                getCarCount();
            }
        }, new Gson().toJson(ids));
    }

    private void carDown(List<String> ids) {
        NetApi.put(UrlKit.CAR_RESOURCE_OFF, new Gson().toJson(ids), new JsonCallback() {

            @Override
            public void onSuccess(String response, int id) {
                Util.toast("操作成功");
                getData(0);
                companyAll.setChecked(false);
                getCarCount();
            }
        });
    }

    private void carUp(List<String> ids) {
        NetApi.put(UrlKit.CAR_RESOURCE_ON, new Gson().toJson(ids), new JsonCallback() {

            @Override
            public void onSuccess(String response, int id) {
                Util.toast("操作成功");
                getData(0);
                companyAll.setChecked(false);
                getCarCount();
            }
        });
    }

    //flag true 弹窗已售
    private void carSale(String carId) {
        NetApi.put(UrlKit.CAR_RESOURCE_SOLD + carId, "", new JsonCallback() {

            @Override
            public void onSuccess(String response, int id) {
                Util.toast("操作成功");
                getCarCount();
                getData(0);
            }
        });
    }

    private void updateCar(String carId) {
        NetApi.put(UrlKit.CAR_RESOURCE_UPDATE_DATE + carId, "", new JsonCallback() {

            @Override
            public void onSuccess(String response, int id) {
                Util.toast("刷新成功");
                getData(0);
            }
        });
    }

}
