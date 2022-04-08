package com.liansheng.carworld.fragment;

import android.os.Bundle;

import com.liansheng.carworld.R;

import butterknife.BindView;

public class IMFrag extends BaseFragment {

//    @BindView(R.id.conversation_layout)
//    ConversationLayout conversationLayout;

    @Override
    public void initData(Bundle savedInstanceState) {
        super.initData(savedInstanceState);
// 初始化聊天面板
//        conversationLayout.initDefault();
    }

    @Override
    public int getLayoutId() {
        return R.layout.frag_im;
    }
}
