package com.liansheng.carworld.activity;

import android.os.Bundle;
import android.view.View;

import com.liansheng.carworld.R;
import com.qmuiteam.qmui.widget.QMUITopBar;
import android.widget.ImageView;
import android.widget.TextView;


import butterknife.BindView;
import butterknife.ButterKnife;

public class TemplateActivity extends BaseActivity {

    //    @BindView(R.id.topbar)
//    QMUITopBar topbar;
    @BindView(R.id.btn_back)
    ImageView btnBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
//        topbar.setTitle("");
//        topbar.addLeftBackImageButton().setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                finish();
//            }
//        });
        tvTitle.setText("");
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        initView();
    }

    private void initView() {

    }

    @Override
    public void initData(Bundle savedInstanceState) {

    }

    @Override
    public int getLayoutId() {
        return 0;
    }//布局文件
}
