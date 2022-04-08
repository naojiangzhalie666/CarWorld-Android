package com.liansheng.carworld.activity.home;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.liansheng.carworld.R;
import com.liansheng.carworld.activity.BaseActivity;
import com.liansheng.carworld.adapter.home.DriverManagerAdapter;
import com.liansheng.carworld.bean.home.DriverSelectBean;
import com.liansheng.carworld.bean.other.PageBean;
import com.liansheng.carworld.net.JsonCallback;
import com.liansheng.carworld.net.NetApi;
import com.liansheng.carworld.net.UrlKit;
import com.liansheng.carworld.utils.ViewUtils;
import com.qmuiteam.qmui.skin.QMUISkinManager;
import com.qmuiteam.qmui.util.QMUIDisplayHelper;
import com.qmuiteam.qmui.widget.popup.QMUIPopup;
import com.qmuiteam.qmui.widget.popup.QMUIPopups;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.droidlover.xdroid.views.appbar.TitleBar;
import cn.droidlover.xdroid.views.appbar.ToolBar;
import cn.droidlover.xdroid.views.recyclerView.DividerLine;
import cn.droidlover.xrecyclerview.XRecyclerContentLayout;
import cn.droidlover.xrecyclerview.XRecyclerView;
import okhttp3.Call;

public class DriverEditAct extends BaseActivity {

    @BindView(R.id.edit_type)
    TextView edit_type;
    private QMUIPopup mNormalPopup;

    @Override
    public void initData(Bundle savedInstanceState) {
        showData();
    }

    @Override
    public int getLayoutId() {
        return R.layout.act_driver_edit;
    }


    @OnClick(R.id.edit_type)
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.edit_type:
                mNormalPopup.show(view);
                break;
        }
    }


    private void showData() {
        List<String> data = new ArrayList<>();
        data.add("管理员");
        data.add("司机");
        ArrayAdapter adapter = new ArrayAdapter(this, R.layout.simple_list_item, data);
        AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                edit_type.setText(data.get(i));
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
                .show(edit_type);
    }

}
