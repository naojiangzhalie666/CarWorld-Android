package cn.droidlover.xdroid.views.textView;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;

import androidx.appcompat.widget.AppCompatTextView;

/**
 * Author: TinhoXu
 * E-mail: xth@erongdu.com
 * Date: 2016/2/23 19:27
 * <p/>
 * Description: TextView 跑马灯效果
 */
public class MarqueeText extends AppCompatTextView {
    public MarqueeText(Context context) {
        super(context);
    }

    public MarqueeText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MarqueeText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setEllipsize(TextUtils.TruncateAt.MARQUEE);
        setGravity(Gravity.CENTER);
        // marquee_forever
        setMarqueeRepeatLimit(-1);
        setSingleLine(true);
        setMaxLines(1);
    }

    @Override
    public boolean isFocused() {
        return true;
    }
}