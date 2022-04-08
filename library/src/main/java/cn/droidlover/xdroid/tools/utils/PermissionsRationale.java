//package cn.droidlover.xdroid.tools.utils;
//
//import android.content.Context;
//import android.content.DialogInterface;
//
//
//import com.yanzhenjie.permission.Rationale;
//import com.yanzhenjie.permission.RequestExecutor;
//
//import java.util.List;
//
//import cn.droidlover.xdroid.R;
///**
// * Author: JinF
// * Date: 2018/4/18 18:10
// * <p/>
// * Description:
// */
//
//public class PermissionsRationale implements Rationale<List<String>> {
//
//    @Override
//    public void showRationale(Context context, List<String> data,final RequestExecutor executor) {
//        new AlertDialog.Builder(context).setMessage(R.string.permission_message_reapply)
//                .setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int i) {
//                        executor.execute();
//                    }
//                }).setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialogInterface, int i) {
//                executor.cancel();
//            }
//        }).show();
//    }
//}
