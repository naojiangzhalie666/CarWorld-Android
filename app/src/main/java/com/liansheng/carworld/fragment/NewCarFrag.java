package com.liansheng.carworld.fragment;

import android.os.Bundle;

import com.liansheng.carworld.R;

public class NewCarFrag extends BaseFragment {

    public static NewCarFrag newInstance() {
        Bundle args = new Bundle();
        NewCarFrag fragment = new NewCarFrag();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getLayoutId() {
        return R.layout.frag_new_car;
    }
}
