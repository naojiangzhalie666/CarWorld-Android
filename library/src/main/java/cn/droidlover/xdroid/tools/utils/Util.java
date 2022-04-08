package cn.droidlover.xdroid.tools.utils;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Rect;
import android.net.Uri;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;


import java.lang.reflect.Field;

import cn.droidlover.xdroid.hold.info.SoftHashMap;

/**
 * Author: TinhoXu
 * E-mail: xth@erongdu.com
 * Date: 2016/10/21 17:02
 * <p/>
 * Description:
 */
public class Util {
    /** 禁止重复提示 */
    private static final long Interval = 3 * 1000;
    /** 消息软引用 */
    private static final SoftHashMap<String, Long> map = new SoftHashMap<>();
    /** Toast 对象 */
    private static Toast toast;

    public static void toast(String msg) {
        toast(ContextHolder.getContext(), msg);
    }

    public static void toast(int id) {
        toast(ContextHolder.getContext(), ContextHolder.getContext().getString(id));
    }

    private static void toast(Context context, String msg) {
        if (!TextUtils.isEmpty(msg)) {
            long preTime = 0;
            if (map.containsKey(msg)) {
                preTime = map.get(msg);
            }
            final long now = System.currentTimeMillis();
            if (now >= preTime + Interval) {
                if (toast != null) {
                    toast.cancel();
                }
                if (context != null) {
                    Toast toast = Toast.makeText(context, msg, Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
                    toast.show();
                    map.put(msg, now);
                    Util.toast = toast;
                }
            }
        }
    }

    /**
     * 隐藏键盘
     *
     * @param view
     */
    public static void hideKeyBoard(View view) {
        InputMethodManager imm = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm.isActive()) {
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    /**
     * 强隐
     */
    public static void hideKeyBoard2(Activity context) {
        final InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (Util.isSoftShowing(context)) {
            imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    private static boolean isSoftShowing(Activity activity) {
        //获取当前屏幕内容的高度
        int screenHeight = activity.getWindow().getDecorView().getHeight();
        //获取View可见区域的bottom
        Rect rect = new Rect();
        //DecorView即为activity的顶级view
        activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(rect);
        //考虑到虚拟导航栏的情况（虚拟导航栏情况下：screenHeight = rect.bottom + 虚拟导航栏高度）
        //选取screenHeight*2/3进行判断
        return screenHeight * 2 / 3 > rect.bottom;
    }

    /**
     * 通过view暴力获取getContext()(Android不支持view.getContext()了)
     *
     * @param view 要获取context的view
     * @return 返回一个activity
     */
    public static Activity getActivity(View view) {
        Activity activity = null;
        if (view.getContext().getClass().getName().contains("com.android.internal.policy.DecorContext")) {
            try {
                Field field = view.getContext().getClass().getDeclaredField("mPhoneWindow");
                field.setAccessible(true);
                Object obj = field.get(view.getContext());
                java.lang.reflect.Method m1 = obj.getClass().getMethod("getContext");
                activity = (Activity) (m1.invoke(obj));
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            activity = (Activity) view.getContext();
        }
        return activity;
    }

    /**
     * 获取通讯录的内容
     *
     * @param uri
     * @return
     */
    public static String[] getPhoneContacts(Uri uri) {
        String[] contact = new String[2];
        try {
            //得到ContentResolver对象
            ContentResolver cr = ContextHolder.getContext().getContentResolver();
            //取得电话本中开始一项的光标
            Cursor cursor = cr.query(uri, null, null, null, null);
            if (cursor != null) {
                cursor.moveToFirst();
                //取得联系人姓名
                int nameFieldColumnIndex = cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME);
                contact[0] = cursor.getString(nameFieldColumnIndex);
                //取得电话号码
                String ContactId = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
                /*Cursor phone = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                        ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "=" + ContactId, null, null);
                if (phone != null) {
                    phone.moveToFirst();
                    contact[1] = phone.getString(phone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                }*/

                // 查看联系人有多少个号码，如果没有号码，返回0
                int phoneCount = cursor
                        .getInt(cursor
                                .getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));
                Cursor phoneCursor;
                if (phoneCount > 0) {
                    // 获得联系人的电话号码列表
                    phoneCursor = ContextHolder.getContext().getContentResolver().query(
                            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                            null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID
                                    + "=" + ContactId, null, null);
                    if (phoneCursor.moveToFirst()) {
                        StringBuffer str = new StringBuffer();
                        do {
                            //遍历所有的联系人下面所有的电话号码
                            String phoneNumber = phoneCursor.getString(phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                            str.append(phoneNumber + ",");
                            //使用Toast技术显示获得的号码
                            //Toast.makeText(context, "联系人电话：" + phoneNumber, Toast.LENGTH_LONG).show();
                        }
                        while (phoneCursor.moveToNext());
                        if (str.toString().length() > 0) {
                            contact[1] = str.toString().substring(0, str.toString().length() - 1);
                        }
                    }
                    phoneCursor.close();
                }
                cursor.close();
            } else {
                return null;
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (Error e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return contact;
    }

    public static void showKeyboard(Context context) {
        ((InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE)).toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
    }
}