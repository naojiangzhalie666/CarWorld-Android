package com.liansheng.carworld.view.pop;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.liansheng.carworld.R;

import cn.droidlover.xdroid.tools.utils.Util;

public class AddPop extends PopupWindow {

    private int type;

    TextView tvTitle;
    EditText tv_content;


    public AddPop(Context context, int type, PopClick popClick) {
        this(context, "", type, "", popClick);
    }

    public AddPop(Context context, String title, int type, String content, PopClick popClick) {
        this.type = type;
        View view = View.inflate(context, R.layout.pop_add, null);
        RelativeLayout item_rl = view.findViewById(R.id.item_rl);
        setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
        setContentView(view);
        setBackgroundDrawable(new ColorDrawable(0xAA000000));
        setAnimationStyle(R.style.bottomMenuAnim);
        tvTitle = view.findViewById(R.id.tv_title);
        tv_content = view.findViewById(R.id.tv_content);
        TextView submit = view.findViewById(R.id.submit);
        TextView cancel = view.findViewById(R.id.cancel);
        setContentView(view);
        submit.setOnClickListener(v -> {
            if (type == 1) {
                String str = tv_content.getText().toString();
                if (TextUtils.isEmpty(str)) {
                    Util.toast(tv_content.getHint().toString());
                    return;
                }
                popClick.chick(v, str);
            } else {
                popClick.chick(v, "");
            }
            tv_content.setText("");
            dismiss();
        });
        if (type == 1) {
            submit.setText("确定");
            cancel.setText("取消");
            setFocusable(true);
            tv_content.setEnabled(true);
        } else {
            submit.setText("接受");
            cancel.setText("拒绝");
            tv_content.setEnabled(false);
            tv_content.setBackground(ContextCompat.getDrawable(context, R.color.transparent));
        }
        tvTitle.setText(title);
        cancel.setOnClickListener(v -> {
            popClick.chick(v,"");
            dismiss();
        });
        item_rl.setOnClickListener(v -> dismiss());
        view.findViewById(R.id.item_ll).setOnClickListener(v -> {

        });

    }

    public void setTitle(String title) {
        tvTitle.setText(title);
    }

    public void setContent(String content) {
        tv_content.setText(content);
    }

}

