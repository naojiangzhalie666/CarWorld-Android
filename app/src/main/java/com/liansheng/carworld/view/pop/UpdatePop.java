package com.liansheng.carworld.view.pop;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.liansheng.carworld.R;
import com.liansheng.carworld.bean.UpdateBean;

import me.wangyuwei.loadingview.LoadingView;

public class UpdatePop extends PopupWindow {
    TextView update_version;
    TextView update_content;
    public LinearLayout update_ll;
    public TextView update_progress;
    public LoadingView loadingView;
    public TextView update_btn;
    ImageView update_close;
    private UpdateBean updateBean;


    public UpdatePop(Context context, PopClick popClick) {
        View view = View.inflate(context, R.layout.pop_update, null);
        setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
        setContentView(view);
        setBackgroundDrawable(new ColorDrawable(0xAA000000));
        setAnimationStyle(R.style.bottomMenuAnim);
        update_version = view.findViewById(R.id.update_version);
        update_content = view.findViewById(R.id.update_content);
        update_ll = view.findViewById(R.id.update_ll);
        update_progress = view.findViewById(R.id.update_progress);
        loadingView = view.findViewById(R.id.loading_view);
        update_btn = view.findViewById(R.id.update_btn);
        update_close = view.findViewById(R.id.update_close);
        update_close.setOnClickListener(v -> {
            popClick.chick(v, "cancel");
            dismiss();
        });
        update_btn.setOnClickListener(v -> {
            popClick.chick(v, updateBean.getDownload());
        });
    }

    public void upData(UpdateBean updateBean, String currentVersion) {
        this.updateBean = updateBean;
        String forceVersion = updateBean.getForce().replace(".", "");
        if (Integer.parseInt(forceVersion) >= Integer.parseInt(currentVersion)) {
            update_close.setVisibility(View.GONE);
        }
        update_version.setText("V" + updateBean.getVersion());
        update_content.setText(updateBean.getDescription());
        update_btn.setEnabled(true);
        update_ll.setVisibility(View.GONE);
        update_btn.setVisibility(View.VISIBLE);
    }
}
