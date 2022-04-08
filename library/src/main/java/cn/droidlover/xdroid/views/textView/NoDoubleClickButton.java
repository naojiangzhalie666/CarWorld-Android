package cn.droidlover.xdroid.views.textView;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.Button;

import androidx.appcompat.widget.AppCompatButton;

/**
 * Author: TinhoXu
 * E-mail: xth@erongdu.com
 * Date: 2016/2/23 19:27
 * <p/>
 * Description: 不允许短时间内点击响应
 */
public class NoDoubleClickButton extends AppCompatButton {
    private long previousTime;

    public NoDoubleClickButton(Context context) {
        super(context);
    }

    public NoDoubleClickButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public NoDoubleClickButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /**
     * @param event
     *         touch事件
     *
     * @return 是否消耗点击事件
     * true - 消耗点击事件
     * false - 不消耗点击事件
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_UP:
                long currentTime = System.currentTimeMillis();
                if (currentTime - previousTime < 1000) {
                    return true;
                }
                previousTime = currentTime;
                break;

            default:
                break;
        }
        return super.onTouchEvent(event);
    }
}