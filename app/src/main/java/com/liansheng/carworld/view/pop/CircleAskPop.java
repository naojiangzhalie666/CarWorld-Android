package com.liansheng.carworld.view.pop;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.liansheng.carworld.R;

import cn.droidlover.xdroid.tools.utils.Util;

public class CircleAskPop extends PopupWindow {

    private EditText tv_content;

    public CircleAskPop(Context context, PopClick popClick) {
        View view = View.inflate(context, R.layout.pop_circle_ask, null);
        RelativeLayout item_rl = view.findViewById(R.id.item_rl);
        setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
        setContentView(view);
        setBackgroundDrawable(new ColorDrawable(0x00000000));
        setAnimationStyle(R.style.bottomMenuAnim);
        tv_content = view.findViewById(R.id.tv_content);
        TextView submit = view.findViewById(R.id.submit);
        setContentView(view);
        submit.setOnClickListener(v -> {
            popClick.chick(v, tv_content.getText().toString());
            tv_content.setText("");
            dismiss();
        });
        setFocusable(true);
        item_rl.setOnClickListener(v -> dismiss());

    }

    public void setUser(String name){
        if(!TextUtils.isEmpty(name)){
            tv_content.setHint("回复："+name);
        }
    }

    @Override
    public void showAtLocation(View parent, int gravity, int x, int y) {
        super.showAtLocation(parent, gravity, x, y);
        shoKeyboard();
    }

    private void shoKeyboard() {
        InputMethodManager inputMethodManager = (InputMethodManager) tv_content.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.toggleSoftInput(1000, InputMethodManager.HIDE_NOT_ALWAYS);
        tv_content.requestFocus();
    }

}

