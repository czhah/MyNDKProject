package com.thedream.cz.myndkproject.widget.banner.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Created by chenzhuang on 2017/10/23.
 * Describe:
 */

public class CircleImageView extends ImageView {

    private boolean isCircle = true;
    private float mWidth;
    private float mHeight;
    private Paint paint;

    public CircleImageView(Context context) {
        this(context, null);
    }

    public CircleImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CircleImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if(isCircle) {
            mWidth = Math.min(getMeasuredWidth(), getMeasuredHeight());
            setMeasuredDimension((int) mWidth, (int)mWidth);
        }
    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = w;
        mHeight = h;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Drawable drawable = getDrawable();
        if(drawable == null) return ;
        Bitmap bitmap = Bitmap.createBitmap((int) mWidth, (int) mHeight, Bitmap.Config.RGB_565);
        if(bitmap == null) return ;

        Matrix matrix = new Matrix();
        float scale;
        float dx = 0, dy = 0;
        float dwidth = drawable.getIntrinsicWidth();
        float dheight = drawable.getIntrinsicHeight();
        if (dwidth * mHeight > mWidth * dheight) {
            scale = mHeight / dheight;
            dx = (mWidth - dwidth * scale) * 0.5f;
        } else {
            scale = mWidth / dwidth;
            dy = (mHeight - dheight * scale) * 0.5f;
        }

        matrix.setScale(scale, scale);
        matrix.postTranslate(Math.round(dx), Math.round(dy));

        Canvas c = new Canvas(bitmap);
        c.concat(matrix);
        drawable.draw(c);

        if(isCircle) {
            int layer = canvas.saveLayer(0, 0, canvas.getWidth(), canvas.getHeight(), paint);
            canvas.drawCircle(mWidth /2, mHeight / 2, mHeight / 2, paint);
            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
            canvas.drawBitmap(bitmap, 0, 0, paint);
            paint.setXfermode(null);
            canvas.restoreToCount(layer);
        }else {
            canvas.drawBitmap(bitmap, 0, 0, paint);
        }

    }
}
