package com.liansheng.carworld.view.pop;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupWindow;

import com.liansheng.carworld.R;
import com.liansheng.carworld.net.UrlKit;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import cn.droidlover.xdroid.tools.utils.ActivityManage;
import cn.droidlover.xdroid.tools.utils.BitmapUtil;
import cn.droidlover.xdroid.tools.utils.FileUtil;
import cn.droidlover.xdroid.tools.utils.Util;

public class CompanyWechatPop extends PopupWindow {


    public CompanyWechatPop(Context context) {
//        this.type = type;
        View view = View.inflate(context, R.layout.pop_company_wechat, null);
        setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
        setBackgroundDrawable(new ColorDrawable(0xAA000000));
        setAnimationStyle(R.style.bottomMenuAnim);
        setContentView(view);
//        ImageView popImg = view.findViewById(R.id.pop_img);
        view.findViewById(R.id.pop_btn).setOnClickListener(v -> {
            String fileName = "car_world_wechat.jpg";
            String file = FileUtil.saveFile(context, UrlKit.FILE_PATH, fileName, BitmapUtil.readBitmap(context, R.mipmap.ic_company_wx));
            if (TextUtils.isEmpty(file)) {
                Util.toast("保存失败");
            } else {
                File file1 = new File(file);
                try {
                    MediaStore.Images.Media.insertImage(ActivityManage.peek().getContentResolver(), file1.getAbsolutePath(), fileName, null);
                    Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                    Uri uri = Uri.fromFile(file1);
                    intent.setData(uri);
                    ActivityManage.peek().sendBroadcast(intent);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                Util.toast("保存成功");
            }
            dismiss();
        });
        view.findViewById(R.id.item_rl).setOnClickListener(v -> dismiss());
        view.findViewById(R.id.item_ll).setOnClickListener(v -> {

        });
    }
}
