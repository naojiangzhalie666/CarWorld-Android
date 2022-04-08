package com.liansheng.carworld.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;


import com.liansheng.carworld.activity.BaseActivity;

import butterknife.Unbinder;
import cn.droidlover.xdroid.base.BaseLazyFragment;
import cn.droidlover.xdroid.base.SimpleRecAdapter;
import cn.droidlover.xdroid.base.UiCallback;
import cn.droidlover.xdroid.base.UiDelegate;
import cn.droidlover.xdroid.base.UiDelegateBase;
import cn.droidlover.xdroid.event.BusFactory;
import cn.droidlover.xdroid.kit.KnifeKit;
import cn.droidlover.xrecyclerview.XRecyclerView;


public abstract class BaseFragment extends BaseLazyFragment implements UiCallback {
    protected View rootView;
    protected Activity context;
    protected UiDelegate uiDelegate;
    private Unbinder unbinder;
    private BaseActivity mBaseActivity;
    protected int MAX_PAGE = 20;//默认最大页数

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(getLayoutId(), null);
            unbinder = KnifeKit.bind(this, rootView);
        } else {
            ViewGroup viewGroup = (ViewGroup) rootView.getParent();
            if (viewGroup != null) {
                viewGroup.removeView(rootView);
            }
        }

        return rootView;
    }

    @Override
    public void initData(Bundle savedInstanceState) {

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (useEventBus()) {
            BusFactory.getBus().register(this);
        }
        setListener();
        initData(savedInstanceState);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof Activity) {
            this.context = (Activity) context;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        context = null;
    }

    @Override
    public boolean useEventBus() {
        return false;
    }

    @Override
    public void setListener() {

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
//        BusFactory.getBus().unregister(this);
        getUiDelegate().destory();
    }

    protected UiDelegate getUiDelegate() {
        if (uiDelegate == null) {
            uiDelegate = UiDelegateBase.create(getContext());
        }
        return uiDelegate;
    }
    protected BaseActivity getBaseActivity() {
        return mBaseActivity;
    }
    @Override
    public void onAttach(Activity activity) {
        // TODO Auto-generated method stub
        mBaseActivity = (BaseActivity) activity;
        super.onAttach(activity);
    }

}
