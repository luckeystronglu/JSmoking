package com.customview.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.ViewTreeObserver;
import android.widget.ImageView;

import com.yzh.luckeylibrary.R;


/**
 * Created by appadmin on 2017/6/28.
 */

public class NumberImageView extends ImageView implements ViewTreeObserver.OnGlobalLayoutListener{

    private int radiu;
//    private float fl = 0.18f;
    private float fl = 0.3f;
    private String number = "0";

    private Paint paint, txtPaint;

    public NumberImageView(Context context) {
        this(context, null);
    }

    public NumberImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public NumberImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        paint = new Paint();
        paint.setAntiAlias(true);
//        paint.setColor(Color.RED);
//        paint.setColor(Color.parseColor("#f8750a"));
        paint.setColor(ContextCompat.getColor(getContext(), R.color.circle_bg_num));

        txtPaint = new Paint();
        txtPaint.setAntiAlias(true);
        txtPaint.setColor(Color.WHITE);
//        txtPaint.setTextSize(30);
//        txtPaint.setStyle(Paint.Style.STROKE);
//        txtPaint.setStrokeWidth(3);

        /**
         * 在控件加载完成以后调用run方法
         */
        this.post(new Runnable() {
            @Override
            public void run() {
                Log.d("print", "------->post:" + getWidth() + "   " + getHeight());
            }
        });
    }

    /**
     *
     * @param w
     * @param h
     * @param oldw
     * @param oldh
     */
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        Log.d("print", "------->onSizeChanged:" + getWidth() + "   " + getHeight());
    }


    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        //当视图树发生改变的时候，就会回调监听
        getViewTreeObserver().addOnGlobalLayoutListener(this);
    }

    @Override
    public void onGlobalLayout() {
        Log.d("print", "------->onGlobalLayout:" + getWidth() + "   " + getHeight());
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        getViewTreeObserver().removeOnGlobalLayoutListener(this);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        int vWidth = getWidth();
        int vHeight = getHeight();
        radiu = (int) (vWidth * fl);
        txtPaint.setTextSize(vWidth * (1f / 4f));

        Drawable drawable = getDrawable();
        if(drawable != null && drawable instanceof BitmapDrawable){
            //获得ImageView中的位图资源
            Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
            int bWidth = bitmap.getWidth();
            int bHeight = bitmap.getHeight();

            Rect rect1 = new Rect(0, 0, bWidth, bHeight);
            Rect rect2 = new Rect(0, radiu*2/3, vWidth - radiu*2/3, vHeight);
            canvas.drawBitmap(bitmap, rect1, rect2, null);

            if (!number.equals("0")) {
                canvas.drawCircle(vWidth - radiu, radiu, radiu, paint);

                canvas.drawText(number,
                        vWidth - radiu - txtPaint.measureText(number)/2,
                        radiu + (txtPaint.descent() - txtPaint.ascent())/2 - txtPaint.descent(),
                        txtPaint);
            }

        } else {
            super.onDraw(canvas);
        }
    }

    public void setNumber(int number){
        if(number > 99){
            this.number = "99+";
        } else {
            this.number = number + "";
        }
        invalidate();
    }
}
