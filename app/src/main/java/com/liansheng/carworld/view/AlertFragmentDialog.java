package com.liansheng.carworld.view;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.liansheng.carworld.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AlertFragmentDialog extends DialogFragment {
    public static final String TAG = AlertFragmentDialog.class.getSimpleName();
    private static final String KEY_TITLE = "title";
    private static final String KEY_CONTENT = "content";
    private static final String KEY_LEFT_BTN_TEXT = "leftBtnText";
    private static final String KEY_RIGHT_BTN_TEXT = "rightBtnText";
    private static final String KEY_ISCANCEL = "isCancel";

    @BindView(R.id.tv_title)
    TextView mTvTitle;
    @BindView(R.id.tv_content)
    TextView mTvContent;
    @BindView(R.id.tv_cancel)
    TextView mTvCancel;
    @BindView(R.id.tv_accomplish)
    TextView mTvAccomplish;
    @BindView(R.id.view_vertical)
    View mViewVertical;
    private LeftClickCallBack mLeftCallBack;
    private RightClickCallBack mRightCallBack;

    public void setLeftCallBack(LeftClickCallBack mLeftCallBack) {
        this.mLeftCallBack = mLeftCallBack;
    }

    public void setRightCallBack(RightClickCallBack mRightCallBack) {
        this.mRightCallBack = mRightCallBack;
    }

    private static AlertFragmentDialog newInstance(String title, String content, String leftBtnText, String rightBtnText, boolean isCancel) {
        AlertFragmentDialog dialog = new AlertFragmentDialog();
        Bundle bundle = new Bundle();
        bundle.putString(KEY_TITLE, title);
        bundle.putString(KEY_CONTENT, content);
        bundle.putString(KEY_LEFT_BTN_TEXT, leftBtnText);
        bundle.putString(KEY_RIGHT_BTN_TEXT, rightBtnText);
        bundle.putBoolean(KEY_ISCANCEL, isCancel);
        dialog.setArguments(bundle);
        return dialog;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // 这个判断很重要
        if (getDialog() == null) {
            setShowsDialog(false);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        View view = inflater.inflate(R.layout.common_dialog_alert, container, false);
        ButterKnife.bind(this, view);
        setData();
        return view;
    }

    private void initDialog() {
        getDialog().setCancelable(getArguments().getBoolean(KEY_ISCANCEL));
        getDialog().getWindow().setGravity(Gravity.CENTER);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(0x00000000));
        DisplayMetrics dm = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
        getDialog().getWindow().setLayout((int) (dm.widthPixels * 0.7), getDialog().getWindow().getAttributes().height);
    }

    private void setData() {
        if (getArguments().getString(KEY_TITLE) == null) {
            mTvTitle.setVisibility(View.GONE);
        }
        mTvTitle.setText(getArguments().getString(KEY_TITLE) == null ? mTvTitle.getText().toString() : getArguments().getString("title"));
        mTvContent.setText(getArguments().getString(KEY_CONTENT) == null ? mTvContent.getText().toString() : getArguments().getString("content"));
        if (getArguments().getString(KEY_LEFT_BTN_TEXT) == null) {
            mTvCancel.setVisibility(View.GONE);
            mViewVertical.setVisibility(View.GONE);
        } else {
            mTvCancel.setText(getArguments().getString(KEY_LEFT_BTN_TEXT));
        }
        if (getArguments().getString(KEY_RIGHT_BTN_TEXT) == null) {
            mTvAccomplish.setVisibility(View.GONE);
            mViewVertical.setVisibility(View.GONE);
        } else {
            mTvAccomplish.setText(getArguments().getString(KEY_RIGHT_BTN_TEXT));
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        initDialog();
    }

    @OnClick({R.id.tv_cancel, R.id.tv_accomplish})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_cancel:
                if (mLeftCallBack != null) {
                    mLeftCallBack.dialogLeftBtnClick();
                }
                dismiss();
                break;
            case R.id.tv_accomplish:
                if (mRightCallBack != null) {
                    mRightCallBack.dialogRightBtnClick();
                }
                dismiss();
                break;
        }
    }

    /**
     * 左边按钮点击回调
     */
    public interface LeftClickCallBack {
        void dialogLeftBtnClick();
    }

    @Override
    public void show(FragmentManager manager, String tag) {
        //修改commit方法为commitAllowingStateLoss
        FragmentTransaction ft = manager.beginTransaction();
        ft.add(this, tag);
        ft.commitAllowingStateLoss();
    }


    /**
     * 右边按钮点击回调
     */
    public interface RightClickCallBack {
        void dialogRightBtnClick();
    }

    public static class Builder {
        private FragmentActivity activity;
        private String title;
        private String content;
        private String leftBtnText;
        private String rightBtnText;
        private LeftClickCallBack leftCallBack;
        private RightClickCallBack rightCallBack;
        private boolean isCancel;

        public Builder(FragmentActivity activity) {
            this.activity = activity;
        }

        public Builder setTitle(String title) {
            this.title = title;
            return this;
        }

        public Builder setContent(String content) {
            this.content = content;
            return this;
        }

        public Builder setLeftBtnText(String leftBtnText) {
            this.leftBtnText = leftBtnText;
            return this;
        }

        public Builder setRightBtnText(String rightBtnText) {
            this.rightBtnText = rightBtnText;
            return this;
        }

        public Builder setLeftCallBack(LeftClickCallBack leftCallBack) {
            this.leftCallBack = leftCallBack;
            return this;
        }

        public Builder setRightCallBack(RightClickCallBack rightCallBack) {
            this.rightCallBack = rightCallBack;
            return this;
        }

        /**
         * 是否可取消 （默认为不可取消）
         * @param cancel true为可取消
         * @return
         */
        public Builder setCancel(boolean cancel) {
            isCancel = cancel;
            return this;
        }

        public AlertFragmentDialog build() {
            AlertFragmentDialog dialogFragment = AlertFragmentDialog.newInstance(title, content, leftBtnText, rightBtnText, isCancel);
            dialogFragment.setLeftCallBack(leftCallBack);
            dialogFragment.setRightCallBack(rightCallBack);
            dialogFragment.show(activity.getSupportFragmentManager(), dialogFragment.TAG);
            return dialogFragment;
        }
    }
}