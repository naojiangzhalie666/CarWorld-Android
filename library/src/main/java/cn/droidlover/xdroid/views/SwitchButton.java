package cn.droidlover.xdroid.views;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RadialGradient;
import android.graphics.RectF;
import android.graphics.Shader;
import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateInterpolator;

import cn.droidlover.xdroid.R;


/**
 * Description: 仿IOS SwitchButton
 * http://blog.csdn.net/bfbx5173/article/details/45191147
 */
public class SwitchButton extends View {
    // 主色调
    private final        int                    DEFAULT_COLOR_PRIMARY      = 0xFF4BD763;
    // 主色调 - 暗色
    private final        int                    DEFAULT_COLOR_PRIMARY_DARK = 0xFF3AC652;
    // 圆角
    private final        float                  RATIO_ASPECT               = 0.68f;
    // 动画加速度 (0,1]
    private final        float                  ANIMATION_SPEED            = 0.1f;
    // 已经打开
    private static final int                    STATE_SWITCH_ON            = 4; // you change value you die
    // 准备关闭
    private static final int                    STATE_SWITCH_ON2           = 3;
    // 准备打开
    private static final int                    STATE_SWITCH_OFF2          = 2;
    // 已经关闭
    private static final int                    STATE_SWITCH_OFF           = 1;
    private final        AccelerateInterpolator interpolator               = new AccelerateInterpolator(2);
    private final        Paint                  paint                      = new Paint();
    private final        Path                   sPath                      = new Path();
    private final        Path                   bPath                      = new Path();
    private final        RectF                  bRectF                     = new RectF();
    private float sAnim, bAnim;
    private RadialGradient shadowGradient;
    private int            state;
    private int            lastState;
    private boolean isCanVisibleDrawing = false;
    private OnClickListener mOnClickListener;
    private int             colorPrimary;
    private int             colorPrimaryDark;
    private boolean         hasShadow;
    private boolean         isOpened;
    private int             mWidth, mHeight;
    private int   actuallyDrawingAreaLeft;
    private int   actuallyDrawingAreaRight;
    private int   actuallyDrawingAreaTop;
    private int   actuallyDrawingAreaBottom;
    private float sWidth, sHeight;
    private float sLeft, sTop, sRight, sBottom;
    private float sCenterX, sCenterY;
    private float sScale;
    private float bOffset;
    private float bRadius, bStrokeWidth;
    private float bWidth;
    private float bLeft, bTop, bRight, bBottom;
    private float bOnLeftX, bOn2LeftX, bOff2LeftX, bOffLeftX;
    private float shadowReservedHeight;

    public SwitchButton(Context context) {
        this(context, null);
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public SwitchButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        setLayerType(LAYER_TYPE_SOFTWARE, null);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.SwitchButton);
        colorPrimary = a.getColor(R.styleable.SwitchButton_primaryColor, DEFAULT_COLOR_PRIMARY);
        colorPrimaryDark = a.getColor(R.styleable.SwitchButton_primaryColorDark, DEFAULT_COLOR_PRIMARY_DARK);
        hasShadow = a.getBoolean(R.styleable.SwitchButton_hasShadow, true);
        isOpened = a.getBoolean(R.styleable.SwitchButton_isOpened, false);
        state = isOpened ? STATE_SWITCH_ON : STATE_SWITCH_OFF;
        lastState = state;
        a.recycle();

        if (colorPrimary == DEFAULT_COLOR_PRIMARY && colorPrimaryDark == DEFAULT_COLOR_PRIMARY_DARK) {
            try {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    TypedValue primaryColorTypedValue = new TypedValue();
                    context.getTheme().resolveAttribute(android.R.attr.colorPrimary, primaryColorTypedValue, true);
                    if (primaryColorTypedValue.data > 0) {
                        colorPrimary = primaryColorTypedValue.data;
                    }
                    TypedValue primaryColorDarkTypedValue = new TypedValue();
                    context.getTheme().resolveAttribute(android.R.attr.colorPrimaryDark, primaryColorDarkTypedValue, true);
                    if (primaryColorDarkTypedValue.data > 0) {
                        colorPrimaryDark = primaryColorDarkTypedValue.data;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void setColor(int newColorPrimary, int newColorPrimaryDark) {
        colorPrimary = newColorPrimary;
        colorPrimaryDark = newColorPrimaryDark;
        invalidate();
    }

    public void setShadow(boolean shadow) {
        hasShadow = shadow;
        invalidate();
    }

    public boolean isOpened() {
        return isOpened;
    }

    public void setOpened(boolean isOpened) {
        int wishState = isOpened ? STATE_SWITCH_ON : STATE_SWITCH_OFF;
        if (wishState == state) {
            return;
        }
        refreshState(wishState);
    }

    // 在这里。提供给调用者的公开方法，传入参数isOpened的值就是调用者所希望立马设置的状态。所以这里直接赋值。
    // 不必要也不能等到refreshState[UI刷新的时候]的时候再赋值。
    // 这样在按钮变瘪的时候发生了意外，再次进入也可以显示用户期望的逻辑状态。
    public void toggleSwitch(boolean isOpened) {
        int wishState = isOpened ? STATE_SWITCH_ON : STATE_SWITCH_OFF;
        if (wishState == state) {
            return;
        }
        if ((wishState == STATE_SWITCH_ON && (state == STATE_SWITCH_OFF || state == STATE_SWITCH_OFF2))
                || (wishState == STATE_SWITCH_OFF && (state == STATE_SWITCH_ON || state == STATE_SWITCH_ON2))) {
            sAnim = 1;
        }
        bAnim = 1;
        refreshState(wishState);
    }

    // 刷新UI前都会经过这里，当UI状态确定为STATE_SWITCH_ON或者STATE_SWITCH_OFF的时候更改逻辑状态。
    private void refreshState(int newState) {
        if (!isOpened && newState == STATE_SWITCH_ON) {
            isOpened = true;
        } else if (isOpened && newState == STATE_SWITCH_OFF) {
            isOpened = false;
        }
        lastState = state;
        state = newState;
        postInvalidate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int resultWidth;
        if (widthMode == MeasureSpec.EXACTLY) {
            resultWidth = widthSize;
        } else {
            resultWidth = (int) (56 * getResources().getDisplayMetrics().density + 0.5f)
                    + getPaddingLeft() + getPaddingRight();
            if (widthMode == MeasureSpec.AT_MOST) {
                resultWidth = Math.min(resultWidth, widthSize);
            }
        }

        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int resultHeight;
        if (heightMode == MeasureSpec.EXACTLY) {
            resultHeight = heightSize;
        } else {
            int selfExpectedResultHeight = (int) (resultWidth * RATIO_ASPECT) + getPaddingTop() + getPaddingBottom();
            resultHeight = selfExpectedResultHeight;
            if (heightMode == MeasureSpec.AT_MOST) {
                resultHeight = Math.min(resultHeight, heightSize);
            }
        }
        setMeasuredDimension(resultWidth, resultHeight);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        // 视图自身宽度
        mWidth = w;
        // 视图自身高度
        mHeight = h;
        isCanVisibleDrawing = mWidth > getPaddingLeft() + getPaddingRight() && mHeight > getPaddingTop() + getPaddingBottom();

        if (isCanVisibleDrawing) {
            int actuallyDrawingAreaWidth  = mWidth - getPaddingLeft() - getPaddingRight();
            int actuallyDrawingAreaHeight = mHeight - getPaddingTop() - getPaddingBottom();

            if (actuallyDrawingAreaWidth * RATIO_ASPECT < actuallyDrawingAreaHeight) {
                actuallyDrawingAreaLeft = getPaddingLeft();
                actuallyDrawingAreaRight = mWidth - getPaddingRight();
                int heightExtraSize = (int) (actuallyDrawingAreaHeight - actuallyDrawingAreaWidth * RATIO_ASPECT);
                actuallyDrawingAreaTop = getPaddingTop() + heightExtraSize / 2;
                actuallyDrawingAreaBottom = getHeight() - getPaddingBottom() - heightExtraSize / 2;
            } else {
                int widthExtraSize = (int) (actuallyDrawingAreaWidth - actuallyDrawingAreaHeight / RATIO_ASPECT);
                actuallyDrawingAreaLeft = getPaddingLeft() + widthExtraSize / 2;
                actuallyDrawingAreaRight = getWidth() - getPaddingRight() - widthExtraSize / 2;
                actuallyDrawingAreaTop = getPaddingTop();
                actuallyDrawingAreaBottom = getHeight() - getPaddingBottom();
            }

            shadowReservedHeight = (int) ((actuallyDrawingAreaBottom - actuallyDrawingAreaTop) * 0.09f);
            sLeft = actuallyDrawingAreaLeft;
            sTop = actuallyDrawingAreaTop + shadowReservedHeight;
            sRight = actuallyDrawingAreaRight;
            sBottom = actuallyDrawingAreaBottom - shadowReservedHeight;

            sWidth = sRight - sLeft;
            sHeight = sBottom - sTop;
            sCenterX = (sRight + sLeft) / 2;
            sCenterY = (sBottom + sTop) / 2;

            bLeft = sLeft;
            bTop = sTop;
            bBottom = sBottom;
            bWidth = sBottom - sTop;
            bRight = sLeft + bWidth;
            final float halfHeightOfS = bWidth / 2; // OfB
            bRadius = halfHeightOfS * 0.95f;
            // 有多瘪~  半径的五分之一左右
            bOffset = bRadius * 0.2f; // offset of switching
            bStrokeWidth = (halfHeightOfS - bRadius) * 2;
            // 在已经开启状态下，按钮距离自身左端的距离
            bOnLeftX = sRight - bWidth;
            // 在准备关闭状态下，按钮距离自身左端的距离
            bOn2LeftX = bOnLeftX - bOffset;
            // 在已经关闭状态下，按钮距离自身左端的距离
            bOffLeftX = sLeft;
            // 在准备开启状态下，按钮距离自身左端的距离
            bOff2LeftX = bOffLeftX + bOffset;
            sScale = 1 - bStrokeWidth / sHeight;

            sPath.reset();
            RectF sRectF = new RectF();
            sRectF.top = sTop;
            sRectF.bottom = sBottom;
            sRectF.left = sLeft;
            sRectF.right = sLeft + sHeight;
            sPath.arcTo(sRectF, 90, 180);
            sRectF.left = sRight - sHeight;
            sRectF.right = sRight;
            sPath.arcTo(sRectF, 270, 180);
            sPath.close();

            bRectF.left = bLeft;
            bRectF.right = bRight;
            bRectF.top = bTop + bStrokeWidth / 2;
            bRectF.bottom = bBottom - bStrokeWidth / 2;
            float bCenterX = (bRight + bLeft) / 2;
            float bCenterY = (bBottom + bTop) / 2;

            shadowGradient = new RadialGradient(bCenterX, bCenterY, bRadius, 0xff000000, 0x00000000, Shader.TileMode.CLAMP);
        }
    }

    private void calcBPath(float percent) {
        bPath.reset();
        bRectF.left = bLeft + bStrokeWidth / 2;
        bRectF.right = bRight - bStrokeWidth / 2;
        bPath.arcTo(bRectF, 90, 180);
        bRectF.left = bLeft + percent * bOffset + bStrokeWidth / 2;
        bRectF.right = bRight + percent * bOffset - bStrokeWidth / 2;
        bPath.arcTo(bRectF, 270, 180);
        bPath.close();
    }

    private float calcBTranslate(float percent) {
        float result = 0;
        switch (state - lastState) {
            case 1:
                if (state == STATE_SWITCH_OFF2) {
                    result = bOffLeftX; // off -> off2
                } else if (state == STATE_SWITCH_ON) {
                    result = bOnLeftX - (bOnLeftX - bOn2LeftX) * percent; // on2 -> on
                }
                break;
            case 2:
                if (state == STATE_SWITCH_ON) {
                    result = bOnLeftX - (bOnLeftX - bOffLeftX) * percent; // off2 -> on
                } else if (state == STATE_SWITCH_ON) {
                    result = bOn2LeftX - (bOn2LeftX - bOffLeftX) * percent;  // off -> on2
                }
                break;
            case 3:
                result = bOnLeftX - (bOnLeftX - bOffLeftX) * percent; // off -> on
                break;
            case -1:
                if (state == STATE_SWITCH_ON2) {
                    result = bOn2LeftX + (bOnLeftX - bOn2LeftX) * percent; // on -> on2
                } else if (state == STATE_SWITCH_OFF) {
                    result = bOffLeftX;  // off2 -> off
                }
                break;
            case -2:
                if (state == STATE_SWITCH_OFF) {
                    result = bOffLeftX + (bOn2LeftX - bOffLeftX) * percent;  // on2 -> off
                } else if (state == STATE_SWITCH_OFF2) {
                    result = bOff2LeftX + (bOnLeftX - bOff2LeftX) * percent;  // on -> off2
                }
                break;
            case -3:
                result = bOffLeftX + (bOnLeftX - bOffLeftX) * percent;  // on -> off
                break;
            default: // init
            case 0:
                if (state == STATE_SWITCH_OFF) {
                    result = bOffLeftX; //  off -> off
                } else if (state == STATE_SWITCH_ON) {
                    result = bOnLeftX; // on -> on
                }
                break;
        }
        return result - bOffLeftX;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (!isCanVisibleDrawing)
            return;

        paint.setAntiAlias(true);
        final boolean isOn = (state == STATE_SWITCH_ON || state == STATE_SWITCH_ON2);
        // Draw background
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(isOn ? colorPrimary : 0xffE3E3E3);
        canvas.drawPath(sPath, paint);

        sAnim = sAnim - ANIMATION_SPEED > 0 ? sAnim - ANIMATION_SPEED : 0;
        bAnim = bAnim - ANIMATION_SPEED > 0 ? bAnim - ANIMATION_SPEED : 0;

        final float dsAnim = interpolator.getInterpolation(sAnim);
        final float dbAnim = interpolator.getInterpolation(bAnim);
        // Draw background animation
        final float scale       = sScale * (isOn ? dsAnim : 1 - dsAnim);
        final float scaleOffset = (sRight - sCenterX - bRadius) * (isOn ? 1 - dsAnim : dsAnim);
        canvas.save();
        canvas.scale(scale, scale, sCenterX + scaleOffset, sCenterY);
        paint.setColor(0xFFFFFFFF);
        canvas.drawPath(sPath, paint);
        canvas.restore();
        // To prepare center bar path
        canvas.save();
        canvas.translate(calcBTranslate(dbAnim), shadowReservedHeight);
        final boolean isState2 = (state == STATE_SWITCH_ON2 || state == STATE_SWITCH_OFF2);
        calcBPath(isState2 ? 1 - dbAnim : dbAnim);
        // Use center bar path to draw shadow
        if (hasShadow) {
            paint.setStyle(Paint.Style.FILL);
            paint.setColor(0xFF333333);
            paint.setShader(shadowGradient);
            canvas.drawPath(bPath, paint);
            paint.setShader(null);
        }
        canvas.translate(0, -shadowReservedHeight);
        // draw bar
        canvas.scale(0.98f, 0.98f, bWidth / 2, bWidth / 2);
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(0xffffffff);
        canvas.drawPath(bPath, paint);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(bStrokeWidth * 0.5f);
        paint.setColor(isOn ? colorPrimaryDark : 0xFFBFBFBF);
        canvas.drawPath(bPath, paint);
        canvas.restore();

        paint.reset();
        if (sAnim > 0 || bAnim > 0)
            invalidate();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if ((state == STATE_SWITCH_ON || state == STATE_SWITCH_OFF) && (sAnim * bAnim == 0)) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    return true;

                case MotionEvent.ACTION_UP:
                    lastState = state;

                    bAnim = 1;
                    if (state == STATE_SWITCH_OFF) {
                        refreshState(STATE_SWITCH_OFF2);
                        listener.toggleToOn(this);
                    } else if (state == STATE_SWITCH_ON) {
                        refreshState(STATE_SWITCH_ON2);
                        listener.toggleToOff(this);
                    }

                    if (mOnClickListener != null) {
                        mOnClickListener.onClick(this);
                    }
                    break;
            }
        }
        return super.onTouchEvent(event);
    }

    @Override
    public void setOnClickListener(OnClickListener l) {
        super.setOnClickListener(l);
        mOnClickListener = l;
    }

    public interface OnStateChangedListener {
        void toggleToOn(SwitchButton view);

        void toggleToOff(SwitchButton view);
    }

    private OnStateChangedListener listener = new OnStateChangedListener() {
        @Override
        public void toggleToOn(SwitchButton view) {
            toggleSwitch(true);
        }

        @Override
        public void toggleToOff(SwitchButton view) {
            toggleSwitch(false);
        }
    };

    public void setOnStateChangedListener(OnStateChangedListener listener) {
        if (listener == null)
            throw new IllegalArgumentException("empty listener");
        this.listener = listener;
    }

    @Override
    public Parcelable onSaveInstanceState() {
        Parcelable superState = super.onSaveInstanceState();
        SavedState ss         = new SavedState(superState);
        ss.isOpened = isOpened;
        return ss;
    }

    @Override
    public void onRestoreInstanceState(Parcelable state) {
        SavedState ss = (SavedState) state;
        super.onRestoreInstanceState(ss.getSuperState());
        this.isOpened = ss.isOpened;
        this.state = this.isOpened ? STATE_SWITCH_ON : STATE_SWITCH_OFF;
        invalidate();
    }

    @SuppressLint("ParcelCreator")
    static final class SavedState extends BaseSavedState {
        private boolean isOpened;

        SavedState(Parcelable superState) {
            super(superState);
        }

        private SavedState(Parcel in) {
            super(in);
            isOpened = 1 == in.readInt();
        }

        @Override
        public void writeToParcel(Parcel out, int flags) {
            super.writeToParcel(out, flags);
            out.writeInt(isOpened ? 1 : 0);
        }
    }
}