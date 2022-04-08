package com.liansheng.carworld.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.kongzue.dialog.interfaces.OnDialogButtonClickListener;
import com.kongzue.dialog.util.BaseDialog;
import com.kongzue.dialog.v3.CustomDialog;
import com.kongzue.dialog.v3.MessageDialog;
import com.liansheng.carworld.R;
import com.liansheng.carworld.activity.InfoActivity;
import com.liansheng.carworld.activity.InviteListActivity;
import com.liansheng.carworld.activity.OrderListActivity;
import com.liansheng.carworld.activity.WalletActivity;
import com.liansheng.carworld.activity.circle.HistoryReleaseAct;
import com.liansheng.carworld.activity.company.GuaranteeAct;
import com.liansheng.carworld.activity.logic.LoginResult;
import com.liansheng.carworld.activity.logic.UserLogic;
import com.liansheng.carworld.activity.me.CollectionActivity;
import com.liansheng.carworld.activity.me.CouponAct;
import com.liansheng.carworld.activity.me.GradeActivity;
import com.liansheng.carworld.activity.me.InfoAuthAct;
import com.liansheng.carworld.activity.me.SettingsAct;
import com.liansheng.carworld.activity.user.LoginYzmActivity;
import com.liansheng.carworld.bean.UserInfo;
import com.liansheng.carworld.bean.event.UIEvent;
import com.liansheng.carworld.bean.me.CouponBean;
import com.liansheng.carworld.bean.me.LicensesBean;
import com.liansheng.carworld.bean.other.PageBean;
import com.liansheng.carworld.kit.Constant;
import com.liansheng.carworld.net.JsonCallback;
import com.liansheng.carworld.net.NetApi;
import com.liansheng.carworld.net.UrlKit;
import com.liansheng.carworld.utils.DateParserHelper;
import com.liansheng.carworld.view.pop.CouponPop;
import com.liansheng.carworld.wxapi.WxUtils;
import com.qmuiteam.qmui.widget.QMUIRadiusImageView;
import com.xuexiang.xui.widget.banner.widget.banner.BannerItem;
import com.xuexiang.xui.widget.banner.widget.banner.SimpleImageBanner;

import org.greenrobot.eventbus.EventBus;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import cn.droidlover.xdroid.hold.info.SharedInfo;
import cn.droidlover.xdroid.tools.utils.ActivityManage;
import cn.droidlover.xdroid.tools.utils.NetworkUtil;
import cn.droidlover.xdroid.tools.utils.Util;
import cn.jpush.android.api.JPushInterface;
import okhttp3.Call;


public class MeFragment extends BaseFragment {

    Unbinder unbinder;
    @BindView(R.id.iv_header)
    QMUIRadiusImageView ivHeader;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.llyt_grade)
    LinearLayout llytGrade;
    @BindView(R.id.tv_money)
    TextView tvMoney;
    @BindView(R.id.llyt_money)
    LinearLayout llytMoney;
    @BindView(R.id.tv_grade)
    TextView tvGrade;
    @BindView(R.id.tv_collection)
    TextView tvCollection;
    @BindView(R.id.tv_coupon)
    TextView tv_coupon;
    @BindView(R.id.tv_zhpf)
    TextView tvZhpf;
    @BindView(R.id.tv_evaluation)
    TextView tvEvaluation;
    @BindView(R.id.tv_fen)
    TextView tvFen;
    @BindView(R.id.ivBanner)
    SimpleImageBanner ivBanner;
    @BindView(R.id.tv_vip)
    TextView tvVip;
    @BindView(R.id.iv_me_qm)
    ImageView ivMeQm;
    @BindView(R.id.iv_me_4s)
    ImageView ivMe4s;
    @BindView(R.id.iv_me_sj)
    ImageView ivMeSj;
    @BindView(R.id.iv_me_dd)
    ImageView ivMeDd;
//    @BindView(R.id.llyt_company)
//    LeftRightLayout llytCompany;

//    private IWXAPI api;

    public MeFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        unbinder = ButterKnife.bind(this, rootView);
        initview();
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_me;
    }

    public void initview() {
//        UserInfo userInfo = getBaseActivity().getLoginResult();
        ivBanner.setSource(getBannerList())
                .setOnItemClickListener((view, t, position) -> {
//                    if (position == 0) {
                    if (UserLogic.isLogin()) {
//                        showInviteDialog();
                        WxUtils.getInstance().wxWeb(SharedInfo.getInstance().getEntity(UserInfo.class).getInvitationLink(), "车无界", "邀请您注册车无界");
                    }
//                    }

                })
                .setIsOnePageLoop(false).startScroll();
    }

    public void showInviteDialog() {
        CustomDialog.build(getBaseActivity(), R.layout.layout_invite_dialog, new CustomDialog.OnBindView() {
            @Override
            public void onBind(final CustomDialog dialog, View v) {
//                QMUIRadiusImageView ivInvite = v.findViewById(R.id.iv_invite);
                ImageView btnOk = v.findViewById(R.id.btn_ok);
                ImageView btnCancel = v.findViewById(R.id.btn_cancel);
                btnOk.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        WxUtils.getInstance().wxWeb(SharedInfo.getInstance().getEntity(UserInfo.class).getInvitationLink(), "车无界", "邀请您注册车无界");
                    }
                });
                btnCancel.setOnClickListener(new View.OnClickListener() {
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

    public void checkLogin() {
        if (!UserLogic.isLogin()) {
            fastLogin();
        }
    }

    public static List<BannerItem> getBannerList() {
        String[] urls = new String[]{
//                UrlKit.API_BASE_URL + "/m001.png",
                UrlKit.API_BASE_URL + "/m002.png"
        };
        ArrayList<BannerItem> list = new ArrayList<>();
        for (int i = 0; i < urls.length; i++) {
            BannerItem item = new BannerItem();
            item.imgUrl = urls[i];
            list.add(item);
        }
        return list;
    }

    @Override
    public void onResume() {
        super.onResume();
        String token = SharedInfo.getInstance().getValue(Constant.KEY_TOKEN, "").toString();
        if (!TextUtils.isEmpty(token)) {
            getUserInfo(token, false);
        } else {
            tvName.setText("\n点击登录");
            tvVip.setText("");
            tvMoney.setText("0");
            tvGrade.setText("0");
            tvCollection.setText("0");
//            ivMeQm.setImageResource(R.mipmap.ic_auth_qm_grey);
//            ivMe4s.setImageResource(R.mipmap.ic_auth_4s_grey);
//            ivMeSj.setImageResource(R.mipmap.ic_auth_sj_grey);
            ivMeQm.setVisibility(View.GONE);
            ivMe4s.setVisibility(View.GONE);
            ivMeSj.setVisibility(View.GONE);
            ivMeDd.setVisibility(View.GONE);
            tvZhpf.setVisibility(View.GONE);
            tvEvaluation.setVisibility(View.GONE);
            tvFen.setVisibility(View.GONE);
            ivHeader.setImageResource(R.mipmap.header);
        }
    }

    public void getUserInfo(String token, boolean flag) {
        NetApi.get(UrlKit.USER_INFORM, null, new JsonCallback() {

            @Override
            public void onSuccess(String response, int id) {
                Gson gson = new Gson();
                UserInfo userInfo = gson.fromJson(response, UserInfo.class);
                if (userInfo == null) {
                    Util.toast("用户信息获取失败");
                    return;
                }
                userInfo.setToken(token);
                getBaseActivity().setLoginResult(userInfo, false);
                SharedInfo.getInstance().saveEntity(userInfo);
                tvZhpf.setVisibility(View.VISIBLE);
                tvEvaluation.setVisibility(View.VISIBLE);
                tvFen.setVisibility(View.VISIBLE);
                tvEvaluation.setText(userInfo.getScore());
                tvVip.setText("");
                if (userInfo.getVip() != null) {
                    if (userInfo.getVip().getExpiration() == null || "2022-01-01T00:00:00".equals(userInfo.getVip().getExpiration())) {
                        tvVip.setText("到期时间∞天");
                    } else {
                        int days = 0;
                        try {
                            days = DateParserHelper.daysBetween(userInfo.getVip().getExpiration());
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        if (days > 0) {
                            tvVip.setText("到期时间还有" + days + "天");
                        }
                    }
                }
                if (!TextUtils.isEmpty(userInfo.getHeadphoto())) {
                    Glide.with(getBaseActivity()).load(UrlKit.getImgUrl(userInfo.getHeadphoto())).into(ivHeader);
                }
                tvName.setText(userInfo.getMobile());
                List<LicensesBean> licenses = userInfo.getLicenses();
                ivMeQm.setVisibility(View.GONE);
                ivMe4s.setVisibility(View.GONE);
                ivMeSj.setVisibility(View.GONE);
                ivMeDd.setVisibility(View.GONE);
                for (int i = 0; i < licenses.size(); i++) {
                    LicensesBean licensesBean = licenses.get(i);
                    if (Constant.AUTH_IDENTITY_CARD.equals(licensesBean.getType()) && "passed".equals(licensesBean.getStatus())) {
                        tvName.setText(licensesBean.getOwner());
                    }
                }
                if (userInfo.getTypes().size() > 0) {
                    for (int i = 0; i < userInfo.getTypes().size(); i++) {
                        if (Constant.USER_TYPE_1.equals(userInfo.getTypes().get(i))) {
                            ivMeQm.setVisibility(View.VISIBLE);
                        } else if (Constant.USER_TYPE_2.equals(userInfo.getTypes().get(i))) {
                            ivMeSj.setVisibility(View.VISIBLE);
                        } else if (Constant.USER_TYPE_3.equals(userInfo.getTypes().get(i))) {
                            ivMe4s.setVisibility(View.VISIBLE);
                        } else if (Constant.USER_TYPE_4.equals(userInfo.getTypes().get(i))) {
                            ivMeDd.setVisibility(View.VISIBLE);
                        }
                    }
                }
                tvMoney.setText(userInfo.getBalance());
                tvGrade.setText(userInfo.getPoints());
                tvCollection.setText(userInfo.getTotalCollectionCounts());
                if (flag) {
                    JPushInterface.setAlias(getActivity(), Constant.JPUSH_MESSAGE, userInfo.getMobile());
                    EventBus.getDefault().post(new UIEvent(UIEvent.UPDATE_HOME));
                }
            }
        });
        HashMap<String, Object> map = new HashMap<>();
        map.put("expired", false);
        map.put("used", false);
        map.put("pageIndex",0);
        map.put("pageSize",5);
        NetApi.get(UrlKit.USER_COUPON, map, new JsonCallback() {
            @Override
            public void onSuccess(String data, int id) {
                PageBean<CouponBean> bean = new Gson().fromJson(data, new TypeToken<PageBean<CouponBean>>() {
                }.getType());
                tv_coupon.setText(String.valueOf(bean.getTotal()));
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @OnClick({R.id.me_setting,R.id.llyt_circle, R.id.iv_me_ll, R.id.llyt_info,R.id.llyt_coupon, R.id.llyt_money, R.id.llyt_grade, R.id.llyt_collection, R.id.llyt_order, R.id.llyt_auth,
            R.id.tv_name, R.id.iv_header, R.id.tv_zhpf, R.id.tv_evaluation, R.id.tv_my_invite, R.id.llyt_company, R.id.llyt_htlb})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.me_setting:
                if (UserLogic.isLogin()) {
                    ActivityManage.push(getActivity(), SettingsAct.class, 1001);
                } else {
                    Util.toast("请先登录");
                }
                break;
            case R.id.iv_me_ll:
                UserLogic.checkLogin(InfoAuthAct.class);
                break;
            case R.id.llyt_info:
                UserLogic.checkLogin(InfoActivity.class);
                break;
            case R.id.llyt_money:
                UserLogic.checkLogin(WalletActivity.class);
                break;
            case R.id.llyt_grade:
                UserLogic.checkLogin(GradeActivity.class);
                break;
            case R.id.llyt_circle:
                if(UserLogic.isLogin()){
                    Intent intent=new Intent();
                    intent.putExtra(Constant.KEY_TITLE,"我的发布");
                    ActivityManage.push(HistoryReleaseAct.class,intent);
                }
                break;
            case R.id.llyt_coupon:
                UserLogic.checkLogin(CouponAct.class);
                break;
            case R.id.llyt_collection:
                UserLogic.checkLogin(CollectionActivity.class);
                break;
            case R.id.llyt_order:
                UserLogic.checkLogin(OrderListActivity.class);
                break;
            case R.id.tv_zhpf:
            case R.id.tv_evaluation:
                showPfDialog();
                break;
            case R.id.tv_my_invite:
                UserLogic.checkLogin(InviteListActivity.class);
                break;
//            case R.id.llyt_company:
//                UserLogic.checkLogin(CompanyAct.class);
//                break;
            case R.id.iv_header:
            case R.id.tv_name:
                if (UserLogic.isLogin()) {
                    ActivityManage.push(InfoActivity.class);
                } else {
                    fastLogin();
                }
                break;
            case R.id.llyt_htlb:
                ActivityManage.push(GuaranteeAct.class);
//                ActivityManage.push(HomeActivity.class);
//                ActivityManage.push(ScanVinActivity.class, null, 1111);
                break;
            case R.id.llyt_auth:
                ActivityManage.push(InfoAuthAct.class);
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && requestCode == 1001) {
            fastLogin();
        } else if (resultCode == Activity.RESULT_OK && requestCode == 1111) {
        }
    }


    private void fastLogin() {
        NetworkUtil.showCutscenes("", "本机号码获取中...");
        UserLogic.quickLogin(new LoginResult() {
            @Override
            public void login(String YDToken, String accessCode) {
                toLogin(YDToken, accessCode);
            }

            @Override
            public void onError(String error) {
                ActivityManage.push(LoginYzmActivity.class);
            }
        });
    }

    private void toLogin(String token, String code) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("type", "phoneNumber");
        map.put("token", token);
        map.put("accessToken", code);
        NetApi.post(UrlKit.LOGIN, map, new JsonCallback() {

            @Override
            public void onSuccess(String response, int id) {
                Map<String, String> hashMap = new Gson().fromJson(response, Map.class);
                SharedInfo.getInstance().saveValue(Constant.KEY_TOKEN, hashMap.get("token"));
                getUserInfo(hashMap.get("token"), true);
                EventBus.getDefault().post(new UIEvent(UIEvent.UPDATE_COMPANY));
            }
        });
    }

    public void showPfDialog() {
        MessageDialog.build(getBaseActivity())
                .setTitle("综合评分说明")
                .setMessage("初始评分为100分，上传商业险评分+20分，货运险评分+50分，营业执照评分+50分。好评加10分，差评减10分。\n望各位司机注重评分，评分低于100分，平台不予以接单。")
                .setOkButton("知道了", new OnDialogButtonClickListener() {
                    @Override
                    public boolean onClick(BaseDialog baseDialog, View v) {
                        return false;
                    }
                })
                .show();
    }
}