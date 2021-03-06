package com.liansheng.carworld.activity.home;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Environment;
import android.os.Vibrator;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.liansheng.carworld.R;
import com.liansheng.carworld.activity.BaseActivity;
import com.liansheng.carworld.bean.IdCard;
import com.liansheng.carworld.bean.ScanVinBean;
import com.liansheng.carworld.wxapi.RecognizeService;
import com.shouzhong.scanner.Callback;
import com.shouzhong.scanner.IScanner;
import com.shouzhong.scanner.IViewFinder;
import com.shouzhong.scanner.NV21;
import com.shouzhong.scanner.Result;
import com.shouzhong.scanner.ScannerUtils;
import com.shouzhong.scanner.ScannerView;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.droidlover.xdroid.tools.utils.BitmapUtil;
import cn.droidlover.xdroid.tools.utils.FileUtil;
import cn.droidlover.xdroid.tools.utils.Util;
import pub.devrel.easypermissions.EasyPermissions;

public class ScanVinActivity extends BaseActivity {

    @BindView(R.id.btn_back)
    ImageView btnBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.sv)
    ScannerView scannerView;
    @BindView(R.id.btn_go)
    Button btnGo;
    @BindView(R.id.et_id)
    EditText etId;
    @BindView(R.id.et_name)
    EditText etName;
    String[] permissions = new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
    public final static int PER_CARMERA = 0X001;
    private Vibrator vibrator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        tvTitle.setText("??????VIN");
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        initView();
    }

    private void initView() {
        if (!EasyPermissions.hasPermissions(this, permissions)) {
            EasyPermissions.requestPermissions(this, "????????????????????????????????????????????????????????????????????????????????????.",
                    PER_CARMERA, permissions);
        }
        scannerView.setViewFinder(new ViewFinder(this));
//        scannerView.setSaveBmp(true);
//        scannerView.setEnableIdCard(true);
//        scannerView.setEnableZXing(true);
//        scannerView.setEnableLicensePlate(true);
//        scannerView.setRotateDegree90Recognition(true);
        scannerView.setShouldAdjustFocusArea(true);
        scannerView.setCallback(new Callback() {
            @Override
            public void result(Result result) {
                if (result.toString().contains("cardNumber")) {
                    startVibrator();
                    IdCard id = new Gson().fromJson(result.data, IdCard.class);
                    etId.setText(id.getCardNumber());
                    etName.setText(id.getName());
                    scannerView.restartPreviewAfterDelay(3000);
                } else {
                    showToast("????????????");
                }
            }
        });
        scannerView.setScanner(new IScanner() {
            @Override
            public Result scan(byte[] data, int width, int height) throws Exception {
                Bitmap bitmap = new NV21(context).nv21ToBitmap(data, width, height);
                if (bitmap != null) {
//                    BitmapUtil.saveMyBitmap(bitmap, "vin")
//                    String path = Environment.getExternalStorageDirectory() + "/DCIM/Screenshots/123.jpg";
//                    if (new File(path).exists()) {
                        String s = ScannerUtils.decodeText(BitmapUtil.saveMyBitmap(bitmap, "vin"));
                        Util.toast(s);
//                    }
//                    RecognizeService.recognizeVincode(context, BitmapUtil.saveMyBitmap(bitmap, "vin"),
//                            result -> {
//                                if (!TextUtils.isEmpty(result)) {
//                                    ScanVinBean vinBean = new Gson().fromJson(result, ScanVinBean.class);
//                                    if (vinBean != null && vinBean.getWords_result() != null && vinBean.getWords_result().size() > 0
//                                            && !TextUtils.isEmpty(vinBean.getWords_result().get(0).getWords())) {
//                                        Util.toast(vinBean.getWords_result().get(0).getWords());
//                                    }
//                                }
//                            });
                }
                return null;
            }
        });
    }

    @OnClick(R.id.btn_go)
    public void onClick() {
        Intent intent = new Intent();
        intent.putExtra("id", etId.getText().toString());
        intent.putExtra("name", etName.getText().toString());
        setResult(Activity.RESULT_OK, intent);
        finish();
    }

    class ViewFinder extends View implements IViewFinder {
        private Rect framingRect;//?????????????????????
        private float widthRatio = 0.9f;//??????????????????view??????????????????
        private float heightRatio = 0.8f;
        private float heightWidthRatio = 0.5626f;//?????????????????????
        private int leftOffset = -1;//?????????????????????????????????????????????????????????????????????????????????
        private int topOffset = -1;//?????????????????????????????????????????????????????????????????????????????????
        private int laserColor = getColor(R.color.order);// ???????????????
        private int maskColor = 0x60000000;// ????????????
        private int borderColor = getColor(R.color.order);// ????????????
        private int borderStrokeWidth = 10;// ????????????
        private int borderLineLength = 72;// ????????????
        private Paint laserPaint;// ?????????
        private Paint maskPaint;// ??????????????????
        private Paint borderPaint;// ????????????
        private int position;

        public ViewFinder(Context context) {
            super(context);
            setWillNotDraw(false);//??????????????????
            laserPaint = new Paint();
            laserPaint.setColor(laserColor);
            laserPaint.setStyle(Paint.Style.FILL);
            maskPaint = new Paint();
            maskPaint.setColor(maskColor);
            borderPaint = new Paint();
            borderPaint.setColor(borderColor);
            borderPaint.setStyle(Paint.Style.STROKE);
            borderPaint.setStrokeWidth(borderStrokeWidth);
            borderPaint.setAntiAlias(true);
        }

        @Override
        protected void onSizeChanged(int xNew, int yNew, int xOld, int yOld) {
            updateFramingRect();
        }

        @Override
        public void onDraw(Canvas canvas) {
            if (getFramingRect() == null) {
                return;
            }
            drawViewFinderMask(canvas);
            drawViewFinderBorder(canvas);
            drawLaser(canvas);
        }

        private void drawLaser(Canvas canvas) {
            Rect framingRect = getFramingRect();
            int top = framingRect.top + 10 + position;
            canvas.drawRect(framingRect.left + 10, top, framingRect.right - 10, top + 5, laserPaint);
            position = framingRect.bottom - framingRect.top - 25 < position ? 0 : position + 2;
            //????????????
            postInvalidateDelayed(20, framingRect.left + 10, framingRect.top + 10, framingRect.right - 10, framingRect.bottom - 10);
        }

        /**
         * ????????????????????????????????????
         */
        private void drawViewFinderMask(Canvas canvas) {
            int width = canvas.getWidth();
            int height = canvas.getHeight();
            Rect framingRect = getFramingRect();
            canvas.drawRect(0, 0, width, framingRect.top, maskPaint);//?????????????????????
            canvas.drawRect(0, framingRect.top, framingRect.left, framingRect.bottom, maskPaint);//?????????????????????
            canvas.drawRect(framingRect.right, framingRect.top, width, framingRect.bottom, maskPaint);//?????????????????????
            canvas.drawRect(0, framingRect.bottom, width, height, maskPaint);//?????????????????????
        }

        /**
         * ????????????????????????
         */
        private void drawViewFinderBorder(Canvas canvas) {
            Rect framingRect = getFramingRect();
            // Top-left corner
            Path path = new Path();
            path.moveTo(framingRect.left, framingRect.top + borderLineLength);
            path.lineTo(framingRect.left, framingRect.top);
            path.lineTo(framingRect.left + borderLineLength, framingRect.top);
            canvas.drawPath(path, borderPaint);
            // Top-right corner
            path.moveTo(framingRect.right, framingRect.top + borderLineLength);
            path.lineTo(framingRect.right, framingRect.top);
            path.lineTo(framingRect.right - borderLineLength, framingRect.top);
            canvas.drawPath(path, borderPaint);
            // Bottom-right corner
            path.moveTo(framingRect.right, framingRect.bottom - borderLineLength);
            path.lineTo(framingRect.right, framingRect.bottom);
            path.lineTo(framingRect.right - borderLineLength, framingRect.bottom);
            canvas.drawPath(path, borderPaint);
            // Bottom-left corner
            path.moveTo(framingRect.left, framingRect.bottom - borderLineLength);
            path.lineTo(framingRect.left, framingRect.bottom);
            path.lineTo(framingRect.left + borderLineLength, framingRect.bottom);
            canvas.drawPath(path, borderPaint);
        }

        /**
         * ??????framingRect????????????????????????????????????
         */
        private synchronized void updateFramingRect() {
            Point viewSize = new Point(getWidth(), getHeight());
            int width = (int) (getWidth() * widthRatio);
//            int height = (int) (heightWidthRatio * width);
            int height = (int) (0.1 * width);
            int left, top;
            if (leftOffset < 0) {
                left = (viewSize.x - width) / 2;//????????????
            } else {
                left = leftOffset;
            }
            if (topOffset < 0) {
                top = (viewSize.y - height) / 2;//????????????
            } else {
                top = topOffset;
            }
            framingRect = new Rect(left, top, left + width, top + height);
        }

        @Override
        public Rect getFramingRect() {
            return framingRect;
        }
    }

    class ViewFinder2 implements IViewFinder {
        @Override
        public Rect getFramingRect() {
            return new Rect(240, 240, 840, 840);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        scannerView.onPause();
    }

    @Override
    protected void onDestroy() {
        if (vibrator != null) {
            vibrator.cancel();
            vibrator = null;
        }
        super.onDestroy();
    }

    @Override
    public void initData(Bundle savedInstanceState) {

    }

    @Override
    protected void onResume() {
        super.onResume();
        scannerView.onResume();
    }

    private void startVibrator() {
        if (vibrator == null)
            vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.vibrate(3000);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_scan_vin;
    }//????????????
}
