package com.liansheng.carworld.view;

import android.os.Handler;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.fragment.app.FragmentManager;

import com.liansheng.carworld.R;
import com.liansheng.carworld.activity.BaseDialogFragment;
import com.liansheng.carworld.view.pop.PopClick;

import cn.droidlover.xdroid.tools.utils.Util;

public class PopAddDialog extends BaseDialogFragment {

    private static final String TAG = PopAddDialog.class.getSimpleName();
    private PopClick popClick;
    TextView tvTitle, tv_content;

    private PopAddDialog(int width, int height, int gravity) {
        super(width, height, gravity);
    }

    public static PopAddDialog show(FragmentManager fm) {
        PopAddDialog dialog = new PopAddDialog(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT, Gravity.CENTER);
        dialog.show(fm, TAG);
        return dialog;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.pop_add;
    }

    @Override
    protected void initViews(View view) {
        tvTitle = view.findViewById(R.id.tv_title);
        tv_content = view.findViewById(R.id.tv_content);
        TextView submit = view.findViewById(R.id.submit);
        TextView cancel = view.findViewById(R.id.cancel);
        submit.setOnClickListener(v -> {
            String str = tv_content.getText().toString();
            if (TextUtils.isEmpty(str)) {
                Util.toast(tv_content.getHint().toString());
                return;
            }
            popClick.chick(v, str);
            dismiss();
        });
        submit.setText("确定");
        cancel.setText("取消");
//            setFocusable(true);
        tv_content.setEnabled(true);
        cancel.setOnClickListener(v -> {
            popClick.chick(v, "");
            dismiss();
        });
//        v.findViewById(R.id.cancel_btn_tv).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                dismiss();
//            }
//        });
//        v.findViewById(R.id.sure_btn_tv).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                dismiss();
//                if (null != mCallback) {
//                    mCallback.onClickSure();
//                }
//            }
//        });
    }


    public void setCallback(PopClick cb) {
        popClick = cb;
    }

    public PopAddDialog setTitle(String title) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                tvTitle.setText(title);
            }
        }, 300);
        return this;
    }

}