package com.liansheng.carworld.fragment;

import android.os.Bundle;

import com.liansheng.carworld.R;

public class EmptyFrag extends BaseFragment {

    public static EmptyFrag newInstance() {
        Bundle args = new Bundle();
        EmptyFrag fragment = new EmptyFrag();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getLayoutId() {
        return R.layout.content_main;
    }
}
