package com.liansheng.carworld.activity.company;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import com.kongzue.dialog.interfaces.OnDialogButtonClickListener;
import com.kongzue.dialog.util.BaseDialog;
import com.kongzue.dialog.v3.MessageDialog;
import com.liansheng.carworld.R;
import com.liansheng.carworld.activity.BaseActivity;
import com.liansheng.carworld.bean.MemberBean;
import com.liansheng.carworld.bean.UserInfo;
import com.liansheng.carworld.bean.me.AddressBean;
import com.liansheng.carworld.kit.Constant;
import com.liansheng.carworld.net.JsonCallback;
import com.liansheng.carworld.net.NetApi;
import com.liansheng.carworld.net.UrlKit;
import com.qmuiteam.qmui.skin.QMUISkinManager;
import com.qmuiteam.qmui.util.QMUIDisplayHelper;
import com.qmuiteam.qmui.widget.popup.QMUIPopup;
import com.qmuiteam.qmui.widget.popup.QMUIPopups;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.droidlover.xdroid.hold.info.SharedInfo;
import cn.droidlover.xdroid.tools.utils.Util;
import cn.droidlover.xdroid.views.textView.NoDoubleClickButton;
import cn.droidlover.xdroid.views.textView.NoDoubleClickTextView;
import cn.jpush.android.api.JPushInterface;
import okhttp3.Call;

public class MembersDetailsAct extends BaseActivity {

    @BindView(R.id.tv_type)
    TextView tv_type;
    @BindView(R.id.tv_account)
    TextView tv_account;
    @BindView(R.id.tv_date)
    TextView tv_date;
    @BindView(R.id.tv_account_type)
    TextView tv_account_type;
    @BindView(R.id.ck_delete)
    Switch ck_delete;
    @BindView(R.id.ck_add)
    Switch ck_add;
    @BindView(R.id.btn_delete)
    NoDoubleClickButton btnDelete;

    private QMUIPopup mNormalPopup;
    private MemberBean bean;

    @Override
    public void initData(Bundle savedInstanceState) {
        ck_delete.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                ck_delete.setText("允许");
            } else {
                ck_delete.setText("拒绝");
            }

        });
        ck_add.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                ck_add.setText("允许");
            } else {
                ck_add.setText("拒绝");
            }
        });
        bean = getIntent().getParcelableExtra(Constant.KEY_BEAN);
        String userType = getIntent().getStringExtra(Constant.KEY_TYPE);
        if ("admin".equals(bean.getType())) {
            tv_account_type.setText("创建者");
        } else if ("manager".equals(bean.getType())) {
            tv_account_type.setText("管理员");
        } else if ("ordinary".equals(bean.getType())) {
            tv_account_type.setText("员工");
        }
        if ("admin".equals(userType)) {
            if (!bean.getType().equals(userType)) {
                Drawable dra = context.getDrawable(R.mipmap.icon_down);
                dra.setBounds(0, 0, dra.getIntrinsicWidth(), dra.getIntrinsicHeight());
                tv_account_type.setCompoundDrawables(null, null, dra, null);
                btnDelete.setVisibility(View.VISIBLE);
                showData();
            }
        } else if ("manager".equals(userType)) {
            if (!"admin".equals(bean.getType()) && !bean.getType().equals(userType)) {
                btnDelete.setVisibility(View.VISIBLE);
            }

        }
        tv_type.setText(bean.getName());
        tv_account.setText(bean.getMobile());
    }

    @Override
    public int getLayoutId() {
        return R.layout.act_members_details;
    }

    @OnClick({R.id.tv_account_type, R.id.tv_account, R.id.btn_delete})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_account:
                callPhone(this, "是否拨打电话？", bean.getMobile());
                break;
            case R.id.btn_delete:
                MessageDialog.build(this)
                        .setTitle("温馨提示")
                        .setMessage("是否删除该员工？")
                        .setCancelButton("取消", new OnDialogButtonClickListener() {
                            @Override
                            public boolean onClick(BaseDialog baseDialog, View v) {
                                return false;
                            }
                        })
                        .setOkButton("确定删除", new OnDialogButtonClickListener() {
                            @Override
                            public boolean onClick(BaseDialog baseDialog, View v) {
                                deleteMember(bean.getId());
                                return false;
                            }
                        })
                        .show();
                break;
            case R.id.tv_account_type:
                if (mNormalPopup != null) {
                    mNormalPopup.show(view);
                }
                break;
        }
    }

    private void showData() {
        List<String> data = new ArrayList<>();
        data.add("管理员");
        data.add("员工");
        ArrayAdapter adapter = new ArrayAdapter(this, R.layout.simple_list_item, data);
        AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                tv_account_type.setText(data.get(i));
                updateMember(bean.getId(), data.get(i));
                if (mNormalPopup != null) {
                    mNormalPopup.dismiss();
                }
            }
        };
        mNormalPopup = QMUIPopups.listPopup(this,
                QMUIDisplayHelper.dp2px(this, 300),
                QMUIDisplayHelper.dp2px(this, 100),
                adapter,
                onItemClickListener)
                .animStyle(QMUIPopup.ANIM_GROW_FROM_CENTER)
                .preferredDirection(QMUIPopup.DIRECTION_BOTTOM)
                .shadow(true)
                .offsetYIfTop(QMUIDisplayHelper.dp2px(this, 10))
                .skinManager(QMUISkinManager.defaultInstance(this))
                .show(tv_account_type);
    }


    private void updateMember(String memberId, String type) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("memberId", memberId);
        if ("管理员".equals(type)) {
            map.put("type", "manager");
        } else {
            map.put("type", "ordinary");
        }
        NetApi.put(UrlKit.USER_MEMBER, map, new JsonCallback() {

            @Override
            public void onSuccess(String response, int id) {
                Util.toast("修改成功");
            }
        });
    }

    private void deleteMember(String memberId) {
        NetApi.delete(UrlKit.USER_MEMBER_DELETE + memberId, null, new JsonCallback() {

            @Override
            public void onSuccess(String response, int id) {
                Util.toast("删除成功");
                finish();
            }
        });
    }
}
