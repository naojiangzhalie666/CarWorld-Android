package com.liansheng.carworld.fragment;

import android.os.Bundle;

import com.liansheng.carworld.R;

import butterknife.BindView;

public class IMMsgFrag extends BaseFragment {

//    @BindView(R.id.contact_layout)
//    ContactLayout contactLayout;

    @Override
    public void initData(Bundle savedInstanceState) {
        super.initData(savedInstanceState);
// 初始化聊天面板
//        contactLayout.initDefault();
    }

    @Override
    public int getLayoutId() {
        return R.layout.frag_im_msg;
    }
}
