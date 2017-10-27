package com.thedream.cz.myndkproject.activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.thedream.cz.myndkproject.R;
import com.thedream.cz.myndkproject.customview.banner.view.CircleImageView;

public class CustomImgAvtivity extends AppCompatActivity {

    private ImageView img1;
    private ImageView img2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_img_avtivity);

        img1 = (ImageView) findViewById(R.id.iv_img1);
        img2 = (CircleImageView) findViewById(R.id.iv_img2);

        img1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                img2.setImageBitmap(BitmapFactory.decodeResource(CustomImgAvtivity.this.getResources(), R.mipmap.one));
            }
        });

        img2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                img2.setImageBitmap(BitmapFactory.decodeResource(CustomImgAvtivity.this.getResources(), R.mipmap.two));
            }
        });
    }

    private Bitmap createBitmap(Bitmap bitmap) {
        Bitmap bt = Bitmap.createBitmap(700, 1200, Bitmap.Config.RGB_565);
        Canvas canvas = new Canvas(bt);
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.BLACK);
        canvas.drawRect(0, 0, 700, 1200, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_OVER));
        Rect rect = new Rect(0, 0, 540, 960);
        Log.i("cz", "width:"+bitmap.getWidth()+",h:"+bitmap.getHeight());
        RectF rectF = new RectF(rect);
        canvas.drawBitmap(bitmap, rect, rectF, paint);
        return bt;
    }
}
