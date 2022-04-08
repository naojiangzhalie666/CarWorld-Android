package com.liansheng.carworld.activity;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;

public abstract class BaseDialogFragment extends DialogFragment {

    private static final String TAG = BaseDialogFragment.class.getSimpleName();

    private int DEFAULT_WIDTH = WindowManager.LayoutParams.MATCH_PARENT;
    private int DEFAULT_HEIGHT = WindowManager.LayoutParams.WRAP_CONTENT;
    private int DEFAULT_GRAVITY = Gravity.BOTTOM;

    private Dialog dialog;

    public BaseDialogFragment() {
        this(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT, Gravity.BOTTOM);
    }

    public BaseDialogFragment(int width, int height, int gravity) {
        DEFAULT_WIDTH = width;
        DEFAULT_HEIGHT = height;
        DEFAULT_GRAVITY = gravity;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        this.getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        View view = inflater.inflate(getLayoutId(), container, false);
        initViews(view);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        dialog = getDialog();
        if (null != dialog) {
            Window window = dialog.getWindow();
            if (null != window) {
                window.getDecorView().setPadding(0, 0, 0, 0);
                window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                WindowManager.LayoutParams lp = window.getAttributes();
                lp.width = DEFAULT_WIDTH;
                lp.height = DEFAULT_HEIGHT;
                lp.gravity = DEFAULT_GRAVITY;
                lp.windowAnimations = android.R.style.Animation_InputMethod;
                window.setAttributes(lp);
            }
            dialog.setCancelable(true);
            dialog.setCanceledOnTouchOutside(true);
        }
    }

    public void setCancelable(boolean cancelable) {
        if (null == dialog) {
            return;
        }
        dialog.setCancelable(cancelable);
    }

    public void setCanceledOnTouchOutside(boolean canceledOnTouchOutside) {
        if (null == dialog) {
            return;
        }
        dialog.setCanceledOnTouchOutside(canceledOnTouchOutside);
    }

    @Override
    public void show(@NonNull FragmentManager manager, @Nullable String tag) {
        super.show(manager, tag);
    }

    protected abstract int getLayoutId();

    protected abstract void initViews(View v);
}