package cn.droidlover.xdroid.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.text.Spannable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import cn.droidlover.xdroid.R;
import cn.droidlover.xdroid.tools.utils.DensityUtil;

/**
 * Description: 横条左右控件
 */
public class LeftRightLayout extends RelativeLayout {
    /**
     * 左边文字
     */
    private String leftTxt;
    /**
     * 左边文字颜色
     */
    private int leftColor;
    /**
     * 左边文字大小
     */
    private float leftSize;
    /**
     * 右边文字
     */
    private String rightTxt;
    /**
     * 右边文字颜色
     */
    private int rightColor;
    /**
     * 右边文字大小
     */
    private float rightSize;
    /**
     * 箭头
     */
    private Bitmap arrow;
    /**
     * 设置GroupView的enabled
     */
    private boolean leftRightEnabled;
    /**
     * 设置GroupView的enabled颜色
     */
    private int enabledColor;
    /**
     * 右侧图片ID
     */
    private int arrowRightRes;
    /**
     * 左侧图片ID
     */
    private int iconImgRes;
    private TextView mLeftView;
    private TextView mRightView;
    private ImageView mImageView;
    private ImageView mIconImg;
    private View mView;

    public String getRightTxt() {
        return rightTxt;
    }

    public void setRightTxt(String rightTxt) {
        this.rightTxt = rightTxt;
        setRightText(rightTxt);
    }

    public LeftRightLayout(Context context) {
        this(context, null);
    }

    public LeftRightLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LeftRightLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttr(context, attrs);
    }

    private void initAttr(Context context, AttributeSet attrs) {
        //加载视图的布局
        mView = LayoutInflater.from(context).inflate(R.layout.left_right_layout, this, true);

        TypedArray mTypedArray = context.obtainStyledAttributes(attrs, R.styleable.LeftRightLayout);
        // 获取自定义属性和默认值
        if (mTypedArray != null) {
            iconImgRes = mTypedArray.getResourceId(R.styleable.LeftRightLayout_iconImg, 0);
            leftTxt = mTypedArray.getString(R.styleable.LeftRightLayout_leftTxt);
            leftColor = mTypedArray.getColor(R.styleable.LeftRightLayout_leftTxtColor,
                    ContextCompat.getColor(context, R.color.text_black));
            leftSize = mTypedArray.getDimension(R.styleable.LeftRightLayout_leftTxtSize, DensityUtil.sp2px(context, 14));
            rightTxt = mTypedArray.getString(R.styleable.LeftRightLayout_rightTxt);
            rightColor = mTypedArray.getColor(R.styleable.LeftRightLayout_rightTxtColor,
                    ContextCompat.getColor(context, R.color.text_grey));
            rightSize = mTypedArray.getDimension(R.styleable.LeftRightLayout_rightTxtSize, DensityUtil.sp2px(context, 14));
            arrowRightRes = mTypedArray.getResourceId(R.styleable.LeftRightLayout_arrow, 0);
            leftRightEnabled = mTypedArray.getBoolean(R.styleable.LeftRightLayout_leftRightEnabled, true);
            enabledColor = mTypedArray.getColor(R.styleable.LeftRightLayout_enabledColor,
                    ContextCompat.getColor(context, R.color.text_grey));

            mTypedArray.recycle();
        }
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        // 获取子控件
        mLeftView = (TextView) findViewById(R.id.left_textView);
        mRightView = (TextView) findViewById(R.id.right_textView);
        mImageView = (ImageView) findViewById(R.id.right_arrow);
        mIconImg = (ImageView) findViewById(R.id.icon_img);

        //将从资源文件中加载的属性设置给子控件
        if (!TextUtils.isEmpty(leftTxt)) {
            setLeftText(leftTxt);
        }

        if (!TextUtils.isEmpty(rightTxt)) {
            setRightText(rightTxt);
        }
        setRightTextColor(rightColor);
        setRightTextSize(rightSize);
        setLeftTextColor(leftColor);
        setLeftTextSize(leftSize);

        setPadding(getPaddingLeft(), getPaddingTop(), getPaddingRight(), getPaddingBottom());
        if (arrowRightRes != 0) {
            arrow = BitmapFactory.decodeResource(getResources(), arrowRightRes);
            mImageView.setImageBitmap(arrow);
            mImageView.setVisibility(View.VISIBLE);
        }

        if (iconImgRes != 0) {
            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), iconImgRes);
            mIconImg.setImageBitmap(bitmap);
            mIconImg.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 设置左边Icon
     */
    public void setIconImg(Bitmap bitmap) {
        mIconImg.setImageBitmap(bitmap);
        mIconImg.setVisibility(View.VISIBLE);
    }

    /**
     * 设置左边Icon
     */
    public void setIconImg(Drawable drawable) {
        mIconImg.setImageDrawable(drawable);
        mIconImg.setVisibility(View.VISIBLE);
    }

    /**
     * 设置左边文字
     */
    public void setLeftText(String text) {
        mLeftView.setText(text);
    }

    public String getLeftTxt() {
        return leftTxt;
    }

    /**
     * 设置左边文字
     */
    public void setLeftText(Spannable text) {
        mLeftView.setText(text);
    }

    /**
     * 设置右边文字
     */
    public void setRightText(Spannable text) {
        if (text.length() > 15) {
            String content = text.toString().substring(0, 13) + "\n" + text.toString().substring(13, text.length());
            mRightView.setText(content);
        }else {
            mRightView.setText(text);
        }
    }

    /**
     * 设置左边文字颜色
     */
    public void setLeftTextColor(int color) {
        mLeftView.setTextColor(color);
    }

    /**
     * 设置左边文字大小
     */
    public void setLeftTextSize(float size) {
        mLeftView.setTextSize(TypedValue.COMPLEX_UNIT_PX, size);
    }

    /**
     * 设置左边文字是否可点击
     */
    public void setLeftTxtEnabled(boolean enabled) {
        mLeftView.setEnabled(enabled);
    }

    /**
     * 设置右边文字
     */
    public void setRightText(String text) {
        if (text.length() > 15) {
            text = text.substring(0, 13) + "\n" + text.substring(13,text.length());
        }
        mRightView.setText(text);
    }

    /**
     * 设置右边文字颜色
     */
    public void setRightTextColor(int color) {
        mRightView.setTextColor(color);
    }

    /**
     * 设置右边文字大小
     */
    public void setRightTextSize(float size) {
        mRightView.setTextSize(TypedValue.COMPLEX_UNIT_PX, size);
    }

    /**
     * 设置右边文字是否可点击
     */
    public void setRightTxtEnabled(boolean enabled) {
        mRightView.setEnabled(enabled);
    }

//    @BindingAdapter("leftTxt")
//    public static void leftTxt(View view, String value) {
//        ((LeftRightLayout) view).setLeftText(value);
//    }
//
//    @BindingAdapter("leftTxt")
//    public static void leftTxt(View view, Spannable value) {
//        ((LeftRightLayout) view).setLeftText(value);
//    }
//
//    @BindingAdapter("rightTxt")
//    public static void rightTxt(View view, String value) {
//        ((LeftRightLayout) view).setRightText(value);
//    }
//
//    @BindingAdapter("rightTxt")
//    public static void rightTxt(View view, Spannable value) {
//        ((LeftRightLayout) view).setRightText(value);
//    }
//
//    @BindingAdapter("iconImg")
//    public static void iconImg(View view, @DrawableRes int image) {
//        if (image != 0) {
//            Bitmap img = BitmapFactory.decodeResource(view.getResources(), image);
//            ((LeftRightLayout) view).setIconImg(img);
//        }
//    }
//
//    @BindingAdapter("iconImg")
//    public static void iconImg(View view, Drawable drawable) {
//        if (drawable != null) {
//            ((LeftRightLayout) view).setIconImg(drawable);
//        }
//    }

    public boolean isLeftRightEnabled() {
        return leftRightEnabled;
    }

    public void setLeftRightEnabled(boolean leftRightEnabled) {
        if (false == leftRightEnabled) {
            setLeftTextColor(enabledColor);
            setRightTextColor(enabledColor);
        } else {
            setLeftTextColor(leftColor);
            setRightTextColor(rightColor);
        }
        setEnabled(leftRightEnabled);
        this.leftRightEnabled = leftRightEnabled;
    }
}