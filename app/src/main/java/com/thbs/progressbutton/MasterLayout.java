package com.thbs.progressbutton;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.example.progressframework.R;

public class MasterLayout extends FrameLayout implements OnClickListener {

    private static final String TAG = MasterLayout.class.getSimpleName();
    public CusImage cusView;
    public int pix = 0;
    public RectF rect;

    /**
     * 这个是一开始出现的外边的没填充灰色背景圈
     */
    private ImageView full_circle_image;
    //* 这个被一直当作容器来显示不同的画面，一开始的圈和后面的勾勾
    private ImageView outletCircleButtonImg;
    private ImageView fillCircle;

    //显示勾勾的画面
    private Path tickPath;
    //显示XX的图
    private Path failPath;

    private Bitmap final_show_icon_bmp, first_icon_bmp;
    private Paint stroke_color, fill_color, final_nick_icon_color;
    private Paint fill_fail_color;

    private AnimationSet scaleFadeInAnim;
    private ScaleAnimation new_scale_in, scale_in;
    private AlphaAnimation fade_in;

    int flg_frmwrk_mode = 0;
    int MODE_UN_START = 1;
    int MODE_SUCCESS = 2;
    int MODE_FAIL = 3;

    boolean first_click = false;

    public MasterLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        setOnClickListener(this);

        initialise();
        setPaint();
        setAnimation();
        displayMetrics();
        iconCreate();
        initView();

    }

    public MasterLayout(Context context) {
        super(context);
        setOnClickListener(this);
        setBackgroundColor(Color.WHITE);

        initialise();
        setPaint();
        setAnimation();
        displayMetrics();
        iconCreate();
        initView();


    }


    // 生成界面需要的内容
    private void initialise() {
        cusView = new CusImage(getContext());
        //这个是外环转圈圈的
        outletCircleButtonImg = new ImageView(getContext());
        full_circle_image = new ImageView(getContext());
        //这个是填充背景的
        fillCircle = new ImageView(getContext());

        fillCircle.setClickable(false);
        cusView.setClickable(false);
        outletCircleButtonImg.setClickable(false);
        full_circle_image.setClickable(false);
        cusView.setClickable(false);

        setClickable(true);
    }

    // 设置各种画笔的地方
    private void setPaint() {
        //这个是外圈最细的圈圈的颜色
        stroke_color = new Paint(Paint.ANTI_ALIAS_FLAG);
        stroke_color.setAntiAlias(true);
        stroke_color.setColor(getResources().getColor(R.color.circle_gray));
        stroke_color.setStrokeWidth(3);
        stroke_color.setStyle(Paint.Style.STROKE);

        //这个是成功时显示的勾勾的颜色
        final_nick_icon_color = new Paint(Paint.ANTI_ALIAS_FLAG);
        final_nick_icon_color.setColor(Color.WHITE);
        final_nick_icon_color.setStrokeWidth(12);
        final_nick_icon_color.setStyle(Paint.Style.STROKE);
        final_nick_icon_color.setAntiAlias(true);

        //这个是成功的时候，填充的背景颜色
        fill_color = new Paint(Paint.ANTI_ALIAS_FLAG);
        fill_color.setColor(getResources().getColor(R.color.main_blue));
        fill_color.setStyle(Paint.Style.FILL_AND_STROKE);
        fill_color.setAntiAlias(true);

        //这个是失败的时候，填充的背景颜色
        fill_fail_color = new Paint(Paint.ANTI_ALIAS_FLAG);
        fill_fail_color.setColor(getResources().getColor(R.color.circle_fill_gray));
        fill_fail_color.setStyle(Paint.Style.FILL_AND_STROKE);
        fill_fail_color.setAntiAlias(true);
    }

    // 配置动画
    private void setAnimation() {

        //这个是最后显示那个勾勾时候用到的渐显放大动画
        scaleFadeInAnim = new AnimationSet(true);
        scaleFadeInAnim.setInterpolator(new AccelerateDecelerateInterpolator());
        scale_in = new ScaleAnimation(0.0f, 1.0f, 0.0f, 1.0f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
                0.5f);
        scale_in.setDuration(150);
        fade_in = new AlphaAnimation(0.0f, 1.0f);
        fade_in.setDuration(150);
        scaleFadeInAnim.addAnimation(scale_in);
        scaleFadeInAnim.addAnimation(fade_in);


        //这个是填充圆圈背景的动画
        new_scale_in = new ScaleAnimation(0.0f, 1.0f, 0.0f, 1.0f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
                0.5f);
        new_scale_in.setDuration(200);
        new_scale_in.setAnimationListener(new AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                cusView.setVisibility(View.GONE);
                outletCircleButtonImg.setVisibility(View.VISIBLE);
                outletCircleButtonImg.setImageBitmap(final_show_icon_bmp);
                flg_frmwrk_mode = MODE_SUCCESS;
                outletCircleButtonImg.startAnimation(scaleFadeInAnim);
            }
        });

    }

    private void displayMetrics() {
        // Responsible for calculating the size of views and canvas based upon
        // screen resolution.
        DisplayMetrics metrics = getContext().getResources()
                .getDisplayMetrics();

        int width = metrics.widthPixels;
        int height = metrics.heightPixels;
        float scarea = width * height;
        pix = (int) Math.sqrt(scarea * 0.0217);

    }

    //设置勾勾和xx的地方
    private void iconCreate() {
        tickPath = new Path();
        tickPath.moveTo(pix * 30 / 100, pix * 50 / 100);
        tickPath.lineTo(pix * 45 / 100, pix * 625 / 1000);
        tickPath.lineTo(pix * 65 / 100, pix * 350 / 1000);

        failPath = new Path();
        failPath.moveTo(pix * 30 / 100, pix * 30 / 100);
        failPath.lineTo(pix * 70 / 100, pix * 70 / 100);

        failPath.moveTo(pix * 70 / 100, pix * 30 / 100);
        failPath.lineTo(pix * 30 / 100, pix * 70 / 100);
    }


    // 初始化界面上的各个view
    public void initView() {

        // Defining and drawing bitmaps and assigning views to the layout

        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.WRAP_CONTENT,
                FrameLayout.LayoutParams.WRAP_CONTENT);

        lp.setMargins(10, 10, 10, 10);

        fillCircle.setVisibility(View.GONE);

        Bitmap.Config conf = Bitmap.Config.ARGB_8888;
        Bitmap full_circle_bmp = Bitmap.createBitmap(pix, pix, conf);
//        Bitmap arc_bmp = Bitmap.createBitmap(pix, pix, conf);
        Bitmap fill_circle_bmp = Bitmap.createBitmap(pix, pix, conf);

        first_icon_bmp = Bitmap.createBitmap(pix, pix, conf); // Bitmap to draw


        final_show_icon_bmp = Bitmap.createBitmap(pix, pix, conf); // Bitmap to draw


        Canvas final_nick_icon_canvas = new Canvas(final_show_icon_bmp);
        Canvas fill_circle_canvas = new Canvas(fill_circle_bmp);
        Canvas full_circle_canvas = new Canvas(full_circle_bmp);


        float startX = (float) (pix * 0.05);
        float endX = (float) (pix * 0.95);
        float startY = (float) (pix * 0.05);
        float endY = (float) (pix * 0.95);


        //通过修改这几个数据，可以控制外圆的显示情况。
        rect = new RectF(startX, startY, endX, endY);

        final_nick_icon_canvas.drawPath(tickPath, final_nick_icon_color); // Draw second icon on canvas( Default - Stop ).
        // *****Set your second icon here****


        fill_circle_canvas.drawArc(rect, 0, 360, false, fill_color);

//        rect.inset(2, 2);
        full_circle_canvas.drawArc(rect, 0, 360, false, stroke_color);

        outletCircleButtonImg.setImageBitmap(first_icon_bmp);
        flg_frmwrk_mode = 1;
        fillCircle.setImageBitmap(fill_circle_bmp);
        full_circle_image.setImageBitmap(full_circle_bmp);


        cusView.setVisibility(View.GONE);


        addView(full_circle_image, lp);

        addView(fillCircle, lp);

        addView(outletCircleButtonImg, lp);

        addView(cusView, lp);

    }

    /**
     * 第一次点击时候显示的动画，外面的圈圈在转动
     */
    public void starShow() {
        if (!first_click) {
            if (flg_frmwrk_mode == MODE_UN_START) {
                first_click = true;
                outletCircleButtonImg.setVisibility(View.VISIBLE);
                cusView.setVisibility(View.VISIBLE);
            }
        }
    }

    /**
     * 显示成功时候的界面,这个是通过curView在值达到100时候回调用.
     */
    public void showSuccessAnimation() {
        // Responsible for final fill up starShow
        final_show_icon_bmp = getSuccessBitmap();
        fillCircle.setImageBitmap(getSuccessFillBitmap());
        full_circle_image.setVisibility(View.INVISIBLE);
        fillCircle.setVisibility(View.VISIBLE);
        fillCircle.startAnimation(new_scale_in);
        outletCircleButtonImg.setVisibility(View.GONE);
    }

    public void showFailAnimation() {

        final_show_icon_bmp = getFailBitmap();
        fillCircle.setImageBitmap(getFillFailBitmap());
        fillCircle.setVisibility(View.VISIBLE);
        fillCircle.startAnimation(new_scale_in);
        outletCircleButtonImg.setVisibility(View.GONE);
    }

    private Bitmap getSuccessFillBitmap() {
        Bitmap.Config conf = Bitmap.Config.ARGB_8888;
        Bitmap failCircleBitmap = Bitmap.createBitmap(pix, pix, conf);
        Canvas fill_circle_canvas = new Canvas(failCircleBitmap);
        fill_circle_canvas.drawArc(rect, 0, 360, false, fill_color);
        return failCircleBitmap;
    }

    private Bitmap getSuccessBitmap() {
        Bitmap.Config conf = Bitmap.Config.ARGB_8888;
        final_show_icon_bmp = Bitmap.createBitmap(pix, pix, conf);
        Canvas final_nick_icon_canvas = new Canvas(final_show_icon_bmp);
        final_nick_icon_canvas.drawPath(tickPath, final_nick_icon_color);
        return final_show_icon_bmp;
    }

    private Bitmap getFillFailBitmap() {
        Bitmap.Config conf = Bitmap.Config.ARGB_8888;
        Bitmap failCircleBitmap = Bitmap.createBitmap(pix, pix, conf);
        Canvas fill_circle_canvas = new Canvas(failCircleBitmap);
        fill_circle_canvas.drawArc(rect, 0, 360, false, fill_fail_color);
        return failCircleBitmap;
    }

    private Bitmap getFailBitmap() {
        Bitmap.Config conf = Bitmap.Config.ARGB_8888;
        final_show_icon_bmp = Bitmap.createBitmap(pix, pix, conf);
        Canvas final_nick_icon_canvas = new Canvas(final_show_icon_bmp);
        final_nick_icon_canvas.drawPath(failPath, final_nick_icon_color);
        return final_show_icon_bmp;
    }

    public void reset() {
        // Responsible for resetting the state of view when Stop is clicked
        cusView.reset();
        cusView.setVisibility(View.GONE);
        full_circle_image.setVisibility(View.VISIBLE);
        outletCircleButtonImg.setImageBitmap(first_icon_bmp);
        outletCircleButtonImg.clearAnimation();

        fillCircle.setVisibility(View.INVISIBLE);
        first_click = false;
        flg_frmwrk_mode = MODE_UN_START;
    }

    @Override
    public void onClick(View v) {
        Log.e(TAG, "onClick() called with: " + "v = [" + v + "]");
        starShow();
    }

    public boolean isUnStartModel() {
        return flg_frmwrk_mode == MODE_UN_START;
    }

    public boolean isSuccessFinishModel() {
        return flg_frmwrk_mode == MODE_SUCCESS;
    }

    public boolean isErrorModel() {
        return flg_frmwrk_mode == MODE_FAIL;
    }

    private static final int MAX_PROGRESS = 100;
    private static final int MIN_PROGRESS = 0;

    public void updateProgress(int progress) {
        if (progress < MIN_PROGRESS) {
            progress = MIN_PROGRESS;
        } else if (progress > MAX_PROGRESS) {
            progress = MAX_PROGRESS;
        }
        if (progress == MAX_PROGRESS) {
            showSuccessAnimation();
        }
        cusView.setUpProgress(progress);
    }

    public void showErrorView() {
        flg_frmwrk_mode = MODE_FAIL;
        showFailAnimation();
    }

    public void showSuccessView() {
        flg_frmwrk_mode = MODE_SUCCESS;
        showSuccessAnimation();
    }
}
